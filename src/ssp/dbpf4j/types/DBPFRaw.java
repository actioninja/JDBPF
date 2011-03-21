package ssp.dbpf4j.types;

import ssp.dbpf4j.tgi.TGIKeys;

/**
 * Defines simple RAW data of DBPF.<br>
 * 
 * This class only stores simple rawData and TGI but nothing more.<br>
 * The decompressedSize will be updated with setData. It is for all unknown or
 * not implemented types and could be compressed or not.
 * 
 * @author Stefan Wertich
 * @version 1.5.0, 24.08.2010
 * 
 */
public class DBPFRaw extends AbstractDBPFType {

	private short[] rawData;

	/**
	 * Constructor.<br>
	 */
	public DBPFRaw() {
		rawData = new short[0];
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString());
		sb.append("\n");
		sb.append("RawData-Size: " + rawData.length);
		return sb.toString();
	}

	/**
	 * Sets the data of the raw type.<br>
	 * This data are normally the rawData.
	 * 
	 * @param data
	 *            The data
	 */
	public void setData(short[] data) {
		this.rawData = data;
		setDecompressedSize(rawData.length);
	}

	/**
	 * Returns the data of the raw type.<br>
	 * This data is equivalent to the rawData.
	 * 
	 * @return The data
	 */
	public short[] getData() {
		return rawData;
	}

	@Override
	public int getType() {
		return TGIKeys.RAW.getFormatID();
	}
}
