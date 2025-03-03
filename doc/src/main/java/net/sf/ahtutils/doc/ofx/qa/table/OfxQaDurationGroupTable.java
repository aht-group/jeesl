package net.sf.ahtutils.doc.ofx.qa.table;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.exlp.interfaces.system.property.Configuration;
import org.exlp.util.jx.JaxbUtil;
import org.jeesl.doc.ofx.OfxMultiLangFactory;
import org.jeesl.exception.processing.UtilsConfigurationException;
import org.jeesl.factory.txt.module.calendar.TxtPeriodJodaFactory;
import org.jeesl.model.xml.io.locale.status.Translations;
import org.jeesl.model.xml.module.dev.qa.Category;
import org.jeesl.model.xml.module.dev.qa.Group;
import org.jeesl.model.xml.module.dev.qa.Groups;
import org.jeesl.model.xml.module.dev.qa.Test;
import org.jeesl.util.query.xpath.StatusXpath;
import org.openfuxml.exception.OfxAuthoringException;
import org.openfuxml.factory.xml.layout.XmlFloatFactory;
import org.openfuxml.factory.xml.ofx.content.XmlCommentFactory;
import org.openfuxml.factory.xml.table.XmlCellFactory;
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
import net.sf.exlp.exception.ExlpXpathNotFoundException;
import net.sf.exlp.exception.ExlpXpathNotUniqueException;

public class OfxQaDurationGroupTable extends AbstractUtilsOfxDocumentationFactory
{
	final static Logger logger = LoggerFactory.getLogger(OfxQaDurationGroupTable.class);
	private static String keyCaptionPrefix = "auTableQaDurationGroup";
	
	private TxtPeriodJodaFactory tfPeriod;
	private List<String> headerKeys;
	
	public OfxQaDurationGroupTable(Configuration config, String[] langs, Translations translations)
	{
		super(config,langs,translations);
		tfPeriod = new TxtPeriodJodaFactory();
		
		headerKeys = new ArrayList<String>();
		headerKeys.add("auTableQaGroup");
		headerKeys.add("auTableQaTestQuantity");
		headerKeys.add("auTableQaTestDuration");
	}
	
	public Table build(List<Category> categories,Groups groups) throws OfxAuthoringException, UtilsConfigurationException
	{
		try
		{	
			Table table = new Table();
			table.setId("table.qa.duration.group");
			
			Comment comment = XmlCommentFactory.build();
			OfxCommentBuilder.fixedId(comment, table.getId());
			DocumentationCommentBuilder.translationKeys(comment,config,UtilsDocumentation.keyTranslationFile);
			DocumentationCommentBuilder.tableHeaders(comment,headerKeys);
			DocumentationCommentBuilder.tableKey(comment,keyCaptionPrefix,"Table Caption Prefix");
			table.setComment(comment);
			
			table.setTitle(OfxMultiLangFactory.title(langs, StatusXpath.getTranslation(translations, keyCaptionPrefix).getLangs()));
			table.setSpecification(createSpecifications());
			table.setContent(createContent(categories,groups));
				
			return table;
		}
		catch (ExlpXpathNotFoundException e) {throw new OfxAuthoringException(e.getMessage());}
		catch (ExlpXpathNotUniqueException e) {throw new OfxAuthoringException(e.getMessage());}
	}
	
	private Specification createSpecifications()
	{
		Columns cols = new Columns();
		cols.getColumn().add(XmlColumnFactory.flex(45,false));
		cols.getColumn().add(XmlColumnFactory.flex(15,true));
		cols.getColumn().add(XmlColumnFactory.flex(40,true));
		
		Specification specification = new Specification();
		specification.setColumns(cols);
		specification.setFloat(XmlFloatFactory.build(false));
		
		return specification;
	}
	
	private Content createContent(List<Category> categories,Groups groups)
	{
		Head head = new Head();
		head.getRow().add(createHeaderRow(headerKeys));
		
		Body body = new Body();
		for(Group g : groups.getGroup())
		{
			body.getRow().add(createRow(categories,g));
		}
		
		Content content = new Content();
		content.getBody().add(body);
		content.setHead(head);
		
		return content;
	}
	
	private Row createRow(List<Category> categories,Group group)
	{
		Row row = new Row();
		JaxbUtil.trace(group);
		row.getCell().add(XmlCellFactory.createParagraphCell(group.getName()));		
		
		int[] total = getSumForGroup(categories,group);
		row.getCell().add(XmlCellFactory.createParagraphCell(""+total[0]));
		row.getCell().add(XmlCellFactory.createParagraphCell(""+tfPeriod.debug(total[1])));
		
		return row;
	}
	
	private int[] getSumForGroup(List<Category> categories,Group group)
	{
		int duration=0;
		int counter = 0;
		for(Category c : categories)
		{
			for(Test t : c.getTest())
			{
				if(t.isVisible() && Objects.nonNull(t.getGroups()))
				{
					for(Group g : t.getGroups().getGroup())
					{
						if(group.getId()==g.getId())
						{
							counter++;
							duration=duration+t.getDuration();
						}
					}
				}
			}
		}
		return new int[]{counter,duration};
	}
}