package ssp.dbpf4j.format;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import ssp.dbpf4j.properties.DBPFProperty;
import ssp.dbpf4j.properties.PropertyType;
import ssp.dbpf4j.types.DBPFExemplar;
import ssp.dbpf4j.types.DBPFLText;
import ssp.dbpf4j.types.DBPFLUA;
import ssp.dbpf4j.types.DBPFRUL;
import ssp.dbpf4j.util.DBPFUtil;

/**
 * Handle encoded data and create coded data.<br>
 * These data are normally in exemplar files with their properties.
 * 
 * @author Stefan
 * @version 1.5.0, 27.08.2010
 * 
 */
public class DBPFCoder {

	// ************************************************************************
	// SPECIFIC DECODING
	// ************************************************************************

	/**
	 * Creates a LUA from the given data.<br>
	 * 
	 * @param dData
	 *            The decompressed data
	 * @return The DBPFLUA
	 */
	public static DBPFLUA createLUA(short[] dData) {
		DBPFLUA type = new DBPFLUA();
		String s = DBPFUtil.getChars(dData, 0x00, dData.length);
		type.setString(s);
		return type;
	}

	/**
	 * Creates a RUL from the given data.<br>
	 * 
	 * @param dData
	 *            The decompressed data
	 * @return The DBPFLUA
	 */
	public static DBPFRUL createRUL(short[] dData) {
		DBPFRUL type = new DBPFRUL();
		String s = DBPFUtil.getChars(dData, 0x00, dData.length);
		type.setString(s);
		return type;
	}

	/**
	 * Creates a LText from the given data.<br>
	 * 
	 * @param dData
	 *            The decompressed data
	 * @return The DBPFLText
	 */
	public static DBPFLText createLText(short[] dData) {
		DBPFLText type = new DBPFLText();
		String s = "";
		int unicode = 0x00;
		if (dData.length > 3) {
			unicode = (int) DBPFUtil.getUint32(dData, 0x03, 1);
		}
		// fourth is 0x10 as unicode indicator
		if (unicode == 0x10) {
			int numberOfChars = (int) DBPFUtil.getUint32(dData, 0x00, 3);
			s = DBPFUtil.getUnicode(dData, 4, numberOfChars);
		} else {
			s = DBPFUtil.getChars(dData, 0, dData.length);
		}
		type.setString(s);
		return type;
	}

	// ************************************************************************
	// DECODING
	// ************************************************************************

	/**
	 * Creates an exemplar from the given data.<br>
	 * 
	 * @param dData
	 *            The decompressed data
	 * @return The exemplar or NULL, if not exemplar
	 */
	public static DBPFExemplar createExemplar(short[] dData) {
		String fileType = DBPFUtil.getChars(dData, 0x00, 3);
		if (fileType.equals(DBPFUtil.MAGICNUMBER_EQZ)) {
			long format = DBPFUtil.getUint32(dData, 0x03, 1);
			@SuppressWarnings("unused")
			long unknown1 = DBPFUtil.getUint32(dData, 0x04, 1);
			@SuppressWarnings("unused")
			long unknown2 = DBPFUtil.getUint32(dData, 0x05, 3);

			DBPFExemplar exemplar = null;
			// B-Format
			if (format == DBPFUtil.FORMAT_BINARY) {
				exemplar = createExemplarB(dData);
			}
			// T-Format
			else if (format == DBPFUtil.FORMAT_TEXT) {
				exemplar = createExemplarT(dData);
			}
			return exemplar;
		}
		return null;
	}

	/**
	 * Creates an exemplar from the given data.<br>
	 * 
	 * The data is in the Binary-format (0x42).<br>
	 * The header is EQZB1###.
	 * 
	 * @param dData
	 *            The decompressed data
	 * @return The exemplar or NULL, if not exemplar
	 */
	private static DBPFExemplar createExemplarB(short[] dData) {
		DBPFExemplar exemplar = new DBPFExemplar();
		// Reading the exemplars TGI
		exemplar.setCohortT(DBPFUtil.getUint32(dData, 0x08, 4));
		exemplar.setCohortG(DBPFUtil.getUint32(dData, 0x0C, 4));
		exemplar.setCohortI(DBPFUtil.getUint32(dData, 0x10, 4));

		// Reading the properties
		int propCount = (int) DBPFUtil.getUint32(dData, 0x14, 4);
		DBPFProperty[] propertyList = new DBPFProperty[propCount];
		// Size of header
		int pos = 0x18;
		for (int i = 0; i < propCount; i++) {
			DBPFProperty prop = decodeProperty(dData, pos);
			propertyList[i] = prop;
			pos += prop.getBinaryLength();
		}
		exemplar.setPropertyList(propertyList);
		exemplar.setFormat(DBPFUtil.FORMAT_BINARY);
		return exemplar;
	}

