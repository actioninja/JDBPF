package ssp.dbpf4j;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ssp.dbpf4j.types.DBPFType;

/**
 * Container to store DBPFTypes in a file.<br>
 * 
 * This is a simple alternative to DBPFFile. The container can easily be read by
 * DBPFReader, write by DBPFWriter and updated with DBPFUpdater.
 * 
 * The default filename is blank.dat.
 * 
 * @author Stefan Wertich
 * @version 1.6.0, 28.12.2010
 * 
 */
public class DBPFContainer {

	private File filename;
	private List<DBPFType> typeList;

	/**
	 * Constructor.<br>
	 */
	public DBPFContainer() {
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
