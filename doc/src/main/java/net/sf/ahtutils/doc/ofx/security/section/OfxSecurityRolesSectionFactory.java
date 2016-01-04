package net.sf.ahtutils.doc.ofx.security.section;

import java.io.FileNotFoundException;

import org.apache.commons.configuration.Configuration;
import org.openfuxml.content.ofx.Comment;
import org.openfuxml.content.ofx.Section;
import org.openfuxml.exception.OfxAuthoringException;
import org.openfuxml.factory.xml.ofx.content.XmlCommentFactory;
import org.openfuxml.factory.xml.ofx.content.structure.XmlSectionFactory;
import org.openfuxml.factory.xml.ofx.content.text.XmlTitleFactory;
import org.openfuxml.util.OfxCommentBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.doc.DocumentationCommentBuilder;
import net.sf.ahtutils.doc.latex.builder.UtilsLatexAdminDocumentationBuilder;
import net.sf.ahtutils.doc.ofx.AbstractUtilsOfxDocumentationFactory;
import net.sf.ahtutils.doc.ofx.security.list.OfxSecurityCategoryListFactory;
import net.sf.ahtutils.doc.ofx.security.table.OfxSecurityRoleTableFactory;
import net.sf.ahtutils.doc.ofx.util.OfxMultiLangFactory;
import net.sf.ahtutils.exception.processing.UtilsConfigurationException;
import net.sf.ahtutils.xml.security.Category;
import net.sf.ahtutils.xml.security.Security;
import net.sf.ahtutils.xml.status.Translations;
import net.sf.exlp.util.xml.JaxbUtil;

public class OfxSecurityRolesSectionFactory extends AbstractUtilsOfxDocumentationFactory
{
	final static Logger logger = LoggerFactory.getLogger(OfxSecurityRolesSectionFactory.class);

	private OfxSecurityCategoryListFactory ofSecurityCategoryList;
	private OfxSecurityRoleTableFactory ofSecurityRoleTable;
	private UtilsLatexAdminDocumentationBuilder adminDocBuilder;
		
	public OfxSecurityRolesSectionFactory(Configuration config, String[] langs, Translations translations)
	{
		super(config,langs,translations);
		ofSecurityCategoryList = new OfxSecurityCategoryListFactory(config,langs,translations,null,null);
		ofSecurityRoleTable = new OfxSecurityRoleTableFactory(config,langs,translations);
		
		adminDocBuilder = new UtilsLatexAdminDocumentationBuilder(config,translations,langs,null,null);
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
			String source = adminDocBuilder.getOfxResource(UtilsLatexAdminDocumentationBuilder.SecurityCode.sActualRoles);
			Section intro = JaxbUtil.loadJAXB(source, Section.class);
			
			Comment cIntro = XmlCommentFactory.build();
			DocumentationCommentBuilder.configKeyReference(cIntro, config, UtilsLatexAdminDocumentationBuilder.SecurityCode.sActualRoles.toString(), "Source file");
			intro.getContent().add(cIntro);
			
			section.getContent().add(intro);
		}
		catch (FileNotFoundException e) {throw new OfxAuthoringException(e.getMessage());}
		
		section.getContent().add(ofSecurityCategoryList.list(security.getCategory()));
		
		for(Category category : security.getCategory())
		{
			if(!category.isSetDocumentation()){category.setDocumentation(false);}
			if(category.isDocumentation())
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