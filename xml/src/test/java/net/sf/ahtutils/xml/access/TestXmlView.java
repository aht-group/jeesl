package net.sf.ahtutils.xml.access;

import java.io.File;
import java.io.FileNotFoundException;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.navigation.TestXmlNavigation;
import org.jeesl.model.xml.system.status.TestXmlDescriptions;
import org.jeesl.model.xml.system.status.TestXmlLangs;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.security.View;
import net.sf.exlp.util.xml.JaxbUtil;

public class TestXmlView extends AbstractXmlAccessTest
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlView.class);
	
	@BeforeClass
	public static void initFiles()
	{
		fXml = new File(rootDir,"view.xml");
	}
    
    @Test
    public void testAclContainer() throws FileNotFoundException
    {
    	View actual = create();
    	View expected = JaxbUtil.loadJAXB(fXml.getAbsolutePath(), View.class);
    	assertJaxbEquals(expected, actual);
    }
    
    private static View create(){return create(true);}
    public static View create(boolean withChilds)
    {
    	View xml = new View();
    	xml.setCode("myCode");
    	xml.setUrlParameter("myUrlParameter");
    	xml.setPosition(1);
    	xml.setVisible(true);
    	xml.setPublic(true);
    	xml.setOnlyLoginRequired(true);
    	xml.setLabel("myLabel");
    	
    	if(withChilds)
    	{
    		xml.setLangs(TestXmlLangs.create(false));
    		xml.setDescriptions(TestXmlDescriptions.create(false));
    		xml.setNavigation(TestXmlNavigation.create(false));
    	}
    	return xml;
    }
    
    public void save() {save(create(),fXml);}
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
			
		TestXmlView.initFiles();	
		TestXmlView test = new TestXmlView();
		test.save();
    }
}