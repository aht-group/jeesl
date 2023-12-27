package org.jeesl.model.xml.system.io.sync;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.ssi.sync.Mappings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlMappings extends AbstractXmlSyncTest<Mappings>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlMappings.class);
	
	public TestXmlMappings(){super(Mappings.class);}
	public static Mappings create(boolean withChildren){return (new TestXmlMappings()).build(withChildren);}
    
    public Mappings build(boolean withChilds)
    {
    	Mappings xml = new Mappings();
    	
    	if(withChilds)
    	{
    		xml.getMapper().add(TestXmlMapper.create(false));
    		xml.getMapper().add(TestXmlMapper.create(false));
    	}
    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlMappings test = new TestXmlMappings();
		test.saveReferenceXml();
    }
}