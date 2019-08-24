package net.sf.ahtutils.doc.ofx.admin.er;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.configuration.Configuration;
import org.openfuxml.content.table.Body;
import org.openfuxml.content.table.Columns;
import org.openfuxml.content.table.Content;
import org.openfuxml.content.table.Head;
import org.openfuxml.content.table.Row;
import org.openfuxml.content.table.Specification;
import org.openfuxml.content.table.Table;
import org.openfuxml.factory.xml.ofx.content.text.XmlTitleFactory;
import org.openfuxml.factory.xml.table.XmlCellFactory;
import org.openfuxml.factory.xml.table.XmlColumnFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.doc.ofx.AbstractUtilsOfxDocumentationFactory;
import net.sf.ahtutils.interfaces.qualifier.EjbErAttribute;
import net.sf.ahtutils.interfaces.qualifier.EjbErAttributes;
import net.sf.ahtutils.model.qualifier.EjbErNode;
import net.sf.ahtutils.xml.status.Translations;

public class OfxClassAttributesTableFactory extends AbstractUtilsOfxDocumentationFactory
{
	final static Logger logger = LoggerFactory.getLogger(OfxClassAttributesTableFactory.class);
	
	public OfxClassAttributesTableFactory(Configuration config, String[] langs, Translations translations)
	{
		super(config,langs,translations);
	}
	
	@SuppressWarnings("unused")
	public Table table(Class<?> c)
	{
		Annotation aClass = c.getAnnotation(EjbErAttributes.class);
		Annotation aNode = c.getAnnotation(EjbErNode.class);
		
		Table table = new Table();
		table.setId(c.getName());
		
		table.setTitle(XmlTitleFactory.build("Specification of Method: "+c.getName()));
		
		table.setSpecification(createSpecifications());
		
		Content content = new Content();
		content.setHead(buildHead());
		Body body = new Body();
			
		List<Field> fields = new ArrayList<Field>();
		fields.addAll(Arrays.asList(c.getDeclaredFields()));
		
		for(Field field : fields)
		{
			
			Annotation aAtt = field.getAnnotation(EjbErAttribute.class);
			if(aAtt!=null)
			{
//				logger.info("Field "+field.getName()+" "+field.getType().getSimpleName());
				body.getRow().add(httpMethod(field));
			}
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
		row.getCell().add(XmlCellFactory.createParagraphCell("Attribute"));
		row.getCell().add(XmlCellFactory.createParagraphCell("Type"));
		
		Head head = new Head();
		head.getRow().add(row);
		return head;
	}
	
	
	private Row httpMethod(Field field)
	{		
		Row row = new Row();
		row.getCell().add(XmlCellFactory.createParagraphCell(field.getName()));
		row.getCell().add(XmlCellFactory.createParagraphCell(field.getType().getSimpleName()));
		return row;
	}
}