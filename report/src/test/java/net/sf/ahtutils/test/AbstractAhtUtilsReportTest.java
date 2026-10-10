package net.sf.ahtutils.test;

import org.jeesl.test.AbstractJeeslReportTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractAhtUtilsReportTest extends AbstractJeeslReportTest
{
	final static Logger logger = LoggerFactory.getLogger(AbstractAhtUtilsReportTest.class);
	
	@BeforeAll
	public static void initDir()
	{
		reportFileLocation="src/main/resources/reports.ahtutils-report/reports.xml";
		loggerConfigFile  ="log4junit.xml";
		loggerConfigPath  ="config.ahtutils-report.test";
		initFile();
	}
	
	@Test
	public void test() throws Exception
	{
		
	}
}