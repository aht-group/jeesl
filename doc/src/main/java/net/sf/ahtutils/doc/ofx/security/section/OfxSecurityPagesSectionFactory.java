package net.sf.ahtutils.doc.ofx.security.section;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

import org.exlp.interfaces.system.property.Configuration;
import org.exlp.util.jx.JaxbUtil;
import org.jeesl.doc.ofx.OfxMultiLangFactory;
import org.jeesl.factory.xml.system.security.XmlActionFactory;
import org.jeesl.model.xml.io.locale.status.Description;
import org.jeesl.model.xml.io.locale.status.Lang;
import org.jeesl.model.xml.io.locale.status.Translations;
import org.jeesl.model.xml.system.security.Action;
import org.jeesl.model.xml.system.security.Role;
import org.jeesl.model.xml.system.security.View;
import org.jeesl.util.query.xpath.StatusXpath;
import org.openfuxml.exception.OfxAuthoringException;
import org.openfuxml.factory.xml.layout.XmlBoxFactory;
import org.openfuxml.factory.xml.layout.XmlColumnFactory;
import org.openfuxml.factory.xml.layout.XmlContainerFactory;
import org.openfuxml.factory.xml.layout.XmlFontFactory;
import org.openfuxml.factory.xml.layout.XmlHeightFactory;
import org.openfuxml.factory.xml.layout.XmlWidthFactory;
import org.openfuxml.factory.xml.list.XmlListFactory;
import org.openfuxml.factory.xml.list.XmlListItemFactory;
import org.openfuxml.factory.xml.media.XmlMediaFactory;
import org.openfuxml.factory.xml.ofx.content.XmlCommentFactory;
import org.openfuxml.factory.xml.ofx.content.structure.XmlParagraphFactory;
import org.openfuxml.factory.xml.ofx.content.structure.XmlSectionFactory;
import org.openfuxml.factory.xml.ofx.editorial.XmlMarginaliaFactory;
import org.openfuxml.factory.xml.text.OfxEmphasisFactory;
import org.openfuxml.model.xml.core.layout.Container;
import org.openfuxml.model.xml.core.layout.Font;
import org.openfuxml.model.xml.core.list.Item;
import org.openfuxml.model.xml.core.list.List;
import org.openfuxml.model.xml.core.media.Image;
import org.openfuxml.model.xml.core.ofx.Comment;
import org.openfuxml.model.xml.core.ofx.Marginalia;
import org.openfuxml.model.xml.core.ofx.Paragraph;
import org.openfuxml.model.xml.core.ofx.Section;
import org.openfuxml.util.OfxCommentBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.doc.ofx.AbstractUtilsOfxDocumentationFactory;
import net.sf.exlp.exception.ExlpXpathNotFoundException;
import net.sf.exlp.exception.ExlpXpathNotUniqueException;

public class OfxSecurityPagesSectionFactory extends AbstractUtilsOfxDocumentationFactory
{
	final static Logger logger = LoggerFactory.getLogger(OfxSecurityPagesSectionFactory.class);

	private OfxEmphasisFactory ofxItalic;
	private Font fMarginText;

	private static int count;
	private final String[] intro = {"The actions defined on this page cannot be carried out by everyone. Depending on your access rights you may or may not be entitled to carry them out. To the side you can see a summary of the available actions and those people who are allowed to perform them.",
										"There are several actions defined on this page, they require certain access rights (membership to a role) to be performed. The available actions are summarised here together with the relevant roles for this page affecting the allowed actions.",
										"Not everyone can perform the actions listed here as these depend on your access rights. To the side you can also find a summary of the available actions and the roles that can perform them.",
										"Your access rights determine which actions you can carry out. A summary is also available to the side of this text which shows you which roles there are and the accompanying available actions.",
										"Access rights are not the same for everyone. This means that the same actions cannot be performed by everyone. Also, a summary is provided to the side of this text which shows you which roles there are and the actions they can perform.",
										"Not everyone can carry out the actions listed here. Here you can also see a summary of the roles and the actions that go with them."
									};
		
	public OfxSecurityPagesSectionFactory(Configuration config, String[] langs, Translations translations)
	{
		super(config,langs,translations);
		ofxItalic = new OfxEmphasisFactory(false,true);
		fMarginText = XmlFontFactory.relative(-3);
	}
	
