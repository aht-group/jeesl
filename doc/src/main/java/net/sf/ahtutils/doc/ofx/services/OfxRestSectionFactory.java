package net.sf.ahtutils.doc.ofx.services;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.exlp.interfaces.system.property.Configuration;
import org.jeesl.interfaces.model.marker.qualifier.RestDescription;
import org.jeesl.model.xml.io.locale.status.Translations;
import org.openfuxml.exception.OfxAuthoringException;
import org.openfuxml.factory.xml.list.XmlListFactory2;
import org.openfuxml.factory.xml.list.XmlListFactory2.Ordering;
import org.openfuxml.factory.xml.list.XmlListItemFactory;
import org.openfuxml.factory.xml.ofx.OfxReferenceFactory;
import org.openfuxml.factory.xml.ofx.content.XmlCommentFactory;
import org.openfuxml.factory.xml.ofx.content.structure.XmlParagraphFactory;
import org.openfuxml.factory.xml.ofx.content.structure.XmlSectionFactory;
import org.openfuxml.factory.xml.ofx.content.text.XmlTitleFactory;
import org.openfuxml.factory.xml.table.XmlCellFactory;
import org.openfuxml.factory.xml.table.XmlColumnFactory;
import org.openfuxml.factory.xml.text.OfxEmphasisFactory;
import org.openfuxml.model.xml.core.ofx.Comment;
import org.openfuxml.model.xml.core.ofx.Listing;
import org.openfuxml.model.xml.core.ofx.Paragraph;
import org.openfuxml.model.xml.core.ofx.Section;
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

import net.sf.ahtutils.doc.ofx.AbstractUtilsOfxDocumentationFactory;

public class OfxRestSectionFactory extends AbstractUtilsOfxDocumentationFactory
{
	final static Logger logger = LoggerFactory.getLogger(OfxRestSectionFactory.class);

	private String listingDir;
	
	public OfxRestSectionFactory(Configuration config, String[] langs, Translations translations,String listingDir)
	{
		super(config,langs,translations);
		this.listingDir=listingDir;
	}
	
	public Section build(Class<?> c) throws OfxAuthoringException
	{
		Annotation a = c.getAnnotation(RestDescription.class);
		Annotation aPath = c.getAnnotation(Path.class);
		if(a==null) {throw new OfxAuthoringException("Not a "+RestDescription.class.getSimpleName());}
		if(aPath==null) {throw new OfxAuthoringException("Not a "+Path.class.getSimpleName());}
		
		RestDescription rest = (RestDescription)a;
		Path path = (Path)aPath;
		
		Section section = XmlSectionFactory.build();
		Comment comment = XmlCommentFactory.build();
		OfxCommentBuilder.doNotModify(comment);
		section.getContent().add(comment);
		
		section.getContent().add(XmlTitleFactory.build(rest.label()));
		section.getContent().add(XmlParagraphFactory.text(rest.description()));
		
		Paragraph p = XmlParagraphFactory.build();
		p.getContent().add("The service is using the context path ");
		p.getContent().add(OfxEmphasisFactory.typewriter(path.value()));
		p.getContent().add(" and offering the following methods:");
		section.getContent().add(p);
		
		org.openfuxml.model.xml.core.list.List list = XmlListFactory2.build(Ordering.unordered);

        List<Section> sections = new ArrayList<Section>();
        int i = 0;
        while ( i < c.getDeclaredMethods().length)
        {
            list.getItem().add(null);
            sections.add(null);
            i++;
        }
        for(Method method : c.getDeclaredMethods())
        {
            for(Annotation methodA : method.getDeclaredAnnotations())
            {
                if(methodA instanceof RestDescription)
                {
                    RestDescription r = (RestDescription)methodA;
                    if(r.orderNr() != 0)
                    {
                        int index = r.orderNr()-1;
                        list.getItem().add(index ,XmlListItemFactory.build(r.label()));
                        sections.add(index, build(c,method));
                    }
                    else
                    {
                        list.getItem().add(XmlListItemFactory.build(r.label()));
                        sections.add(build(c,method));
                    }
                }
            }
        }
        System.out.println(list.getItem().size());
        for(int j = 0; j < list.getItem().size(); j++)
        {
            if (list.getItem().get(j) == null)
            {
                list.getItem().remove(j);
                j--;
            }
        }
        System.out.println(list.getItem().size());

        for(int j = 0; j < section.getContent().size(); j++)
        {
            if (section.getContent().get(j) == null)
            {
                list.getItem().remove(j);
                j--;
            }
        }

        section.getContent().add(list);
        section.getContent().addAll(sections);

        return section;
	}
	
