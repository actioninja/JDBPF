package ssp.dbpf4j.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Formatter;
import java.util.Locale;
import java.util.Vector;

import ssp.dbpf4j.properties.PropertyType;

/**
 * Various tools for DBPF.<br>
 * 
 * @author Stefan Wertich
 * @version 1.5.0, 25.08.2010
 * 
 */
public class DBPFUtil {

	/**
	 * The logger name for logging events
	 */
	public static final String LOGGER_NAME = "DBPF4J";

	/**
	 * Magic number for DBPF
	 */
	public static final String MAGICNUMBER_DBPF = "DBPF";

	/**
	 * Magic number for Exemplar files
	 */
	public static final String MAGICNUMBER_EQZ = "EQZ";

	/**
	 * Magic number for Compressed data
	 */
	public static final int MAGICNUMBER_QFS = 0xFB10;

	/**
	 * Magic number for the B-Format of an exemplar
	 * 
	 */
	public static final short FORMAT_BINARY = 0x42;

	/**
	 * Magic number for the T-Format of an exemplar
	 * 
	 */
	public static final short FORMAT_TEXT = 0x54;

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
		case FORMAT_BINARY:
			ret = "B-Format (0x42)";
			break;
		case FORMAT_TEXT:
			ret = "T-Format (0x54)";
			break;
		}
		return ret;
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// CONVERT, FORMAT, DATE
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * Converts a hex value given by a long to a float value.<br>
	 * 
	 * @param hexValue
	 *            The hex value
	 * @return The float
	 */
	public static float convertHexToFloat(long hexValue) {
		return Float.intBitsToFloat((int) hexValue);
	}

	/**
	 * Convert a float value to a hex value given as a long.<br>
	 * 
	 * @param floatValue
	 *            The float value
	 * @return The hex value
	 */
	public static long convertFloatToHex(float floatValue) {
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
		return new Formatter().format("%0" + length + "x", value).toString();
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
		return toHex(convertFloatToHex(value), length);
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
	public static boolean toBool(long l) {
		if (l == 1L) {
			return true;
		}
		return false;
	}

	/**
	 * Returns a formatted date.<br>
	 * The pattern is: yyyy-MM-dd,HH:mm:ss
	 * 
	 * @param date
	 *            The date
	 * @return The formatted date
	 */
	public static String formatDate(long date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd,HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(date * 1000);
		return format.format(cal.getTime());
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// TGI
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * Check if the given TGI is same as another TGI.<br>
	 * 
	 * If any of the ids is -1, it will be ignored, means this id does not
	 * matter anything.<br>
	 * If one of the array has NOT the length 3, FALSE will be returned.
	 * 
	 * @param tgiEntry
	 *            The TGI of an entry
	 * @param tgiCheck
	 *            The TGI to check with
	 * @return TRUE, if the both are same; FALSE, otherwise
	 * 
	 * @deprecated Replaced by DBPFUtil2.isTGI()
	 */
	public static boolean isTGI(long[] tgiEntry, long[] tgiCheck) {
		return DBPFUtil2.isTGI(tgiEntry, tgiCheck);
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
		return toShortArray(convertFloatToHex(value), length);
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
		StringBuffer sb = new StringBuffer();
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
		toArray(convertFloatToHex(value), dest, offset, length);
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
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < length; i++) {
				sb.append(toHex(data[start + length - 1 - i], 2));
			}
			String hexString = sb.toString();

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

			// get signifikant bits
			String value = hexString.substring(i);
			long val = Long.parseLong(value, 16);

			// create specific maximum from length and signifikant bits length
			final long MAX = (long) Math.pow(16, (slength - i));

			// get final result
			result = -(MAX - val);
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

			// get signifikant bits
			String value = hexString.substring(i);
			long val = Long.parseLong(value, 16);

			// create specific maximum from length and signifikant bits length
			final long MAX = (long) Math.pow(16, (slength - i));

			// get final result
			result = -(MAX - val);
		} else {
			result = Long.parseLong(hexString, 16);
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
	public static long getValue(PropertyType type, short[] data, int start,
			int length) {
		if (type == PropertyType.SINT32 || type == PropertyType.SINT64) {
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
	public static void setValue(PropertyType type, long value, short[] data,
			int start, int length) {
		if (type == PropertyType.SINT32 || type == PropertyType.SINT64) {
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
		return convertHexToFloat(val);
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
		long val = convertFloatToHex(value);
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
		StringBuffer sb = new StringBuffer();
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
		StringBuffer sb = new StringBuffer();
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
	public static Vector<String> getLines(short[] data, int start) {
		Vector<String> readData = new Vector<String>();
		StringBuffer sb = new StringBuffer();
		int i = start;
		while (i < data.length) {
			short dat = data[i];
			if (dat == 0x0D || dat == 0x0A) {
				if (sb.length() != 0) {
					readData.addElement(sb.toString());
					sb = new StringBuffer();
				}
			} else {
				sb.append((char) dat);
			}
			i++;
		}
		// check if last element without CRLF and add it
		if (sb.length() != 0) {
			readData.addElement(sb.toString());
		}
		return readData;
	}
}
