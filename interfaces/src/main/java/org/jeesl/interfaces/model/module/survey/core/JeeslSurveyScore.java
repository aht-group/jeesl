package org.jeesl.interfaces.model.module.survey.core;

import java.io.Serializable;

import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyQuestion;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;

import net.sf.ahtutils.interfaces.model.behaviour.EjbSaveable;

public interface JeeslSurveyScore<L extends JeeslLang, D extends JeeslDescription,
									SCHEME extends JeeslSurveyScheme<L,D,?,?>,
									QUESTION extends JeeslSurveyQuestion<L,D,?,?,?,?,?,?,?,?,?>>
			extends Serializable,EjbSaveable
{
	QUESTION getQuestion();
	void setQuestion(QUESTION question);
	
	SCHEME getScheme();
	void setScheme(SCHEME scheme);
	
	Boolean getCalculateScore();
	void setCalculateScore(Boolean calculateScore);
	
	Double getMinScore();
	void setMinScore(Double minScore);
	
	Double getMaxScore();
	void setMaxScore(Double maxScore);
	
	Boolean getShowScore();
	void setShowScore(Boolean showScore);
}