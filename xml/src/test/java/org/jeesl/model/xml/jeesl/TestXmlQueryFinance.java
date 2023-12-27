package org.jeesl.model.xml.jeesl;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.domain.finance.TestXmlFinance;
import org.jeesl.model.xml.io.db.query.QueryFinance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlQueryFinance extends AbstractXmlJeeslTest<QueryFinance>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlQueryFinance.class);
	
	public TestXmlQueryFinance(){super(QueryFinance.class);}
	public static QueryFinance create(boolean withChildren){return (new TestXmlQueryFinance()).build(withChildren);}
    
    public QueryFinance build(boolean withChilds)
    {
    	QueryFinance xml = new QueryFinance();
	        	
	    	if(withChilds)
	    	{
	    		xml.setFinance(TestXmlFinance.create(false));
	    	}
	    	
	    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlQueryFinance test = new TestXmlQueryFinance();
		test.saveReferenceXml();
    }
}