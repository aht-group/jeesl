package org.jeesl.factory.json.module.survey;

import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyValidation;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyValidationAlgorithm;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.model.json.survey.validation.Validation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonSurveyValidationFactory<L extends JeeslLang,D extends JeeslDescription,
											VALGORITHM extends JeeslSurveyValidationAlgorithm<L,D>,
											VALIDATION extends JeeslSurveyValidation<L,D,?,VALGORITHM>>
{
	final static Logger logger = LoggerFactory.getLogger(JsonSurveyValidationFactory.class);
	
	private final String localeCode;
	private final Validation q;
	
	private JsonSurveyValidationAlgorithmFactory<L,D,VALGORITHM> jfAlgorithm;
	
	public JsonSurveyValidationFactory(String localeCode, Validation q)
	{
		this.localeCode=localeCode;
		this.q=q;
		if(q.getAlgorithm()!=null) {jfAlgorithm = new JsonSurveyValidationAlgorithmFactory<>(localeCode,q.getAlgorithm());}
	}
	
	public static Validation build() {return new Validation();}
	
	public Validation build(VALIDATION ejb)
	{
		Validation json = build();
		
		if(q.getId()!=null) {json.setId(ejb.getId());}
		if(q.getAlgorithm()!=null) {json.setAlgorithm(jfAlgorithm.build(ejb.getAlgorithm()));}
		
		if(q.getConfig()!=null) {json.setConfig(ejb.getConfig());}
		
		if(q.getMessage()!=null) {json.setMessage(ejb.getDescription().get(localeCode).getLang());}
		
		return json;
	}
}