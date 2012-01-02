package net.sf.ahtutils.xml.access;

import java.io.File;
import java.io.FileNotFoundException;

import net.sf.ahtutils.test.UtilsXmlTstBootstrap;
import net.sf.ahtutils.xml.status.TestXmlDescriptions;
import net.sf.ahtutils.xml.status.TestXmlLangs;
import net.sf.exlp.util.xml.JaxbUtil;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlGroup extends AbstractXmlAccessTest
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlGroup.class);
	
	@BeforeClass
	public static void initFiles()
	{
		fXml = new File(rootDir,"group.xml");
	}
    
    @Test
    public void testAclContainer() throws FileNotFoundException
    {
    	Group actual = create();
    	Group expected = (Group)JaxbUtil.loadJAXB(fXml.getAbsolutePath(), Group.class);
    	assertJaxbEquals(expected, actual);
    }
    
    private static Group create(){return create(true);}
    public static Group create(boolean withChilds)
    {
    	Group xml = new Group();
    	xml.setCode("myCode");
    	
    	if(withChilds)
    	{
    		xml.setLangs(TestXmlLangs.create(false));
    		xml.setDescriptions(TestXmlDescriptions.create(false));
    		xml.setViews(TestXmlViews.create(false));
    		xml.setGroups(TestXmlGroups.create(false));
    	}
    	return xml;
    }
    
    public void save() {save(create(),fXml);}
	
	public static void main(String[] args)
    {
		UtilsXmlTstBootstrap.init();
			
		TestXmlGroup.initFiles();	
		TestXmlGroup test = new TestXmlGroup();
		test.save();
    }
}