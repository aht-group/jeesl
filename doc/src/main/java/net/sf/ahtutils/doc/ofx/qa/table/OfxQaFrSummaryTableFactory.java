package net.sf.ahtutils.doc.ofx.qa.table;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.exlp.interfaces.system.property.Configuration;
import org.exlp.util.jx.JaxbUtil;
import org.jeesl.exception.processing.UtilsConfigurationException;
import org.jeesl.model.xml.io.locale.status.Lang;
import org.jeesl.model.xml.io.locale.status.Translations;
import org.jeesl.model.xml.module.dev.qa.Category;
import org.jeesl.model.xml.module.dev.qa.Result;
import org.jeesl.model.xml.module.dev.qa.Test;
import org.jeesl.util.query.xpath.StatusXpath;
import org.openfuxml.exception.OfxAuthoringException;
import org.openfuxml.factory.xml.layout.XmlAlignmentFactory;
import org.openfuxml.factory.xml.layout.XmlFloatFactory;
import org.openfuxml.factory.xml.ofx.content.XmlCommentFactory;
import org.openfuxml.factory.xml.ofx.content.text.XmlTitleFactory;
import org.openfuxml.factory.xml.table.XmlCellFactory;
import org.openfuxml.factory.xml.table.XmlColumnFactory;
import org.openfuxml.model.xml.core.ofx.Comment;
import org.openfuxml.model.xml.core.table.Body;
import org.openfuxml.model.xml.core.table.Cell;
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
import net.sf.ahtutils.doc.ofx.status.OfxStatusImageFactory;
import net.sf.ahtutils.xml.aht.Aht;
import net.sf.exlp.exception.ExlpXpathNotFoundException;
import net.sf.exlp.exception.ExlpXpathNotUniqueException;

public class OfxQaFrSummaryTableFactory extends AbstractUtilsOfxDocumentationFactory
{
	final static Logger logger = LoggerFactory.getLogger(OfxQaNfrQuestionTableFactory.class);
	private static String keyCaptionPrefix = "auTableQmAgreement";
	
	private List<String> headerKeys;
	
	private String imagePathPrefix;
	public String getImagePathPrefix() {return imagePathPrefix;}
	public void setImagePathPrefix(String imagePathPrefix) {this.imagePathPrefix = imagePathPrefix;}
	
	private Aht testConditions;
	private Aht resultStatus;
	
	public OfxQaFrSummaryTableFactory(Configuration config, String lang, Translations translations)
	{
		this(config,new String[] {lang},translations);
	}
	public OfxQaFrSummaryTableFactory(Configuration config, String[] langs, Translations translations)
	{
		super(config,langs,translations);
		
		headerKeys = new ArrayList<String>();
		headerKeys.add("auTableQaTestCode");
		headerKeys.add("auTableQaTestCase");
	}
	
	public Table build(boolean withResults, Category category,Aht testConditions,Aht resultStatus) throws OfxAuthoringException, UtilsConfigurationException
	{
		this.testConditions=testConditions;
		this.resultStatus=resultStatus;
		try
		{	
			Table table = toOfx(withResults,category);
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
	
	public Table toOfx(boolean withResults, Category category)
	{
		Table table = new Table();
		table.setSpecification(createSpecifications());
		table.setContent(createContent(withResults,category));
		return table;
	}
	
	private Specification createSpecifications()
	{
		Columns cols = new Columns();
		cols.getColumn().add(XmlColumnFactory.build(XmlAlignmentFactory.Horizontal.left));
		cols.getColumn().add(XmlColumnFactory.flex(80));
		cols.getColumn().add(XmlColumnFactory.build(XmlAlignmentFactory.Horizontal.center));
		cols.getColumn().add(XmlColumnFactory.build(XmlAlignmentFactory.Horizontal.center));
			
		Specification specification = new Specification();
		specification.setColumns(cols);
		specification.setFloat(XmlFloatFactory.build(false));
		
		return specification;
	}
	
	private Content createContent(boolean withResults, Category category)
	{
		Head head = new Head();
		head.getRow().add(createHeaderRow(headerKeys));
		head.getRow().get(0).getCell().add(XmlCellFactory.createParagraphCell(category.getQa().getClient()));
		head.getRow().get(0).getCell().add(XmlCellFactory.createParagraphCell(category.getQa().getDeveloper()));
		
		Body body = new Body();
		for(Test test : category.getTest())
		{
			if(test.isVisible())
			{
				body.getRow().add(createRow(withResults,test));
			}
		}
		
		Content content = new Content();
		content.getBody().add(body);
		content.setHead(head);
		
		return content;
	}
	
	private Row createRow(boolean withResults, Test test)
	{
		Row row = new Row();
		JaxbUtil.trace(test);
		try
		{
			Cell cellClient;
			if(withResults)
			{
				cellClient = new Cell();
				for(Result result : test.getResults().getResult())
				{
					if(result.getStaff().getRole().getCode().equals("qaManager"))
					{
						cellClient.getContent().add(OfxStatusImageFactory.build(imagePathPrefix,StatusXpath.getStatus(resultStatus.getStatus(), result.getStatus().getCode())));
					}
				}
			}
			else{cellClient=XmlCellFactory.createParagraphCell("");}
			
			
			row.getCell().add(XmlCellFactory.createParagraphCell(test.getCode()));
			row.getCell().add(XmlCellFactory.createParagraphCell(test.getName()));
			
			if(Objects.nonNull(test.getInfo()) && Objects.nonNull(test.getInfo().getStatus()))
			{
				row.getCell().add(XmlCellFactory.image(OfxStatusImageFactory.build(imagePathPrefix,StatusXpath.getStatus(testConditions.getStatus(), test.getInfo().getStatus().getCode()))));
			}
			else
			{
				row.getCell().add(XmlCellFactory.createParagraphCell(""));
			}
			
			
			row.getCell().add(cellClient);
		}
		catch (ExlpXpathNotFoundException e) {e.printStackTrace();}
		catch (ExlpXpathNotUniqueException e) {e.printStackTrace();}
		
		return row;
	}
}