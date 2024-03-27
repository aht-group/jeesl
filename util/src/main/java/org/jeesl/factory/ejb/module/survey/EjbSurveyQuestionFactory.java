package org.jeesl.factory.ejb.module.survey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jeesl.api.facade.module.survey.JeeslSurveyCoreFacade;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyOption;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyOptionSet;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyQuestion;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyQuestionElement;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyQuestionUnit;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveySection;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.model.xml.module.survey.Question;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbSurveyQuestionFactory<L extends JeeslLang, D extends JeeslDescription,
				SECTION extends JeeslSurveySection<L,D,?,SECTION,QUESTION>,
				QUESTION extends JeeslSurveyQuestion<L,D,SECTION,?,?,QE,?,UNIT,OPTIONS,OPTION,?>,
				QE extends JeeslSurveyQuestionElement<L,D,QE,?>,
				UNIT extends JeeslSurveyQuestionUnit<L,D,UNIT,?>,
				OPTIONS extends JeeslSurveyOptionSet<L,D,?,OPTION>,
				OPTION extends JeeslSurveyOption<L,D>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbSurveyQuestionFactory.class);
	
	final Class<QUESTION> cQuestion;
    
	private JeeslSurveyCoreFacade<L,D,?,?,?,?,?,?,?,SECTION,QUESTION,?,?,?,?,OPTIONS,OPTION,?> fSurvey;
	
	public EjbSurveyQuestionFactory(final Class<QUESTION> cQuestion){this(null,cQuestion);}
	
	public EjbSurveyQuestionFactory(JeeslSurveyCoreFacade<L,D,?,?,?,?,?,?,?,SECTION,QUESTION,?,?,?,?,OPTIONS,OPTION,?> fSurvey, final Class<QUESTION> cQuestion)
	{
		this.fSurvey = fSurvey;
        this.cQuestion = cQuestion;
	}
	    
	public QUESTION build(SECTION section,UNIT unit, Question xQuestion)
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
	
	public QUESTION build(SECTION section,UNIT unit, String code,int position,String topic,String question,String remark)
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
		if(fSurvey!=null) {question = fSurvey.load(question);}
		if(question.getOptionSet()==null) {return question.getOptions();}
		else
		{
			OPTIONS set = question.getOptionSet();
			if(fSurvey!=null) {set = fSurvey.load(set);}
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
	
	public Map<SECTION,List<QUESTION>> loadMap(JeeslSurveyCoreFacade<L,D,?,?,?,?,?,?,?,SECTION,QUESTION,?,?,?,?,OPTIONS,OPTION,?> fSurvey)
	{
		Map<SECTION,List<QUESTION>> map = new HashMap<SECTION,List<QUESTION>>();
		for(QUESTION q : fSurvey.allOrderedPosition(cQuestion))
		{
			if(!map.containsKey(q.getSection())){map.put(q.getSection(),new ArrayList<QUESTION>());}
			map.get(q.getSection()).add(q);
		}
		return map;
	}
}