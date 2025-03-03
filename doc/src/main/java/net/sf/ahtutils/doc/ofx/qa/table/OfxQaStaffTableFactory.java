package net.sf.ahtutils.doc.ofx.qa.table;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;
import org.jeesl.doc.ofx.OfxMultiLangFactory;
import org.jeesl.exception.processing.UtilsConfigurationException;
import org.jeesl.model.xml.io.locale.status.Translations;
import org.jeesl.model.xml.module.dev.qa.Group;
import org.jeesl.model.xml.system.security.Staff;
import org.jeesl.util.query.xpath.StatusXpath;
import org.openfuxml.exception.OfxAuthoringException;
import org.openfuxml.factory.xml.layout.XmlAlignmentFactory;
import org.openfuxml.factory.xml.layout.XmlFloatFactory;
import org.openfuxml.factory.xml.ofx.content.XmlCommentFactory;
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
import net.sf.exlp.exception.ExlpXpathNotFoundException;
import net.sf.exlp.exception.ExlpXpathNotUniqueException;

public class OfxQaStaffTableFactory extends AbstractUtilsOfxDocumentationFactory
{
	final static Logger logger = LoggerFactory.getLogger(OfxQaStaffTableFactory.class);
	
	private static String keyCaptionTeam  = "auTableQmTeamCaption";
	private static String keyCaptionGroup = "auTableQmGroupCaption";
	
	public static enum ColumCode{organisation,department,organisationDepartment,name,role,responsibility}
		
	public OfxQaStaffTableFactory(org.exlp.interfaces.system.property.Configuration config, String lang, Translations translations)
	{
		this(config,new String[] {lang},translations);
	}
	public OfxQaStaffTableFactory(org.exlp.interfaces.system.property.Configuration config, String[] langs, Translations translations)
	{
		super(config,langs,translations);
	}
	
	public Table team(List<Staff> staffs) throws OfxAuthoringException, UtilsConfigurationException
	{
		try
		{
			Table table = new Table();
			table.setSpecification(createSpecifications());
			table.setId("table.qa.team");
			table.setTitle(OfxMultiLangFactory.title(langs,StatusXpath.getTranslation(translations, keyCaptionTeam).getLangs()));
			
			Comment comment = XmlCommentFactory.build();
			OfxCommentBuilder.fixedId(comment, table.getId());
			DocumentationCommentBuilder.translationKeys(comment,config,UtilsDocumentation.keyTranslationFile);
			DocumentationCommentBuilder.tableHeaders(comment,buildHeaderKeys());
			DocumentationCommentBuilder.tableKey(comment,keyCaptionTeam,"Table Caption Prefix");
			OfxCommentBuilder.doNotModify(comment);
			table.setComment(comment);
			
			table.setContent(createContent(staffs));
			
			return table;
		}
		catch (ExlpXpathNotFoundException e) {throw new OfxAuthoringException(e.getMessage());}
		catch (ExlpXpathNotUniqueException e) {throw new OfxAuthoringException(e.getMessage());}
	}
	
	public Table group(Group group) throws OfxAuthoringException, UtilsConfigurationException
	{
		try
		{
			Table table = new Table();
			table.setSpecification(createSpecifications());
			table.setId("table.qa.group."+group.getId());
			table.setTitle(OfxMultiLangFactory.title(langs,StatusXpath.getTranslation(translations, keyCaptionGroup).getLangs(),null,": "+group.getName()));
			
			Comment comment = XmlCommentFactory.build();
			OfxCommentBuilder.fixedId(comment, table.getId());
			DocumentationCommentBuilder.translationKeys(comment,config,UtilsDocumentation.keyTranslationFile);
			DocumentationCommentBuilder.tableHeaders(comment,buildHeaderKeys());
			DocumentationCommentBuilder.tableKey(comment,keyCaptionGroup,"Table Caption Prefix");
			table.setComment(comment);
			
			table.setContent(createContent(group.getStaff()));
			
			return table;
		}
		catch (ExlpXpathNotFoundException e) {throw new OfxAuthoringException(e.getMessage());}
		catch (ExlpXpathNotUniqueException e) {throw new OfxAuthoringException(e.getMessage());}
	}
	
