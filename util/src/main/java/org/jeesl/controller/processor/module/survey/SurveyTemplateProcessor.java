package org.jeesl.controller.processor.module.survey;

import java.util.Iterator;

import org.jeesl.model.xml.module.survey.Question;
import org.jeesl.model.xml.module.survey.Section;
import org.jeesl.model.xml.module.survey.Template;

public class SurveyTemplateProcessor
{
	public static void removeInvisibleElements(Template template)
	{
		for(Iterator<Section> iSection = template.getSection().iterator(); iSection.hasNext();)
		{
		    Section s = iSection.next();
		    if(!s.isVisible()) {iSection.remove();}
		    else
		    {
		    	for(Iterator<Question> iQuestion = s.getQuestion().iterator(); iQuestion.hasNext();)
				{
				    Question q = iQuestion.next();
				    if(!q.isVisible()) {iQuestion.remove();}
				}
		    }
		}
	}
}