package org.jeesl.interfaces.controller.processor.module.survey;

import org.jeesl.interfaces.model.module.survey.data.JeeslSurveyAnswer;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyQuestion;

public interface JeeslScoreProcessor <ANSWER extends JeeslSurveyAnswer<?,?,?,?,?,?>>
{
	void calculateScore(ANSWER answer);
}