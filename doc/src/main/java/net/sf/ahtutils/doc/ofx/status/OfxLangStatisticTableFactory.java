package net.sf.ahtutils.doc.ofx.status;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import net.sf.ahtutils.model.pojo.status.TranslationStatistic;
import net.sf.exlp.exception.ExlpXpathNotFoundException;
import net.sf.exlp.exception.ExlpXpathNotUniqueException;
import net.sf.exlp.util.io.StringIO;

import org.jeesl.model.xml.io.locale.status.Translations;
import org.jeesl.util.query.xpath.StatusXpath;
import org.openfuxml.exception.OfxAuthoringException;
import org.openfuxml.factory.ConfigurationProviderFacotry;
import org.openfuxml.factory.xml.table.XmlCellFactory;
import org.openfuxml.factory.xml.table.XmlColumnFactory;
import org.openfuxml.interfaces.configuration.ConfigurationProvider;
import org.openfuxml.interfaces.configuration.DefaultSettingsManager;
import org.openfuxml.interfaces.media.CrossMediaManager;
import org.openfuxml.media.cross.NoOpCrossMediaManager;
import org.openfuxml.model.xml.core.table.Body;
import org.openfuxml.model.xml.core.table.Columns;
import org.openfuxml.model.xml.core.table.Content;
import org.openfuxml.model.xml.core.table.Head;
import org.openfuxml.model.xml.core.table.Row;
import org.openfuxml.model.xml.core.table.Specification;
import org.openfuxml.model.xml.core.table.Table;
import org.openfuxml.renderer.latex.content.table.LatexGridTableRenderer;
import org.openfuxml.util.configuration.settings.OfxDefaultSettingsManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OfxLangStatisticTableFactory
{
	final static Logger logger = LoggerFactory.getLogger(OfxLangStatisticTableFactory.class);
		
	private String lang;
	private Translations translations;
	
	private ConfigurationProvider cp;
	
	public OfxLangStatisticTableFactory(String lang, Translations translations)
	{
		this.lang=lang;
		this.translations=translations;
		
		CrossMediaManager cmm = new NoOpCrossMediaManager();
		DefaultSettingsManager dsm = new OfxDefaultSettingsManager();
		cp = ConfigurationProviderFacotry.build(cmm, dsm);
		
		logger.warn("NYI CMM/DSM");
	}
	
	public void saveDescription(File f, List<TranslationStatistic> lLangs, String[] headerKeys)
	{
		try
		{
			logger.debug("Saving Reference to "+f);
			LatexGridTableRenderer renderer = new LatexGridTableRenderer(cp);
			renderer.render(toOfx(lLangs,headerKeys));
			StringWriter actual = new StringWriter();
			renderer.write(actual);
			StringIO.writeTxt(f, actual.toString());
			
		}
		catch (OfxAuthoringException e) {logger.error("Something went wrong during ofx/latex transformation ",e);}
		catch (IOException e) {logger.error("Cannot save the file to "+f.getAbsolutePath(),e);}
	}
	
	public Table toOfx(List<TranslationStatistic> lLangs, String[] headerKeys)
	{
		Table table = new Table();
		table.setSpecification(createSpecifications());
		
		try{table.setContent(createContent(lLangs,headerKeys));}
		catch (ExlpXpathNotFoundException e) {e.printStackTrace();}
		catch (ExlpXpathNotUniqueException e) {e.printStackTrace();}
		
		return table;
	}
	
	private Specification createSpecifications()
	{
		Columns cols = new Columns();
		cols.getColumn().add(XmlColumnFactory.relative(30));
		cols.getColumn().add(XmlColumnFactory.relative(10));
		cols.getColumn().add(XmlColumnFactory.relative(10));
		cols.getColumn().add(XmlColumnFactory.relative(10));
		
		Specification specification = new Specification();
		specification.setColumns(cols);
		
		return specification;
	}
	
	private Content createContent(List<TranslationStatistic> lLangs, String[] headerKeys) throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	{
		Row row = new Row();
		for(String headerKey : headerKeys)
		{
			row.getCell().add(XmlCellFactory.createParagraphCell(StatusXpath.getLang(translations, headerKey, lang).getTranslation()));
		}
		
		Head head = new Head();
		head.getRow().add(row);
		
		Body body = new Body();
		for(TranslationStatistic stat : lLangs)
		{
			body.getRow().add(createRow(stat));
		}
		
		Content content = new Content();
		content.getBody().add(body);
		content.setHead(head);
		
		return content;
	}
	
	private Row createRow(TranslationStatistic stat) throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	{		
		Row row = new Row();
		row.getCell().add(XmlCellFactory.createParagraphCell(stat.getFile()));
		row.getCell().add(XmlCellFactory.createParagraphCell(stat.getAllTranslations()));
		row.getCell().add(XmlCellFactory.createParagraphCell(stat.getVersionOutdated()));
		row.getCell().add(XmlCellFactory.createParagraphCell(stat.getMissing()));
		return row;
	}	
}