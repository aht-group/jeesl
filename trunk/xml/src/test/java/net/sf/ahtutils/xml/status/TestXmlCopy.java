package net.sf.ahtutils.xml.status;

import java.io.File;
import java.io.FileNotFoundException;

import net.sf.ahtutils.test.UtilsXmlTestBootstrap;
import net.sf.exlp.util.xml.JaxbUtil;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlCopy extends AbstractXmlStatusTest
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlCopy.class);
	
	@BeforeClass
	public static void initFiles()
	{
		fXml = new File(rootDir,Copy.class.getSimpleName()+".xml");
	}
    
    @Test
    public void testXml() throws FileNotFoundException
    {
    	Copy actual = create(true);
    	Copy expected = JaxbUtil.loadJAXB(fXml.getAbsolutePath(), Copy.class);
    	assertJaxbEquals(expected, actual);
    }
    
    public static Copy create(boolean withChilds)
    {
    	Copy xml = new Copy();
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
			
		TestXmlCopy.initFiles();	
		TestXmlCopy test = new TestXmlCopy();
		test.save();
    }
}