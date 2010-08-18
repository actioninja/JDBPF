package ssp.dbpf4j.properties;

import ssp.dbpf4j.util.DBPFUtil;

/**
 * Defines a long property.<br>
 * 
 * @author Stefan Wertich
 * @version 1.4.0, 18.08.2010
 * 
 */
public class DBPFLongProperty implements DBPFProperty {

	private long nameValue;
	private short dataType;
	private long[] values;
	private boolean rep = true;

	/**
	 * Constructor.<br>
	 * 
	 */
	public DBPFLongProperty() {
		this(DBPFProperties.UNKNOWN, DBPFDataTypes.UINT32, new long[0]);
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
	public DBPFLongProperty(long nameValue, short dataType, long[] values) {
		this.nameValue = nameValue;
		this.dataType = dataType;
		if (values != null) {
			updateCount(values.length, false);
			for (int i = 0; i < values.length; i++) {
				setLong(values[i], i);
			}
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
		sb.append("RepSize: " + values.length);
		if (values.length > 0) {
			sb.append(",");
			sb.append("Values: ");
			for (int i = 0; i < values.length; i++) {
				sb.append(DBPFUtil.toHex(values[i], 8));
				sb.append(" ");
			}
		}
		sb.append("\n");
		return sb.toString();
	}

	/**
	 * Returns the long at the given index.<br>
	 * 
	 * If index throws ArrayIndexOfBoundsException zero will be returned!
	 * 
	 * @param index
	 *            The index
	 * @return The long value
	 */
	public long getLong(int index) {
		try {
			return values[index];
		} catch (ArrayIndexOutOfBoundsException e) {
			return 0;
		}
	}

	/**
	 * Sets the long value at the given index.<br>
	 * 
	 * @param val
	 *            The value
	 * @param index
	 *            The index
	 * @return TRUE, if set successfull; FALSE, if ArrayIndexOfBoundsException
	 */
	public boolean setLong(long val, int index) {
		try {
			values[index] = val;
			return true;
		} catch (ArrayIndexOutOfBoundsException e) {
			return false;
		}
	}

	@Override
	public void updateCount(int repSize, boolean copy) {
		long[] temp = new long[repSize];
		if (copy && repSize > 0) {
			int min = Math.min(values.length, repSize);
			System.arraycopy(values, 0, temp, 0, min);
		}
		this.values = temp;
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
		return values.length;
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
