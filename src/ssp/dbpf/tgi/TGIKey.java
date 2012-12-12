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

import java.util.logging.Level;

import ssp.dbpf.util.DBPFLogger;
import ssp.dbpf.util.DBPFUtil;

/**
 * Defines a TGI key.<br>
 * 
 * @author Stefan Wertich, mapsonswen@web.de
 * @version 2.0.0, 13.08.2012
 * 
 */
public class TGIKey {

	private long tid;
	private long gid;
	private long iid;

	/**
	 * Constructor.<br>
	 * 
	 * All ids are zero.
	 */
	public TGIKey() {
		this(0, 0, 0);
	}

	/**
	 * Constructor.<br>
	 * 
	 * @param tid
	 *            The TID
	 * @param gid
	 *            The GID
	 * @param iid
	 *            The IID
	 */
	public TGIKey(long tid, long gid, long iid) {
		this.tid = tid;
		this.gid = gid;
		this.iid = iid;
	}

	@Override
	public Object clone() {
		return new TGIKey(tid, gid, iid);
	}

	/**
	 * Clones the TGIKey, modifys with the given values and return a new TGIKey.<br>
	 * 
	 * A NEGATIVE value will not change the corresponding id.
	 * 
	 * @param tid
	 *            The new TID or NEGATIVE
	 * @param gid
	 *            The new GID or NEGATIVE
	 * @param iid
	 *            The new IID or NEGATIVE
	 * @return The cloned modified TGI
	 */
	public TGIKey clone(long tid, long gid, long iid) {
		TGIKey tgiKey = (TGIKey) this.clone();
		if (tid >= 0) {
			tgiKey.setTID(tid);
		}
		if (gid >= 0) {
			tgiKey.setGID(gid);
		}
		if (iid >= 0) {
			tgiKey.setIID(iid);
		}
		return tgiKey;
	}

	/**
	 * Constructor.<br>
	 * 
	 * @param tgi
	 *            The TGI
	 */
	public TGIKey(long[] tgi) {
		this(tgi[0], tgi[1], tgi[2]);
	}

	/**
	 * Returns a string with TID, GID and IID separated with whitespace.<br>
	 * e.g. 65428005 34006788 00001000
	 * 
	 * @return A string with TGI
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(DBPFUtil.toHex(tid, 8));
		sb.append(" ");
		sb.append(DBPFUtil.toHex(gid, 8));
		sb.append(" ");
		sb.append(DBPFUtil.toHex(iid, 8));
		return sb.toString();
	}

	/**
	 * Encodes this TGIKey to a string with TGI in Hex and separated with
	 * whitespace.<br>
	 * e.g.: 0x65428005 0x34006788 0x00001000<br>
	 * Use {@link #decode(String)} to get the TGI back.
	 */
	public String encode() {
		StringBuilder sb = new StringBuilder();
		sb.append("0x");
		sb.append(DBPFUtil.toHex(tid, 8));
		sb.append(" ");
		sb.append("0x");
		sb.append(DBPFUtil.toHex(gid, 8));
		sb.append(" ");
		sb.append("0x");
		sb.append(DBPFUtil.toHex(iid, 8));
		return sb.toString();
	}

	/**
	 * Decodes an encoded string with TGI in Hex and separated with whitespace.<br>
	 * e.g.: 0x65428005 0x34006788 0x00001000<br>
	 * Use {@link #encode()} to create a valid string.
	 * 
	 * @param encoded
	 *            The encoded string
	 * @return TRUE, if decoded; FALSE, otherwise
	 */
	public boolean decode(String encoded) {
		String[] tgi = encoded.split(" ");
		if (tgi.length == 3) {
			this.tid = Long.decode(tgi[0]);
			this.gid = Long.decode(tgi[1]);
			this.iid = Long.decode(tgi[2]);
			return true;
		} else {
			DBPFLogger
					.toLog("TGIKey", Level.WARNING,
							"Can not decode string with more than 3 values: "
									+ encoded);
		}
		return false;
	}

	@Override
	public int hashCode() {
		long combined = this.tid ^ this.gid ^ this.iid;
		if (combined > Integer.MAX_VALUE)
			combined += 2 * Integer.MIN_VALUE;
		return new Long(combined).intValue();
	}

	/**
	 * Check if another key has the same TGI as this one.<br>
	 * 
	 * If any of the ids is NEGATIVE, it will be ignored, means this id does not
	 * matter anything.
	 * 
	 * If the given object is not of TGIKey, TGIKeys or a long array, this will
	 * throw a DBPFException!
	 * 
	 * @param obj
	 *            The TGI to check with
	 * @return TRUE, if the both are same; FALSE, otherwise
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TGIKey) {
			TGIKey key = (TGIKey) obj;
			boolean tidOK = (key.getTID() < 0) || (this.tid == key.getTID());
			boolean gidOK = (key.getGID() < 0) || (this.gid == key.getGID());
			boolean iidOK = (key.getIID() < 0) || (this.iid == key.getIID());
			if (tidOK && gidOK && iidOK) {
				return true;
			}
		} else if (obj instanceof TGIKeys) {
			TGIKeys tgiKeys = (TGIKeys) obj;
			return this.equals(tgiKeys.getTGIKey());
		} else if (obj instanceof long[]) {
			long[] array = (long[]) obj;
			if (array.length == 3) {
				return this.equals(new TGIKey(array));
			} else {
				DBPFLogger
						.toLog("TGIKey", Level.WARNING,
								"The given array has not the length of 3. Can not compare it!");
			}
		} else {
			DBPFLogger.toLog("TGIKey", Level.WARNING,
					"Can not compare TGIKey with an object from class: "
							+ obj.getClass().getName());
		}
		return false;
	}

	/**
	 * Returns an array with the TGI.<br>
	 * 
	 * @return The tgi array as TID, GID, IID
	 */
	public long[] getTGI() {
		return new long[] { tid, gid, iid };
	}

	/**
	 * Sets the TGI of this TGIKey.<br>
	 * The array has to have the length of 3 and will be interpreted as [0]=TID,
	 * [1]=GID, [2]=IID.
	 * 
	 * @param tgi
	 *            The array with TID, GID, IID
	 */
	public void setTGI(long[] tgi) {
		if (tgi.length == 3) {
			this.tid = tgi[0];
			this.gid = tgi[1];
			this.iid = tgi[2];
		}
	}

	/**
	 * @return the tid
	 */
	public long getTID() {
		return tid;
	}

	/**
	 * @param tid
	 *            the tid to set
	 */
	public void setTID(long tid) {
		this.tid = tid;
	}

	/**
	 * @return the gid
	 */
	public long getGID() {
		return gid;
	}

	/**
	 * @param gid
	 *            the gid to set
	 */
	public void setGID(long gid) {
		this.gid = gid;
	}

	/**
	 * @return the iid
	 */
	public long getIID() {
		return iid;
	}

	/**
	 * @param iid
	 *            the iid to set
	 */
	public void setIID(long iid) {
		this.iid = iid;
	}

}
