package net.sf.ahtutils.xml.status;

import java.io.FileNotFoundException;

import net.sf.ahtutils.test.UtilsXmlTestBootstrap;
import net.sf.exlp.util.xml.JaxbUtil;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlCapability extends AbstractXmlStatusTest
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlCapability.class);
	
	@BeforeClass public static void initFiles(){setXmlFile(dirSuffix, Capability.class);}
	
    @Test
    public void testXml() throws FileNotFoundException
    {
    	Capability actual = create(true);
    	Capability expected = JaxbUtil.loadJAXB(fXml.getAbsolutePath(), Capability.class);
    	assertJaxbEquals(expected, actual);
    }
    
    public static Capability create(boolean withChilds)
    {
    	Capability xml = new Capability();
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
			
		TestXmlCapability.initFiles();	
		TestXmlCapability test = new TestXmlCapability();
		test.save();
    }
}