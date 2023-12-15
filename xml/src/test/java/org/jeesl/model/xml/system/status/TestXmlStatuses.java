package org.jeesl.model.xml.system.status;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.locale.status.Statuses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlStatuses extends AbstractXmlStatusTest<Statuses>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlStatuses.class);
	
	public TestXmlStatuses(){super(Statuses.class);}
	public static Statuses create(boolean withChildren){return (new TestXmlStatuses()).build(withChildren);} 
    
    public Statuses build(boolean withChilds)
    {
        Statuses xml = new Statuses();
    	xml.setGroup("myGroup");

    	if(withChilds)
    	{
    		xml.getStatus().add(TestXmlStatus.create(false));
            xml.getStatus().add(TestXmlStatus.create(false));
    	}
    	return xml;
    }

	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlStatuses test = new TestXmlStatuses();
		test.saveReferenceXml();
    }
}