package org.jeesl.factory.xls.system.io.report;

import org.jeesl.AbstractJeeslUtilTest;
import org.jeesl.factory.xlsx.io.report.XlsColumnFactory;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXlsColumnFactory extends AbstractJeeslUtilTest
{
	final static Logger logger = LoggerFactory.getLogger(TestXlsColumnFactory.class);
		
	@Test public void code2index()
    {	
		Assert.assertEquals(0,XlsColumnFactory.code2index("A"));
		Assert.assertEquals(25,XlsColumnFactory.code2index("Z"));
		Assert.assertEquals(188,XlsColumnFactory.code2index("GG"));
    }
	
	@Test public void index2code()
    {	
		Assert.assertEquals("A",XlsColumnFactory.index2code(0));
		Assert.assertEquals("Z",XlsColumnFactory.index2code(25));
		Assert.assertEquals("GG",XlsColumnFactory.index2code(188));
    }
}