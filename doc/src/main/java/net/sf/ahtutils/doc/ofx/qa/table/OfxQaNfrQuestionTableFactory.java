package net.sf.ahtutils.doc.ofx.qa.table;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.configuration.Configuration;
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
import org.openfuxml.factory.xml.table.OfxCellFactory;
import org.openfuxml.factory.xml.table.OfxColumnFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.doc.ofx.AbstractUtilsOfxDocumentationFactory;
import net.sf.ahtutils.doc.ofx.util.OfxMultiLangFactory;
import net.sf.ahtutils.xml.aht.Aht;
import net.sf.ahtutils.xml.status.Lang;
import net.sf.ahtutils.xml.status.Translations;
import net.sf.ahtutils.xml.survey.Question;
import net.sf.ahtutils.xml.xpath.StatusXpath;
import net.sf.exlp.exception.ExlpXpathNotFoundException;
import net.sf.exlp.exception.ExlpXpathNotUniqueException;

public class OfxQaNfrQuestionTableFactory extends AbstractUtilsOfxDocumentationFactory
{
	final static Logger logger = LoggerFactory.getLogger(OfxQaSummaryTableFactory.class);
	
	private List<String> headerKeys;
	
	private String imagePathPrefix;
	public String getImagePathPrefix() {return imagePathPrefix;}
	public void setImagePathPrefix(String imagePathPrefix) {this.imagePathPrefix = imagePathPrefix;}
		
	public OfxQaNfrQuestionTableFactory(Configuration config, String[] langs, Translations translations)
	{
		super(config,langs,translations);
		
		headerKeys = new ArrayList<String>();
		headerKeys.add("auTableQaNfrNr");
		headerKeys.add("auTableQaNfrQuestion");
		headerKeys.add("auTableQaNfrUnit");
	}
	
	public Table build(net.sf.ahtutils.xml.survey.Section section) throws OfxAuthoringException
	{
		try
		{	
			Table table = toOfx(section);
			table.setId("table.qa.nfr.questions."+section.getPosition());
			
			Lang lCaption = StatusXpath.getLang(translations, "auTableQaNfrQuestionSummary", langs[0]);
			table.setTitle(XmlTitleFactory.build(lCaption.getTranslation()+": "+section.getDescription().getValue()));
						
			return table;
		}
		catch (ExlpXpathNotFoundException e) {throw new OfxAuthoringException(e.getMessage());}
		catch (ExlpXpathNotUniqueException e) {throw new OfxAuthoringException(e.getMessage());}
	}
	
	public Table toOfx(net.sf.ahtutils.xml.survey.Section section)
	{
		Table table = new Table();
		table.setSpecification(createSpecifications());
		table.setContent(createContent(section));
		return table;
	}
	
	private Specification createSpecifications()
	{
		Specification spec = new Specification();
		spec.setFloat(XmlFloatFactory.build(false));
		
		spec.setColumns(new Columns());
		spec.getColumns().getColumn().add(OfxColumnFactory.build(XmlAlignmentFactory.Horizontal.left));
		spec.getColumns().getColumn().add(OfxColumnFactory.flex(80));
		spec.getColumns().getColumn().add(OfxColumnFactory.build(XmlAlignmentFactory.Horizontal.center));
		
		return spec;
	}
	
	private Content createContent(net.sf.ahtutils.xml.survey.Section section)
	{
		Head head = new Head();
		head.getRow().add(createHeaderRow(headerKeys));
		
		Body body = new Body();
		for(Question q : section.getQuestion())
		{
			body.getRow().add(createRow(q));
		}
		
		Content content = new Content();
		content.getBody().add(body);
		content.setHead(head);
		
		return content;
	}
	
	private Row createRow(Question q)
	{
		Row row = new Row();
		
		row.getCell().add(OfxCellFactory.createParagraphCell(q.getPosition()));
		row.getCell().add(OfxCellFactory.createParagraphCell(q.getQuestion().getValue()));
		row.getCell().add(OfxMultiLangFactory.cell(langs, q.getUnit().getCode(), units));
		
		return row;
	}
	
	private Aht units;
	public Aht getUnits() {return units;}
	public void setUnits(Aht units) {this.units = units;}
}