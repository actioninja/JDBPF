package ssp.dbpf4j.properties;

import ssp.dbpf4j.util.DBPFUtil;

/**
 * Defines a string property.<br>
 * 
 * The string internal consists of a chars array.
 * 
 * @author Stefan Wertich
 * @version 1.4.0, 18.08.2010
 * 
 */
public class DBPFStringProperty implements DBPFProperty {

	private long nameValue;
	private short dataType;
	private char[] value;
	private boolean rep = true;

	/**
	 * Constructor.<br>
	 */
	public DBPFStringProperty() {
		this(DBPFProperties.UNKNOWN, DBPFDataTypes.STRING, "");
	}

	/**
	 * Constructor.<br>
	 * 
	 * @param nameValue
	 *            The nameValue
	 * @param dataType
	 *            The dataType
	 * @param values
	 *            The values
	 */
	public DBPFStringProperty(long nameValue, short dataType, String values) {
		this.nameValue = nameValue;
		this.dataType = dataType;
		if (values != null) {
			// updateCount(value.length(), false); not necessary
			setString(values);
		}
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("NameValue: " + DBPFUtil.toHex(nameValue, 8));
		sb.append(",");
		sb.append("DataType: " + DBPFUtil.toHex(getDataType(), 2));
		sb.append(",");
		sb.append("Rep: " + rep);
		sb.append(",");
		sb.append("RepSize: " + value.length);
		if (value.length > 0) {
			sb.append(",");
			sb.append("Values: ");
			for (int i = 0; i < value.length; i++) {
				sb.append(value[i]);
			}
		}
		sb.append("\n");
		return sb.toString();
	}

	/**
	 * Returns the string.<br>
	 * 
	 * @return The string
	 */
	public String getString() {
		return new String(value);
	}

	/**
	 * Sets the string.<br>
	 * The internal array will be automatically set to the necessary size.
	 * 
	 * @param s
	 *            The string
	 */
	public void setString(String s) {
		value = new char[s.length()];
		if (s.length() > 0) {
			s.getChars(0, s.length(), value, 0);
		}
	}

	/**
	 * Returns the length of the string.<br>
	 * 
	 * @return The length
	 */
	public int getLength() {
		return value.length;
	}

	@Override
	public void updateCount(int repSize, boolean copy) {
		// do nothing, cause this will be done with setString
	}

	@Override
	public short getDataType() {
		return dataType;
	}

	@Override
	public long getID() {
		return nameValue;
	}

	@Override
	public int getCount() {
		return value.length;
	}

	@Override
	public void setID(long nameValue) {
		this.nameValue = nameValue;
	}

	@Override
	public void setDataType(short dataType) {
		this.dataType = dataType;
	}

	@Override
	public boolean hasCount() {
		return rep;
	}

	@Override
	public void setHasCount(boolean rep) {
		this.rep = rep;
	}
}
