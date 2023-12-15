package org.jeesl.model.xml.system.io.sync;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.locale.status.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.test.AbstractAhtUtilsXmlTest;
import net.sf.ahtutils.xml.sync.DataUpdate;

public class TestXmlDataUpdate extends AbstractXmlSyncTest<DataUpdate>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlDataUpdate.class);
	
	public TestXmlDataUpdate(){super(DataUpdate.class);}
	public static DataUpdate create(boolean withChildren){return (new TestXmlDataUpdate()).build(withChildren);}
    
    public DataUpdate build(boolean withChilds)
    {
    	DataUpdate xml = new DataUpdate();
    	xml.setBegin(AbstractAhtUtilsXmlTest.getDefaultXmlDate());
    	xml.setFinished(AbstractAhtUtilsXmlTest.getDefaultXmlDate());
    	
    	if(withChilds)
    	{
    		xml.getMapper().add(TestXmlMapper.create(false));
    		xml.getMapper().add(TestXmlMapper.create(false));
    		xml.setResult(TestXmlResult.create(false));
    		xml.setType(new Type());
    		xml.setExceptions(TestXmlExceptions.create(false));
    		xml.getSync().add(TestXmlSync.create(false));
    	}
    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlDataUpdate test = new TestXmlDataUpdate();
		test.saveReferenceXml();
    }
}