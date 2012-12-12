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

import ssp.dbpf.tgi.TGIKeys;

/**
 * Defines a LText of the DBPF.<br>
 * 
 * The LText is UNICODE, so all languages are supported.<br>
 * The decompressedSize is set with setString.
 * 
 * @author Stefan Wertich, mapsonswen@web.de
 * @version 2.0.0, 13.08.2012
 * 
 */
public class DBPFLText extends AbstractDBPFType {

	private char[] data;

	/**
	 * Constructor.<br>
	 */
	public DBPFLText() {
		data = new char[0];
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString());
		sb.append("\n");
		sb.append("Data-Size: " + data.length);
		if (data.length > 0) {
			sb.append("\n");
			sb.append(data);
		}
		return sb.toString();
	}

	/**
	 * Sets the string.<br>
	 * 
	 * @param s
	 *            The string
	 */
	public void setString(String s) {
		this.data = new char[s.length()];
		s.getChars(0, s.length(), data, 0);
		// 4 (UNICODE Identifier), 2*dataLength (UNICODE Char)
		setDecompressedSize(4 + 2 * data.length);
	}

	/**
	 * Returns the string.<br>
	 * 
	 * @return The data
	 */
	public String getString() {
		return new String(data);
	}

	@Override
	public int getType() {
		return TGIKeys.LTEXT.getFormatID();
	}
}
