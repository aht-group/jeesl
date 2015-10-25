package net.sf.ahtutils.xml.audit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.test.UtilsXmlTestBootstrap;

public class TestXmlRevision extends AbstractXmlAuditTest<Revision>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlRevision.class);
	
	public TestXmlRevision(){super(Revision.class);}
	public static Revision create(boolean withChildren){return (new TestXmlRevision()).build(withChildren);}
    
    public Revision build(boolean withChilds)
    {
    	Revision xml = new Revision();
    	xml.setRev(123);
    	xml.setDate(AbstractXmlAuditTest.getDefaultXmlDate());
    	
    	if(withChilds)
    	{
    		xml.setUser(TestXmlUser.create(false));
    		xml.getScope().add(TestXmlScope.create(false));xml.getScope().add(TestXmlScope.create(false));
    	}
    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		UtilsXmlTestBootstrap.init();
		TestXmlRevision test = new TestXmlRevision();
		test.saveReferenceXml();
    }
}