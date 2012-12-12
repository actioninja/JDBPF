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

import ssp.dbpf.types.DBPFLText;
import ssp.dbpf.util.DBPFUtil;

/**
 * Converter between LText data and LText type.<br>
 * @author Stefan Wertich, mapsonswen@web.de
 * @version 2.0.0, 22.08.2012
 *
 */
public class LTextConverter {

	/**
	 * Private Constructor prevents instance.<br>
	 */
	private LTextConverter() {
	}

	/**
	 * Creates a LText from the given data.<br>
	 * 
	 * @param dData
	 *            The decompressed data
	 * @return The DBPFLText
	 */
	public static DBPFLText createType(short[] dData) {
		DBPFLText type = new DBPFLText();
		String s = "";
		int unicode = 0x00;
		if (dData.length > 3) {
			unicode = (int) DBPFUtil.getUint32(dData, 0x03, 1);
		}
		// fourth is 0x10 as unicode indicator
		if (unicode == 0x10) {
			int numberOfChars = (int) DBPFUtil.getUint32(dData, 0x00, 3);
			s = DBPFUtil.getUnicode(dData, 4, numberOfChars);
		} else {
			s = DBPFUtil.getChars(dData, 0, dData.length);
		}
		type.setString(s);
		return type;
	}
	
	/**
	 * Creates the data from the given DBPFLText.<br>
	 * 
	 * The data is always UNICODE format!
	 * 
	 * @param type
	 *            The DBPFLText
	 * @return The data
	 */
	public static short[] createData(DBPFLText type) {
		String s = type.getString();
		short[] data = new short[2 * s.length() + 4];
		DBPFUtil.setUint32(s.length(), data, 0x00, 3);
		// fourth is always 0x10 as UNICODE indicator
		DBPFUtil.setUint32(0x10, data, 0x03, 1);
		DBPFUtil.setUnicode(s, data, 0x04);
		return data;
	}
}
