package net.sf.ahtutils.xml.access;

import java.io.File;
import java.io.FileNotFoundException;

import net.sf.exlp.util.xml.JaxbUtil;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.system.status.TestXmlDescriptions;
import org.jeesl.model.xml.system.status.TestXmlLangs;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlRole extends AbstractXmlAccessTest
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlRole.class);
	
	@BeforeClass
	public static void initFiles()
	{
		fXml = new File(rootDir,"role.xml");
	}
    
    @Test
    public void testXml() throws FileNotFoundException
    {
    	Role actual = create();
    	Role expected = (Role)JaxbUtil.loadJAXB(fXml.getAbsolutePath(), Role.class);
    	assertJaxbEquals(expected, actual);
    }
    
    private static Role create(){return create(true);}
    public static Role create(boolean withChilds)
    {
    	Role xml = new Role();
    	xml.setCode("myCode");
    	xml.setName("myName");
    	
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
			
		TestXmlRole.initFiles();	
		TestXmlRole test = new TestXmlRole();
		test.save();
    }
}