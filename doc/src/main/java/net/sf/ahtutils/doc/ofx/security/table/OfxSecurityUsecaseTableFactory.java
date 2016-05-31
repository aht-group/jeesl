package net.sf.ahtutils.doc.ofx.security.table;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.configuration.Configuration;
import org.openfuxml.content.layout.Font;
import org.openfuxml.content.layout.Layout;
import org.openfuxml.content.table.Body;
import org.openfuxml.content.table.Columns;
import org.openfuxml.content.table.Content;
import org.openfuxml.content.table.Head;
import org.openfuxml.content.table.Row;
import org.openfuxml.content.table.Specification;
import org.openfuxml.content.table.Table;
import org.openfuxml.exception.OfxAuthoringException;
import org.openfuxml.factory.xml.layout.XmlFloatFactory;
import org.openfuxml.factory.xml.layout.XmlFontFactory;
import org.openfuxml.factory.xml.ofx.layout.XmlLayoutFactory;
import org.openfuxml.factory.xml.ofx.layout.XmlLineFactory;
import org.openfuxml.factory.xml.table.OfxCellFactory;
import org.openfuxml.factory.xml.table.OfxColumnFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.doc.ofx.AbstractUtilsOfxDocumentationFactory;
import net.sf.ahtutils.doc.ofx.util.OfxMultiLangFactory;
import net.sf.ahtutils.factory.xml.security.XmlActionFactory;
import net.sf.ahtutils.xml.security.Action;
import net.sf.ahtutils.xml.security.Usecase;
import net.sf.ahtutils.xml.security.View;
import net.sf.ahtutils.xml.status.Translations;
import net.sf.exlp.util.xml.JaxbUtil;

public class OfxSecurityUsecaseTableFactory extends AbstractUtilsOfxDocumentationFactory
{
	final static Logger logger = LoggerFactory.getLogger(OfxSecurityUsecaseTableFactory.class);
	
	private List<String> headerKeys;
	private Font font;
		
	public OfxSecurityUsecaseTableFactory(Configuration config, String[] langs, Translations translations)
	{
		super(config,langs,translations);
		
		headerKeys = new ArrayList<String>();
		headerKeys.add("auTableHeadSecurityUsecaseView");
		headerKeys.add("auTableHeadSecurityUsecaseAction");
		headerKeys.add("auTableHeadSecurityUsecaseDescription");
		
		font = XmlFontFactory.relative(-2);
	}
	
	public Table build(Usecase usecase) throws OfxAuthoringException
	{
		Table table = new Table();
//		table.setId("table.qa.nfr.questions."+section.getPosition());
		table.setSpecification(createSpecifications());
		
		table.setTitle(OfxMultiLangFactory.title(langs, usecase.getLangs(),"Views and Actions for ",null));
			
		table.setContent(createContent(usecase));
		
//		if(usecase.getCode().equals("programs.Management")){JaxbUtil.info(table);}
		
		return table;
	}
	
	private Specification createSpecifications()
	{
		Specification spec = new Specification();
		spec.setFloat(XmlFloatFactory.build(false));
		spec.setColumns(new Columns());
		spec.getColumns().getColumn().add(OfxColumnFactory.flex(15,true));
		spec.getColumns().getColumn().add(OfxColumnFactory.flex(30,true));
		spec.getColumns().getColumn().add(OfxColumnFactory.flex(55,false));
		
		return spec;
	}
	
	private Content createContent(Usecase usecase) throws OfxAuthoringException
	{
		Head head = new Head();
		head.getRow().add(createHeaderRow(headerKeys));
		
		Body body = new Body();
		body.setLayout(XmlLayoutFactory.build(font));
		if(usecase.isSetViews())
		{
			for(int i=0;i<usecase.getViews().getView().size();i++)
			{
				View view = usecase.getViews().getView().get(i);
				body.getRow().addAll(createRows(view,usecase,(i==0)));
			}
		}
		
		Content content = new Content();
		content.getBody().add(body);
		content.setHead(head);
		
		return content;
	}
	
	private List<Row> createRows(View view,Usecase uc, boolean firstView) throws OfxAuthoringException
	{
		logger.trace(view.getCode());
		List<Row> rows = new ArrayList<Row>();
		
		if(!uc.isSetActions() && uc.getActions().getAction().size()==0)
		{
			rows.add(viewOnly(view));
		}
		else
		{
			boolean firstAction = true;
			boolean matchedAction = false;
			for(Action action : uc.getActions().getAction())
			{
				if(action.getView().getCode().equals(view.getCode()))
				{
					JaxbUtil.trace(action);
					Row row = new Row();
					if(firstAction)
					{
						row.getCell().add(OfxMultiLangFactory.cell(langs, view.getLangs()));
						firstAction=false;
					}
					else
					{
						row.getCell().add(OfxCellFactory.createParagraphCell(""));
					}
					row.getCell().add(OfxMultiLangFactory.cell(langs, XmlActionFactory.toLangs(action)));
					row.getCell().add(OfxMultiLangFactory.cell(langs, XmlActionFactory.toDescriptions(action)));
					rows.add(row);
					matchedAction=true;
				}
			}
			if(!matchedAction){rows.add(viewOnly(view));}
		}
		
		if(!firstView)
		{
			Layout layout = XmlLayoutFactory.build();
			layout.getLine().add(XmlLineFactory.top());
			rows.get(0).setLayout(layout);
		}
		
		return rows;
	}
	
	private Row viewOnly(View view)
	{
		Row row = new Row();
		row.getCell().add(OfxMultiLangFactory.cell(langs, view.getLangs()));
		row.getCell().add(OfxCellFactory.createParagraphCell(""));
		row.getCell().add(OfxCellFactory.createParagraphCell(""));
		return row;
	}
}