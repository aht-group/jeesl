package net.sf.ahtutils.doc.ofx.security.section;

import java.io.FileNotFoundException;

import org.exlp.interfaces.system.property.Configuration;
import org.exlp.util.jx.JaxbUtil;
import org.jeesl.controller.util.comparator.primitive.BooleanComparator;
import org.jeesl.doc.latex.builder.JeeslLatexAdminDocumentationBuilder;
import org.jeesl.doc.ofx.OfxMultiLangFactory;
import org.jeesl.exception.processing.UtilsConfigurationException;
import org.jeesl.model.xml.io.locale.status.Translations;
import org.jeesl.model.xml.system.security.Category;
import org.jeesl.model.xml.system.security.Security;
import org.openfuxml.exception.OfxAuthoringException;
import org.openfuxml.factory.xml.ofx.content.XmlCommentFactory;
import org.openfuxml.factory.xml.ofx.content.structure.XmlSectionFactory;
import org.openfuxml.factory.xml.ofx.content.text.XmlTitleFactory;
import org.openfuxml.model.xml.core.ofx.Comment;
import org.openfuxml.model.xml.core.ofx.Section;
import org.openfuxml.util.OfxCommentBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.doc.DocumentationCommentBuilder;
import net.sf.ahtutils.doc.ofx.AbstractUtilsOfxDocumentationFactory;
import net.sf.ahtutils.doc.ofx.security.list.OfxSecurityCategoryListFactory;
import net.sf.ahtutils.doc.ofx.security.table.OfxSecurityRoleTableFactory;

public class OfxSecurityRolesSectionFactory extends AbstractUtilsOfxDocumentationFactory
{
	final static Logger logger = LoggerFactory.getLogger(OfxSecurityRolesSectionFactory.class);

	private OfxSecurityCategoryListFactory ofSecurityCategoryList;
	private OfxSecurityRoleTableFactory ofSecurityRoleTable;
	private JeeslLatexAdminDocumentationBuilder adminDocBuilder;
		
	public OfxSecurityRolesSectionFactory(Configuration config, String[] langs, Translations translations)
	{
		super(config,langs,translations);
		ofSecurityCategoryList = new OfxSecurityCategoryListFactory(config,langs,translations,null);
		ofSecurityRoleTable = new OfxSecurityRoleTableFactory(config,langs,translations);
		
		adminDocBuilder = new JeeslLatexAdminDocumentationBuilder(config,translations,langs,null);
	}
	
	
	public Section build(Security security) throws OfxAuthoringException, UtilsConfigurationException
	{
		Section section = XmlSectionFactory.build();
		section.getContent().add(XmlTitleFactory.build("Roles"));
		
		Comment comment = XmlCommentFactory.build();
		OfxCommentBuilder.doNotModify(comment);
		section.getContent().add(comment);
		
		try
		{
			String source = adminDocBuilder.getOfxResource(JeeslLatexAdminDocumentationBuilder.SecurityCode.sActualRoles);
			Section intro = JaxbUtil.loadJAXB(source, Section.class);
			
			Comment cIntro = XmlCommentFactory.build();
			DocumentationCommentBuilder.configKeyReference(cIntro, config, JeeslLatexAdminDocumentationBuilder.SecurityCode.sActualRoles.toString(), "Source file");
			intro.getContent().add(cIntro);
			
			section.getContent().add(intro);
		}
		catch (FileNotFoundException e) {throw new OfxAuthoringException(e.getMessage());}
		
		section.getContent().add(ofSecurityCategoryList.descriptionList(security.getCategory()));
		
		for(Category category : security.getCategory())
		{
			if(BooleanComparator.active(category.isDocumentation()))
			{
				section.getContent().add(build(category));
			}
		}
		
		return section;
	}
	
	private Section build(Category category) throws OfxAuthoringException
	{
		Section section = XmlSectionFactory.build();
		section.getContent().add(OfxMultiLangFactory.title(langs, category.getLangs()));
		
		section.getContent().addAll(OfxMultiLangFactory.paragraph(langs, category.getDescriptions()));
		section.getContent().add(ofSecurityRoleTable.build(category));
		return section;
	}
}