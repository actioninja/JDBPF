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

import ssp.dbpf.fsh.dxt.DXTImage;

/**
 * Defines an entry in a FSHContainer.<br>
 * 
 * @author Stefan Wertich, mapsonswen@web.de
 * @version 2.0.0, 14.08.2012
 * 
 */
public class FSHEntry {

	// !pal = Global palette (8-bit indexed color)
	// 0000 = Buildings, props, network intersection, terrain textures
	// rail = Rail textures
	// TB2, TB3 = Sprite animations
	// FISH
	// 89c5
	// BMP1 = Daeley's FSHImageIO
	private String entryName;

	private FSHEntryType type;
	private Point center;
	private Point position;
	private DXTImage dxtImage;

	/**
	 * Constructor.<br>
	 * 
	 * @param entryName
	 *            The name of the entry
	 * @param type
	 *            The type of the entry
	 * @param center
	 *            The center of image or for image to spin around (max: 65535)
	 * @param position
	 *            The position to display image from left/top
	 * @param dxtImage
	 *            The container with color and alpha image
	 */
	public FSHEntry(String entryName, FSHEntryType type,
			Point center, Point position, DXTImage dxtImage) {
		this.entryName = entryName;
		this.type = type;
		this.center = center;
		this.position = position;
		this.dxtImage = dxtImage;
	}

	/**
	 * Returns the size for this entry.<br>
	 * 
	 * @return The entrySize
	 */
	public int getEntrySize() {
		return dxtImage.getWidth() * dxtImage.getHeight()
				/ type.getPixelPerByte();
	}

	/**
	 * @return the type
	 */
	public FSHEntryType getType() {
		return type;
	}

	/**
	 * @return the center
	 */
	public Point getCenter() {
		return center;
	}

	/**
	 * @return the position
	 */
	public Point getPosition() {
		return position;
	}

	/**
	 * @return the dxtImage
	 */
	public DXTImage getDxtImage() {
		return dxtImage;
	}

	/**
	 * @return the entryName
	 */
	public String getEntryName() {
		return entryName;
	}

}