	public Section build(View view) throws OfxAuthoringException
	{
		if(view.getCode().equals("programBudget")){JaxbUtil.trace(view);}
		Section section = XmlSectionFactory.build();
		section.setContainer(true);
		
		Comment comment = XmlCommentFactory.build();
		OfxCommentBuilder.doNotModify(comment);
		section.getContent().add(comment);
		
		if(Objects.nonNull(view.getDescriptions()))
		{
			section.getContent().addAll(OfxMultiLangFactory.paragraph(langs, view.getDescriptions()));
		}

		if(Objects.nonNull(view.getActions()) && view.getActions().getAction().size()>0)
		{
			section.getContent().addAll(introductionAction(view));
			List list = XmlListFactory.unordered();
			for(Action action : view.getActions().getAction())
			{
				Item item = XmlListItemFactory.build();
				
				for(String lang : langs)
				{
					Paragraph p = XmlParagraphFactory.lang(lang);
					try
					{
						JaxbUtil.trace(action);
						Lang l = StatusXpath.getLang(XmlActionFactory.toLangs(action), lang);
						p.getContent().add(ofxItalic.build(l.getTranslation()));
						p.getContent().add(": ");
					}
					catch (ExlpXpathNotFoundException e) {e.printStackTrace();}
					catch (ExlpXpathNotUniqueException e) {e.printStackTrace();}
					
					try
					{
						Description d = StatusXpath.getDescription(XmlActionFactory.toDescriptions(action), lang);
						p.getContent().add(d.getValue());
					}
					catch (ExlpXpathNotFoundException e) {e.printStackTrace();}
					catch (ExlpXpathNotUniqueException e) {e.printStackTrace();}
					
					item.getContent().add(p);
				}
				list.getItem().add(item);
			}
			section.getContent().add(list);
		}
		
		return section;
	}
	
	private java.util.List<Serializable> introductionAction(View view)
	{
		java.util.List<Serializable> content = new ArrayList<Serializable>();
		
		Paragraph p = new Paragraph();
		p.getContent().add(intro[count]);
//		p.getContent().add("The security indicators on the status bar show your roles and allowed actions for this page.");
//		p.getContent().add("");
		content.add(p);

		if(++count == intro.length) {count = 0;}

		if(view.getRoles().getRole().size()>0)
		{
			Marginalia m = XmlMarginaliaFactory.build();
			m.getContent().add(XmlBoxFactory.build());
			
			Image image = new Image();
			image.setWidth(XmlWidthFactory.percentage(100));
			image.setMedia(XmlMediaFactory.build("svg.aht-utils/icon/ui/security/shield-red.svg", "icon/ui/security/shield-red"));
			
			Container cTopText = XmlContainerFactory.build(fMarginText);
			for(String lang : langs)
			{
				Paragraph pText = XmlParagraphFactory.lang(lang);
				pText.getContent().add("Restricted to:");
				cTopText.getContent().add(pText);
				
//				Paragraph pRoles = XmlParagraphFactory.build(lang);
//				try
//				{
//					Lang l = StatusXpath.getLang(view.getLangs(), lang);
//					pRoles.getContent().add(ofxItalic.build(l.getTranslation()));
//					pRoles.getContent().add(":");
//				}
//				catch (ExlpXpathNotFoundException e) {e.printStackTrace();}
//				catch (ExlpXpathNotUniqueException e) {e.printStackTrace();}
//
//				cTopText.getContent().add(pRoles);
			}
	
			m.getContent().add(XmlColumnFactory.build(XmlWidthFactory.percentage(25),XmlHeightFactory.shift(-0.60),image));
			m.getContent().add(XmlWidthFactory.percentage(5));
			m.getContent().add(XmlColumnFactory.build(XmlWidthFactory.percentage(70),cTopText));
			
			Container cRoles = XmlContainerFactory.build(fMarginText);
			for(Role role : view.getRoles().getRole())
			{
				cRoles.getContent().addAll(OfxMultiLangFactory.paragraph(langs, role.getLangs()));
			}
			m.getContent().add(cRoles);
			
			content.add(m);
		}
		
		return content;
	}
}