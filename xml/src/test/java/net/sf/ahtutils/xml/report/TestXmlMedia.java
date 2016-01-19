package net.sf.ahtutils.xml.report;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.test.UtilsXmlTestBootstrap;
import net.sf.exlp.util.xml.JaxbUtil;

public class TestXmlMedia extends AbstractXmlReportTest
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlMedia.class);
	
	@BeforeClass
	public static void initFiles()
	{
		fXml = new File(rootDir,"media2.xml");
	}
    
    @Test
    public void testMedia() throws FileNotFoundException
    {
    	Media test = create();
    	Media ref = (Media)JaxbUtil.loadJAXB(fXml.getAbsolutePath(), Media.class);
    	assertJaxbEquals(ref, test);
    }
    
    public static Media create(){return create("pdf");}
    public static Media create(String type)
    {
    	Media media = new Media();
    	media.setDir("testDir");
    	media.getJr().add(TestXmlJr.create());
    	media.setType(type);
//    	media.setChart(new Chart());
    	return media;
    }
    
    public void save() {save(create(),fXml);}
	
	public static void main(String[] args)
    {
		UtilsXmlTestBootstrap.init();
			
		TestXmlMedia.initJaxb();
		TestXmlMedia.initFiles();	
		TestXmlMedia test = new TestXmlMedia();
		test.save();
    }
}