package org.jeesl.model.xml.module.survey;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.domain.finance.TestXmlFigures;
import org.jeesl.model.xml.system.status.TestXmlStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlSurvey extends AbstractXmlSurveyTest<Survey>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlSurvey.class);
	
	public TestXmlSurvey(){super(Survey.class);}
	public static Survey create(boolean withChildren){return (new TestXmlSurvey()).build(withChildren);}   
    
    public Survey build(boolean withChilds)
    {
    	Survey xml = new Survey();
    	xml.setId(123);
    	xml.setName("myName");
    	xml.setValidFrom(AbstractXmlSurveyTest.getDefaultXmlDate());
    	xml.setValidTo(AbstractXmlSurveyTest.getDefaultXmlDate());
    	
    	if(withChilds)
    	{    		
    		xml.setTemplate(TestXmlTemplate.create(false));
    		xml.setStatus(TestXmlStatus.create(false));
    		xml.getData().add(TestXmlData.create(false));xml.getData().add(TestXmlData.create(false));
    		xml.setFigures(TestXmlFigures.create(false));
    	}
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlSurvey test = new TestXmlSurvey();
		test.saveReferenceXml();
    }
}