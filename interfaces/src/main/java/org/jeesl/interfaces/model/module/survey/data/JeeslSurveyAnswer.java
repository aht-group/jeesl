package org.jeesl.interfaces.model.module.survey.data;

import java.io.Serializable;
import java.util.List;

import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyOption;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyQuestion;
import org.jeesl.interfaces.model.module.survey.simple.JeeslSurveySimpleAnswer;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.with.parent.EjbWithParentAttributeResolver;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface JeeslSurveyAnswer<L extends JeeslLang, D extends JeeslDescription,
									QUESTION extends JeeslSurveyQuestion<L,D,?,?,?,?,?,?,?,OPTION,?>,
									MATRIX extends JeeslSurveyMatrix<L,D,?,OPTION>,
									DATA extends JeeslSurveyData<L,D,?,?,?>,
									OPTION extends JeeslSurveyOption<L,D>>
			extends Serializable,EjbWithId,EjbSaveable,
					EjbWithParentAttributeResolver,
					JeeslSurveySimpleAnswer
{
	public enum Attributes{data,question,option,valueBoolean}
	
	DATA getData();
	void setData(DATA data);
	
	QUESTION getQuestion();
	void setQuestion(QUESTION question);
	
	Boolean getValueBoolean();
	void setValueBoolean(Boolean valueBoolean);
	
	List<OPTION> getOptions();
	void setOptions(List<OPTION> option);
	
	Double getScore();
	void setScore(Double score);
	
	Double getScoreBonus();
	void setScoreBonus(Double scoreBonus);
	
	String getRemark();
	void setRemark(String remark);
	
	OPTION getOption();
	void setOption(OPTION option);
	
	List<MATRIX> getMatrix();
	void setMatrix(List<MATRIX> matrix);
}