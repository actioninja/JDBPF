package ssp.dbpf;

import java.io.File;
import java.util.Vector;

import junit.framework.TestCase;

import org.junit.Test;

import ssp.dbpf.converter.types.ExemplarConverter;
import ssp.dbpf.event.DBPFException;
import ssp.dbpf.io.DBPFReader;
import ssp.dbpf.io.DBPFWriter;
import ssp.dbpf.properties.DBPFProperty;
import ssp.dbpf.types.DBPFExemplar;
import ssp.dbpf.util.DBPFConstant;
import ssp.xtools.io.XFileFilter;
import ssp.xtools.util.FileOp;

/**
 * Tests the DBPF exemplars.<br>
 * 
 * @author Stefan Wertich, mapsonswen@web.de
 * @version 2.0.0, 09.12.2012
 * 
 */
public class JUnitExemplars extends TestCase {

	private File srcFolder = new File("testfiles" + File.separator
			+ "exemplars");
	private File dstFolder = new File("testfiles" + File.separator
			+ "exemplars" + File.separator + "out");

	@Test
	public void testExemplars() {
		if (!dstFolder.exists()) {
			assertEquals(true, dstFolder.mkdir());
		}

		TestUtil.printLine("TEST: Exemplars");
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
	 * Checks the file.<br>
	 * 
	 * @param file
	 *            The file
	 * @throws DBPFException Thrown, if any error occur
	 */
	public void check(File file) throws DBPFException {
		TestUtil.printLine("---------------------------");
		TestUtil.printLine("File: " + file);
		short[] rawData = DBPFReader.readRawData(file);
		DBPFExemplar exem = ExemplarConverter.createType(rawData);
		if (exem != null) {
			TestUtil.printLine(exem.toString());
			for (DBPFProperty prop : exem.getPropertyList()) {
				TestUtil.printLine(prop.toString());
			}

			short[] data = ExemplarConverter.createData(exem,
					DBPFConstant.FORMAT_TEXT);
			// DBPFPackager packager = new DBPFPackager();
			// data = packager.compress(data);
			File fileout = new File(dstFolder, file.getName() + ".out");
			DBPFWriter.writeRawData(fileout, data);
			//
			// // decompress
			// rawData = DBPFReader.readRawData(new File(
			// "temp/TestC.eqz"));
			// rawData = packager.decompress(rawData);
			// DBPFWriter.writeRawData(new File("temp/TestD.eqz"), rawData);
		} else {
			fail("Can not decode Exemplar for: " + file);
		}
	}

}
