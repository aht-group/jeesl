package org.jeesl.model.xml.system.io.db;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.db.Db;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestDb extends AbstractXmlDbseedTest<Db>
{
	final static Logger logger = LoggerFactory.getLogger(AbstractXmlDbseedTest.class);
	
	public TestDb(){super(Db.class);}
	public static Db create(boolean withChildren){return (new TestDb()).build(withChildren);}
	
    public Db build(boolean withChilds)
    {
    	Db xml = new Db();
    	xml.setPathIde("myPathIde");
    	xml.setPathExport("myPathExport");
    	
    	if(withChilds)
    	{
    		xml.getSeed().add(TestSeed.create(false));
    	}
    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestDb test = new TestDb();
		test.saveReferenceXml();
    }
}