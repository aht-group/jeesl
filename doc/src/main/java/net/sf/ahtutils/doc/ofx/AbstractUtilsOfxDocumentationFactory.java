package net.sf.ahtutils.doc.ofx;

import java.util.ArrayList;
import java.util.List;

import org.exlp.controller.handler.system.property.ConfigLoader;
import org.jeesl.model.xml.io.locale.status.Translations;
import org.jeesl.util.query.xpath.StatusXpath;
import org.openfuxml.factory.xml.table.XmlCellFactory;
import org.openfuxml.model.xml.core.ofx.Paragraph;
import org.openfuxml.model.xml.core.table.Cell;
import org.openfuxml.model.xml.core.table.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.exception.ExlpXpathNotFoundException;
import net.sf.exlp.exception.ExlpXpathNotUniqueException;

public class AbstractUtilsOfxDocumentationFactory
{
	final static Logger logger = LoggerFactory.getLogger(AbstractUtilsOfxDocumentationFactory.class);
		
	protected org.exlp.interfaces.system.property.Configuration config;
	protected String[] langs;
	protected Translations translations;
	protected String imagePathPrefix;
	
	public AbstractUtilsOfxDocumentationFactory(org.exlp.interfaces.system.property.Configuration config, String[] langs, Translations translations)
	{
		this.config=config;
		logger.warn("Config2 NYI");
		this.langs=langs;
		this.translations=translations;
	}
	
	@Deprecated
	protected Row createHeaderRow(String[] headerKeys)
	{
		List<String> keys = new ArrayList<String>();
		for(String key : headerKeys){keys.add(key);}
		return createHeaderRow(keys);
	}
	
	protected Row createHeaderRow(List<String> headerKeys)
	{
		Row row = new Row();
		for(String headerKey : headerKeys)
		{
			Cell cell = XmlCellFactory.build();
			for(String lang : langs)
			{
				StringBuffer sb = new StringBuffer();
				if(headerKey.length()>0)
				{
					try
					{
						sb.append(StatusXpath.getLang(translations, headerKey, lang).getTranslation());
					}
					catch (ExlpXpathNotFoundException e)
					{
						sb.append(e.getMessage());
						logger.warn(sb.toString());
					}
					catch (ExlpXpathNotUniqueException e)
					{
						sb.append(e.getMessage());
						logger.warn(sb.toString());
					}
				}
				else
				{
					sb.append("");
				}
				Paragraph p = new Paragraph();
				p.setLang(lang);
				p.getContent().add(sb.toString());
				cell.getContent().add(p);
			}
			
			row.getCell().add(cell);
		}
		return row;
	}
}