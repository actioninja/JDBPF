package ssp.dbpf4j.types;

/**
 * Defines a LText of the DBPF.<br>
 * 
 * The LText is UNICODE, so all languages are supported.<br>
 * The decompressedSize is set with setString.
 * 
 * @author Stefan Wertich
 * @version 1.5.0, 24.08.2010
 * 
 */
public class DBPFLText extends AbstractDBPFType {

	private char[] data;

	/**
	 * Constructor.<br>
	 */
	public DBPFLText() {
		data = new char[0];
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(super.toString());
		sb.append(", ");
		sb.append("Data-Size: " + data.length);
		return sb.toString();
	}

	@Override
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
		// 4 (UNICODE Identifier), 2*dataLength (UNICODE Char)
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
}