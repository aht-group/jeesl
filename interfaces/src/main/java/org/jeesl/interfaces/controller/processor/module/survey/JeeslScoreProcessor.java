package org.jeesl.interfaces.controller.processor.module.survey;

import org.jeesl.interfaces.model.module.survey.data.JeeslSurveyAnswer;

public interface JeeslScoreProcessor <ANSWER extends JeeslSurveyAnswer<?,?,?,?,?,?>>
{
	void calculateScore(ANSWER answer);
}