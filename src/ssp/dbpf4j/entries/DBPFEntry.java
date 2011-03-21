package ssp.dbpf4j.entries;

import java.io.File;

import ssp.dbpf4j.tgi.TGIKey;
import ssp.dbpf4j.util.DBPFUtil;

/**
 * Defines an element of a DBPF file.<br>
 * 
 * @author Stefan Wertich
 * @version 1.6.0, 29.12.2010
 * 
 */
public class DBPFEntry {

	// for later reading of file
	private File filename;

	// Global, this will read from DBPF File
	private TGIKey tgiKey;

	private long offset;
	private long size;

	/**
	 * Constructor.<br>
	 * 
	 * @param tgiKey
	 *            The TGI Key
	 */
	public DBPFEntry(TGIKey tgiKey) {
		this.tgiKey = tgiKey;
		offset = 0;
		size = 0;
		filename = null;
	}

	/**
	 * Returns the TGI of this entry.<br>
	 * 
	 * @return The TGIKey
	 */
	public TGIKey getTGIKey() {
		return tgiKey;
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
			return tgiKey.equals(entry.getTGIKey());
		}
		return false;
	}

	/**
	 * Returns a string with TGI separated by space, offset and size.<br>
	 * 
	 * @return A string
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(tgiKey.toString());
		sb.append("\n");
		sb.append("Offset: ");
		sb.append(DBPFUtil.toHex(offset, 8));
		sb.append(" ");
		sb.append("Size: ");
		sb.append(size);
		return sb.toString();
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