/*
 * Copyright (c) 2012 by Stefan Wertich.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 * 
 * This code is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this code.  If not, see <http://www.gnu.org/licenses/>.
 */
package ssp.dbpf.io;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.logging.Level;

import ssp.dbpf.DBPFCollection;
import ssp.dbpf.DBPFContainer;
import ssp.dbpf.converter.DBPFConverter;
import ssp.dbpf.entries.DBPFEntry;
import ssp.dbpf.event.DBPFException;
import ssp.dbpf.tgi.TGIKey;
import ssp.dbpf.util.DBPFConstant;
import ssp.dbpf.util.DBPFLogger;

/**
 * Reads the DBPF format.<br>
 * 
 * @author Stefan Wertich, mapsonswen@web.de
 * @version 2.0.0, 09.12.2012
 * 
 */
public class DBPFReader {

	private static final String LOGNAME = DBPFReader.class.getSimpleName();

	/**
	 * Constructor.<br>
	 * 
	 * PRIVATE to prevent instance.
	 */
	private DBPFReader() {
	}

	/**
	 * Checks only the fileType of the file, if it is DBPF.<br>
	 * 
	 * @param filename
	 *            The filename
	 * @return TRUE, if file is a DBPF file; FALSE, if not
	 * @throws DBPFException
	 *             Thrown, if file not found or I/O error
	 */
	public static boolean checkFileType(File filename) throws DBPFException {
		try {
			RandomAccessFile raf = new RandomAccessFile(filename, "r");
			String fileType = readChars(raf, 4);
			raf.close();
			if (fileType.equals(DBPFConstant.MAGICNUMBER_DBPF)) {
				return true;
			}
		} catch (FileNotFoundException e) {
			throw new DBPFException(LOGNAME, e.getMessage());
		} catch (IOException e) {
			throw new DBPFException(LOGNAME, e.getMessage());
		}
		return false;
	}

	/**
	 * Reads a DBPF file.<br>
	 * 
	 * @param filename
	 *            The filename of the DBPF file
	 * @return The container or NULL, if file no DBPF file
	 * @throws DBPFException
	 *             Thrown, if file not found or I/O error
	 */
	public static DBPFContainer read(File filename) throws DBPFException {
		// to store the entries of the file
		DBPFContainer container = new DBPFContainer();

		RandomAccessFile raf = null;

		try {
			raf = new RandomAccessFile(filename, "r");

			// if file can be opened, set the filename
			container.setFilename(filename);

			// Analyze the fileType
			String fileType = readChars(raf, 4);
			if (fileType.equals(DBPFConstant.MAGICNUMBER_DBPF)) {
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

				container.setMajorVersion(majorVersion);
				container.setMinorVersion(minorVersion);
				container.setDateCreated(dateCreated);
				container.setDateModified(dateModified);
				container.setIndexEntryCount(indexEntryCount);
				container.setIndexOffsetLocation(indexOffsetLocation);
				container.setIndexSize(indexSize);

				// Read the index
				raf.seek(indexOffsetLocation);
				for (int i = 0; i < indexEntryCount; i++) {
					long tid = readUint32(raf, 4);
					long gid = readUint32(raf, 4);
					long iid = readUint32(raf, 4);
					long offset = readUint32(raf, 4);
					long size = readUint32(raf, 4);
					DBPFEntry entry = new DBPFEntry(new TGIKey(tid, gid, iid));
					entry.setOffset(offset);
					entry.setSize(size);
					entry.setFilename(filename);
					container.addEntry(entry);

					// System.out.println(entry.toString());
				}
			} else {
				container = null;
			}
		} catch (FileNotFoundException e) {
			throw new DBPFException(LOGNAME, e.getMessage());
		} catch (IOException e) {
			throw new DBPFException(LOGNAME, e.getMessage());
		} finally {
			if (raf != null) {
				try {
					raf.close();
				} catch (IOException e) {
					DBPFLogger.toLog(LOGNAME, Level.WARNING, e.getMessage());
				}
			}
		}
		return container;
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
	public static String readChars(RandomAccessFile raf, int length)
			throws IOException {
		StringBuilder sb = new StringBuilder();
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
	public static long readUint32(RandomAccessFile raf, int length)
			throws IOException {
		long sum = 0;
		for (int i = 0; i < length; i++) {
			int readed = raf.read();
			sum += (readed * Math.pow(16 * 16, i));
		}
		return sum;
	}

	/**
	 * Reads the data from the file for the given entry.<br>
	 * Opens the file, maps the entry content to memory, read and close the file
	 * when finished. The data might be compressed! Uses RandomAccessFile with
	 * MappedByteBuffer for reading.
	 * 
	 * @param entry
	 *            The entry
	 * @return The read rawData, might be empty
	 * @throws DBPFException
	 *             Thrown, if file not found or I/O error
	 */
	public static short[] readData(DBPFEntry entry) throws DBPFException {
		File filename = entry.getFilename();
		short[] data = new short[(int) entry.getSize()];
		try {
			RandomAccessFile raf = new RandomAccessFile(filename, "r");
			
			// old version with simple seek
//			raf.seek(entry.getOffset());
//			for (int i = 0; i < data.length; i++) {
//				data[i] = (short) raf.read();
//			}
			
			// new version with memory map from SC4Devotion: CasperVg
			FileChannel fc = raf.getChannel();
			MappedByteBuffer mbb = fc.map(FileChannel.MapMode.READ_ONLY,
					entry.getOffset(), entry.getSize());
			// System.out.println("Capacity of Buffer: "+mbb.capacity());
			for (int i = 0; i < data.length; i++) {
				// The get functions deliver signed byte, so this has to be
				// converted to unsigned short
				data[i] = (short) (mbb.get() & 0xff);
			}
			raf.close();
		} catch (FileNotFoundException e) {
			throw new DBPFException(LOGNAME, e.getMessage());
		} catch (IOException e) {
			throw new DBPFException(LOGNAME, e.getMessage());
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
	 * @return The read rawData, might be empty
	 * @throws DBPFException
	 *             Thrown, if file not found or I/O error
	 */
	public static short[] readRawData(File filename) throws DBPFException {
		short[] rawData = new short[(int) filename.length()];
		try {
			FileInputStream fis = new FileInputStream(filename);
			BufferedInputStream bis = new BufferedInputStream(fis);
			for (int i = 0; i < rawData.length; i++) {
				rawData[i] = (short) bis.read();
			}
			bis.close();
		} catch (FileNotFoundException e) {
			throw new DBPFException(LOGNAME, e.getMessage());
		} catch (IOException e) {
			throw new DBPFException(LOGNAME, e.getMessage());
		}
		return rawData;
	}

	// ***********************************************************************
	// DBPFCollection
	// ***********************************************************************

	/**
	 * Reads the entrys and stores them in a collection.<br>
	 * 
	 * @param filename
	 *            The filename
	 * @return The collection
	 * @throws DBPFException
	 *             Thrown, if error while reading
	 */
	public static DBPFCollection readCollection(File filename)
			throws DBPFException {
		DBPFContainer dbpfFile = DBPFReader.read(filename);

		DBPFCollection collection = new DBPFCollection();
		collection.setFilename(filename);
		for (DBPFEntry entry : dbpfFile.getEntryList()) {
			collection.addType(DBPFConverter.createType(entry));
		}
		return collection;
	}
}
