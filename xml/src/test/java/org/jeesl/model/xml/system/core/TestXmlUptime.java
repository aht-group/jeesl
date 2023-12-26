package org.jeesl.model.xml.system.core;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.system.constraint.Uptime;
import org.jeesl.model.xml.system.status.TestXmlType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlUptime extends AbstractXmlSystemTest<Uptime>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlUptime.class);
	
	public TestXmlUptime(){super(Uptime.class);}
	public static Uptime create(boolean withChildren){return (new TestXmlUptime()).build(withChildren);}
    
    public Uptime build(boolean withChilds)
    {
    	Uptime xml = new Uptime();
    	xml.setSince(TestXmlUptime.getDefaultXmlDate());
    	
    	if(withChilds)
    	{
    		xml.setType(TestXmlType.create(false));
    	}
    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlUptime test = new TestXmlUptime();
		test.saveReferenceXml();
    }
}