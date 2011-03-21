package ssp.dbpf4j.types;

import ssp.dbpf4j.tgi.TGIKey;

/**
 * The abstract DBPFType.<br>
 * 
 * The TGI will be initialized with {0L, 0L, 0L}.
 * 
 * @author Stefan Wertich
 * @version 1.6.0, 29.12.2010
 * 
 */
public abstract class AbstractDBPFType implements DBPFType {

	protected TGIKey tgiKey;
	protected boolean compressed;
	protected long decompressedSize;

	/**
	 * Constructor.<br>
	 */
	public AbstractDBPFType() {
		tgiKey = new TGIKey();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("TGI: ");
		sb.append(tgiKey.toString());
		sb.append(",");
		sb.append("Compressed: ");
		sb.append(compressed);
		sb.append(",");
		sb.append("Decompressed Size: ");
		sb.append(decompressedSize);
		return sb.toString();
	}

	@Override
	public long getTID() {
		return getTGIKey().getTID();
	}

	@Override
	public long getGID() {
		return getTGIKey().getGID();
	}

	@Override
	public long getIID() {
		return getTGIKey().getIID();
	}

	@Override
	public abstract int getType();

	@Override
	public TGIKey getTGIKey() {
		return tgiKey;
	}

	@Override
	public void setTGIKey(TGIKey tgiKey) {
		this.tgiKey = tgiKey;
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
