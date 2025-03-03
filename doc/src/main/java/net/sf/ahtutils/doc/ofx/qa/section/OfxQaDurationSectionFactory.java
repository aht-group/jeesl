package net.sf.ahtutils.doc.ofx.qa.section;

import java.io.FileNotFoundException;
import java.util.List;

import org.exlp.interfaces.system.property.Configuration;
import org.exlp.util.jx.JaxbUtil;
import org.jeesl.exception.processing.UtilsConfigurationException;
import org.jeesl.model.xml.io.locale.status.Translations;
import org.jeesl.model.xml.module.dev.qa.Category;
import org.jeesl.model.xml.module.dev.qa.Groups;
import org.openfuxml.exception.OfxAuthoringException;
import org.openfuxml.factory.xml.ofx.content.XmlCommentFactory;
import org.openfuxml.factory.xml.ofx.content.structure.XmlSectionFactory;
import org.openfuxml.factory.xml.ofx.content.text.XmlTitleFactory;
import org.openfuxml.model.xml.core.ofx.Comment;
import org.openfuxml.model.xml.core.ofx.Section;
import org.openfuxml.util.OfxCommentBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.doc.latex.builder.UtilsLatexQaDocumentationBuilder;
import net.sf.ahtutils.doc.ofx.AbstractUtilsOfxDocumentationFactory;
import net.sf.ahtutils.doc.ofx.qa.table.OfxQaDurationFrCategoryTable;
import net.sf.ahtutils.doc.ofx.qa.table.OfxQaDurationFrSummaryTable;
import net.sf.ahtutils.doc.ofx.qa.table.OfxQaDurationGroupTable;

public class OfxQaDurationSectionFactory extends AbstractUtilsOfxDocumentationFactory
{
	final static Logger logger = LoggerFactory.getLogger(OfxQaDurationSectionFactory.class);

	private OfxQaDurationFrSummaryTable ofSummary;
	private OfxQaDurationFrCategoryTable ofDuration;
	private OfxQaDurationGroupTable ofGroup;
	
	public OfxQaDurationSectionFactory(Configuration config, String[] langs, Translations translations)
	{
		super(config,langs,translations);
		ofSummary = new OfxQaDurationFrSummaryTable(config,langs,translations);
		ofDuration = new OfxQaDurationFrCategoryTable(config,langs,translations);
		ofGroup = new OfxQaDurationGroupTable(config,langs,translations);
	}
	
	public Section build(List<Category> categories,Groups groups) throws OfxAuthoringException, UtilsConfigurationException
	{
		Section section = XmlSectionFactory.build();
		section.setId("section.qa.durations");
		section.setContainer(false);
		section.getContent().add(XmlTitleFactory.build("Test Durations"));
		
		Comment comment = XmlCommentFactory.build();
		OfxCommentBuilder.doNotModify(comment);
		section.getContent().add(comment);
		
		section.getContent().add(introduction());
		
		section.getContent().add(ofSummary.build(categories));
		section.getContent().add(durationsGroups(categories,groups));
		section.getContent().add(durationsFrCategories(categories));
		
		return section;
	}
	
	private Section introduction() throws OfxAuthoringException
	{
		Section section;
		try
		{
			section = JaxbUtil.loadJAXB(UtilsLatexQaDocumentationBuilder.rsrcDuration, Section.class);
			
			Comment comment = XmlCommentFactory.build();
			OfxCommentBuilder.doNotModify(comment);
			section.getContent().add(comment);
			
			return section;
		}
		catch (FileNotFoundException e) {throw new OfxAuthoringException(e.getMessage());}
	}
	
	private Section durationsFrCategories(List<Category> categories) throws OfxAuthoringException, UtilsConfigurationException
	{
		Section section = XmlSectionFactory.build();
		section.setId("section.qa.durations.fr");
		section.setContainer(false);
		section.getContent().add(XmlTitleFactory.build("Detailed durations for FR Categories"));
		
		for(Category category : categories)
		{
			section.getContent().add(ofDuration.build(category));
		}
		return section;
	}
	
	private Section durationsGroups(List<Category> categories,Groups groups) throws OfxAuthoringException, UtilsConfigurationException
	{
		Section section = XmlSectionFactory.build();
		section.setId("section.qa.durations.groups");
		section.setContainer(false);
		section.getContent().add(XmlTitleFactory.build("Test Durations for QA Groups"));
		
		section.getContent().add(ofGroup.build(categories,groups));
		return section;
	}
}