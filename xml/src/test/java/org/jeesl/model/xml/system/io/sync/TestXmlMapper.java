package org.jeesl.model.xml.system.io.sync;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.ssi.sync.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlMapper extends AbstractXmlSyncTest<Mapper>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlMapper.class);
    
	public TestXmlMapper(){super(Mapper.class);}
	public static Mapper create(boolean withChildren){return (new TestXmlMapper()).build(withChildren);}
    
    public Mapper build(boolean withChilds)
    {
    	Mapper xml = new Mapper();
    	xml.setClazz(TestXmlMapper.class.getName());
    	xml.setOldId(123);
    	xml.setNewId(345);
    	xml.setOldCode("mc1");
    	xml.setNewCode("mc2");
    	xml.setCode("myCode");
    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlMapper test = new TestXmlMapper();
		test.saveReferenceXml();
    }
}