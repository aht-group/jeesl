package org.jeesl.model.xml.system.revision;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.label.Revision;
import org.jeesl.model.xml.system.security.TestXmlUser;
import org.jeesl.model.xml.system.status.TestXmlStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TestXmlRevision extends AbstractXmlRevisionTest<Revision>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlRevision.class);
	
	public TestXmlRevision(){super(Revision.class);}
	public static Revision create(boolean withChildren){return (new TestXmlRevision()).build(withChildren);} 
    
    public Revision build(boolean withChilds)
    {
    	Revision xml = new Revision();
    	xml.setRecord(AbstractXmlRevisionTest.getDefaultXmlDate());
    	
    	if(withChilds)
    	{
    		xml.setUser(TestXmlUser.create(false));
    		xml.setStatus(TestXmlStatus.create(false));
    	}
    	    	
    	return xml;
    }
    
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();	
		TestXmlRevision test = new TestXmlRevision();
		test.saveReferenceXml();
    }
}