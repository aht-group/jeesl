package org.jeesl.factory.xml.module.survey;

import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;
import org.jeesl.api.facade.module.survey.JeeslSurveyCoreFacade;
import org.jeesl.factory.xml.system.lang.XmlDescriptionFactory;
import org.jeesl.factory.xml.system.util.text.XmlRemarkFactory;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurveyScheme;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurveyScore;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyOption;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyOptionSet;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyQuestion;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyQuestionElement;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyQuestionUnit;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveySection;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.model.xml.module.survey.Section;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlSectionFactory<L extends JeeslLang,D extends JeeslDescription,
								SCHEME extends JeeslSurveyScheme<L,D,?,SCORE>,
								SECTION extends JeeslSurveySection<L,D,?,SECTION,QUESTION>,
								QUESTION extends JeeslSurveyQuestion<L,D,SECTION,?,?,QE,SCORE,UNIT,OPTIONS,OPTION,?>,
								QE extends JeeslSurveyQuestionElement<L,D,QE,?>,
								SCORE extends JeeslSurveyScore<L,D,SCHEME,QUESTION>,
								UNIT extends JeeslSurveyQuestionUnit<L,D,UNIT,?>,
								OPTIONS extends JeeslSurveyOptionSet<L,D,?,OPTION>,
								OPTION extends JeeslSurveyOption<L,D>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlSectionFactory.class);
		
	private JeeslSurveyCoreFacade<L,D,?,?,?,SECTION,QUESTION,?,?,?,?> fSurvey;
	
	private final String localeCode;
	private final Section q;
	
	private XmlSectionFactory<L,D,SCHEME,SECTION,QUESTION,QE,SCORE,UNIT,OPTIONS,OPTION> xfSection;
	private XmlQuestionFactory<L,D,SCHEME,QUESTION,QE,SCORE,UNIT,OPTIONS,OPTION> xfQuestion;
	
	public XmlSectionFactory(String localeCode, Section q)
	{
		this.localeCode=localeCode;
		this.q=q;
		
		if(ObjectUtils.isNotEmpty(q.getSection())) {xfSection = new XmlSectionFactory<>(localeCode,q.getSection().get(0));}
		if(ObjectUtils.isNotEmpty(q.getQuestion())) {xfQuestion = new XmlQuestionFactory<>(localeCode,q.getQuestion().get(0));}
	}
	
	public void lazyLoad(JeeslSurveyCoreFacade<L,D,?,?,?,SECTION,QUESTION,?,?,?,?> fSurvey)
	{
		this.fSurvey=fSurvey;
		if(Objects.nonNull(xfSection)) {xfSection.lazyLoad(fSurvey);}
		if(xfQuestion!=null) {xfQuestion.lazyLoad(fSurvey);}
	}
	
	public Section build(SECTION ejb)
	{
		if(fSurvey!=null){ejb = fSurvey.load(ejb);}
		Section xml = new Section();
		if(Objects.nonNull(q.getId())) {xml.setId(ejb.getId());}
		if(Objects.nonNull(q.getCode())) {xml.setCode(ejb.getCode());}
		if(Objects.nonNull(q.getPosition())) {xml.setPosition(ejb.getPosition());}
		if(Objects.nonNull(q.isVisible())) {xml.setVisible(ejb.isVisible());}
		
		if(Objects.nonNull(q.getDescription())) {xml.setDescription(XmlDescriptionFactory.build(ejb.getName().get(localeCode).getLang()));}
		if(ObjectUtils.allNotNull(q.getRemark(),ejb.getRemark())) {xml.setRemark(XmlRemarkFactory.build(ejb.getRemark()));}
		
		if(ObjectUtils.isNotEmpty(q.getQuestion()))
		{
			for(QUESTION question : ejb.getQuestions())
			{
				xml.getQuestion().add(xfQuestion.build(question));
			}
		}
		
		if(ObjectUtils.isNotEmpty(q.getSection()))
		{
			for(SECTION section : ejb.getSections())
			{
				xml.getSection().add(xfSection.build(section));
			}
		}
		
		return xml;
	}
}