package net.sf.ahtutils.xml.status;

import java.io.File;
import java.io.FileNotFoundException;

import net.sf.ahtutils.test.UtilsXmlTestBootstrap;
import net.sf.exlp.util.xml.JaxbUtil;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlResult extends AbstractXmlStatusTest
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlResult.class);
	
	@BeforeClass public static void initFiles(){setXmlFile(dirSuffix, Result.class);}
    
    @Test
    public void testXml() throws FileNotFoundException
    {
    	Result actual = create(true);
    	Result expected = JaxbUtil.loadJAXB(fXml.getAbsolutePath(), Result.class);
    	assertJaxbEquals(expected, actual);
    }
    
    public static Result create(boolean withChilds)
    {
    	Result xml = new Result();
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
//    		xml.setParent(TestXmlParent.create(false));
    	}
    	
    	return xml;
    }
    
    public void save() {save(create(true),fXml);}
	
	public static void main(String[] args)
    {
		UtilsXmlTestBootstrap.init();
			
		TestXmlResult.initFiles();	
		TestXmlResult test = new TestXmlResult();
		test.save();
    }
}