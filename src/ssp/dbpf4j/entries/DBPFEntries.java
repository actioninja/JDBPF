package ssp.dbpf4j.entries;

import ssp.dbpf4j.util.DBPFUtil2;

/**
 * Stores the definitions of the DBPFEntries.<br>
 * 
 * @author Stefan Wertich
 * @version 1.2.4, 08.09.2009
 * 
 */
public class DBPFEntries {

	/**
	 * Returns a string for this TGI.<br>
	 * 
	 * First check the WAV type, then the LTEXT type, to prevents errors.
	 * 
	 * @param tgi
	 *            The TGI
	 * @return The string or UNKNOWN, if TGI not found
	 */
	public static String getString(long[] tgi) {
		String type = "UNKNOWN";
		if (DBPFUtil2.isTGI(tgi, DBPFEntries.DIRECTORY)) {
			type = "DIR";
		} else if (DBPFUtil2.isTGI(tgi, DBPFEntries.LD)) {
			type = "LD";
		} else if (DBPFUtil2.isTGI(tgi, DBPFEntries.EXEMPLAR)) {
			type = "EXEMPLAR";
		} else if (DBPFUtil2.isTGI(tgi, DBPFEntries.PNG)) {
			type = "PNG";
		} else if (DBPFUtil2.isTGI(tgi, DBPFEntries.PNG_ICON)) {
			type = "PNG (Icon)";
		} else if (DBPFUtil2.isTGI(tgi, DBPFEntries.LUA)) {
			type = "LUA";
		} else if (DBPFUtil2.isTGI(tgi, DBPFEntries.WAV)) {
			type = "WAV";
		} else if (DBPFUtil2.isTGI(tgi, DBPFEntries.LTEXT)) {
			type = "LTEXT";
		} else if (DBPFUtil2.isTGI(tgi, DBPFEntries.RUL)) {
			type = "RUL";
		} else if (DBPFUtil2.isTGI(tgi, DBPFEntries.FSH)) {
			type = "FSH";
		} else if (DBPFUtil2.isTGI(tgi, DBPFEntries.S3D)) {
			type = "S3D";
		}
		return type;
	}

	/**
	 * Directory file
	 */
	public static final long[] DIRECTORY = new long[] { 0xe86b1eefL,
			0xe86b1eefL, 0x286b1f03L };

	/**
	 * LD file
	 */
	public static final long[] LD = new long[] { 0x6be74c60L, 0x6be74c60L, -1L };

	/**
	 * Exemplar file: LotInfo, LotConfig
	 */
	public static final long[] EXEMPLAR = new long[] { 0x6534284aL, -1L, -1L };

	/**
	 * PNG file (image, icon)
	 */
	public static final long[] PNG = new long[] { 0x856ddbacL, -1L, -1L };

	/**
	 * PNG: Menu building icons, bridges, overlays
	 */
	public static final long[] PNG_ICON = new long[] { 0x856ddbacL,
			0x6a386d26L, -1L };

	/**
	 * FSH
	 */
	public static final long[] FSH = new long[] { 0x7ab50e44L, -1L, -1L };

	/**
	 * S3D
	 */
	public static final long[] S3D = new long[] { 0x5ad0e817L, -1L, -1L };

	/**
	 * LUA file: Missions, Advisors, Tutorials and Packaging files
	 */
	public static final long[] LUA = new long[] { 0xca63e2a3L, 0x4a5e8ef6L, -1L };

	/**
	 * LUA file: Generators, Attractors, Repulsors and System LUA
	 */
	public static final long[] LUA_GEN = new long[] { 0xca63e2a3L, 0x4a5e8f3fL,
			-1L };

	/**
	 * RUL: Network rules
	 */
	public static final long[] RUL = new long[] { 0x0a5bcf4bL, 0xaa5bcf57L, -1L };

	/**
	 * LTEXT or WAV
	 */
	public static final long[] LTEXT = new long[] { 0x2026960bL, -1L, -1L };

	/**
	 * WAV
	 */
	public static final long[] WAV = new long[] { 0x2026960bL, 0xaa4d1933L, -1L };

}