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
package ssp.dbpf.types;

import java.awt.image.BufferedImage;

import ssp.dbpf.converter.types.PNGConverter;
import ssp.dbpf.tgi.TGIKeys;

/**
 * Defines a PNG of DBPF.<br>
 * 
 * The PNG is a open image format and the data stored as BufferedImage.<br>
 * The decompressedSize is updated with setImage. Although the image might be a
 * compressed format, it could be compressed with QFS.
 * 
 * @author Stefan Wertich, mapsonswen@web.de
 * @version 2.0.0, 04.12.2012
 * 
 */
public class DBPFPNG extends AbstractDBPFType {

	private BufferedImage image;

	/**
	 * Constructor.<br>
	 */
	public DBPFPNG() {
		image = null;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString());
		if (image != null) {
			sb.append(", ");
			sb.append("Image-Size: " + image.getWidth() + "x"
					+ image.getHeight());
		}
		return sb.toString();
	}

	/**
	 * Sets the image of the PNG.<br>
	 * 
	 * @param image
	 *            The image
	 */
	public void setImage(BufferedImage image) {
		this.image = image;
		if (image != null) {
			setDecompressedSize(PNGConverter.calcDataLength(image));
		}
	}

	/**
	 * Returns the imageData of the PNG.<br>
	 * 
	 * @return The data
	 */
	public BufferedImage getImage() {
		return image;
	}

	@Override
	public int getType() {
		return TGIKeys.PNG.getFormatID();
	}
}
