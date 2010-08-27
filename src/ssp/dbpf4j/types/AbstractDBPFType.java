package ssp.dbpf4j.types;

import ssp.dbpf4j.util.DBPFUtil;

/**
 * The abstract DBPFType.<br>
 * 
 * The TGI will be initialized with {0L, 0L, 0L}.
 * 
 * @author Stefan Wertich
 * @version 1.5.0, 24.08.2010
 * 
 */
public abstract class AbstractDBPFType implements DBPFType {

	protected long[] tgi;
	protected boolean compressed;
	protected long decompressedSize;

	/**
	 * Constructor.<br>
	 */
	public AbstractDBPFType() {
		tgi = new long[] { 0x0L, 0x0L, 0x0L };
	}

	@Override
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
		return sb.toString();
	}

	@Override
	public String toDetailString() {
		return toString();
	}

	@Override
	public abstract int getType();

	@Override
	public long[] getTGI() {
		return tgi;
	}

	@Override
	public void setTGI(long[] tgi) {
		this.tgi = tgi;
	}
	
	@Override
	public long getTID() {
		return tgi[0];
	}

	@Override
	public long getGID() {
		return tgi[1];
	}

	@Override
	public long getIID() {
		return tgi[2];
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
