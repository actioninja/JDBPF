package ssp.dbpf4j;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import ssp.dbpf4j.entries.DBPFEntry;
import ssp.dbpf4j.util.DBPFUtil;

/**
 * Defines a DBPF file.<br>
 * 
 * @author Stefan Wertich
 * @version 1.6.0, 07.01.2011
 * 
 */
public class DBPFFile {

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
	public DBPFFile() {
		this.filename = new File("blank.dat");
		this.entryList = new ArrayList<DBPFEntry>(10);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("File: " + filename);
		sb.append("\n");
		sb.append("Version: " + majorVersion + "." + minorVersion);
		sb.append(", ");
		sb.append("Created: " + DBPFUtil.formatDate(dateCreated));
		sb.append(", ");
		sb.append("Modified: " + DBPFUtil.formatDate(dateModified));
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
