package ssp.dbpf4j.types;

import ssp.dbpf4j.util.DBPFUtil;

/**
 * Defines a LText of the DBPF.<br>
 * 
 * The LText is UNICODE, so all languages are supported.<br>
 * The decompressedSize is set with setString.
 * 
 * @author Stefan Wertich
 * @version 0.9.1.27, 27.01.2009
 * 
 */
public class DBPFLText implements DBPFType {

	private long[] tgi;
	private char[] data;
	private boolean compressed;
	private long decompressedSize;

	/**
	 * Constructor.<br>
	 */
	public DBPFLText() {
		tgi = new long[0];
		data = new char[0];
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("TGI: ");
		sb.append(DBPFUtil.toHex(tgi[0], 8));
		sb.append(",");
		sb.append(DBPFUtil.toHex(tgi[1], 8));
		sb.append(",");
		sb.append(DBPFUtil.toHex(tgi[2], 8));
		sb.append(",");
		sb.append("Compressed: ");
		sb.append(compressed);
		sb.append(", ");
		sb.append("Data-Size: " + data.length);
		return sb.toString();
	}

	public String toDetailString() {
		StringBuffer sb = new StringBuffer(toString());
		if (data.length > 0) {
			sb.append("\n");
			sb.append(data);
		}
		return sb.toString();
	}

	/**
	 * Sets the string.<br>
	 * 
	 * @param s
	 *            The string
	 */
	public void setString(String s) {
		this.data = new char[s.length()];
		s.getChars(0, s.length(), data, 0);
		setDecompressedSize(4 + 2 * data.length);
	}

	/**
	 * Returns the string.<br>
	 * 
	 * @return The data
	 */
	public String getString() {
		return new String(data);
	}

	@Override
	public int getType() {
		return DBPFTypes.LTEXT;
	}

	@Override
	public long[] getTGI() {
		return tgi;
	}

	@Override
	public void setTGI(long[] tgi) {
		this.tgi = tgi;
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
