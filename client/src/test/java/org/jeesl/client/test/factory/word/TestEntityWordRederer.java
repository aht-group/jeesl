package org.jeesl.client.test.factory.word;

import java.io.IOException;

import org.exlp.util.io.log.LoggerInit;
import org.exlp.util.jx.JaxbUtil;
import org.jeesl.doc.word.EntityWordRenderer;
import org.jeesl.model.xml.io.label.Entities;
import org.jeesl.model.xml.io.label.Entity;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aspose.words.Document;

public class TestEntityWordRederer extends AbstractWordCli
{
	final static Logger logger = LoggerFactory.getLogger(TestEntityWordRederer.class);
	
	private static String xmlEntities = "../util/src/test/resources/data/xml/word/entity.xml";
	private static String savingDirectory = "target/TestEntitiesRender";
	
	static Document templateDoc;
	
	public TestEntityWordRederer() throws Exception 
	{
//			this.templateDoc = new Document("../util/src/test/resources/data/docx/templates/entity.dotx");
	}
	
	@Test
	public void dummy() {}
	
	public void renderEntities() throws IOException, Exception
	{
		Entities entities = JaxbUtil.loadJAXB(mrl.searchIs(xmlEntities), Entities.class);
		JaxbUtil.info(entities);
		
		int count=0; 
		for (Entity e : entities.getEntity())
		{
			EntityWordRenderer entityRenderer = new EntityWordRenderer(new Document("../util/src/test/resources/data/docx/templates/entity.dotx"),null,null,null, null);
			entityRenderer.render(e, savingDirectory+e.getCode()+".docx",false);			
			count++;
			
		}
	}
	
	public static void main(String[] args) throws Exception
	{
		LoggerInit loggerInit = new LoggerInit("log4j.xml");
		loggerInit.path("jeesl/client/config");
		loggerInit.init();
		TestEntityWordRederer cli = new TestEntityWordRederer();
		cli.renderEntities();	  
	}
}
