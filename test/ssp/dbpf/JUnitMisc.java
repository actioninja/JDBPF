package ssp.dbpf;

import java.io.File;
import java.util.Vector;

import junit.framework.TestCase;

import org.junit.Test;

import ssp.dbpf.converter.DBPFConverter;
import ssp.dbpf.converter.types.ExemplarConverter;
import ssp.dbpf.entries.DBPFEntry;
import ssp.dbpf.event.DBPFException;
import ssp.dbpf.io.DBPFReader;
import ssp.dbpf.io.DBPFWriter;
import ssp.dbpf.properties.DBPFProperty;
import ssp.dbpf.types.DBPFExemplar;
import ssp.dbpf.util.DBPFConstant;
import ssp.dbpf.util.DBPFUtil;
import ssp.dbpf.util.DBPFUtil2;
import ssp.xtools.io.XFileFilter;
import ssp.xtools.util.FileOp;

/**
 * Tests the DBPF misc.<br>
 * 
 * @author Stefan Wertich, mapsonswen@web.de
 * @version 2.0.0, 09.12.2012
 * 
 */
public class JUnitMisc extends TestCase {

	private File srcFolder = new File("testfiles" + File.separator + "misc");
	private File dstFolder = new File("testfiles" + File.separator + "misc"
			+ File.separator + "out");

	@Test
	public void testMisc() {
		if (!dstFolder.exists()) {
			assertEquals(true, dstFolder.mkdir());
		}

		TestUtil.printLine("TEST: Misc");
		TestUtil.printLine();
		Vector<File> filelist = new Vector<File>();
		XFileFilter xff = new XFileFilter("template");
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
	 * @throws DBPFException
	 *             Thrown, if any error occur
	 */
	public void check(File file) throws DBPFException {
		TestUtil.printLine("File: " + file);
		DBPFContainer db = DBPFReader.read(file);
		for (DBPFEntry entry : db.getEntryList()) {
			DBPFExemplar exem = DBPFConverter.createExemplar(entry);

			if (exem != null) {
				TestUtil.printLine(exem.toString());
				for (DBPFProperty prop : exem.getPropertyList()) {
					TestUtil.printLine(prop.toString());
					if (prop.getID() == DBPFUtil.toValue("0x491332ee", false)) {
						for (int i = 0; i < prop.getCount(); i++) {
							TestUtil.printLine("(" + i + ") "
									+ DBPFUtil2.getFloatValue(prop, i));
						}
					}
				}

				short[] data = ExemplarConverter.createData(exem,
						DBPFConstant.FORMAT_TEXT);
				// DBPFPackager packager = new DBPFPackager();
				// data = packager.compress(data);
				File fileout = new File(dstFolder, file.getName());
				DBPFWriter.writeRawData(fileout, data);
				//
				// // decompress
				// rawData = DBPFReader.readRawData(new File(
				// "temp/TestC.eqz"));
				// rawData = packager.decompress(rawData);
				// DBPFWriter.writeRawData(new File("temp/TestD.eqz"),
				// rawData);
			} else {
				TestUtil.printLine("No Exemplar: " + entry);
			}
		}
		TestUtil.printLine();
	}

}
