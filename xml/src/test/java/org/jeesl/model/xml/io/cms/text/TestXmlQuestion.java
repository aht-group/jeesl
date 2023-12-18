package org.jeesl.model.xml.io.cms.text;

import org.jeesl.JeeslXmlTestBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.text.Question;

public class TestXmlQuestion extends AbstractXmlTextOldTest<Question>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlQuestion.class);
	
	public TestXmlQuestion(){super(Question.class);}
	public static Question create(boolean withChildren){return (new TestXmlQuestion()).build(withChildren);}
    
    public Question build(boolean withChilds){return create(withChilds,"myKey","myValue");}
    public static Question create(boolean withChilds, String key, String description)
    {
    	Question xml = new Question();
    	xml.setVersion(1);
    	xml.setKey(key);
    	xml.setValue(description);
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlQuestion test = new TestXmlQuestion();
		test.saveReferenceXml();
    }
}