	private Specification createSpecifications()
	{
		Columns cols = new Columns();
		if(this.columns.length==2)
		{
			XmlColumnFactory.add(cols,XmlAlignmentFactory.Horizontal.left);
			cols.getColumn().add(XmlColumnFactory.flex(60));
		}
		else if(this.columns.length==3)
		{
			XmlColumnFactory.add(cols,XmlAlignmentFactory.Horizontal.left);
			cols.getColumn().add(XmlColumnFactory.flex(35));
			cols.getColumn().add(XmlColumnFactory.flex(40));
		}
		else if(this.columns.length==4)
		{
			
			cols.getColumn().add(XmlColumnFactory.flex(30));
			cols.getColumn().add(XmlColumnFactory.flex(15));
			cols.getColumn().add(XmlColumnFactory.flex(35));
			cols.getColumn().add(XmlColumnFactory.flex(20));
		}
		else {logger.warn("Columns "+columns+" NYI");}
			
		
		Specification specification = new Specification();
		specification.setColumns(cols);
		specification.setFloat(XmlFloatFactory.build(false));
		
		return specification;
	}
	
	private Content createContent(List<Staff> lStaff)
	{
		Body body = new Body();
		Head head = new Head();
		
		
		head.getRow().add(createHeaderRow(buildHeaderKeys()));
		
		for(Staff staff : lStaff)
		{
			Row row = new Row();
			for(ColumCode col : columns)
			{
				row.getCell().add(build(col,staff));
			}
			body.getRow().add(row);
		}
		
		Content content = new Content();
		content.getBody().add(body);
		content.setHead(head);
		
		return content;
	}
	
	private Cell build(ColumCode column, Staff staff)
	{
		switch(column)
		{
			case organisation: return XmlCellFactory.createParagraphCell(staff.getStatus().getLabel());
			case department: if(Objects.nonNull(staff.getType())) {return XmlCellFactory.createParagraphCell(staff.getType().getLabel());}
			case organisationDepartment: StringBuffer sb = new StringBuffer();
											if(Objects.nonNull(staff.getStatus())) {sb.append(staff.getStatus().getLabel());}
											if(ObjectUtils.allNotNull(staff.getType(),staff.getStatus())){sb.append(", ");}
											if(Objects.nonNull(staff.getType())) {sb.append(staff.getType().getLabel());}
											return XmlCellFactory.createParagraphCell(sb.toString());
			case role: return OfxMultiLangFactory.cell(langs, staff.getRole().getLangs());
			case responsibility: return cellResponsibility(staff);
			case name: return XmlCellFactory.createParagraphCell(staff.getUser().getFirstName()+" "+staff.getUser().getLastName());
		}
		return XmlCellFactory.createParagraphCell("");
	}
	
	private String header(ColumCode column)
	{
		switch(column)
		{
			case organisationDepartment: return "auTableQaOrganisation";
			case organisation: return "auTableQaOrganisation";
			case department: return "auTableQaDepartment";
			case role: return "auTableQaRole";
			case name: return "auTableQaName";
			case responsibility: return "auTableQaResponsibilities";
			default: return "";
		}
	}
	
	private List<String> buildHeaderKeys()
	{
		List<String> headers = new ArrayList<String>();
		for(ColumCode col : columns)
		{
			headers.add(header(col));
		}
		return headers;
	}
	
	private ColumCode[] columns;
	public void setColumns(ColumCode... columns) {this.columns = columns;}
	
	private Cell cellResponsibility(Staff staff)
	{
		if(Objects.nonNull(staff.getResponsible()) && Objects.nonNull(staff.getResponsible().getLabel()))
		{
			return XmlCellFactory.createParagraphCell(staff.getResponsible().getLabel());
		}
		else
		{
			return XmlCellFactory.createParagraphCell("");
		}
	}
}