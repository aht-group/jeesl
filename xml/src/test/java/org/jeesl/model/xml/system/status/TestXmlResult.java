package org.jeesl.model.xml.system.status;

import org.jeesl.JeeslXmlTestBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.status.Result;

public class TestXmlResult extends AbstractXmlStatusTest<Result>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlResult.class);
	
	public TestXmlResult(){super(Result.class);}
	public static Result create(boolean withChildren){return (new TestXmlResult()).build(withChildren);} 
    
    public Result build(boolean withChilds)
    {
    	Result xml = new Result();
    	xml.setId(123l);
    	xml.setCode("myCode");
    	xml.setVisible(true);
    	xml.setGroup("myGroup");
    	xml.setLabel("myLabel");
    	xml.setImage("test/green.png");
    	xml.setPosition(1);
    	
    	if(withChilds)
    	{
    		xml.setLangs(TestXmlLangs.create(false));
    		xml.setDescriptions(TestXmlDescriptions.create(false));
//    		xml.setParent(TestXmlParent.create(false));
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