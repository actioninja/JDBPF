package ssp.dbpf4j.io;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.logging.Level;
import java.util.logging.Logger;

import ssp.dbpf4j.DBPFFile;
import ssp.dbpf4j.entries.DBPFEntry;
import ssp.dbpf4j.util.DBPFUtil;

/**
 * Reads the DBPF format.<br>
 * 
 * @author Stefan Wertich
 * @version 1.5.0, 26.08.2010
 * 
 */
public class DBPFReader {

	/**
	 * Constructor.<br>
	 */
	public DBPFReader() {
	}

	/**
	 * Checks only the fileType of the file, if it is DBPF.<br>
	 * 
	 * @param filename
	 *            The filename
	 * @return TRUE, if file is a DBPF file; FALSE, if not or error occur
	 */
	public boolean checkFileType(File filename) {
		try {
			RandomAccessFile raf = new RandomAccessFile(filename, "r");
			String fileType = readChars(raf, 4);
			if (fileType.equals(DBPFUtil.MAGICNUMBER_DBPF)) {
				return true;
			}
		} catch (FileNotFoundException e) {
			Logger.getLogger(DBPFUtil.LOGGER_NAME).log(Level.SEVERE,
					"[DBPFReader] File not found: " + filename, e);
		} catch (IOException e) {
			Logger.getLogger(DBPFUtil.LOGGER_NAME).log(Level.SEVERE,
					"[DBPFReader] IOException for file: " + filename, e);
		}
		return false;
	}

	/**
	 * Reads a DBPF file.<br>
	 * 
	 * @param filename
	 *            The filename of the DBPF file
	 * @return The file object or NULL, if file not found or error
	 */
	public DBPFFile read(File filename) {
		// to store the entries of the file
		DBPFFile dbpfFile = new DBPFFile();

		RandomAccessFile raf = null;

		try {
			raf = new RandomAccessFile(filename, "r");

			// if file can be opened, set the filename
			dbpfFile.setFilename(filename);

			// Analyze the fileType
			String fileType = readChars(raf, 4);
			if (fileType.equals(DBPFUtil.MAGICNUMBER_DBPF)) {
				long majorVersion = readUint32(raf, 4);
				long minorVersion = readUint32(raf, 4);
				raf.skipBytes(12);
				long dateCreated = readUint32(raf, 4);
				long dateModified = readUint32(raf, 4);
				@SuppressWarnings("unused")
				long indexType = readUint32(raf, 4);
				long indexEntryCount = readUint32(raf, 4);
				long indexOffsetLocation = readUint32(raf, 4);
				long indexSize = readUint32(raf, 4);

				dbpfFile.setMajorVersion(majorVersion);
				dbpfFile.setMinorVersion(minorVersion);
				dbpfFile.setDateCreated(dateCreated);
				dbpfFile.setDateModified(dateModified);
				dbpfFile.setIndexEntryCount(indexEntryCount);
				dbpfFile.setIndexOffsetLocation(indexOffsetLocation);
				dbpfFile.setIndexSize(indexSize);

				// Read the index
				raf.seek(indexOffsetLocation);
				for (int i = 0; i < indexEntryCount; i++) {
					long tid = readUint32(raf, 4);
					long gid = readUint32(raf, 4);
					long iid = readUint32(raf, 4);
					long offset = readUint32(raf, 4);
					long size = readUint32(raf, 4);
					DBPFEntry entry = new DBPFEntry(tid, gid, iid);
					entry.setOffset(offset);
					entry.setSize(size);
					entry.setFilename(filename);
					dbpfFile.getEntryList().addElement(entry);

					// System.out.println(entry.toString());
				}
			} else {
				dbpfFile = null;
			}
		} catch (FileNotFoundException e) {
			Logger.getLogger(DBPFUtil.LOGGER_NAME).log(Level.SEVERE,
					"[DBPFReader] File not found: " + filename, e);
			dbpfFile = null;
		} catch (IOException e) {
			Logger.getLogger(DBPFUtil.LOGGER_NAME).log(Level.SEVERE,
					"[DBPFReader] IOException for file: " + filename, e);
			dbpfFile = null;
		} finally {
			try {
				raf.close();
			} catch (IOException e) {
				Logger.getLogger(DBPFUtil.LOGGER_NAME).log(Level.SEVERE,
						"[DBPFReader] IOException for file: " + filename, e);
			}
		}
		return dbpfFile;
	}

	/**
	 * Reads chars from stream till length reached.<br>
	 * 
	 * @param raf
	 *            The stream
	 * @param length
	 *            The length
	 * @return The readed chars as a string
	 * @throws IOException
	 *             Thrown, if error occur
	 */
	public String readChars(RandomAccessFile raf, int length)
			throws IOException {
		StringBuffer sb = new StringBuffer();
		int readed = 0;
		for (int i = 0; i < length; i++) {
			readed = raf.read();
			if (readed != -1) {
				sb.append((char) readed);
			}
		}
		return sb.toString();
	}

	/**
	 * Reads a UINT32 value from the stream till length reached.<br>
	 * 
	 * @param raf
	 *            The stream
	 * @param length
	 *            The length
	 * @return The uint32 as a long value (cause of signed JAVA values)
	 * @throws IOException
	 *             Thrown, if error occur
	 */
	public long readUint32(RandomAccessFile raf, int length) throws IOException {
		long sum = 0;
		for (int i = 0; i < length; i++) {
			int readed = raf.read();
			sum += (readed * Math.pow(16 * 16, i));
		}
		return sum;
	}

	/**
	 * Reads the data from the file for the given entry.<br>
	 * Opens the file, seek to offset, read and close the file when finished.
	 * The data might be compressed! Uses RandomAccessFile for reading.
	 * 
	 * @param entry
	 *            The entry
	 * @return The readed rawData, length might be zero, if error
	 */
	public static short[] readData(DBPFEntry entry) {
		short[] data = new short[(int) entry.getSize()];
		File filename = entry.getFilename();
		try {
			RandomAccessFile raf = new RandomAccessFile(filename, "r");
			raf.seek(entry.getOffset());
			for (int i = 0; i < data.length; i++) {
				data[i] = (short) raf.read();
			}
			raf.close();
		} catch (FileNotFoundException e) {
			Logger.getLogger(DBPFUtil.LOGGER_NAME).log(Level.SEVERE,
					"[DBPFReader] File not found: " + filename, e);
		} catch (IOException e) {
			Logger.getLogger(DBPFUtil.LOGGER_NAME).log(Level.SEVERE,
					"[DBPFReader] IOException for file: " + filename, e);
		}
		return data;
	}

	/**
	 * Reads the rawData from the file.<br>
	 * Opens the file, read and close the file when finished.
	 * 
	 * Uses BufferedInputstream with FileInputStream for reading.
	 * 
	 * @param filename
	 *            The filename
	 * @return The readed rawData, length might be zero, if error
	 */
	public static short[] readRawData(File filename) {
		short[] rawData = new short[(int) filename.length()];
		try {
			FileInputStream fis = new FileInputStream(filename);
			BufferedInputStream bis = new BufferedInputStream(fis);
			for (int i = 0; i < rawData.length; i++) {
				rawData[i] = (short) bis.read();
			}
			bis.close();
		} catch (FileNotFoundException e) {
			Logger.getLogger(DBPFUtil.LOGGER_NAME).log(Level.SEVERE,
					"[DBPFReader] File not found: " + filename, e);
		} catch (IOException e) {
			Logger.getLogger(DBPFUtil.LOGGER_NAME).log(Level.SEVERE,
					"[DBPFReader] IOException for file: " + filename, e);
		}
		return rawData;
	}
}
