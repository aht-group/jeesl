package org.jeesl.model.xml.status;

import org.jeesl.UtilsXmlTestBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.status.SubProgram;
import net.sf.ahtutils.xml.status.TestXmlDescriptions;
import net.sf.ahtutils.xml.status.TestXmlLangs;

public class TestXmlSubProgram extends AbstractXmlStatusTest<SubProgram>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlSubProgram.class);
	
	public TestXmlSubProgram(){super(SubProgram.class);}
	public static SubProgram create(boolean withChildren){return (new TestXmlSubProgram()).build(withChildren);}   
    
    public SubProgram build(boolean withChildren)
    {
    	SubProgram xml = new SubProgram();
    	xml.setId(123);
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
		UtilsXmlTestBootstrap.init();
		TestXmlSubProgram test = new TestXmlSubProgram();
		test.saveReferenceXml();
    }
}