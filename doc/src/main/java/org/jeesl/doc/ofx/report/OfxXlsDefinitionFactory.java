package org.jeesl.doc.ofx.report;

import java.util.ArrayList;
import java.util.List;

import org.exlp.interfaces.system.property.Configuration;
import org.jeesl.doc.ofx.OfxMultiLangFactory;
import org.jeesl.model.xml.io.locale.status.Translations;
import org.jeesl.model.xml.io.report.XlsColumn;
import org.jeesl.model.xml.io.report.XlsSheet;
import org.jeesl.util.query.xpath.ReportXpath;
import org.openfuxml.exception.OfxAuthoringException;
import org.openfuxml.factory.xml.layout.XmlFloatFactory;
import org.openfuxml.factory.xml.ofx.content.XmlCommentFactory;
import org.openfuxml.factory.xml.ofx.content.text.XmlTitleFactory;
import org.openfuxml.factory.xml.table.XmlCellFactory;
import org.openfuxml.factory.xml.table.XmlColumnFactory;
import org.openfuxml.interfaces.configuration.DefaultSettingsManager;
import org.openfuxml.interfaces.media.CrossMediaManager;
import org.openfuxml.model.xml.core.ofx.Comment;
import org.openfuxml.model.xml.core.table.Body;
import org.openfuxml.model.xml.core.table.Columns;
import org.openfuxml.model.xml.core.table.Content;
import org.openfuxml.model.xml.core.table.Head;
import org.openfuxml.model.xml.core.table.Row;
import org.openfuxml.model.xml.core.table.Specification;
import org.openfuxml.model.xml.core.table.Table;
import org.openfuxml.util.OfxCommentBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.doc.ofx.AbstractUtilsOfxDocumentationFactory;
import net.sf.exlp.exception.ExlpXpathNotFoundException;
import net.sf.exlp.exception.ExlpXpathNotUniqueException;

public class OfxXlsDefinitionFactory extends AbstractUtilsOfxDocumentationFactory
{
	final static Logger logger = LoggerFactory.getLogger(OfxXlsDefinitionFactory.class);
	
	private List<String> headerKeys;
	
	public OfxXlsDefinitionFactory(Configuration config,String[] langs, Translations translations,CrossMediaManager cmm,DefaultSettingsManager dsm)
	{
		super(config,langs,translations);
		
		headerKeys = new ArrayList<String>();
		headerKeys.add("auXlsDefinitionColumn");
//		headerKeys.add("auXlsDefinitionRequired");
		headerKeys.add("auXlsDefinitionAttribute");
		headerKeys.add("auXlsDefinitionDescription");
	}
	
	public Table build(XlsSheet sheet) throws OfxAuthoringException, ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	{
		Table table = new Table();
		table.setSpecification(createSpecifications());
		
		table.setContent(createContent(sheet));
		
		table.setId("table.qa.team");

		table.setTitle(XmlTitleFactory.build("Import Definition"));
		
		Comment comment = XmlCommentFactory.build();
		OfxCommentBuilder.fixedId(comment, table.getId());
//			DocumentationCommentBuilder.translationKeys(comment,config,UtilsDocumentation.keyTranslationFile);
//			DocumentationCommentBuilder.tableHeaders(comment,headerKeys);
//			DocumentationCommentBuilder.tableKey(comment,keyCaption,"Table Caption Prefix");
		OfxCommentBuilder.doNotModify(comment);
		table.setComment(comment);
		
		return table;
	}
	
	private Specification createSpecifications()
	{
		Columns cols = new Columns();
		cols.getColumn().add(XmlColumnFactory.flex(15,true));
		cols.getColumn().add(XmlColumnFactory.flex(35,true));
		cols.getColumn().add(XmlColumnFactory.flex(50));
			
		
		Specification specification = new Specification();
		specification.setColumns(cols);
		specification.setFloat(XmlFloatFactory.build(false));
		
		return specification;
	}
	
	private Content createContent(XlsSheet sheet) throws OfxAuthoringException, ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	{
		Head head = new Head();
		head.getRow().add(createHeaderRow(headerKeys));
		
		Body body = new Body();
		List<XlsColumn> columns = ReportXpath.getColumns(sheet);
		for (XlsColumn xlsColumn : columns)
		{
			body.getRow().add(createRow(xlsColumn));
		}
		
		Content content = new Content();
		content.getBody().add(body);
		content.setHead(head);
		
		return content;
	}
	
	private Row createRow(XlsColumn xlsColumn) throws OfxAuthoringException
	{
		Row row = new Row();
		
		StringBuffer sb = new StringBuffer();
		sb.append(xlsColumn.getColumn());
		if(xlsColumn.isRequired()){sb.append(" (required)");}
		
		row.getCell().add(XmlCellFactory.createParagraphCell(sb.toString()));
		row.getCell().add(OfxMultiLangFactory.cell(langs, xlsColumn.getLangs()));
		row.getCell().add(OfxMultiLangFactory.cell(langs, xlsColumn.getDescriptions()));
		
		return row;
	}	
}