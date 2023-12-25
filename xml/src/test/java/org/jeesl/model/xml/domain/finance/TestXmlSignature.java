package org.jeesl.model.xml.domain.finance;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.system.security.TestXmlRole;
import org.jeesl.model.xml.system.status.TestXmlLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.finance.Signature;

public class TestXmlSignature extends AbstractXmlFinanceTest<Signature>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlSignature.class);
	
	public TestXmlSignature(){super(Signature.class);}
	public static Signature create(boolean withChildren){return (new TestXmlSignature()).build(withChildren);}
    
    public Signature build(boolean withChildren)
    {
    	Signature xml = new Signature();
    	xml.setPosition(1);
    	xml.setCode("myCode");
    	xml.setLabel("myLabel");
    	
    	if(withChildren)
    	{
    		xml.setLevel(TestXmlLevel.create(false));
    		xml.setRole(TestXmlRole.create(false));
    	}
    	    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlSignature test = new TestXmlSignature();
		test.saveReferenceXml();
    }
}