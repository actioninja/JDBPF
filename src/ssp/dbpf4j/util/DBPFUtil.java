package ssp.dbpf4j.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Formatter;
import java.util.Vector;

/**
 * Various tools for DBPF.<br>
 * 
 * @author Stefan Wertich
 * @version 1.3.0, 04.11.2009
 * 
 */
public class DBPFUtil {

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
	// HEX, DATE
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
	 * Converts an UINT32 value to an HEX string.<br>
	 * Cause of unsigned it is given as long but will be cast to int.
	 * 
	 * @param l
	 *            The uint32 value
	 * @param length
	 *            The length of the string
	 * @return The hex string
	 */
	public static String toHex(long l, int length) {
		return new Formatter().format("%0" + length + "x", l).toString();
	}

	/**
	 * Converts an FLOAT32 value to an HEX string.<br>
	 * 
	 * @param f
	 *            The float32 value
	 * @param length
	 *            The length of the string
	 * @return The hex string
	 */
	public static String toHex(float f, int length) {
		return toHex(convertFloatToHex(f), length);
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
	 */
	public static boolean isTGI(long[] tgiEntry, long[] tgiCheck) {
		if (tgiEntry.length == 3 && tgiCheck.length == 3) {
			boolean tidOK = (tgiCheck[0] == -1) || (tgiEntry[0] == tgiCheck[0]);
			boolean gidOK = (tgiCheck[1] == -1) || (tgiEntry[1] == tgiCheck[1]);
			boolean iidOK = (tgiCheck[2] == -1) || (tgiEntry[2] == tgiCheck[2]);
			if (tidOK && gidOK && iidOK) {
				return true;
			}
		}
		return false;
	}
	

	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// ARRAY: convert, read, write
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * Converts a UINT32 to short array.<br>
	 * The array will be sorted in Low-To-High-Order!
	 * 
	 * e.g.:<br>
	 * 530 dec is 0212 hex, so with length 2 it will be [0]=0x12,[1]=0x02<br>
	 * 530 dec is 0212 hex, so with length 3 it will be
	 * [0]=0x12,[1]=0x02,[2]=0x00<br>
	 * 
	 * @param value
	 *            The uint32 value
	 * @param length
	 *            The length of the array, normally 4
	 * @return An array with length strings in Low-To-High-order
	 */
	public static short[] toShortArray(long value, int length) {
		short[] ret = new short[length];
		for (int i = 0; i < length; i++) {
			long rest = value % 256;
			ret[i] = (short) rest;
			value = value / 256;
		}
		return ret;
	}

	/**
	 * Converts a FLOAT32 to short array.<br>
	 * The array will be sorted in Low-To-High-Order!
	 * 
	 * e.g.:<br>
	 * 530 dec is 0212 hex, so with length 2 it will be [0]=0x12,[1]=0x02<br>
	 * 530 dec is 0212 hex, so with length 3 it will be
	 * [0]=0x12,[1]=0x02,[2]=0x00<br>
	 * 
	 * @param value
	 *            The float value
	 * @param length
	 *            The length of the array, normally 4
	 * @return An array with length strings in Low-To-High-order
	 */
	public static short[] toShortArray(float value, int length) {
		return toShortArray(convertFloatToHex(value), length);
	}

	/**
	 * Reads an UINT32 till length reached.<br>
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
		long sum = 0;
		for (int i = 0; i < length; i++) {
			short readed = data[start + i];
			sum += (readed * Math.pow(16 * 16, i));
		}
		return sum;
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
		short[] array = toShortArray(value, length);
		for (int i = 0; i < array.length; i++) {
			data[start + i] = array[i];
		}
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
