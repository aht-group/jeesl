package org.jeesl.doc.ofx.security;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.exlp.interfaces.system.property.Configuration;
import org.exlp.util.jx.JaxbUtil;
import org.jeesl.model.xml.io.locale.status.Translations;
import org.jeesl.model.xml.system.security.TestXmlView;
import org.jeesl.model.xml.system.security.View;
import org.jeesl.model.xml.system.security.Views;
import org.jeesl.model.xml.system.status.TestXmlDescription;
import org.jeesl.model.xml.system.status.TestXmlLang;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openfuxml.exception.OfxAuthoringException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.doc.ofx.security.table.OfxSecurityViewTableFactory;
import net.sf.ahtutils.test.AhtUtilsDocBootstrap;

public class TestOfxViewTableFactory extends AbstractOfxSecurityFactoryTest
{
	final static Logger logger = LoggerFactory.getLogger(TestOfxViewTableFactory.class);
	
	private static Configuration config;
	private static Translations translations;
	
	private OfxSecurityViewTableFactory fOfx;
	private final String[] langs = {"de"};
	
	private static List<String> headerKeys;
	
	@BeforeAll
	public static void initFiles() throws FileNotFoundException
	{
		headerKeys = new ArrayList<String>();
		headerKeys.add("key1");
		headerKeys.add("key2");
		
		fXml = new File(rootDir,"tableView.xml");
		fTxt = new File(rootDir,"tableView.tex");
		
		translations = JaxbUtil.loadJAXB("src/test/resources/data/xml/dummyTranslations.xml", Translations.class);
	}
	
	@BeforeEach
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
	
	@Test @Disabled
	public void testOfx() throws FileNotFoundException
	{
/*		Views views = createViews();
		
		Table actual = fOfx.toOfx(views.getView(),headerKeys);
		saveXml(actual,fXml,false);
		Table expected = JaxbUtil.loadJAXB(fXml.getAbsolutePath(), Table.class);
		assertJaxbEquals(expected, actual);
*/	}
	
	@Test @Disabled
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