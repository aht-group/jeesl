package org.jeesl.util.query.json;

import java.util.ArrayList;
import java.util.Date;

import org.jeesl.factory.json.module.survey.JsonSurveyConditionFactory;
import org.jeesl.factory.json.module.survey.JsonSurveyOptionFactory;
import org.jeesl.factory.json.module.survey.JsonSurveyQuestionFactory;
import org.jeesl.factory.json.module.survey.JsonSurveySectionFactory;
import org.jeesl.factory.json.module.survey.JsonSurveyValidationAlgorithmFactory;
import org.jeesl.factory.json.module.survey.JsonSurveyValidationFactory;
import org.jeesl.model.json.survey.Answer;
import org.jeesl.model.json.survey.Cell;
import org.jeesl.model.json.survey.Condition;
import org.jeesl.model.json.survey.Matrix;
import org.jeesl.model.json.survey.Option;
import org.jeesl.model.json.survey.Question;
import org.jeesl.model.json.survey.Section;
import org.jeesl.model.json.survey.JsonSurvey;
import org.jeesl.model.json.survey.Template;
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
	
	public static Template templateExport()
	{
		Question trigger = JsonSurveyQuestionFactory.build();
		trigger.setId(0l);
		
		Option triggerOption = JsonSurveyOptionFactory.build();
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
		
		Option option = JsonSurveyOptionFactory.build();
		option.setId(0l);
		option.setPosition(0);
		option.setCode("");
		option.setLabel("");
		option.setDescription("");
		option.setColumn(true);
		option.setRow(true);
		option.setCell(true);
		
		Question question = JsonSurveyQuestionFactory.build();
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
		question.setOptions(new ArrayList<Option>());question.getOptions().add(option);
		
		question.setCondition("");
		question.setConditions(new ArrayList<Condition>());question.getConditions().add(condition);
		question.setValidations(new ArrayList<Validation>());question.getValidations().add(validation);
		
		question.setMandatory(true);
		question.setShowEmptyOption(true);
		
		Section section = JsonSurveySectionFactory.build();
		section.setId(0l);
		section.setCode("");
		section.setName("");
		section.getQuestions().add(question);
		
		Template xml = new Template();
		xml.setId(Long.valueOf(0));
		xml.getSections().add(section);
		
		return xml;
	}
	
	public static Answer answers()
	{		
		Answer json = new Answer();
		json.setId(Long.valueOf(1));
		json.setQuestion(JsonSurveyQuestionFactory.id(1));
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
		Answer answer = new Answer();
		answer.setValueBoolean(true);
		answer.setValueDouble(2d);
		answer.setValueNumber(1);
		answer.setValueText("");
		answer.setOption(JsonSurveyOptionFactory.id(0));
		
		Cell cell = new Cell();
		cell.setColumn(0l);
		cell.setRow(0l);
		cell.setAnswer(answer);
		
		Matrix json = new Matrix();
		json.setCells(new ArrayList<Cell>());
		json.getCells().add(cell);
		return json;
	}
}