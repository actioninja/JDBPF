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
 * The abstract DBPFType.<br>
 * 
 * The TGI will be initialized with {0L, 0L, 0L}.
 * 
 * @author Stefan Wertich, mapsonswen@web.de
 * @version 2.0.0, 13.08.2012
 * 
 */
public abstract class AbstractDBPFType implements DBPFType {

	protected TGIKey tgiKey;
	protected boolean compressed;
	protected long decompressedSize;

	/**
	 * Constructor.<br>
	 */
	public AbstractDBPFType() {
		tgiKey = new TGIKey();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("TGI: ");
		sb.append(tgiKey.toString());
		sb.append(",");
		sb.append("Compressed: ");
		sb.append(compressed);
		sb.append(",");
		sb.append("Decompressed Size: ");
		sb.append(decompressedSize);
		return sb.toString();
	}

	@Override
	public long getTID() {
		return getTGIKey().getTID();
	}

	@Override
	public long getGID() {
		return getTGIKey().getGID();
	}

	@Override
	public long getIID() {
		return getTGIKey().getIID();
	}

	@Override
	public abstract int getType();

	@Override
	public TGIKey getTGIKey() {
		return tgiKey;
	}

	@Override
	public void setTGIKey(TGIKey tgiKey) {
		this.tgiKey = tgiKey;
	}

	@Override
	public boolean isCompressed() {
		return compressed;
	}

	@Override
	public void setCompressed(boolean compressed) {
		this.compressed = compressed;
	}

	@Override
	public long getDecompressedSize() {
		return decompressedSize;
	}

	@Override
	public void setDecompressedSize(long decompressedSize) {
		this.decompressedSize = decompressedSize;
	}
}
