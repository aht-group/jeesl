package net.sf.ahtutils.report.renderer;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.report.exception.ReportException;
import net.sf.ahtutils.test.AbstractAhtUtilsReportTest;
import net.sf.exlp.exception.ExlpXpathNotFoundException;
import net.sf.exlp.exception.ExlpXpathNotUniqueException;

public class TestReportRenderer extends AbstractAhtUtilsReportTest
{
	final static Logger logger = LoggerFactory.getLogger(TestReportRenderer.class);
    
//	@Before
	public void initExample() throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException, FileNotFoundException, ReportException
	{
		initHandler();
		initExample("testReport");
	}
	
	@Test public void dummy() {}
	
//	@Test
	public void emptyPages() throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException, ReportException, IOException
	{
		createPdf();
		writePdf();
		assertEmptyPage(pdf.toByteArray());	    
	}
			
	public static void main(String[] args) throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException, ReportException, IOException
    {
		initLogger();
		TestReportRenderer test = new TestReportRenderer();
		test.initHandler();
		test.initExample("testReportChart");
		test.createPdf();
		test.writePdf();
    }
}