	/**
	 * Decodes the property from the rawData at the given offset.<br>
	 * 
	 * @param dData
	 *            The rawData
	 * @param offset
	 *            The offset
	 * @return The property or NULL, if cannot decoded
	 */
	public static DBPFProperty decodeProperty(short[] dData, int offset) {
		long id = DBPFUtil.getUint32(dData, offset, 4);
		offset += 4;
		short typeID = (short) DBPFUtil.getUint32(dData, offset, 2);
		PropertyType type = PropertyType.forID.get(typeID);

		offset += 2;
		long hasCountLong = DBPFUtil.getUint32(dData, offset, 1);
		offset += 1;
		@SuppressWarnings("unused")
		long unknown = DBPFUtil.getUint32(dData, offset, 2);
		offset += 2;

		boolean hasCount = false;
		int count = 1;
		if (hasCountLong == 0x80 || type == PropertyType.STRING) {
			// explicit check of PropertyType.STRING for some strange found
			// files
			hasCount = true;
			count = (int) DBPFUtil.getUint32(dData, offset, 4);
			offset += 4;
		}
		// System.out.println("Name: " + DBPFUtil.toHexString(id, 8) +
		// ", Type: "
		// + DBPFUtil.toHexString(type.id, 4) + ", HasCount: "
		// + DBPFUtil.toHexString(hasCountLong, 2) + ", Count: " + count);

		DBPFProperty prop = null;
		try {
			prop = type.propertyRawConstructor.newInstance(id, count, type,
					hasCount, dData, offset);
		} catch (InstantiationException ie) {
			Logger.getLogger(DBPFUtil.LOGGER_NAME).log(Level.SEVERE,
					ie.getMessage(), ie);
		} catch (IllegalAccessException iae) {
			Logger.getLogger(DBPFUtil.LOGGER_NAME).log(Level.SEVERE,
					iae.getMessage(), iae);
		} catch (IllegalArgumentException iae) {
			Logger.getLogger(DBPFUtil.LOGGER_NAME).log(Level.SEVERE,
					iae.getMessage(), iae);
		} catch (InvocationTargetException ite) {
			Logger.getLogger(DBPFUtil.LOGGER_NAME).log(Level.SEVERE,
					ite.getMessage(), ite);
		}
		if (prop == null) {
			System.err.println("Property can not be decoded! "
					+ DBPFUtil.toHex(id, 8) + "," + DBPFUtil.toHex(typeID, 4)
					+ "," + hasCount + "," + count);
		}
		return prop;
	}

	/**
	 * Creates an exemplar from the given data.<br>
	 * 
	 * The data is in the Text-format (0x54).
	 * 
	 * @param exemplar
	 *            The exemplar
	 * @param dData
	 *            The decompressed data
	 */
	private static DBPFExemplar createExemplarT(short[] dData) {
		DBPFExemplar exemplar = new DBPFExemplar();
		// Read all lines
		Vector<String> lines = DBPFUtil.getLines(dData, 0x08);
		// for (String string : lines) {
		// System.out.println(string);
		// }
		int start = 0;
		while (!lines.get(start).contains("=")) {
			start++;
		}
		// Get cohort from first line
		String[] nameValue = lines.get(start).split("=");
		String[] value = nameValue[1].split(":");
		String[] tgi = value[1].split(",");
		exemplar.setCohortT(Long.decode(tgi[0].substring(1)));
		exemplar.setCohortG(Long.decode(tgi[1]));
		exemplar.setCohortI(Long.decode(tgi[2].substring(0, 10)));

		// Get propCount from second line
		nameValue = lines.get(start + 1).split("=");
		int propCount = Integer.decode(nameValue[1]);
		DBPFProperty[] propertyList = new DBPFProperty[propCount];
		for (int i = 0; i < propertyList.length; i++) {
			DBPFProperty prop = decodeProperty(lines.get(start + 2 + i));
			propertyList[i] = prop;
		}
		exemplar.setPropertyList(propertyList);
		exemplar.setFormat(DBPFUtil.FORMAT_TEXT);
		return exemplar;
	}

