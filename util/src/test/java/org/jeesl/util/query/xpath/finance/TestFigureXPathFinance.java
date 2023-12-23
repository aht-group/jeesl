package org.jeesl.util.query.xpath.finance;

import org.jeesl.AbstractJeeslUtilTest;
import org.jeesl.model.xml.domain.finance.TestXmlFigures;
import org.jeesl.model.xml.domain.finance.TestXmlFinance;
import org.jeesl.model.xml.module.finance.Figures;
import org.jeesl.model.xml.module.finance.Finance;
import org.jeesl.util.query.xpath.FiguresXpath;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.exception.ExlpXpathNotFoundException;
import net.sf.exlp.exception.ExlpXpathNotUniqueException;

public class TestFigureXPathFinance extends AbstractJeeslUtilTest
{
	final static Logger logger = LoggerFactory.getLogger(TestFigureXPathFinance.class);
    
	private Figures figures;
	private Finance f1,f2,f3;
	
	@Before
	public void iniDbseed()
	{
		figures = TestXmlFigures.create(false);

		f1 = TestXmlFinance.create(false);f1.setCode("ok"); figures.getFinance().add(f1);
		f2 = TestXmlFinance.create(false);f2.setCode("multi"); figures.getFinance().add(f2);
		f3 = TestXmlFinance.create(false);f3.setCode("multi"); figures.getFinance().add(f3);
	}
	
	@Test
	public void find() throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	{
		Finance actual = FiguresXpath.getFinance(figures, f1.getCode());
	    Assert.assertEquals(f1,actual);
	}

	@Test(expected=ExlpXpathNotFoundException.class)
	public void testNotFound() throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	{
		FiguresXpath.getFinance(figures, "-1");
	}
	
	 @Test(expected=ExlpXpathNotUniqueException.class)
	 public void testUnique() throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	 {
		 FiguresXpath.getFinance(figures, f2.getCode());
	 }
}