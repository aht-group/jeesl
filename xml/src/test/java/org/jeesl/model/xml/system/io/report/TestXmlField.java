package org.jeesl.model.xml.system.io.report;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.report.Field;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlField extends AbstractXmlReportTest<Field>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlField.class);
	
	public TestXmlField(){super(Field.class);}
	public static Field create(boolean withChildren){return (new TestXmlField()).build(withChildren);}   
    
    public Field build(boolean withChildren)
    {
    	Field field = new Field();
    	field.setExpression("/report/title");
    	field.setType("field");
    	field.setName("title");
    	return field;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlField test = new TestXmlField();
		test.saveReferenceXml();
    }
}