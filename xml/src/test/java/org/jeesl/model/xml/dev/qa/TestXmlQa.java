package org.jeesl.model.xml.dev.qa;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.module.dev.qa.Qa;
import org.jeesl.model.xml.module.survey.TestXmlSurvey;
import org.jeesl.model.xml.system.security.TestXmlStaff;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlQa extends AbstractXmlQaTest<Qa>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlQa.class);
	
	public TestXmlQa(){super(Qa.class);}
	public static Qa create(boolean withChildren){return (new TestXmlQa()).build(withChildren);}  
    
    public Qa build(boolean withChilds)
    {
    	Qa xml = new Qa();
    	xml.setId(123l);
    	xml.setClient("myClient");
    	xml.setDeveloper("myDeveloper");
    	
    	if(withChilds)
    	{
    		xml.getCategory().add(TestXmlCategory.create(false));xml.getCategory().add(TestXmlCategory.create(false));
    		xml.getStaff().add(TestXmlStaff.create(false));xml.getStaff().add(TestXmlStaff.create(false));
    		xml.setSurvey(TestXmlSurvey.create(false));
    		xml.setGroups(TestXmlGroups.create(false));
    		xml.getChecklist().add(TestXmlChecklist.create(false));xml.getChecklist().add(TestXmlChecklist.create(false));
    	}
    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlQa test = new TestXmlQa();
		test.saveReferenceXml();
    }
}