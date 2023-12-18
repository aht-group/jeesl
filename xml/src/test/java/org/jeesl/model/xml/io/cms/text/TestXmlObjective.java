package org.jeesl.model.xml.io.cms.text;

import org.jeesl.JeeslXmlTestBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.text.Objective;

public class TestXmlObjective extends AbstractXmlTextOldTest<Objective>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlObjective.class);
	
	public TestXmlObjective(){super(Objective.class);}
	public static Objective create(boolean withChildren){return (new TestXmlObjective()).build(withChildren);}
    
    public Objective build(boolean withChilds){return create(withChilds,"myKey","myValue");}
    public static Objective create(boolean withChilds, String key, String description)
    {
    	Objective xml = new Objective();
    	xml.setVersion(1);
    	xml.setKey(key);
    	xml.setValue(description);
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlObjective test = new TestXmlObjective();
		test.saveReferenceXml();
    }
}