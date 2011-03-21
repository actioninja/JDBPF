package ssp.dbpf4j.types;

import ssp.dbpf4j.tgi.TGIKeys;

/**
 * Defines a RUL of DBPF.<br>
 * 
 * The RUL is simple ASCII text, so beware of special characters!<br>
 * The decompressedSize is updated with setString.
 * 
 * @author Stefan Wertich
 * @version 1.5.0, 24.08.2010
 * 
 */
public class DBPFRUL extends AbstractDBPFType {

	private char[] data;

	/**
	 * Constructor.<br>
	 */
	public DBPFRUL() {
		data = new char[0];
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString());
		sb.append("\n");
		sb.append("Data-Size: " + data.length);
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
		setDecompressedSize(data.length);
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
		return TGIKeys.RUL.getFormatID();
	}
}
