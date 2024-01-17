package org.jeesl.controller.processor.module.survey.validation;

import org.jeesl.interfaces.controller.processor.SurveyValidator;
import org.jeesl.interfaces.model.json.module.survey.SurveyValidatorConfiguration;
import org.jeesl.interfaces.model.module.survey.simple.JeeslSurveySimpleAnswer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SurveyDoubleRangeValidator implements SurveyValidator
{
	final static Logger logger = LoggerFactory.getLogger(SurveyDoubleRangeValidator.class);
	public static final long serialVersionUID=1;

	@Override
	public void init(SurveyValidatorConfiguration config)
	{
		logger.info("NYI");
	}
	
	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();	
		return sb.toString();
	}

	@Override
	public boolean validate(JeeslSurveySimpleAnswer answer)
	{
		// TODO Auto-generated method stub
		return false;
	}

	
}