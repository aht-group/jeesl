package org.jeesl.model.xml.system.status;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.locale.status.SubProgram;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlSubProgram extends AbstractXmlStatusTest<SubProgram>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlSubProgram.class);
	
	public TestXmlSubProgram(){super(SubProgram.class);}
	public static SubProgram create(boolean withChildren){return (new TestXmlSubProgram()).build(withChildren);}   
    
    public SubProgram build(boolean withChildren)
    {
    	SubProgram xml = new SubProgram();
    	xml.setId(123l);
    	xml.setCode("myCode");
    	xml.setVisible(true);
    	xml.setGroup("myGroup");
    	xml.setLabel("myLabel");
    	xml.setImage("test/green.png");
    	xml.setPosition(1);
    	
    	if(withChildren)
    	{
    		xml.setLangs(TestXmlLangs.create(false));
    		xml.setDescriptions(TestXmlDescriptions.create(false));
    	}

    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlSubProgram test = new TestXmlSubProgram();
		test.saveReferenceXml();
    }
}