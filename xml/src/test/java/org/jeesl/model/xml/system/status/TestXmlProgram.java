package org.jeesl.model.xml.system.status;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.locale.status.Program;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlProgram extends AbstractXmlStatusTest<Program>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlProgram.class);
	
	public TestXmlProgram(){super(Program.class);}
	public static Program create(boolean withChildren){return (new TestXmlProgram()).build(withChildren);}   
    
    public Program build(boolean withChildren)
    {
    	Program xml = new Program();
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
		TestXmlProgram test = new TestXmlProgram();
		test.saveReferenceXml();
    }
}