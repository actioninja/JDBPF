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
package ssp.dbpf.converter.types;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;

import javax.imageio.ImageIO;

import ssp.dbpf.converter.DBPFPackager;
import ssp.dbpf.event.DBPFException;
import ssp.dbpf.io.ShortArrayInputStream;
import ssp.dbpf.io.ShortArrayOutputStream;
import ssp.dbpf.types.DBPFPNG;
import ssp.dbpf.util.DBPFConstant;
import ssp.dbpf.util.DBPFLogger;
import ssp.dbpf.util.DBPFUtil;

/**
 * Converts between BufferedImage and short array.<br>
 * 
 * @author Stefan Wertich, mapsonswen@web.de
 * @version 2.0.0, 09.12.2012
 * 
 */
public class PNGConverter {

	private static final String LOGNAME = PNGConverter.class.getSimpleName();

	/**
	 * Create an image from the given imageData.<br>
	 * <ul>
	 * <li>If the imageData is encoded this will decode the data automatically.</li>
	 * <li>If the imageData is null or has a length of zero this will throw an
	 * exception.</li>
	 * </ul>
	 * 
	 * @param imageData
	 *            The imageData, may be encoded
	 * @return The image
	 * @throws DBPFException
	 *             Thrown, if type cannot be created
	 */
	public static DBPFPNG createType(short[] imageData) throws DBPFException {
		DBPFPNG type = new DBPFPNG();

		BufferedImage bim = null;
		// ImageData exists: Try to read the image
		if (imageData != null && imageData.length != 0) {
			ShortArrayInputStream vis = new ShortArrayInputStream(
					getCheckedData(imageData));
			try {
				bim = ImageIO.read(vis);
			} catch (IOException e) {
				throw new DBPFException(LOGNAME, "Cannot decode imageData: "
						+ Arrays.toString(imageData) + ", ERROR: "
						+ e.getMessage());
			}
		}
		if (bim == null) {
			throw new DBPFException(LOGNAME, "Cannot decode imageData: "
					+ Arrays.toString(imageData));
		}
		type.setImage(bim);
		return type;
	}

	/**
	 * This will return the checked imageData.<br>
	 * If the imageData corresponds to PNG image it will return the data as it
	 * is, else it will try to decode the data and return the decoded data. <br>
	 * If the imageData is NULL or has length of zero, it returns an empty short
	 * array.
	 * 
	 * @param imageData
	 *            The imageData
	 * @return The data as it is or decoded
	 */
	private static short[] getCheckedData(short[] imageData) {
		if (imageData != null && imageData.length != 0) {
			String magic = DBPFUtil.getChars(imageData, 0, 4);
			// Uncompressed data ([0]=0x89,[1]=P,[2]=N,[3]=G): OK
			if (magic.equals(DBPFConstant.MAGICNUMBER_PNG)) {
				return imageData;
			}
			// Encoded data: Need to be decoded!
			String message = "Encoded data for png image found! "
					+ "Try to decode (length=" + imageData.length + ") ...";
			DBPFLogger.toLog(LOGNAME, Level.WARNING, message);
			DBPFPackager packager = new DBPFPackager();
			return packager.decompress(imageData);
		}
		return new short[0];
	}

	/**
	 * Create the imageData from the given DBPFPNG.<br>
	 * Using the {@link ShortArrayOutputStream ShortArrayOutputStream}.
	 * 
	 * @param png
	 *            The type
	 * @return The imageData
	 */
	public static short[] createData(DBPFPNG png) {
		int dataLength = calcDataLength(png.getImage());
		ShortArrayOutputStream vos = new ShortArrayOutputStream(dataLength);
		try {
			ImageIO.write(png.getImage(), "png", vos);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return vos.toShortArray();
	}

	/**
	 * Returns the data length for the given image after calculating.<br>
	 * 
	 * @param bim
	 *            The image
	 * @return The length in bytes
	 */
	public static int calcDataLength(BufferedImage bim) {
		int numberOfBands = bim.getSampleModel().getNumBands();
		int dataLength = bim.getWidth() * bim.getHeight() * numberOfBands;
		return dataLength;
	}
}
