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
package ssp.dbpf.fsh;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.logging.Level;

import ssp.dbpf.event.DBPFException;
import ssp.dbpf.fsh.dxt.DXTCoder;
import ssp.dbpf.fsh.dxt.DXTException;
import ssp.dbpf.fsh.dxt.DXTImage;
import ssp.dbpf.util.DBPFConstant;
import ssp.dbpf.util.DBPFLogger;
import ssp.dbpf.util.DBPFUtil;

/**
 * Conversation between FSHContainer and Short Array<.br>
 * 
 * Uses the DXTCoder for decode/encode of image data.<br>
 * 
 * @author Stefan Wertich, mapsonswen@web.de
 * @version 2.0.0, 09.12.2012
 * 
 */
public class FSHHandler {

	private static final String LOGNAME = FSHHandler.class.getSimpleName();

	// **************************************************************
	// DBPFFSH/short[] to FSHContainer
	// **************************************************************

	/**
	 * Creates the FSHContainer from the given data.<br>
	 * 
	 * @param data
	 *            The data
	 * @return The FSHContainer
	 * @throws DBPFException
	 *             Thrown, if data cannot be decoded
	 */
	public static FSHContainer createContainer(short[] data)
			throws DBPFException {

		String magicNumber = DBPFUtil.getChars(data, 0, 4);
		if (!magicNumber.equals(DBPFConstant.MAGICNUMBER_FSH)) {
			String message = "No FSH/SHPI identifier found: " + magicNumber;
			throw new DBPFException(LOGNAME, message);
		}

		long fileSize = DBPFUtil.getUint32(data, 4, 4);
		int numberOfEntries = (int) DBPFUtil.getUint32(data, 8, 4);

		String directoryID = DBPFUtil.getChars(data, 12, 4);
		FSHContainer container = new FSHContainer(
				FSHDirectoryID.forID(directoryID));

		int offset = DBPFConstant.HEADERSIZE_FSH;
		int offsetEntrySize = 0;

		for (int i = 0; i < numberOfEntries; i++) {
			String entryName = DBPFUtil.getChars(data, offset, 4);
			int offsetEntry = (int) DBPFUtil.getUint32(data, offset + 0x04, 4);
			FSHEntry entry;
			try {
				entry = decodeEntry(data, entryName, offsetEntry);
				container.addEntry(entry);
				offset += DBPFConstant.HEADERSIZE_FSH_DIR;
				offsetEntrySize += (DBPFConstant.HEADERSIZE_FSH_ENTRY + entry
						.getEntrySize());
			} catch (DXTException e) {
				throw new DBPFException(LOGNAME, e.getMessage());
			}
		}
		offset += offsetEntrySize;

		if (offset != fileSize) {
			DBPFLogger.toLog(LOGNAME, Level.WARNING,
					"Data array may contain additional data: " + "Offset: "
							+ offset + ", FileSize: " + fileSize);
		}

		return container;
	}

	/**
	 * Decodes a FSHEntry from the data.<br>
	 * 
	 * @param data
	 *            The data
	 * @param entryName
	 *            The entryName of the entry
	 * @param offset
	 *            The offset to start
	 * @return The decoded FSHEntry
	 * @throws DXTException
	 */
	private static FSHEntry decodeEntry(short[] data, String entryName,
			int offset) throws DXTException {
		// long recordID = DBPFUtil.getUint32(data, offset + 0x00, 0x01);
		// long blockSize = DBPFUtil.getUint32(data, offset + 0x01, 0x03);
		// Create the typeCode from recordID and blockSize & 0x7F
		int typeCode = (int) DBPFUtil.getUint32(data, offset + 0x00, 0x04) & 0x7F;
		FSHEntryType entryType = FSHEntryType.forTypeCode(typeCode);

		int width = (int) DBPFUtil.getUint32(data, offset + 0x04, 0x02);
		int height = (int) DBPFUtil.getUint32(data, offset + 0x06, 0x02);
		int centerX = (int) DBPFUtil.getUint32(data, offset + 0x08, 0x02);
		int centerY = (int) DBPFUtil.getUint32(data, offset + 0x0A, 0x02);
		Point center = new Point(centerX, centerY);

		int posX = (int) DBPFUtil.getUint32(data, offset + 0x0C, 0x02);
		int posY = (int) DBPFUtil.getUint32(data, offset + 0x0E, 0x02);
		Point position = new Point(posX, posY);

		offset += DBPFConstant.HEADERSIZE_FSH_ENTRY;

		if (entryType != FSHEntryType.DXT1 && entryType != FSHEntryType.DXT3) {
			DBPFLogger
					.toLog(LOGNAME, Level.WARNING,
							"Only DXT compression is supported for now! Skip decoding of entry.");
			DXTImage dxtImage = new DXTImage(width, height, false);
			return new FSHEntry(entryName, entryType, center, position,
					dxtImage);
		} else {
			boolean hasAlpha = (entryType == FSHEntryType.DXT3);
			DXTImage dxtImage = new DXTImage(width, height, hasAlpha);
			DXTCoder coder = new DXTCoder();
			coder.decode(dxtImage, data, offset);
			return new FSHEntry(entryName, entryType, center, position,
					dxtImage);
		}
	}

