package org.jeesl.doc.ofx;

import java.util.ArrayList;
import java.util.List;

import org.jeesl.model.xml.jeesl.Container;
import org.openfuxml.content.layout.Font;
import org.openfuxml.content.list.Item;
import org.openfuxml.content.ofx.Paragraph;
import org.openfuxml.content.ofx.Title;
import org.openfuxml.content.table.Cell;
import org.openfuxml.exception.OfxAuthoringException;
import org.openfuxml.factory.xml.ofx.content.structure.XmlParagraphFactory;
import org.openfuxml.factory.xml.ofx.content.text.XmlTitleFactory;
import org.openfuxml.factory.xml.ofx.list.XmlListItemFactory;
import org.openfuxml.factory.xml.table.OfxCellFactory;
import org.openfuxml.factory.xml.text.OfxTextFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.interfaces.model.status.UtilsDescription;
import net.sf.ahtutils.interfaces.model.status.UtilsLang;
import net.sf.ahtutils.interfaces.model.status.UtilsStatus;
import net.sf.ahtutils.xml.aht.Aht;
import net.sf.ahtutils.xml.status.Description;
import net.sf.ahtutils.xml.status.Descriptions;
import net.sf.ahtutils.xml.status.Lang;
import net.sf.ahtutils.xml.status.Langs;
import net.sf.ahtutils.xml.status.Status;
import net.sf.ahtutils.xml.status.Translations;
import net.sf.ahtutils.xml.xpath.StatusXpath;
import net.sf.exlp.exception.ExlpXpathNotFoundException;
import net.sf.exlp.exception.ExlpXpathNotUniqueException;

@Deprecated // Better use OfxMultiLocaleFactory
public class OfxMultiLangFactory<L extends UtilsLang>
{	
	final static Logger logger = LoggerFactory.getLogger(OfxMultiLangFactory.class);
	
	private final List<String> localeCodes;
	
	public OfxMultiLangFactory(List<String> localeCodes)
	{
		this.localeCodes=localeCodes;
	}
	
	public <S extends UtilsStatus<S,L,D>, D extends UtilsDescription> Title title(String localeCode, UtilsStatus<S,L,D> status) {return title(localeCode,status,null);}
	public <S extends UtilsStatus<S,L,D>, D extends UtilsDescription> Title title(String localeCode, UtilsStatus<S,L,D> status, String suffix)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(status.getName().get(localeCode).getLang());
		if(suffix!=null) {sb.append(" ").append(suffix);}
		
