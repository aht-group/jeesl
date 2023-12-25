package org.jeesl.model.xml.domain.finance;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.system.status.TestXmlLevels;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.finance.Signatures;

public class TestXmlSignatures extends AbstractXmlFinanceTest<Signatures>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlSignatures.class);
	
	public TestXmlSignatures(){super(Signatures.class);}
	public static Signatures create(boolean withChildren){return (new TestXmlSignatures()).build(withChildren);}
    
    public Signatures build(boolean withChildren)
    {
    	Signatures xml = new Signatures();
    	xml.setCode("myCode");
    	xml.setLabel("myLabel");
    	
    	if(withChildren)
    	{
    		xml.setLevels(TestXmlLevels.create(false));
    		xml.getSignatures().add(TestXmlSignatures.create(false));
    		xml.getSignatures().add(TestXmlSignatures.create(false));
    		
    		xml.getSignature().add(TestXmlSignature.create(false));
    		xml.getSignature().add(TestXmlSignature.create(false));
    	}
    	    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlSignatures test = new TestXmlSignatures();
		test.saveReferenceXml();
    }
}