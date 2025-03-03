package net.sf.ahtutils.doc.ofx.qa.table;

import java.util.ArrayList;
import java.util.List;

import org.exlp.interfaces.system.property.Configuration;
import org.jeesl.doc.ofx.OfxMultiLangFactory;
import org.jeesl.model.xml.io.locale.status.Lang;
import org.jeesl.model.xml.io.locale.status.Translations;
import org.jeesl.model.xml.module.survey.Question;
import org.jeesl.model.xml.xsd.Container;
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
import net.sf.exlp.exception.ExlpXpathNotFoundException;
import net.sf.exlp.exception.ExlpXpathNotUniqueException;

public class OfxQaNfrQuestionTableFactory extends AbstractUtilsOfxDocumentationFactory
{
	final static Logger logger = LoggerFactory.getLogger(OfxQaNfrQuestionTableFactory.class);
	
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
	
	public Table build(org.jeesl.model.xml.module.survey.Section mainSection, org.jeesl.model.xml.module.survey.Section subSection) throws OfxAuthoringException
	{
		try
		{	
			Table table = toOfx(subSection);
			table.setId("table.qa.nfr.questions."+subSection.getPosition());
			
			Lang lCaption = StatusXpath.getLang(translations, "auTableQaNfrQuestionSummary", langs[0]);
			table.setTitle(XmlTitleFactory.build(lCaption.getTranslation()+": "+mainSection.getDescription().getValue()+" - "+subSection.getDescription().getValue()));
						
			return table;
		}
		catch (ExlpXpathNotFoundException e) {throw new OfxAuthoringException(e.getMessage());}
		catch (ExlpXpathNotUniqueException e) {throw new OfxAuthoringException(e.getMessage());}
	}
	
	public Table toOfx(org.jeesl.model.xml.module.survey.Section section)
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
		XmlColumnFactory.add(spec.getColumns(),XmlAlignmentFactory.Horizontal.left);
		spec.getColumns().getColumn().add(XmlColumnFactory.flex(80));
		XmlColumnFactory.add(spec.getColumns(),XmlAlignmentFactory.Horizontal.center);
		
		return spec;
	}
	
	private Content createContent(org.jeesl.model.xml.module.survey.Section section)
	{
		Head head = new Head();
		head.getRow().add(createHeaderRow(headerKeys));
		
		Body body = new Body();
		for(Question q : section.getQuestion())
		{
			if(q.isVisible())
			{
				body.getRow().add(createRow(q));
			}
		}
		
		Content content = new Content();
		content.getBody().add(body);
		content.setHead(head);
		
		return content;
	}
	
	private Row createRow(Question q)
	{
		Row row = new Row();
		
		row.getCell().add(XmlCellFactory.createParagraphCell(q.getPosition()));
		row.getCell().add(XmlCellFactory.createParagraphCell(q.getQuestion().getValue()));
		row.getCell().add(OfxMultiLangFactory.cell(langs, q.getUnit().getCode(), units));
		
		return row;
	}
	
	private Container units; public Container getUnits() {return units;} public void setUnits(Container units) {this.units = units;}
}