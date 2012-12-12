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

import ssp.dbpf.types.DBPFWAV;

/**
 * Converter between WAV data and WAV type.<br>
 * 
 * @author Stefan Wertich, mapsonswen@web.de
 * @version 2.0.0, 04.12.2012
 * 
 */
public class WAVConverter {

	/**
	 * Private Constructor prevents instance.<br>
	 */
	private WAVConverter() {
	}

	/**
	 * Creates a DBPFWAV from the given data.<br>
	 * 
	 * @param dData
	 *            The decompressed data
	 * @return The DBPFWAV
	 */
	public static DBPFWAV createType(short[] dData) {
		DBPFWAV type = new DBPFWAV();
		type.setData(dData);
		return type;
	}

	/**
	 * Creates the data from the given DBPFWAV.<br>
	 * 
	 * @param type
	 *            The DBPFWAV
	 * @return The data
	 */
	public static short[] createData(DBPFWAV type) {
		short[] data = type.getData();
		return data;
	}
}
