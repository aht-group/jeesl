package org.jeesl.model.xml.system.status;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.locale.status.EvaluationMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlEvaluationMethod extends AbstractXmlStatusTest<EvaluationMethod>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlEvaluationMethod.class);
	
	public TestXmlEvaluationMethod(){super(EvaluationMethod.class);}
	public static EvaluationMethod create(boolean withChildren){return (new TestXmlEvaluationMethod()).build(withChildren);} 
    
    public EvaluationMethod build(boolean withChilds)
    {
    	EvaluationMethod xml = new EvaluationMethod();
    	xml.setCode("myCode");
    	xml.setVisible(true);
    	xml.setGroup("myGroup");
    	xml.setLabel("myLabel");
    	xml.setImage("test/green.png");
    	xml.setPosition(1);
    	
    	if(withChilds)
    	{
    		xml.setLangs(TestXmlLangs.create(false));
    		xml.setDescriptions(TestXmlDescriptions.create(false));
    	}
    	
    	return xml;
    }
    
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();	
		TestXmlEvaluationMethod test = new TestXmlEvaluationMethod();
		test.saveReferenceXml();
    }
}