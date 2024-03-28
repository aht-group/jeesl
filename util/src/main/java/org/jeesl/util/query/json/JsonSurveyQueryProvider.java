package org.jeesl.util.query.json;

import java.util.ArrayList;
import java.util.Date;

import org.jeesl.factory.json.module.survey.JsonSurveyConditionFactory;
import org.jeesl.factory.json.module.survey.JsonSurveyOptionFactory;
import org.jeesl.factory.json.module.survey.JsonSurveyQuestionFactory;
import org.jeesl.factory.json.module.survey.JsonSurveySectionFactory;
import org.jeesl.factory.json.module.survey.JsonSurveyValidationAlgorithmFactory;
import org.jeesl.factory.json.module.survey.JsonSurveyValidationFactory;
import org.jeesl.model.json.module.survey.data.JsonAnswer;
import org.jeesl.model.json.module.survey.data.JsonCell;
import org.jeesl.model.json.module.survey.data.JsonSurvey;
import org.jeesl.model.json.module.survey.question.JsonOption;
import org.jeesl.model.json.module.survey.question.JsonQuestion;
import org.jeesl.model.json.module.survey.question.JsonSection;
import org.jeesl.model.json.survey.Condition;
import org.jeesl.model.json.survey.Matrix;
import org.jeesl.model.json.survey.JsonTemplate;
import org.jeesl.model.json.survey.validation.Validation;
import org.jeesl.model.json.survey.validation.ValidationAlgorithm;
import org.jeesl.model.json.system.status.JsonType;

public class JsonSurveyQueryProvider
{
	public static JsonSurvey survey()
	{				
		JsonSurvey json = new JsonSurvey();
		json.setId(Long.valueOf(1));
		json.setLabel("");
		json.setDateStart(new Date());
		json.setDateEnd(new Date());
		json.setStatus(JsonStatusQueryProvider.statusLabel());
		return json;
	}
	
	public static JsonTemplate templateExport()
	{
		JsonQuestion trigger = JsonSurveyQuestionFactory.build();
		trigger.setId(0l);
		
		JsonOption triggerOption = JsonSurveyOptionFactory.build();
		triggerOption.setId(0l);
		
		JsonType type = new JsonType();
		
		ValidationAlgorithm algorithm = JsonSurveyValidationAlgorithmFactory.build();
		algorithm.setId(0l);
		algorithm.setCode("");
		algorithm.setConfig("");
		
		Validation validation = JsonSurveyValidationFactory.build();
		validation.setId(0l);
		validation.setAlgorithm(algorithm);
		validation.setConfig("");
		validation.setMessage("");
		
		Condition condition = JsonSurveyConditionFactory.build();
		condition.setId(0l);
		condition.setTrigger(trigger);
		condition.setOption(triggerOption);
		condition.setType(type);
		
		JsonOption option = JsonSurveyOptionFactory.build();
		option.setId(0l);
		option.setPosition(0);
		option.setCode("");
		option.setLabel("");
		option.setDescription("");
		option.setColumn(true);
		option.setRow(true);
		option.setCell(true);
		
		JsonQuestion question = JsonSurveyQuestionFactory.build();
		question.setId(0l);
		question.setVisible(true);
		question.setPosition(0);
		question.setCode("");
		question.setTopic("");
		question.setQuestion("");
		question.setRemark("");
		question.setCalculateScore(true);
		question.setMinScore(0d);
		question.setMaxScore(0d);
		question.setShowBoolean(true);
		question.setShowInteger(true);
		question.setShowDouble(true);
		question.setShowDate(true);
		question.setShowText(true);
		question.setShowScore(true);
		question.setShowRemark(true);
		question.setShowSelectOne(true);
		question.setShowSelectMulti(true);
		question.setShowMatrix(true);
		question.setOptions(new ArrayList<JsonOption>());question.getOptions().add(option);
		
		question.setCondition("");
		question.setConditions(new ArrayList<Condition>());question.getConditions().add(condition);
		question.setValidations(new ArrayList<Validation>());question.getValidations().add(validation);
		
		question.setMandatory(true);
		question.setShowEmptyOption(true);
		
		JsonSection section = JsonSurveySectionFactory.build();
		section.setId(0l);
		section.setCode("");
		section.setName("");
		section.getQuestions().add(question);
		
		JsonTemplate xml = new JsonTemplate();
		xml.setId(Long.valueOf(0));
		xml.getSections().add(section);
		
		return xml;
	}
	
	public static JsonAnswer answers()
	{		
		JsonAnswer json = new JsonAnswer();
		json.setId(Long.valueOf(1));
		json.setValueBoolean(true);
		json.setValueDouble(2d);
		json.setValueNumber(1);
		json.setValueText("");
		json.setRemark("");
		json.setMatrix(matrix());
		json.setOption(JsonSurveyOptionFactory.id(0));
		return json;
	}
	
	private static Matrix matrix()
	{
		JsonAnswer answer = new JsonAnswer();
		answer.setValueBoolean(true);
		answer.setValueDouble(2d);
		answer.setValueNumber(1);
		answer.setValueText("");
		answer.setOption(JsonSurveyOptionFactory.id(0));
		
		JsonCell cell = new JsonCell();
		cell.setColumn(0l);
		cell.setRow(0l);
		cell.setAnswer(answer);
		
		Matrix json = new Matrix();
		json.setCells(new ArrayList<JsonCell>());
		json.getCells().add(cell);
		return json;
	}
}