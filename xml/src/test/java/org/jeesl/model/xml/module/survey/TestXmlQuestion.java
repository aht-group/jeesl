package org.jeesl.model.xml.module.survey;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.domain.finance.TestXmlFigures;
import org.jeesl.model.xml.io.cms.text.TestXmlRemark;
import org.jeesl.model.xml.system.status.TestXmlUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlQuestion extends AbstractXmlSurveyTest<Question>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlQuestion.class);
	
	public TestXmlQuestion(){super(Question.class);}
	public static Question create(boolean withChildren){return (new TestXmlQuestion()).build(withChildren);}   

    public Question build(boolean withChilds)
    {
    	Question xml = new Question();
    	xml.setId(123l);
    	xml.setPosition(2);
    	xml.setVisible(true);
    	xml.setCode("myCode");
    	xml.setTopic("myTopic");
    	
    	if(withChilds)
    	{
    		xml.setUnit(TestXmlUnit.create(false));
    		xml.setRemark(TestXmlRemark.create(false));
    		xml.setQuestion(org.jeesl.model.xml.io.cms.text.TestXmlQuestion.create(false));
    		xml.setAnswer(TestXmlAnswer.create(false));
    		xml.setScore(TestXmlScore.create(false));
    		xml.setFigures(TestXmlFigures.create(false));
    	}
    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlQuestion test = new TestXmlQuestion();
		test.saveReferenceXml();
    }
}