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
package ssp.dbpf.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ssp.dbpf.properties.DBPFPropertyTypes;

/**
 * Basic tools for operations on DBPF.<br>
 * 
 * @author Stefan Wertich, mapsonswen@web.de
 * @version 2.0.0, 10.11.2012
 * 
 */
public class DBPFUtil {

	/**
	 * The format for FLOAT values, used for Text-Format
	 */
	public static final DecimalFormat FLOAT_FORMAT;

	// Creates the FLOAT_FORMAT
	static {
		DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.US);
		FLOAT_FORMAT = new DecimalFormat("#0.#");
		FLOAT_FORMAT.setMaximumFractionDigits(6);
		FLOAT_FORMAT.setDecimalFormatSymbols(dfs);
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// EXEMPLAR
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * Returns a string for the given exemplar format.<br>
	 * 
	 * @param format
	 *            The format
	 * @return A string; Unknown, if not known format
	 */
	public static String getExemplarFormat(short format) {
		String ret = "Unknown";
		switch (format) {
		case DBPFConstant.FORMAT_BINARY:
			ret = "B-Format (0x42)";
			break;
		case DBPFConstant.FORMAT_TEXT:
			ret = "T-Format (0x54)";
			break;
		}
		return ret;
	}

	/**
	 * Converts a hex value given by a long to a float value.<br>
	 * 
	 * @param hexValue
	 *            The hex value
	 * @return The float
	 */
	public static float toFloat(long hexValue) {
		return Float.intBitsToFloat((int) hexValue);
	}

	/**
	 * Convert a float value to a hex value given as a long.<br>
	 * 
	 * @param floatValue
	 *            The float value
	 * @return The hex value
	 */
	public static long toHex(float floatValue) {
		int val = Float.floatToIntBits(floatValue);
		long ret = val;
		// To prevent the long filled with F at beginning, resulting
		// from int to long when float was negative, we shift the long value
		// left and then right with no sign
		if (val < 0) {
			ret = ret << 32;
			ret = ret >>> 32;
		}
		return ret;
	}

	/**
	 * Converts a long value to an HEX string.<br>
	 * 
	 * @param value
	 *            The long value
	 * @param length
	 *            The length of the string
	 * @return The hex string
	 */
	public static String toHex(long value, int length) {
		String ret = String.format("%0" + length + "x", value);
		// for negative values the formatter doesn't fit to length,
		// so we cut the string to the wished length.
		if (value < 0) {
			ret = ret.substring(ret.length() - length);
		}
		return ret;
	}

	/**
	 * Converts an float value to an HEX string.<br>
	 * 
	 * @param value
	 *            The float value
	 * @param length
	 *            The length of the string
	 * @return The hex string
	 */
	public static String toHex(float value, int length) {
		return toHex(toHex(value), length);
	}

	/**
	 * Returns the boolean string for the value.<br>
	 * 
	 * @param value
	 *            The value: 0x00=False, 0x01=True
	 * @return True or False as string
	 */
	public static String toBooleanString(long value) {
		if (value == 0x01) {
			return "True";
		}
		return "False";
	}

	/**
	 * Return the long value of the boolean.<br>
	 * If boolean is TRUE this will return 1L, else 0L.
	 * 
	 * @param b
	 *            The boolean
	 * @return 1L, if boolean is TRUE; 0L, otherwise
	 */
	public static long toLong(boolean b) {
		if (b) {
			return 1L;
		}
		return 0L;
	}

