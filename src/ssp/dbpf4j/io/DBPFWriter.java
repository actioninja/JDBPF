package ssp.dbpf4j.io;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;
import java.util.logging.Level;

import ssp.dbpf4j.DBPFContainer;
import ssp.dbpf4j.entries.DBPFEntry;
import ssp.dbpf4j.format.DBPFCoder;
import ssp.dbpf4j.format.DBPFConverter;
import ssp.dbpf4j.tgi.TGIKey;
import ssp.dbpf4j.types.DBPFLText;
import ssp.dbpf4j.types.DBPFLUA;
import ssp.dbpf4j.types.DBPFType;
import ssp.dbpf4j.util.DBPFConstant;
import ssp.dbpf4j.util.DBPFUtil;

/**
 * Writes the DBPF format.<br>
 * 
 * @author Stefan Wertich
 * @version 1.6.0, 07.01.2011
 * 
 */
public class DBPFWriter {

	/**
	 * Constructor.<br>
	 * 
	 * PRIVATE to prevent instance.
	 */
	private DBPFWriter() {
	}

	/**
	 * Writes a list with DBPFType to a DBPF file.<br>
	 * 
	 * Updates first the directory and if necessary adds a directory to the
	 * writelist.
	 * 
	 * @param filename
	 *            The filename of the DBPF file
	 * @param writeList
	 *            The list of DBPFType to write to file
	 * @return TRUE, if successful written; FALSE, otherwise
	 */
	public static boolean write(File filename, List<DBPFType> writeList) {

		// Updates the directory of the writeList
		DBPFUpdater.updateDirectory(writeList);

		RandomAccessFile raf = null;
		try {
			raf = new RandomAccessFile(filename, "rw");

			// create necessary file data
			String fileType = DBPFConstant.MAGICNUMBER_DBPF;
			long majorVersion = 1;
			long minorVersion = 0;
			long dateCreated = System.currentTimeMillis() / 1000;
			long dateModified = System.currentTimeMillis() / 1000;
			long indexType = 7;
			long indexEntryCount = writeList.size();
			long indexOffsetLocation = DBPFConstant.HEADERSIZE_DBPF;
			long indexSize = 5 * 4 * indexEntryCount;

			// set minimum file size
			long count = indexOffsetLocation + indexSize;
			raf.setLength(count);

			// Write header
			writeChars(raf, fileType);
			writeUINT32(raf, majorVersion, 4);
			writeUINT32(raf, minorVersion, 4);
			writeUINT32(raf, 0, 12);
			writeUINT32(raf, dateCreated, 4);
			writeUINT32(raf, dateModified, 4);
			writeUINT32(raf, indexType, 4);
			writeUINT32(raf, indexEntryCount, 4);
			writeUINT32(raf, indexOffsetLocation, 4);
			writeUINT32(raf, indexSize, 4);
			writeUINT32(raf, 0, 48);

			// Write rawData, remember offset position and store length
			long[] offsetList = new long[writeList.size()];
			long[] sizeList = new long[writeList.size()];
			for (int i = 0; i < writeList.size(); i++) {
				short[] data = DBPFConverter.createData(writeList.get(i));
				offsetList[i] = raf.getFilePointer();
				sizeList[i] = data.length;
				indexOffsetLocation += sizeList[i];
				for (int j = 0; j < data.length; j++) {
					writeUINT32(raf, data[j], 1);
				}
			}

			// Write index
			for (int i = 0; i < writeList.size(); i++) {
				TGIKey tgiKey = writeList.get(i).getTGIKey();
				writeUINT32(raf, tgiKey.getTID(), 4);
				writeUINT32(raf, tgiKey.getGID(), 4);
				writeUINT32(raf, tgiKey.getIID(), 4);
				writeUINT32(raf, offsetList[i], 4);
				writeUINT32(raf, sizeList[i], 4);
			}

			// Update index location
			raf.seek(40);
			writeUINT32(raf, indexOffsetLocation, 4);

			raf.close();
		} catch (FileNotFoundException e) {
			DBPFUtil.toLog("DBPFWriter", Level.SEVERE, e.getMessage());
			return false;
		} catch (IOException e) {
			DBPFUtil.toLog("DBPFWriter", Level.SEVERE, e.getMessage());
			return false;
		} finally {
			try {
				raf.close();
			} catch (IOException e) {
				DBPFUtil.toLog("DBPFWriter", Level.SEVERE, e.getMessage());
			}
		}
		return true;
	}

	/**
	 * Write a string to stream till length reached.<br>
	 * 
	 * @param raf
	 *            The stream
	 * @param s
	 *            The string to write
	 * @throws IOException
	 *             Thown, if error occur
	 */
	public static void writeChars(RandomAccessFile raf, String s)
			throws IOException {
		for (int i = 0; i < s.length(); i++) {
			raf.write(s.charAt(i));
		}
	}

