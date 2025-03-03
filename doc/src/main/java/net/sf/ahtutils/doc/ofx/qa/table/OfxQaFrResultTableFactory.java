package net.sf.ahtutils.doc.ofx.qa.table;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Objects;

import org.exlp.interfaces.system.property.Configuration;
import org.exlp.util.jx.JaxbUtil;
import org.jeesl.model.xml.io.locale.status.Lang;
import org.jeesl.model.xml.io.locale.status.Translations;
import org.jeesl.model.xml.module.dev.qa.Result;
import org.jeesl.model.xml.module.dev.qa.Test;
import org.jeesl.util.query.xpath.StatusXpath;
import org.openfuxml.exception.OfxAuthoringException;
import org.openfuxml.factory.xml.layout.XmlAlignmentFactory;
import org.openfuxml.factory.xml.layout.XmlFloatFactory;
import org.openfuxml.factory.xml.ofx.content.text.XmlTitleFactory;
import org.openfuxml.factory.xml.table.XmlCellFactory;
import org.openfuxml.factory.xml.table.XmlColumnFactory;
import org.openfuxml.model.xml.core.table.Body;
import org.openfuxml.model.xml.core.table.Columns;
import org.openfuxml.model.xml.core.table.Content;
import org.openfuxml.model.xml.core.table.Head;
import org.openfuxml.model.xml.core.table.Row;
import org.openfuxml.model.xml.core.table.Specification;
import org.openfuxml.model.xml.core.table.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.doc.ofx.AbstractUtilsOfxDocumentationFactory;
import net.sf.ahtutils.doc.ofx.status.OfxStatusImageFactory;
import net.sf.exlp.exception.ExlpXpathNotFoundException;
import net.sf.exlp.exception.ExlpXpathNotUniqueException;

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
		if(Objects.nonNull(test.getResults()))
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
		
		if(Objects.nonNull(result.getRecord())) {row.getCell().add(XmlCellFactory.createParagraphCell(df.format(result.getRecord().toGregorianCalendar().getTime())));}
		else{row.getCell().add(XmlCellFactory.createParagraphCell(""));}
		
		row.getCell().add(XmlCellFactory.createParagraphCell(result.getStaff().getUser().getLastName()));
		row.getCell().add(XmlCellFactory.image(OfxStatusImageFactory.build(imagePathPrefix,result.getStatus())));
		row.getCell().add(XmlCellFactory.createParagraphCell(result.getActual().getValue()));
		row.getCell().add(XmlCellFactory.createParagraphCell(result.getComment().getValue()));
		return row;
	}
}