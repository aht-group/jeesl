package org.jeesl.model.xml.system.core;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.system.constraint.Constraint;
import org.jeesl.model.xml.system.status.TestXmlDescription;
import org.jeesl.model.xml.system.status.TestXmlDescriptions;
import org.jeesl.model.xml.system.status.TestXmlLang;
import org.jeesl.model.xml.system.status.TestXmlLangs;
import org.jeesl.model.xml.system.status.TestXmlType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlConstraint extends AbstractXmlSystemTest<Constraint>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlConstraint.class);
	
	public TestXmlConstraint(){super(Constraint.class);}
	public static Constraint create(boolean withChildren){return (new TestXmlConstraint()).build(withChildren);}
        
    public Constraint build(boolean withChilds)
    {
    	Constraint xml = new Constraint();
    	xml.setCode("myCode");
    	
    	if(withChilds)
    	{
    		xml.setType(TestXmlType.create(false));
    		xml.setLangs(TestXmlLangs.create(false));
    		xml.setDescriptions(TestXmlDescriptions.create(false));
    		xml.getConstraintAttribute().add(TestXmlConstraintAttribute.create(false));xml.getConstraintAttribute().add(TestXmlConstraintAttribute.create(false));
    		xml.setConstraintSolution(TestXmlConstraintSolution.create(false));
    		xml.setLang(TestXmlLang.create(false));
    		xml.setDescription(TestXmlDescription.create(false));
    	}
    	return xml;
    }
    
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlConstraint test = new TestXmlConstraint();
		test.saveReferenceXml();
    }
}