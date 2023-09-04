package net.sf.ahtutils.doc.ofx.qa.section;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.configuration.Configuration;
import org.jeesl.model.xml.jeesl.Container;
import org.jeesl.model.xml.module.survey.Answer;
import org.jeesl.model.xml.module.survey.Question;
import org.jeesl.model.xml.module.survey.Survey;
import org.jeesl.model.xml.system.security.Staff;
import org.openfuxml.content.ofx.Comment;
import org.openfuxml.content.ofx.Paragraph;
import org.openfuxml.content.ofx.Section;
import org.openfuxml.content.ofx.Title;
import org.openfuxml.content.table.Table;
import org.openfuxml.exception.OfxAuthoringException;
import org.openfuxml.factory.xml.ofx.content.XmlCommentFactory;
import org.openfuxml.factory.xml.ofx.content.structure.XmlParagraphFactory;
import org.openfuxml.factory.xml.ofx.content.structure.XmlSectionFactory;
import org.openfuxml.factory.xml.ofx.content.text.XmlTitleFactory;
import org.openfuxml.util.OfxCommentBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.doc.ofx.AbstractUtilsOfxDocumentationFactory;
import net.sf.ahtutils.doc.ofx.qa.table.OfxQaNfrQuestionTableFactory;
import net.sf.ahtutils.doc.ofx.qa.table.OfxQaNfrResultTableFactory;
import net.sf.ahtutils.xml.status.Translations;
import net.sf.exlp.util.xml.JaxbUtil;

public class OfxQaNfrSectionFactory extends AbstractUtilsOfxDocumentationFactory
{
	final static Logger logger = LoggerFactory.getLogger(OfxQaNfrSectionFactory.class);
	
	private OfxQaNfrQuestionTableFactory ofxTableQuestions;
	private OfxQaNfrResultTableFactory ofxTableAnswers;
	
	public OfxQaNfrSectionFactory(Configuration config, String[] langs, Translations translations)
	{
		super(config,langs,translations);
		imagePathPrefix = config.getString("doc.ofx.imagePathPrefixQA");
		ofxTableQuestions = new OfxQaNfrQuestionTableFactory(config,langs,translations);
		ofxTableAnswers = new OfxQaNfrResultTableFactory(config,langs,translations);
	}
	
	public void setUnits(Container units) {ofxTableQuestions.setUnits(units);}
	
	public Section build(boolean withResults, org.jeesl.model.xml.module.survey.Section mainSection, Survey surveyAnswers, List<Staff> staff) throws OfxAuthoringException
	{
		Section xml = XmlSectionFactory.build();

		xml.getContent().add(XmlTitleFactory.build(mainSection.getDescription().getValue()));
		
		Comment comment = XmlCommentFactory.build();
		OfxCommentBuilder.doNotModify(comment);
		xml.getContent().add(comment);
		
		Map<Long,Map<Long,Answer>> mapAnswers = buildAnswerMap(surveyAnswers);
		
		List<Section> sections = new ArrayList<Section>();
		for(org.jeesl.model.xml.module.survey.Section subSection : mainSection.getSection())
		{
			sections.add(section(withResults,mainSection,subSection,mapAnswers,staff));
		}

		if(sections.size()==1)
		{
			for(Serializable s : sections.get(0).getContent())
			{
				logger.trace(s.getClass().getSimpleName());
				if(!(s instanceof Title)){xml.getContent().add(s);}
			}
		}
		else
		{
			xml.getContent().addAll(sections);
		}
		
		return xml;
	}
	
	private Section section(boolean withResults, org.jeesl.model.xml.module.survey.Section mainSection, org.jeesl.model.xml.module.survey.Section subSection, Map<Long,Map<Long,Answer>> mapAnswers, List<Staff> staff) throws OfxAuthoringException
	{
		Section xml = XmlSectionFactory.build();

		xml.getContent().add(XmlTitleFactory.build(subSection.getDescription().getValue()));
		
		if(subSection.isSetRemark()){xml.getContent().add(XmlParagraphFactory.text(subSection.getRemark().getValue()));}
		if(subSection.isSetQuestion())
		{
			xml.getContent().add(ofxTableQuestions.build(mainSection,subSection));
			xml.getContent().addAll(questionRemarks(subSection));
		}
		
		Table table = ofxTableAnswers.build(subSection,mapAnswers,staff);
		JaxbUtil.trace(table);
		if(withResults && table.isSetContent() && table.getContent().isSetBody() && table.getContent().getBody().get(0).isSetRow())
		{
			xml.getContent().add(table);
		}
		
		return xml;
	}
	
	private Map<Long,Map<Long,Answer>> buildAnswerMap(Survey surveyAnswers)
	{
		Map<Long,Map<Long,Answer>> map = new Hashtable<Long,Map<Long,Answer>>();
		
		for(Answer a : surveyAnswers.getData().get(0).getAnswer())
		{
			long sId = a.getData().getCorrelation().getCorrelation().get(0).getId();
			long qId = a.getQuestion().getId();
			if(!map.containsKey(sId)){map.put(sId, new Hashtable<Long,Answer>());}
			map.get(sId).put(qId, a);
			logger.trace(sId+" "+qId);
		}
		return map;
	}
	
	private List<Paragraph> questionRemarks(org.jeesl.model.xml.module.survey.Section section)
	{
		List<Paragraph> list = new ArrayList<Paragraph>();
		
		for(Question q : section.getQuestion())
		{
			
			if(q.isSetRemark() && Objects.nonNull(q.getRemark()) && q.getRemark().getValue().trim().length()>0)
			{
				JaxbUtil.trace(q);
				StringBuffer sb = new StringBuffer();
				sb.append(" (").append(q.getPosition()).append(") ");
				sb.append(q.getRemark().getValue());
				list.add(XmlParagraphFactory.text(sb.toString()));
			}
		}
		
		return list;
	}
}