package org.jeesl.interfaces.model.module.survey.data;

import java.io.Serializable;
import java.util.List;

import org.jeesl.interfaces.model.module.survey.core.JeeslSurvey;
import org.jeesl.interfaces.model.module.survey.correlation.JeeslSurveyCorrelation;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.with.primitive.date.EjbWithRecord;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface JeeslSurveyData<L extends JeeslLang, D extends JeeslDescription,
									SURVEY extends JeeslSurvey<L,D,?,?,?>,
									ANSWER extends JeeslSurveyAnswer<L,D,?,?,?,?>,
									CORRELATION extends JeeslSurveyCorrelation<?>>
			extends Serializable,EjbWithId,EjbWithRecord
{
	public enum Attributes{correlation,survey,answers}
	
	SURVEY getSurvey();
	void setSurvey(SURVEY survey);
	
	CORRELATION getCorrelation();
	void setCorrelation(CORRELATION correlation);
	
	List<ANSWER> getAnswers();
	void setAnswers(List<ANSWER> answers);
}