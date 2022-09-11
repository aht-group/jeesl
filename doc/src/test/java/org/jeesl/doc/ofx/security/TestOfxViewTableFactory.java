package org.jeesl.doc.ofx.security;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.DefaultConfigurationBuilder;
import org.jeesl.model.xml.system.security.TestXmlView;
import org.jeesl.model.xml.system.security.View;
import org.jeesl.model.xml.system.security.Views;
import org.jeesl.model.xml.system.status.TestXmlDescription;
import org.jeesl.model.xml.system.status.TestXmlLang;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.openfuxml.exception.OfxAuthoringException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.doc.ofx.security.table.OfxSecurityViewTableFactory;
import net.sf.ahtutils.test.AhtUtilsDocBootstrap;
import net.sf.ahtutils.xml.status.Translations;
import net.sf.exlp.util.xml.JaxbUtil;

public class TestOfxViewTableFactory extends AbstractOfxSecurityFactoryTest
{
	final static Logger logger = LoggerFactory.getLogger(TestOfxViewTableFactory.class);
	
	private static Configuration config;
	private static Translations translations;
	
	private OfxSecurityViewTableFactory fOfx;
	private final String[] langs = {"de"};
	
	private static List<String> headerKeys;
	
	@BeforeClass
	public static void initFiles() throws FileNotFoundException, ConfigurationException
	{
		headerKeys = new ArrayList<String>();
		headerKeys.add("key1");
		headerKeys.add("key2");
		
		fXml = new File(rootDir,"tableView.xml");
		fTxt = new File(rootDir,"tableView.tex");
		
		DefaultConfigurationBuilder builder = new DefaultConfigurationBuilder();
		config = builder.getConfiguration(false);
		translations = JaxbUtil.loadJAXB("src/test/resources/data/xml/dummyTranslations.xml", Translations.class);
	}
	
	@Before
	public void init()
	{	
		super.initOfx();
		fOfx = new OfxSecurityViewTableFactory(config,langs, translations);
	}
	
	private Views createViews()
	{
		Views views = new Views();
		
		View view = TestXmlView.create(true);
		view.getLangs().getLang().add(TestXmlLang.create(false,langs[0],"View xyz"));
		view.getDescriptions().getDescription().add(TestXmlDescription.create(false,langs[0],"This view is for testing purposes only."));
		
		views.getView().add(view);
		return views;
	}
	
	@Test @Ignore
	public void testOfx() throws FileNotFoundException
	{
/*		Views views = createViews();
		
		Table actual = fOfx.toOfx(views.getView(),headerKeys);
		saveXml(actual,fXml,false);
		Table expected = JaxbUtil.loadJAXB(fXml.getAbsolutePath(), Table.class);
		assertJaxbEquals(expected, actual);
*/	}
	
	@Test @Ignore
	public void testLatex() throws OfxAuthoringException, IOException
	{
/*		Table actual = fOfx.toOfx(createViews().getView(),headerKeys);
		LatexGridTableRenderer renderer = new LatexGridTableRenderer(cmm,dsm);
		renderer.render(actual);
    	debug(renderer);
    	save(renderer,fTxt);
    	assertText(renderer,fTxt);
*/	}
	
	public static void main(String[] args) throws Exception
    {
		AhtUtilsDocBootstrap.init();
		
		TestOfxViewTableFactory.initFiles();
		TestOfxViewTableFactory test = new TestOfxViewTableFactory();
		test.setSaveReference(true);
		test.init();
	
		test.testOfx();
		test.testLatex();
    }
}