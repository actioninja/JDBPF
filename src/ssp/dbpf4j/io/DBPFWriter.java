package ssp.dbpf4j.io;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Vector;

import ssp.dbpf4j.DBPFFile;
import ssp.dbpf4j.entries.DBPFEntry;
import ssp.dbpf4j.types.DBPFType;
import ssp.dbpf4j.util.DBPFConverter;
import ssp.dbpf4j.util.DBPFUtil;

/**
 * Writes the DBPF format.<br>
 * 
 * @author Stefan Wertich
 * @version 1.3.1, 20.01.2010
 * 
 */
public class DBPFWriter {

	/**
	 * Constructor.<br>
	 */
	public DBPFWriter() {
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
	public boolean write(File filename, Vector<DBPFType> writeList) {

		// Updates the directory of the writeList
		DBPFConverter.updateDirectory(writeList);

		RandomAccessFile raf = null;
		try {
			raf = new RandomAccessFile(filename, "rw");

			// create necessary file data
			String fileType = DBPFUtil.MAGICNUMBER_DBPF;
			long majorVersion = 1;
			long minorVersion = 0;
			long dateCreated = System.currentTimeMillis() / 1000;
			long dateModified = System.currentTimeMillis() / 1000;
			long indexType = 7;
			long indexEntryCount = writeList.size();
			long indexOffsetLocation = DBPFFile.HEADER_SIZE;
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
				long[] tgi = writeList.get(i).getTGI();
				writeUINT32(raf, tgi[0], 4);
				writeUINT32(raf, tgi[1], 4);
				writeUINT32(raf, tgi[2], 4);
				writeUINT32(raf, offsetList[i], 4);
				writeUINT32(raf, sizeList[i], 4);
			}

			// Update index location
			raf.seek(40);
			writeUINT32(raf, indexOffsetLocation, 4);

			raf.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
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
	public void writeChars(RandomAccessFile raf, String s) throws IOException {
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
	public void writeUINT32(RandomAccessFile raf, long value, int size)
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
		try {
			File filename = entry.getFilename();
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
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
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
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
}
