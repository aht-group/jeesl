package org.jeesl.factory.json.module.survey;

import java.util.Objects;

import org.jeesl.api.facade.module.survey.JeeslSurveyTemplateFacade;
import org.jeesl.factory.builder.module.survey.SurveyTemplateFactoryBuilder;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurvey;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurveyScheme;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurveyScore;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurveyTemplate;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurveyTemplateCategory;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurveyTemplateStatus;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurveyTemplateVersion;
import org.jeesl.interfaces.model.module.survey.data.JeeslSurveyAnswer;
import org.jeesl.interfaces.model.module.survey.data.JeeslSurveyData;
import org.jeesl.interfaces.model.module.survey.data.JeeslSurveyMatrix;
import org.jeesl.interfaces.model.module.survey.data.JeeslSurveyStatus;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyCondition;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyOption;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyOptionSet;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyQuestion;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyQuestionType;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyQuestionUnit;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveySection;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyValidation;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyValidationAlgorithm;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.model.json.module.survey.question.JsonSection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonSurveySectionFactory<L extends JeeslLang,D extends JeeslDescription,
				SURVEY extends JeeslSurvey<L,D,SS,TEMPLATE,DATA>,
				SS extends JeeslSurveyStatus<L,D,SS,?>,
				SCHEME extends JeeslSurveyScheme<L,D,TEMPLATE,SCORE>,
				ALGORITHM extends JeeslSurveyValidationAlgorithm<L,D>,
				TEMPLATE extends JeeslSurveyTemplate<L,D,SCHEME,TEMPLATE,VERSION,TS,TC,SECTION,OPTIONS,?>,
				VERSION extends JeeslSurveyTemplateVersion<L,D,TEMPLATE>,
				TS extends JeeslSurveyTemplateStatus<L,D,TS,?>,
				TC extends JeeslSurveyTemplateCategory<L,D,TC,?>,
				SECTION extends JeeslSurveySection<L,D,TEMPLATE,SECTION,QUESTION>,
				QUESTION extends JeeslSurveyQuestion<L,D,SECTION,CONDITION,VALIDATION,QE,SCORE,UNIT,OPTIONS,OPTION,?>,
				CONDITION extends JeeslSurveyCondition<QUESTION,QE,OPTION>,
				VALIDATION extends JeeslSurveyValidation<L,D,QUESTION,ALGORITHM>,
				QE extends JeeslSurveyQuestionType<L,D,QE,?>,
				SCORE extends JeeslSurveyScore<L,D,SCHEME,QUESTION>,
				UNIT extends JeeslSurveyQuestionUnit<L,D,UNIT,?>,
				ANSWER extends JeeslSurveyAnswer<L,D,QUESTION,MATRIX,DATA,OPTION>,
				MATRIX extends JeeslSurveyMatrix<L,D,ANSWER,OPTION>,DATA extends JeeslSurveyData<L,D,SURVEY,ANSWER,?>,
				OPTIONS extends JeeslSurveyOptionSet<L,D,TEMPLATE,OPTION>,OPTION extends JeeslSurveyOption<L,D>>
{
	final static Logger logger = LoggerFactory.getLogger(JsonSurveySectionFactory.class);
	
	private final String localeCode;
	private final JsonSection q;
	
	private JeeslSurveyTemplateFacade<SCHEME,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,SCORE,OPTIONS,OPTION> fTemplate;
	
	private JsonSurveyQuestionFactory<L,D,ALGORITHM,QUESTION,CONDITION,VALIDATION,QE,SCORE,OPTION> jfQuestion;
	
	public JsonSurveySectionFactory(String localeCode, JsonSection q){this(localeCode,q,null,null);}
	public JsonSurveySectionFactory(String localeCode, JsonSection q,
			SurveyTemplateFactoryBuilder<L,D,?,?,?,?,?,?,?,SECTION,QUESTION,CONDITION,VALIDATION,QE,SCORE,UNIT,OPTIONS,OPTION> fbTemplate,
			JeeslSurveyTemplateFacade<SCHEME,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,SCORE,OPTIONS,OPTION> fTemplate)
	{
		this.localeCode=localeCode;
		this.q=q;
		this.fTemplate=fTemplate;

		if(!q.getQuestions().isEmpty()) {jfQuestion = new JsonSurveyQuestionFactory<>(localeCode,q.getQuestions().get(0),fbTemplate,fTemplate);}
	}
	
	public JsonSection build(SECTION ejb)
	{
		if(Objects.nonNull(fTemplate)) {ejb = fTemplate.loadSurveySection(ejb);}
		JsonSection json = build();
		
		json.setId(ejb.getId());
		if(q.isSetCode()){json.setCode(ejb.getCode());}
		if(q.isSetName()){json.setName(ejb.getName().get(localeCode).getLang());}
		
		for(QUESTION q : ejb.getQuestions())
		{
			if(q.isVisible())
			{
				json.getQuestions().add(jfQuestion.build(q));
			}
		}
		
		return json;
	}
	
	public static JsonSection build() {return new JsonSection();}
	public static JsonSection id(long id)
	{
		JsonSection json = build();
		json.setId(id);
		return json;
	}
}