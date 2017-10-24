package org.jeesl.interfaces.model.module.survey.question;

import java.io.Serializable;

import net.sf.ahtutils.interfaces.model.behaviour.EjbSaveable;
import net.sf.ahtutils.interfaces.model.status.UtilsStatus;
import net.sf.ahtutils.interfaces.model.with.parent.EjbWithParentAttributeResolver;
import net.sf.ahtutils.interfaces.model.with.position.EjbWithPosition;
import net.sf.ahtutils.model.interfaces.with.EjbWithId;

public interface JeeslSurveyCondition<QUESTION extends JeeslSurveyQuestion<?,?,?,?,?,?,?,?,?>,
										QE extends UtilsStatus<QE,?,?>>
			extends Serializable,EjbWithId,EjbSaveable,
					EjbWithPosition,EjbWithParentAttributeResolver
{
	public enum Attributes{question}
	
	QUESTION getQuestion();
	void setQuestion(QUESTION question);
	
	QE getElement();
	void setElement(QE element);
}