package org.jeesl.model.xml.dev.qa;

import org.jeesl.JeeslXmlTestBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.qa.Results;

public class TestXmlResults extends AbstractXmlQaTest<Results>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlResults.class);
	
	public TestXmlResults(){super(Results.class);}
	public static Results create(boolean withChildren){return (new TestXmlResults()).build(withChildren);}  
    
    public Results build(boolean withChilds)
    {
    	Results xml = new Results();
    	
    	if(withChilds)
    	{
    		xml.getResult().add(TestXmlResult.create(false));xml.getResult().add(TestXmlResult.create(false));
    	}
    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlResults test = new TestXmlResults();
		test.saveReferenceXml();
    }
}