package net.sf.ahtutils.xml.qa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.test.UtilsXmlTestBootstrap;
import net.sf.ahtutils.xml.status.TestXmlStatus;

public class TestXmlResult extends AbstractXmlQaTest<Result>
{
	final static Logger logger = LoggerFactory.getLogger(net.sf.ahtutils.xml.qa.Test.class);
	
	public TestXmlResult(){super(Result.class);}
	public static Result create(boolean withChildren){return (new TestXmlResult()).build(withChildren);}  
    
    @Override public Result build(boolean withChilds)
    {
    	Result xml = new Result();
    	xml.setId(123);
    	xml.setRecord(TestXmlResult.getDefaultXmlDate());
    	
    	if(withChilds)
    	{
    		xml.setStatus(TestXmlStatus.create(false));
    		xml.setActual(TestXmlActual.create(false));
    		xml.setComment(TestXmlComment.create(false));
    	}
    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		UtilsXmlTestBootstrap.init();
		TestXmlResult test = new TestXmlResult();
		test.saveReferenceXml();
    }
}