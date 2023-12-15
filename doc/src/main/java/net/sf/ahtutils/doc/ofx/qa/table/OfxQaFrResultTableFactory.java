package net.sf.ahtutils.doc.ofx.qa.table;

import net.sf.ahtutils.doc.ofx.AbstractUtilsOfxDocumentationFactory;
import net.sf.ahtutils.doc.ofx.status.OfxStatusImageFactory;
import net.sf.ahtutils.xml.qa.Result;
import net.sf.ahtutils.xml.qa.Test;
import net.sf.exlp.exception.ExlpXpathNotFoundException;
import net.sf.exlp.exception.ExlpXpathNotUniqueException;
import net.sf.exlp.util.xml.JaxbUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.commons.configuration.Configuration;
import org.jeesl.model.xml.io.locale.status.Lang;
import org.jeesl.model.xml.io.locale.status.Translations;
import org.jeesl.util.query.xpath.StatusXpath;
import org.openfuxml.content.table.Body;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OfxQaFrResultTableFactory extends AbstractUtilsOfxDocumentationFactory
{
	final static Logger logger = LoggerFactory.getLogger(OfxQaFrResultTableFactory.class);
	
	private DateFormat df;
	
	public OfxQaFrResultTableFactory(Configuration config, String lang, Translations translations)
	{
		this(config,new String[] {lang},translations);
	}
	public OfxQaFrResultTableFactory(Configuration config, String[] langs, Translations translations)
	{
		super(config,langs,translations);
		imagePathPrefix = config.getString("doc.ofx.imagePathPrefixQA");
		df = SimpleDateFormat.getDateInstance();
	}
	
	public Table build(Test test) throws OfxAuthoringException
	{
		try
		{
			Table table = new Table();
			if(langs.length>1){logger.warn("Incorrect Assignment");}
			Lang lCaption = StatusXpath.getLang(translations, "auTableCaptionQaTestResults", langs[0]);
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
		cols.getColumn().add(XmlColumnFactory.flex(20,true));
		cols.getColumn().add(XmlColumnFactory.flex(20,true));
		cols.getColumn().add(XmlColumnFactory.build(XmlAlignmentFactory.Horizontal.left));
		cols.getColumn().add(XmlColumnFactory.flex(20,true));
		cols.getColumn().add(XmlColumnFactory.flex(60));
		
		Specification specification = new Specification();
		specification.setFloat(XmlFloatFactory.build(false));
		specification.setColumns(cols);
		
		return specification;
	}
	
	protected Row createHeaderRow(Test test)
	{
		Row row = new Row();
		row.getCell().add(XmlCellFactory.createParagraphCell("Date"));
		row.getCell().add(XmlCellFactory.createParagraphCell("User"));
		row.getCell().add(XmlCellFactory.createParagraphCell("Result"));
		row.getCell().add(XmlCellFactory.createParagraphCell("Actual"));
		row.getCell().add(XmlCellFactory.createParagraphCell("Comment"));
		
		return row;
	}
	
	private Content createTableContent(Test test)
	{
		Head head = new Head();
		head.getRow().add(createHeaderRow(test));
		
		Body body = new Body();
		if(test.isSetResults())
		{
			for(Result result : test.getResults().getResult())
			{
				body.getRow().add(buildRow(result));
			}
		}
		
		Content content = new Content();
		content.getBody().add(body);
		content.setHead(head);
		
		return content;
	}
	
	private Row buildRow(Result result)
	{
		JaxbUtil.trace(result);
		Row row = new Row();
		
		if(result.isSetRecord()){row.getCell().add(XmlCellFactory.createParagraphCell(df.format(result.getRecord().toGregorianCalendar().getTime())));}
		else{row.getCell().add(XmlCellFactory.createParagraphCell(""));}
		
		row.getCell().add(XmlCellFactory.createParagraphCell(result.getStaff().getUser().getLastName()));
		row.getCell().add(XmlCellFactory.image(OfxStatusImageFactory.build(imagePathPrefix,result.getStatus())));
		row.getCell().add(XmlCellFactory.createParagraphCell(result.getActual().getValue()));
		row.getCell().add(XmlCellFactory.createParagraphCell(result.getComment().getValue()));
		return row;
	}
}