package net.sf.ahtutils.controller.factory.ofx.lang;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.exlp.util.jx.JaxbUtil;
import org.jeesl.model.pojo.io.locale.TranslationStatistic;
import org.jeesl.model.xml.io.locale.status.Translations;
import org.jeesl.test.JeeslDocBootstrap;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openfuxml.exception.OfxAuthoringException;
import org.openfuxml.model.xml.core.table.Table;
import org.openfuxml.renderer.latex.content.table.LatexGridTableRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.doc.latex.writer.TestLatexTranslationStatFactory;
import net.sf.ahtutils.doc.ofx.status.OfxLangStatisticTableFactory;

public class TestOfxLangStatisticTableFactory extends AbstractOfxStatusFactoryTest
{
	final static Logger logger = LoggerFactory.getLogger(TestOfxLangStatisticTableFactory.class);
	
	private OfxLangStatisticTableFactory fOfx;
	private final String lang ="de";
	private static Translations translations;
	private String[] headerKeys = {"key1","key1"};
	private List<TranslationStatistic> lStats;
	
	@BeforeAll
	public static void initFiles() throws FileNotFoundException
	{
		fXml = new File(rootDir,"tableView.xml");
		fTxt = new File(rootDir,"tableView.tex");
		
		translations = JaxbUtil.loadJAXB("src/test/resources/data/xml/dummyTranslations.xml", Translations.class);
	}
	
	@BeforeEach
	public void init()
	{	
		super.initOfx();
		fOfx = new OfxLangStatisticTableFactory(lang, translations);
		
		TestLatexTranslationStatFactory tLs = TestLatexTranslationStatFactory.factory();
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
		LatexGridTableRenderer renderer = new LatexGridTableRenderer(cp);
		renderer.render(actual);
    	debug(renderer);
    	save(renderer,fTxt);
    	assertText(renderer,fTxt);
	}
	
	public static void main(String[] args) throws Exception
    {
		JeeslDocBootstrap.init();
		
		TestOfxLangStatisticTableFactory.initFiles();
		TestOfxLangStatisticTableFactory test = new TestOfxLangStatisticTableFactory();
		test.setSaveReference(true);
		test.init();
	
		test.testOfx();
		test.testLatex();
    }
}