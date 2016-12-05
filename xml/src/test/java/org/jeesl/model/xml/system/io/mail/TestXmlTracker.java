package org.jeesl.model.xml.system.io.mail;

import org.jeesl.JeeslXmlTestBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.test.AbstractAhtUtilsXmlTest;
import net.sf.ahtutils.xml.mail.Tracker;

public class TestXmlTracker extends AbstractXmlMailTest<Tracker>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlTracker.class);
	
	public TestXmlTracker(){super(Tracker.class);}
	public static Tracker create(boolean withChildren){return (new TestXmlTracker()).build(withChildren);}
	
    public Tracker build(boolean withChilds)
    {
    	Tracker xml = new Tracker();
    	xml.setId(123);
    	xml.setRefId(345);
    	xml.setType("myType");
    	xml.setCreated(AbstractAhtUtilsXmlTest.getDefaultXmlDate());
    	xml.setSent(AbstractAhtUtilsXmlTest.getDefaultXmlDate());
    	xml.setRetryCounter(1);
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlTracker test = new TestXmlTracker();
		test.saveReferenceXml();
    }
}