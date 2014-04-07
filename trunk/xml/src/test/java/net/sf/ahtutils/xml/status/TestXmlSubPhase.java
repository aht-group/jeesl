package net.sf.ahtutils.xml.status;

import java.io.File;
import java.io.FileNotFoundException;

import net.sf.ahtutils.test.UtilsXmlTestBootstrap;
import net.sf.exlp.util.xml.JaxbUtil;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlSubPhase extends AbstractXmlStatusTest
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlSubPhase.class);
	
	@BeforeClass
	public static void initFiles()
	{
		fXml = new File(rootDir,SubPhase.class.getSimpleName()+".xml");
	}
    
    @Test
    public void testXml() throws FileNotFoundException
    {
    	SubPhase actual = create(true);
    	SubPhase expected = JaxbUtil.loadJAXB(fXml.getAbsolutePath(), SubPhase.class);
    	assertJaxbEquals(expected, actual);
    }
    
    public static SubPhase create(boolean withChilds)
    {
    	SubPhase xml = new SubPhase();
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
			
		TestXmlSubPhase.initFiles();	
		TestXmlSubPhase test = new TestXmlSubPhase();
		test.save();
    }
}