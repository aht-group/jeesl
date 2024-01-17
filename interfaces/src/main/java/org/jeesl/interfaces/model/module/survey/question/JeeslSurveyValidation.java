package org.jeesl.interfaces.model.module.survey.question;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.with.parent.EjbWithParentAttributeResolver;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPositionVisible;
import org.jeesl.interfaces.model.with.system.locale.EjbWithDescription;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslSurveyValidation<L extends JeeslLang, D extends JeeslDescription,
										QUESTION extends JeeslSurveyQuestion<L,D,?,?,?,?,?,?,?,?,?>,
										VALGORITHM extends JeeslSurveyValidationAlgorithm<L,D>>
			extends Serializable,EjbWithId,EjbSaveable,EjbRemoveable,
					EjbWithDescription<D>,
					EjbWithPositionVisible,EjbWithParentAttributeResolver
{
	public enum Attributes{question}
	
	QUESTION getQuestion();
	void setQuestion(QUESTION question);
	
	VALGORITHM getAlgorithm();
	void setAlgorithm(VALGORITHM algorithm);
	
	String getConfig();
	void setConfig(String config);
}