	/**
	 * Decodes the property from the given string.<br>
	 * 
	 * @param propString
	 *            The property string
	 * @return The DBPFProperty or NULL, if not known
	 */
	public static DBPFProperty decodeProperty(String propString) {
		String[] tokens = propString.split("=");

		// now analyze the nameValue
		String[] nameValues = tokens[0].split(":");
		long id = Long.decode(nameValues[0]);

		// System.out.println(DBPFUtil.toHex(nameValue, 8));

		// now analyze the value
		String[] propValues = tokens[1].split(":");
		PropertyType type = PropertyType.valueOf(propValues[0].toUpperCase());

		int count = Integer.parseInt(propValues[1]);
		boolean hasCount = true;
		if (count == 0) {
			hasCount = false;
			count = 1;
		}
		String s = propValues[2];
		s = s.replace('{', ' ');
		s = s.replace('}', ' ');
		s = s.replace('\"', ' ');
		String[] data = s.trim().split(",");

		DBPFProperty prop = null;
		try {
			prop = type.propertyTextConstructor.newInstance(id, count, type,
					hasCount, data);
		} catch (InstantiationException ie) {
			Logger.getLogger(DBPFUtil.LOGGER_NAME).log(Level.SEVERE,
					ie.getMessage(), ie);
		} catch (IllegalAccessException iae) {
			Logger.getLogger(DBPFUtil.LOGGER_NAME).log(Level.SEVERE,
					iae.getMessage(), iae);
		} catch (IllegalArgumentException iae) {
			Logger.getLogger(DBPFUtil.LOGGER_NAME).log(Level.SEVERE,
					iae.getMessage(), iae);
		} catch (InvocationTargetException ite) {
			Logger.getLogger(DBPFUtil.LOGGER_NAME).log(Level.SEVERE,
					ite.getMessage(), ite);
		}
		if (prop == null) {
			System.err.println("Property can not be decoded! "
					+ DBPFUtil.toHex(id, 8) + "," + DBPFUtil.toHex(type.id, 4)
					+ "," + hasCount + "," + count);
		}
		return prop;
	}

	// ************************************************************************
	// SPECIFIC ENCODING
	// ************************************************************************

	/**
	 * Creates the data from the given DBPFLUA.<br>
	 * 
	 * @param type
	 *            The DBPFLUA
	 * @return The data
	 */
	public static short[] createLUAData(DBPFLUA type) {
		String s = type.getString();
		short[] data = new short[s.length()];
		DBPFUtil.setChars(s, data, 0x00);
		return data;
	}

	/**
	 * Creates the data from the given DBPFRUL.<br>
	 * 
	 * @param type
	 *            The DBPFRUL
	 * @return The data
	 */
	public static short[] createRULData(DBPFRUL type) {
		String s = type.getString();
		short[] data = new short[s.length()];
		DBPFUtil.setChars(s, data, 0x00);
		return data;
	}

	/**
	 * Creates the data from the given DBPFLText.<br>
	 * 
	 * The data is always UNICODE format!
	 * 
	 * @param type
	 *            The DBPFLText
	 * @return The data
	 */
	public static short[] createLTextData(DBPFLText type) {
		String s = type.getString();
		short[] data = new short[2 * s.length() + 4];
		DBPFUtil.setUint32(s.length(), data, 0x00, 3);
		// fourth is always 0x10 as UNICODE indicator
		DBPFUtil.setUint32(0x10, data, 0x03, 1);
		DBPFUtil.setUnicode(s, data, 0x04);
		return data;
	}

	// ************************************************************************
	// ENCODING
	// ************************************************************************

	/**
	 * Create the data for the given exemplar.<br>
	 * 
	 * @param exemplar
	 *            The exemplar
	 * @param format
	 *            DBPFUtil.MAGICNUMBER_B_FORMAT or T_FORMAT
	 * @return The data
	 */
	public static short[] createExemplarData(DBPFExemplar exemplar, short format) {
		if (format == DBPFUtil.FORMAT_BINARY) {
			return createExemplarDataB(exemplar);
		} else if (format == DBPFUtil.FORMAT_TEXT) {
			return createExemplarDataT(exemplar);
		}
		return null;
	}

