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
package ssp.dbpf.io;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ssp.dbpf.DBPFContainer;
import ssp.dbpf.converter.DBPFConverter;
import ssp.dbpf.entries.DBPFEntry;
import ssp.dbpf.event.DBPFException;
import ssp.dbpf.tgi.TGIKey;
import ssp.dbpf.tgi.TGIKeys;
import ssp.dbpf.types.DBPFDirectory;
import ssp.dbpf.types.DBPFRaw;
import ssp.dbpf.types.DBPFType;
import ssp.dbpf.util.DBPFUtil;

/**
 * Updater for DBPFF.<br>
 * 
 * @author Stefan Wertich, mapsonswen@web.de
 * @version 2.0.0, 09.12.2012
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
	// DBPF
	// ************************************************************************

	/**
	 * Updates a DBPF file with the given filename with the typeList.<br>
	 * This will read the DBPF file given by the filename and replace each entry
	 * by the one from the given typeList, if the TGI is the same. Then it will
	 * write the updated entrys to the given filename.
	 * 
	 * @param filename
	 *            The file to update
	 * @param typeList
	 *            The typeList
	 * @return TRUE, if successful updated; FALSE, otherwise
	 * @throws DBPFException
	 *             Thrown, if error occur
	 */
	public static boolean updateDBPF(File filename, List<DBPFType> typeList)
			throws DBPFException {
		DBPFContainer file = DBPFReader.read(filename);
		boolean found = false;
		List<DBPFType> writeList = new ArrayList<DBPFType>();
		for (DBPFEntry entry : file.getEntryList()) {
			found = false;
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
		}
		DBPFWriter.write(filename, writeList);
		return true;
	}

	// ***********************************************************************
	// DIRECTORY
	// ***********************************************************************

	/**
	 * Updates the directory for the given typelist.<br>
	 * If dir is not in typeList this will add a dir entry. If no compressed
	 * entrys in typeList this will remove the dir entry.
	 * 
	 * @param typeList
	 *            The typelist
	 */
	public static void updateDirectory(List<DBPFType> typeList) {
		TGIKey tgiKeyDir = TGIKeys.DIRECTORY.getTGIKey();

		DBPFType dir = null;
		List<DBPFType> v = new ArrayList<DBPFType>();
		for (DBPFType type : typeList) {
			if (type.getTGIKey().equals(tgiKeyDir)) {
				dir = type;
			} else if (type.isCompressed()) {
				v.add(type);
			}
		}
		if (v.size() == 0) {
			if (dir != null) {
				typeList.remove(dir);
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
				typeList.add(dir);
			}
		}
	}
}
