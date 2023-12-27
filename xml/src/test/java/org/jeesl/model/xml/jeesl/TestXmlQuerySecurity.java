package org.jeesl.model.xml.jeesl;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.db.query.QuerySecurity;
import org.jeesl.model.xml.system.security.TestXmlRole;
import org.jeesl.model.xml.system.security.TestXmlStaff;
import org.jeesl.model.xml.system.security.TestXmlUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlQuerySecurity extends AbstractXmlJeeslTest<QuerySecurity>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlQuerySecurity.class);
	
	public TestXmlQuerySecurity(){super(QuerySecurity.class);}
	public static QuerySecurity create(boolean withChildren){return (new TestXmlQuerySecurity()).build(withChildren);}
    
    public QuerySecurity build(boolean withChilds)
    {
		QuerySecurity xml = new QuerySecurity();
	        	
	    	if(withChilds)
	    	{
	    		xml.setUser(TestXmlUser.create(false));
	    		xml.setStaff(TestXmlStaff.create(false));
	    		xml.setRole(TestXmlRole.create(false));
	    	}
	    	
	    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlQuerySecurity test = new TestXmlQuerySecurity();
		test.saveReferenceXml();
    }
}