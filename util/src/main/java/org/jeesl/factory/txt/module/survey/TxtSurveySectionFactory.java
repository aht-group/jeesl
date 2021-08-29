package org.jeesl.factory.txt.module.survey;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveySection;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TxtSurveySectionFactory <L extends JeeslLang, D extends JeeslDescription, SECTION extends JeeslSurveySection<L,D,?,SECTION,?>>
{
	final static Logger logger = LoggerFactory.getLogger(TxtSurveySectionFactory.class);
		
	public String codes(List<SECTION> sections)
	{
		List<String> list = new ArrayList<String>();
		if(sections!=null)
		{
			for(SECTION section : sections)
			{
				list.add(section.getCode());
			}
		}
		return StringUtils.join(list,", ");
	}
}