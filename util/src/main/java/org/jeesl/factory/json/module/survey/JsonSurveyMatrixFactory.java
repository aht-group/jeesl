package org.jeesl.factory.json.module.survey;

import java.util.ArrayList;
import java.util.List;

import org.jeesl.interfaces.model.module.survey.core.JeeslSurveyScore;
import org.jeesl.interfaces.model.module.survey.data.JeeslSurveyAnswer;
import org.jeesl.interfaces.model.module.survey.data.JeeslSurveyData;
import org.jeesl.interfaces.model.module.survey.data.JeeslSurveyMatrix;
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
import org.jeesl.model.json.module.survey.data.JsonCell;
import org.jeesl.model.json.survey.Matrix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonSurveyMatrixFactory<L extends JeeslLang,D extends JeeslDescription,
									
									
									
								
									
									ANSWER extends JeeslSurveyAnswer<L,D,?,MATRIX,DATA,OPTION>,
									MATRIX extends JeeslSurveyMatrix<L,D,ANSWER,OPTION>,
									DATA extends JeeslSurveyData<L,D,?,ANSWER,?>,
									
									OPTION extends JeeslSurveyOption<L,D>>
{
	final static Logger logger = LoggerFactory.getLogger(JsonSurveyMatrixFactory.class);

	private final Matrix q;
	
	private JsonSurveyAnswerFactory<L,D,ANSWER,MATRIX,DATA,OPTION> jfAnswer;

	public JsonSurveyMatrixFactory(String localeCode, Matrix q)
	{
		this.q=q;
		if(q.getCells()!=null && !q.getCells().isEmpty() && q.getCells().get(0).getAnswer()!=null)
		{
			jfAnswer = new JsonSurveyAnswerFactory<>(localeCode,q.getCells().get(0).getAnswer());
		}
	}
	
	public Matrix build(List<MATRIX> matrix)
	{
		Matrix json = new Matrix();
		json.setCells(new ArrayList<JsonCell>());
		
		for(MATRIX cell : matrix)
		{
			json.getCells().add(cell(cell));
		}
		
		return json;
	}
	
	
	private JsonCell cell(MATRIX cell)
	{
		JsonCell json = new JsonCell();
		json.setId(cell.getId());
		json.setRow(cell.getRow().getId());
		json.setColumn(cell.getColumn().getId());

		if(q.getCells()!=null && !q.getCells().isEmpty() && q.getCells().get(0).getAnswer()!=null)
		{
			json.setAnswer(jfAnswer.build(cell));
		}
		return json;
	}
}