		return XmlTitleFactory.build(localeCode, sb.toString());
	}
	
	public <S extends UtilsStatus<S,L,D>, D extends UtilsDescription> Cell cellLabel(UtilsStatus<S,L,D> status)
	{
		Cell cell = OfxCellFactory.build();
//		if(font!=null){cell.getContent().add(font);}
		cell.getContent().addAll(paragraphLabels(status));
		return cell;
	}
	
	private <S extends UtilsStatus<S,L,D>, D extends UtilsDescription> List<Paragraph> paragraphLabels(UtilsStatus<S,L,D> status)
	{
		List<Paragraph> paragraphs = new ArrayList<Paragraph>();
		
		for(String key : localeCodes)
		{
			Paragraph p = XmlParagraphFactory.lang(key);
//			p.getContent().add(font);
			if(status.getName()!=null && status.getName().containsKey(key)) {p.getContent().add(status.getName().get(key).getLang());}
			else {p.getContent().add("-!-");}
			paragraphs.add(p);
			
		}
		return paragraphs;
	}
	
	public static Title title(String[] keys, Translations translations, String captionKey) throws OfxAuthoringException
	{
		Title title = XmlTitleFactory.build();
		for(String lang : keys)
		{
			try
			{
				Lang lCaption = StatusXpath.getLang(translations, captionKey, lang);
				title.getContent().add(OfxTextFactory.build(lang,lCaption.getTranslation()));
			}
			catch (ExlpXpathNotFoundException e) {throw new OfxAuthoringException(e.getMessage());}
			catch (ExlpXpathNotUniqueException e) {throw new OfxAuthoringException(e.getMessage());}
		}
		return title;
	}
	
	public static Cell cell(String[] keys, Translations translations, String key) throws OfxAuthoringException
	{
		Cell cell = OfxCellFactory.build();
		for(String lang : keys)
		{
			try
			{
				Lang lCaption = StatusXpath.getLang(translations, key, lang);
				cell.getContent().add(XmlParagraphFactory.build(lang,lCaption.getTranslation()));
			}
			catch (ExlpXpathNotFoundException e) {throw new OfxAuthoringException(e.getMessage());}
			catch (ExlpXpathNotUniqueException e) {throw new OfxAuthoringException(e.getMessage());}
		}
		return cell;
	}
	
	public static Title title(String[] keys, Langs langs) throws OfxAuthoringException{return title(keys,langs,null,null);}
	public static Title title(String[] keys, Langs langs, String prefix, String suffix) throws OfxAuthoringException
	{
		Title title = XmlTitleFactory.build();
		for(String key : keys)
		{
			StringBuffer sb = new StringBuffer();
			if(prefix!=null){sb.append(prefix);}
			try
			{
				Lang lCaption = StatusXpath.getLang(langs,key);
				sb.append(lCaption.getTranslation());
			}
			catch (ExlpXpathNotFoundException e) {throw new OfxAuthoringException(e.getMessage());}
			catch (ExlpXpathNotUniqueException e) {throw new OfxAuthoringException(e.getMessage());}
			if(suffix!=null){sb.append(suffix);}
			title.getContent().add(OfxTextFactory.build(key,sb.toString()));
		}
		return title;
	}
	
	public static Cell cell(String[] langs, String code, Aht aht)
	{
		if(aht!=null)
		{
			try
			{
				Status status = StatusXpath.getStatus(aht.getStatus(), code);
				return cell(langs,status.getLangs());
			}
			catch (ExlpXpathNotFoundException e) {logger.warn(e.getMessage());}
			catch (ExlpXpathNotUniqueException e) {logger.warn(e.getMessage());}
		}
		return OfxCellFactory.createParagraphCell(code);
	}
	public static Cell cell(String[] langs, String code, Container container)
	{
		if(container!=null)
		{
			try
			{
				Status status = StatusXpath.getStatus(container.getStatus(), code);
				return cell(langs,status.getLangs());
			}
			catch (ExlpXpathNotFoundException e) {logger.warn(e.getMessage());}
			catch (ExlpXpathNotUniqueException e) {logger.warn(e.getMessage());}
		}
		return OfxCellFactory.createParagraphCell(code);
	}
	
	
	public static Cell cell(String[] keys, Langs langs){return cell(keys,langs,null);}

	public static Cell cell(String[] keys, Langs langs, Font font)
	{
		Cell cell = OfxCellFactory.build();
		if(font!=null){cell.getContent().add(font);}
		cell.getContent().addAll(paragraph(keys,langs));
		return cell;
	}
	
	public static Cell cell(String[] keys, Descriptions descriptions) throws OfxAuthoringException {return cell(keys,descriptions,null);}
	public static Cell cell(String[] keys, Descriptions descriptions, Font font) throws OfxAuthoringException
	{
		Cell cell = OfxCellFactory.build();
		if(font!=null){cell.getContent().add(font);}
		cell.getContent().addAll(paragraph(keys,descriptions));
		return cell;
	}
	
	public static Item item(String[] keys, Descriptions descriptions) throws OfxAuthoringException
	{
		Item item = XmlListItemFactory.build();
		item.getContent().addAll(paragraph(keys,descriptions));
		return item;
	}
	
	public static List<Item> items(String[] localeCodes, Langs langs, Descriptions descriptions) throws OfxAuthoringException
	{
		List<Item> list = new ArrayList<Item>();
		for(String localeCode : localeCodes)
		{
			try
			{
				Lang lang = StatusXpath.getLang(langs, localeCode);
				Description description = StatusXpath.getDescription(descriptions, localeCode);
				Item item = XmlListItemFactory.build(localeCode,lang.getTranslation(),description.getValue());
				list.add(item);
			}
			catch (ExlpXpathNotFoundException e) {throw new OfxAuthoringException(e.getMessage());}
			catch (ExlpXpathNotUniqueException e) {throw new OfxAuthoringException(e.getMessage());}
		}
		
		return list;
	}
	
	public static List<Paragraph> paragraph(String[] keys, Langs langs){return paragraph(keys,langs,null);}
	
	public static List<Paragraph> paragraph(String[] keys, Langs langs, Font font)
	{
		List<Paragraph> paragraphs = new ArrayList<Paragraph>();
		
		for(String key : keys)
		{
			Paragraph p = XmlParagraphFactory.lang(key);
			p.getContent().add(font);
			String text = "!!!No-Translation!!!";
			try
			{
				Lang lCaption = StatusXpath.getLang(langs,key);
				text = lCaption.getTranslation();
			}
			catch (ExlpXpathNotFoundException e) {e.printStackTrace();}
			catch (ExlpXpathNotUniqueException e) {e.printStackTrace();}
			p.getContent().add(text);
			paragraphs.add(p);
		}
		return paragraphs;
	}
	
	public static List<Paragraph> paragraph(String[] keys, Descriptions descriptions) throws OfxAuthoringException
	{
		List<Paragraph> paragraphs = new ArrayList<Paragraph>();
		
		for(String key : keys)
		{
			Paragraph p = XmlParagraphFactory.lang(key);
			String text = "!!!No-Translation!!!";
			try
			{
				Description d = StatusXpath.getDescription(descriptions, key);
				text = d.getValue();
			}
			catch (ExlpXpathNotFoundException e) {throw new OfxAuthoringException(e.getMessage());}
			catch (ExlpXpathNotUniqueException e) {throw new OfxAuthoringException(e.getMessage());}
			p.getContent().add(text);
			paragraphs.add(p);
		}
		return paragraphs;
	}
}