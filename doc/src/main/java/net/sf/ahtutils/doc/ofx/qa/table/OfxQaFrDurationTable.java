package net.sf.ahtutils.doc.ofx.qa.table;

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
import org.openfuxml.factory.xml.table.OfxCellFactory;
import org.openfuxml.factory.xml.table.OfxColumnFactory;
import org.openfuxml.util.OfxCommentBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.doc.DocumentationCommentBuilder;
import net.sf.ahtutils.doc.UtilsDocumentation;
import net.sf.ahtutils.doc.ofx.AbstractUtilsOfxDocumentationFactory;
import net.sf.ahtutils.doc.ofx.util.OfxMultiLangFactory;
import net.sf.ahtutils.xml.qa.Category;
import net.sf.ahtutils.xml.qa.Test;
import net.sf.ahtutils.xml.status.Translations;
import net.sf.ahtutils.xml.xpath.StatusXpath;
import net.sf.exlp.exception.ExlpXpathNotFoundException;
import net.sf.exlp.exception.ExlpXpathNotUniqueException;
import net.sf.exlp.util.xml.JaxbUtil;

public class OfxQaFrDurationTable extends AbstractUtilsOfxDocumentationFactory
{
	final static Logger logger = LoggerFactory.getLogger(OfxQaNfrQuestionTableFactory.class);
	private static String keyCaptionPrefix = "auTableQmAgreement";
	
	private List<String> headerKeys;
	
	public OfxQaFrDurationTable(Configuration config, String[] langs, Translations translations)
	{
		super(config,langs,translations);
		
		headerKeys = new ArrayList<String>();
		headerKeys.add("auTableQaTestCode");
		headerKeys.add("auTableQaTestCase");
		headerKeys.add("auTableQaTestDuration");
	}
	
	public Table build(Category category) throws OfxAuthoringException
	{
		try
		{	
			Table table = new Table();
			table.setId("table.qa.duration.fr."+category.getCode());
			
			Comment comment = XmlCommentFactory.build();
			OfxCommentBuilder.fixedId(comment, table.getId());
			DocumentationCommentBuilder.translationKeys(comment,config,UtilsDocumentation.keyTranslationFile);
			DocumentationCommentBuilder.tableHeaders(comment,headerKeys);
			DocumentationCommentBuilder.tableKey(comment,keyCaptionPrefix,"Table Caption Prefix");
			OfxCommentBuilder.doNotModify(comment);
			table.setComment(comment);
			
			table.setTitle(OfxMultiLangFactory.title(langs, StatusXpath.getTranslation(translations, "auTableQaSummaryDuration").getLangs(),null," "+category.getName()));
			table.setSpecification(createSpecifications());
			table.setContent(createContent(category));
				
			return table;
		}
		catch (ExlpXpathNotFoundException e) {throw new OfxAuthoringException(e.getMessage());}
		catch (ExlpXpathNotUniqueException e) {throw new OfxAuthoringException(e.getMessage());}
	}
	
	private Specification createSpecifications()
	{
		Columns cols = new Columns();
		cols.getColumn().add(OfxColumnFactory.flex(10,true));
		cols.getColumn().add(OfxColumnFactory.flex(80,false));
		cols.getColumn().add(OfxColumnFactory.flex(10,true));
		
		Specification specification = new Specification();
		specification.setColumns(cols);
		specification.setFloat(XmlFloatFactory.build(false));
		
		return specification;
	}
	
	private Content createContent(Category category)
	{
		Head head = new Head();
		head.getRow().add(createHeaderRow(headerKeys));
		
		Body body = new Body();
		for(Test t : category.getTest())
		{
			body.getRow().add(createRow(t));
		}
		
		Content content = new Content();
		content.getBody().add(body);
		content.setHead(head);
		
		return content;
	}
	
	private Row createRow(Test test)
	{
		Row row = new Row();
		JaxbUtil.trace(test);
		row.getCell().add(OfxCellFactory.createParagraphCell(test.getCode()));
		row.getCell().add(OfxCellFactory.createParagraphCell(test.getName()));
		row.getCell().add(OfxCellFactory.createParagraphCell(""+test.getDuration()));
		
		return row;
	}
}