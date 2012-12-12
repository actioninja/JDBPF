package ssp.dbpf;

import junit.framework.TestCase;

import org.junit.Test;

import ssp.dbpf.properties.DBPFProperties;
import ssp.dbpf.util.DBPFUtil;

/**
 * Tests the DBPF with some basics.<br>
 * 
 * @author Stefan Wertich, mapsonswen@web.de
 * @version 0.0.1, 10.11.2012
 * 
 */
public class JUnitBasic extends TestCase {

	@Test
	public void testZero() {
		TestUtil.printLine("TEST: Zero");
		TestUtil.printLine();
		check(0L, "00000000");
	}

	@Test
	public void testNegative() {
		TestUtil.printLine("TEST: Negative");
		TestUtil.printLine();
		check(-1L, "ffffffff");
	}

	@Test
	public void testProp() {
		TestUtil.printLine("TEST: Prop");
		TestUtil.printLine();
		check(DBPFProperties.BULLDOZE_COST.getId(), "099afacd");
	}

	@Test
	public void testPropNegative() {
		TestUtil.printLine("TEST: Prop Negative");
		TestUtil.printLine();
		check(-DBPFProperties.BULLDOZE_COST.getId(), "f6650533");
	}

	/**
	 * Checks a value.<br>
	 * 
	 * @param value The value
	 */
	public void check(long value, String hex) {
		TestUtil.printLine("Use test Value: " + value);
		TestUtil.printLine("as Binary: " + Long.toBinaryString(value));
		String hexString = DBPFUtil.toHex(value, 8);
		TestUtil.printLine("as HEX: " + hexString);		
		assertEquals(hex, hexString);

		int size = 20; // 128*128*128;
		int length = 5;
		TestUtil.printLine("Array Size: " + size + " with Value length: " + length);

		TestUtil.printLine("Test with old setUint32 method...");
		short[] data = new short[size];
		long timeA = System.currentTimeMillis();
		for (int i = 0; i < size / length; i++) {
			DBPFUtil.setUint32(value, data, length * i, length);
		}
		long timeA2 = System.currentTimeMillis() - timeA;
		TestUtil.printData(data);

		TestUtil.printLine("Test with new setInt32 method...");
		short[] data2 = new short[size];
		long timeB = System.currentTimeMillis();
		for (int i = 0; i < size / length; i++) {
			DBPFUtil.setInt32(value, data2, length * i, length);
		}
		long timeB2 = System.currentTimeMillis() - timeB;
		TestUtil.printData(data2);

		// give different time
		TestUtil.printLine("setUint32 = " + timeA2 + " msec, setInt32 = " + timeB2
				+ " msec");

		TestUtil.printLine("Read value from array with getSint32:");
		long val = DBPFUtil.getSint32(data2, 0, length);
		TestUtil.printLine("Read Value: " + val);
		assertEquals(value, val);
		TestUtil.printLine();
	}

}
