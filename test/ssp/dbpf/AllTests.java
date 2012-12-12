package ssp.dbpf;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Tests the whole DBPF package.<br>
 * 
 * Except the JUnitSimCity test cause of long time.<br>
 * 
 * @author Stefan Wertich, mapsonswen@web.de
 * @version 2.0.0, 09.12.2012
 *
 */
public class AllTests extends TestSuite {
	public static Test suite() {
		TestSuite mySuite = new TestSuite("JDBPF Test-Suite");
		mySuite.addTestSuite(ssp.dbpf.JUnitBasic.class);
		mySuite.addTestSuite(ssp.dbpf.JUnitExemplars.class);
		mySuite.addTestSuite(ssp.dbpf.JUnitFSH.class);
		mySuite.addTestSuite(ssp.dbpf.JUnitImages.class);
		mySuite.addTestSuite(ssp.dbpf.JUnitLots.class);
		mySuite.addTestSuite(ssp.dbpf.JUnitMisc.class);
		mySuite.addTestSuite(ssp.dbpf.JUnitProperties.class);
		return mySuite;
	}
}
