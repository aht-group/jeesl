package net.sf.ahtutils.controller.factory.ofx.navigation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import net.sf.ahtutils.controller.factory.xml.navigation.XmlMenuItemFactory;
import net.sf.ahtutils.test.AhtUtilsDocBootstrap;
import net.sf.ahtutils.xml.navigation.Menu;
import net.sf.exlp.util.xml.JaxbUtil;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openfuxml.exception.OfxAuthoringException;
import org.openfuxml.renderer.processor.latex.addon.graph.LatexTreeRenderer;
import org.openfuxml.xml.addon.graph.Tree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestOfxMenuTreeFactory extends AbstractOfxNavigationFactoryTest
{
	final static Logger logger = LoggerFactory.getLogger(TestOfxMenuTreeFactory.class);
	
	private OfxMenuTreeFactory factory;
	
	private Menu menu;
	
	@BeforeClass
	public static void initFiles()
	{
		fXml = new File(rootDir,"menu.xml");
		fTxt = new File(rootDir,"menu.tex");
	}
	
	@Before
	public void init()
	{	
		factory = new OfxMenuTreeFactory();
		menu = new Menu();
		
		menu.getMenuItem().add(XmlMenuItemFactory.create("myLabel"));
		menu.getMenuItem().add(XmlMenuItemFactory.create("myLabel2"));
		menu.getMenuItem().get(0).getMenuItem().add(XmlMenuItemFactory.create("myLabel1.1"));
	}
	
	@Test
	public void testOfx() throws FileNotFoundException
	{
		Tree actual = factory.create(menu);
		saveXml(actual,fXml,false);
		Tree expected = JaxbUtil.loadJAXB(fXml.getAbsolutePath(), Tree.class);
		this.assertJaxbEquals(expected, actual);
	}
	
	@Test
	public void testLatex() throws OfxAuthoringException, IOException
	{
		Tree actual = factory.create(menu);
		LatexTreeRenderer renderer = new LatexTreeRenderer();
		renderer.render(actual);
    	debug(renderer);
    	save(renderer,fTxt);
    	assertText(renderer,fTxt);	
	}
	
	public static void main(String[] args) throws Exception
    {
		AhtUtilsDocBootstrap.init();
		
		TestOfxMenuTreeFactory.initFiles();
		TestOfxMenuTreeFactory test = new TestOfxMenuTreeFactory();
		test.setSaveReference(true);
		test.init();
		test.testOfx();
		test.testLatex();
    }
}