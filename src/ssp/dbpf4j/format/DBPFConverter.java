package ssp.dbpf4j.format;

import java.util.Vector;

import ssp.dbpf4j.entries.DBPFEntries;
import ssp.dbpf4j.entries.DBPFEntry;
import ssp.dbpf4j.io.DBPFReader;
import ssp.dbpf4j.types.DBPFDirectory;
import ssp.dbpf4j.types.DBPFExemplar;
import ssp.dbpf4j.types.DBPFLText;
import ssp.dbpf4j.types.DBPFLUA;
import ssp.dbpf4j.types.DBPFPNG;
import ssp.dbpf4j.types.DBPFRUL;
import ssp.dbpf4j.types.DBPFRaw;
import ssp.dbpf4j.types.DBPFType;
import ssp.dbpf4j.types.DBPFTypes;
import ssp.dbpf4j.util.DBPFUtil;
import ssp.dbpf4j.util.DBPFUtil2;

/**
 * This class provide functions to convert between DBPFEntry, DBPFType and
 * RawData.<br>
 * 
 * @author Stefan Wertich
 * @version 1.3.0, 15.11.2009
 * 
 */
public class DBPFConverter {

	/**
	 * Constructor.<br>
	 */
	public DBPFConverter() {
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
		long[] tgi = entry.getTGI();
		// System.out.println("Entry: "+entry.toString()+","+entry.getFilename());
		// read rawdata from entry
		short[] data = DBPFReader.readData(entry);
		DBPFPackager packager = new DBPFPackager();
		short[] dData = packager.decompress(data);

		if (onlyRawType) {
			DBPFRaw type = new DBPFRaw();
			type.setTGI(tgi);
			type.setData(data);
			type.setCompressed(packager.isCompressed());
			type.setDecompressedSize(packager.getDecompressedSize());
			return type;
		}

		DBPFType type = null;
		if (DBPFUtil2.isTGI(tgi, DBPFEntries.EXEMPLAR)) {
			type = DBPFCoder.createExemplar(dData);
		} else if (DBPFUtil2.isTGI(tgi, DBPFEntries.PNG)) {
			type = new DBPFPNG();
			((DBPFPNG) type).setImageData(dData);
		} else if (DBPFUtil2.isTGI(tgi, DBPFEntries.WAV)) {
			// FIXME not implemented yet, so use DBPFRaw
			type = null;
		} else if (DBPFUtil2.isTGI(tgi, DBPFEntries.LTEXT)) {
			type = DBPFCoder.createLText(dData);
		} else if (DBPFUtil2.isTGI(tgi, DBPFEntries.DIRECTORY)) {
			type = new DBPFDirectory();
			((DBPFDirectory) type).setData(data);
		} else if (DBPFUtil2.isTGI(tgi, DBPFEntries.LUA)) {
			type = DBPFCoder.createLUA(dData);
		} else if (DBPFUtil2.isTGI(tgi, DBPFEntries.RUL)) {
			type = DBPFCoder.createRUL(dData);
		} else if (DBPFUtil2.isTGI(tgi, DBPFEntries.FSH)) {
			// FIXME not implemented yet, so use DBPFRaw
			type = null;
		} else if (DBPFUtil2.isTGI(tgi, DBPFEntries.S3D)) {
			// FIXME not implemented yet, so use DBPFRaw
			type = null;
		}

		if (type == null) {
			type = new DBPFRaw();
			((DBPFRaw) type).setData(data);
		}
		type.setTGI(tgi);
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
		if (DBPFUtil2.isTGI(entry.getTGI(), DBPFEntries.EXEMPLAR)) {
			DBPFType type = DBPFConverter.createType(entry);
			if (type instanceof DBPFExemplar) {
				return (DBPFExemplar) type;
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
		if (DBPFUtil2.isTGI(entry.getTGI(), DBPFEntries.PNG)) {
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
		if (DBPFUtil2.isTGI(entry.getTGI(), DBPFEntries.LTEXT)) {
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
		short[] data = new short[0];
		if (type.getType() == DBPFTypes.EXEMPLAR) {
			DBPFExemplar exem = (DBPFExemplar) type;
			data = DBPFCoder.createExemplarData(exem, exem.getFormat());
		} else if (type.getType() == DBPFTypes.PNG
				|| type.getType() == DBPFTypes.PNG_ICON) {
			DBPFPNG png = (DBPFPNG) type;
			data = png.getImageData();
		} else if (type.getType() == DBPFTypes.WAV) {
			// FIXME not implmented yet, so use DBPFRaw
			DBPFRaw raw = (DBPFRaw) type;
			data = raw.getData();
		} else if (type.getType() == DBPFTypes.LTEXT) {
			data = DBPFCoder.createLTextData((DBPFLText) type);
		} else if (type.getType() == DBPFTypes.DIRECTORY) {
			DBPFDirectory dir = (DBPFDirectory) type;
			data = dir.getData();
		} else if (type.getType() == DBPFTypes.LUA) {
			data = DBPFCoder.createLUAData((DBPFLUA) type);
		} else if (type.getType() == DBPFTypes.RUL) {
			data = DBPFCoder.createRULData((DBPFRUL) type);
		} else if (type.getType() == DBPFTypes.FSH) {
			// FIXME not implmented yet, so use DBPFRaw
			DBPFRaw raw = (DBPFRaw) type;
			data = raw.getData();
		} else if (type.getType() == DBPFTypes.S3D) {
			// FIXME not implmented yet, so use DBPFRaw
			DBPFRaw raw = (DBPFRaw) type;
			data = raw.getData();
		} else {
			DBPFRaw raw = (DBPFRaw) type;
			data = raw.getData();
		}

		// Compress the known files, if they were compressed,
		// the unknown are RAW and leave as they was!!!
		if (type.isCompressed() && (type.getType() != DBPFTypes.RAW)) {
			DBPFPackager packager = new DBPFPackager();
			data = packager.compress(data);
		}
		return data;
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// Various
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * Updates the directory for the given writelist.<br>
	 * If dir is not in writeList this will add a dir entry. If no compressed
	 * entrys in writeList this will remove the dir entry.
	 * 
	 * @param writeList
	 *            The writelist
	 */
	public static void updateDirectory(Vector<DBPFType> writeList) {
		DBPFType dir = null;
		Vector<DBPFType> v = new Vector<DBPFType>();
		for (DBPFType type : writeList) {
			if (DBPFUtil2.isTGI(type.getTGI(), DBPFEntries.DIRECTORY)) {
				dir = type;
			} else if (type.isCompressed()) {
				v.addElement(type);
			}
		}
		if (v.size() == 0) {
			if (dir != null) {
				writeList.remove(dir);
			}
		} else {
			short[] data = new short[v.size() * 16];
			int pos = 0x00;
			for (DBPFType type : v) {
				long[] tgi = type.getTGI();
				DBPFUtil.setUint32(tgi[0], data, pos, 4);
				DBPFUtil.setUint32(tgi[1], data, pos + 4, 4);
				DBPFUtil.setUint32(tgi[2], data, pos + 8, 4);
				DBPFUtil.setUint32(type.getDecompressedSize(), data, pos + 12,
						4);
				pos += 16;
			}
			if (dir != null) {
				if (dir instanceof DBPFDirectory) {
					((DBPFDirectory) dir).setData(data);
				} else {
					((DBPFRaw) dir).setData(data);
				}
			} else {
				dir = new DBPFDirectory();
				dir.setCompressed(false);
				dir.setTGI(DBPFEntries.DIRECTORY);
				((DBPFDirectory) dir).setData(data);
				writeList.addElement(dir);
			}
		}
	}

}
