package org.jeesl.factory.txt.system.locale;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jeesl.AbstractJeeslUtilTest;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestTranslationMap extends AbstractJeeslUtilTest
{
	final static Logger logger = LoggerFactory.getLogger(TestTranslationMap.class);
	
	private TranslationMap tMap;
	
	private String[] t1,t2,t3;
	private List<String[]> translations;
	private Set<String> langs;
	private Map<String,String> deTranslations;
	
	@Before
	public void init()
	{
		tMap = new TranslationMap();
		translations = new ArrayList<String[]>();
		langs = new HashSet<String>();
		deTranslations = new Hashtable<String,String>();
		
		t1 = new String[] {"de","key1","lang1"};translations.add(t1);
		t2 = new String[] {"de","key2","lang2"};translations.add(t2);
		t3 = new String[] {"en","key3","lang3"};translations.add(t3);
	}
	
	private void addAllTranslations()
	{
		for(String[] s : translations)
		{
			tMap.add(s);
			langs.add(s[0]);
			if(s[0].equals("de")){deTranslations.put(s[1], s[2]);}
		}
	}
	
	@Test
	public void sizeLanguages()
    {	
		tMap.add(t1);
		Assert.assertEquals(1, tMap.sizeLanguages());
		
		tMap.add(t2);
		Assert.assertEquals(1, tMap.sizeLanguages());
		
		tMap.add(t3);
		Assert.assertEquals(2, tMap.sizeLanguages());
    }
	
	@Test
	public void sizeKeys() throws JeeslNotFoundException
	{
		addAllTranslations();
		Assert.assertEquals(2, tMap.sizeKeys("de"));
		Assert.assertEquals(1, tMap.sizeKeys("en"));
	}
	
	@Test(expected=JeeslNotFoundException.class)
	public void sizeKeysUnknown() throws JeeslNotFoundException
	{
		tMap.sizeKeys("-1");
	}
	
	@Test
	public void translate()
	{
		addAllTranslations();
		for(String[] s : translations)
		{
			Assert.assertEquals(s[2], tMap.translate(s[0], s[1]));
		}
	}
	
	@Test(expected=JeeslNotFoundException.class)
	public void translateUnknowLang() throws JeeslNotFoundException
	{
		addAllTranslations();
		tMap.translateWithException("-1","-1");
	}
	
	@Test(expected=JeeslNotFoundException.class)
	public void translateUnknowKey() throws JeeslNotFoundException
	{
		addAllTranslations();
		tMap.translateWithException("de","-1");
	}
	
	@Test
	public void getLangKeys()
	{
		addAllTranslations();
		List<String> list = tMap.getLangKeys();
		Assert.assertEquals(langs.size(), list.size());
		for(String langKey : list)
		{
			Assert.assertTrue(langs.contains(langKey));
		}
	}
	
	@Test
	public void getTranslationKeys() throws JeeslNotFoundException
	{
		addAllTranslations();
		List<String> list = tMap.getTranslationKeys("de");
		Assert.assertEquals(deTranslations.size(), list.size());
		for(String key : list)
		{
			Assert.assertTrue(deTranslations.containsKey(key));
			Assert.assertEquals(deTranslations.get(key), tMap.translate("de", key));
		}
	}
	
	@Test(expected=JeeslNotFoundException.class)
	public void getTranslationKeysUnknows() throws JeeslNotFoundException
	{
		tMap.getTranslationKeys("-1");
	}
}
