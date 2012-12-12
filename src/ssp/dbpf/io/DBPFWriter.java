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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;
import java.util.logging.Level;

import ssp.dbpf.DBPFCollection;
import ssp.dbpf.converter.DBPFConverter;
import ssp.dbpf.converter.types.LTextConverter;
import ssp.dbpf.converter.types.LUAConverter;
import ssp.dbpf.entries.DBPFEntry;
import ssp.dbpf.event.DBPFException;
import ssp.dbpf.tgi.TGIKey;
import ssp.dbpf.types.DBPFLText;
import ssp.dbpf.types.DBPFLUA;
import ssp.dbpf.types.DBPFType;
import ssp.dbpf.util.DBPFConstant;
import ssp.dbpf.util.DBPFLogger;
import ssp.dbpf.util.DBPFUtil;

/**
 * Writes the DBPF format.<br>
 * 
 * @author Stefan Wertich, mapsonswen@web.de
 * @version 2.0.0, 09.12.2012
 * 
 */
public class DBPFWriter {

	private static final String LOGNAME = DBPFWriter.class.getSimpleName();

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
	 * @throws DBPFException
	 *             Thrown, if any error occur
	 */
	public static void write(File filename, List<DBPFType> writeList)
			throws DBPFException {

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
			throw new DBPFException(LOGNAME, e.getMessage());
		} catch (IOException e) {
			throw new DBPFException(LOGNAME, e.getMessage());
		} finally {
			try {
				raf.close();
			} catch (IOException e) {
				DBPFLogger.toLog(LOGNAME, Level.WARNING, e.getMessage());
			}
		}
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
	 * @throws DBPFException
	 *             Thrown, if any error occur
	 */
	public static void writeData(DBPFEntry entry, short[] data)
			throws DBPFException {
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
		} catch (FileNotFoundException e) {
			throw new DBPFException(LOGNAME, e.getMessage());
		} catch (IOException e) {
			throw new DBPFException(LOGNAME, e.getMessage());
		}
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
	 * @throws DBPFException
	 *             Thrown, if any error occur
	 */
	public static void writeRawData(File filename, short[] rawData)
			throws DBPFException {
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
		} catch (FileNotFoundException e) {
			throw new DBPFException(LOGNAME, e.getMessage());
		} catch (IOException e) {
			throw new DBPFException(LOGNAME, e.getMessage());
		}
	}

	// ***********************************************************************
	// DBPFCollection
	// ***********************************************************************

	/**
	 * Writes the collection to a file.<br>
	 * 
	 * @param collection
	 *            The DBPFCollection
	 * @return TRUE, if written; FALSE, if collection is NULL
	 * @throws DBPFException
	 */
	public static boolean writeCollection(DBPFCollection collection)
			throws DBPFException {
		if (collection != null) {
			DBPFWriter
					.write(collection.getFilename(), collection.getTypeList());
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
	 * @throws DBPFException
	 *             Thrown, if any error occur
	 */
	public static void writeText(File filename, List<String> data,
			boolean unicode) throws DBPFException {

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < data.size(); i++) {
			sb.append(data.get(i));
			sb.append(DBPFConstant.CRLF);
		}
		String dataText = sb.toString();

		short[] rawData = new short[0];
		if (unicode) {
			DBPFLText text = new DBPFLText();
			text.setCompressed(false);
			text.setString(dataText);
			rawData = LTextConverter.createData(text);
		} else {
			DBPFLUA text = new DBPFLUA();
			text.setCompressed(false);
			text.setString(dataText);
			rawData = LUAConverter.createData(text);
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
	 * @throws DBPFException
	 *             Thrown, if any error occur
	 */
	public static void writeTGI(File filename, TGIKey tgiKey)
			throws DBPFException {
		FileWriter fw;
		try {
			fw = new FileWriter(filename);
			fw.write(DBPFUtil.toHex(tgiKey.getTID(), 8) + DBPFConstant.CRLF
					+ DBPFUtil.toHex(tgiKey.getGID(), 8) + DBPFConstant.CRLF
					+ DBPFUtil.toHex(tgiKey.getIID(), 8) + DBPFConstant.CRLF);
			fw.flush();
			fw.close();
		} catch (IOException e) {
			throw new DBPFException(LOGNAME, e.getMessage());
		}
	}
}
