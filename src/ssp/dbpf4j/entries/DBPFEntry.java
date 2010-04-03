package ssp.dbpf4j.entries;

import java.io.File;

import ssp.dbpf4j.util.DBPFUtil;

/**
 * Defines an element of a DBPF file.<br>
 * 
 * @author Stefan Wertich
 * @version 1.2.0, 20.08.2009
 * 
 */
public class DBPFEntry {

	// for later reading of file
	private File filename;

	// Global, this will read from DBPF File
	private long tid;
	private long gid;
	private long iid;
	private long offset;
	private long size;

	/**
	 * Constructor.<br>
	 * 
	 * @param tgi
	 *            The TGI of this entry
	 */
	public DBPFEntry(long[] tgi) {
		this(tgi[0], tgi[1], tgi[2]);
	}

	/**
	 * Constructor.<br>
	 * 
	 * @param tid
	 *            The type ID
	 * @param gid
	 *            The group ID
	 * @param iid
	 *            The instance ID
	 */
	public DBPFEntry(long tid, long gid, long iid) {
		this.tid = tid;
		this.gid = gid;
		this.iid = iid;
		offset = 0;
		size = 0;
		filename = null;
	}

	/**
	 * Returns the TGI of this entry.<br>
	 * 
	 * @return An array with TID, GID and IID
	 */
	public long[] getTGI() {
		return new long[] { tid, gid, iid };
	}

	/**
	 * Compares another entry with this.<br>
	 * The two entries are equal, if each TID, GID and IID are the same.
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 * @return TRUE, if the two TGIs are the same; FALSE, otherwise
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof DBPFEntry) {
			DBPFEntry entry = (DBPFEntry) obj;
			boolean tidOK = (entry.getTid() == tid);
			boolean gidOK = (entry.getGid() == gid);
			boolean iidOK = (entry.getIid() == iid);
			if (tidOK && gidOK && iidOK) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns a string with TGI separated by space.<br>
	 * 
	 * @return A string
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(DBPFUtil.toHex(tid, 8));
		sb.append(" ");
		sb.append(DBPFUtil.toHex(gid, 8));
		sb.append(" ");
		sb.append(DBPFUtil.toHex(iid, 8));
		return sb.toString();
	}

	/**
	 * Returns a string with TGI, Offset and Size.<br>
	 * 
	 * @return A string
	 */
	public String toDetailString() {
		StringBuffer sb = new StringBuffer(toString());
		sb.append("\n");
		sb.append("Offset: ");
		sb.append(DBPFUtil.toHex(offset, 8));
		sb.append(" ");
		sb.append("Size: ");
		sb.append(size);
		return sb.toString();
	}

	/**
	 * @return the tid
	 */
	public long getTid() {
		return tid;
	}

	/**
	 * @param tid
	 *            the tid to set
	 */
	public void setTid(long tid) {
		this.tid = tid;
	}

	/**
	 * @return the gid
	 */
	public long getGid() {
		return gid;
	}

	/**
	 * @param gid
	 *            the gid to set
	 */
	public void setGid(long gid) {
		this.gid = gid;
	}

	/**
	 * @return the iid
	 */
	public long getIid() {
		return iid;
	}

	/**
	 * @param iid
	 *            the iid to set
	 */
	public void setIid(long iid) {
		this.iid = iid;
	}

	/**
	 * @return the offset
	 */
	public long getOffset() {
		return offset;
	}

	/**
	 * @param offset
	 *            the offset to set
	 */
	public void setOffset(long offset) {
		this.offset = offset;
	}

	/**
	 * @return the size
	 */
	public long getSize() {
		return size;
	}

	/**
	 * @param size
	 *            the size to set
	 */
	public void setSize(long size) {
		this.size = size;
	}

	/**
	 * @return the filename
	 */
	public File getFilename() {
		return filename;
	}

	/**
	 * @param filename
	 *            the filename to set
	 */
	public void setFilename(File filename) {
		this.filename = filename;
	}

}