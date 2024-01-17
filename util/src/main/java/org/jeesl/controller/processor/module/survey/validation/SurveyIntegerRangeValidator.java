package org.jeesl.controller.processor.module.survey.validation;

import org.jeesl.interfaces.controller.processor.SurveyValidator;
import org.jeesl.interfaces.model.json.module.survey.SurveyValidatorConfiguration;
import org.jeesl.interfaces.model.module.survey.simple.JeeslSurveySimpleAnswer;
import org.jeesl.model.json.survey.validation.config.JsonValidationIntegerRange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SurveyIntegerRangeValidator implements SurveyValidator
{
	final static Logger logger = LoggerFactory.getLogger(SurveyIntegerRangeValidator.class);
	public static final long serialVersionUID=1;

	private JsonValidationIntegerRange config;
	
	public SurveyIntegerRangeValidator()
	{
		
	}
	
	@Override
	public void init(SurveyValidatorConfiguration config)
	{
		if(config instanceof JsonValidationIntegerRange) {this.config = (JsonValidationIntegerRange)config;}
		else {logger.warn("Config has wrong type");}
		
	}
	
	@Override public boolean validate(JeeslSurveySimpleAnswer answer)
	{
		if(config.getMin()!=null)
		{
			if(answer.getValueNumber()==null) {return false;}
			else if(answer.getValueNumber()<config.getMin())
			{
				logger.info("Min:"+config.getMin()+" Value:"+answer.getValueNumber());
				return false;
			}
		}
		if(config.getMax()!=null)
		{
			if(answer.getValueNumber()==null) {return false;}
			else if(answer.getValueNumber()>config.getMax())
			{
				logger.info("Max:"+config.getMin()+" Value:"+answer.getValueNumber());
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