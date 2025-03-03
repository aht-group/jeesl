package net.sf.ahtutils.doc.latex.writer;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

import org.jeesl.model.xml.io.locale.status.Translations;
import org.openfuxml.exception.OfxAuthoringException;
import org.openfuxml.exception.OfxConfigurationException;
import org.openfuxml.interfaces.configuration.ConfigurationProvider;
import org.openfuxml.model.xml.core.ofx.Section;
import org.openfuxml.model.xml.core.table.Table;
import org.openfuxml.renderer.latex.content.structure.LatexSectionRenderer;
import org.openfuxml.renderer.latex.content.table.LatexTableRenderer;
import org.openfuxml.renderer.latex.preamble.LatexPreamble;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.doc.UtilsDocumentation;
import net.sf.exlp.util.io.StringIO;

public class AbstractDocumentationLatexWriter
{	
	final static Logger logger = LoggerFactory.getLogger(AbstractDocumentationLatexWriter.class);
	
	protected org.exlp.interfaces.system.property.Configuration config;
	protected String baseLatexDir;
	
	protected Translations translations;
	protected String[] langs;
	
	protected ConfigurationProvider cp;
	
	
	
	public AbstractDocumentationLatexWriter(org.exlp.interfaces.system.property.Configuration config, Translations translations,String[] langs, ConfigurationProvider cp)
	{
		this.config=config;
		this.translations=translations;
		this.langs=langs;
		this.cp=cp;
		
		baseLatexDir = config.getString(UtilsDocumentation.keyBaseLatexDir);
	}
	
	protected void writeTable(Table table, File f) throws OfxAuthoringException, IOException
	{
		LatexTableRenderer tableRenderer = new LatexTableRenderer(cp);
		tableRenderer.setPreBlankLine(false);
		tableRenderer.render(table);
		
		StringWriter sw = new StringWriter();
		tableRenderer.write(sw);
		StringIO.writeTxt(f, sw.toString());
	}
	
	protected void writeSection(Section section, File f) throws OfxAuthoringException, IOException, OfxConfigurationException {writeSection(1, section, f);}
	protected void writeSection(int sectionLevel, Section section, File f) throws OfxAuthoringException, IOException, OfxConfigurationException
	{
		LatexSectionRenderer sectionRenderer = new LatexSectionRenderer(cp,sectionLevel,new LatexPreamble(cp));
		sectionRenderer.render(section);
		
		StringWriter sw = new StringWriter();
		sectionRenderer.write(sw);
		logger.info("Writing to : "+f.getAbsolutePath());
		StringIO.writeTxt(f, sw.toString());
	}
}