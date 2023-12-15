package org.jeesl.model.xml.text;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.cms.text.Team;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlTeam extends AbstractXmlTextTest<Team>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlTeam.class);
	
	public TestXmlTeam(){super(Team.class);}
	public static Team create(boolean withChildren){return (new TestXmlTeam()).build(withChildren);}
    
    public Team build(boolean withChilds)
    {
    	Team xml = new Team();
    	xml.setVersion(1);
    	xml.setKey("myKey");
    	xml.setValue("myValue");
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlTeam test = new TestXmlTeam();
		test.saveReferenceXml();
    }
}