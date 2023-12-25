package org.jeesl.factory.ejb.module.survey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeesl.api.facade.module.survey.JeeslSurveyCoreFacade;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.model.module.survey.data.JeeslSurveyAnswer;
import org.jeesl.interfaces.model.module.survey.data.JeeslSurveyData;
import org.jeesl.interfaces.model.module.survey.data.JeeslSurveyMatrix;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyOption;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyQuestion;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveySection;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.model.xml.module.survey.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbSurveyAnswerFactory<SECTION extends JeeslSurveySection<?,?,?,SECTION,QUESTION>,
									QUESTION extends JeeslSurveyQuestion<?,?,SECTION,?,?,?,?,?,?,OPTION,?>,
									ANSWER extends JeeslSurveyAnswer<?,?,QUESTION,MATRIX,DATA,OPTION>,
									MATRIX extends JeeslSurveyMatrix<?,?,ANSWER,OPTION>,
									DATA extends JeeslSurveyData<?,?,?,ANSWER,?>,
									OPTION extends JeeslSurveyOption<?,?>
			>
{
	final static Logger logger = LoggerFactory.getLogger(EjbSurveyAnswerFactory.class);
	
	private final Class<QUESTION> cQuestion;
	private final Class<ANSWER> cAnswer;
	private final Class<OPTION> cOption;

	public EjbSurveyAnswerFactory(final Class<QUESTION> cQuestion, final Class<ANSWER> cAnswer, final Class<OPTION> cOption)
	{       
		this.cQuestion = cQuestion;
        this.cAnswer = cAnswer;
        this.cOption = cOption;
	}
   	
	public ANSWER build(QUESTION question, DATA data, Answer answer)
	{
		Boolean valueBoolean = null;if(answer.isSetValueBoolean()){valueBoolean=answer.isValueBoolean();}
		Integer valueNumber = null;if(answer.isSetValueNumber()){valueNumber=answer.getValueNumber();}
		return build(question, data,valueBoolean,valueNumber);
	}
	public ANSWER build(QUESTION question, DATA data)
	{
		return build(question,data,null,null);
	}
	
	public ANSWER build(QUESTION question, DATA data, Boolean valueBoolean, Integer valueNumber)
	{
		ANSWER ejb = null;
		try
		{
			ejb = cAnswer.newInstance();
			ejb.setQuestion(question);
			ejb.setData(data);
			ejb.setValueBoolean(valueBoolean);
			ejb.setValueNumber(valueNumber);
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		
		return ejb;
	}
	
	@Deprecated
	public static <QUESTION extends JeeslSurveyQuestion<?,?,?,?,?,?,?,?,?,OPTION,?>,
					ANSWER extends JeeslSurveyAnswer<?,?,QUESTION,MATRIX,DATA,OPTION>,
					MATRIX extends JeeslSurveyMatrix<?,?,ANSWER,OPTION>,
					DATA extends JeeslSurveyData<?,?,?,ANSWER,?>,
					OPTION extends JeeslSurveyOption<?,?>
					>
			Map<QUESTION,ANSWER> toQuestionMap(List<ANSWER> list)
	{
		Map<QUESTION,ANSWER> map = new HashMap<QUESTION,ANSWER>();
		for(ANSWER a : list){map.put(a.getQuestion(),a);}
		return map;
	}
	
	public Map<QUESTION,ANSWER> toMapQuestion(List<ANSWER> list)
	{
		Map<QUESTION,ANSWER> map = new HashMap<>();
		for(ANSWER a : list){map.put(a.getQuestion(),a);}
		return map;
	}
	
	public static <L extends JeeslLang, D extends JeeslDescription,
					QUESTION extends JeeslSurveyQuestion<L,D,?,?,?,?,?,?,?,OPTION,?>,
					ANSWER extends JeeslSurveyAnswer<L,D,QUESTION,MATRIX,DATA,OPTION>,
					MATRIX extends JeeslSurveyMatrix<L,D,ANSWER,OPTION>,
					DATA extends JeeslSurveyData<L,D,?,ANSWER,?>,
					OPTION extends JeeslSurveyOption<L,D>
					>
		Map<Long,ANSWER> toQuestionIdMap(List<ANSWER> list)
	{
		Map<Long,ANSWER> map = new HashMap<Long,ANSWER>();
		for(ANSWER a : list){map.put(a.getQuestion().getId(),a);}
		return map;
	}
	
	public Map<DATA,List<ANSWER>> toDataMap(List<ANSWER> answers)
	{
		Map<DATA,List<ANSWER>> map = new HashMap<DATA,List<ANSWER>>();
		for(ANSWER a : answers)
		{
			if(!map.containsKey(a.getData())) {map.put(a.getData(), new ArrayList<ANSWER>());}
			map.get(a.getData()).add(a);
		}
		return map;
	}
	
	public boolean belongsToSection(ANSWER answer, SECTION section, boolean defaultResult)
	{
		if(section==null){return defaultResult;}
		{
			return answer.getQuestion().getSection().equals(section);
		}
	}
	
	public void update(JeeslSurveyCoreFacade<?,?,?,?,?,?,?,?,?,?,SECTION,QUESTION,?,?,?,ANSWER,MATRIX,DATA,?,OPTION,?> fSurvey,
						org.jeesl.model.json.survey.Answer json,
						DATA data,
						Map<Long,ANSWER> map)
	{
		try
		{
			ANSWER ejb = null;
			if(map.containsKey(json.getQuestion().getId()))
			{
				ejb = map.get(json.getQuestion().getId());
			}
			else if(json.getId()!=null && json.getId()>0)
			{
				ejb = fSurvey.find(cAnswer,json.getId());
			}
			else
			{
				QUESTION question = fSurvey.find(cQuestion,json.getQuestion().getId());
				ejb = this.build(question, data);
			}
			
			ejb.setValueText(json.getValueText());
			ejb.setValueBoolean(json.getValueBoolean());
			ejb.setValueNumber(json.getValueNumber());
			ejb.setValueDouble(json.getValueDouble());
			
			if(json.getOption()!=null && json.getOption().getId()!=null && json.getOption().getId()>0) 
			{
				ejb.setOption(fSurvey.find(cOption,json.getOption().getId()));
			}
			else{ejb.setOption(null);}
			
			fSurvey.save(ejb);
		}
		catch (JeeslNotFoundException e) {e.printStackTrace();}
		catch (JeeslConstraintViolationException e) {e.printStackTrace();}
		catch (JeeslLockingException e) {e.printStackTrace();}
	}
}