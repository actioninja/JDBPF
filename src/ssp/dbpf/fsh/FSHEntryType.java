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

/**
 * Definition of supported FSH Entry Types.<br>
 * 
 * Currently supported are: DXT1, DXT3.
 * 
 * @author Stefan Wertich, mapsonswen@web.de
 * @version 2.0.0, 14.08.2012
 * 
 */
public enum FSHEntryType {

	/**
	 * DXT1 4x4 packed, no alpha<br>
	 * Palette: none<br>
	 * Compression: 4x4 grid compressed<br>
	 * half-byte per pixel
	 */
	DXT1(2, 0x60),

	/**
	 * DXT3 4x4 packed, alpha premultiplied<br>
	 * Palette: none<br>
	 * Compression: 4x4 grid compressed<br>
	 * half-byte per pixel
	 */
	DXT3(1, 0x61);

	
	private int pixelPerByte;
	private int typeCode;

	/**
	 * Constructor.<br>
	 * 
	 * @param pixelPerByte
	 *            The pixel per byte
	 * @param typeCode
	 *            The typeCode
	 */
	FSHEntryType(int pixelPerByte, int typeCode) {
		this.pixelPerByte = pixelPerByte;
		this.typeCode = typeCode;
	}

	@Override
	public String toString() {
		return "TypeCode: " + typeCode + ", PixelPerByte: " + pixelPerByte;
	}

	/**
	 * Returns the FSHEntryType for the given typeCode.<br>
	 * 
	 * @param typeCode
	 *            The typeCode
	 * @return The FSHEntryType
	 */
	public static FSHEntryType forTypeCode(int typeCode) {
		for (FSHEntryType type : values()) {
			if (type.getTypeCode() == typeCode) {
				return type;
			}
		}
		throw new UnsupportedOperationException("[FSHEntryType] Typecode not supported: "
				+ typeCode);
	}

	/**
	 * Returns the pixel per byte.<br>
	 * 
	 * @return The pixel per byte
	 */
	public int getPixelPerByte() {
		return pixelPerByte;
	}

	/**
	 * Returns the typeCode.<br>
	 * 
	 * @return The typeCode
	 */
	public int getTypeCode() {
		return typeCode;
	}
}
