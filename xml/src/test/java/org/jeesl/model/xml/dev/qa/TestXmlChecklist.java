package org.jeesl.model.xml.dev.qa;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.system.security.TestXmlStaff;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.qa.Checklist;

public class TestXmlChecklist extends AbstractXmlQaTest<Checklist>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlChecklist.class);
	
	public TestXmlChecklist(){super(Checklist.class);}
	public static Checklist create(boolean withChildren){return (new TestXmlChecklist()).build(withChildren);}  
    
    public Checklist build(boolean withChilds)
    {
    	Checklist xml = new Checklist();
    	
    	if(withChilds)
    	{
    		xml.setStaff(TestXmlStaff.create(false));
    		xml.getCategory().add(TestXmlCategory.create(false));xml.getCategory().add(TestXmlCategory.create(false));
    	}
    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlChecklist test = new TestXmlChecklist();
		test.saveReferenceXml();
    }
}