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

package ssp.dbpf.util;

/**
 * Defines DBPF constants.<br>
 * 
 * @author Stefan Wertich, mapsonswen@web.de
 * @version 2.0.0, 14.08.2012
 * 
 */
public interface DBPFConstant {

	/**
	 * Magic number for all DBPF files
	 */
	public static final String MAGICNUMBER_DBPF = "DBPF";

	/**
	 * Magic number for Exemplar files
	 */
	public static final String MAGICNUMBER_EQZ = "EQZ";

	/**
	 * Magic number for Cohort files
	 */
	public static final String MAGICNUMBER_CQZ = "CQZ";

	/**
	 * Magic Number for FSH files
	 */
	public static final String MAGICNUMBER_FSH = "SHPI";

	/**
	 * Magic Number for PNG files
	 */
	public static final String MAGICNUMBER_PNG = new String((char) 0x89 + "PNG");

	/**
	 * Magic number for Compressed data
	 */
	public static final int MAGICNUMBER_QFS = 0xFB10;

	/**
	 * Magic number for the B-Format of an exemplar
	 * 
	 */
	public static final short FORMAT_BINARY = 0x42;

	/**
	 * Magic number for the T-Format of an exemplar
	 * 
	 */
	public static final short FORMAT_TEXT = 0x54;

	/**
	 * Header size of a DBPF file
	 */
	public static final int HEADERSIZE_DBPF = 0x60; // =96dec

	/**
	 * Header size of a FSH file
	 */
	public static final int HEADERSIZE_FSH = 0x10; // =16dec;

	/**
	 * Header size of a directory inside a FSH file
	 */
	public static final int HEADERSIZE_FSH_DIR = 0x08;

	/**
	 * Header size of a entry inside a FSH file
	 */
	public static final int HEADERSIZE_FSH_ENTRY = 0x10; // =16dec;

	/**
	 * This defines the string for next line
	 */
	public static final String CRLF = "\r\n"; // 0x0D 0x0A

}