	/**
	 * Create the data for the given exemplar.<br>
	 * 
	 * The data is in the Binary-format (0x42).
	 * 
	 * @param exemplar
	 *            The exemplar
	 * @return The data
	 */
	private static short[] createExemplarDataB(DBPFExemplar exemplar) {
		DBPFProperty[] propList = exemplar.getPropertyList();
		short[] data = new short[DBPFCoder.calcExemplarDataLength(exemplar)];
		DBPFUtil.setChars(DBPFUtil.MAGICNUMBER_EQZ, data, 0x00);
		DBPFUtil.setUint32(DBPFUtil.FORMAT_BINARY, data, 0x03, 1);
		long unknown1 = 0x31;
		DBPFUtil.setUint32(unknown1, data, 0x04, 1);
		long unknown2 = 0x232323;
		DBPFUtil.setUint32(unknown2, data, 0x05, 3);
		DBPFUtil.setUint32(exemplar.getCohortT(), data, 0x08, 4);
		DBPFUtil.setUint32(exemplar.getCohortG(), data, 0x0c, 4);
		DBPFUtil.setUint32(exemplar.getCohortI(), data, 0x10, 4);
		DBPFUtil.setUint32(propList.length, data, 0x14, 4);
		int pos = 0x18;
		for (DBPFProperty prop : propList) {
			short[] pdata = prop.toRaw();
			for (int i = 0; i < pdata.length; i++) {
				data[pos + i] = pdata[i];
			}
			pos += pdata.length;
		}
		return data;
	}

	/**
	 * Create the data for the given exemplar.<br>
	 * 
	 * The data is in the Text-format (0x54).
	 * 
	 * @param exemplar
	 *            The exemplar
	 * @return The data
	 */
	private static short[] createExemplarDataT(DBPFExemplar exemplar) {
		final String CRLF = "\r\n";
		StringBuffer sb = new StringBuffer();
		// Header
		sb.append(DBPFUtil.MAGICNUMBER_EQZ);
		sb.append("T1###");
		sb.append(CRLF);
		// Parent Cohort Key
		sb.append("ParentCohort=Key:{0x");
		sb.append(DBPFUtil.toHex(exemplar.getCohortT(), 8));
		sb.append(",0x");
		sb.append(DBPFUtil.toHex(exemplar.getCohortG(), 8));
		sb.append(",0x");
		sb.append(DBPFUtil.toHex(exemplar.getCohortI(), 8));
		sb.append("}");
		sb.append(CRLF);
		// PropCount
		DBPFProperty[] propList = exemplar.getPropertyList();
		sb.append("PropCount=0x");
		sb.append(DBPFUtil.toHex(propList.length, 8));
		sb.append(CRLF);
		// Propertys
		for (int i = 0; i < propList.length; i++) {
			try {
				sb.append(propList[i].toText());
				sb.append(CRLF);
			} catch (IOException e) {
				Logger.getLogger(DBPFUtil.LOGGER_NAME).log(Level.SEVERE,
						e.getMessage(), e);
			}
		}
		short[] data = new short[sb.length()];
		DBPFUtil.setChars(sb.toString(), data, 0);
		return data;
	}

	/**
	 * Create the data for the given property.<br>
	 * 
	 * @param prop
	 *            The property to create the data from
	 * @param format
	 *            DBPFUtil.MAGICNUMBER_B_FORMAT or T_FORMAT
	 * @return The array
	 */
	public static short[] createPropertyData(DBPFProperty prop, short format) {
		if (format == DBPFUtil.FORMAT_BINARY) {
			return prop.toRaw();
		} else if (format == DBPFUtil.FORMAT_TEXT) {
			String s = "";
			try {
				s = prop.toText();
			} catch (IOException e) {
				Logger.getLogger(DBPFUtil.LOGGER_NAME).log(Level.SEVERE,
						e.getMessage(), e);
			}
			short[] retData = new short[s.length()];
			for (int i = 0; i < retData.length; i++) {
				retData[i] = (short) s.charAt(i);
			}
			return retData;
		}
		return null;
	}

	/**
	 * Returns the data length for the given exemplar after calculating.<br>
	 * 
	 * The length is for the Binary-format (0x42).
	 * 
	 * @param exemplar
	 *            The exemplar
	 * @return The length in bytes
	 */
	public static int calcExemplarDataLength(DBPFExemplar exemplar) {
		int dataLength = 0x18;
		DBPFProperty[] propList = exemplar.getPropertyList();
		for (DBPFProperty prop : propList) {
			// Updates the hasCount
			if (prop.getCount() > 1) {
				// if more than one value
				prop.setHasCount(true);
			} else if (!prop.hasCount()) {
				// if hasCount not already set
				prop.setHasCount(false);
			}
			dataLength += prop.getBinaryLength();
		}
		return dataLength;
	}
}
