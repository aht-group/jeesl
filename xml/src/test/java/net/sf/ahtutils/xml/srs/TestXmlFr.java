package net.sf.ahtutils.xml.srs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.test.UtilsXmlTestBootstrap;
import net.sf.ahtutils.xml.text.TestXmlDescription;
import net.sf.ahtutils.xml.text.TestXmlRemark;

public class TestXmlFr extends AbstractXmlSrsTest<Fr>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlFr.class);
	
	public TestXmlFr(){super(Fr.class);}
	public static Fr create(boolean withChildren){return (new TestXmlFr()).build(withChildren);}
    
    public Fr build(boolean withChildren)
    {
    	Fr xml = new Fr();
    	xml.setId(123);
    	xml.setCode("myCode");
    	xml.setName("myName");
    	
    	if(withChildren)
    	{
    		xml.setDescription(TestXmlDescription.create(false));
    		xml.setRemark(TestXmlRemark.create(false));
    	}
    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		UtilsXmlTestBootstrap.init();
		TestXmlFr test = new TestXmlFr();
		test.saveReferenceXml();
    }
}