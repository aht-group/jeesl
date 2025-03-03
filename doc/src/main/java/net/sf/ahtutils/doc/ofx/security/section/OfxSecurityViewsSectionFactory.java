package net.sf.ahtutils.doc.ofx.security.section;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.exlp.util.jx.JaxbUtil;
import org.jeesl.controller.util.comparator.primitive.BooleanComparator;
import org.jeesl.doc.latex.builder.JeeslLatexAdminDocumentationBuilder;
import org.jeesl.doc.ofx.OfxMultiLangFactory;
import org.jeesl.exception.processing.UtilsConfigurationException;
import org.jeesl.model.xml.io.locale.status.Translations;
import org.jeesl.model.xml.system.security.Role;
import org.jeesl.model.xml.system.security.Security;
import org.jeesl.model.xml.system.security.View;
import org.jeesl.util.query.xpath.StatusXpath;
import org.openfuxml.exception.OfxAuthoringException;
import org.openfuxml.factory.xml.ofx.content.XmlCommentFactory;
import org.openfuxml.factory.xml.ofx.content.structure.XmlParagraphFactory;
import org.openfuxml.factory.xml.ofx.content.structure.XmlSectionFactory;
import org.openfuxml.factory.xml.ofx.content.text.XmlTitleFactory;
import org.openfuxml.factory.xml.text.OfxTextFactory;
import org.openfuxml.model.xml.core.ofx.Comment;
import org.openfuxml.model.xml.core.ofx.Paragraph;
import org.openfuxml.model.xml.core.ofx.Section;
import org.openfuxml.model.xml.core.ofx.Title;
import org.openfuxml.util.OfxCommentBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.doc.DocumentationCommentBuilder;
import net.sf.ahtutils.doc.ofx.AbstractUtilsOfxDocumentationFactory;
import net.sf.ahtutils.doc.ofx.security.list.OfxSecurityCategoryListFactory;
import net.sf.ahtutils.doc.ofx.security.table.OfxSecurityActionTableFactory;
import net.sf.ahtutils.doc.ofx.security.table.OfxSecurityViewTableFactory;
import net.sf.exlp.exception.ExlpXpathNotFoundException;
import net.sf.exlp.exception.ExlpXpathNotUniqueException;

public class OfxSecurityViewsSectionFactory extends AbstractUtilsOfxDocumentationFactory
{
	final static Logger logger = LoggerFactory.getLogger(OfxSecurityViewsSectionFactory.class);

	private OfxSecurityCategoryListFactory ofSecurityCategoryList;
	private OfxSecurityViewTableFactory ofSecurityViewTable;
	private OfxSecurityActionTableFactory ofSecurityActionTable;
	private JeeslLatexAdminDocumentationBuilder adminDocBuilder;
		
	public OfxSecurityViewsSectionFactory(org.exlp.interfaces.system.property.Configuration config, String[] langs, Translations translations)
	{
		super(config,langs,translations);
		ofSecurityCategoryList = new OfxSecurityCategoryListFactory(config,langs,translations,null);
		ofSecurityViewTable = new OfxSecurityViewTableFactory(config,langs,translations);
		ofSecurityActionTable = new OfxSecurityActionTableFactory(config,langs,translations);
		
		adminDocBuilder = new JeeslLatexAdminDocumentationBuilder(config,translations,langs,null);
	}
	
