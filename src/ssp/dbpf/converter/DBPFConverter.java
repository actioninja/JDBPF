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
package ssp.dbpf.converter;

import java.util.logging.Level;

import ssp.dbpf.converter.types.CohortConverter;
import ssp.dbpf.converter.types.DirectoryConverter;
import ssp.dbpf.converter.types.ExemplarConverter;
import ssp.dbpf.converter.types.FSHConverter;
import ssp.dbpf.converter.types.LTextConverter;
import ssp.dbpf.converter.types.LUAConverter;
import ssp.dbpf.converter.types.PNGConverter;
import ssp.dbpf.converter.types.RULConverter;
import ssp.dbpf.converter.types.S3DConverter;
import ssp.dbpf.converter.types.WAVConverter;
import ssp.dbpf.entries.DBPFEntry;
import ssp.dbpf.event.DBPFException;
import ssp.dbpf.io.DBPFReader;
import ssp.dbpf.tgi.TGIKey;
import ssp.dbpf.tgi.TGIKeys;
import ssp.dbpf.types.DBPFCohort;
import ssp.dbpf.types.DBPFDirectory;
import ssp.dbpf.types.DBPFExemplar;
import ssp.dbpf.types.DBPFFSH;
import ssp.dbpf.types.DBPFLText;
import ssp.dbpf.types.DBPFLUA;
import ssp.dbpf.types.DBPFPNG;
import ssp.dbpf.types.DBPFRUL;
import ssp.dbpf.types.DBPFRaw;
import ssp.dbpf.types.DBPFS3D;
import ssp.dbpf.types.DBPFType;
import ssp.dbpf.types.DBPFWAV;
import ssp.dbpf.util.DBPFLogger;

/**
 * This class provide functions to convert between DBPFEntry, DBPFType and
 * RawData.<br>
 * 
 * @author Stefan Wertich, mapsonswen@web.de
 * @version 2.0.0, 04.12.2012
 * 
 */
public class DBPFConverter {

	public static final String LOGNAME = DBPFConverter.class.getSimpleName();

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
	 * @return The DBPFType
	 * @throws DBPFException
	 *             Thrown, if entry cannot be read
	 */
	public static DBPFType createType(DBPFEntry entry) throws DBPFException {
		return createType(entry, false);
	}

	/**
	 * Creates a DBPFType from given DBPFEntry.<br>
	 * 
	 * If not onlyRawType, then read the data, decompress, decode and return the
	 * type. Otherwise or if error while converting occur, use the DBPFRaw.
	 * 
	 * @param entry
	 *            The DBPFEntry
	 * @param onlyRawType
	 *            TRUE, if only returns DBPFRaw with no decompress or decode
	 * @return The DBPFRaw
	 * @throws DBPFException
	 *             Thrown, if entry cannot be read
	 */
	public static DBPFType createType(DBPFEntry entry, boolean onlyRawType)
			throws DBPFException {
		TGIKey tgiKey = entry.getTGIKey();
		// System.out.println("Entry: "+entry.toString()+","+entry.getFilename());
		// read rawdata from entry
		short[] data = DBPFReader.readData(entry);
		DBPFPackagerInfo info = new DBPFPackagerInfo();
		short[] dData = DBPFPackager.decompress(data, info);

		DBPFType type = null;
		// if not only raw, try to decode the data
		if (!onlyRawType) {
			try {
				if (tgiKey.equals(TGIKeys.EXEMPLAR)) {
					type = ExemplarConverter.createType(dData);
				} else if (tgiKey.equals(TGIKeys.COHORT)) {
					type = CohortConverter.createType(dData);
				} else if (tgiKey.equals(TGIKeys.PNG)) {
					type = PNGConverter.createType(dData);
				} else if (tgiKey.equals(TGIKeys.WAV)) {
					type = WAVConverter.createType(dData);
				} else if (tgiKey.equals(TGIKeys.LTEXT)) {
					type = LTextConverter.createType(dData);
				} else if (tgiKey.equals(TGIKeys.DIRECTORY)) {
					type = DirectoryConverter.createType(dData);
				} else if (tgiKey.equals(TGIKeys.LUA)) {
					type = LUAConverter.createType(dData);
				} else if (tgiKey.equals(TGIKeys.RUL)) {
					type = RULConverter.createType(dData);
				} else if (tgiKey.equals(TGIKeys.FSH)) {
					type = FSHConverter.createType(dData);
				} else if (tgiKey.equals(TGIKeys.S3D)) {
					type = S3DConverter.createType(dData);
				}
			} catch (DBPFException e) {
				DBPFLogger.toLog(LOGNAME, Level.SEVERE, e.getMessage()
						+ " Use raw type for data.");
				type = null;
			}
		}

		// if only raw or decoding failed, use raw type
		if (onlyRawType || type == null) {
			type = new DBPFRaw();
			((DBPFRaw) type).setData(data);
		}
		type.setTGIKey(tgiKey);
		type.setCompressed(info.isCompressed());
		type.setDecompressedSize(info.getDecompressedSize());
		return type;
	}

