package org.jeesl.model.xml.module.survey;

import org.jeesl.JeeslXmlTestBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlOption extends AbstractXmlSurveyTest<Option>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlOption.class);
	
	public TestXmlOption(){super(Option.class);}
	public static Option create(boolean withChildren){return (new TestXmlOption()).build(withChildren);}   

    public Option build(boolean withChilds)
    {
    		Option xml = new Option();
	    	xml.setId(123);
	    	xml.setLabel("myLabel");
	    	xml.setCode("myCode");
    	
	    	if(withChilds)
	    	{
	
	    	}
    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlOption test = new TestXmlOption();
		test.saveReferenceXml();
    }
}