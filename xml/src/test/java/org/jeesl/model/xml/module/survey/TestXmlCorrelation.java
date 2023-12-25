package org.jeesl.model.xml.module.survey;

import org.jeesl.JeeslXmlTestBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlCorrelation extends AbstractXmlSurveyTest<Correlation>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlCorrelation.class);
	
	public TestXmlCorrelation(){super(Correlation.class);}
	public static Correlation create(boolean withChildren){return (new TestXmlCorrelation()).build(withChildren);}   

    public Correlation build(boolean withChilds)
    {
    	Correlation xml = new Correlation();
    	xml.setId(123);
    	xml.setType("myType");
    	if(withChilds)
    	{    		
    		xml.getCorrelation().add(TestXmlCorrelation.create(false));
    		xml.getCorrelation().add(TestXmlCorrelation.create(false));
    	}
    	return xml;
    }
    
    public void save() {save(create(true),fXml);}
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlCorrelation test = new TestXmlCorrelation();
		test.saveReferenceXml();
    }
}