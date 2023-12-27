package org.jeesl.model.xml.system.io.sync;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.ssi.sync.Result;
import org.jeesl.model.xml.system.status.TestXmlStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlResult extends AbstractXmlSyncTest<Result>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlResult.class);
	
	public TestXmlResult(){super(Result.class);}
	public static Result create(boolean withChildren){return (new TestXmlResult()).build(withChildren);}
    
    public Result build(boolean withChilds)
    {
    	Result xml = new Result();
    	xml.setTotal(3l);
    	xml.setSuccess(2l);
    	xml.setFail(1l);
    	xml.setSkip(0l);
    	
    	if(withChilds)
    	{
    		xml.setStatus(TestXmlStatus.create(false));
    	}
    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlResult test = new TestXmlResult();
		test.saveReferenceXml();
    }
}