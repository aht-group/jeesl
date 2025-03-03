package net.sf.ahtutils.doc.ofx.qa.table;

import java.util.ArrayList;
import java.util.List;

import org.exlp.util.jx.JaxbUtil;
import org.jeesl.doc.ofx.OfxMultiLangFactory;
import org.jeesl.exception.processing.UtilsConfigurationException;
import org.jeesl.factory.txt.module.calendar.TxtPeriodJodaFactory;
import org.jeesl.model.xml.io.locale.status.Translations;
import org.jeesl.model.xml.module.dev.qa.Category;
import org.jeesl.model.xml.module.dev.qa.Test;
import org.jeesl.util.query.xpath.StatusXpath;
import org.openfuxml.exception.OfxAuthoringException;
import org.openfuxml.factory.xml.layout.XmlFloatFactory;
import org.openfuxml.factory.xml.ofx.content.XmlCommentFactory;
import org.openfuxml.factory.xml.table.XmlCellFactory;
import org.openfuxml.factory.xml.table.XmlColumnFactory;
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

import net.sf.ahtutils.doc.DocumentationCommentBuilder;
import net.sf.ahtutils.doc.UtilsDocumentation;
import net.sf.ahtutils.doc.ofx.AbstractUtilsOfxDocumentationFactory;
import net.sf.exlp.exception.ExlpXpathNotFoundException;
import net.sf.exlp.exception.ExlpXpathNotUniqueException;

public class OfxQaDurationFrSummaryTable extends AbstractUtilsOfxDocumentationFactory
{
	final static Logger logger = LoggerFactory.getLogger(OfxQaDurationFrSummaryTable.class);
	private static String keyCaptionPrefix = "auTableQaSummaryDuration";
	
	private TxtPeriodJodaFactory tfPeriod;
	private List<String> headerKeys;
	
	public OfxQaDurationFrSummaryTable(org.exlp.interfaces.system.property.Configuration config, String[] langs, Translations translations)
	{
		super(config,langs,translations);
		tfPeriod = new TxtPeriodJodaFactory();
		
		headerKeys = new ArrayList<String>();
		headerKeys.add("auTableQaTestCode");
		headerKeys.add("auTableQaTestCase");
		headerKeys.add("auTableQaTestDuration");
	}
	
	public Table build(List<Category> categories) throws OfxAuthoringException, UtilsConfigurationException
	{
		try
		{	
			Table table = new Table();
			table.setId("table.qa.duration.summary");
			
			Comment comment = XmlCommentFactory.build();
			OfxCommentBuilder.fixedId(comment, table.getId());
			DocumentationCommentBuilder.translationKeys(comment,config,UtilsDocumentation.keyTranslationFile);
			DocumentationCommentBuilder.tableHeaders(comment,headerKeys);
			DocumentationCommentBuilder.tableKey(comment,keyCaptionPrefix,"Table Caption Prefix");
			OfxCommentBuilder.doNotModify(comment);
			table.setComment(comment);
			
			table.setTitle(OfxMultiLangFactory.title(langs, StatusXpath.getTranslation(translations, keyCaptionPrefix).getLangs()));
			table.setSpecification(createSpecifications());
			table.setContent(createContent(categories));
				
			return table;
		}
		catch (ExlpXpathNotFoundException e) {throw new OfxAuthoringException(e.getMessage());}
		catch (ExlpXpathNotUniqueException e) {throw new OfxAuthoringException(e.getMessage());}
	}
	
	private Specification createSpecifications()
	{
		Columns cols = new Columns();
		cols.getColumn().add(XmlColumnFactory.flex(20,true));
		cols.getColumn().add(XmlColumnFactory.flex(60,false));
		cols.getColumn().add(XmlColumnFactory.flex(20,true));
		
		Specification specification = new Specification();
		specification.setColumns(cols);
		specification.setFloat(XmlFloatFactory.build(false));
		
		return specification;
	}
	
	private Content createContent(List<Category> categories)
	{
		Head head = new Head();
		head.getRow().add(createHeaderRow(headerKeys));
		
		Body body = new Body();
		for(Category c : categories)
		{
			body.getRow().add(createRow(c));
		}
		
		Content content = new Content();
		content.getBody().add(body);
		content.setHead(head);
		
		return content;
	}
	
	private Row createRow(Category category)
	{
		Row row = new Row();
		JaxbUtil.trace(category);
		row.getCell().add(XmlCellFactory.createParagraphCell(category.getCode()));
		row.getCell().add(XmlCellFactory.createParagraphCell(category.getName()));
		
		int duration = totalDuration(category);
		row.getCell().add(XmlCellFactory.createParagraphCell(tfPeriod.debug(duration)));
		
		return row;
	}
	
	private int totalDuration(Category category)
	{
		int total=0;
		for(Test t : category.getTest())
		{
			if(t.isVisible())
			{
				total=total+t.getDuration();
			}
		}
		return total;
	}
}