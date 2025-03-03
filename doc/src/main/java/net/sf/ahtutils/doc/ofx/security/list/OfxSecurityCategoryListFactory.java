package net.sf.ahtutils.doc.ofx.security.list;

import java.io.IOException;
import java.io.StringWriter;

import org.jeesl.doc.ofx.OfxMultiLangFactory;
import org.jeesl.model.xml.io.locale.status.Description;
import org.jeesl.model.xml.io.locale.status.Lang;
import org.jeesl.model.xml.io.locale.status.Translations;
import org.jeesl.model.xml.system.security.Category;
import org.jeesl.util.query.xpath.StatusXpath;
import org.openfuxml.exception.OfxAuthoringException;
import org.openfuxml.factory.xml.list.XmlListFactory;
import org.openfuxml.factory.xml.ofx.content.XmlCommentFactory;
import org.openfuxml.interfaces.configuration.ConfigurationProvider;
import org.openfuxml.model.xml.core.list.Item;
import org.openfuxml.model.xml.core.list.List;
import org.openfuxml.model.xml.core.ofx.Comment;
import org.openfuxml.model.xml.core.ofx.Paragraph;
import org.openfuxml.renderer.latex.content.list.LatexListRenderer;
import org.openfuxml.renderer.latex.content.structure.LatexSectionRenderer;
import org.openfuxml.util.OfxCommentBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.doc.ofx.AbstractUtilsOfxDocumentationFactory;
import net.sf.exlp.exception.ExlpXpathNotFoundException;
import net.sf.exlp.exception.ExlpXpathNotUniqueException;

public class OfxSecurityCategoryListFactory extends AbstractUtilsOfxDocumentationFactory
{
	final static Logger logger = LoggerFactory.getLogger(OfxSecurityCategoryListFactory.class);
	
	private ConfigurationProvider cp;
	
	public OfxSecurityCategoryListFactory(org.exlp.interfaces.system.property.Configuration config, String lang, Translations translations,ConfigurationProvider cp)
	{
		this(config,new String[] {lang},translations,cp);
	}
	public OfxSecurityCategoryListFactory(org.exlp.interfaces.system.property.Configuration config,String[] langs, Translations translations,ConfigurationProvider cp)
	{
		super(config,langs,translations);
		this.cp=cp;
	}
	
	@Deprecated public String saveDescription(java.util.List<Category> categories) throws OfxAuthoringException
	{
		try
		{
			LatexListRenderer renderer = new LatexListRenderer(cp,false);
			renderer.render(create(categories),new LatexSectionRenderer(cp,0,null));
			StringWriter sw = new StringWriter();
			renderer.write(sw);
			return sw.toString();
		}
		catch (IOException e) {throw new OfxAuthoringException(e.getMessage());}
	}
	
	public String saveDescriptionSec(java.util.List<org.jeesl.model.xml.system.security.Category> categories) throws OfxAuthoringException
	{
		try
		{
			LatexListRenderer renderer = new LatexListRenderer(cp,false);
			renderer.render(descriptionList(categories),new LatexSectionRenderer(cp,0,null));
			StringWriter sw = new StringWriter();
			renderer.write(sw);
			return sw.toString();
		}
		catch (IOException e) {throw new OfxAuthoringException(e.getMessage());}
	}
	
	@Deprecated public List create(java.util.List<Category> lRc)
	{
		Comment comment = XmlCommentFactory.build();
		OfxCommentBuilder.doNotModify(comment);
		
		List list = XmlListFactory.unordered();
		list.setComment(comment);
		
		for(Category category : lRc)
		{
			try {list.getItem().add(createItem(category));}
			catch (ExlpXpathNotFoundException e) {e.printStackTrace();}
			catch (ExlpXpathNotUniqueException e) {e.printStackTrace();}
		}
		
		return list;
	}
	
	public List descriptionList(java.util.List<org.jeesl.model.xml.system.security.Category> categories) throws OfxAuthoringException
	{
		List list = XmlListFactory.description();
		
		for(org.jeesl.model.xml.system.security.Category category : categories)
		{
			list.getItem().addAll(OfxMultiLangFactory.items(langs, category.getLangs(), category.getDescriptions()));
		}
		
		return list;
	}
	
	@Deprecated private Item createItem(Category category) throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	{
		String description,text;
		try
		{
			if(langs.length>1){logger.warn("Incorrect Assignment");}
			Lang l = StatusXpath.getLang(category.getLangs(), langs[0]);
			description = l.getTranslation();
		}
		catch (ExlpXpathNotFoundException e){description = e.getMessage();}
		catch (ExlpXpathNotUniqueException e){description = e.getMessage();}
		
		try
		{
			if(langs.length>1){logger.warn("Incorrect Assignment");}
			Description d = StatusXpath.getDescription(category.getDescriptions(), langs[0]);
			text = d.getValue();
		}
		catch (ExlpXpathNotFoundException e){text = e.getMessage();}
		catch (ExlpXpathNotUniqueException e){text = e.getMessage();}		
		
		Paragraph p = new Paragraph();
		p.getContent().add(text);
		
		Item item = new Item();
		item.setName(description);
		item.getContent().add(p);
		
		return item;
	}
}