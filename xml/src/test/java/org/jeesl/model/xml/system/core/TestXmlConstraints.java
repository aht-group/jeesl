package org.jeesl.model.xml.system.core;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.system.constraint.Constraints;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlConstraints extends AbstractXmlSystemTest<Constraints>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlConstraints.class);
	
	public TestXmlConstraints(){super(Constraints.class);}
	public static Constraints create(boolean withChildren){return (new TestXmlConstraints()).build(withChildren);}
        
    public Constraints build(boolean withChilds)
    {
    	Constraints xml = new Constraints();

    	if(withChilds)
    	{
    		xml.getConstraintScope().add(TestXmlConstraintScope.create(false));xml.getConstraintScope().add(TestXmlConstraintScope.create(false));
    		xml.getConstraintAttribute().add(TestXmlConstraintAttribute.create(false));xml.getConstraintAttribute().add(TestXmlConstraintAttribute.create(false));
    	}
    	
    	return xml;
    }
    
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlConstraints test = new TestXmlConstraints();
		test.saveReferenceXml();
    }
}