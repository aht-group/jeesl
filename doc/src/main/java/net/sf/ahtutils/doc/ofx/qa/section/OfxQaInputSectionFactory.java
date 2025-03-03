package net.sf.ahtutils.doc.ofx.qa.section;

import org.apache.commons.io.FilenameUtils;
import org.exlp.interfaces.system.property.Configuration;
import org.jeesl.model.xml.io.locale.status.Translations;
import org.jeesl.model.xml.module.dev.qa.Category;
import org.jeesl.model.xml.module.dev.qa.Qa;
import org.jeesl.model.xml.module.survey.Template;
import org.openfuxml.exception.OfxAuthoringException;
import org.openfuxml.factory.xml.ofx.content.XmlCommentFactory;
import org.openfuxml.factory.xml.ofx.content.structure.XmlSectionFactory;
import org.openfuxml.model.xml.core.ofx.Comment;
import org.openfuxml.model.xml.core.ofx.Section;
import org.openfuxml.util.OfxCommentBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.doc.ofx.AbstractUtilsOfxDocumentationFactory;

public class OfxQaInputSectionFactory extends AbstractUtilsOfxDocumentationFactory
{
	final static Logger logger = LoggerFactory.getLogger(OfxQaInputSectionFactory.class);

	public OfxQaInputSectionFactory(Configuration config, String lang, Translations translations)
	{
		this(config,new String[] {lang},translations);
	}
	public OfxQaInputSectionFactory(Configuration config, String[] langs, Translations translations)
	{
		super(config,langs,translations);
	}
	
	// FR Categories
	public Section build(Qa qa, String path) throws OfxAuthoringException
	{
		Section section = XmlSectionFactory.build();
		section.setContainer(true);
		
		Comment comment = XmlCommentFactory.build();
		OfxCommentBuilder.doNotModify(comment);
		section.getContent().add(comment);
		
		for(Category c : qa.getCategory())
		{
			if(!c.getCode().equals("T"))
			{
				for(String lang : langs)
				{
					section.getContent().add(buildCategory(c,lang+path));
				}
			}
		}
		
		return section;
	}
	
	private Section buildCategory(Category c, String path)
	{
		Section section = XmlSectionFactory.build();
		section.setContainer(true);
		section.setInclude(path+"/"+c.getCode());
		return section;
	}
	
	// NFR Sections
	public Section build(Template template, String path) throws OfxAuthoringException
	{
		Section section = XmlSectionFactory.build();
		section.setContainer(true);
		
		Comment comment = XmlCommentFactory.build();
		OfxCommentBuilder.doNotModify(comment);
		section.getContent().add(comment);
		
		for(org.jeesl.model.xml.module.survey.Section s : template.getSection())
		{
			for(String lang : langs)
			{
				Section sub = XmlSectionFactory.build();
				sub.setContainer(true);
				sub.setLang(lang);
				sub.setInclude(FilenameUtils.normalize(lang+"/"+path+"/"+s.getPosition()));
				section.getContent().add(sub);
			}
		}
		
		return section;
	}
}