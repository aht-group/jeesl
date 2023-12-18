package org.jeesl.model.xml.io.cms.text;

import org.jeesl.JeeslXmlTestBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlAnswer extends AbstractXmlTextOldTest<Answer>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlAnswer.class);
	
	public TestXmlAnswer(){super(Answer.class);}
	public static Answer create(boolean withChildren){return (new TestXmlAnswer()).build(withChildren);}
    
    public Answer build(boolean withChilds){return create(withChilds,"myKey","myValue");}
    public static Answer create(boolean withChilds, String key, String description)
    {
    	Answer xml = new Answer();
    	xml.setVersion(1);
    	xml.setKey(key);
    	xml.setValue(description);
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlAnswer test = new TestXmlAnswer();
		test.saveReferenceXml();
    }
}