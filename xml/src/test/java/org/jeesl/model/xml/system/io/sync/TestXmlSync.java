package org.jeesl.model.xml.system.io.sync;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.ssi.sync.Sync;
import org.jeesl.model.xml.system.status.TestXmlStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlSync extends AbstractXmlSyncTest<Sync>
{
	final static Logger logger = LoggerFactory.getLogger(Sync.class);
	
	public TestXmlSync(){super(Sync.class);}
	public static Sync create(boolean withChildren){return (new TestXmlSync()).build(withChildren);}
	
    public Sync build(boolean withChilds)
    {
    	Sync xml = new Sync();
    	xml.setClientId(123l);
    	xml.setServerId(345l);
    	
    	if(withChilds)
    	{
    		xml.setStatus(TestXmlStatus.create(false));
    		xml.setResult(org.jeesl.model.xml.system.status.TestXmlResult.create(false));
    	}
    	
    	return xml;
    }

	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();	
		TestXmlSync test = new TestXmlSync();
		test.saveReferenceXml();
    }
}