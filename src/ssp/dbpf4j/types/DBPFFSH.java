package ssp.dbpf4j.types;

import ssp.dbpf4j.util.DBPFUtil;

/**
 * Defines a FSH of DBPF.<br>
 * 
 * version 1.1.0: Only stores the rawData like DBPFRaw
 * The decompressedSize will be updated with setData.
 * 
 * @author Stefan Wertich
 * @version 1.1.0, 26.02.2009
 * 
 */
public class DBPFFSH implements DBPFType {

	private long[] tgi;
	private short[] rawData;
	private boolean compressed;
	private long decompressedSize;

	/**
	 * Constructor.<br>
	 */
	public DBPFFSH() {
		tgi = new long[0];
		rawData = new short[0];
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("TGI: ");
		sb.append(DBPFUtil.toHex(tgi[0], 8));
		sb.append(",");
		sb.append(DBPFUtil.toHex(tgi[1], 8));
		sb.append(",");
		sb.append(DBPFUtil.toHex(tgi[2], 8));
		sb.append(",");
		sb.append("Compressed: ");
		sb.append(compressed);
		sb.append(", ");
		sb.append("RawData-Size: " + rawData.length);
		return sb.toString();
	}

	public String toDetailString() {
		return toString();
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
		return DBPFTypes.RAW;
	}

	@Override
	public long[] getTGI() {
		return tgi;
	}

	@Override
	public void setTGI(long[] tgi) {
		this.tgi = tgi;
	}

	@Override
	public boolean isCompressed() {
		return compressed;
	}

	@Override
	public void setCompressed(boolean compressed) {
		this.compressed = compressed;
	}

	@Override
	public long getDecompressedSize() {
		return decompressedSize;
	}

	@Override
	public void setDecompressedSize(long decompressedSize) {
		this.decompressedSize = decompressedSize;
	}
}
