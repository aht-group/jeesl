package net.sf.ahtutils.xml.qa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.test.UtilsXmlTestBootstrap;
import net.sf.ahtutils.xml.status.TestXmlStatus;

public class TestXmlInfo extends AbstractXmlQaTest<Info>
{
	final static Logger logger = LoggerFactory.getLogger(Info.class);
	
	public TestXmlInfo(){super(Info.class);}
	public static Info create(boolean withChildren){return (new TestXmlInfo()).build(withChildren);}   
    
    public Info build(boolean withChilds)
    {
    	Info xml = new Info();
    	xml.setId(123);
    	xml.setRecord(TestXmlInfo.getDefaultXmlDate());
    	
    	if(withChilds)
    	{
    		xml.setStatus(TestXmlStatus.create(false));
    		xml.setComment(TestXmlComment.create(false));
    	}
    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		UtilsXmlTestBootstrap.init();
		TestXmlInfo test = new TestXmlInfo();
		test.saveReferenceXml();
    }
}