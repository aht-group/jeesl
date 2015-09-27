package net.sf.ahtutils.xml.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.test.UtilsXmlTestBootstrap;
import net.sf.ahtutils.xml.status.TestXmlType;

public class TestXmlRequest extends AbstractXmlSystemTest<Request>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlRequest.class);
	
	public TestXmlRequest(){super(Request.class);}
	public static Request create(boolean withChildren){return (new TestXmlRequest()).build(withChildren);}
    
    public Request build(boolean withChilds)
    {
    	Request xml = new Request();
    	xml.setCounter(123);
    	
    	if(withChilds)
    	{
    		xml.setType(TestXmlType.create(false));
    	}
    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		UtilsXmlTestBootstrap.init();
		TestXmlRequest test = new TestXmlRequest();
		test.saveReferenceXml();
    }
}