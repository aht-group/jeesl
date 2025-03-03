package org.jeesl.doc.ofx.constraints;

import java.util.ArrayList;
import java.util.List;

import org.exlp.interfaces.system.property.Configuration;
import org.jeesl.doc.ofx.OfxMultiLangFactory;
import org.jeesl.exception.processing.UtilsConfigurationException;
import org.jeesl.model.xml.io.locale.status.Translations;
import org.jeesl.model.xml.system.constraint.Constraint;
import org.jeesl.model.xml.system.constraint.ConstraintScope;
import org.jeesl.model.xml.xsd.Container;
import org.openfuxml.exception.OfxAuthoringException;
import org.openfuxml.factory.xml.layout.XmlFloatFactory;
import org.openfuxml.factory.xml.ofx.content.XmlCommentFactory;
import org.openfuxml.factory.xml.table.XmlColumnFactory;
import org.openfuxml.model.xml.core.ofx.Comment;
import org.openfuxml.model.xml.core.table.Body;
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

public class OfxConstraintTableFactory extends AbstractUtilsOfxDocumentationFactory
{
	final static Logger logger = LoggerFactory.getLogger(OfxConstraintTableFactory.class);
	private static String keyCaptionPrefix = "auTableQmAgreement";
	
	private List<String> headerKeys;
	
	@SuppressWarnings("unused")
	private String imagePathPrefix; public void setImagePathPrefix(String imagePathPrefix) {this.imagePathPrefix = imagePathPrefix;}
	
	private Container constraintTypes; public void setConstraintTypes(Container constraintTypes) {this.constraintTypes = constraintTypes;}
	
	public OfxConstraintTableFactory(Configuration config, String[] langs, Translations translations)
	{
		super(config,langs,translations);
		
		headerKeys = new ArrayList<String>();
		headerKeys.add("auTableConstraintType");
//		headerKeys.add("auTableConstraintAttribute");
		headerKeys.add("auTableConstraintDescription");
	}
	
	public Table build(ConstraintScope scope) throws OfxAuthoringException, UtilsConfigurationException
	{
		Table table = new Table();
		table.setId("table.constraints."+scope.getCategory()+"."+scope.getCode());
		table.setSpecification(createSpecifications());
		
		table.setTitle(OfxMultiLangFactory.title(langs,scope.getLangs()));
		
		Comment comment = XmlCommentFactory.build();
		OfxCommentBuilder.fixedId(comment, table.getId());
		DocumentationCommentBuilder.translationKeys(comment,config,UtilsDocumentation.keyTranslationFile);
		DocumentationCommentBuilder.tableHeaders(comment,headerKeys);
		DocumentationCommentBuilder.tableKey(comment,keyCaptionPrefix,"Table Caption Prefix");
		OfxCommentBuilder.doNotModify(comment);
		table.setComment(comment);
		
		table.setContent(createContent(scope));
		
		return table;
	}
	
	private Specification createSpecifications()
	{
		Columns cols = new Columns();
		cols.getColumn().add(XmlColumnFactory.flex(20,true));
//		cols.getColumn().add(OfxColumnFactory.flex(20,true));
		cols.getColumn().add(XmlColumnFactory.flex(60));
			
		Specification specification = new Specification();
		specification.setColumns(cols);
		specification.setFloat(XmlFloatFactory.build(false));
		
		return specification;
	}
	
	private Content createContent(ConstraintScope scope) throws OfxAuthoringException
	{
		Head head = new Head();
		head.getRow().add(createHeaderRow(headerKeys));
		
		Body body = new Body();
		for(Constraint c : scope.getConstraint())
		{
			body.getRow().add(createRow(c));
		}
		
		Content content = new Content();
		content.getBody().add(body);
		content.setHead(head);
		
		return content;
	}
	
	private Row createRow(Constraint c) throws OfxAuthoringException
	{
		Row row = new Row();
		row.getCell().add(OfxMultiLangFactory.cell(langs, c.getType().getCode(), constraintTypes));
//		row.getCell().add(OfxCellFactory.createParagraphCell(""));
		row.getCell().add(OfxMultiLangFactory.cell(langs, c.getDescriptions()));
		return row;
	}	
}