package org.jeesl.controller.processor.module.survey.validation;

import org.jeesl.interfaces.controller.processor.SurveyValidator;
import org.jeesl.interfaces.model.json.module.survey.SurveyValidatorConfiguration;
import org.jeesl.interfaces.model.module.survey.simple.JeeslSurveySimpleAnswer;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SurveyDateNotInFutureValidator implements SurveyValidator
{
	final static Logger logger = LoggerFactory.getLogger(SurveyDateNotInFutureValidator.class);
	public static final long serialVersionUID=1;
	
	public SurveyDateNotInFutureValidator()
	{
		
	}
	
	@Override
	public void init(SurveyValidatorConfiguration config)
	{
		
		
	}
	
	@Override public boolean validate(JeeslSurveySimpleAnswer answer)
	{
		DateTime dtNow = new DateTime().withTimeAtStartOfDay();
		
		if(answer.getValueDate()!=null)
		{
			DateTime dtAnswer = new DateTime(answer.getValueDate());
			if(dtAnswer.isAfter(dtNow))
			{
				return false;
			}
		}
		
		return true;
	}
	
	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();	
		return sb.toString();
	}	
}