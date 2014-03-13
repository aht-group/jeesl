package net.sf.ahtutils.xml.xpath.finance;

import net.sf.ahtutils.test.AbstractXmlTest;
import net.sf.ahtutils.xml.finance.Figures;
import net.sf.ahtutils.xml.finance.Finance;
import net.sf.ahtutils.xml.finance.TestXmlFigures;
import net.sf.ahtutils.xml.finance.TestXmlFinance;
import net.sf.ahtutils.xml.xpath.FiguresXpath;
import net.sf.exlp.exception.ExlpXpathNotFoundException;
import net.sf.exlp.exception.ExlpXpathNotUniqueException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestFigureXPathFinance extends AbstractXmlTest
{
	final static Logger logger = LoggerFactory.getLogger(TestFigureXPathFinance.class);
    
	private Figures figures;
	private Finance f1,f2,f3;
	
	@Before
	public void iniDbseed()
	{
		figures = TestXmlFigures.create(false);

		f1 = TestXmlFinance.create(false);f1.setCode("ok");figures.getFinance().add(f1);
		f2 = TestXmlFinance.create(false);f2.setCode("multi");figures.getFinance().add(f2);
		f3 = TestXmlFinance.create(false);f3.setCode("multi");figures.getFinance().add(f3);
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