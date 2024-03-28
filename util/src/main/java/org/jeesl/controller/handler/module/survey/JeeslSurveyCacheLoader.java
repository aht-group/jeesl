package org.jeesl.controller.handler.module.survey;

import java.util.List;

import org.jeesl.api.bean.module.survey.JeeslSurveyCache;
import org.jeesl.api.facade.module.survey.JeeslSurveyTemplateFacade;
import org.jeesl.factory.builder.module.survey.SurveyTemplateFactoryBuilder;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurveyTemplate;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyCondition;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyOption;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyQuestion;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveySection;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslSurveyCacheLoader <TEMPLATE extends JeeslSurveyTemplate<?,?,?,TEMPLATE,?,?,?,SECTION,?,?>,
									SECTION extends JeeslSurveySection<?,?,TEMPLATE,SECTION,QUESTION>,
									QUESTION extends JeeslSurveyQuestion<?,?,SECTION,CONDITION,VALIDATION,?,?,?,?,OPTION,?>,
									CONDITION extends JeeslSurveyCondition<QUESTION,?,OPTION>,
									VALIDATION extends JeeslSurveyValidation<?,?,QUESTION,?>,
									OPTION extends JeeslSurveyOption<?,?>>
			implements JeeslSurveyCache<TEMPLATE,SECTION,QUESTION,CONDITION,VALIDATION>
{
	final static Logger logger = LoggerFactory.getLogger(JeeslSurveyCacheLoader.class);
	
	private final JeeslSurveyTemplateFacade<?,TEMPLATE,?,?,?,SECTION,QUESTION,?,?,?,OPTION> fTemplate;
	
	private final SurveyTemplateFactoryBuilder<?,?,?,?,?,TEMPLATE,?,?,?,SECTION,QUESTION,CONDITION,VALIDATION,?,?,?,?,OPTION> fbTemplate;
	
	public JeeslSurveyCacheLoader(SurveyTemplateFactoryBuilder<?,?,?,?,?,TEMPLATE,?,?,?,SECTION,QUESTION,CONDITION,VALIDATION,?,?,?,?,OPTION> fbTemplate,
									JeeslSurveyTemplateFacade<?,TEMPLATE,?,?,?,SECTION,QUESTION,?,?,?,OPTION> fTemplate)
	{
		this.fbTemplate=fbTemplate;
//		this.fbCore=fbCore;
		
		this.fTemplate=fTemplate;
	}
	
	@Override public List<SECTION> getSections(TEMPLATE template)
	{
		TEMPLATE t = fTemplate.load(template, false, false);
		return t.getSections();
	}

	@Override public List<CONDITION> getConditions(QUESTION question)
	{
		return fTemplate.allForParent(fbTemplate.getClassCondition(), question);
	}
	
	@Override public List<VALIDATION> getValidations(QUESTION question)
	{
		return fTemplate.allForParent(fbTemplate.getClassValidation(), question);
	}

	@Override public List<QUESTION> getQuestions(SECTION section)
	{
		return fTemplate.allForParent(fbTemplate.getClassQuestion(), section);
	}
}