package org.jeesl.interfaces.model.module.survey.correlation;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.module.survey.data.JeeslSurveyData;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslSurveyCorrelation<DATA extends JeeslSurveyData<?,?,?,?,?>>
			extends Serializable,EjbWithId,EjbSaveable
{
	public enum Attributes{survey,data}
//	DATA getData();
//	void setData(DATA data);
}