	// **************************************************************
	// FSHContainer to short[]
	// **************************************************************

	/**
	 * Creates the Container data for the container.<br>
	 * 
	 * @param container
	 *            The container
	 * @return The data
	 * @throws DBPFException
	 *             Thrown, if data cannot be created
	 */
	public static short[] createData(FSHContainer container)
			throws DBPFException {

		// Calculate the resulting filesize
		List<FSHEntry> entryList = container.getEntryList();
		int numberOfEntries = entryList.size();
		int fileSize = DBPFConstant.HEADERSIZE_FSH
				+ numberOfEntries
				* (DBPFConstant.HEADERSIZE_FSH_DIR + DBPFConstant.HEADERSIZE_FSH_ENTRY);
		for (int i = 0; i < numberOfEntries; i++) {
			FSHEntry entry = entryList.get(i);
			fileSize += entry.getEntrySize();
		}
		short data[] = new short[fileSize];

		DBPFUtil.setChars(DBPFConstant.MAGICNUMBER_FSH, data, 0);
		DBPFUtil.setUint32(fileSize, data, 4, 2);
		DBPFUtil.setUint32(numberOfEntries, data, 8, 4);
		DBPFUtil.setChars(container.getDirectoryID().getID(), data, 12);

		// The offset for the directory writing
		int offsetDir = DBPFConstant.HEADERSIZE_FSH;
		// The offset for the entry writing
		int offsetEntry = DBPFConstant.HEADERSIZE_FSH + numberOfEntries
				* DBPFConstant.HEADERSIZE_FSH_DIR;

		for (FSHEntry fshContainerEntry : entryList) {
			DBPFUtil.setChars(fshContainerEntry.getEntryName(), data, offsetDir);
			DBPFUtil.setUint32(offsetEntry, data, offsetDir + 4, 4);
			offsetDir += DBPFConstant.HEADERSIZE_FSH_DIR;
			try {
				offsetEntry = encodeEntry(fshContainerEntry, data, offsetEntry);
			} catch (DXTException e) {
				throw new DBPFException(LOGNAME, e.getMessage());
			}
		}
		return data;
	}

	/**
	 * Encodes the data from the given entry.<br>
	 * 
	 * @param entry
	 *            The entry
	 * @param data
	 *            The data
	 * @param offset
	 *            The offset
	 * @return The offset after encoding
	 * @throws DXTException
	 */
	private static int encodeEntry(FSHEntry entry, short[] data, int offset)
			throws DXTException {
		DXTImage dxtImage = entry.getDxtImage();
		BufferedImage imageColor = dxtImage.getColor();
		FSHEntryType entryType;
		if (dxtImage.isAlpha()) {
			entryType = FSHEntryType.DXT3;
		} else {
			entryType = FSHEntryType.DXT1;
		}
		int xsize = imageColor.getWidth();
		int ysize = imageColor.getHeight();
		DBPFUtil.setUint32(entryType.getTypeCode(), data, offset, 4);
		DBPFUtil.setUint32(xsize, data, offset + 4, 2);
		DBPFUtil.setUint32(ysize, data, offset + 6, 2);
		DBPFUtil.setUint32(0, data, offset + 8, 8);
		offset += DBPFConstant.HEADERSIZE_FSH_ENTRY;

		DXTCoder coder = new DXTCoder();
		offset = coder.encode(dxtImage, data, offset);
		return offset;
	}
}
