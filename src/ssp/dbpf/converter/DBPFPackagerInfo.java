/*
 * Copyright (c) 2013 by Stefan Wertich.  All Rights Reserved.
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
package ssp.dbpf.converter;

/**
 * Stores information about the data given to packager.<br>
 * 
 * @author Stefan Wertich, mapsonswen@web.de
 * @version 2.0.0, 23.02.2013<br>
 *          modified 23.02.2013
 * 
 */
public class DBPFPackagerInfo {

	private long compressedSize = 0;
	private long decompressedSize = 0;
	private boolean compressed = false;

	/**
	 * @return the compressedSize
	 */
	public long getCompressedSize() {
		return compressedSize;
	}

	/**
	 * @param compressedSize
	 *            the compressedSize to set
	 */
	public void setCompressedSize(long compressedSize) {
		this.compressedSize = compressedSize;
	}

	/**
	 * @return the decompressedSize
	 */
	public long getDecompressedSize() {
		return decompressedSize;
	}

	/**
	 * @param decompressedSize
	 *            the decompressedSize to set
	 */
	public void setDecompressedSize(long decompressedSize) {
		this.decompressedSize = decompressedSize;
	}

	/**
	 * @return the compressed
	 */
	public boolean isCompressed() {
		return compressed;
	}

	/**
	 * @param compressed
	 *            the compressed to set
	 */
	public void setCompressed(boolean compressed) {
		this.compressed = compressed;
	}

}