	/**
	 * Write a UINT32 value to the stream with the given size.<br>
	 * 
	 * e.g. The value 1230626450 is 0x4959DE92 and will - with size 4 - be
	 * written as 92 DE 59 49 to stream.
	 * 
	 * @param raf
	 *            The stream
	 * @param value
	 *            The value to write
	 * @param size
	 *            The size of bytes
	 * @throws IOException
	 *             If error occur
	 */
	public static void writeUINT32(RandomAccessFile raf, long value, int size)
			throws IOException {
		for (int i = 0; i < size; i++) {
			long rest = value % 256;
			raf.write((int) rest);
			value = value / 256;
		}

	}

	/**
	 * Writes data to a file for the given entry.<br>
	 * Opens (create) the file, seek to offset, write and close the file when
	 * finished. Be sure the offset is set correctly of the entry to prevent
	 * overriding!
	 * 
	 * @param entry
	 *            The DBPFEntry
	 * @param data
	 *            The rawData to write
	 * @return TRUE, if successfull written; FALSE, otherwise
	 */
	public static boolean writeData(DBPFEntry entry, short[] data) {
		RandomAccessFile raf = null;
		File filename = entry.getFilename();
		try {
			if (!filename.exists()) {
				filename.createNewFile();
			}
			raf = new RandomAccessFile(filename, "rw");
			raf.seek(entry.getOffset());
			for (int i = 0; i < data.length; i++) {
				raf.write(data[i]);
			}
			raf.close();
			return true;
		} catch (FileNotFoundException e) {
			DBPFUtil.toLog("DBPFWriter", Level.SEVERE, e.getMessage());
		} catch (IOException e) {
			DBPFUtil.toLog("DBPFWriter", Level.SEVERE, e.getMessage());
		}
		return false;
	}

	/**
	 * Writes rawData to the file.<br>
	 * Opens (create) the file, write and close the file when finished. Uses
	 * BufferedOutputStream with FileOutputStream for writing.
	 * 
	 * @param filename
	 *            The filename
	 * @param rawData
	 *            The rawData to write
	 * @return TRUE, if successfull written; FALSE, otherwise
	 */
	public static boolean writeRawData(File filename, short[] rawData) {
		try {
			if (!filename.exists()) {
				filename.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(filename);
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			for (int i = 0; i < rawData.length; i++) {
				bos.write(rawData[i]);
			}
			bos.close();
			return true;
		} catch (FileNotFoundException e) {
			DBPFUtil.toLog("DBPFWriter", Level.SEVERE, e.getMessage());
		} catch (IOException e) {
			DBPFUtil.toLog("DBPFWriter", Level.SEVERE, e.getMessage());
		}
		return false;
	}

	// ***********************************************************************
	// DBPFContainer
	// ***********************************************************************

	/**
	 * Writes the container to file.<br>
	 * 
	 * @param container
	 *            The DBPFContainer
	 * @return TRUE, if written; FALSE, if error
	 */
	public static boolean writeContainer(DBPFContainer container) {
		if (container != null) {
			return DBPFWriter.write(container.getFilename(),
					container.getTypeList());
		}
		return false;
	}

	// ***********************************************************************
	// SPECIAL
	// ***********************************************************************

	/**
	 * Saves the data to a text file.<br>
	 * The data can be saved as simple ASCII or UNICODE text. For ASCII it uses
	 * DBPFLUA, for UNICODE it uses DBPFLTEXT.
	 * 
	 * @param filename
	 *            The filename
	 * @param data
	 *            The data
	 * @param unicode
	 *            TRUE, if saving as UNICODE; FALSE, for ASCII
	 */
	public static void writeText(File filename, List<String> data,
			boolean unicode) {

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < data.size(); i++) {
			sb.append(data.get(i));
			sb.append(DBPFUtil.CRLF);
		}
		String dataText = sb.toString();

		short[] rawData = new short[0];
		if (unicode) {
			DBPFLText text = new DBPFLText();
			text.setCompressed(false);
			text.setString(dataText);
			rawData = DBPFCoder.createLTextData(text);
		} else {
			DBPFLUA text = new DBPFLUA();
			text.setCompressed(false);
			text.setString(dataText);
			rawData = DBPFCoder.createLUAData(text);
		}
		DBPFWriter.writeRawData(filename, rawData);

	}

	/**
	 * Saves the TGI file with the given TGI.<br>
	 * 
	 * @param filename
	 *            The filename to save to
	 * @param tgiKey
	 *            The TGIKey
	 */
	public static void writeTGI(File filename, TGIKey tgiKey) {
		FileWriter fw;
		try {
			fw = new FileWriter(filename);
			fw.write(DBPFUtil.toHex(tgiKey.getTID(), 8) + DBPFUtil.CRLF
					+ DBPFUtil.toHex(tgiKey.getGID(), 8) + DBPFUtil.CRLF
					+ DBPFUtil.toHex(tgiKey.getIID(), 8) + DBPFUtil.CRLF);
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
