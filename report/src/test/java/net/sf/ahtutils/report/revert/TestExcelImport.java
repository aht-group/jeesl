package net.sf.ahtutils.report.revert;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.report.revert.excel.DummyEntity;
import net.sf.ahtutils.report.revert.excel.importers.ExcelSimpleSerializableImporter;
import net.sf.ahtutils.test.AbstractAhtUtilsReportTest;
import net.sf.ahtutils.test.AhtUtilsReportBootstrap;

public class TestExcelImport extends AbstractAhtUtilsReportTest {
	
	final static Logger logger = LoggerFactory.getLogger(TestExcelImport.class);

	private String filename = "src/test/resources/data/xlsx/importTest.xlsx";
	
	
	//@Ignore
	@Test
	public void test() throws Exception {
		
		// Initialize Logging
		AhtUtilsReportBootstrap.init();
		
		// Initialize the importer
		ExcelSimpleSerializableImporter statusImporter = ExcelSimpleSerializableImporter.factory(new AhtUtilsXlsDefinitionResolver(), "testReport", filename);
		
		// Select the first sheet in Excel file to be the active one
		statusImporter.selectFirstSheet();
		
		// Show the content of the first row (where column titles should be)
		statusImporter.debugHeader();
		
		// Show first after header (2nd) where first data should be found
		statusImporter.debugFirstRow();
		
		// Let the importer set the given column values to entity properties and get a list of entities for all rows
		Map<Object, List<String>> importedEntities = statusImporter.execute(true);
		
		// Specify how to format dates in debug output
		DateFormat df = SimpleDateFormat.getDateInstance();
		System.out.println("Size " +importedEntities.size());
		// Debug the entity properties
		for (Object importedEntity : importedEntities.keySet())
		{
			DummyEntity entity = (DummyEntity) importedEntity;
		//	logger.info("Imported ValueString Property = " +entity.getValueString());
		//	logger.info("Imported ValueDouble Property = " +entity.getValueDouble());
		//	logger.info("Imported ValueDate   Property = " +df.format(entity.getValueDate()));
			System.out.println("Imported Code Property = " +entity.getCode());
		}
		
	}

}
