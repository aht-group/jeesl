package org.jeesl.util.query.xpath.report;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jeesl.AbstractJeeslUtilTest;
import org.jeesl.factory.xml.system.lang.XmlLangsFactory;
import org.jeesl.model.xml.io.locale.status.Langs;
import org.jeesl.util.query.xpath.ReportXpath;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.exception.ExlpXpathNotFoundException;

public class TestReportXpathSerializeable extends AbstractJeeslUtilTest
{
	final static Logger logger = LoggerFactory.getLogger(TestReportXpathSerializeable.class);
    
	private List<Serializable> list;
	private Langs langs;
	
	@Before
	public void init()
	{
		list = new ArrayList<>();
		langs = XmlLangsFactory.build();
	}
	
	@Test
	public void langs() throws ExlpXpathNotFoundException
	{
		list.add("a");
		list.add(langs);
		list.add("b");
		
		Langs actual = ReportXpath.getFirstLangs(list);
		Assert.assertEquals(langs,actual);
	}

	@Test( expected = ExlpXpathNotFoundException.class )
	public void langsNotFound() throws ExlpXpathNotFoundException
	{
		list.add("a");
		list.add("b");
		
		Langs actual = ReportXpath.getFirstLangs(list);
		Assert.assertEquals(langs,actual);
	}
}