package org.jeesl.doc.ofx.cms.module.workflow;

import java.util.List;

import org.jeesl.doc.ofx.cms.generic.AbstractJeeslOfxTableFactory;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.locale.JeeslLocaleProvider;
import org.jeesl.model.xml.module.workflow.Permission;
import org.jeesl.model.xml.module.workflow.Permissions;
import org.jeesl.model.xml.module.workflow.Stage;
import org.jeesl.model.xml.module.workflow.Transition;
import org.jeesl.util.query.xpath.StatusXpath;
import org.jeesl.util.query.xpath.WorkflowXpath;
import org.openfuxml.content.ofx.Comment;
import org.openfuxml.content.ofx.Paragraph;
import org.openfuxml.content.table.Body;
import org.openfuxml.content.table.Columns;
import org.openfuxml.content.table.Content;
import org.openfuxml.content.table.Row;
import org.openfuxml.content.table.Specification;
import org.openfuxml.content.table.Table;
import org.openfuxml.factory.xml.layout.XmlFloatFactory;
import org.openfuxml.factory.xml.list.OfxListItemFactory;
import org.openfuxml.factory.xml.list.XmlListFactory;
import org.openfuxml.factory.xml.ofx.content.XmlCommentFactory;
import org.openfuxml.factory.xml.ofx.content.text.XmlTitleFactory;
import org.openfuxml.factory.xml.table.XmlBodyFactory;
import org.openfuxml.factory.xml.table.XmlCellFactory;
import org.openfuxml.factory.xml.table.XmlColumnFactory;
import org.openfuxml.factory.xml.table.XmlColumnsFactory;
import org.openfuxml.factory.xml.table.XmlContentFactory;
import org.openfuxml.factory.xml.table.XmlRowFactory;
import org.openfuxml.factory.xml.text.OfxEmphasisFactory;
import org.openfuxml.interfaces.configuration.OfxTranslationProvider;
import org.openfuxml.util.OfxCommentBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.interfaces.model.status.UtilsLang;
import net.sf.exlp.exception.ExlpXpathNotFoundException;
import net.sf.exlp.exception.ExlpXpathNotUniqueException;

public class OfxTableWorkflowProcess <L extends UtilsLang, LOC extends JeeslLocale<L,?,LOC,?>> extends AbstractJeeslOfxTableFactory<L,LOC>
{
	final static Logger logger = LoggerFactory.getLogger(OfxTableWorkflowProcess.class);

	public OfxTableWorkflowProcess(OfxTranslationProvider tp)
	{
		super(tp);
	}
	
	public Table build(JeeslLocaleProvider<LOC> lp, org.jeesl.model.xml.module.workflow.Process process)
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
	
	private Table toOfx(JeeslLocaleProvider<LOC> lp, org.jeesl.model.xml.module.workflow.Process process)
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
	
	private Content createContent(JeeslLocaleProvider<LOC> lp, org.jeesl.model.xml.module.workflow.Process process)
	{
		Body body = XmlBodyFactory.build();
		
		for(Stage stage : process.getStage())
		{
			body.getRow().add(createRow(lp,process,stage));
		}
		
		return XmlContentFactory.build(super.buildTableHeader(lp),body);
	}
	
	private Row createRow(JeeslLocaleProvider<LOC> lp, org.jeesl.model.xml.module.workflow.Process process, Stage stage)
	{		
		Row row = XmlRowFactory.build();
		row.getCell().add(ofxMultiLocale.cell(lp, stage.getLangs()));
		
		if(stage.isSetTransition()) {row.getCell().add(XmlCellFactory.list(transitions(lp,process,stage.getTransition())));}
		else {row.getCell().add(XmlCellFactory.createParagraphCell(""));}
		
		if(stage.isSetPermissions() && stage.getPermissions().isSetPermission()) {row.getCell().add(XmlCellFactory.list(permissions(lp,stage.getPermissions())));}
		else {row.getCell().add(XmlCellFactory.createParagraphCell(""));}
		
		return row;
	}
	
	private org.openfuxml.content.list.List permissions(JeeslLocaleProvider<LOC> lp, Permissions permissions)
	{
		org.openfuxml.content.list.List list = XmlListFactory.unordered();
		for(Permission permission : permissions.getPermission())
		{
			for(String localeCode : lp.getLocaleCodes())
			{
				try
				{
					Paragraph p = new Paragraph();
					p.getContent().add(StatusXpath.getLang(permission.getRole().getLangs(),localeCode).getTranslation());
					list.getItem().add(OfxListItemFactory.build(localeCode,p));
				}
				catch (ExlpXpathNotFoundException | ExlpXpathNotUniqueException e) {e.printStackTrace();}
			}
		}
		return list;
	}
	
	private org.openfuxml.content.list.List transitions(JeeslLocaleProvider<LOC> lp, org.jeesl.model.xml.module.workflow.Process process, List<Transition> transitions)
	{
		org.openfuxml.content.list.List list = XmlListFactory.unordered();
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
					
					list.getItem().add(OfxListItemFactory.build(localeCode,p));
				}
				catch (ExlpXpathNotFoundException | ExlpXpathNotUniqueException e) {e.printStackTrace();}
			}
		}
		return list;
	}
}