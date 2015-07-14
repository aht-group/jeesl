package net.sf.ahtutils.doc.ofx.qa.category;

import java.util.ArrayList;
import java.util.List;

import net.sf.ahtutils.doc.DocumentationCommentBuilder;
import net.sf.ahtutils.doc.UtilsDocumentation;
import net.sf.ahtutils.doc.ofx.AbstractUtilsOfxDocumentationFactory;
import net.sf.ahtutils.doc.ofx.status.OfxStatusImageFactory;
import net.sf.ahtutils.xml.aht.Aht;
import net.sf.ahtutils.xml.qa.Category;
import net.sf.ahtutils.xml.qa.Result;
import net.sf.ahtutils.xml.qa.Test;
import net.sf.ahtutils.xml.status.Lang;
import net.sf.ahtutils.xml.status.Translations;
import net.sf.ahtutils.xml.xpath.StatusXpath;
import net.sf.exlp.exception.ExlpXpathNotFoundException;
import net.sf.exlp.exception.ExlpXpathNotUniqueException;

import org.apache.commons.configuration.Configuration;
import org.openfuxml.content.ofx.Comment;
import org.openfuxml.content.table.Body;
import org.openfuxml.content.table.Cell;
import org.openfuxml.content.table.Columns;
import org.openfuxml.content.table.Content;
import org.openfuxml.content.table.Head;
import org.openfuxml.content.table.Row;
import org.openfuxml.content.table.Specification;
import org.openfuxml.content.table.Table;
import org.openfuxml.exception.OfxAuthoringException;
import org.openfuxml.factory.xml.layout.XmlAlignmentFactory;
import org.openfuxml.factory.xml.layout.XmlFloatFactory;
import org.openfuxml.factory.xml.ofx.content.XmlCommentFactory;
import org.openfuxml.factory.xml.ofx.content.text.XmlTitleFactory;
import org.openfuxml.factory.xml.table.OfxCellFactory;
import org.openfuxml.factory.xml.table.OfxColumnFactory;
import org.openfuxml.util.OfxCommentBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OfxQaSummaryTableFactory extends AbstractUtilsOfxDocumentationFactory
{
	final static Logger logger = LoggerFactory.getLogger(OfxQaSummaryTableFactory.class);
	private static String keyCaptionPrefix = "auTableQmAgreement";
	
	private List<String> headerKeys;
	
	private String imagePathPrefix;
	public String getImagePathPrefix() {return imagePathPrefix;}
	public void setImagePathPrefix(String imagePathPrefix) {this.imagePathPrefix = imagePathPrefix;}
	
	private Aht testConditions;
	private Aht resultStatus;
	
	public OfxQaSummaryTableFactory(Configuration config, String lang, Translations translations)
	{
		this(config,new String[] {lang},translations);
	}
	public OfxQaSummaryTableFactory(Configuration config, String[] langs, Translations translations)
	{
		super(config,langs,translations);
		
		headerKeys = new ArrayList<String>();
		headerKeys.add("auTableQaTestCode");
		headerKeys.add("auTableQaTestCase");
	}
	
	public Table build(Category category,Aht testConditions,Aht resultStatus) throws OfxAuthoringException
	{
		this.testConditions=testConditions;
		this.resultStatus=resultStatus;
		try
		{	
			Table table = toOfx(category);
			table.setId("table.qa.agreements."+category.getCode());
			
			if(langs.length>1){logger.warn("Incorrect Assignment");}
			Lang lCaption = StatusXpath.getLang(translations, "auTableQmSummary", langs[0]);
			table.setTitle(XmlTitleFactory.build(lCaption.getTranslation()+": "+category.getName()+" ("+category.getCode()+")"));
			
			Comment comment = XmlCommentFactory.build();
			OfxCommentBuilder.fixedId(comment, table.getId());
			DocumentationCommentBuilder.translationKeys(comment,config,UtilsDocumentation.keyTranslationFile);
			DocumentationCommentBuilder.tableHeaders(comment,headerKeys);
			DocumentationCommentBuilder.tableKey(comment,keyCaptionPrefix,"Table Caption Prefix");
			OfxCommentBuilder.doNotModify(comment);
			table.setComment(comment);
			
			return table;
		}
		catch (ExlpXpathNotFoundException e) {throw new OfxAuthoringException(e.getMessage());}
		catch (ExlpXpathNotUniqueException e) {throw new OfxAuthoringException(e.getMessage());}
	}
	
	public Table toOfx(Category category)
	{
		Table table = new Table();
		table.setSpecification(createSpecifications());
		table.setContent(createContent(category));
		return table;
	}
	
	private Specification createSpecifications()
	{
		Columns cols = new Columns();
		cols.getColumn().add(OfxColumnFactory.build(XmlAlignmentFactory.Horizontal.left));
		cols.getColumn().add(OfxColumnFactory.flex(80));
		cols.getColumn().add(OfxColumnFactory.build(XmlAlignmentFactory.Horizontal.center));
		cols.getColumn().add(OfxColumnFactory.build(XmlAlignmentFactory.Horizontal.center));
			
		Specification specification = new Specification();
		specification.setColumns(cols);
		specification.setFloat(XmlFloatFactory.build(false));
		
		return specification;
	}
	
	private Content createContent(Category category)
	{
		Head head = new Head();
		head.getRow().add(createHeaderRow(headerKeys));
		head.getRow().get(0).getCell().add(OfxCellFactory.createParagraphCell(category.getQa().getClient()));
		head.getRow().get(0).getCell().add(OfxCellFactory.createParagraphCell(category.getQa().getDeveloper()));
		
		Body body = new Body();
		for(Test staff : category.getTest())
		{
			body.getRow().add(createRow(staff));
		}
		
		Content content = new Content();
		content.getBody().add(body);
		content.setHead(head);
		
		return content;
	}
	
	private Row createRow(Test test)
	{
		Row row = new Row();
		
		try
		{
			Cell cellClient = new Cell();
			for(Result result : test.getResults().getResult())
			{
				if(result.getStaff().getRole().getCode().equals("qaManager"))
				{
					cellClient.getContent().add(OfxStatusImageFactory.build(imagePathPrefix,StatusXpath.getStatus(resultStatus.getStatus(), result.getStatus().getCode())));
				}
			}
			
			row.getCell().add(OfxCellFactory.createParagraphCell(test.getCode()));
			row.getCell().add(OfxCellFactory.createParagraphCell(test.getName()));
			row.getCell().add(OfxCellFactory.image(OfxStatusImageFactory.build(imagePathPrefix,StatusXpath.getStatus(testConditions.getStatus(), test.getInfo().getStatus().getCode()))));
			row.getCell().add(cellClient);
		}
		catch (ExlpXpathNotFoundException e) {e.printStackTrace();}
		catch (ExlpXpathNotUniqueException e) {e.printStackTrace();}
		
		return row;
	}	
}