	/**
	 * Returns the boolean for the given long.<br>
	 * If long is 1L this will return TRUE, else FALSE.
	 * 
	 * @param l
	 *            The long value
	 * @return TRUE, if value = 1L; FALSE, otherwise
	 */
	public static boolean toBoolean(long l) {
		if (l == 1L) {
			return true;
		}
		return false;
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// ARRAY: convert, read, write
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * Converts a long value to a short array.<br>
	 * The long value could be positive or negative. The result will be set into
	 * the array from lowest to highest truncing bytes, if the length of the
	 * array is too small. The array will be sorted in Low-To-High-Order!
	 * 
	 * e.g.:<br>
	 * 530 dec is 0212 hex, so with length 2 it will be [0]=0x12,[1]=0x02<br>
	 * 530 dec is 0212 hex, so with length 3 it will be
	 * [0]=0x12,[1]=0x02,[2]=0x00<br>
	 * 
	 * @param value
	 *            The long value
	 * @param length
	 *            The length of the array
	 * @return An array in Low-To-High-Order
	 */
	public static short[] toShortArray(long value, int length) {
		short[] ret = new short[length];
		toArray(value, ret, 0, length);
		return ret;
	}

	/**
	 * Converts a float value to a short array.<br>
	 * The float value could be positive or negative. The result will be set
	 * into the array from lowest to highest truncing bytes, if the length of
	 * the array is too small. The array will be sorted in Low-To-High-Order!
	 * 
	 * e.g.:<br>
	 * 530 dec is 0212 hex, so with length 2 it will be [0]=0x12,[1]=0x02<br>
	 * 530 dec is 0212 hex, so with length 3 it will be
	 * [0]=0x12,[1]=0x02,[2]=0x00<br>
	 * 
	 * @param value
	 *            The float value
	 * @param length
	 *            The length of the array
	 * @return An array in Low-To-High-Order
	 */
	public static short[] toShortArray(float value, int length) {
		return toShortArray(toHex(value), length);
	}

	/**
	 * Converts a long value to a short array.<br>
	 * The long value could be positive or negative. The result will be set into
	 * the array from lowest to highest truncing bytes, if the length of the
	 * array is too small. The array will be sorted in Low-To-High-Order!
	 * 
	 * e.g.:<br>
	 * 530 dec is 0212 hex, so with length 2 it will be [0]=0x12,[1]=0x02<br>
	 * 530 dec is 0212 hex, so with length 3 it will be
	 * [0]=0x12,[1]=0x02,[2]=0x00<br>
	 * 
	 * @param value
	 *            The long value
	 * @param dest
	 *            The destination array
	 * @param offset
	 *            The offset inside the array
	 * @param length
	 *            The length of the array
	 */
	public static void toArray(long value, short[] dest, int offset, int length) {
		// create the hex string from the value
		String s = Long.toHexString(value);
		// fill with necessary zeros to fit into array
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 2 * length - s.length(); i++) {
			sb.append('0');
		}
		s = sb.toString() + s;
		// fill the array from lowest to highest
		int idx;
		for (int i = 0; i < length; i++) {
			idx = s.length() - 2 * i;
			dest[offset + i] = Short.parseShort(s.substring(idx - 2, idx), 16);
		}
	}

	/**
	 * Converts a float value to a short array.<br>
	 * The float value could be positive or negative. The result will be set
	 * into the array from lowest to highest truncing bytes, if the length of
	 * the array is too small. The array will be sorted in Low-To-High-Order!
	 * 
	 * e.g.:<br>
	 * 530 dec is 0212 hex, so with length 2 it will be [0]=0x12,[1]=0x02<br>
	 * 530 dec is 0212 hex, so with length 3 it will be
	 * [0]=0x12,[1]=0x02,[2]=0x00<br>
	 * 
	 * @param value
	 *            The float value
	 * @param dest
	 *            The destination array
	 * @param offset
	 *            The offset inside the array
	 * @param length
	 *            The length of the array
	 */
	public static void toArray(float value, short[] dest, int offset, int length) {
		toArray(toHex(value), dest, offset, length);
	}

