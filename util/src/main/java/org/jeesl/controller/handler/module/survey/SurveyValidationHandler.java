package org.jeesl.controller.handler.module.survey;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.exlp.util.io.JsonUtil;
import org.exlp.util.io.StringUtil;
import org.jeesl.api.bean.module.survey.JeeslSurveyCache;
import org.jeesl.controller.util.comparator.ejb.module.survey.SurveyQuestionComparator;
import org.jeesl.interfaces.controller.processor.SurveyValidator;
import org.jeesl.interfaces.model.json.module.survey.SurveyValidatorConfiguration;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurveyTemplate;
import org.jeesl.interfaces.model.module.survey.data.JeeslSurveyAnswer;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyOption;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyQuestion;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveySection;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyValidation;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SurveyValidationHandler<L extends JeeslLang, D extends JeeslDescription,
							TEMPLATE extends JeeslSurveyTemplate<L,D,?,TEMPLATE,?,?,?,SECTION,?,?>,
							SECTION extends JeeslSurveySection<L,D,TEMPLATE,SECTION,QUESTION>,
							QUESTION extends JeeslSurveyQuestion<L,D,SECTION,?,VALIDATION,?,?,?,?,OPTION,?>,
							VALIDATION extends JeeslSurveyValidation<L,D,QUESTION,?>,
							ANSWER extends JeeslSurveyAnswer<L,D,QUESTION,?,?,OPTION>,
							OPTION extends JeeslSurveyOption<L,D>>
	implements Serializable
{
	final static Logger logger = LoggerFactory.getLogger(SurveyValidationHandler.class);
	private static final long serialVersionUID = 1L;
	
	private JeeslSurveyCache<TEMPLATE,SECTION,QUESTION,?,VALIDATION> cache;
	private final Comparator<QUESTION> cpQuestion;
	
	private final List<QUESTION> questions;
//	private final Map<QUESTION,ANSWER> answers;
	private final Map<QUESTION,List<D>> errors; public Map<QUESTION, List<D>> getErrors() {return errors;}
	private final Map<VALIDATION,SurveyValidator> validators; public Map<VALIDATION,SurveyValidator> getValidators() {return validators;}
	private final Map<QUESTION,List<VALIDATION>> validations; public Map<QUESTION,List<VALIDATION>> getValidations() {return validations;}
	private final Map<QUESTION,Set<QUESTION>> triggers; public Map<QUESTION,Set<QUESTION>> getTriggers() {return triggers;}
	
	private boolean debugOnInfo;
	
	public SurveyValidationHandler(
//			SurveyCoreFactoryBuilder<L,D,?,?,?,?,TEMPLATE,?,?,?,SECTION,QUESTION,?,VALIDATION,?,?,?,ANSWER,?,?,?,OPTION,?,?> fbCore,
									JeeslSurveyCache<TEMPLATE,SECTION,QUESTION,?,VALIDATION> cache
									)
	{
		this.cache=cache;
		questions = new ArrayList<QUESTION>();
		validators = new HashMap<VALIDATION,SurveyValidator>();
		validations  = new HashMap<QUESTION,List<VALIDATION>>();
		errors  = new HashMap<QUESTION,List<D>>();
		triggers = new HashMap<QUESTION,Set<QUESTION>>();
		
		cpQuestion = new SurveyQuestionComparator<QUESTION>().factory(SurveyQuestionComparator.Type.position);
		
		debugOnInfo = false;
	}
	
	public void clear()
	{
		questions.clear();
		validations.clear();
		validations.clear();
		errors.clear();
		triggers.clear();
	}
	
	public void init(TEMPLATE template)
	{
		for(SECTION section : cache.getSections(template))
		{
			logger.trace(section.toString());
			for(QUESTION question : cache.getQuestions(section))
			{
				logger.trace("\t"+question.toString());
				initQuestion(question);
			}
		}
		Collections.sort(questions,cpQuestion);
	}
	
	public void initQuestion(QUESTION question)
	{
		questions.add(question);
		List<VALIDATION> list = cache.getValidations(question);
		if(list==null) {logger.warn("THe validation List is null ...");}
		else
		{
			validations.put(question,list);
			for(VALIDATION v : list)
			{
				try
				{
					Class<?> cAlgorithm = Class.forName(v.getAlgorithm().getCode()).asSubclass(SurveyValidator.class);
					if(debugOnInfo) {logger.info("Configuration of "+cAlgorithm.getSimpleName());}
					SurveyValidator validator = (SurveyValidator)cAlgorithm.newInstance();
					
					if(v.getAlgorithm().getConfig()!=null && v.getAlgorithm().getConfig().trim().length()>0)
					{
						Class<?> cConfig = Class.forName(v.getAlgorithm().getConfig()).asSubclass(SurveyValidatorConfiguration.class);
						if(debugOnInfo) {logger.info("Config: "+v.getConfig());}
						SurveyValidatorConfiguration config = (SurveyValidatorConfiguration)JsonUtil.read(cConfig,v.getConfig());
						JsonUtil.info(config);
						validator.init(config);
					}
					
					validators.put(v,validator);
					
				}
				catch (ClassNotFoundException | IOException | InstantiationException | IllegalAccessException e) {e.printStackTrace();}
			}
		}
	}
	
	public void evaluateList(List<ANSWER> answers) 
	{
		errors.clear();
		for(ANSWER a : answers)
		{
			if(validations.containsKey(a.getQuestion()) && !validations.get(a.getQuestion()).isEmpty())
			{
				validate(a);
			}
		}
	}
	
//	public void update(ANSWER answer)
//	{
//		if(debug) {logger.info("Update "+answer.toString()+" for Question:"+answer.getQuestion());}
//		answers.put(answer.getQuestion(),answer);
//		if(debug)
//		{
//			StringBuilder sb = new StringBuilder();
//			sb.append("Updating "+answer.toString());
//			sb.append(" triggering ");
//			if(triggers.containsKey(answer.getQuestion())) {sb.append(triggers.get(answer.getQuestion()).size());}
//			else {sb.append("0");}
//			logger.info(sb.toString());
//		}	
//		
//		if(triggers.containsKey(answer.getQuestion()))
//		{
//			for(QUESTION q : triggers.get(answer.getQuestion()))
//			{
//				
//			}
//		}	
//	}

	private boolean validate(ANSWER answer)
	{
		QUESTION question = answer.getQuestion();
		if(debugOnInfo) {logger.info("Validating Question: "+question.toString());}
		for(VALIDATION v : validations.get(question))
		{
			SurveyValidator validator = validators.get(v);
			boolean validationResult = validator.validate(answer);
			logger.trace(v.getPosition()+" Valid Entry: "+validationResult);
			if(!validationResult)
			{
				if(!errors.containsKey(question)) {errors.put(question,new ArrayList<D>());}
				errors.get(question).add(v.getDescription().get("en"));
			}
		}
		
		return false;
	}
	
	boolean hasErrors; public boolean isHasErrors() {return !errors.isEmpty();}

	public void debug()
	{
		logger.info(StringUtil.stars());
		logger.info("Debugging "+this.getClass().getSimpleName()+" Questions:"+questions.size());
		for(QUESTION q : questions)
		{
			StringBuilder sb = new StringBuilder();
			sb.append("Q: "+q.getCode());
			if(triggers.containsKey(q))
			{
				sb.append(" Triggers: "+triggers.size());
				List<String> l = new ArrayList<String>();
				
				for(QUESTION qt : triggers.get(q)){l.add(qt.getCode());}
				sb.append(" [ ").append(StringUtils.join(l, ",")).append(" ]");
			}
			logger.trace(sb.toString());
		}
	}
}