	public Section build(Security security) throws OfxAuthoringException, UtilsConfigurationException
	{
		Section section = XmlSectionFactory.build();
		section.getContent().add(XmlTitleFactory.build("Views & Actions"));
		
		Comment comment = XmlCommentFactory.build();
		OfxCommentBuilder.doNotModify(comment);
		section.getContent().add(comment);
		
		try
		{
			String source = adminDocBuilder.getOfxResource(JeeslLatexAdminDocumentationBuilder.SecurityCode.sActualViews);
			Section intro = JaxbUtil.loadJAXB(source, Section.class);
			
			Comment cIntro = XmlCommentFactory.build();
			DocumentationCommentBuilder.configKeyReference(cIntro, config, JeeslLatexAdminDocumentationBuilder.SecurityCode.sActualViews.toString(), "Source file");
			intro.getContent().add(cIntro);
			
			section.getContent().add(intro);
		}
		catch (FileNotFoundException e) {throw new OfxAuthoringException(e.getMessage());}
		
//		section.getContent().add(ofSecurityCategoryList.list(security.getCategory()));
		
		for(org.jeesl.model.xml.system.security.Category category : security.getCategory())
		{
			if(BooleanComparator.active(category.isDocumentation()))
			{
				section.getContent().add(build(category));
			}
		}
		
		return section;
	}
	
	private Section build(org.jeesl.model.xml.system.security.Category category) throws OfxAuthoringException
	{
		Section section = XmlSectionFactory.build();
		section.getContent().add(OfxMultiLangFactory.title(langs, category.getLangs()));
		
		section.getContent().addAll(OfxMultiLangFactory.paragraph(langs, category.getDescriptions()));
		section.getContent().add(ofSecurityViewTable.build(category));
		
		if(Objects.nonNull(category.getViews()))
		{
			for(View view : category.getViews().getView())
			{
				if(BooleanComparator.active(view.isDocumentation()) && Objects.nonNull(view.getActions()) && view.getActions().getAction().size()>0)
				{
					section.getContent().add(build(category, view));
				}
			}
		}
		
		return section;
	}
	
	private Section build(org.jeesl.model.xml.system.security.Category category, View view) throws OfxAuthoringException
	{
		Section section = XmlSectionFactory.build();
		
		Title title = XmlTitleFactory.build();
		for(String key : langs)
		{
			StringBuffer sb = new StringBuffer();
			try
			{
				sb.append(StatusXpath.getLang(category.getLangs(),key).getTranslation());
				sb.append(" - ");
				sb.append(StatusXpath.getLang(view.getLangs(),key).getTranslation());
			}
			catch (ExlpXpathNotFoundException e) {throw new OfxAuthoringException(e.getMessage());}
			catch (ExlpXpathNotUniqueException e) {throw new OfxAuthoringException(e.getMessage());}
			title.getContent().add(OfxTextFactory.build(key,sb.toString()));
		}
		section.getContent().add(title);
		section.getContent().addAll(OfxMultiLangFactory.paragraph(langs, view.getDescriptions()));
		
		if(Objects.nonNull(view.getRoles()) && view.getRoles().getRole().size()>0)
		{
			section.getContent().addAll(buildRoles(view));
		}
		
		section.getContent().add(ofSecurityActionTable.build(view));
		
		return section;
	}
	
	private List<Paragraph> buildRoles(View view) throws OfxAuthoringException
	{
		List<Paragraph> content = new ArrayList<Paragraph>();
		List<Role> roles = view.getRoles().getRole();
		try
		{
			for(String key : langs)
			{
				StringBuffer sb = new StringBuffer();
				sb.append("The relevant roles for this page are");
				for(int i=1;i<=roles.size();i++)
				{
					sb.append(" ("+i+")");
					sb.append(" "+StatusXpath.getLang(roles.get(i-1).getLangs(),key).getTranslation());
					if(roles.size()>1)
					{
						if(i<roles.size()-1){sb.append(",");}
						else if(i<roles.size()){sb.append(" and");}
						else{sb.append(".");}
					}
				}
				
				Paragraph p = XmlParagraphFactory.lang(key);
				p.getContent().add(sb.toString());
				
				if(view.getCode().equals("projectGeneral")){logger.info(sb.toString());}
				
				content.add(p);
			}
		}
		catch (ExlpXpathNotFoundException e) {throw new OfxAuthoringException(e.getMessage());}
		catch (ExlpXpathNotUniqueException e) {throw new OfxAuthoringException(e.getMessage());}
		return content;
	}
}