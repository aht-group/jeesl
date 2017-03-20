package org.jeesl.doc.ofx.security;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.DefaultConfigurationBuilder;
import org.jeesl.factory.xml.system.security.XmlCategoryFactory;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openfuxml.content.list.List;
import org.openfuxml.exception.OfxAuthoringException;
import org.openfuxml.interfaces.renderer.latex.OfxLatexRenderer;
import org.openfuxml.renderer.latex.content.list.LatexListRenderer;
import org.openfuxml.renderer.latex.content.structure.LatexSectionRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.doc.ofx.security.list.OfxSecurityCategoryListFactory;
import net.sf.ahtutils.factory.xml.status.XmlDescriptionFactory;
import net.sf.ahtutils.factory.xml.status.XmlDescriptionsFactory;
import net.sf.ahtutils.factory.xml.status.XmlLangFactory;
import net.sf.ahtutils.factory.xml.status.XmlLangsFactory;
import net.sf.ahtutils.test.AhtUtilsDocBootstrap;
import net.sf.ahtutils.xml.security.Category;
import net.sf.ahtutils.xml.status.Translations;
import net.sf.exlp.util.xml.JaxbUtil;

public class TestOfxCategoryListFactory extends AbstractOfxSecurityFactoryTest
{
	final static Logger logger = LoggerFactory.getLogger(TestOfxCategoryListFactory.class);
	
	private static Configuration config;
	private static Translations translations;
	
	private OfxSecurityCategoryListFactory factory;
	
	private final String lang ="de";
	private Category rc1;
//	private RoleCategory rc2;
	private java.util.List<Category> list;
	
	private OfxLatexRenderer parentSection;
	
	@BeforeClass
	public static void initFiles() throws ConfigurationException, FileNotFoundException
	{
		fXml = new File(rootDir,"listRoleCategory.xml");
		fTxt = new File(rootDir,"listRoleCategory.tex");
		
		DefaultConfigurationBuilder builder = new DefaultConfigurationBuilder();
		config = builder.getConfiguration(false);
		translations = JaxbUtil.loadJAXB("src/test/resources/data/xml/dummyTranslations.xml", Translations.class);
	}
	
	@Before
	public void init()
	{	
		super.initOfx();
		
		parentSection = new LatexSectionRenderer(cp,0,null);
		factory = new OfxSecurityCategoryListFactory(config,lang,translations,cp);
		list = new ArrayList<Category>();
		rc1 = createCategory(1);list.add(rc1);
	}
	
	private Category createCategory(int id)
	{
		Category rc = XmlCategoryFactory.build();
		rc.setLangs(XmlLangsFactory.build(XmlLangFactory.create(lang, "Category "+id)));
		rc.setDescriptions(XmlDescriptionsFactory.build(XmlDescriptionFactory.create(lang, id+" This category is for testing purposes only.")));
		return rc;
	}
	
	@Test
	public void testOfx() throws FileNotFoundException, OfxAuthoringException
	{
		List actual = factory.descriptionList(list);
		saveXml(actual,fXml,false);
		List expected = JaxbUtil.loadJAXB(fXml.getAbsolutePath(), List.class);
		this.assertJaxbEquals(expected, actual);
	}
	
	@Test
	public void testLatex() throws OfxAuthoringException, IOException
	{
		List actual = factory.descriptionList(list);
		LatexListRenderer renderer = new LatexListRenderer(cp);
		renderer.render(actual,parentSection);
    	debug(renderer);
    	save(renderer,fTxt);
    	assertText(renderer,fTxt);
		
	}
	
	public static void main(String[] args) throws Exception
    {
		AhtUtilsDocBootstrap.init();
		
		TestOfxCategoryListFactory.initFiles();
		TestOfxCategoryListFactory test = new TestOfxCategoryListFactory();
		test.setSaveReference(true);
		test.init();
		test.testOfx();
		test.testLatex();
    }
}