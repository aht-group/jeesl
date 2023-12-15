package org.jeesl.model.xml.system.status;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.locale.status.Levels;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlLevels extends AbstractXmlStatusTest<Levels>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlLevels.class);
	
	public TestXmlLevels(){super(Levels.class);}
	public static Levels create(boolean withChildren){return (new TestXmlLevels()).build(withChildren);}   
    
    public Levels build(boolean withChilds)
    {
    	Levels xml = new Levels();
    	xml.setGroup("myGroup");
    	
    	if(withChilds)
    	{
    		xml.getLevel().add(TestXmlLevel.create(false));
    	}
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlLevels test = new TestXmlLevels();
		test.saveReferenceXml();
    }
}