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
public class DBPFFloatProperty extends AbstractDBPFProperty {

	private float[] values;

	/**
	 * Constructor.<br>
	 */
	public DBPFFloatProperty() {
		this(DBPFPropertyManager.getProperties().OTHER.getId(),
				DBPFPropertyTypes.FLOAT32, new float[0]);
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
	public DBPFFloatProperty(long id, DBPFPropertyTypes type, float[] values) {
		super(id, 0, type, false, null, 0);

		if (values != null) {
			setCount(values.length);
			setHasCount(true);
			for (int i = 0; i < values.length; i++) {
				setFloat(values[i], i);
			}
		}
	}

	public DBPFFloatProperty(long id, int count, DBPFPropertyTypes type,
			boolean hasCount, short[] rawData, int offset) {
		super(id, count, type, hasCount, rawData, offset);
	}

	public DBPFFloatProperty(long id, int count, DBPFPropertyTypes type,
			boolean hasCount, String[] data) {
		super(id, count, type, hasCount, data);
	}

	@Override
	protected void initValuesFromRaw(short[] rawData, int offset) {
		values = new float[count];
		for (int i = 0; i < count; i++) {
			float val = DBPFUtil.getFloat32(rawData, offset, type.length);
			values[i] = val;
			offset += type.length;
		}
	}

	@Override
	protected void initValuesFromText(String[] data) {
		values = new float[count];
		for (int i = 0; i < data.length; i++) {
			setFloat(Float.parseFloat(data[i].trim()), i);
		}
	}

	@Override
	protected void valueToRaw(short[] data, int offset) {
		DBPFPropertyTypes type = getType();
		for (int i = 0; i < getCount(); i++) {
			DBPFUtil.setFloat32(getFloat(i), data, offset, type.length);
			offset += type.length;
		}
	}

	@Override
	protected void valueToText(Appendable destination) throws IOException {
		int last = getCount() - 1;
		for (int i = 0; i < getCount(); i++) {
			float f = getFloat(i);
			int fi = (int) f;
			float r = f % fi;
			if (r == 0 || f == 0) {
				// if float value is pure integer
				destination.append(String.valueOf(fi));
			} else {
				destination.append(DBPFUtil.FLOAT_FORMAT.format(f));
			}
			if (i != last) {
				destination.append(",");
			}
		}
	}

	// public void setValues(DBPFProperty src) {
	// values = ((float[])((DBPFFloatProperty)src).values).clone();
	// setCount(src.getCount());
	// }
	//
	// public void setValues(Object values) {
	// this.values = ((float[])values).clone();
	// }
	//
	// /**
	// * Returns a copy of the float[] that this property represents.
	// *
	// * @return a copy of the float[] that is this property's value
	// */
	// public Object getValues() {
	// return floatVal().clone();
	// }

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString());
		sb.append(",");
		sb.append("Count: " + values.length);
		if (values.length > 0) {
			sb.append(",");
			sb.append("Values: ");
			for (int i = 0; i < values.length; i++) {
				sb.append(DBPFUtil.toHex(values[i], 4));
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
	public float getFloat(int index) {
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
	public boolean setFloat(float val, int index) {
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
