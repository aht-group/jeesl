package net.sf.ahtutils.doc.ofx.security.table;

import java.util.ArrayList;
import java.util.List;

import org.jeesl.doc.ofx.OfxMultiLangFactory;
import org.jeesl.model.xml.io.locale.status.Translations;
import org.jeesl.model.xml.system.security.View;
import org.jeesl.model.xml.system.security.Views;
import org.openfuxml.exception.OfxAuthoringException;
import org.openfuxml.factory.xml.layout.XmlFloatFactory;
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

public class OfxSecurityViewTableFactory extends AbstractUtilsOfxDocumentationFactory
{
final static Logger logger = LoggerFactory.getLogger(OfxSecurityUsecaseTableFactory.class);
	
	private List<String> headerKeys;
		
	public OfxSecurityViewTableFactory(org.exlp.interfaces.system.property.Configuration config, String[] langs, Translations translations)
	{
		super(config,langs,translations);
		
		headerKeys = new ArrayList<String>();
		headerKeys.add("auTableHeadSecurityUsecaseName");
		headerKeys.add("auTableHeadSecurityUsecaseDescription");
	}
	
	public Table build(org.jeesl.model.xml.system.security.Category category) throws OfxAuthoringException
	{
		Table table = new Table();
//		table.setId("table.qa.nfr.questions."+section.getPosition());
		table.setSpecification(createSpecifications());
		
		table.setTitle(OfxMultiLangFactory.title(langs, category.getLangs()));
			
		table.setContent(createContent(category.getViews()));
		
		return table;
	}
	
	private Specification createSpecifications()
	{
		Specification spec = new Specification();
		spec.setFloat(XmlFloatFactory.build(false));
		spec.setColumns(new Columns());
		spec.getColumns().getColumn().add(XmlColumnFactory.flex(30,true));
		spec.getColumns().getColumn().add(XmlColumnFactory.flex(70,false));
		
		return spec;
	}
	
	private Content createContent(Views views) throws OfxAuthoringException
	{
		Head head = new Head();
		head.getRow().add(createHeaderRow(headerKeys));
		
		Body body = new Body();
		for(View view : views.getView())
		{
			body.getRow().add(createRow(view));
		}
		
		Content content = new Content();
		content.getBody().add(body);
		content.setHead(head);
		
		return content;
	}
	
	private Row createRow(View view) throws OfxAuthoringException
	{
		Row row = new Row();
		
		row.getCell().add(OfxMultiLangFactory.cell(langs, view.getLangs()));
		row.getCell().add(OfxMultiLangFactory.cell(langs, view.getDescriptions()));
		
		return row;
	}
}