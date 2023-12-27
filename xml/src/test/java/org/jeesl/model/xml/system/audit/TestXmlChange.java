package org.jeesl.model.xml.system.audit;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.db.revision.Change;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlChange extends AbstractXmlAuditTest<Change>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlChange.class);
	
	public TestXmlChange(){super(Change.class);}
	public static Change create(boolean withChildren){return (new TestXmlChange()).build(withChildren);}
    
    public Change build(boolean withChilds)
    {
    	Change xml = new Change();
    	xml.setAid(1);
    	xml.setAction("action");
    	xml.setText("myValue");
    	xml.setType("myType");
    	
    	if(withChilds)
    	{
    		xml.setScope(TestXmlScope.create(false));
    	}
    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlChange test = new TestXmlChange();
		test.saveReferenceXml();
    }
}