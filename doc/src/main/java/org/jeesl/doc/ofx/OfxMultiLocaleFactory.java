package org.jeesl.doc.ofx;

import java.util.ArrayList;
import java.util.List;

import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.locale.JeeslLocaleProvider;
import org.jeesl.util.query.xpath.StatusXpath;
import org.openfuxml.content.list.Item;
import org.openfuxml.content.ofx.Paragraph;
import org.openfuxml.content.ofx.Title;
import org.openfuxml.content.table.Cell;
import org.openfuxml.factory.xml.list.OfxListItemFactory;
import org.openfuxml.factory.xml.ofx.content.structure.XmlParagraphFactory;
import org.openfuxml.factory.xml.ofx.content.text.XmlTitleFactory;
import org.openfuxml.factory.xml.table.XmlCellFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.interfaces.model.status.UtilsDescription;
import net.sf.ahtutils.interfaces.model.status.UtilsLang;
import net.sf.ahtutils.interfaces.model.status.UtilsStatus;
import net.sf.ahtutils.xml.status.Descriptions;
import net.sf.ahtutils.xml.status.Langs;
import net.sf.exlp.exception.ExlpXpathNotFoundException;
import net.sf.exlp.exception.ExlpXpathNotUniqueException;

public class OfxMultiLocaleFactory<L extends UtilsLang, LOC extends JeeslLocale<L,?,LOC,?>>
{	
	final static Logger logger = LoggerFactory.getLogger(OfxMultiLocaleFactory.class);
		
	public OfxMultiLocaleFactory()
	{
		
	}
	
	public Cell cell(JeeslLocaleProvider<LOC> lp, Langs langs) {return XmlCellFactory.build(paragraphs(lp,langs,false));}
	public <S extends UtilsStatus<S,L,D>, D extends UtilsDescription> Cell cellLabel(JeeslLocaleProvider<LOC> lp, UtilsStatus<S,L,D> status)
	{
		Cell cell = XmlCellFactory.build();
//		if(font!=null){cell.getContent().add(font);}
		cell.getContent().addAll(paragraphLabels(lp,status));
		return cell;
	}
	
	private <S extends UtilsStatus<S,L,D>, D extends UtilsDescription> List<Paragraph> paragraphLabels(JeeslLocaleProvider<LOC> lp, UtilsStatus<S,L,D> status)
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
	
	public <S extends UtilsStatus<S,L,D>, D extends UtilsDescription> Title title(JeeslLocaleProvider<LOC> lp, UtilsStatus<S,L,D> status) {return title(lp,status,null);}
	public <S extends UtilsStatus<S,L,D>, D extends UtilsDescription> Title title(JeeslLocaleProvider<LOC> lp, UtilsStatus<S,L,D> status, String suffix)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(status.getName().get(lp.getPrimaryLocaleCode()).getLang());
		if(suffix!=null) {sb.append(" ").append(suffix);}
		
		return XmlTitleFactory.build(lp.getPrimaryLocaleCode(), sb.toString());
	}
	public List<Title> title(JeeslLocaleProvider<LOC> lp, Langs langs)
	{
		List<Title> titles = new ArrayList<Title>();
		for(String localeCode : lp.getLocaleCodes())
		{
			try {
				titles.add(XmlTitleFactory.build(localeCode, StatusXpath.getLang(langs, localeCode).getTranslation()));
			} catch (ExlpXpathNotFoundException | ExlpXpathNotUniqueException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return titles;
	}
	
	public List<Item> listDescription(JeeslLocaleProvider<LOC> lp, Langs langs, Descriptions descriptions)
	{
		List<Item> items = new ArrayList<>();
		for(String localeCode : lp.getLocaleCodes())
		{
			try
			{
				Item item = OfxListItemFactory.build();
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
	public List<Item> listItem(JeeslLocaleProvider<LOC> lp, Langs langs)
	{
		List<Item> items = new ArrayList<>();
		for(String localeCode : lp.getLocaleCodes())
		{
			try
			{
				Item item = OfxListItemFactory.build();
				item.setLang(localeCode);
				item.getContent().add(XmlParagraphFactory.text(StatusXpath.getLang(langs, localeCode).getTranslation()));
				items.add(item);
			}
			catch (ExlpXpathNotFoundException | ExlpXpathNotUniqueException e) {e.printStackTrace();}
		}
		return items;
	}
	
	public List<Paragraph> paragraphs(JeeslLocaleProvider<LOC> lp, Descriptions descriptions, boolean includeEmpty)
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
	public List<Paragraph> paragraphs(JeeslLocaleProvider<LOC> lp, Langs langs, boolean includeEmpty)
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