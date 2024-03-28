package org.jeesl.factory.ejb.module.survey;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.ObjectUtils;
import org.jeesl.api.facade.module.survey.JeeslSurveyCoreFacade;
import org.jeesl.api.facade.module.survey.JeeslSurveyTemplateFacade;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyOption;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyOptionSet;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyQuestion;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyQuestionUnit;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveySection;
import org.jeesl.model.xml.module.survey.Question;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbSurveyQuestionFactory<SECTION extends JeeslSurveySection<?,?,?,SECTION,QUESTION>,
										QUESTION extends JeeslSurveyQuestion<?,?,SECTION,?,?,?,?,UNIT,OPTIONS,OPTION,?>,
										UNIT extends JeeslSurveyQuestionUnit<?,?,UNIT,?>,
										OPTIONS extends JeeslSurveyOptionSet<?,?,?,OPTION>,
										OPTION extends JeeslSurveyOption<?,?>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbSurveyQuestionFactory.class);
	
	final Class<QUESTION> cQuestion;
    
	private JeeslSurveyTemplateFacade<?,?,?,?,?,SECTION,QUESTION,?,OPTIONS,OPTION> fTemplate;
	
	public EjbSurveyQuestionFactory<SECTION,QUESTION,UNIT,OPTIONS,OPTION> facade(JeeslSurveyTemplateFacade<?,?,?,?,?,SECTION,QUESTION,?,OPTIONS,OPTION> fTemplate) {this.fTemplate=fTemplate; return this;}
	
	public static <SECTION extends JeeslSurveySection<?,?,?,SECTION,QUESTION>,
					QUESTION extends JeeslSurveyQuestion<?,?,SECTION,?,?,?,?,UNIT,OPTIONS,OPTION,?>,
					UNIT extends JeeslSurveyQuestionUnit<?,?,UNIT,?>,
					OPTIONS extends JeeslSurveyOptionSet<?,?,?,OPTION>,
					OPTION extends JeeslSurveyOption<?,?>>
				EjbSurveyQuestionFactory<SECTION,QUESTION,UNIT,OPTIONS,OPTION> instance(final Class<QUESTION> cQuestion) {return new EjbSurveyQuestionFactory<>(cQuestion);}
	private EjbSurveyQuestionFactory(final Class<QUESTION> cQuestion)
	{
		this.cQuestion = cQuestion;
	}
	    
	public QUESTION build(SECTION section, UNIT unit, Question xQuestion)
	{
		return build(section,unit,
				xQuestion.getCode(),
				xQuestion.getPosition(),
				xQuestion.getTopic(),
				xQuestion.getQuestion().getValue(),
				xQuestion.getRemark().getValue());
	}
	
	public QUESTION build(SECTION section){return build(section,null,null,0,null,null,null);}
	public QUESTION build(SECTION section,UNIT unit){return build(section,unit,null,0,null,null,null);}
	
	public QUESTION build(SECTION section, UNIT unit, String code,int position,String topic,String question,String remark)
	{
		QUESTION ejb = id(0);
		ejb.setRendered(true);
		ejb.setSection(section);
		ejb.setUnit(unit);
		ejb.setCode(code);
		ejb.setPosition(position);
		ejb.setTopic(topic);
		ejb.setQuestion(question);
		ejb.setRemark(remark);
		ejb.setMandatory(false);
		ejb.setShowEmptyOption(false);
		
		return ejb;
	}
	
	public QUESTION id(long id)
	{
		QUESTION ejb = null;
		try
		{
			ejb = cQuestion.newInstance();
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		
		return ejb;
	}
	
	public List<OPTION> toOptions(QUESTION question)
	{
		if(ObjectUtils.isNotEmpty(fTemplate)) {question = fTemplate.loadSurveyQuersion(question);}
		if(question.getOptionSet()==null) {return question.getOptions();}
		else
		{
			OPTIONS set = question.getOptionSet();
			if(ObjectUtils.isNotEmpty(fTemplate)) {set = fTemplate.loadSurveyOptions(set);}
			return set.getOptions();
		}
	}
	
	public List<QUESTION> toSectionQuestions(SECTION section, List<QUESTION> questions)
	{
		List<QUESTION> list = new ArrayList<QUESTION>();
		
		for(QUESTION q : questions)
		{
			if(q.getSection().equals(section))
			{
				list.add(q);
			}
		}
		return list;
	}
	
	public List<SECTION> toSection(List<QUESTION> questions)
	{
		Set<SECTION> set = new HashSet<SECTION>();
		for(QUESTION question : questions)
		{
			set.add(question.getSection());
		}
		return new ArrayList<SECTION>(set);
	}
}