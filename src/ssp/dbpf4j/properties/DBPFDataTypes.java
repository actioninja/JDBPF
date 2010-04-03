package ssp.dbpf4j.properties;

/**
 * Defines the datatypes of the properties.<br>
 * @author Stefan Wertich
 * @version 1.2.0, 20.08.2009
 *
 */
public class DBPFDataTypes {

	/*
	 * The available dataTypes
	 */
	public static final short DATATYPE_UINT8 = 0x0100;
	public static final short DATATYPE_UINT16 = 0x0200;
	public static final short DATATYPE_UINT32 = 0x0300;
	public static final short DATATYPE_SINT32 = 0x0700;
	public static final short DATATYPE_SINT64 = 0x0800;
	public static final short DATATYPE_FLOAT32 = 0x0900;
	public static final short DATATYPE_BOOLEAN = 0x0b00;
	public static final short DATATYPE_STRING = 0x0c00;

	/**
	 * Returns the dataType for the given string.<br>
	 * 
	 * @param s
	 *            The string
	 * @return The dataType or -1, if not interpretable
	 */
	public static short getType(String s) {
		short dataType = -1;
		s = s.toLowerCase().trim();
		if (s.equals("uint8")) {
			dataType = DBPFDataTypes.DATATYPE_UINT8;
		} else if (s.equals("uint16")) {
			dataType = DBPFDataTypes.DATATYPE_UINT16;
		} else if (s.equals("uint32")) {
			dataType = DBPFDataTypes.DATATYPE_UINT32;
		} else if (s.equals("sint32")) {
			dataType = DBPFDataTypes.DATATYPE_SINT32;
		} else if (s.equals("sint64")) {
			dataType = DBPFDataTypes.DATATYPE_SINT64;
		} else if (s.equals("float32")) {
			dataType = DBPFDataTypes.DATATYPE_FLOAT32;
		} else if (s.equals("bool")) {
			dataType = DBPFDataTypes.DATATYPE_BOOLEAN;
		} else if (s.equals("string")) {
			dataType = DBPFDataTypes.DATATYPE_STRING;
		}
		return dataType;
	}

	/**
	 * Returns the string of the dataType for the given dataType.<br>
	 * 
	 * @param s
	 *            The dataType
	 * @return The string, empty if dataType not found
	 */
	public static String getString(short s) {
		String ret = "";
		switch (s) {
		case DBPFDataTypes.DATATYPE_UINT8:
			ret = "Uint8";
			break;
		case DBPFDataTypes.DATATYPE_UINT16:
			ret = "Uint16";
			break;
		case DBPFDataTypes.DATATYPE_UINT32:
			ret = "Uint32";
			break;
		case DBPFDataTypes.DATATYPE_SINT32:
			ret = "Sint32";
			break;
		case DBPFDataTypes.DATATYPE_SINT64:
			ret = "Sint64";
			break;
		case DBPFDataTypes.DATATYPE_FLOAT32:
			ret = "Float32";
			break;
		case DBPFDataTypes.DATATYPE_STRING:
			ret = "String";
			break;
		case DBPFDataTypes.DATATYPE_BOOLEAN:
			ret = "Bool";
			break;
		}
		return ret;
	}
	
	/**
	 * Returns the length for the dataType in bytes.<br>
	 * 
	 * @param dataType
	 *            The dataType as Integer
	 * @return The length
	 */
	public static int getLength(int dataType) {
		int length = 0;
		switch (dataType) {
		case DBPFDataTypes.DATATYPE_UINT8:
			length = 1;
			break;
		case DBPFDataTypes.DATATYPE_UINT16:
			length = 2;
			break;
		case DBPFDataTypes.DATATYPE_UINT32:
			length = 4;
			break;
		case DBPFDataTypes.DATATYPE_SINT32:
			length = 4;
			break;
		case DBPFDataTypes.DATATYPE_SINT64:
			length = 8;
			break;
		case DBPFDataTypes.DATATYPE_FLOAT32:
			length = 4;
			break;
		case DBPFDataTypes.DATATYPE_BOOLEAN:
			length = 1;
			break;
		case DBPFDataTypes.DATATYPE_STRING:
			length = 1;
			break;
		}
		return length;
	}

}
