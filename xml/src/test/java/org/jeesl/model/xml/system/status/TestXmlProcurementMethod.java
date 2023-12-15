package org.jeesl.model.xml.system.status;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.locale.status.ProcurementMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlProcurementMethod extends AbstractXmlStatusTest<ProcurementMethod>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlProcurementMethod.class);
	
	public TestXmlProcurementMethod(){super(ProcurementMethod.class);}
	public static ProcurementMethod create(boolean withChildren){return (new TestXmlProcurementMethod()).build(withChildren);} 
    
    public ProcurementMethod build(boolean withChilds)
    {
    	ProcurementMethod xml = new ProcurementMethod();
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
    	}
    	
    	return xml;
    }
    
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlProcurementMethod test = new TestXmlProcurementMethod();
		test.saveReferenceXml();
    }
}