	private Section build(Class<?> c, Method method)
	{
		RestDescription rest=null;
		for(Annotation methodA : method.getDeclaredAnnotations())
		{
			if(methodA instanceof RestDescription){rest = (RestDescription)methodA;}
		}
		
		StringBuffer sbId = new StringBuffer();
		sbId.append(listingDir.replace("/", "."));
		sbId.append(".").append(c.getSimpleName().toLowerCase());
		sbId.append(".").append(method.getName().toLowerCase());
	
		Section section = XmlSectionFactory.build();
		section.getContent().add(XmlTitleFactory.build("Service: " + rest.label()));
		section.getContent().add(XmlParagraphFactory.text(rest.description()));
		
		section.getContent().add(table(method,sbId.toString(),rest.label()));
		
		Paragraph p = XmlParagraphFactory.build();
		p.getContent().add("The specifications for the method call are listed in Table ");
		p.getContent().add(OfxReferenceFactory.build(sbId.toString()));
		
		if(!rest.withListing()){p.getContent().add(".");}
		else{p.getContent().add(" and an example of the response is shown in the following listing.");}
		section.getContent().add(p);
		
		if(rest.withListing())
		{
			Listing listing = new Listing();
			listing.setExternal(listingDir+"/"+c.getSimpleName()+"/"+method.getName()+".xml");
			section.getContent().add(listing);
		}
		return section;
	}
	
	private Table table(Method method, String tableId, String methodLabel)
	{
		Table table = new Table();
		table.setId(tableId);
		
		table.setTitle(XmlTitleFactory.build("Specification of Method: "+methodLabel));
		
		table.setSpecification(createSpecifications());
		
		Content content = new Content();
		content.setHead(buildHead());
		Body body = new Body();
				
		for(Annotation a : method.getDeclaredAnnotations())
		{
			if     (a instanceof GET){body.getRow().add(httpMethod(GET.class.getSimpleName()));}
			else if(a instanceof POST){body.getRow().add(httpMethod(POST.class.getSimpleName()));}
			else if(a instanceof Path){body.getRow().add(path((Path)a));}
			else if(a instanceof Consumes){body.getRow().add(consumes((Consumes)a));}
			else if(a instanceof Produces){body.getRow().add(produces((Produces)a));}
			
		}
		content.getBody().add(body);
		table.setContent(content);
		return table;
	}
	
	private Specification createSpecifications()
	{
		Columns cols = new Columns();	
		cols.getColumn().add(XmlColumnFactory.flex(25));
		cols.getColumn().add(XmlColumnFactory.flex(75));
		Specification specification = new Specification();
		specification.setColumns(cols);
		return specification;
	}
	
	private Head buildHead()
	{
		Row row = new Row();
		row.getCell().add(XmlCellFactory.createParagraphCell("Type"));
		row.getCell().add(XmlCellFactory.createParagraphCell("Description"));
		
		Head head = new Head();
		head.getRow().add(row);
		return head;
	}
	
	
	private Row httpMethod(String value)
	{		
		Row row = new Row();
		row.getCell().add(XmlCellFactory.createParagraphCell("HTTP Method"));
		row.getCell().add(XmlCellFactory.createParagraphCell(value));
		return row;
	}
	
	private Row path(Path value)
	{		
		Row row = new Row();
		row.getCell().add(XmlCellFactory.createParagraphCell("Path"));
		
		Paragraph p = XmlParagraphFactory.build();
		p.getContent().add(OfxEmphasisFactory.typewriter(value.value()));
		Cell cell = XmlCellFactory.build();
		cell.getContent().add(p);
		row.getCell().add(cell);
		
		return row;
	}
	
	private Row consumes(Consumes value)
	{		
		Row row = new Row();
		row.getCell().add(XmlCellFactory.createParagraphCell("Consumes"));
		row.getCell().add(XmlCellFactory.createParagraphCell(value.value()[0]));
		return row;
	}
	
	private Row produces(Produces value)
	{		
		Row row = new Row();
		row.getCell().add(XmlCellFactory.createParagraphCell("Produces"));
		row.getCell().add(XmlCellFactory.createParagraphCell(value.value()[0]));
		return row;
	}
}