package ssp.dbpf;

import java.io.File;
import java.util.Vector;

import junit.framework.TestCase;

import org.junit.Test;

import ssp.dbpf.converter.PropertyConverter;
import ssp.dbpf.event.DBPFException;
import ssp.dbpf.io.DBPFReader;
import ssp.dbpf.io.DBPFWriter;
import ssp.dbpf.properties.DBPFProperty;
import ssp.dbpf.util.DBPFConstant;
import ssp.dbpf.util.DBPFUtil;
import ssp.xtools.io.XFileFilter;
import ssp.xtools.util.FileOp;

/**
 * Tests the DBPF properties.<br>
 * 
 * @author Stefan Wertich, mapsonswen@web.de
 * @version 2.0.0, 09.12.2012
 * 
 */
public class JUnitProperties extends TestCase {

	private File srcFolder = new File("testfiles" + File.separator + "properties");
	private File dstFolder = new File("testfiles" + File.separator + "properties"
			+ File.separator + "out");
	
	@Test
	public void testProperties() {
		if (!dstFolder.exists()) {
			assertEquals(true, dstFolder.mkdir());
		}
		
		TestUtil.printLine("TEST: Properties");
		TestUtil.printLine();
		Vector<File> filelist = new Vector<File>();
		XFileFilter xff = new XFileFilter("eqz");
		FileOp.listFiles(srcFolder, filelist, xff, false);

		for (File file : filelist) {
			try {
				check(file);
			} catch (DBPFException e) {
				fail(e.getMessage());
			}
		}
		
		assertEquals(filelist.size(), dstFolder.listFiles().length);
	}

	/**
	 * Checks a value.<br>
	 * 
	 * @param value
	 *            The value
	 * @throws DBPFException Thrown, if any error occur
	 */
	public void check(File file) throws DBPFException {
		TestUtil.printLine("> Read file: " + file);
		short[] rawData = DBPFReader.readRawData(file);
		DBPFProperty prop;
		if (FileOp.getBasename(file).endsWith("Text")) {
			prop = PropertyConverter.createProperty(DBPFUtil.getChars(rawData,
					0, rawData.length));
		} else {
			prop = PropertyConverter.createProperty(rawData, 0);
		}
		if (prop != null) {
			TestUtil.printLine(prop.toString());
			short[] data = PropertyConverter.createData(prop,
					DBPFConstant.FORMAT_TEXT);
			File fileout = new File(dstFolder, file.getName());
			DBPFWriter.writeRawData(fileout, data);
		} else {
			fail("Can not decode property for file: "+file);
		}
	}

}
