package org.jeesl.factory.txt.module.survey;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyOption;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;

public class TxtOptionFactory <L extends JeeslLang, D extends JeeslDescription, OPTION extends JeeslSurveyOption<L,D>>
{
	private final String localeCode;
	
	public TxtOptionFactory(String localeCode)
	{
		this.localeCode=localeCode;
	}
	
	public String labels (List<OPTION> list)
	{
		if(list==null || list.isEmpty()){return null;}
		List<String> result = new ArrayList<String>();
		for(OPTION ejb : list){result.add(ejb.getName().get(localeCode).getLang());}
		return StringUtils.join(result, ", ");
	}
}