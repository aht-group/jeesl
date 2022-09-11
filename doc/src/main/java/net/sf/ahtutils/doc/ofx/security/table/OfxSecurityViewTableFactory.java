package net.sf.ahtutils.doc.ofx.security.table;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.configuration.Configuration;
import org.jeesl.doc.ofx.OfxMultiLangFactory;
import org.openfuxml.content.table.Body;
import org.openfuxml.content.table.Columns;
import org.openfuxml.content.table.Content;
import org.openfuxml.content.table.Head;
import org.openfuxml.content.table.Row;
import org.openfuxml.content.table.Specification;
import org.openfuxml.content.table.Table;
import org.openfuxml.exception.OfxAuthoringException;
import org.openfuxml.factory.xml.layout.XmlFloatFactory;
import org.openfuxml.factory.xml.table.XmlColumnFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.doc.ofx.AbstractUtilsOfxDocumentationFactory;
import net.sf.ahtutils.xml.security.View;
import net.sf.ahtutils.xml.security.Views;
import net.sf.ahtutils.xml.status.Translations;

public class OfxSecurityViewTableFactory extends AbstractUtilsOfxDocumentationFactory
{
final static Logger logger = LoggerFactory.getLogger(OfxSecurityUsecaseTableFactory.class);
	
	private List<String> headerKeys;
		
	public OfxSecurityViewTableFactory(Configuration config, String[] langs, Translations translations)
	{
		super(config,langs,translations);
		
		headerKeys = new ArrayList<String>();
		headerKeys.add("auTableHeadSecurityUsecaseName");
		headerKeys.add("auTableHeadSecurityUsecaseDescription");
	}
	
	@Deprecated
	public Table build(net.sf.ahtutils.xml.access.Category category) throws OfxAuthoringException
	{
		Table table = new Table();
//		table.setId("table.qa.nfr.questions."+section.getPosition());
		table.setSpecification(createSpecifications());
		
		table.setTitle(OfxMultiLangFactory.title(langs, category.getLangs()));
			
		table.setContent(createContent(category.getViews()));
		
		return table;
	}
	
	public Table build(net.sf.ahtutils.xml.security.Category category) throws OfxAuthoringException
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