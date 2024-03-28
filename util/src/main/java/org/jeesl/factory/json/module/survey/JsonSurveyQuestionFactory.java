package org.jeesl.factory.json.module.survey;

import java.util.ArrayList;

import org.jeesl.api.facade.module.survey.JeeslSurveyCoreFacade;
import org.jeesl.api.facade.module.survey.JeeslSurveyTemplateFacade;
import org.jeesl.factory.builder.module.survey.SurveyTemplateFactoryBuilder;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurveyScore;
import org.jeesl.interfaces.model.module.survey.data.JeeslSurveyAnswer;
import org.jeesl.interfaces.model.module.survey.data.JeeslSurveyData;
import org.jeesl.interfaces.model.module.survey.data.JeeslSurveyMatrix;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyCondition;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyOption;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyOptionSet;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyQuestion;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyQuestionElement;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyQuestionUnit;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveySection;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyValidation;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyValidationAlgorithm;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.model.json.module.survey.question.JsonOption;
import org.jeesl.model.json.module.survey.question.JsonQuestion;
import org.jeesl.model.json.survey.Condition;
import org.jeesl.model.json.survey.validation.Validation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonSurveyQuestionFactory<L extends JeeslLang,D extends JeeslDescription,
										VALGORITHM extends JeeslSurveyValidationAlgorithm<L,D>,
										SECTION extends JeeslSurveySection<L,D,?,SECTION,QUESTION>,
										QUESTION extends JeeslSurveyQuestion<L,D,SECTION,CONDITION,VALIDATION,QE,SCORE,?,?,OPTION,?>,
										CONDITION extends JeeslSurveyCondition<QUESTION,QE,OPTION>,
										VALIDATION extends JeeslSurveyValidation<L,D,QUESTION,VALGORITHM>,
										QE extends JeeslSurveyQuestionElement<L,D,QE,?>,
										SCORE extends JeeslSurveyScore<L,D,?,QUESTION>,
										
										ANSWER extends JeeslSurveyAnswer<L,D,QUESTION,?,?,OPTION>,
										OPTION extends JeeslSurveyOption<L,D>>
{
	final static Logger logger = LoggerFactory.getLogger(JsonSurveyQuestionFactory.class);
	

	private JeeslSurveyTemplateFacade<?,?,?,?,?,SECTION,QUESTION,QE,SCORE,?,OPTION> fTemplate;
	private SurveyTemplateFactoryBuilder<L,D,?,?,?,?,?,?,?,SECTION,QUESTION,CONDITION,VALIDATION,QE,SCORE,?,?,OPTION> fbTemplate;
	
	private final String localeCode;
	private JsonQuestion q;
	
	private JsonSurveyOptionFactory<OPTION> jfOption;
	private JsonSurveyConditionFactory<CONDITION,QE,OPTION> jfCondition;
	private JsonSurveyValidationFactory<L,D,VALGORITHM,VALIDATION> jfValidation;
	
	public JsonSurveyQuestionFactory(JsonQuestion q) {this(null, q,null,null);}
	public JsonSurveyQuestionFactory(String localeCode, JsonQuestion q) {this(localeCode, q,null,null);}
	public JsonSurveyQuestionFactory(String localeCode, JsonQuestion q,
			SurveyTemplateFactoryBuilder<L,D,?,?,?,?,?,?,?,SECTION,QUESTION,CONDITION,VALIDATION,QE,SCORE,?,?,OPTION> fbTemplate,
			JeeslSurveyTemplateFacade<?,?,?,?,?,SECTION,QUESTION,QE,SCORE,?,OPTION> fTemplate)
	{
		this.localeCode=localeCode;
		this.q=q;
		this.fbTemplate=fbTemplate;
		this.fTemplate=fTemplate;
		if(q.getOptions()!=null && !q.getOptions().isEmpty()) {jfOption = new JsonSurveyOptionFactory<OPTION>(localeCode,q.getOptions().get(0));}
		if(q.getConditions()!=null && !q.getConditions().isEmpty()) {jfCondition = new JsonSurveyConditionFactory<CONDITION,QE,OPTION>(localeCode,q.getConditions().get(0));}
		if(q.getValidations()!=null && !q.getValidations().isEmpty()) {jfValidation = new JsonSurveyValidationFactory<L,D,VALGORITHM,VALIDATION>(localeCode,q.getValidations().get(0));}
	}
	
	public JsonQuestion build(QUESTION ejb)
	{
		JsonQuestion json = build();
		
		if(q.getId()!=null) {json.setId(ejb.getId());}
		if(q.getVisible()!=null) {json.setVisible(ejb.isVisible());}
		if(q.getPosition()!=null) {json.setPosition(ejb.getPosition());}
		if(q.isSetCode()){json.setCode(ejb.getCode());}
		if(q.isSetTopic() && ejb.getName().containsKey(localeCode)){json.setTopic(ejb.getName().get(localeCode).getLang());}
		
		if(q.isSetQuestion() && ejb.getText().containsKey(localeCode)){json.setQuestion(ejb.getText().get(localeCode).getLang());}
		if(q.isSetRemark() && ejb.getDescription().containsKey(localeCode)){json.setRemark(ejb.getDescription().get(localeCode).getLang());}
		
		if(q.isSetCalculateScore()){json.setCalculateScore(ejb.getCalculateScore());}
		if(q.isSetMinScore()){json.setMinScore(ejb.getMinScore());}
		if(q.isSetMaxScore()){json.setMaxScore(ejb.getMaxScore());}
		
		if(q.isSetShowBoolean()){json.setShowBoolean(ejb.getShowBoolean());}
		if(q.isSetShowInteger()){json.setShowInteger(ejb.getShowInteger());}
		if(q.setShowDouble()){json.setShowDouble(ejb.getShowDouble());}
		if(q.isSetShowText()){json.setShowText(ejb.getShowText());}
		if(q.isSetShowScore()){json.setShowScore(ejb.getShowScore());}
		if(q.isSetShowRemark()){json.setShowRemark(ejb.getShowRemark());}
		if(q.getShowDate()!=null){json.setShowDate(ejb.getShowDate());}
		if(q.isSetShowSelectOne()){json.setShowSelectOne(ejb.getShowSelectOne());}
		if(q.isSetShowSelectMulti()){json.setShowSelectMulti(ejb.getShowSelectMulti());}
		if(q.getShowMatrix()!=null){json.setShowMatrix(ejb.getShowMatrix());}
		
		if(q.getMandatory()!=null) {json.setMandatory(ejb.getMandatory());}
		if(q.getShowEmptyOption()!=null) {json.setShowEmptyOption(ejb.getShowEmptyOption());}
		
		if(q.getOptions()!=null && !q.getOptions().isEmpty())
		{
			ejb = fTemplate.loadSurveyQuersion(ejb);
			if(!ejb.getOptions().isEmpty()) {json.setOptions(new ArrayList<JsonOption>());}
			for(OPTION option : ejb.getOptions())
			{
				json.getOptions().add(jfOption.build(option));
			}
		}

		if(q.getCondition()!=null) {json.setCondition(ejb.getRenderCondition());}
		if(q.getConditions()!=null && !q.getConditions().isEmpty() && fTemplate!=null && fbTemplate!=null)
		{
			json.setConditions(new ArrayList<Condition>());
			for(CONDITION condition : fTemplate.allForParent(fbTemplate.getClassCondition(), ejb))
			{
				json.getConditions().add(jfCondition.build(condition));
			}
		}
		if(q.getValidations()!=null && !q.getValidations().isEmpty() && fTemplate!=null && fbTemplate!=null)
		{
			json.setValidations(new ArrayList<Validation>());
			for(VALIDATION validation : fTemplate.allForParent(fbTemplate.getClassValidation(), ejb))
			{
				json.getValidations().add(jfValidation.build(validation));
			}
		}
		
		return json;
	}
	
	public static JsonQuestion build() {return new JsonQuestion();}
	public static JsonQuestion id(long id)
	{
		JsonQuestion json = build();
		json.setId(id);
		return json;
	}
}