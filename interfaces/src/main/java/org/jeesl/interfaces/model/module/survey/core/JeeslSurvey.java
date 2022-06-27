package org.jeesl.interfaces.model.module.survey.core;

import java.io.Serializable;
import java.util.List;

import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.module.survey.data.JeeslSurveyData;
import org.jeesl.interfaces.model.module.survey.data.JeeslSurveyStatus;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.with.date.ju.EjbWithDateRange;
import org.jeesl.interfaces.model.with.system.locale.EjbWithDescription;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLang;
import org.jeesl.interfaces.model.with.system.status.JeeslWithStatus;

public interface JeeslSurvey<L extends JeeslLang, D extends JeeslDescription,
								SS extends JeeslSurveyStatus<L,D,SS,?>,
								TEMPLATE extends JeeslSurveyTemplate<L,D,?,TEMPLATE,?,?,?,?,?,?>,
								DATA extends JeeslSurveyData<L,D,?,?,?>
>
			extends Serializable,EjbSaveable,EjbWithDateRange,
						JeeslWithStatus<SS>,EjbWithLang<L>,EjbWithDescription<D>
{
	public enum Attributes{id,template,status,startDate,endDate}
	public enum Status{open,preparation,closed};
	
	TEMPLATE getTemplate();
	void setTemplate(TEMPLATE template);
	
	List<DATA> getSurveyData();
	void setSurveyData(List<DATA> data);
}