package org.jeesl.factory.xml.system.lang;

import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;

import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.model.xml.io.locale.status.Lang;
import org.jeesl.model.xml.io.locale.status.Langs;
import org.jeesl.util.comparator.xml.status.LangComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlLangsFactory <L extends JeeslLang>
{
	final static Logger logger = LoggerFactory.getLogger(XmlLangsFactory.class);
		
	private static Comparator<Lang> comparator = LangComparator.factory(LangComparator.Type.key);
	
	private Langs q;
	
	private XmlLangFactory<L> xfLang;
	
	public XmlLangsFactory(Langs q)
	{
		this.q=q;
		if(Objects.nonNull(q.getLang())) {xfLang = new XmlLangFactory<L>(q.getLang().get(0));}
	}
	
	public Langs getUtilsLangs(Map<String,L> mapLangs)
	{
		Langs xml = new Langs();
		
		if(Objects.nonNull(q.getLang()) && mapLangs!=null)
		{
			for(L ahtLang : mapLangs.values())
			{
				xml.getLang().add(xfLang.getUtilsLang(ahtLang));
			}
		}
		Collections.sort(xml.getLang(), comparator);
		return xml;
	}
	
	public static Langs build()
	{
		return new Langs();
	}
	
	public static Langs build(Lang lang)
	{
		Langs xml = build();
		xml.getLang().add(lang);
		return xml;
	}
	
	public static Langs build(Lang... lang)
	{
		Langs xml = build();
		for(Lang l : lang)
		{
			xml.getLang().add(l);
		}
		
		return xml;
	}
	
	public static Langs build(String[] langs)
	{
		Langs xml = build();
		for(String lang : langs)
		{
			xml.getLang().add(XmlLangFactory.create(lang,""));
		}
		return xml;
	}
}