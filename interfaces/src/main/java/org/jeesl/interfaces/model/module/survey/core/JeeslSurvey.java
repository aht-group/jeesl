package org.jeesl.interfaces.model.module.survey.core;

import java.io.Serializable;
import java.util.List;

import org.jeesl.interfaces.model.module.survey.data.JeeslSurveyData;
import org.jeesl.interfaces.model.module.survey.data.JeeslSurveyStatus;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.util.date.EjbWithDateRange;

import net.sf.ahtutils.interfaces.model.behaviour.EjbSaveable;
import net.sf.ahtutils.interfaces.model.with.utils.UtilsWithStatus;
import net.sf.ahtutils.model.interfaces.with.EjbWithDescription;
import net.sf.ahtutils.model.interfaces.with.EjbWithLang;

public interface JeeslSurvey<L extends JeeslLang, D extends JeeslDescription,
								SS extends JeeslSurveyStatus<L,D,SS,?>,
								TEMPLATE extends JeeslSurveyTemplate<L,D,?,TEMPLATE,?,?,?,?,?,?>,
								DATA extends JeeslSurveyData<L,D,?,?,?>
>
			extends Serializable,EjbSaveable,EjbWithDateRange,
						UtilsWithStatus<L,D,SS>,EjbWithLang<L>,EjbWithDescription<D>
{
	public enum Attributes{id,template,status,startDate,endDate}
	public enum Status{open,preparation,closed};
	
	TEMPLATE getTemplate();
	void setTemplate(TEMPLATE template);
	
	List<DATA> getSurveyData();
	void setSurveyData(List<DATA> data);
}