package org.jeesl.model.xml.jeesl;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.dev.qa.TestXmlTest;
import org.jeesl.model.xml.module.survey.TestXmlAnswer;
import org.jeesl.model.xml.module.survey.TestXmlSurvey;
import org.jeesl.model.xml.module.survey.TestXmlSurveys;
import org.jeesl.model.xml.module.survey.TestXmlTemplate;
import org.jeesl.model.xml.module.survey.TestXmlTemplates;
import org.jeesl.model.xml.system.io.report.TestXmlReport;
import org.jeesl.model.xml.system.revision.TestXmlEntity;
import org.jeesl.model.xml.system.security.TestXmlCategory;
import org.jeesl.model.xml.system.security.TestXmlStaff;
import org.jeesl.model.xml.system.status.TestXmlLangs;
import org.jeesl.model.xml.system.status.TestXmlModel;
import org.jeesl.model.xml.system.status.TestXmlStatus;
import org.jeesl.model.xml.system.status.TestXmlType;
import org.jeesl.model.xml.system.symbol.TestXmlGraphic;
import org.jeesl.model.xml.system.util.TestXmlTrafficLight;
import org.jeesl.model.xml.system.util.TestXmlTrafficLights;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.aht.Query;

public class TestXmlQuery extends AbstractXmlAhtTest<Query>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlQuery.class);
	
	public TestXmlQuery(){super(Query.class);}
	public static Query create(boolean withChildren){return (new TestXmlQuery()).build(withChildren);}  
    
    public Query build(boolean withChilds)
    {
    	Query xml = new Query();
    	xml.setLang("myLang");
    	
    	if(withChilds)
    	{

    		xml.setLangs(TestXmlLangs.create(false));
    		xml.setStatus(TestXmlStatus.create(false));
    		xml.setType(TestXmlType.create(false));
    		xml.setModel(TestXmlModel.create(false));
    		
    		xml.setTest(TestXmlTest.create(false));
    		
    		xml.setCategory(TestXmlCategory.create(false));
    		xml.setStaff(TestXmlStaff.create(false));
    		
    		//Report
    		xml.setReport(TestXmlReport.create(false));
    		
    		//Revision
    		xml.setEntity(TestXmlEntity.create(false));
    		
    		//Survey
    		xml.setTemplates(TestXmlTemplates.create(false));
    		xml.setTemplate(TestXmlTemplate.create(false));
    		xml.setSurveys(TestXmlSurveys.create(false));
    		xml.setSurvey(TestXmlSurvey.create(false));
    		xml.setAnswer(TestXmlAnswer.create(false));
    		
    		//Symbol
    		xml.setGraphic(TestXmlGraphic.create(false));
    		
    		xml.setTrafficLights(TestXmlTrafficLights.create(false));
    		xml.setTrafficLight(TestXmlTrafficLight.create(false));
    	}
    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlQuery test = new TestXmlQuery();
		test.saveReferenceXml();
    }
}