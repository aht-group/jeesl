package net.sf.ahtutils.controller.factory.ofx.lang;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import net.sf.ahtutils.controller.factory.latex.TestLatexStatusFactory;
import net.sf.ahtutils.controller.factory.ofx.status.OfxLangStatisticTableFactory;
import net.sf.ahtutils.model.pojo.status.TranslationStatistic;
import net.sf.ahtutils.test.AhtUtilsTstBootstrap;
import net.sf.ahtutils.xml.status.Translations;
import net.sf.exlp.util.xml.JaxbUtil;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openfuxml.content.ofx.table.Table;
import org.openfuxml.exception.OfxAuthoringException;
import org.openfuxml.renderer.processor.latex.content.table.LatexGridTableRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestOfxLangStatisticTableFactory extends AbstractOfxStatusFactoryTest
{
	final static Logger logger = LoggerFactory.getLogger(TestOfxLangStatisticTableFactory.class);
	
	private OfxLangStatisticTableFactory fOfx;
	private final String lang ="de";
	private static Translations translations;
	private String[] headerKeys = {"key1","key1"};
	private List<TranslationStatistic> lStats;
	
	@BeforeClass
	public static void initFiles() throws FileNotFoundException
	{
		fXml = new File(rootDir,"tableView.xml");
		fTxt = new File(rootDir,"tableView.tex");
		
		translations = JaxbUtil.loadJAXB("src/test/resources/data/xml/dummyTranslations.xml", Translations.class);
	}
	
	@Before
	public void init()
	{	
		fOfx = new OfxLangStatisticTableFactory(lang, translations);
		
		TestLatexStatusFactory tLs = TestLatexStatusFactory.factory();
		lStats = tLs.createStatistic();
	}
	
	@Test
	public void testOfx() throws FileNotFoundException
	{		
		Table actual = fOfx.toOfx(lStats,headerKeys);
		saveXml(actual,fXml,false);
		Table expected = JaxbUtil.loadJAXB(fXml.getAbsolutePath(), Table.class);
		assertJaxbEquals(expected, actual);
	}
	
	@Test
	public void testLatex() throws OfxAuthoringException, IOException
	{
		Table actual = fOfx.toOfx(lStats,headerKeys);
		LatexGridTableRenderer renderer = new LatexGridTableRenderer();
		renderer.render(actual);
    	debug(renderer);
    	save(renderer,fTxt);
    	assertText(renderer,fTxt);
	}
	
	public static void main(String[] args) throws Exception
    {
		AhtUtilsTstBootstrap.init();
		
		TestOfxLangStatisticTableFactory.initFiles();
		TestOfxLangStatisticTableFactory test = new TestOfxLangStatisticTableFactory();
		test.setSaveReference(true);
		test.init();
	
		test.testOfx();
		test.testLatex();
    }
}