package net.sf.ahtutils.xml.access;

import java.io.File;
import java.io.FileNotFoundException;

import net.sf.exlp.util.io.LoggerInit;
import net.sf.exlp.util.xml.JaxbUtil;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlViews extends AbstractXmlAccessTest
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlViews.class);
	
	@BeforeClass
	public static void initFiles()
	{
		fXml = new File(rootDir,"views.xml");
	}
    
    @Test
    public void testAclContainer() throws FileNotFoundException
    {
    	Views actual = create();
    	Views expected = (Views)JaxbUtil.loadJAXB(fXml.getAbsolutePath(), Views.class);
    	assertJaxbEquals(expected, actual);
    }
    
    private static Views create(){return create(true);}
    public static Views create(boolean withChilds)
    {
    	Views xml = new Views();
    	
    	if(withChilds)
    	{
    		xml.getView().add(TestXmlView.create(false));
    	}
    	return xml;
    }
    
    public void save() {save(create(),fXml);}
	
	public static void main(String[] args)
    {
		LoggerInit loggerInit = new LoggerInit("log4j.xml");	
			loggerInit.addAltPath("src/test/resources/config");
			loggerInit.init();		
			
		TestXmlViews.initFiles();	
		TestXmlViews test = new TestXmlViews();
		test.save();
    }
}