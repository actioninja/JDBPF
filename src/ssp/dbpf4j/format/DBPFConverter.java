package ssp.dbpf4j.format;


import ssp.dbpf4j.entries.DBPFEntry;
import ssp.dbpf4j.io.DBPFReader;
import ssp.dbpf4j.tgi.TGIKey;
import ssp.dbpf4j.tgi.TGIKeys;
import ssp.dbpf4j.types.DBPFCohort;
import ssp.dbpf4j.types.DBPFDirectory;
import ssp.dbpf4j.types.DBPFExemplar;
import ssp.dbpf4j.types.DBPFLText;
import ssp.dbpf4j.types.DBPFLUA;
import ssp.dbpf4j.types.DBPFPNG;
import ssp.dbpf4j.types.DBPFRUL;
import ssp.dbpf4j.types.DBPFRaw;
import ssp.dbpf4j.types.DBPFType;

/**
 * This class provide functions to convert between DBPFEntry, DBPFType and
 * RawData.<br>
 * 
 * @author Stefan Wertich
 * @version 1.6.0, 28.12.2010
 * 
 */
public class DBPFConverter {

	/**
	 * Constructor.<br>
	 * 
	 * PRIVATE to prevent instance.
	 */
	private DBPFConverter() {
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// Create type from entry
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * Creates a DBPFType from given DBPFEntry.<br>
	 * 
	 * Analyze the entry and read the data, decompress, decode and return the
	 * type. If the entry is not known the simple DBPFRaw will be returned.
	 * 
	 * @param entry
	 *            The DBPFEntry
	 * @return The DBPFRaw
	 */
	public static DBPFType createType(DBPFEntry entry) {
		return createType(entry, false);
	}

	/**
	 * Creates a DBPFType from given DBPFEntry.<br>
	 * 
	 * If not onlyRawType, then read the data, decompress, decode and return the
	 * type, else it simple set the data to the DBPFRaw.
	 * 
	 * @param entry
	 *            The DBPFEntry
	 * @param onlyRawType
	 *            TRUE, if only returns DBPFRaw with no decompress or decode
	 * @return The DBPFRaw
	 */
	public static DBPFType createType(DBPFEntry entry, boolean onlyRawType) {
		TGIKey tgiKey = entry.getTGIKey();
		// System.out.println("Entry: "+entry.toString()+","+entry.getFilename());
		// read rawdata from entry
		short[] data = DBPFReader.readData(entry);
		DBPFPackager packager = new DBPFPackager();
		short[] dData = packager.decompress(data);

		if (onlyRawType) {
			DBPFRaw type = new DBPFRaw();
			type.setTGIKey(tgiKey);
			type.setData(data);
			type.setCompressed(packager.isCompressed());
			type.setDecompressedSize(packager.getDecompressedSize());
			return type;
		}

		DBPFType type = null;
		if (tgiKey.equals(TGIKeys.EXEMPLAR)) {
			type = DBPFCoder.createExemplar(dData);
		} else if(tgiKey.equals(TGIKeys.COHORT)) {
            type = DBPFCoder.createCohort(dData);
        } else if (tgiKey.equals(TGIKeys.PNG)) {
			type = new DBPFPNG();
			((DBPFPNG) type).setImageData(dData);
		} else if (tgiKey.equals(TGIKeys.WAV)) {
			// FIXME not implemented yet, so use DBPFRaw
			type = null;
		} else if (tgiKey.equals(TGIKeys.LTEXT)) {
			type = DBPFCoder.createLText(dData);
		} else if (tgiKey.equals(TGIKeys.DIRECTORY)) {
			type = new DBPFDirectory();
			((DBPFDirectory) type).setData(data);
		} else if (tgiKey.equals(TGIKeys.LUA)) {
			type = DBPFCoder.createLUA(dData);
		} else if (tgiKey.equals(TGIKeys.RUL)) {
			type = DBPFCoder.createRUL(dData);
		} else if (tgiKey.equals(TGIKeys.FSH)) {
			// FIXME not implemented yet, so use DBPFRaw
			type = null;
		} else if (tgiKey.equals(TGIKeys.S3D)) {
			// FIXME not implemented yet, so use DBPFRaw
			type = null;
		}

		if (type == null) {
			type = new DBPFRaw();
			((DBPFRaw) type).setData(data);
		}
		type.setTGIKey(tgiKey);
		type.setCompressed(packager.isCompressed());
		type.setDecompressedSize(packager.getDecompressedSize());
		return type;
	}

	/**
	 * Create an exemplar from the given entry. If the entry is not an exemplar
	 * or the exmplar could not be created this return null.
	 * 
	 * @param entry
	 *            The entry
	 * @return The exemplar or NULL
	 */
	public static DBPFExemplar createExemplar(DBPFEntry entry) {
		if (entry.getTGIKey().equals(TGIKeys.EXEMPLAR)) {
			DBPFType type = DBPFConverter.createType(entry);
			if (type instanceof DBPFExemplar) {
				return (DBPFExemplar) type;
			}
		}
		return null;
	}
	
	/**
     * Create a cohort from the given entry. If the entry is not a cohort
     * or the cohort could not be created this return null.
     *
     * @param entry
     *            The entry
     * @return The cohort or NULL
     */
    public static DBPFCohort createCohort(DBPFEntry entry) {
        if(entry.getTGIKey().equals(TGIKeys.COHORT)) {
            DBPFType type = DBPFConverter.createType(entry);
            if(type instanceof DBPFCohort) {
                return (DBPFCohort) type;
            }
        }
        return null;
    }

	/**
	 * Create a PNG from the given entry. If the entry is not a PNG or the PNG
	 * could not be created this return null.
	 * 
	 * @param entry
	 *            The entry
	 * @return The PNG or NULL
	 */
	public static DBPFPNG createPNG(DBPFEntry entry) {
		if (entry.getTGIKey().equals(TGIKeys.PNG)) {
			DBPFType type = DBPFConverter.createType(entry);
			if (type instanceof DBPFPNG) {
				return (DBPFPNG) type;
			}
		}
		return null;
	}

	/**
	 * Create a LTEXT from the given entry. If the entry is not a LTEXT or the
	 * LTEXT could not be created this return null.
	 * 
	 * @param entry
	 *            The entry
	 * @return The LText or NULL
	 */
	public static DBPFLText createLTEXT(DBPFEntry entry) {
		if (entry.getTGIKey().equals(TGIKeys.LTEXT)) {
			DBPFType type = DBPFConverter.createType(entry);
			if (type instanceof DBPFLText) {
				return (DBPFLText) type;
			}
		}
		return null;
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// Create data from type
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * Creates an array with the data from the DBPFType.<br>
	 * 
	 * @param type
	 *            The DBPFType
	 * @return The data
	 */
	public static short[] createData(DBPFType type) {
		int formatID = type.getType();
		short[] data = new short[0];
		if (formatID == TGIKeys.EXEMPLAR.getFormatID()) {
			DBPFExemplar exem = (DBPFExemplar) type;
			data = DBPFCoder.createExemplarData(exem, exem.getFormat());
		} else if (formatID == TGIKeys.PNG.getFormatID()
				|| formatID == TGIKeys.PNG_ICON.getFormatID()) {
			DBPFPNG png = (DBPFPNG) type;
			data = png.getImageData();
		} else if (formatID == TGIKeys.WAV.getFormatID()) {
			// FIXME not implmented yet, so use DBPFRaw
			DBPFRaw raw = (DBPFRaw) type;
			data = raw.getData();
		} else if (formatID == TGIKeys.LTEXT.getFormatID()) {
			data = DBPFCoder.createLTextData((DBPFLText) type);
		} else if (formatID == TGIKeys.DIRECTORY.getFormatID()) {
			DBPFDirectory dir = (DBPFDirectory) type;
			data = dir.getData();
		} else if (formatID == TGIKeys.LUA.getFormatID()) {
			data = DBPFCoder.createLUAData((DBPFLUA) type);
		} else if (formatID == TGIKeys.RUL.getFormatID()) {
			data = DBPFCoder.createRULData((DBPFRUL) type);
		} else if (formatID == TGIKeys.FSH.getFormatID()) {
			// FIXME not implmented yet, so use DBPFRaw
			DBPFRaw raw = (DBPFRaw) type;
			data = raw.getData();
		} else if (formatID == TGIKeys.S3D.getFormatID()) {
			// FIXME not implmented yet, so use DBPFRaw
			DBPFRaw raw = (DBPFRaw) type;
			data = raw.getData();
		} else {
			DBPFRaw raw = (DBPFRaw) type;
			data = raw.getData();
		}

		// Compress the known files, if they were compressed,
		// the unknown are RAW and leave as they was!!!
		if (type.isCompressed() && (formatID != TGIKeys.RAW.getFormatID())) {
			DBPFPackager packager = new DBPFPackager();
			data = packager.compress(data);
		}
		return data;
	}

}
