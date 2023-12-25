package org.jeesl.doc.ofx.cms.module.workflow;

import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;
import org.jeesl.doc.ofx.cms.generic.AbstractJeeslOfxTableFactory;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleManager;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.model.xml.module.workflow.Permission;
import org.jeesl.model.xml.module.workflow.Permissions;
import org.jeesl.model.xml.module.workflow.Stage;
import org.jeesl.model.xml.module.workflow.Transition;
import org.jeesl.util.query.xpath.StatusXpath;
import org.openfuxml.factory.xml.layout.XmlFloatFactory;
import org.openfuxml.factory.xml.list.XmlListFactory;
import org.openfuxml.factory.xml.list.XmlListItemFactory;
import org.openfuxml.factory.xml.ofx.content.XmlCommentFactory;
import org.openfuxml.factory.xml.ofx.content.text.XmlTitleFactory;
import org.openfuxml.factory.xml.table.XmlBodyFactory;
import org.openfuxml.factory.xml.table.XmlCellFactory;
import org.openfuxml.factory.xml.table.XmlColumnFactory;
import org.openfuxml.factory.xml.table.XmlColumnsFactory;
import org.openfuxml.factory.xml.table.XmlContentFactory;
import org.openfuxml.factory.xml.table.XmlRowFactory;
import org.openfuxml.interfaces.configuration.OfxTranslationProvider;
import org.openfuxml.model.xml.core.ofx.Comment;
import org.openfuxml.model.xml.core.ofx.Paragraph;
import org.openfuxml.model.xml.core.table.Body;
import org.openfuxml.model.xml.core.table.Columns;
import org.openfuxml.model.xml.core.table.Content;
import org.openfuxml.model.xml.core.table.Row;
import org.openfuxml.model.xml.core.table.Specification;
import org.openfuxml.model.xml.core.table.Table;
import org.openfuxml.util.OfxCommentBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.exception.ExlpXpathNotFoundException;
import net.sf.exlp.exception.ExlpXpathNotUniqueException;

public class OfxTableWorkflowProcess <L extends JeeslLang, LOC extends JeeslLocale<L,?,LOC,?>> extends AbstractJeeslOfxTableFactory<L,LOC>
{
	final static Logger logger = LoggerFactory.getLogger(OfxTableWorkflowProcess.class);

	public OfxTableWorkflowProcess(OfxTranslationProvider tp)
	{
		super(tp);
	}
	
	public Table build(JeeslLocaleManager<LOC> lp, org.jeesl.model.xml.module.workflow.Process process)
	{
		tableHeaders.clear();
		super.addHeader("Stage");
		super.addHeader("Transition");
		super.addHeader("Role");

		Table table = toOfx(lp,process);
		table.setId("table.srs.implementation.");
		try
		{
			table.setTitle(XmlTitleFactory.build("Workflow: "+StatusXpath.getLang(process.getLangs(),lp.getPrimaryLocaleCode()).getTranslation()));
		}
		catch (ExlpXpathNotFoundException | ExlpXpathNotUniqueException e) {e.printStackTrace();}
		
		Comment comment = XmlCommentFactory.build();
		OfxCommentBuilder.fixedId(comment, table.getId());
		OfxCommentBuilder.doNotModify(comment);
		table.setComment(comment);
		
		return table;
	}
	
	private Table toOfx(JeeslLocaleManager<LOC> lp, org.jeesl.model.xml.module.workflow.Process process)
	{
		Table table = new Table();
		table.setSpecification(createSpecifications());
		table.setContent(createContent(lp,process));
		return table;
	}
	
	private Specification createSpecifications()
	{
		Columns cols = XmlColumnsFactory.build();
		cols.getColumn().add(XmlColumnFactory.flex(35));
		cols.getColumn().add(XmlColumnFactory.flex(40));
		cols.getColumn().add(XmlColumnFactory.flex(30));
			
		Specification specification = new Specification();
		specification.setLong(true);
		specification.setColumns(cols);
		specification.setFloat(XmlFloatFactory.build(false));
		
		return specification;
	}
	
	private Content createContent(JeeslLocaleManager<LOC> lp, org.jeesl.model.xml.module.workflow.Process process)
	{
		Body body = XmlBodyFactory.build();
		
		for(Stage stage : process.getStage())
		{
			body.getRow().add(createRow(lp,process,stage));
		}
		
		return XmlContentFactory.build(super.buildTableHeader(lp),body);
	}
	
	private Row createRow(JeeslLocaleManager<LOC> lp, org.jeesl.model.xml.module.workflow.Process process, Stage stage)
	{		
		Row row = XmlRowFactory.build();
		row.getCell().add(ofxMultiLocale.cell(lp, stage.getLangs()));
		
		if(ObjectUtils.isNotEmpty(stage.getTransition())) {row.getCell().add(XmlCellFactory.list(transitions(lp,process,stage.getTransition())));}
		else {row.getCell().add(XmlCellFactory.createParagraphCell(""));}
		
		if(Objects.nonNull(stage.getPermissions()) && ObjectUtils.isNotEmpty(stage.getPermissions().getPermission())) {row.getCell().add(XmlCellFactory.list(permissions(lp,stage.getPermissions())));}
		else {row.getCell().add(XmlCellFactory.createParagraphCell(""));}
		
		return row;
	}
	
	private org.openfuxml.model.xml.core.list.List permissions(JeeslLocaleManager<LOC> lp, Permissions permissions)
	{
		org.openfuxml.model.xml.core.list.List list = XmlListFactory.unordered();
		for(Permission permission : permissions.getPermission())
		{
			for(String localeCode : lp.getLocaleCodes())
			{
				try
				{
					Paragraph p = new Paragraph();
					p.getContent().add(StatusXpath.getLang(permission.getRole().getLangs(),localeCode).getTranslation());
					list.getItem().add(XmlListItemFactory.build(localeCode,p));
				}
				catch (ExlpXpathNotFoundException | ExlpXpathNotUniqueException e) {e.printStackTrace();}
			}
		}
		return list;
	}
	
	private org.openfuxml.model.xml.core.list.List transitions(JeeslLocaleManager<LOC> lp, org.jeesl.model.xml.module.workflow.Process process, List<Transition> transitions)
	{
		org.openfuxml.model.xml.core.list.List list = XmlListFactory.unordered();
		for(Transition t : transitions)
		{
			for(String localeCode : lp.getLocaleCodes())
			{
				try
				{
					Paragraph p = new Paragraph();
					p.getContent().add(StatusXpath.getLang(t.getLangs(),localeCode).getTranslation());
					
//					Stage destination = WorkflowXpath.toStage(process, t.getStage().getId());
//					p.getContent().add(" (");
//					p.getContent().add(OfxEmphasisFactory.bold(StatusXpath.getLang(destination.getLangs(),localeCode).getTranslation()));
//					p.getContent().add(")");
					
					list.getItem().add(XmlListItemFactory.build(localeCode,p));
				}
				catch (ExlpXpathNotFoundException | ExlpXpathNotUniqueException e) {e.printStackTrace();}
			}
		}
		return list;
	}
}