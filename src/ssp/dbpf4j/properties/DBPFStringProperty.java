package ssp.dbpf4j.properties;

import ssp.dbpf4j.util.DBPFUtil;

/**
 * Defines a string property.<br>
 * 
 * The string internal consists of a chars array.
 * 
 * @author Stefan Wertich
 * @version 1.0.3, 07.02.2009
 * 
 */
public class DBPFStringProperty implements DBPFProperty {

	private long nameValue;
	private short dataType;
	private char[] value;
	private boolean rep;

	/**
	 * Constructor.<br>
	 */
	public DBPFStringProperty() {
		nameValue = DBPFProperties.UNKNOWN;
		dataType = DBPFDataTypes.DATATYPE_STRING;
		value = new char[0];
		rep = true;
	}

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
		s.getChars(0, s.length(), value, 0);
	}
	
	@Override
	public void updateRepSize(int repSize, boolean copy) {
		// do nothing, cause this will be done with setString		
	}

	@Override
	public short getDataType() {
		return dataType;
	}

	@Override
	public long getNameValue() {
		return nameValue;
	}

	@Override
	public int getRepSize() {
		return value.length;
	}

	@Override
	public void setNameValue(long nameValue) {
		this.nameValue = nameValue;
	}

	@Override
	public void setDataType(short dataType) {
		this.dataType = dataType;
	}

	@Override
	public boolean isRep() {
		return rep;
	}

	@Override
	public void setRep(boolean rep) {
		this.rep = rep;		
	}
}
