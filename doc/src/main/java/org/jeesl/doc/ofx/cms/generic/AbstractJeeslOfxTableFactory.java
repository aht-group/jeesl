package org.jeesl.doc.ofx.cms.generic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.model.json.io.label.JsonTranslation;
import org.jeesl.model.xml.io.locale.status.Translations;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleManager;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.openfuxml.factory.xml.ofx.content.structure.XmlParagraphFactory;
import org.openfuxml.factory.xml.table.XmlCellFactory;
import org.openfuxml.factory.xml.table.XmlHeadFactory;
import org.openfuxml.interfaces.configuration.OfxTranslationProvider;
import org.openfuxml.model.xml.core.ofx.Paragraph;
import org.openfuxml.model.xml.core.table.Cell;
import org.openfuxml.model.xml.core.table.Head;
import org.openfuxml.model.xml.core.table.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractJeeslOfxTableFactory<L extends JeeslLang, LOC extends JeeslLocale<L,?,LOC,?>>
								extends AbstractJeeslOfxFactory<L,LOC>
{
	final static Logger logger = LoggerFactory.getLogger(AbstractJeeslOfxTableFactory.class);
	
	protected List<String> localeCodes;
	protected List<String> tableHeaderKeys;
	
	protected final List<JsonTranslation> tableHeaders;
	
	protected Translations translations;
	
	public AbstractJeeslOfxTableFactory(OfxTranslationProvider tp)
	{	
		super(tp);
		tableHeaderKeys = new ArrayList<String>();
		tableHeaders = new ArrayList<JsonTranslation>();
	}
	
	protected <E extends Enum<E>>void addHeaderKey(Class<?> c)
	{
		JsonTranslation json = new JsonTranslation();
		json.setEntity(c.getSimpleName());
		tableHeaders.add(json);
	}
	protected <E extends Enum<E>>void addHeaderKey(Class<?> c, E code)
	{
		JsonTranslation json = new JsonTranslation();
		json.setEntity(c.getSimpleName());
		json.setCode(code.toString());
		tableHeaders.add(json);
	}
	protected void addHeaderMulti(JeeslLocaleManager<LOC> lp, Map<String,L> multiLang)
	{
		JsonTranslation json = new JsonTranslation();
		
		Map<String,String> map = new HashMap<String,String>();
		for(String key : multiLang.keySet())
		{
			if(lp.hasLocale(key))
			{
				map.put(key,multiLang.get(key).getLang());
			}
		}
		json.setMultiLang(map);
		tableHeaders.add(json);
	}
	protected void addHeader(String label)
	{
		JsonTranslation json = new JsonTranslation();
		json.setLabel(label);
		tableHeaders.add(json);
	}
	
	protected <E extends Enum<E>>void addHeaderKey(E code)
	{
		tableHeaderKeys.add(code.toString());
	}
	
	protected Head buildHead()
	{
		Row row = new Row();
		logger.info("Building Head with keys:"+tableHeaderKeys.size()+" locales:"+tp.getLocaleCodes().size());
		for(String headerKey : tableHeaderKeys)
		{
			Cell cell = XmlCellFactory.build();
			for(String localeCode : tp.getLocaleCodes())
			{
				cell.getContent().add(XmlParagraphFactory.build(localeCode,tp.tlEntity(localeCode, headerKey)));
			}
			row.getCell().add(cell);
		}
		
		return XmlHeadFactory.build(row);
	}
	
	protected Head buildTableHeader(JeeslLocaleManager<LOC> lp)
	{
		Row row = new Row();
//		logger.info("Building Head with headers:"+tableHeaders.size()+" locales:"+tp.getLocaleCodes().size());
		for(JsonTranslation json : tableHeaders)
		{
			Cell cell = XmlCellFactory.build();
			if(json.getEntity()!=null)
			{
				logger.trace("Building via TP "+json.getEntity()+" "+json.getCode());
				for(String localeCode : lp.getLocaleCodes())
				{
					Paragraph p = XmlParagraphFactory.build(localeCode,tp.tAttribute(localeCode,json.getEntity(),json.getCode()));
					cell.getContent().add(p);
				}
			}
			else if(json.getMultiLang()!=null)
			{
				for(String localeCode : json.getMultiLang().keySet())
				{
					cell.getContent().add(XmlParagraphFactory.build(localeCode,json.getMultiLang().get(localeCode)));
				}
			}
			else if(json.getLabel()!=null)
			{
				cell.getContent().add(XmlParagraphFactory.text(json.getLabel()));
			}
			row.getCell().add(cell);
		}
		
		return XmlHeadFactory.build(row);
	}
}