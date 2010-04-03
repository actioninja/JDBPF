package ssp.dbpf4j.types;

import ssp.dbpf4j.util.DBPFUtil;

/**
 * Defines a PNG of DBPF.<br>
 * 
 * The PNG is a open image format and the data stored as imageData.<br>
 * The decompressedSize is updated with setImageData. Although the imageData
 * might be compressed, it could be compressed with QFS.
 * 
 * @author Stefan Wertich
 * @version 0.9.1.23, 23.01.2009
 * 
 */
public class DBPFPNG implements DBPFType {

	private long[] tgi;
	private short[] imageData;
	private boolean compressed;
	private long decompressedSize;

	/**
	 * Constructor.<br>
	 */
	public DBPFPNG() {
		tgi = new long[0];
		imageData = new short[0];
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
		sb.append("ImageData-Size: " + imageData.length);
		return sb.toString();
	}

	public String toDetailString() {
		return toString();
	}

	/**
	 * Sets the imageData of the PNG.<br>
	 * 
	 * @param imageData
	 *            The data
	 */
	public void setImageData(short[] imageData) {
		this.imageData = imageData;
		setDecompressedSize(imageData.length);
	}

	/**
	 * Returns the imageData of the PNG.<br>
	 * 
	 * @return The data
	 */
	public short[] getImageData() {
		return imageData;
	}

	@Override
	public int getType() {
		return DBPFTypes.PNG;
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
