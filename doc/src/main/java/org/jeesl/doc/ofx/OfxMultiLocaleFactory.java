package org.jeesl.doc.ofx;

import java.util.ArrayList;
import java.util.List;

import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleManager;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.model.xml.io.locale.status.Descriptions;
import org.jeesl.model.xml.io.locale.status.Langs;
import org.jeesl.util.query.xpath.StatusXpath;
import org.openfuxml.factory.xml.list.XmlListItemFactory;
import org.openfuxml.factory.xml.ofx.content.structure.XmlParagraphFactory;
import org.openfuxml.factory.xml.ofx.content.text.XmlTitleFactory;
import org.openfuxml.factory.xml.table.XmlCellFactory;
import org.openfuxml.model.xml.core.list.Item;
import org.openfuxml.model.xml.core.ofx.Paragraph;
import org.openfuxml.model.xml.core.ofx.Title;
import org.openfuxml.model.xml.core.table.Cell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.exception.ExlpXpathNotFoundException;
import net.sf.exlp.exception.ExlpXpathNotUniqueException;

public class OfxMultiLocaleFactory<L extends JeeslLang, LOC extends JeeslLocale<L,?,LOC,?>>
{	
	final static Logger logger = LoggerFactory.getLogger(OfxMultiLocaleFactory.class);
		
	public OfxMultiLocaleFactory()
	{
		
	}
	
	public Cell cell(JeeslLocaleManager<LOC> lp, Langs langs) {return XmlCellFactory.build(paragraphs(lp,langs,false));}
	public <S extends JeeslStatus<L,D,S>, D extends JeeslDescription> Cell cellLabel(JeeslLocaleManager<LOC> lp, JeeslStatus<L,D,S> status)
	{
		Cell cell = XmlCellFactory.build();
//		if(font!=null){cell.getContent().add(font);}
		cell.getContent().addAll(paragraphLabels(lp,status));
		return cell;
	}
	
	private <S extends JeeslStatus<L,D,S>, D extends JeeslDescription> List<Paragraph> paragraphLabels(JeeslLocaleManager<LOC> lp, JeeslStatus<L,D,S> status)
	{
		List<Paragraph> paragraphs = new ArrayList<Paragraph>();
		
		for(String key : lp.getLocaleCodes())
		{
			Paragraph p = XmlParagraphFactory.lang(key);
//			p.getContent().add(font);
			if(status.getName()!=null && status.getName().containsKey(key)) {p.getContent().add(status.getName().get(key).getLang());}
			else {p.getContent().add("-!-");}
			paragraphs.add(p);
			
		}
		return paragraphs;
	}
	
	public <S extends JeeslStatus<L,D,S>, D extends JeeslDescription> Title title(JeeslLocaleManager<LOC> lp, JeeslStatus<L,D,S> status) {return title(lp,status,null);}
	public <S extends JeeslStatus<L,D,S>, D extends JeeslDescription> Title title(JeeslLocaleManager<LOC> lp, JeeslStatus<L,D,S> status, String suffix)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(status.getName().get(lp.getPrimaryLocaleCode()).getLang());
		if(suffix!=null) {sb.append(" ").append(suffix);}
		
		return XmlTitleFactory.build(lp.getPrimaryLocaleCode(), sb.toString());
	}
	public Title title(JeeslLocaleManager<LOC> lp, Langs langs)
	{
		try
		{
			return XmlTitleFactory.build(StatusXpath.getLang(langs, lp.getPrimaryLocaleCode()).getTranslation());
		}
		catch (ExlpXpathNotFoundException | ExlpXpathNotUniqueException e)
		{
			return XmlTitleFactory.build(e.getMessage());
		}
	}
	public List<Title> titles(JeeslLocaleManager<LOC> lp, Langs langs)
	{
		List<Title> titles = new ArrayList<Title>();
		for(String localeCode : lp.getLocaleCodes())
		{
			try
			{
				titles.add(XmlTitleFactory.build(localeCode, StatusXpath.getLang(langs, localeCode).getTranslation()));
			} catch (ExlpXpathNotFoundException | ExlpXpathNotUniqueException e) {e.printStackTrace();}
		}
		return titles;
	}
	
	public List<Item> listDescription(JeeslLocaleManager<LOC> lp, Langs langs, Descriptions descriptions)
	{
		List<Item> items = new ArrayList<>();
		for(String localeCode : lp.getLocaleCodes())
		{
			try
			{
				Item item = XmlListItemFactory.build();
				item.setLang(localeCode);
				item.setName(StatusXpath.getLang(langs, localeCode).getTranslation());
				item.getContent().add(XmlParagraphFactory.text(StatusXpath.getDescription(descriptions, localeCode).getValue()));
				items.add(item);
			}
			catch (ExlpXpathNotFoundException | ExlpXpathNotUniqueException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return items;
	}
	public List<Item> listItem(JeeslLocaleManager<LOC> lp, Langs langs)
	{
		List<Item> items = new ArrayList<>();
		for(String localeCode : lp.getLocaleCodes())
		{
			try
			{
				Item item = XmlListItemFactory.build();
				item.setLang(localeCode);
				item.getContent().add(XmlParagraphFactory.text(StatusXpath.getLang(langs, localeCode).getTranslation()));
				items.add(item);
			}
			catch (ExlpXpathNotFoundException | ExlpXpathNotUniqueException e) {e.printStackTrace();}
		}
		return items;
	}
	
	public List<Paragraph> paragraphs(JeeslLocaleManager<LOC> lp, Descriptions descriptions, boolean includeEmpty)
	{
		List<Paragraph> list = new ArrayList<>();
		for(String localeCode : lp.getLocaleCodes())
		{
			try
			{
				String text = StatusXpath.getDescription(descriptions, localeCode).getValue();
				if(text!=null && text.trim().length()>0)
				{
					list.add(XmlParagraphFactory.build(localeCode,text));
				}
				
			}
			catch (ExlpXpathNotFoundException | ExlpXpathNotUniqueException e) {e.printStackTrace();}
		}
		return list;
	}
	public List<Paragraph> paragraphs(JeeslLocaleManager<LOC> lp, Langs langs, boolean includeEmpty)
	{
		List<Paragraph> list = new ArrayList<>();
		for(String localeCode : lp.getLocaleCodes())
		{
			try
			{
				String text = StatusXpath.getLang(langs, localeCode).getTranslation();
				if(text!=null && text.trim().length()>0)
				{
					list.add(XmlParagraphFactory.build(localeCode,text));
				}
				
			}
			catch (ExlpXpathNotFoundException | ExlpXpathNotUniqueException e) {e.printStackTrace();}
		}
		return list;
	}
}