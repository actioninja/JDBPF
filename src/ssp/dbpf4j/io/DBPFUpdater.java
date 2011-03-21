package ssp.dbpf4j.io;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ssp.dbpf4j.DBPFFile;
import ssp.dbpf4j.entries.DBPFEntry;
import ssp.dbpf4j.format.DBPFConverter;
import ssp.dbpf4j.tgi.TGIKey;
import ssp.dbpf4j.tgi.TGIKeys;
import ssp.dbpf4j.types.DBPFDirectory;
import ssp.dbpf4j.types.DBPFRaw;
import ssp.dbpf4j.types.DBPFType;
import ssp.dbpf4j.util.DBPFUtil;

/**
 * Updater for DBPFFiles and DBPFContainer.<br>
 * 
 * @author Stefan Wertich
 * @version 1.6.0, 29.12.2010
 * 
 */
public class DBPFUpdater {

	/**
	 * Constructor.<br>
	 * 
	 * PRIVATE to prevent instance.
	 */
	private DBPFUpdater() {
	}

	// ************************************************************************
	// DBPFFile
	// ************************************************************************

	/**
	 * Updates the given DBPF file with the typeList.<br>
	 * This will read the DBPF file given by the filename and replace each entry
	 * by the one from the given typeList, if the TGI is the same. Then it will
	 * write the updated entrys to the given filename.
	 * 
	 * @param filename
	 *            The file to update
	 * @param typeList
	 *            The typeList
	 * @return TRUE, if successful updated; FALSE, otherwise
	 */
	public static boolean updateDBPFFile(File filename, List<DBPFType> typeList) {
		DBPFFile file = DBPFReader.read(filename);
		boolean found = false;
		List<DBPFType> writeList = new ArrayList<DBPFType>();
		for (DBPFEntry entry : file.getEntryList()) {
			for (int j = 0; j < typeList.size(); j++) {
				DBPFType type = typeList.get(j);
				if (entry.getTGIKey().equals(type.getTGIKey())) {
					writeList.add(type);
					found = true;
				}
			}
			if (!found) {
				writeList.add(DBPFConverter.createType(entry));
			}
			found = false;
		}
		DBPFWriter.write(filename, writeList);
		return true;
	}

	// ***********************************************************************
	// DIRECTORY
	// ***********************************************************************

	/**
	 * Updates the directory for the given writelist.<br>
	 * If dir is not in writeList this will add a dir entry. If no compressed
	 * entrys in writeList this will remove the dir entry.
	 * 
	 * @param writeList
	 *            The writelist
	 */
	public static void updateDirectory(List<DBPFType> writeList) {
		TGIKey tgiKeyDir = TGIKeys.DIRECTORY.getTGIKey();

		DBPFType dir = null;
		List<DBPFType> v = new ArrayList<DBPFType>();
		for (DBPFType type : writeList) {
			if (type.getTGIKey().equals(tgiKeyDir)) {
				dir = type;
			} else if (type.isCompressed()) {
				v.add(type);
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
				long[] tgi = type.getTGIKey().getTGI();
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
				dir.setTGIKey(tgiKeyDir);
				((DBPFDirectory) dir).setData(data);
				writeList.add(dir);
			}
		}
	}
}
