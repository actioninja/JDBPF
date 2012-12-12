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
import java.util.Vector;

import ssp.dbpf.entries.DBPFEntry;
import ssp.dbpf.util.DBPFUtil2;

/**
 * Defines a DBPF file.<br>
 * 
 * @author Stefan Wertich, mapsonswen@web.de
 * @version 2.0.0, 13.08.2012
 * 
 */
public class DBPFContainer {

	private long majorVersion = 0;
	private long minorVersion = 0;
	private long dateCreated = 0;
	private long dateModified = 0;
	private long indexEntryCount = 0;
	private long indexOffsetLocation = 0;
	private long indexSize = 0;

	private File filename;
	private List<DBPFEntry> entryList;

	/**
	 * Constructor.<br>
	 */
	public DBPFContainer() {
		this.filename = new File("blank.dat");
		this.entryList = new ArrayList<DBPFEntry>(10);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("File: " + filename);
		sb.append("\n");
		sb.append("Version: " + majorVersion + "." + minorVersion);
		sb.append(", ");
		sb.append("Created: " + DBPFUtil2.formatDate(dateCreated));
		sb.append(", ");
		sb.append("Modified: " + DBPFUtil2.formatDate(dateModified));
		sb.append("\n");
		sb.append("IndexEntryCount: " + indexEntryCount);
		sb.append(", ");
		sb.append("IndexOffsetLocation: " + indexOffsetLocation);
		sb.append(", ");
		sb.append("IndexSize: " + indexSize);
		sb.append("\n");
		for (DBPFEntry element : entryList) {
			sb.append(element.toString());
			sb.append("\n");
		}
		return sb.toString();
	}

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
	 * @return the fileList
	 */
	public List<DBPFEntry> getEntryList() {
		return entryList;
	}

	/**
	 * @param fileList
	 *            the fileList to set
	 */
	public void setEntryList(Vector<DBPFEntry> fileList) {
		this.entryList = fileList;
	}

	/**
	 * Adds a DBPFEntry to the entryList.<br>
	 * 
	 * @param entry
	 *            The entry
	 */
	public void addEntry(DBPFEntry entry) {
		entryList.add(entry);
	}

	/**
	 * @return the version
	 */
	public long getMajorVersion() {
		return majorVersion;
	}

	/**
	 * @param majorVersion
	 *            the version to set
	 */
	public void setMajorVersion(long majorVersion) {
		this.majorVersion = majorVersion;
	}

	/**
	 * @return the archiveID
	 */
	public long getDateCreated() {
		return dateCreated;
	}

	/**
	 * @param dateCreated
	 *            the archiveID to set
	 */
	public void setDateCreated(long dateCreated) {
		this.dateCreated = dateCreated;
	}

	/**
	 * @return the archiveType
	 */
	public long getDateModified() {
		return dateModified;
	}

	/**
	 * @param dateModified
	 *            the archiveType to set
	 */
	public void setDateModified(long dateModified) {
		this.dateModified = dateModified;
	}

	/**
	 * @return the numberFiles
	 */
	public long getIndexEntryCount() {
		return indexEntryCount;
	}

	/**
	 * @param indexEntryCount
	 *            the numberFiles to set
	 */
	public void setIndexEntryCount(long indexEntryCount) {
		this.indexEntryCount = indexEntryCount;
	}

	/**
	 * @return the minorVersion
	 */
	public long getMinorVersion() {
		return minorVersion;
	}

	/**
	 * @param minorVersion
	 *            the minorVersion to set
	 */
	public void setMinorVersion(long minorVersion) {
		this.minorVersion = minorVersion;
	}

	/**
	 * @return the indexOffsetLocation
	 */
	public long getIndexOffsetLocation() {
		return indexOffsetLocation;
	}

	/**
	 * @param indexOffsetLocation
	 *            the indexOffsetLocation to set
	 */
	public void setIndexOffsetLocation(long indexOffsetLocation) {
		this.indexOffsetLocation = indexOffsetLocation;
	}

	/**
	 * @return the indexSize
	 */
	public long getIndexSize() {
		return indexSize;
	}

	/**
	 * @param indexSize
	 *            the indexSize to set
	 */
	public void setIndexSize(long indexSize) {
		this.indexSize = indexSize;
	}

}
