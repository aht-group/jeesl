package net.sf.ahtutils.xml.status;

import java.io.File;
import java.io.FileNotFoundException;

import net.sf.ahtutils.test.UtilsXmlTestBootstrap;
import net.sf.exlp.util.xml.JaxbUtil;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlTypes extends AbstractXmlStatusTest
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlTypes.class);
	
	@BeforeClass
	public static void initFiles()
	{
		fXml = new File(rootDir,"types.xml");
	}
    
    @Test
    public void testXml() throws FileNotFoundException
    {
    	Types actual = create(true);
    	Types expected = JaxbUtil.loadJAXB(fXml.getAbsolutePath(), Types.class);
    	assertJaxbEquals(expected, actual);
    }
    
    public static Types create(boolean withChilds)
    {
    	Types xml = new Types();
    	xml.setGroup("myGroup");
    	
    	if(withChilds)
    	{
    		xml.getType().add(TestXmlType.create(false));
    		xml.getType().add(TestXmlType.create(false));
    	}
    	return xml;
    }
    
    public void save() {save(create(true),fXml);}
	
	public static void main(String[] args)
    {
		UtilsXmlTestBootstrap.init();
			
		TestXmlTypes.initFiles();	
		TestXmlTypes test = new TestXmlTypes();
		test.save();
    }
}