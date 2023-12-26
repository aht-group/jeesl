package org.jeesl.model.xml.system.core;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.system.constraint.ConstraintScope;
import org.jeesl.model.xml.system.status.TestXmlDescription;
import org.jeesl.model.xml.system.status.TestXmlDescriptions;
import org.jeesl.model.xml.system.status.TestXmlLang;
import org.jeesl.model.xml.system.status.TestXmlLangs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlConstraintScope extends AbstractXmlSystemTest<ConstraintScope>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlConstraintScope.class);
	
	public TestXmlConstraintScope(){super(ConstraintScope.class);}
	public static ConstraintScope create(boolean withChildren){return (new TestXmlConstraintScope()).build(withChildren);}
        
    public ConstraintScope build(boolean withChilds)
    {
    	ConstraintScope xml = new ConstraintScope();
    	xml.setId(123l);
    	xml.setCode("myCode");
    	xml.setCategory("myCategory");
    	
    	if(withChilds)
    	{
    		xml.setLangs(TestXmlLangs.create(false));
    		xml.setDescriptions(TestXmlDescriptions.create(false));
    		
    		xml.getConstraint().add(TestXmlConstraint.create(false));xml.getConstraint().add(TestXmlConstraint.create(false));
    		
    		xml.setLang(TestXmlLang.create(false));
    		xml.setDescription(TestXmlDescription.create(false));
    	}
    	
    	return xml;
    }
    
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlConstraintScope test = new TestXmlConstraintScope();
		test.saveReferenceXml();
    }
}