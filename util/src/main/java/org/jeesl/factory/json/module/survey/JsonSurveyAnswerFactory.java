package org.jeesl.factory.json.module.survey;

import java.util.Objects;

import org.jeesl.interfaces.model.module.survey.data.JeeslSurveyAnswer;
import org.jeesl.interfaces.model.module.survey.data.JeeslSurveyData;
import org.jeesl.interfaces.model.module.survey.data.JeeslSurveyMatrix;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyOption;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.model.json.module.survey.data.JsonAnswer;
import org.jeesl.model.json.module.survey.question.JsonQuestion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonSurveyAnswerFactory<L extends JeeslLang,D extends JeeslDescription,									
									ANSWER extends JeeslSurveyAnswer<L,D,?,MATRIX,DATA,OPTION>,
									MATRIX extends JeeslSurveyMatrix<L,D,ANSWER,OPTION>,
									DATA extends JeeslSurveyData<L,D,?,ANSWER,?>,
									OPTION extends JeeslSurveyOption<L,D>>
{
	final static Logger logger = LoggerFactory.getLogger(JsonSurveyAnswerFactory.class);
	
	private final JsonAnswer q;
	
	private JsonSurveyMatrixFactory<L,D,ANSWER,MATRIX,DATA,OPTION> jfMatrix;
	private JsonSurveyOptionFactory<OPTION> jfOption;
	
	public JsonSurveyAnswerFactory(String localeCode, JsonAnswer q)
	{
		this.q=q;
		
		if(Objects.nonNull(q.getQuestion())) {logger.warn("Upclimbing the ER hierarchy is not supported by desing");}
		if(q.getMatrix()!=null) {jfMatrix = new JsonSurveyMatrixFactory<>(localeCode,q.getMatrix());}
		if(q.getOption()!=null) {jfOption = new JsonSurveyOptionFactory<OPTION>(localeCode,q.getOption());}
	}
	
	public JsonAnswer build(ANSWER answer)
	{
		JsonAnswer json = build();
		if(q.getId()>0) {json.setId(answer.getId());}
		
		if(q.getValueText()!=null && answer.getValueText()!=null) {json.setValueText(answer.getValueText());}
		if(q.getValueBoolean()!=null && answer.getValueBoolean()!=null) {json.setValueBoolean(answer.getValueBoolean());}
		if(q.getValueNumber()!=null && answer.getValueNumber()!=null) {json.setValueNumber(answer.getValueNumber());}
		if(q.getValueDouble()!=null && answer.getValueDouble()!=null) {json.setValueDouble(answer.getValueDouble());}
		
		if(q.getRemark()!=null && answer.getRemark()!=null) {json.setRemark(answer.getRemark());}
		if(q.getOption()!=null && answer.getOption()!=null) {json.setOption(jfOption.build(answer.getOption()));}
		
		if(q.getMatrix()!=null && answer.getQuestion().getShowMatrix()!=null && answer.getQuestion().getShowMatrix())
		{
			json.setMatrix(jfMatrix.build(answer.getMatrix()));
		}
		return json;
	}
	
	public JsonAnswer build(MATRIX matrix)
	{
		JsonAnswer json = build();
		
		if(q.getValueText()!=null && matrix.getValueText()!=null) {json.setValueText(matrix.getValueText());}
		if(q.getValueBoolean()!=null && matrix.getValueBoolean()!=null) {json.setValueBoolean(matrix.getValueBoolean());}
		if(q.getValueNumber()!=null && matrix.getValueNumber()!=null) {json.setValueNumber(matrix.getValueNumber());}
		if(q.getValueDouble()!=null && matrix.getValueDouble()!=null) {json.setValueDouble(matrix.getValueDouble());}
		
		if(q.getOption()!=null && matrix.getOption()!=null) {json.setOption(jfOption.build(matrix.getOption()));}
		
		return json;
	}
	
	public static JsonAnswer build(){return new JsonAnswer();}
	public static JsonAnswer build(JsonQuestion question){JsonAnswer json = build();json.setQuestion(question);return json;}
	
	public static JsonAnswer build(long id)
	{
		JsonAnswer json = build();
		json.setId(id);
		return json;
	}
}