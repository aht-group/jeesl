package net.sf.ahtutils.xml.access;

import java.io.File;
import java.io.FileNotFoundException;

import net.sf.ahtutils.test.UtilsXmlTestBootstrap;
import net.sf.ahtutils.xml.status.TestXmlDescriptions;
import net.sf.ahtutils.xml.status.TestXmlLangs;
import net.sf.exlp.util.xml.JaxbUtil;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlUsecase extends AbstractXmlAccessTest
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlUsecase.class);
	
	@BeforeClass
	public static void initFiles()
	{
		fXml = new File(rootDir,"usecase.xml");
	}
    
    @Test
    public void testAclContainer() throws FileNotFoundException
    {
    	Usecase actual = create();
    	Usecase expected = JaxbUtil.loadJAXB(fXml.getAbsolutePath(), Usecase.class);
    	assertJaxbEquals(expected, actual);
    }
    
    private static Usecase create(){return create(true);}
    public static Usecase create(boolean withChilds)
    {
    	Usecase xml = new Usecase();
    	xml.setCode("myCode");
    	
    	if(withChilds)
    	{
    		xml.setLangs(TestXmlLangs.create(false));
    		xml.setDescriptions(TestXmlDescriptions.create(false));
    		xml.setActions(TestXmlActions.create(false));
    		xml.setViews(TestXmlViews.create(false));
    	}
    	return xml;
    }
    
    public void save() {save(create(),fXml);}
	
	public static void main(String[] args)
    {
		UtilsXmlTestBootstrap.init();
			
		TestXmlUsecase.initFiles();	
		TestXmlUsecase test = new TestXmlUsecase();
		test.save();
    }
}