package net.sf.ahtutils.xml.access;

import java.io.File;
import java.io.FileNotFoundException;

import net.sf.exlp.util.xml.JaxbUtil;

import org.jeesl.JeeslXmlTestBootstrap;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlRoles extends AbstractXmlAccessTest
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlRoles.class);
	
	@BeforeClass
	public static void initFiles()
	{
		fXml = new File(rootDir,"roles.xml");
	}
    
    @Test
    public void testAclContainer() throws FileNotFoundException
    {
    	Roles actual = create();
    	Roles expected = (Roles)JaxbUtil.loadJAXB(fXml.getAbsolutePath(), Roles.class);
    	assertJaxbEquals(expected, actual);
    }
    
    private static Roles create(){return create(true);}
    public static Roles create(boolean withChilds)
    {
    	Roles xml = new Roles();
    	
    	if(withChilds)
    	{

    	}
    	return xml;
    }
    
    public void save() {save(create(),fXml);}
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
			
		TestXmlRoles.initFiles();	
		TestXmlRoles test = new TestXmlRoles();
		test.save();
    }
}