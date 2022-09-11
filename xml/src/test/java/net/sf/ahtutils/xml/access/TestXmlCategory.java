package net.sf.ahtutils.xml.access;

import java.io.File;
import java.io.FileNotFoundException;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.system.security.TestXmlViews;
import org.jeesl.model.xml.system.status.TestXmlDescriptions;
import org.jeesl.model.xml.system.status.TestXmlLangs;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.security.Category;
import net.sf.exlp.util.xml.JaxbUtil;

public class TestXmlCategory extends AbstractXmlAccessTest
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlCategory.class);
	
	@BeforeClass
	public static void initFiles()
	{
		fXml = new File(rootDir,"category.xml");
	}
    
    @Test
    public void testXml() throws FileNotFoundException
    {
    	Category actual = create();
    	Category expected = JaxbUtil.loadJAXB(fXml.getAbsolutePath(), Category.class);
    	assertJaxbEquals(expected, actual);
    }
    
    private static Category create(){return create(true);}
    public static Category create(boolean withChilds)
    {
    	Category xml = new Category();
    	xml.setCode("myCode");
    	xml.setPosition(1);
    	
    	if(withChilds)
    	{
    		xml.setLangs(TestXmlLangs.create(false));
    		xml.setDescriptions(TestXmlDescriptions.create(false));
    		xml.setViews(TestXmlViews.create(false));

    	}
    	
    	return xml;
    }
    
    public void save() {save(create(),fXml);}
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
			
		TestXmlCategory.initFiles();	
		TestXmlCategory test = new TestXmlCategory();
		test.save();
    }
}