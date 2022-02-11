package org.jeesl.controller.processor.system.io.mail;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.jeesl.AbstractJeeslUtilTest;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestMultiSectionParser extends AbstractJeeslUtilTest
{
	final static Logger logger = LoggerFactory.getLogger(TestMultiSectionParser.class);
	
	private FtlMultiSectionParser parser;
	
	@Before public void init()
	{
		parser = new FtlMultiSectionParser();
	}
	
//	@Test
	public void parseCorrectString()
	{
		String text = "##top\\n" +
					  "Rambura,ku wa ${date}\\n\\n" +
    
    				  "##left\\n" +
    				  "INTARA Y'UBURENGERAZUBA\\n" +
    				  "AKARERE KA NYABIHU\\n" +
    				  "UMURENGE WA RAMBURA\\n\\n" +
    
    				  "##right\\n" +
    				  "BWANA UMUNYAMABANGA\\n" +
    				  "NSHINGWABIKORWA W'AKARERE KA\\n" +
    				  "NYABIHU";

		Map<String, String> parsedText = parser.parse(text);
		
		assertEquals(3, parsedText.size());
		assertTrue(parsedText.containsKey("top"));
		assertTrue(parsedText.containsKey("left"));
		assertTrue(parsedText.containsKey("right"));
		assertEquals("<p>Rambura,ku wa ${date}</p>", parsedText.get("top"));
		assertEquals("<p>INTARA Y'UBURENGERAZUBA</p>" +
					 "<p>AKARERE KA NYABIHU</p>" +
					 "<p>UMURENGE WA RAMBURA</p>", parsedText.get("left"));
		assertEquals("<p>BWANA UMUNYAMABANGA</p>" +
					 "<p>NSHINGWABIKORWA W'AKARERE KA</p>" +
					 "<p>NYABIHU</p>", parsedText.get("right"));
	}
	
	@Test public void parseEmptyString()
	{
		assertEquals(new HashMap<>(), parser.parse(""));
	}
	
//	@Test
	public void parseDoubleKeys()
	{
		String text = "##top\\n" +
    				  "Rambura,ku wa ${date}\\n\\n" +
    
    				  "##left\\n" +
    				  "INTARA Y'UBURENGERAZUBA\\n" +
    				  "AKARERE KA NYABIHU\\n" +
    				  "UMURENGE WA RAMBURA\\n\\n" +
    
    				  "##top\\n" +
    				  "BWANA UMUNYAMABANGA\\n" +
    				  "NSHINGWABIKORWA W'AKARERE KA\\n" +
    				  "NYABIHU";
		
		Map<String, String> parsedText = parser.parse(text);
		
		assertEquals(2, parsedText.size());
		assertTrue(parsedText.containsKey("top"));
		assertTrue(parsedText.containsKey("left"));
		assertNotEquals("<p>Rambura,ku wa ${date}</p>", parsedText.get("top"));
		assertEquals("<p>INTARA Y'UBURENGERAZUBA</p>" +
					 "<p>AKARERE KA NYABIHU</p>" +
					 "<p>UMURENGE WA RAMBURA</p>", parsedText.get("left"));
		assertEquals("<p>BWANA UMUNYAMABANGA</p>" +
					 "<p>NSHINGWABIKORWA W'AKARERE KA</p>" +
					 "<p>NYABIHU</p>", parsedText.get("top"));
	}
	
//	@Test
	public void parseStartingWithoutKey()
	{
		String text = "Rambura,ku wa ${date}\\n\\n" +
    
    				  "##left\\n" +
    				  "INTARA Y'UBURENGERAZUBA\\n" +
    				  "AKARERE KA NYABIHU\\n" +
    				  "UMURENGE WA RAMBURA\\n\\n" +
    
    				  "##right\\n" +
    				  "BWANA UMUNYAMABANGA\\n" +
    				  "NSHINGWABIKORWA W'AKARERE KA\\n" +
    				  "NYABIHU";
		
		Map<String,String> parsedText = parser.parse(text);
		
		assertEquals(2, parsedText.size());
		assertFalse(parsedText.containsKey("top"));
		assertTrue(parsedText.containsKey("left"));
		assertTrue(parsedText.containsKey("right"));
		assertFalse(parsedText.values().stream().anyMatch(entry -> entry.indexOf("Rambura,ku wa ${date}") > -1));
		assertEquals("<p>INTARA Y'UBURENGERAZUBA</p>" +
					 "<p>AKARERE KA NYABIHU</p>" +
					 "<p>UMURENGE WA RAMBURA</p>", parsedText.get("left"));
		assertEquals("<p>BWANA UMUNYAMABANGA</p>" +
					 "<p>NSHINGWABIKORWA W'AKARERE KA</p>" +
					 "<p>NYABIHU</p>", parsedText.get("right"));
	}
	
	@Test public void parseWithoutAnyKey()
	{
		String text = "Rambura,ku wa ${date}\\n\\n" +
    
    				  "INTARA Y'UBURENGERAZUBA\\n" +
    				  "AKARERE KA NYABIHU\\n" +
    				  "UMURENGE WA RAMBURA\\n\\n" +
    
    				  "BWANA UMUNYAMABANGA\\n" +
    				  "NSHINGWABIKORWA W'AKARERE KA\\n" +
    				  "NYABIHU";
		
		Map<String, String> parsedText = parser.parse(text);
		
		assertEquals(new HashMap<>(), parsedText);
	}
	
	@Test public void parseKeysOnly()
	{
		String text = "##top\\n\\n" +

    				  "##right\\n\\n\\n" +

    				  "##right\\n";
		
		Map<String, String> parsedText = parser.parse(text);
		
		assertEquals(new HashMap<>(), parsedText);
	}
}
