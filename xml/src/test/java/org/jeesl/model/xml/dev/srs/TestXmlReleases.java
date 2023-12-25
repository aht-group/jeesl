package org.jeesl.model.xml.dev.srs;

import org.jeesl.JeeslXmlTestBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlReleases extends AbstractXmlSrsTest<Releases>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlReleases.class);
	
	public TestXmlReleases(){super(Releases.class);}
	public static Releases create(boolean withChildren){return (new TestXmlReleases()).build(withChildren);}
    
    public Releases build(boolean withChildren)
    {
    	Releases xml = new Releases();
    	xml.setModule("myModule");
    	
    	if(withChildren)
    	{
    		xml.getReleases().add(TestXmlReleases.create(false));xml.getReleases().add(TestXmlReleases.create(false));
    		xml.getRelease().add(TestXmlRelease.create(false));xml.getRelease().add(TestXmlRelease.create(false));
    	}
    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlReleases test = new TestXmlReleases();
		test.saveReferenceXml();
    }
}