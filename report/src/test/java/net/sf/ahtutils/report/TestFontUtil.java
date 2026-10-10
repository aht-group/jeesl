
package net.sf.ahtutils.report;

import java.io.IOException;

import net.sf.ahtutils.report.exception.ReportException;
import net.sf.ahtutils.test.AbstractAhtUtilsReportTest;

import org.jdom2.JDOMException;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestFontUtil extends AbstractAhtUtilsReportTest
{
	final static Logger logger = LoggerFactory.getLogger(TestFontUtil.class);
    		
	@Test
	public void fontUtil() throws ReportException, IOException, JDOMException
	{
		ReportUtilFonts fontUtil = new ReportUtilFonts(reports);
		fontUtil.replaceAll("SansSerif", "Arial", false);
	}
	
}