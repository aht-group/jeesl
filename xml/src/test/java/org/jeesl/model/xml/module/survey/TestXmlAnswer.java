package org.jeesl.model.xml.module.survey;

import org.jeesl.JeeslXmlTestBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.survey.Answer;

public class TestXmlAnswer extends AbstractXmlSurveyTest<Answer>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlAnswer.class);
	
	public TestXmlAnswer(){super(Answer.class);}
	public static Answer create(boolean withChildren){return (new TestXmlAnswer()).build(withChildren);}   
    
    public Answer build(boolean withChilds)
    {
    	Answer xml = new Answer();
    	xml.setId(123);
    	xml.setValueBoolean(true);
    	xml.setValueNumber(123);
    	xml.setValueDouble(123.45);
    	xml.setScore(2.3);
    	
    	if(withChilds)
    	{
    		xml.setData(TestXmlData.create(false));
    		xml.setQuestion(TestXmlQuestion.create(false));
    		xml.setAnswer(org.jeesl.model.xml.text.TestXmlAnswer.create(false));
    		xml.setRemark(org.jeesl.model.xml.text.TestXmlRemark.create(false));
    	}
    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlAnswer test = new TestXmlAnswer();
		test.saveReferenceXml();
    }
}