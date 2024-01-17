package org.jeesl.interfaces.model.module.survey.question;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.with.parent.EjbWithParentAttributeResolver;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPosition;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslSurveyCondition<QUESTION extends JeeslSurveyQuestion<?,?,?,?,?,?,?,?,?,?,?>,
										QE extends JeeslStatus<?,?,QE>,
										OPTION extends JeeslSurveyOption<?,?>>
			extends Serializable,EjbWithId,EjbSaveable,EjbRemoveable,
					EjbWithPosition,EjbWithParentAttributeResolver
{
	public enum Attributes{question}
	
	QUESTION getQuestion();
	void setQuestion(QUESTION question);
	
	QUESTION getTriggerQuestion();
	void setTriggerQuestion(QUESTION triggerQuestion);
	
	QE getElement();
	void setElement(QE element);
	
	OPTION getOption();
	void setOption(OPTION option);
	
	boolean isNegate();
	void setNegate(boolean negate);
	
}