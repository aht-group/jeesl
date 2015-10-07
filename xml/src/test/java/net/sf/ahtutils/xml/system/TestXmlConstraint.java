package net.sf.ahtutils.xml.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.test.UtilsXmlTestBootstrap;
import net.sf.ahtutils.xml.status.TestXmlDescriptions;
import net.sf.ahtutils.xml.status.TestXmlLangs;
import net.sf.ahtutils.xml.status.TestXmlType;

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
    	}
    	
    	return xml;
    }
    
	public static void main(String[] args)
    {
		UtilsXmlTestBootstrap.init();
		TestXmlConstraint test = new TestXmlConstraint();
		test.saveReferenceXml();
    }
}