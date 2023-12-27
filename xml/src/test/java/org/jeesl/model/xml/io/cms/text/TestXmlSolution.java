package org.jeesl.model.xml.io.cms.text;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.cms.chars.Solution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlSolution extends AbstractXmlTextOldTest<Solution>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlSolution.class);
	
	public TestXmlSolution(){super(Solution.class);}
	public static Solution create(boolean withChildren){return (new TestXmlSolution()).build(withChildren);}
    
    public Solution build(boolean withChilds){return create(withChilds,"myKey","myValue");}
    public static Solution create(boolean withChilds, String key, String description)
    {
    	Solution xml = new Solution();
    	xml.setVersion(1);
    	xml.setKey(key);
    	xml.setValue(description);
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlSolution test = new TestXmlSolution();
		test.saveReferenceXml();
    }
}