package ssp.dbpf4j.util;

import java.util.Vector;

import ssp.dbpf4j.properties.DBPFDataTypes;
import ssp.dbpf4j.properties.DBPFFloatProperty;
import ssp.dbpf4j.properties.DBPFLongProperty;
import ssp.dbpf4j.properties.DBPFProperties;
import ssp.dbpf4j.properties.DBPFProperty;
import ssp.dbpf4j.properties.DBPFStringProperty;
import ssp.dbpf4j.types.DBPFExemplar;
import ssp.dbpf4j.types.DBPFLText;
import ssp.dbpf4j.types.DBPFLUA;
import ssp.dbpf4j.types.DBPFRUL;

/**
 * Handle encoded data and create coded data.<br>
 * These data are normally in exemplar files with their properties.
 * 
 * @author Stefan
 * @version 1.2.5, 16.09.2009
 * 
 */
public class DBPFCoder {

	// ************************************************************************
	// DECODING
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
			DBPFProperty prop = createPropertyB(dData, pos);
			propertyList[i] = prop;
			pos += getPropertyBLength(prop);
		}
		exemplar.setPropertyList(propertyList);
		exemplar.setFormat(DBPFUtil.FORMAT_BINARY);
		return exemplar;
	}

	/**
	 * Create the property from the given data.<br>
	 * 
	 * The data is in the Binary-format (0x42).
	 * 
	 * @param dData
	 *            The data with property content
	 * @return The DBPFProperty or NULL, if not known
	 */
	public static DBPFProperty createPropertyB(short[] dData, int offset) {
		int pos = offset;
		long nameValue = DBPFUtil.getUint32(dData, pos, 4);
		pos += 4;
		short dataType = (short) DBPFUtil.getUint32(dData, pos, 2);
		pos += 2;
		long repLong = DBPFUtil.getUint32(dData, pos, 1);
		pos += 1;
		@SuppressWarnings("unused")
		long unknown = DBPFUtil.getUint32(dData, pos, 2);
		pos += 2;

		boolean rep = false;
		int repSize = 1;
		if (repLong == 0x80 || dataType == DBPFDataTypes.DATATYPE_STRING) {
			rep = true;
			repSize = (int) DBPFUtil.getUint32(dData, pos, 4);
			pos += 4;
		}
//		 System.out.println("Name: " + DBPFUtil.toHex(nameValue, 8) + "Type: "
//		 + DBPFUtil.toHex(dataType, 4) + "Rep: "
//		 + DBPFUtil.toHex(repLong, 2) + "RepSize: " + repSize);

		// check the dataType
		if (dataType == DBPFDataTypes.DATATYPE_STRING) {
			DBPFStringProperty prop = new DBPFStringProperty();
			prop.setNameValue(nameValue);
			prop.setDataType(dataType);
			prop.updateRepSize(repSize, false);
			prop.setRep(rep);
			prop.setString(DBPFUtil.getChars(dData, pos, repSize));
			pos += repSize;
			return prop;
		} else if (dataType == DBPFDataTypes.DATATYPE_UINT8
				|| dataType == DBPFDataTypes.DATATYPE_UINT16
				|| dataType == DBPFDataTypes.DATATYPE_UINT32
				|| dataType == DBPFDataTypes.DATATYPE_SINT32
				|| dataType == DBPFDataTypes.DATATYPE_SINT64
				|| dataType == DBPFDataTypes.DATATYPE_BOOLEAN) {
			DBPFLongProperty prop = new DBPFLongProperty();
			prop.setNameValue(nameValue);
			prop.setDataType(dataType);
			prop.updateRepSize(repSize, false);
			prop.setRep(rep);
			int length = DBPFDataTypes.getLength(dataType);
			for (int i = 0; i < repSize; i++) {
				long val = DBPFUtil.getUint32(dData, pos, length);
				prop.setLong(val, i);
				pos += length;
			}
			return prop;
		} else if (dataType == DBPFDataTypes.DATATYPE_FLOAT32) {
			DBPFFloatProperty prop = new DBPFFloatProperty();
			prop.setNameValue(nameValue);
			prop.setDataType(dataType);
			prop.updateRepSize(repSize, false);
			prop.setRep(rep);
			int length = DBPFDataTypes.getLength(dataType);
			for (int i = 0; i < repSize; i++) {
				float val = DBPFUtil.getFloat32(dData, pos, length);
				prop.setFloat(val, i);
				pos += length;
			}
			return prop;
		} else {
			System.err.println("[DBPFCoder: createPropertyB] Unknown dataType: " + dataType);
			System.err.println("Please inform the author about this!");
		}
		return null;
	}

	/**
	 * Returns the data length for the given property.<br>
	 * This will take dataType, rep and repSize! Use this only for reading
	 * propertys, for writing use calcPropertyBLength!
	 * 
	 * The length is for the Binary-format (0x42).
	 * 
	 * @return The length in bytes
	 */
	public static int getPropertyBLength(DBPFProperty prop) {
		int length = 9;
		if (prop.isRep()) {
			length += 4;
		}
		int dataTypeLength = DBPFDataTypes.getLength(prop.getDataType());
		length += (prop.getRepSize() * dataTypeLength);
		return length;
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
			DBPFProperty prop = createPropertyT(lines.get(start + 2 + i));
			propertyList[i] = prop;
		}
		exemplar.setPropertyList(propertyList);
		exemplar.setFormat(DBPFUtil.FORMAT_TEXT);
		return exemplar;
	}

	/**
	 * Create the property from the given string.<br>
	 * 
	 * The data is in the Text-format (0x54).
	 * 
	 * @param propString
	 *            The string with property content
	 * @return The DBPFProperty or NULL, if not known
	 */
	public static DBPFProperty createPropertyT(String propString) {
		String[] tokens = propString.split("=");

		// now analyze the nameValue
		String[] nameValues = tokens[0].split(":");
		long nameValue = Long.decode(nameValues[0]);

		// System.out.println(DBPFUtil.toHex(nameValue, 8));

		// now analyze the value
		String[] propValues = tokens[1].split(":");
		short dataType = DBPFDataTypes.getType(propValues[0]);
		int repSize = Integer.parseInt(propValues[1]);
		boolean rep = true;
		if (repSize == 0) {
			rep = false;
			repSize = 1;
		}
		String s = propValues[2];
		s = s.replace('{', ' ');
		s = s.replace('}', ' ');
		s = s.replace('\"', ' ');
		String[] values = s.trim().split(",");

		// check the dataType
		if (dataType == DBPFDataTypes.DATATYPE_STRING) {
			String val = s.trim();
			DBPFStringProperty prop = new DBPFStringProperty();
			prop.setDataType(dataType);
			prop.setNameValue(nameValue);
			prop.setRep(true);
			prop.setString(val);
			return prop;
		} else if (dataType == DBPFDataTypes.DATATYPE_UINT8
				|| dataType == DBPFDataTypes.DATATYPE_UINT32
				|| dataType == DBPFDataTypes.DATATYPE_SINT32
				|| dataType == DBPFDataTypes.DATATYPE_SINT64) {
			DBPFLongProperty prop = new DBPFLongProperty();
			prop.updateRepSize(repSize, false);
			prop.setDataType(dataType);
			prop.setNameValue(nameValue);
			prop.setRep(rep);
			for (int i = 0; i < values.length; i++) {
				if (values[i].length() > 0) {
					String val = values[i].trim();
					if (!val.contains("0x")) {
						val = "0x" + val;
					}
					prop.setLong(Long.decode(val), i);
				} else {
					prop.setLong(0x00, i);
				}
			}
			return prop;
		} else if (dataType == DBPFDataTypes.DATATYPE_FLOAT32) {
			DBPFFloatProperty prop = new DBPFFloatProperty();
			prop.setNameValue(nameValue);
			prop.setDataType(dataType);
			prop.updateRepSize(repSize, false);
			prop.setRep(rep);
			for (int i = 0; i < values.length; i++) {
				prop.setFloat(Float.parseFloat(values[i].trim()), i);
			}
			return prop;
		} else if (dataType == DBPFDataTypes.DATATYPE_BOOLEAN) {
			DBPFLongProperty prop = new DBPFLongProperty();
			prop.updateRepSize(repSize, false);
			prop.setDataType(dataType);
			prop.setNameValue(nameValue);
			prop.setRep(rep);
			for (int i = 0; i < values.length; i++) {
				long val = 0x00;
				if (values[i].trim().toLowerCase().equals("true") ||
				// this was wrong but implemented in older DBPF4J version
						values[i].equals("0x01")) {
					val = 0x01;
				}
				prop.setLong(val, i);
			}
			return prop;
		}
		return null;
	}

	// ************************************************************************
	// ENCODING
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
			short[] pdata = createPropertyDataB(prop);
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
			sb.append(createPropertyDataT(propList[i]));
			sb.append(CRLF);
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
			return createPropertyDataB(prop);
		} else if (format == DBPFUtil.FORMAT_TEXT) {
			String s = createPropertyDataT(prop);
			short[] retData = new short[s.length()];
			for (int i = 0; i < retData.length; i++) {
				retData[i] = (short) s.charAt(i);
			}
			return retData;
		}
		return null;
	}

	/**
	 * Create the data for the given property.<br>
	 * 
	 * The data is in the Binary-format (0x42).
	 * 
	 * @param prop
	 *            The property to create the data from
	 * @return The array
	 */
	private static short[] createPropertyDataB(DBPFProperty prop) {
		int dataLength = calcPropertyBLength(prop);
		short[] data = new short[dataLength];
		long nameValue = prop.getNameValue();
		short dataType = prop.getDataType();
		long repSize = prop.getRepSize();
		boolean rep = prop.isRep();

		int start = 9;
		if (rep) {
			start += 4;
		}
		int dataTypeLength = DBPFDataTypes.getLength(dataType);

		DBPFUtil.setUint32(nameValue, data, 0, 4);
		DBPFUtil.setUint32(dataType, data, 4, 2);
		if (rep) {
			DBPFUtil.setUint32(0x80, data, 6, 1);
			DBPFUtil.setUint32(repSize, data, 9, 4);
		} else {
			DBPFUtil.setUint32(0x00, data, 6, 1);
		}
		DBPFUtil.setUint32(0x00, data, 7, 2);

		// check the dataType
		if (dataType == DBPFDataTypes.DATATYPE_STRING) {
			DBPFStringProperty p = (DBPFStringProperty) prop;
			DBPFUtil.setChars(p.getString(), data, start);
		} else if (dataType == DBPFDataTypes.DATATYPE_UINT8
				|| dataType == DBPFDataTypes.DATATYPE_UINT32
				|| dataType == DBPFDataTypes.DATATYPE_SINT32
				|| dataType == DBPFDataTypes.DATATYPE_SINT64
				|| dataType == DBPFDataTypes.DATATYPE_BOOLEAN) {
			DBPFLongProperty p = (DBPFLongProperty) prop;
			int pos = start;
			for (int i = 0; i < p.getRepSize(); i++) {
				DBPFUtil.setUint32(p.getLong(i), data, pos, dataTypeLength);
				pos += dataTypeLength;
			}
		} else if (dataType == DBPFDataTypes.DATATYPE_FLOAT32) {
			DBPFFloatProperty p = (DBPFFloatProperty) prop;
			int pos = start;
			for (int i = 0; i < p.getRepSize(); i++) {
				DBPFUtil.setFloat32(p.getFloat(i), data, pos, dataTypeLength);
				pos += dataTypeLength;
			}
		}
		return data;
	}

	/**
	 * Create a string for the given property.<br>
	 * 
	 * The data is in the Text-format (0x54), e.g.:<br>
	 * 0x00000010:{"Exemplar Type"}=Uint32:0:{0x00000002}
	 * 
	 * This uses the Hashtable of DBPFProperties, so be sure, you have set them.
	 * 
	 * @param prop
	 *            The property to create the data from
	 * @return The string
	 */
	private static String createPropertyDataT(DBPFProperty prop) {
		short dataType = prop.getDataType();
		int repSize = prop.getRepSize();
		if (dataType == DBPFDataTypes.DATATYPE_STRING) {
			repSize = 0;
		}

		StringBuffer sb = new StringBuffer();
		sb.append("0x");
		sb.append(DBPFUtil.toHex(prop.getNameValue(), 8));
		sb.append(":{\"");
		String propName = DBPFProperties.propertyList.get(prop.getNameValue());
		if (propName == null) {
			propName = "UNKNOWN";
		}
		sb.append(propName);
		sb.append("\"}=");
		sb.append(DBPFDataTypes.getString(prop.getDataType()));
		sb.append(":");
		sb.append(repSize);
		sb.append(":{");
		if (dataType == DBPFDataTypes.DATATYPE_STRING) {
			DBPFStringProperty p = (DBPFStringProperty) prop;
			sb.append("\"");
			sb.append(p.getString());
			sb.append("\"");
			sb.append("}");
		} else if (dataType == DBPFDataTypes.DATATYPE_UINT8
				|| dataType == DBPFDataTypes.DATATYPE_UINT16
				|| dataType == DBPFDataTypes.DATATYPE_UINT32
				|| dataType == DBPFDataTypes.DATATYPE_SINT32
				|| dataType == DBPFDataTypes.DATATYPE_SINT64) {
			DBPFLongProperty p = (DBPFLongProperty) prop;
			for (int i = 0; i < p.getRepSize(); i++) {
				String s = DBPFUtil.toHex(p.getLong(i), 2 * DBPFDataTypes
						.getLength(p.getDataType()));
				sb.append("0x");
				sb.append(s);
				sb.append(",");
			}
			sb.replace(sb.length() - 1, sb.length(), "}");
		} else if (dataType == DBPFDataTypes.DATATYPE_FLOAT32) {
			DBPFFloatProperty p = (DBPFFloatProperty) prop;
			for (int i = 0; i < p.getRepSize(); i++) {
				sb.append(p.getFloat(i));
				sb.append(",");
			}
			sb.replace(sb.length() - 1, sb.length(), "}");
		} else if (dataType == DBPFDataTypes.DATATYPE_BOOLEAN) {
			DBPFLongProperty p = (DBPFLongProperty) prop;
			for (int i = 0; i < p.getRepSize(); i++) {
				String s = "False";
				if (p.getLong(i) == 0x01) {
					s = "True";
				}
				sb.append(s);
				sb.append(",");
			}
			sb.replace(sb.length() - 1, sb.length(), "}");
		}

		return sb.toString();
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
			dataLength += calcPropertyBLength(prop);
		}
		return dataLength;
	}

	/**
	 * Returns the data length for the given property after updating the rep.<br>
	 * 
	 * If the repSize is 1, the rep is false, else it is true.
	 * 
	 * The length is for the Binary-format (0x42).
	 * 
	 * @return The length in bytes
	 */
	public static int calcPropertyBLength(DBPFProperty prop) {
		boolean rep = true;
		if (prop.getRepSize() == 1 && !prop.isRep()) {
			rep = false;
		}
		prop.setRep(rep);
		return getPropertyBLength(prop);
	}
}
