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
package ssp.dbpf;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ssp.dbpf.types.DBPFType;

/**
 * Container to store DBPFTypes in a file.<br>
 * 
 * This is a simple alternative to {@link DBPFContainer DBPFFile}. The container can
 * easily be read by DBPFReader, write by DBPFWriter and updated with
 * DBPFUpdater.
 * 
 * The default filename is blank.dat.
 * 
 * @author Stefan Wertich, mapsonswen@web.de
 * @version 2.0.0, 13.08.2012
 * 
 */
public class DBPFCollection {

	private File filename;
	private List<DBPFType> typeList;

	/**
	 * Constructor.<br>
	 */
	public DBPFCollection() {
		filename = new File("blank.dat");
		typeList = new ArrayList<DBPFType>();
	}

	@Override
	public String toString() {
		return "File: " + filename + ", TypeList-Size: " + typeList.size();
	}

	/**
	 * Adds a type to the typeList.<br>
	 * 
	 * @param type
	 *            The type
	 */
	public void addType(DBPFType type) {
		typeList.add(type);
	}

	// **********************************************************
	// GET / SET
	// **********************************************************

	/**
	 * @return the filename
	 */
	public File getFilename() {
		return filename;
	}

	/**
	 * @param filename
	 *            the filename to set
	 */
	public void setFilename(File filename) {
		this.filename = filename;
	}

	/**
	 * @return the entryList
	 */
	public List<DBPFType> getTypeList() {
		return typeList;
	}

	/**
	 * @param typeList
	 *            the typeList to set
	 */
	public void setTypeList(List<DBPFType> typeList) {
		this.typeList = typeList;
	}

}