	/**
	 * Converts an short array to a long value.<br>
	 * 
	 * This method is normally used for Binary format of an exemplar.
	 * 
	 * The array is sorted Low-To-High-Order. The value could be signed
	 * interpreted or not.
	 * 
	 * 
	 * @param data
	 *            The array
	 * @param start
	 *            The start in the array
	 * @param length
	 *            The length
	 * @param signed
	 *            TRUE, if value is signed; FALSE, otherwise
	 * @return The long value
	 */
	public static long toValue(short[] data, int start, int length,
			boolean signed) {
		long result = 0L;

		// value is negative, if last short is 0xF
		if (signed && (data[start + length - 1] & 0xF0) == 0xF0) {

			// create an hex string from the shorts
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < length; i++) {
				sb.append(toHex(data[start + length - 1 - i], 2));
			}
			String hexString = sb.toString();

			result = getResult(hexString);
		} else {
			for (int i = 0; i < length; i++) {
				short readed = data[start + i];
				result += (readed * Math.pow(256, i));
			}
		}
		return result;
	}

	/**
	 * Converts a hexString to a long value.<br>
	 * 
	 * The method is normally used for Text format of an exemplar.
	 * 
	 * The value could be signed interpreted or not. The string could start with
	 * 0x or without.
	 * 
	 * @param hexString
	 *            The hexString
	 * @param signed
	 *            TRUE, if value is signed; FALSE, otherwise
	 * @return The long value
	 */
	public static long toValue(String hexString, boolean signed) {
		long result = 0L;
		hexString = hexString.toLowerCase();
		if (hexString.startsWith("0x")) {
			hexString = hexString.substring(2);
		}
		// value is negative, if last short is 0xF
		if (signed && hexString.startsWith("f")) {
			result = getResult(hexString);
		} else {
			result = Long.parseLong(hexString, 16);
		}
		return result;
	}

	/**
	 * Get the result from the given HexString.<br>
	 * This method is used by {@link #toValue(String, boolean)} and
	 * {@link #toValue(short[], int, int, boolean)}.
	 * 
	 * @param hexString
	 *            The hexString
	 * @return The result
	 */
	private static long getResult(String hexString) {
		long result = 0L;
		// get signifikant index
		int slength = hexString.length();
		int i = 0;
		while (i < slength) {
			if (hexString.charAt(i) == 'f') {
				i++;
			} else {
				break;
			}
		}

		if (i == slength) {
			// string consists of 0xFFFFFFFF
			result = -1;
		} else {
			// get signifikant bits
			String value = "0" + hexString.substring(i);
			long val = Long.parseLong(value, 16);

			// create specific maximum from length and signifikant bits
			// length
			final long MAX = (long) Math.pow(16, (slength - i));

			// get final result
			result = -(MAX - val);
		}
		return result;
	}

	/**
	 * Reads a value till length reached.<br>
	 * 
	 * @param type
	 *            PropertyType.SINT32 or PropertyType.SINT64 for signed values;
	 *            Otherwise for unsigned
	 * @param data
	 *            The data
	 * @param start
	 *            The start offset
	 * @param length
	 *            The length
	 * 
	 * @return A long value
	 */
	public static long getValue(DBPFPropertyTypes type, short[] data,
			int start, int length) {
		if (type == DBPFPropertyTypes.SINT32
				|| type == DBPFPropertyTypes.SINT64) {
			return getSint32(data, start, length);
		}
		return getUint32(data, start, length);
	}

	/**
	 * Writes a value till length reached.<br>
	 * 
	 * @param type
	 *            PropertyType.SINT32 or PropertyType.SINT64 for signed values;
	 *            Otherwise for unsigned
	 * @param value
	 *            The value
	 * @param data
	 *            The data
	 * @param start
	 *            The start offset
	 * @param length
	 *            The length
	 */
	public static void setValue(DBPFPropertyTypes type, long value,
			short[] data, int start, int length) {
		if (type == DBPFPropertyTypes.SINT32
				|| type == DBPFPropertyTypes.SINT64) {
			setSint32(value, data, start, length);
		}
		setUint32(value, data, start, length);
	}

	/**
	 * Reads an UINT32 till length reached.<br>
	 * 
	 * Only for positive values for the result!
	 * 
	 * @param data
	 *            The data
	 * @param start
	 *            The start offset
	 * @param length
	 *            The length
	 * 
	 * @return A long value to store UINT32
	 */
	public static long getUint32(short[] data, int start, int length) {
		return toValue(data, start, length, false);
	}

	/**
	 * Writes an UINT32 till length reached.<br>
	 * 
	 * @param value
	 *            The value
	 * @param data
	 *            The data
	 * @param start
	 *            The start offset
	 * @param length
	 *            The length
	 * 
	 */
	public static void setUint32(long value, short[] data, int start, int length) {
		toArray(value, data, start, length);
	}

	/**
	 * Reads an INT32 till length reached.<br>
	 * 
	 * This is the method with byte shifting, e.g. for 4 bytes:<br>
	 * value = value | (((data[start] &0xFF) << 0));<br>
	 * value = value | (((data[start+1] &0xFF) << 8));<br>
	 * value = value | (((data[start+2] &0xFF) << 16));<br>
	 * value = value | (((data[start+3] &0xFF) << 24));<br>
	 * 
	 * @param data
	 *            The data
	 * @param start
	 *            The start offset
	 * @param length
	 *            The length
	 * 
	 * @return A long value to store INT32
	 */

	public static long getInt32(short[] data, int start, int length) {
		long value = 0;
		for (int i = 0; i < length; i++) {
			value = value | ((data[start + i] & 0xFF) << i * 8);
		}
		return value;
	}

	/**
	 * Writes an INT32 till length reached.<br>
	 * 
	 * This is the method with byte shifting, e.g. for 4 bytes:<br>
	 * data2[start + j] = (short) (((value << 24) >> 24) & 0xFF);<br>
	 * data2[start + j + 1] = (short) (((value << 16) >> 24) & 0xFF);<br>
	 * data2[start + j + 2] = (short) (((value << 8) >> 24) & 0xFF);<br>
	 * data2[start + j + 3] = (short) (((value << 0) >> 24) & 0xFF);<br>
	 * 
	 * @param value
	 *            The value
	 * @param data
	 *            The data
	 * @param start
	 *            The start offset
	 * @param length
	 *            The length
	 * 
	 */
	public static void setInt32(long value, short[] data, int start, int length) {
		int length2 = length - 1;
		int maxShift = length2 * 8;
		for (int j = 0; j < length; j++) {
			data[start + j] = (short) (((value << (length2 - j) * 8) >> maxShift) & 0xFF);
		}

	}

	/**
	 * Reads an SINT32 till length reached.<br>
	 * 
	 * For positive and negative values for the result.
	 * 
	 * @param data
	 *            The data
	 * @param start
	 *            The start offset
	 * @param length
	 *            The length
	 * 
	 * @return A long value to store SINT32
	 */
	public static long getSint32(short[] data, int start, int length) {
		return toValue(data, start, length, true);
	}

	/**
	 * Writes an SINT32 till length reached.<br>
	 * 
	 * @param value
	 *            The value
	 * @param data
	 *            The data
	 * @param start
	 *            The start offset
	 * @param length
	 *            The length
	 * 
	 */
	public static void setSint32(long value, short[] data, int start, int length) {
		toArray(value, data, start, length);
	}

	/**
	 * Reads an FLOAT32 till length reached.<br>
	 * 
	 * @param data
	 *            The data
	 * @param start
	 *            The start offset
	 * @param length
	 *            The length
	 * 
	 * @return A long value to store UINT32
	 */
	public static float getFloat32(short[] data, int start, int length) {
		long val = getUint32(data, start, length);
		return toFloat(val);
	}

	/**
	 * Writes an FLOAT32 till length reached.<br>
	 * 
	 * @param value
	 *            The float value
	 * @param data
	 *            The data
	 * @param start
	 *            The start offset
	 * @param length
	 *            The length
	 */
	public static void setFloat32(float value, short[] data, int start,
			int length) {
		long val = toHex(value);
		setUint32(val, data, start, length);
	}

	/**
	 * Reads chars till length reached.<br>
	 * 
	 * @param data
	 *            The data
	 * @param start
	 *            The start offset
	 * @param length
	 *            The length
	 * @return A string
	 */
	public static String getChars(short[] data, int start, int length) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			sb.append((char) data[start + i]);
		}
		return sb.toString();
	}

	/**
	 * Writes chars till length of string reached.<br>
	 * 
	 * @param s
	 *            The string
	 * @param data
	 *            The data
	 * @param start
	 *            The start offset
	 */
	public static void setChars(String s, short[] data, int start) {
		for (int i = 0; i < s.length(); i++) {
			data[start + i] = (short) s.charAt(i);
		}
	}

	/**
	 * Reads UNICODE till length reached.<br>
	 * 
	 * @param data
	 *            The data
	 * @param start
	 *            The start offset
	 * @param length
	 *            The length
	 * @return A string
	 */
	public static String getUnicode(short[] data, int start, int length) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			int val = data[start + 2 * i] + 256 * data[start + 2 * i + 1];
			sb.append((char) val);
		}
		return sb.toString();
	}

	/**
	 * Writes UNICODE till length of string reached.<br>
	 * 
	 * @param s
	 *            The string
	 * @param data
	 *            The data
	 * @param start
	 *            The start offset
	 */
	public static void setUnicode(String s, short[] data, int start) {
		for (int i = 0; i < s.length(); i++) {
			char[] c = Character.toChars(s.codePointAt(i));
			data[start + 2 * i] = (short) c[0];
			if (c.length > 1) {
				data[start + 2 * i + 1] = (short) c[1];
			}
		}
	}

	/**
	 * Reads bytes as strings till end reached.<br>
	 * 
	 * The strings terminates with 0x0D and/or 0x0A. If length of string is
	 * zero, it will not be added.
	 * 
	 * @param data
	 *            The data
	 * @param start
	 *            The start index
	 * @return The list with strings
	 */
	public static List<String> getLines(short[] data, int start) {
		ArrayList<String> readData = new ArrayList<String>();
		StringBuilder sb = new StringBuilder();
		int i = start;
		while (i < data.length) {
			short dat = data[i];
			if (dat == 0x0D || dat == 0x0A) {
				if (sb.length() != 0) {
					readData.add(sb.toString());
					sb = new StringBuilder();
				}
			} else {
				sb.append((char) dat);
			}
			i++;
		}
		// check if last element without CRLF and add it
		if (sb.length() != 0) {
			readData.add(sb.toString());
		}
		return readData;
	}

}
