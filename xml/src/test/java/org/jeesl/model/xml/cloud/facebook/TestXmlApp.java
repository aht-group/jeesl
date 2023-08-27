package org.jeesl.model.xml.cloud.facebook;

import org.exlp.model.xml.net.Url;
import org.jeesl.JeeslXmlTestBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.cloud.facebook.App;

public class TestXmlApp extends AbstractXmlFacebookTest<App>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlApp.class);
	
	public TestXmlApp(){super(App.class);}
	public static App create(boolean withChildren){return (new TestXmlApp()).build(withChildren);}
    
    public App build(boolean withChilds)
    {
    	App xml = new App();
    	xml.setAppId("123");
    	xml.setScope("myScope");
    	xml.setSecret("mySecret");
    	
    	App.Redirect redirect = new App.Redirect();
    	
    	Url url = new Url();
    	url.setCode("myCode");
    	url.setValue("http://my.app");
    	redirect.getUrl().add(url);
    	
    	xml.setRedirect(redirect);
    	
    	return xml;
    }

	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlApp test = new TestXmlApp();
		test.saveReferenceXml();
    }
}