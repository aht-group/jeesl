package org.jeesl.factory.json.module.survey;

import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyCondition;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyOption;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.model.json.module.survey.question.JsonOption;
import org.jeesl.model.json.module.survey.question.JsonQuestion;
import org.jeesl.model.json.survey.Condition;
import org.jeesl.model.json.system.status.JsonType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonSurveyConditionFactory<CONDITION extends JeeslSurveyCondition<?,QE,OPTION>,
										QE extends JeeslStatus<?,?,QE>,
										OPTION extends JeeslSurveyOption<?,?>>
{
	final static Logger logger = LoggerFactory.getLogger(JsonSurveyConditionFactory.class);
	
	@SuppressWarnings("unused")
	private final String localeCode;
	private final Condition q;
	
	public JsonSurveyConditionFactory(String localeCode, Condition q)
	{
		this.localeCode=localeCode;
		this.q=q;
	}
	
	public static Condition build() {return new Condition();}
	
	public Condition build(CONDITION ejb)
	{
		Condition json = build();
		
		if(q.getId()!=null) {json.setId(ejb.getId());}
		json.setNegate(ejb.isNegate());
		json.setPosition(ejb.getPosition());
		
		if(q.getTrigger()!=null)
		{
			JsonQuestion trigger = new JsonQuestion();
			trigger.setId(ejb.getTriggerQuestion().getId());
			json.setTrigger(trigger);
		}
		if(q.getOption()!=null)
		{
			JsonOption option = new JsonOption();
			option.setId(ejb.getOption().getId());
			json.setOption(option);
		}
		if(q.getType()!=null)
		{
			JsonType type = new JsonType();
			type.setCode(ejb.getElement().getCode());
			json.setType(type);
		}
		
		return json;
	}
	
}