	/**
	 * Create an exemplar from the given entry. If the entry is not an exemplar
	 * or the exmplar could not be created this return null.
	 * 
	 * @param entry
	 *            The entry
	 * @return The exemplar or NULL if entry no exemplar
	 * @throws DBPFException
	 *             Thrown, if entry cannot be read
	 */
	public static DBPFExemplar createExemplar(DBPFEntry entry)
			throws DBPFException {
		if (entry.getTGIKey().equals(TGIKeys.EXEMPLAR)) {
			DBPFType type = DBPFConverter.createType(entry);
			if (type instanceof DBPFExemplar) {
				return (DBPFExemplar) type;
			}
		}
		return null;
	}

	/**
	 * Create a cohort from the given entry. If the entry is not a cohort or the
	 * cohort could not be created this return null.
	 * 
	 * @param entry
	 *            The entry
	 * @return The cohort or NULL if entry no cohort
	 * @throws DBPFException
	 *             Thrown, if entry cannot be read
	 */
	public static DBPFCohort createCohort(DBPFEntry entry) throws DBPFException {
		if (entry.getTGIKey().equals(TGIKeys.COHORT)) {
			DBPFType type = DBPFConverter.createType(entry);
			if (type instanceof DBPFCohort) {
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
	 * @return The PNG or NULL if entry no PNG
	 * @throws DBPFException
	 *             Thrown, if entry cannot be read
	 */
	public static DBPFPNG createPNG(DBPFEntry entry) throws DBPFException {
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
	 * @return The LText or NULL if entry no LTEXT
	 * @throws DBPFException
	 *             Thrown, if entry cannot be read
	 */
	public static DBPFLText createLTEXT(DBPFEntry entry) throws DBPFException {
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
	 * @throws DBPFException
	 *             Thrown, if cannot create data from type
	 */
	public static short[] createData(DBPFType type) throws DBPFException {
		int formatID = type.getType();
		short[] data = new short[0];
		if (formatID == TGIKeys.EXEMPLAR.getFormatID()) {
			DBPFExemplar exem = (DBPFExemplar) type;
			data = ExemplarConverter.createData(exem, exem.getFormat());
		} else if (formatID == TGIKeys.COHORT.getFormatID()) {
			DBPFCohort cohort = (DBPFCohort) type;
			data = CohortConverter.createData(cohort, cohort.getFormat());
		} else if (formatID == TGIKeys.PNG.getFormatID()
				|| formatID == TGIKeys.PNG_ICON.getFormatID()) {
			data = PNGConverter.createData((DBPFPNG) type);
		} else if (formatID == TGIKeys.WAV.getFormatID()) {
			data = WAVConverter.createData((DBPFWAV) type);
		} else if (formatID == TGIKeys.LTEXT.getFormatID()) {
			data = LTextConverter.createData((DBPFLText) type);
		} else if (formatID == TGIKeys.DIRECTORY.getFormatID()) {
			data = DirectoryConverter.createData((DBPFDirectory) type);
		} else if (formatID == TGIKeys.LUA.getFormatID()) {
			data = LUAConverter.createData((DBPFLUA) type);
		} else if (formatID == TGIKeys.RUL.getFormatID()) {
			data = RULConverter.createData((DBPFRUL) type);
		} else if (formatID == TGIKeys.FSH.getFormatID()) {
			data = FSHConverter.createData((DBPFFSH) type);
		} else if (formatID == TGIKeys.S3D.getFormatID()) {
			data = S3DConverter.createData((DBPFS3D) type);
		} else {
			DBPFRaw raw = (DBPFRaw) type;
			data = raw.getData();
		}

		// Compress the known files, if they were compressed,
		// the unknown are RAW and leave as they was!!!
		if (type.isCompressed() && (formatID != TGIKeys.RAW.getFormatID())) {
			data = DBPFPackager.compress(data);
		}
		return data;
	}

}
