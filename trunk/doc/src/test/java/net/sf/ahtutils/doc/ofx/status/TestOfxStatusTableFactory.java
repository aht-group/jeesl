package net.sf.ahtutils.doc.ofx.status;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import net.sf.ahtutils.controller.factory.ofx.lang.AbstractOfxStatusFactoryTest;
import net.sf.ahtutils.doc.UtilsDocumentation;
import net.sf.ahtutils.exception.processing.UtilsConfigurationException;
import net.sf.ahtutils.factory.xml.status.XmlDescriptionFactory;
import net.sf.ahtutils.factory.xml.status.XmlLangFactory;
import net.sf.ahtutils.test.AhtUtilsDocBootstrap;
import net.sf.ahtutils.xml.aht.Aht;
import net.sf.ahtutils.xml.status.Descriptions;
import net.sf.ahtutils.xml.status.Langs;
import net.sf.ahtutils.xml.status.Status;
import net.sf.ahtutils.xml.status.Translations;
import net.sf.exlp.util.xml.JaxbUtil;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.DefaultConfigurationBuilder;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openfuxml.content.table.Table;
import org.openfuxml.exception.OfxAuthoringException;
import org.openfuxml.renderer.latex.content.table.LatexGridTableRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestOfxStatusTableFactory extends AbstractOfxStatusFactoryTest
{
	final static Logger logger = LoggerFactory.getLogger(TestOfxStatusTableFactory.class);
	
	private static Configuration config;
	
	private OfxStatusTableFactory fOfx;
	private final String lang ="de";
	private Aht xmlStatus;
	private static Translations translations;
	
	@BeforeClass
	public static void initFiles() throws FileNotFoundException, ConfigurationException
	{
		fXml = new File(rootDir,"tableStatus.xml");
		fTxt = new File(rootDir,"tableStatus.tex");
		
		DefaultConfigurationBuilder builder = new DefaultConfigurationBuilder();
		config = builder.getConfiguration(false);
		config.setProperty(UtilsDocumentation.keyBaseDocDir, "target");
		
		translations = JaxbUtil.loadJAXB("data/xml/dummyTranslations.xml", Translations.class);
	}
	
	@Before
	public void init()
	{			
		super.initOfx();
		Status status = new Status();
		status.setCode("myCode");
		status.setLangs(new Langs());
		status.setDescriptions(new Descriptions());
		
		status.getLangs().getLang().add(XmlLangFactory.create(lang, "myLang"));
		status.getDescriptions().getDescription().add(XmlDescriptionFactory.create(lang, "myDescription"));
		
		xmlStatus = new Aht();
		xmlStatus.getStatus().add(status);
		
		fOfx = new OfxStatusTableFactory(config,lang,translations);
	}
	
	@Test
	public void testOfx() throws FileNotFoundException, UtilsConfigurationException
	{	
		Table actual = fOfx.toOfx(xmlStatus);
		saveXml(actual,fXml,false);
		Table expected = JaxbUtil.loadJAXB(fXml.getAbsolutePath(), Table.class);
		assertJaxbEquals(expected, actual);
	}
	
	@Test
	public void testLatex() throws OfxAuthoringException, IOException, UtilsConfigurationException
	{
		Table actual = fOfx.toOfx(xmlStatus);
		LatexGridTableRenderer renderer = new LatexGridTableRenderer(cmm,dsm);
		renderer.render(actual);
    	debug(renderer);
    	save(renderer,fTxt);
    	assertText(renderer,fTxt);
	}
	
	public static void main(String[] args) throws Exception
    {
		AhtUtilsDocBootstrap.init();
		
		TestOfxStatusTableFactory.initFiles();
		TestOfxStatusTableFactory test = new TestOfxStatusTableFactory();
		test.setSaveReference(true);
		test.init();
	
		test.testOfx();
		test.testLatex();
    }
}