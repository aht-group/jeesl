package org.jeesl.doc.ofx.constraints;

import org.jeesl.doc.ofx.OfxMultiLangFactory;
import org.jeesl.exception.processing.UtilsConfigurationException;
import org.jeesl.model.xml.io.locale.status.Translations;
import org.jeesl.model.xml.system.constraint.Constraint;
import org.jeesl.model.xml.system.constraint.ConstraintScope;
import org.jeesl.model.xml.xsd.Container;
import org.openfuxml.exception.OfxAuthoringException;
import org.openfuxml.factory.xml.layout.XmlSpacingFactory;
import org.openfuxml.factory.xml.layout.XmlWidthFactory;
import org.openfuxml.factory.xml.list.XmlListFactory2;
import org.openfuxml.factory.xml.ofx.content.XmlCommentFactory;
import org.openfuxml.factory.xml.ofx.content.structure.XmlParagraphFactory;
import org.openfuxml.factory.xml.ofx.content.structure.XmlSectionFactory;
import org.openfuxml.factory.xml.ofx.editorial.XmlMarginaliaFactory;
import org.openfuxml.factory.xml.ofx.layout.XmlLayoutFactory;
import org.openfuxml.model.xml.core.layout.Layout;
import org.openfuxml.model.xml.core.media.Image;
import org.openfuxml.model.xml.core.media.Media;
import org.openfuxml.model.xml.core.ofx.Comment;
import org.openfuxml.model.xml.core.ofx.Marginalia;
import org.openfuxml.model.xml.core.ofx.Paragraph;
import org.openfuxml.model.xml.core.ofx.Section;
import org.openfuxml.util.OfxCommentBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.doc.ofx.AbstractUtilsOfxDocumentationFactory;

public class OfxConstraintScopeSectionFactory extends AbstractUtilsOfxDocumentationFactory
{
	final static Logger logger = LoggerFactory.getLogger(OfxConstraintScopeSectionFactory.class);

	private OfxConstraintTableFactory ofTable;
	private Layout layout;
		
	public void setConstraintTypes(Container constraintTypes) {ofTable.setConstraintTypes(constraintTypes);}
	
	public OfxConstraintScopeSectionFactory(org.exlp.interfaces.system.property.Configuration config, String[] langs, Translations translations)
	{
		super(config,langs,translations);
		ofTable = new OfxConstraintTableFactory(config,langs,translations);
		
		layout = XmlLayoutFactory.build(XmlSpacingFactory.pt(0));
	}
	
	public Section build(ConstraintScope scope) throws OfxAuthoringException, UtilsConfigurationException
	{
		Section section = XmlSectionFactory.build();
		section.setContainer(true);
		
		Comment comment = XmlCommentFactory.build();
		OfxCommentBuilder.doNotModify(comment);
		section.getContent().add(comment);
		
//		Table table = ofTable.build(scope);
		
		Paragraph p = XmlParagraphFactory.build();
		p.getContent().add(marginalia());
		p.getContent().addAll(OfxMultiLangFactory.paragraph(langs, scope.getDescriptions()).get(0).getContent());
//		p.getContent().add(" All constraints are summarised in Table ");
//		p.getContent().add(OfxReferenceFactory.build(table.getId()));
//		p.getContent().add(".");
		
		section.getContent().add(p);
//		section.getContent().add(table);
		section.getContent().add(list(scope));
		
		return section;
	}
		
	private org.openfuxml.model.xml.core.list.List list(ConstraintScope scope) throws OfxAuthoringException
	{
		org.openfuxml.model.xml.core.list.List list = XmlListFactory2.unordered();
		list.setLayout(layout);
		for(Constraint c : scope.getConstraint())
		{
			list.getItem().add(OfxMultiLangFactory.item(langs, c.getDescriptions()));
		}
		
		return list;
	}
	
	private Marginalia marginalia()
	{
		Media media = new Media();
		media.setSrc("svg.aht-utils/icon/ui/system/constraint.svg");
		media.setDst("icon/ui/system/constraint");
		
		Image image = new Image();
		image.setMedia(media);
//		image.setAlignment(XmlAlignmentFactory.buildHorizontal(XmlAlignmentFactory.Horizontal.center));
		image.setWidth(XmlWidthFactory.percentage(50));
		
		return XmlMarginaliaFactory.build(image);
	}
}