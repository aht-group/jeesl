package net.sf.ahtutils.doc.ofx.qa.table;

import org.apache.commons.configuration.Configuration;
import org.jeesl.util.query.xpath.StatusXpath;
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
import org.openfuxml.factory.xml.ofx.content.text.XmlTitleFactory;
import org.openfuxml.factory.xml.table.XmlCellFactory;
import org.openfuxml.factory.xml.table.XmlColumnFactory;
import org.openfuxml.trancoder.html.HtmlTranscoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.doc.ofx.AbstractUtilsOfxDocumentationFactory;
import net.sf.ahtutils.xml.qa.Group;
import net.sf.ahtutils.xml.qa.Groups;
import net.sf.ahtutils.xml.qa.Test;
import net.sf.ahtutils.xml.status.Lang;
import net.sf.ahtutils.xml.status.Translations;
import net.sf.exlp.exception.ExlpXpathNotFoundException;
import net.sf.exlp.exception.ExlpXpathNotUniqueException;
import net.sf.exlp.util.xml.JaxbUtil;

public class OfxQaFrTableFactory extends AbstractUtilsOfxDocumentationFactory
{
	final static Logger logger = LoggerFactory.getLogger(OfxQaFrTableFactory.class);
	
	public OfxQaFrTableFactory(Configuration config, String lang, Translations translations)
	{
		this(config,new String[] {lang},translations);
	}
	public OfxQaFrTableFactory(Configuration config, String[] langs, Translations translations)
	{
		super(config,langs,translations);
	}
	
	public Table buildTableTestDetails(Test test) throws OfxAuthoringException
	{
		try
		{
			Table table = new Table();
			if(langs.length>1){logger.warn("Incorrect Assignment");}
			Lang lCaption = StatusXpath.getLang(translations, "auTableCaptionQaTest", langs[0]);
			table.setTitle(XmlTitleFactory.build(lCaption.getTranslation()+" "+test.getCode()));
			
			table.setSpecification(createTableSpecifications());
			table.setContent(createTableContent(test));
			JaxbUtil.trace(table);
			return table;
		}
		catch (ExlpXpathNotFoundException e) {throw new OfxAuthoringException(e.getMessage());}
		catch (ExlpXpathNotUniqueException e) {throw new OfxAuthoringException(e.getMessage());}
	}
	
	private Specification createTableSpecifications()
	{
		Columns cols = new Columns();
		XmlColumnFactory.add(cols,XmlAlignmentFactory.Horizontal.left);
		cols.getColumn().add(XmlColumnFactory.flex(80));
		
		Specification specification = new Specification();
		specification.setFloat(XmlFloatFactory.build(false));
		specification.setColumns(cols);
		
		return specification;
	}
	
	protected Row createHeaderRow(Test test)
	{
		Row row = new Row();
		row.getCell().add(XmlCellFactory.createParagraphCell("Test Case"));
		row.getCell().add(XmlCellFactory.createParagraphCell(test.getCode()));
		
		return row;
	}
	
	private Content createTableContent(Test test)
	{
		JaxbUtil.trace(test);

		Head head = new Head();
		head.getRow().add(createHeaderRow(test));
		
		Body body = new Body();
		body.getRow().add(buildTitle(test));
		body.getRow().add(buildReference(test));
		body.getRow().add(buildPreCondition(test));
		body.getRow().add(buildSteps(test));
		body.getRow().add(buildDuration(test));
		if(test.isSetGroups() && test.getGroups().isSetGroup()){body.getRow().add(buildGroups(test.getGroups()));}
		
		Content content = new Content();
		content.getBody().add(body);
		content.setHead(head);
		
		return content;
	}
	
	private Row buildTitle(Test test)
	{
		Row row = new Row();
		row.getCell().add(XmlCellFactory.createParagraphCell("Title"));
		row.getCell().add(XmlCellFactory.createParagraphCell(test.getName()));
		return row;
	}
	
	private Row buildReference(Test test)
	{
		Row row = new Row();
		row.getCell().add(XmlCellFactory.createParagraphCell("Reference"));
		if(test.isSetReference() && test.getReference().isSetValue())
		{
			row.getCell().add(XmlCellFactory.createParagraphCell(test.getReference().getValue()));
		}
		else
		{
			row.getCell().add(XmlCellFactory.createParagraphCell(""));
		}
		return row;
	}
	
	private Row buildDuration(Test test)
	{
		Row row = new Row();
		row.getCell().add(XmlCellFactory.createParagraphCell("Duration"));
		row.getCell().add(XmlCellFactory.createParagraphCell(test.getDuration()+" minutes"));
		return row;
	}
	
	private Row buildGroups(Groups groups)
	{
		Row row = new Row();
		row.getCell().add(XmlCellFactory.createParagraphCell("Groups"));
		
		StringBuffer sb = new StringBuffer();
		for(Group g : groups.getGroup())
		{
			sb.append(g.getName()+", ");
		}
		if(sb.length()>0){sb = new StringBuffer(sb.substring(0, sb.length()-2));}
		
		row.getCell().add(XmlCellFactory.createParagraphCell(sb.toString()));
		return row;
	}
	
	private Row buildPreCondition(Test test)
	{
		Row row = new Row();
		row.getCell().add(XmlCellFactory.createParagraphCell("Pre-Condition"));
		if(test.isSetPreCondition() && test.getPreCondition().isSetValue())
		{
			row.getCell().add(XmlCellFactory.createParagraphCell(test.getPreCondition().getValue()));
		}
		else
		{
			row.getCell().add(XmlCellFactory.createParagraphCell(""));
		}
		return row;
	}
	
	private Row buildSteps(Test test)
	{
		Row row = new Row();
		row.getCell().add(XmlCellFactory.createParagraphCell("Test Steps"));
		if(test.isSetSteps() && test.getSteps().isSetValue())
		{
			logger.trace("Steps");
			HtmlTranscoder transcoder = new HtmlTranscoder();
			transcoder.transcode(test.getSteps().getValue());
			Cell cell = XmlCellFactory.build();
			cell.getContent().addAll(transcoder.transcode(test.getSteps().getValue()).getContent());
			row.getCell().add(cell);
		}
		else
		{
			row.getCell().add(XmlCellFactory.createParagraphCell(""));
		}
		return row;
	}
}