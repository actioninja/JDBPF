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

import ssp.dbpf.tgi.TGIKey;

/**
 * Defines an interface for all DBPFTypes.<br>
 * <br>
 * The following types are currently supported:<br>
 * <ol>
 * <li>EQZB1 = Binary format</li>
 * <li>EQZT1 = Text format</li>
 * </ol>
 * 
 * @author Stefan Wertich, mapsonswen@web.de
 * @version 2.0.0, 13.08.2012
 * 
 */
public interface DBPFType {

	/**
	 * @param decompressedSize
	 *            the uncompressedSize to set
	 */
	public void setDecompressedSize(long decompressedSize);

	/**
	 * @return the uncompressedSize
	 */
	public long getDecompressedSize();

	/**
	 * This flag signalize, if the file should be compressed.<br>
	 * 
	 * @param compressed
	 *            set the compressed
	 */
	public void setCompressed(boolean compressed);

	/**
	 * 
	 * @return TRUE, if compressed; FALSE, otherwise
	 */
	public boolean isCompressed();

	/**
	 * Returns the specific type of this type.<br>
	 * 
	 * @return The type
	 */
	public int getType();

	/**
	 * Sets the TGI of this type.<br>
	 * 
	 * @param tgiKey
	 *            The TGIKey
	 */
	public void setTGIKey(TGIKey tgiKey);

	/**
	 * Returns the TGI of this type.<br>
	 * 
	 * @return The TGI
	 */
	public TGIKey getTGIKey();
	
	/**
	 * Returns the TID.<br>
	 * 
	 * @return The TID
	 */
	public long getTID();

	/**
	 * Returns the GID.<br>
	 * 
	 * @return The GID
	 */
	public long getGID();

	/**
	 * Returns the IID.<br>
	 * 
	 * @return The IID
	 */
	public long getIID();

}
