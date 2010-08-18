package ssp.dbpf4j.properties;

import ssp.dbpf4j.util.DBPFUtil;

/**
 * Defines a long property.<br>
 * 
 * @author Stefan Wertich
 * @version 1.0.3, 07.02.2009
 * 
 */
public class DBPFFloatProperty implements DBPFProperty {

	private long nameValue;
	private short dataType;
	private float[] value;
	private boolean rep = true;

	
	/**
	 * Constructor.<br>
	 */
	public DBPFFloatProperty() {
		this(DBPFProperties.UNKNOWN, DBPFDataTypes.FLOAT32, new float[0]);
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
	public DBPFFloatProperty(long nameValue, short dataType, float[] values) {
		this.nameValue = nameValue;
		this.dataType = dataType;
		if (values != null) {
			updateCount(values.length, false);
			for (int i = 0; i < values.length; i++) {
				setFloat(values[i], i);
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
		sb.append("RepSize: " + value.length);
		if (value.length > 0) {
			sb.append(",");
			sb.append("Values: ");
			for (int i = 0; i < value.length; i++) {
				sb.append(DBPFUtil.toHex(value[i],4));
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
	public float getFloat(int index) {
		try {
			return value[index];
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
	public boolean setFloat(float val, int index) {
		try {
			value[index] = val;
			return true;
		} catch (ArrayIndexOutOfBoundsException e) {
			return false;
		}
	}
	
	@Override
	public void updateCount(int repSize, boolean copy) {
		float[] temp = new float[repSize];
		if (copy && repSize > 0) {
			int min = Math.min(value.length, repSize);
			System.arraycopy(value, 0, temp, 0, min);
		}
		this.value = temp;
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
