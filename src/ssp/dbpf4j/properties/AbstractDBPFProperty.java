package ssp.dbpf4j.properties;

import java.io.IOException;

import ssp.dbpf4j.util.DBPFUtil;

/**
 * The abstract DBPFProperty class.<br>
 * 
 * @author Stefan Wertich
 * @version 1.5.0, 24.08.2010
 * 
 */
public abstract class AbstractDBPFProperty implements DBPFProperty {

	protected long id;
	protected int count;
	protected PropertyType type;
	protected boolean hasCount;

	// protected Object values;

	/**
	 * Creates a new property with the given values.<br>
	 * 
	 * @param id
	 *            The ID
	 * @param count
	 *            The count
	 * @param type
	 *            The type
	 * @param hasCount
	 *            TRUE, if hasCount; FALSE, otherwise
	 * 
	 */
	// public AbstractDBPFProperty(long id, int count, PropertyType type,
	// boolean hasCount) {
	// this.id = id;
	// this.count = count;
	// this.type = type;
	// this.hasCount = hasCount;
	// }

	/**
	 * Creates a new property by decoding the given raw data.<br>
	 * 
	 * @param id
	 *            The ID
	 * @param count
	 *            The count
	 * @param type
	 *            The type
	 * @param hasCount
	 *            TRUE, if hasCount; FALSE, otherwise
	 * @param rawData
	 *            The rawData
	 * @param offset
	 *            The offset
	 */
	public AbstractDBPFProperty(long id, int count, PropertyType type,
			boolean hasCount, short[] rawData, int offset) {
		this.id = id;
		this.count = count;
		this.type = type;
		this.hasCount = hasCount;

		initValuesFromRaw(rawData, offset);
	}

	/**
	 * Creates a new property by decoding the given data.<br>
	 * 
	 * @param id
	 *            The ID
	 * @param count
	 *            The count
	 * @param type
	 *            The type
	 * @param hasCount
	 *            TRUE, if hasCount; FALSE, otherwise
	 * @param data
	 *            The data
	 */
	public AbstractDBPFProperty(long id, int count, PropertyType type,
			boolean hasCount, String[] data) {
		this.id = id;
		this.count = count;
		this.type = type;
		this.hasCount = hasCount;

		initValuesFromText(data);
	}

	/**
	 * Called from the
	 * {@link #AbstractDBPFProperty(long, int, PropertyType, boolean, short[], int)}
	 * constructor after id, count, type, and hasCount have been initialized.
	 * 
	 * @param rawData
	 *            the data to decode
	 * @param offset
	 *            the index in {@code rawData} where the values data starts
	 */
	protected abstract void initValuesFromRaw(short[] rawData, int offset);

	/**
	 * Called from the
	 * {@link #AbstractDBPFProperty(long, int, PropertyType, boolean, String[])}
	 * constructor after id, count, type, and hasCount have been initialized.
	 * 
	 * @param data
	 *            the data to decode
	 */
	protected abstract void initValuesFromText(String[] data);

	@Override
	public short[] toRaw() {
		// Updates the hasCount
		if (getCount() > 1) {
			// if more than one value
			setHasCount(true);
		} else if (!hasCount()) {
			// if hasCount not already set
			setHasCount(false);
		}

		// writes the basic property information
		short[] data = new short[getBinaryLength()];
		DBPFUtil.setUint32(getID(), data, 0, 4);
		DBPFUtil.setUint32(getType().id, data, 4, 2);
		if (hasCount()) {
			DBPFUtil.setUint32(0x80, data, 6, 1);
			DBPFUtil.setUint32(getCount(), data, 9, 4);
		} else {
			DBPFUtil.setUint32(0x00, data, 6, 1);
		}
		DBPFUtil.setUint32(0x00, data, 7, 2);

		// gets the offset and sets the property values
		int offset = 9;
		if (hasCount()) {
			offset += 4;
		}
		valueToRaw(data, offset);

		return data;
	}

	/**
	 * Writes the property values to the array at the offset.<br>
	 * 
	 * @param data
	 *            The data
	 * @param offset
	 *            The offset
	 */
	protected abstract void valueToRaw(short[] data, int offset);

	@Override
	public String toText() throws IOException {
		int count = getCount();
		// Text-Format use never repeat for one value
		if (count == 1 && !hasCount()) {
			count = 0;
		}
		// if STRING, use zero for count
		if (getType() == PropertyType.STRING) {
			count = 0;
		}

		StringBuffer sb = new StringBuffer();
		sb.append("0x");
		sb.append(DBPFUtil.toHex(getID(), 8));
		sb.append(":{\"");
		String propName = DBPFProperties.getString(getID());
		if (propName == null) {
			propName = "UNKNOWN";
		}
		sb.append(propName);
		sb.append("\"}=");
		sb.append(getType().toString());
		sb.append(':');

		sb.append(Integer.toString(count));
		sb.append(":{");
		valueToText(sb);
		sb.append('}');

		return sb.toString();
	}

	/**
	 * Writes the property values to the appendable destination.<br>
	 * 
	 * @param destination
	 *            The destination
	 */
	protected abstract void valueToText(Appendable destination) throws IOException;

	// public abstract Object getValues();
	//
	// public Object getValue(int index) {
	// return Array.get(values, index);
	// }
	//
	// public Object getValuesQuick() {
	// return this.values;
	// }
	//
	// public void setValues(Object src, int srcPos, int destPos, int length) {
	// System.arraycopy(src, srcPos, values, destPos, length);
	// }
	//
	// public void setValue(Object value, int index) {
	// Array.set(values, index, value);
	// }
	//
	// public abstract void setValues(Object values);
	//
	// public abstract void setValues(DBPFProperty src);

	// public int getDatumLength() {
	// return type.length;
	// }
	//

	// /**
	// * This method should be overridden to append a String representation of
	// the
	// * {@code value} of this property to the given Appendable. This is called
	// * by the default implementation of {@link #toText(java.lang.Appendable)}.
	// *
	// * @param destination the Appendable that receives the text form of this
	// * property's value.
	// * @throws java.io.IOException if the given Apendable throws an exception
	// */
	// protected abstract void valueToText(Appendable destination) throws
	// java.io.IOException;

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("ID: " + DBPFUtil.toHex(id, 8));
		sb.append(",");
		sb.append("DataType: " + DBPFUtil.toHex(getType().id, 2));
		sb.append(",");
		sb.append("HasCount: " + hasCount);
		return sb.toString();
	}

	@Override
	public int getBinaryLength() {
		int length = 9;
		if (hasCount()) {
			length += 4;
		}
		length += (getCount() * getType().length);
		return length;
	}

	@Override
	public long getID() {
		return id;
	}

	@Override
	public void setID(long id) {
		this.id = id;
	}

	@Override
	public PropertyType getType() {
		return type;
	}

	@Override
	public void setType(PropertyType type) {
		this.type = type;
	}

	@Override
	public boolean hasCount() {
		return hasCount;
	}

	@Override
	public void setHasCount(boolean hasCount) {
		this.hasCount = hasCount;
	}

	@Override
	public int getCount() {
		return count;
	}

	@Override
	public void setCount(int count) {
		this.count = count;
	}

}
