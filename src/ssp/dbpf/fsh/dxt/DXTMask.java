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
package ssp.dbpf.fsh.dxt;

/**
 * Defines some masks for DXT.<br>
 * 
 * @author Stefan Wertich, mapsonswen@web.de
 * @version 2.0.0, 14.08.2012
 * 
 */
public enum DXTMask {

	/**
	 * For Color (4 byte): Alpha canal 8 bit
	 */
	ALPHA_4BYTE(0xFF000000, 24),
	/**
	 * For Color (4 byte): Red canal 8 bit
	 */
	RED_4BYTE(0x00FF0000, 16),
	/**
	 * For Color (4 byte): Green canal 8 bit
	 */
	GREEN_4BYTE(0x0000FF00, 8),
	/**
	 * For Color (4 byte): Blue canal 8 bit
	 */
	BLUE_4BYTE(0x000000FF, 0),
	
	/**
	 * For Color (2 byte - 565): Red canal 5 bit
	 */
	RED_2BYTE(Integer.parseInt("1111100000000000", 2), 11),
	/**
	 * For Color (2 byte - 565): Green canal 6 bit
	 */
	GREEN_2BYTE(Integer.parseInt("0000011111100000", 2), 5),
	/**
	 * For Color (2 byte - 565): Blue canal 5 bit
	 */
	BLUE_2BYTE(Integer.parseInt("0000000000011111", 2), 0),
	
	/**
	 * For Color (1 byte): Alpha canal 2 bit
	 */
	ALPHA_1BYTE(Integer.parseInt("11000000", 2), 6),
	/**
	 * For Color (1 byte): Red canal 2 bit
	 */
	RED_1BYTE(Integer.parseInt("00110000", 2), 4),
	/**
	 * For Color (1 byte): Green canal 2 bit
	 */
	GREEN_1BYTE(Integer.parseInt("00001100", 2), 2),
	/**
	 * For Color (1 byte): Blue canal 2 bit
	 */
	BLUE_1BYTE(Integer.parseInt("00000011", 2), 0),
	
	/**
	 * The Highest 4 bit
	 */
	HIGHBITS4(0xF0,4), //Integer.parseInt("11110000", 2), 4),
	/**
	 * The Lowest 4 bit
	 */
	LOWBITS4(0x0F,0); //Integer.parseInt("00001111", 2), 0);

	private int mask;
	private int shift;

	/**
	 * Constructor.<br>
	 * 
	 * @param mask
	 *            The mask
	 * @param shift
	 *            The shift for the first significant bit
	 */
	DXTMask(int mask, int shift) {
		this.mask = mask;
		this.shift = shift;
	}

	/**
	 * Returns the mask.<br>
	 * 
	 * @return The mask
	 */
	public int getMask() {
		return mask;
	}

	/**
	 * Returns the shift.<br>
	 * 
	 * @return The shift
	 */
	public int getShift() {
		return shift;
	}
}
