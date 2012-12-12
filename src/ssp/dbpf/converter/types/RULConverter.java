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

import ssp.dbpf.types.DBPFRUL;
import ssp.dbpf.util.DBPFUtil;

/**
 * Converter between RUL data and RUL type.<br>
 * 
 * @author Stefan Wertich, mapsonswen@web.de
 * @version 2.0.0, 22.08.2012
 *
 */
public class RULConverter {
	
	/**
	 * Private Constructor prevents instance.<br>
	 */
	private RULConverter() {
	}
	
	/**
	 * Creates a RUL from the given data.<br>
	 * 
	 * @param dData
	 *            The decompressed data
	 * @return The DBPFLUA
	 */
	public static DBPFRUL createType(short[] dData) {
		DBPFRUL type = new DBPFRUL();
		String s = DBPFUtil.getChars(dData, 0x00, dData.length);
		type.setString(s);
		return type;
	}
	
	/**
	 * Creates the data from the given DBPFRUL.<br>
	 * 
	 * @param type
	 *            The DBPFRUL
	 * @return The data
	 */
	public static short[] createData(DBPFRUL type) {
		String s = type.getString();
		short[] data = new short[s.length()];
		DBPFUtil.setChars(s, data, 0x00);
		return data;
	}
}
