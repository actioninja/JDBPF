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
package ssp.dbpf.tgi;


/**
 * Defines the TGI Keys with their textual representation.<br>
 * 
 * @author Stefan Wertich, mapsonswen@web.de
 * @version 2.0.0, 04.12.2012
 * 
 */
public enum TGIKeys {

	/**
	 * ATTENTION: Do not change this order or some comparison will fail!
	 */

	/**
	 * RAW format file, used for unknown formats
	 */
	RAW(0, 0x0L, 0x0L, 0x0L, "RAW"),
	/**
	 * Directory file
	 */
	DIRECTORY(1, 0xe86b1eefL, 0xe86b1eefL, 0x286b1f03L, "DIR"),
	/**
	 * LotDescriptor file
	 */
	LD(2, 0x6be74c60L, 0x6be74c60L, -1L, "LD"),
	/**
	 * Exemplar file: LotInfo, LotConfig
	 */
	EXEMPLAR(3, 0x6534284aL, -1L, -1L, "EXEMPLAR"),
	/**
	 * Cohort file
	 */
	COHORT(4, 0x05342861L, -1L, -1L, "COHORT"),
	/**
	 * PNG file: Menu building icons, bridges, overlays
	 */
	PNG_ICON(12, 0x856ddbacL, 0x6a386d26L, -1L, "PNG (Icon)"),
	/**
	 * PNG file: Image, Icon
	 */
	PNG(11, 0x856ddbacL, -1L, -1L, "PNG"),
	/**
	 * FSH file
	 */
	FSH(13, 0x7ab50e44L, -1L, -1L, "FSH"),
	/**
	 * S3D file
	 */
	S3D(14, 0x5ad0e817L, -1L, -1L, "S3D"),
	/**
	 * LUA file: Missions, Advisors, Tutorials and Packaging files
	 */
	LUA(21, 0xca63e2a3L, 0x4a5e8ef6L, -1L, "LUA"),
	/**
	 * LUA file: Generators, Attractors, Repulsors and System LUA
	 */
	LUA_GEN(21, 0xca63e2a3L, 0x4a5e8f3fL, -1L, "LUA (Generators)"),
	/**
	 * RUL file: Network rules
	 */
	RUL(22, 0x0a5bcf4bL, 0xaa5bcf57L, -1L, "RUL"),
	/**
	 * WAV file: Wave sound
	 */
	WAV(31, 0x2026960bL, 0xaa4d1933L, -1L, "WAV"),
	/**
	 * LTEXT file: Language text files or Wave
	 */
	LTEXT(23, 0x2026960bL, -1L, -1L, "LTEXT");

	private int formatID;
	private TGIKey tgiKey;
	private String label;

	/**
	 * Constructor.<br>
	 * 
	 * @param formatID
	 *            The format ID
	 * 
	 * @param tid
	 *            The TID
	 * @param gid
	 *            The GID
	 * @param iid
	 *            The IID
	 * @param label
	 *            The text
	 */
	TGIKeys(int formatID, long tid, long gid, long iid, String label) {
		this.formatID = formatID;
		this.tgiKey = new TGIKey(tid, gid, iid);
		this.label = label;
	}

	/**
	 * Returns the formatID.<br>
	 * 
	 * @return The formatID
	 */
	public int getFormatID() {
		return formatID;
	}

	/**
	 * Returns the TGIKey.<br>
	 * 
	 * @return The TGIKey
	 */
	public TGIKey getTGIKey() {
		return tgiKey;
	}

	/**
	 * Returns the text for the TGIKey.<br>
	 * 
	 * @return The text
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Returns the TGIKeys for the given TGIKey.<br>
	 * If the tgiKey can not be found, it return NULL.
	 * 
	 * @param tgiKey
	 *            The TGIKey
	 * @return The text or NULL
	 */
	public static TGIKeys forTGIKey(TGIKey tgiKey) {
		for (TGIKeys keys : values()) {
			if (keys.getTGIKey().equals(tgiKey)) {
				return keys;
			}
		}
		return null;
	}

}
