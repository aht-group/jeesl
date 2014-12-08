package net.sf.ahtutils.xml.status;

import java.io.FileNotFoundException;

import net.sf.ahtutils.test.UtilsXmlTestBootstrap;
import net.sf.exlp.util.xml.JaxbUtil;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlMainType extends AbstractXmlStatusTest
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlMainType.class);
	
	@BeforeClass public static void initFiles(){setXmlFile(dirSuffix, MainType.class);}
    
    @Test
    public void testXml() throws FileNotFoundException
    {
    	MainType actual = create(true);
    	MainType expected = JaxbUtil.loadJAXB(fXml.getAbsolutePath(), MainType.class);
    	assertJaxbEquals(expected, actual);
    }
    
    public static MainType create(boolean withChilds)
    {
    	MainType xml = new MainType();
    	xml.setCode("myCode");
    	xml.setVisible(true);
    	xml.setGroup("myGroup");
    	xml.setLabel("myLabel");
    	xml.setImage("test/green.png");
    	xml.setPosition(1);
    	
    	if(withChilds)
    	{
    		xml.setLangs(TestXmlLangs.create(false));
    		xml.setDescriptions(TestXmlDescriptions.create(false));
    	}
    	
    	return xml;
    }
    
    public void save() {save(create(true),fXml);}
	
	public static void main(String[] args)
    {
		UtilsXmlTestBootstrap.init();
			
		TestXmlMainType.initFiles();	
		TestXmlMainType test = new TestXmlMainType();
		test.save();
    }
}