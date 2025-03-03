package net.sf.ahtutils.doc.ofx.qa.table;

import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;
import org.exlp.interfaces.system.property.Configuration;
import org.exlp.util.jx.JaxbUtil;
import org.jeesl.model.xml.io.locale.status.Lang;
import org.jeesl.model.xml.io.locale.status.Translations;
import org.jeesl.model.xml.module.dev.qa.Group;
import org.jeesl.model.xml.module.dev.qa.Groups;
import org.jeesl.model.xml.module.dev.qa.Test;
import org.jeesl.util.query.xpath.StatusXpath;
import org.openfuxml.exception.OfxAuthoringException;
import org.openfuxml.factory.xml.layout.XmlAlignmentFactory;
import org.openfuxml.factory.xml.layout.XmlFloatFactory;
import org.openfuxml.factory.xml.ofx.content.text.XmlTitleFactory;
import org.openfuxml.factory.xml.table.XmlCellFactory;
import org.openfuxml.factory.xml.table.XmlColumnFactory;
import org.openfuxml.model.xml.core.table.Body;
import org.openfuxml.model.xml.core.table.Cell;
import org.openfuxml.model.xml.core.table.Columns;
import org.openfuxml.model.xml.core.table.Content;
import org.openfuxml.model.xml.core.table.Head;
import org.openfuxml.model.xml.core.table.Row;
import org.openfuxml.model.xml.core.table.Specification;
import org.openfuxml.model.xml.core.table.Table;
import org.openfuxml.trancoder.html.HtmlTranscoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.doc.ofx.AbstractUtilsOfxDocumentationFactory;
import net.sf.exlp.exception.ExlpXpathNotFoundException;
import net.sf.exlp.exception.ExlpXpathNotUniqueException;

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
		if(Objects.nonNull(test.getGroups()) && ObjectUtils.isNotEmpty(test.getGroups().getGroup())) {body.getRow().add(buildGroups(test.getGroups()));}
		
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
		if(Objects.nonNull(test.getReference()) && Objects.nonNull(test.getReference().getValue()))
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
		if(Objects.nonNull(test.getPreCondition()) && Objects.nonNull(test.getPreCondition().getValue()))
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
		if(Objects.nonNull(test.getSteps()) && Objects.nonNull(test.getSteps().getValue()))
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