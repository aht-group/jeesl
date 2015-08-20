package net.sf.ahtutils.doc.ofx.report;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.configuration.Configuration;
import org.openfuxml.content.ofx.Comment;
import org.openfuxml.content.table.Body;
import org.openfuxml.content.table.Columns;
import org.openfuxml.content.table.Content;
import org.openfuxml.content.table.Head;
import org.openfuxml.content.table.Row;
import org.openfuxml.content.table.Specification;
import org.openfuxml.content.table.Table;
import org.openfuxml.exception.OfxAuthoringException;
import org.openfuxml.factory.xml.layout.XmlFloatFactory;
import org.openfuxml.factory.xml.ofx.content.XmlCommentFactory;
import org.openfuxml.factory.xml.ofx.content.text.XmlTitleFactory;
import org.openfuxml.factory.xml.table.OfxCellFactory;
import org.openfuxml.factory.xml.table.OfxColumnFactory;
import org.openfuxml.interfaces.DefaultSettingsManager;
import org.openfuxml.interfaces.media.CrossMediaManager;
import org.openfuxml.util.OfxCommentBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.doc.ofx.AbstractUtilsOfxDocumentationFactory;
import net.sf.ahtutils.doc.ofx.util.OfxMultiLangFactory;
import net.sf.ahtutils.xml.report.XlsColumn;
import net.sf.ahtutils.xml.report.XlsSheet;
import net.sf.ahtutils.xml.status.Translations;

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
	
	public Table build(XlsSheet sheet) throws OfxAuthoringException
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
		cols.getColumn().add(OfxColumnFactory.flex(15,true));
		cols.getColumn().add(OfxColumnFactory.flex(35,true));
		cols.getColumn().add(OfxColumnFactory.flex(50));
			
		
		Specification specification = new Specification();
		specification.setColumns(cols);
		specification.setFloat(XmlFloatFactory.build(false));
		
		return specification;
	}
	
	private Content createContent(XlsSheet sheet)
	{
		Head head = new Head();
		head.getRow().add(createHeaderRow(headerKeys));
		
		Body body = new Body();
		for(XlsColumn xlsColumn : sheet.getXlsColumn())
		{
			body.getRow().add(createRow(xlsColumn));
		}
		
		Content content = new Content();
		content.getBody().add(body);
		content.setHead(head);
		
		return content;
	}
	
	private Row createRow(XlsColumn xlsColumn)
	{
		Row row = new Row();
		
		StringBuffer sb = new StringBuffer();
		sb.append(xlsColumn.getColumn());
		if(xlsColumn.isRequired()){sb.append(" (required)");}
		
		row.getCell().add(OfxCellFactory.createParagraphCell(sb.toString()));
		row.getCell().add(OfxMultiLangFactory.cell(langs, xlsColumn.getLangs()));
		row.getCell().add(OfxMultiLangFactory.cell(langs, xlsColumn.getDescriptions()));
		
		return row;
	}	
}