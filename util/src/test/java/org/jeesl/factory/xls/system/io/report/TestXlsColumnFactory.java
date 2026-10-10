package org.jeesl.factory.xls.system.io.report;

import org.jeesl.AbstractJeeslUtilTest;
import org.jeesl.factory.xlsx.io.report.XlsColumnFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXlsColumnFactory extends AbstractJeeslUtilTest
{
	final static Logger logger = LoggerFactory.getLogger(TestXlsColumnFactory.class);
		
	@Test public void code2index()
    {	
		Assertions.assertEquals(0,XlsColumnFactory.code2index("A"));
		Assertions.assertEquals(25,XlsColumnFactory.code2index("Z"));
		Assertions.assertEquals(188,XlsColumnFactory.code2index("GG"));
    }
	
	@Test public void index2code()
    {	
		Assertions.assertEquals("A",XlsColumnFactory.index2code(0));
		Assertions.assertEquals("Z",XlsColumnFactory.index2code(25));
		Assertions.assertEquals("GG",XlsColumnFactory.index2code(188));
    }
}