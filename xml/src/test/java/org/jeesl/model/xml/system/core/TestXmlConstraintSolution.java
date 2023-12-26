package org.jeesl.model.xml.system.core;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.system.constraint.ConstraintSolution;
import org.jeesl.model.xml.system.status.TestXmlDescriptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlConstraintSolution extends AbstractXmlSystemTest<ConstraintSolution>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlConstraintSolution.class);
	
	public TestXmlConstraintSolution(){super(ConstraintSolution.class);}
	public static ConstraintSolution create(boolean withChildren){return (new TestXmlConstraintSolution()).build(withChildren);}
        
    public ConstraintSolution build(boolean withChilds)
    {
    	ConstraintSolution xml = new ConstraintSolution();
    	
    	if(withChilds)
    	{
    		xml.setDescriptions(TestXmlDescriptions.create(false));
    	}
    	return xml;
    }
    
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlConstraintSolution test = new TestXmlConstraintSolution();
		test.saveReferenceXml();
    }
}