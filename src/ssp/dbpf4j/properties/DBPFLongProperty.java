package ssp.dbpf4j.properties;

import java.io.IOException;
import java.util.Arrays;

import ssp.dbpf4j.util.DBPFUtil;

/**
 * Defines a long property.<br>
 * 
 * @author Stefan Wertich
 * @version 1.5.0, 24.08.2010
 * 
 */
public class DBPFLongProperty extends AbstractDBPFProperty {

	private long[] values;

	/**
	 * Constructor.<br>
	 * 
	 */
	public DBPFLongProperty() {
		this(DBPFProperties.UNKNOWN, PropertyType.UINT32, new long[0]);
	}

	/**
	 * Constructor.<br>
	 * 
	 * @param id
	 *            The ID
	 * @param type
	 *            The type
	 * @param values
	 *            The values
	 */
	public DBPFLongProperty(long id, PropertyType type, long[] values) {
		super(id, 0, type, false, null, 0);

		if (values != null) {
			setCount(values.length);
			setHasCount(true);
			for (int i = 0; i < values.length; i++) {
				setLong(values[i], i);
			}
		}
	}

	public DBPFLongProperty(long id, int count, PropertyType type,
			boolean hasCount, short[] rawData, int offset) {
		super(id, count, type, hasCount, rawData, offset);
	}

	public DBPFLongProperty(long id, int count, PropertyType type,
			boolean hasCount, String[] data) {
		super(id, count, type, hasCount, data);
	}

	@Override
	protected void initValuesFromRaw(short[] rawData, int offset) {
		values = new long[count];
		for (int i = 0; i < count; i++) {
			long val = DBPFUtil.getValue(type, rawData, offset, type.length);
			values[i] = val;
			offset += type.length;
		}
	}

	@Override
	protected void initValuesFromText(String[] data) {
		values = new long[count];
		for (int i = 0; i < data.length; i++) {
			long value = 0x00;
			if (data[i].length() > 0) {
				String val = data[i].trim();
				if (type == PropertyType.BOOL) {
					if (val.toLowerCase().equals("true") ||
					// wrong implemented in older DBPF4J versions:
							val.equals("0x01")) {
						value = 0x01;
					}
				} else {
					if (!val.contains("0x")) {
						val = "0x" + val;
					}
					boolean signed = (type == PropertyType.SINT32 || type == PropertyType.SINT64);
					value = DBPFUtil.toValue(val, signed);
				}
			}
			setLong(value, i);
		}
	}

	@Override
	protected void valueToRaw(short[] data, int offset) {
		PropertyType type = getType();
		for (int i = 0; i < getCount(); i++) {
			DBPFUtil.setValue(type, getLong(i), data, offset, type.length);
			offset += type.length;
		}
	}

	@Override
	protected void valueToText(Appendable destination) throws IOException {
		int last = getCount() - 1;
		for (int i = 0; i < getCount(); i++) {
			if (type == PropertyType.BOOL) {
				destination.append(DBPFUtil.toBooleanString(getLong(i)));
			} else {
				destination.append("0x");
				destination.append(DBPFUtil.toHex(getLong(i), 2 * type.length));
			}
			if (i != last) {
				destination.append(",");
			}
		}
	}

	// public Object getValues() {
	// return longVal().clone();
	// }
	//
	// @Override
	// public void setValues(DBPFProperty src) {
	// values = ((long[])((DBPFLongProperty)src).values).clone();
	// }
	//
	// public void setValues(Object values) {
	// this.values = ((long[])values).clone();
	// setCount(longVal().length);
	// }

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(super.toString());
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
	public void setCount(int count) {
		if (count != values.length) {
			values = Arrays.copyOf(values, count);
		}
		super.setCount(count);
	}

	@Override
	public int getCount() {
		return values.length;
	}
}
