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
 * Definition for a pixel in DXT.<br>
 * 
 * The pixel has red, green and blue.
 * 
 * @author Stefan Wertich, mapsonswen@web.de
 * @version 2.0.0, 14.08.2012
 * 
 */
public class DXTPixel {

	public int r;
	public int g;
	public int b;

	/**
	 * Constructor.<br>
	 */
	public DXTPixel() {
	}

	/**
	 * Returns a DXTPixel for the given RGB integer.<br>
	 * The RGB integer represent a 24 Bit (888) RGB.
	 * 
	 * @param rgb
	 *            The RGB integer
	 * @return The DXTPixel
	 */
	public static DXTPixel getInstance(int rgb) {
		DXTPixel p = new DXTPixel();
		p.from24Bit(rgb);
		return p;
	}

	/**
	 * Returns a DXTPixel for the given two bytes.<br>
	 * The two bytes represent a 16 Bit (565) RGB.
	 * 
	 * @param lowByte
	 *            The first byte (LowBit)
	 * @param highByte
	 *            The second byte (HighBit)
	 * @return The DXTPixel
	 */
	public static DXTPixel getInstance(short lowByte, short highByte) {
		int rgb = (highByte & 0xFF) << 8 | (lowByte & 0xFF);
		DXTPixel p = new DXTPixel();
		p.from16Bit(rgb);
		return p;
	}

	/**
	 * Gets the red, green and blue from the integer.<br>
	 * The integer consists of 8 bit Red, 8 bit Green, 8 bit Blue.
	 * 
	 * @param rgb
	 *            The RGB integer
	 */
	public void from24Bit(int rgb) {
		r = (rgb & DXTMask.RED_4BYTE.getMask()) >> 16;
		g = (rgb & DXTMask.GREEN_4BYTE.getMask()) >> 8;
		b = (rgb & DXTMask.BLUE_4BYTE.getMask());
	}

	/**
	 * Returns the integer of the red, green and blue.<br>
	 * The integer consists of 8 bit Red, 8 bit Green, 8 bit Blue.
	 * 
	 * @return The RGB integer
	 */
	public int to24Bit() {
		return ((r << 16) | (g << 8) | b);
	}

	/**
	 * Gets the red, green and blue from integer.<br>
	 * The integer consists of 5 bit Red, 6 bit Green, 5 bit Blue.
	 * 
	 * @param rgb
	 *            The RGB integer
	 */
	public void from16Bit(int rgb) {
		this.r = (rgb >> 11) << 3;
		this.g = ((rgb & DXTMask.GREEN_2BYTE.getMask()) >> 5) << 2;
		this.b = ((rgb & DXTMask.BLUE_2BYTE.getMask()) >> 0) << 3;
	}

	/**
	 * Returns the integer of the red, green and blue.<br>
	 * The integer consists of 5 bit Red, 6 bit Green, 5 bit Blue.
	 * 
	 * @return The RGB integer
	 */
	public int to16Bit() {
		return ((((r >> 3) << 11) | ((g >> 2) << 5) | (b >> 3)));
	}

	/**
	 * Returns the Manhattan score of the pixel.<br>
	 * This means: r + g + b
	 * 
	 * @return The score
	 */
	public int score() {
		return r + g + b;
	}

	/**
	 * Returns the Manhattan distance to another pixel in RGB space.<br>
	 * This means: (delta red)^2 + (delta green)^2 + (delta blue)^2<br>
	 * If the pixel is NULL, this will return the Manhatten distance to the
	 * origin in RGB space.
	 * 
	 * @param px
	 *            The other pixel
	 * @return The distance
	 * 
	 */
	public int distance(DXTPixel px) {
		if (px == null) {
			return r * r + g * g + b * b;
		}
		int dr = this.r - px.r;
		int dg = this.g - px.g;
		int db = this.b - px.b;
		return (dr * dr) + (dg * dg) + (db * db);
	}
}
