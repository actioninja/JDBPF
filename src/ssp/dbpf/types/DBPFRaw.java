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
 * Defines simple RAW data of DBPF.<br>
 * 
 * This class only stores simple rawData and TGI but nothing more.<br>
 * The decompressedSize will be updated with setData. It is for all unknown or
 * not implemented types and could be compressed or not.
 * 
 * @author Stefan Wertich, mapsonswen@web.de
 * @version 2.0.0, 13.08.2012
 * 
 */
public class DBPFRaw extends AbstractDBPFType {

	private short[] rawData;

	/**
	 * Constructor.<br>
	 */
	public DBPFRaw() {
		rawData = new short[0];
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString());
		sb.append("\n");
		sb.append("RawData-Size: " + rawData.length);
		return sb.toString();
	}

	/**
	 * Sets the data of the raw type.<br>
	 * This data are normally the rawData.
	 * 
	 * @param data
	 *            The data
	 */
	public void setData(short[] data) {
		this.rawData = data;
		setDecompressedSize(rawData.length);
	}

	/**
	 * Returns the data of the raw type.<br>
	 * This data is equivalent to the rawData.
	 * 
	 * @return The data
	 */
	public short[] getData() {
		return rawData;
	}

	@Override
	public int getType() {
		return TGIKeys.RAW.getFormatID();
	}
}
