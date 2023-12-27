package org.jeesl.model.xml.system.audit;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.db.revision.Revisions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlRevisions extends AbstractXmlAuditTest<Revisions>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlRevisions.class);
	
	public TestXmlRevisions(){super(Revisions.class);}
	public static Revisions create(boolean withChildren){return (new TestXmlRevisions()).build(withChildren);}
    
    public Revisions build(boolean withChilds)
    {
    	Revisions xml = new Revisions();
    	
    	if(withChilds)
    	{
    		xml.getRevision().add(TestXmlRevision.create(false));
    		xml.getRevision().add(TestXmlRevision.create(false));
    	}
    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlRevisions test = new TestXmlRevisions();
		test.saveReferenceXml();
    }
}