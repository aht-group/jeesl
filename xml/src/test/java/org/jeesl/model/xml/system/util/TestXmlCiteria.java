package org.jeesl.model.xml.system.util;

import org.jeesl.JeeslXmlTestBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlCiteria extends AbstractXmlUtilsTest<Criteria>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlCiteria.class);
	
	public TestXmlCiteria(){super(Criteria.class);}
	public static Criteria create(boolean withChildren){return (new TestXmlCiteria()).build(withChildren);}
	
    public Criteria build(boolean withChilds)
    {
    	Criteria xml = new Criteria();
        xml.setId(123l);
        xml.setCode("myCode");
        xml.setType("Boolean");
        xml.setValue("xx");
    	
    	if(withChilds)
    	{
    		
    	}
    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlCiteria test = new TestXmlCiteria();
		test.saveReferenceXml();
    }
}