/*
 * Copyright (c) 2012 by Stefan Wertich.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 * 
 * This code is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this code.  If not, see <http://www.gnu.org/licenses/>.
 */
package ssp.dbpf.properties;

import java.util.Arrays;

import ssp.dbpf.util.DBPFUtil;

/**
 * Defines a string property.<br>
 * 
 * The string internal consists of a chars array.
 * 
 * @author Stefan Wertich, mapsonswen@web.de
 * @version 2.0.0, 13.08.2012
 * 
 */
public class DBPFStringProperty extends AbstractDBPFProperty {

	private char[] values;

	/**
	 * Constructor.<br>
	 */
	public DBPFStringProperty() {
		this(0L, DBPFPropertyTypes.STRING, "");
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
	public DBPFStringProperty(long id, DBPFPropertyTypes type, String values) {
		super(id, 0, type, false, null, 0);

		if (values != null) {
			setCount(values.length());
			setHasCount(true);
			setString(values);
		}
	}

	public DBPFStringProperty(long id, int count, DBPFPropertyTypes type,
			boolean hasCount, short[] rawData, int offset) {
		super(id, count, type, hasCount, rawData, offset);
	}

	public DBPFStringProperty(long id, int count, DBPFPropertyTypes type,
			boolean hasCount, String[] data) {
		super(id, count, type, hasCount, data);
	}

	@Override
	protected void initValuesFromRaw(short[] rawData, int offset) {
		values = DBPFUtil.getChars(rawData, offset, count).toCharArray();
	}

	@Override
	protected void initValuesFromText(String[] data) {
		setString(data[0].trim());
	}

	@Override
	protected void valueToRaw(short[] data, int offset) {
		DBPFUtil.setChars(getString(), data, offset);
	}

	@Override
	protected void valueToText(Appendable destination)
			throws java.io.IOException {
		destination.append("\"");
		destination.append(getString());
		destination.append("\"");
	}

	// @Override
	// public void getValues(int srcPos, Object dest, int destPos, int length) {
	// if (dest instanceof char[])
	// ((String)values).getChars(srcPos, srcPos+length-1, (char[])dest,
	// destPos);
	// else throw new UnsupportedOperationException();
	// }
	//
	// @Override
	// public Object getValues() {
	// return values;
	// }
	//
	// @Override
	// public Object getValue(int index) {
	// if (index != 0)
	// throw new IndexOutOfBoundsException();
	//
	// return values;
	// }
	//
	// @Override
	// public void setValues(DBPFProperty dest) {
	// values = ((DBPFStringProperty)dest).values;
	// }
	//
	// @Override
	// public void setValues(Object src, int srcPos, int destPos, int length) {
	// if (src instanceof CharSequence
	// && srcPos == 0
	// && destPos == 0
	// && length == ((String)src).length())
	// {
	// this.values = src.toString();
	// } else
	// if (src instanceof char[]) {
	// this.values = new String((char[])src);
	// }
	// }
	//
	// @Override
	// public void setValues(Object values) {
	// this.values = (String)values;
	// }
	//
	// @Override
	// public void setValue(Object value, int index) {
	// if (index != 0)
	// throw new IndexOutOfBoundsException();
	// this.values = (String)value;
	// }

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString());
		sb.append(",");
		sb.append("RepSize: " + values.length);
		if (values.length > 0) {
			sb.append(",");
			sb.append("Values: ");
			for (int i = 0; i < values.length; i++) {
				sb.append(values[i]);
			}
		}
		return sb.toString();
	}

	/**
	 * Returns the string.<br>
	 * 
	 * @return The string
	 */
	public String getString() {
		return new String(values);
	}

	/**
	 * Sets the string.<br>
	 * The internal array will be automatically set to the necessary size.
	 * 
	 * @param s
	 *            The string
	 */
	public void setString(String s) {
		values = new char[s.length()];
		if (s.length() > 0) {
			s.getChars(0, s.length(), values, 0);
		}
	}

	/**
	 * Returns the length of the string.<br>
	 * 
	 * @return The length
	 */
	public int getLength() {
		return values.length;
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
