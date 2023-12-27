package org.jeesl.model.xml.system.audit;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.db.revision.Scope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlScope extends AbstractXmlAuditTest<Scope>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlScope.class);
	
	public TestXmlScope(){super(Scope.class);}
	public static Scope create(boolean withChildren){return (new TestXmlScope()).build(withChildren);}
    
    public Scope build(boolean withChilds)
    {
    	Scope xml = new Scope();
    	xml.setId(123);
    	xml.setCategory("myCategory");
    	xml.setEntity("myEntity");
    	xml.setClazz("myClass");
    	
    	if(withChilds)
    	{
    		xml.setRevision(TestXmlRevision.create(false));
    		xml.getChange().add(TestXmlChange.create(false));xml.getChange().add(TestXmlChange.create(false));
    	}
    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlScope test = new TestXmlScope();
		test.saveReferenceXml();
    }
}