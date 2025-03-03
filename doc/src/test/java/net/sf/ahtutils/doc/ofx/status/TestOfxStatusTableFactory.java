package net.sf.ahtutils.doc.ofx.status;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.exlp.interfaces.system.property.Configuration;
import org.exlp.util.jx.JaxbUtil;
import org.jeesl.exception.processing.UtilsConfigurationException;
import org.jeesl.factory.xml.system.lang.XmlDescriptionFactory;
import org.jeesl.factory.xml.system.lang.XmlLangFactory;
import org.jeesl.model.xml.io.locale.status.Descriptions;
import org.jeesl.model.xml.io.locale.status.Langs;
import org.jeesl.model.xml.io.locale.status.Status;
import org.jeesl.model.xml.io.locale.status.Translations;
import org.jeesl.test.JeeslBootstrap;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openfuxml.exception.OfxAuthoringException;
import org.openfuxml.model.xml.core.table.Table;
import org.openfuxml.renderer.latex.content.table.LatexGridTableRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.controller.factory.ofx.lang.AbstractOfxStatusFactoryTest;
import net.sf.ahtutils.xml.aht.Aht;

public class TestOfxStatusTableFactory extends AbstractOfxStatusFactoryTest
{
	final static Logger logger = LoggerFactory.getLogger(TestOfxStatusTableFactory.class);
	
	private Configuration config;
	
	private OfxStatusTableFactory fOfx;
	private final String lang ="de";
	private Aht xmlStatus;
	private static Translations translations;
	
	@BeforeAll
	public static void initFiles() throws FileNotFoundException
	{
		fXml = new File(rootDir,"tableStatus.xml");
		fTxt = new File(rootDir,"tableStatus.tex");
	
		
		translations = JaxbUtil.loadJAXB("data/xml/dummyTranslations.xml", Translations.class);
	}
	
	@BeforeEach
	public void init() throws UtilsConfigurationException
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
		LatexGridTableRenderer renderer = new LatexGridTableRenderer(cp);
		renderer.render(actual);
    	debug(renderer);
    	save(renderer,fTxt);
    	assertText(renderer,fTxt);
	}
	
	public static void main(String[] args) throws Exception
    {
		Configuration config = JeeslBootstrap.init();
		
		TestOfxStatusTableFactory.initFiles();
		TestOfxStatusTableFactory test = new TestOfxStatusTableFactory();
		test.setSaveReference(true);
		test.init();
	
		test.testOfx();
		test.testLatex();
    }
}