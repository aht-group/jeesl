package net.sf.ahtutils.doc.ofx.qa.section;

import org.jeesl.model.xml.io.locale.status.Translations;
import org.jeesl.model.xml.module.dev.qa.Group;
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

import net.sf.ahtutils.doc.ofx.AbstractUtilsOfxDocumentationFactory;
import net.sf.ahtutils.doc.ofx.qa.table.OfxQaStaffTableFactory;

public class OfxQaScheduleSectionFactory extends AbstractUtilsOfxDocumentationFactory
{
	final static Logger logger = LoggerFactory.getLogger(OfxQaScheduleSectionFactory.class);

	private OfxQaStaffTableFactory ofStaff;
	
	public OfxQaScheduleSectionFactory(org.exlp.interfaces.system.property.Configuration config, String[] langs, Translations translations)
	{
		super(config,langs,translations);
		ofStaff = new OfxQaStaffTableFactory(config,langs,translations);
		ofStaff.setColumns(OfxQaStaffTableFactory.ColumCode.organisationDepartment,OfxQaStaffTableFactory.ColumCode.name,OfxQaStaffTableFactory.ColumCode.responsibility);
	}
	
	public Section build(Groups groups) throws OfxAuthoringException
	{
		Section section = XmlSectionFactory.build();
		section.setContainer(true);
		section.getContent().add(XmlTitleFactory.build("Test Groups"));
		
		Comment comment = XmlCommentFactory.build();
		OfxCommentBuilder.doNotModify(comment);
		section.getContent().add(comment);
		
		for(Group group : groups.getGroup())
		{
			section.getContent().add(build(group));
		}
		
		return section;
	}
	
	private Section build(Group group) throws OfxAuthoringException
	{
		Section section = XmlSectionFactory.build();
		section.getContent().add(XmlTitleFactory.build(group.getName()));
		
				
		return section;
	}
}