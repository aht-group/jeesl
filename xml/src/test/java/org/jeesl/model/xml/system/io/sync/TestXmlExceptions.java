package org.jeesl.model.xml.system.io.sync;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.ssi.sync.Exceptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlExceptions extends AbstractXmlSyncTest<Exceptions>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlExceptions.class);
	
	public TestXmlExceptions(){super(Exceptions.class);}
	public static Exceptions create(boolean withChildren){return (new TestXmlExceptions()).build(withChildren);}
    
    public Exceptions build(boolean withChilds)
    {
    	Exceptions xml = new Exceptions();

    	if(withChilds)
    	{
    		xml.getException().add(TestXmlException.create(false));
    		xml.getException().add(TestXmlException.create(false));
    	}
    	
    	return xml;
    }
    
    public void save() {save(create(true),fXml);}
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();	
		TestXmlExceptions test = new TestXmlExceptions();
		test.saveReferenceXml();
    }
}