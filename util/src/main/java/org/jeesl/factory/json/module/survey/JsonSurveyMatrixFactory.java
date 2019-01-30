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
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveySection;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyValidation;
import org.jeesl.model.json.survey.Cell;
import org.jeesl.model.json.survey.Matrix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.interfaces.model.status.UtilsDescription;
import net.sf.ahtutils.interfaces.model.status.UtilsLang;
import net.sf.ahtutils.interfaces.model.status.UtilsStatus;

public class JsonSurveyMatrixFactory<L extends UtilsLang,D extends UtilsDescription,
									SECTION extends JeeslSurveySection<L,D,?,SECTION,QUESTION>,
									QUESTION extends JeeslSurveyQuestion<L,D,SECTION,CONDITION,VALIDATION,QE,SCORE,UNIT,OPTIONS,OPTION,?>,
									CONDITION extends JeeslSurveyCondition<QUESTION,QE,OPTION>,
									VALIDATION extends JeeslSurveyValidation<QUESTION,?>,
									QE extends UtilsStatus<QE,L,D>,
									SCORE extends JeeslSurveyScore<L,D,?,QUESTION>,
									UNIT extends UtilsStatus<UNIT,L,D>,
									ANSWER extends JeeslSurveyAnswer<L,D,QUESTION,MATRIX,DATA,OPTION>,
									MATRIX extends JeeslSurveyMatrix<L,D,ANSWER,OPTION>,
									DATA extends JeeslSurveyData<L,D,?,ANSWER,?>,
									OPTIONS extends JeeslSurveyOptionSet<L,D,?,OPTION>,
									OPTION extends JeeslSurveyOption<L,D>>
{
	final static Logger logger = LoggerFactory.getLogger(JsonSurveyMatrixFactory.class);

	private final Matrix q;
	
	private JsonSurveyAnswerFactory<L,D,SECTION,QUESTION,CONDITION,VALIDATION,QE,SCORE,UNIT,ANSWER,MATRIX,DATA,OPTIONS,OPTION> jfAnswer;

	public JsonSurveyMatrixFactory(Matrix q)
	{
		this.q=q;
		if(q.getCells()!=null && !q.getCells().isEmpty() && q.getCells().get(0).getAnswer()!=null)
		{
			jfAnswer = new JsonSurveyAnswerFactory<L,D,SECTION,QUESTION,CONDITION,VALIDATION,QE,SCORE,UNIT,ANSWER,MATRIX,DATA,OPTIONS,OPTION>(q.getCells().get(0).getAnswer());
		}
		
	}
	
	public Matrix build(List<MATRIX> matrix)
	{
		Matrix json = new Matrix();
		json.setCells(new ArrayList<Cell>());
		
		for(MATRIX cell : matrix)
		{
			json.getCells().add(cell(cell));
		}
		
		return json;
	}
	
	
	private Cell cell(MATRIX cell)
	{
		Cell json = new Cell();
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