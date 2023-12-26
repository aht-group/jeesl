package org.jeesl.model.xml.system.core;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.system.constraint.ConstraintAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlConstraintAttribute extends AbstractXmlSystemTest<ConstraintAttribute>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlConstraintAttribute.class);
	
	public TestXmlConstraintAttribute(){super(ConstraintAttribute.class);}
	public static ConstraintAttribute create(boolean withChildren){return (new TestXmlConstraintAttribute()).build(withChildren);}
        
    public ConstraintAttribute build(boolean withChilds)
    {
    	ConstraintAttribute xml = new ConstraintAttribute();
    	xml.setClazz(String.class.getName());
    	xml.setAttribute("myAttribute");
    	
    	if(withChilds)
    	{
    		
    	}
    	
    	return xml;
    }
    
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlConstraintAttribute test = new TestXmlConstraintAttribute();
		test.saveReferenceXml();
    }
}