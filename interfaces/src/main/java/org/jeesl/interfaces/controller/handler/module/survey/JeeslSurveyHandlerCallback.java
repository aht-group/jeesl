package org.jeesl.interfaces.controller.handler.module.survey;

import java.io.Serializable;

import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveySection;

public interface JeeslSurveyHandlerCallback <SECTION extends JeeslSurveySection<?,?,?,?,?>>
		extends Serializable
{
	void saveSection(JeeslSurveyHandler<SECTION> handler, SECTION section) throws JeeslConstraintViolationException, JeeslLockingException;
}