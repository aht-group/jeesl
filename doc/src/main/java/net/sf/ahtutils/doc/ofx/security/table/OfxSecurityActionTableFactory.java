package net.sf.ahtutils.doc.ofx.security.table;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.exlp.util.jx.JaxbUtil;
import org.jeesl.doc.ofx.OfxMultiLangFactory;
import org.jeesl.factory.xml.system.security.XmlActionFactory;
import org.jeesl.model.xml.io.locale.status.Translations;
import org.jeesl.model.xml.system.security.Action;
import org.jeesl.model.xml.system.security.Role;
import org.jeesl.model.xml.system.security.View;
import org.openfuxml.exception.OfxAuthoringException;
import org.openfuxml.factory.xml.layout.XmlFloatFactory;
import org.openfuxml.factory.xml.ofx.layout.XmlLayoutFactory;
import org.openfuxml.factory.xml.ofx.layout.XmlLineFactory;
import org.openfuxml.factory.xml.table.XmlCellFactory;
import org.openfuxml.factory.xml.table.XmlColumnFactory;
import org.openfuxml.model.xml.core.layout.Layout;
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

public class OfxSecurityActionTableFactory extends AbstractUtilsOfxDocumentationFactory
{
	final static Logger logger = LoggerFactory.getLogger(OfxSecurityActionTableFactory.class);
	
	private List<String> headerKeys;
		
	public OfxSecurityActionTableFactory(org.exlp.interfaces.system.property.Configuration config, String[] langs, Translations translations)
	{
		super(config,langs,translations);
		
		headerKeys = new ArrayList<String>();
		headerKeys.add("auTableHeadSecurityUsecaseName");
		headerKeys.add("auTableHeadSecurityUsecaseDescription");
	}
	
	public Table build(View view) throws OfxAuthoringException
	{
		Table table = new Table();
//		table.setId("table.qa.nfr.questions."+section.getPosition());
		table.setSpecification(createSpecifications(view));
		
		table.setTitle(OfxMultiLangFactory.title(langs, view.getLangs(),"Access rights for page:",null));
			
		table.setContent(createContent(view));
		
		return table;
	}
	
	private Specification createSpecifications(View view)
	{
		int roles=0;
		if(Objects.nonNull(view.getRoles())) {roles = view.getRoles().getRole().size();}
		
		Specification spec = new Specification();
		spec.setFloat(XmlFloatFactory.build(false));
		spec.setColumns(new Columns());
		spec.getColumns().getColumn().add(XmlColumnFactory.flex(30,true));
		spec.getColumns().getColumn().add(XmlColumnFactory.flex((100-30-(3*roles)),false));
		for(int i=1;i<=roles;i++)
		{
			spec.getColumns().getColumn().add(XmlColumnFactory.flex(3,false));
		}
		
		return spec;
	}
	
	private Content createContent(View view) throws OfxAuthoringException
	{
		Head head = new Head();
		head.getRow().add(createHeaderRow(headerKeys));
		if(Objects.nonNull(view.getRoles()))
		{
			for(int i=1;i<=view.getRoles().getRole().size();i++)
			{
				head.getRow().get(0).getCell().add(XmlCellFactory.createParagraphCell(i));
			}
		}
		
		Body body = new Body();
		body.getRow().add(page(view));
		for(Action uc : view.getActions().getAction())
		{
			body.getRow().add(createRow(view,uc));
		}
		
		Content content = new Content();
		content.getBody().add(body);
		content.setHead(head);
		
		return content;
	}
	
	@SuppressWarnings("unused")
	private Row page(View view) throws OfxAuthoringException
	{
		Row row = new Row();
		
		Layout layout = XmlLayoutFactory.build();
		layout.getLine().add(XmlLineFactory.bottom());
		row.setLayout(layout);
		
		row.getCell().add(XmlCellFactory.createParagraphCell("Page"));
		row.getCell().add(XmlCellFactory.createParagraphCell("User is allowed to access the page."));
		
		if(Objects.nonNull(view.getRoles()))
		{
			for(Role role : view.getRoles().getRole())
			{
				row.getCell().add(XmlCellFactory.createParagraphCell("X"));
			}
		}
		return row;
	}
	
	private Row createRow(View view, Action action) throws OfxAuthoringException
	{
		Row row = new Row();
		JaxbUtil.trace(action);
		row.getCell().add(OfxMultiLangFactory.cell(langs, XmlActionFactory.toLangs(action)));
		row.getCell().add(OfxMultiLangFactory.cell(langs, XmlActionFactory.toDescriptions(action)));
		
		if(Objects.nonNull(view.getRoles()))
		{
			for(Role role : view.getRoles().getRole())
			{
				boolean active = false;
				if(Objects.nonNull(action.getRoles()))
				{
					for(Role r : action.getRoles().getRole())
					{
						if(role.getCode().equals(r.getCode())){active=true;}
					}
				}
				
				if(active){row.getCell().add(XmlCellFactory.createParagraphCell("X"));}
				else {row.getCell().add(XmlCellFactory.createParagraphCell(""));}
			}
		}
		
		return row;
	}
}