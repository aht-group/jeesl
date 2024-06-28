package org.jeesl.factory.xlsx.module.survey;

import java.util.List;

import org.jeesl.controller.util.comparator.primitive.BooleanComparator;
import org.jeesl.factory.ejb.module.survey.EjbSurveyOptionFactory;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyOption;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyOptionSet;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyQuestion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XlsSurveyQuestionFactory <QUESTION extends JeeslSurveyQuestion<?,?,?,?,?,?,?,?,OPTIONS,OPTION,?>,
										OPTIONS extends JeeslSurveyOptionSet<?,?,?,OPTION>,
										OPTION extends JeeslSurveyOption<?,?>>
{
	final static Logger logger = LoggerFactory.getLogger(XlsSurveyQuestionFactory.class);
	
	private final EjbSurveyOptionFactory<QUESTION,OPTION> efOption;
	
	public XlsSurveyQuestionFactory(EjbSurveyOptionFactory<QUESTION,OPTION> efOption)
	{
		this.efOption = efOption;
	}

	public int toSize(QUESTION question)
	{
		if(BooleanComparator.active(question.getShowMatrix()))
		{
			List<OPTION> oRows = efOption.toRows(question.getOptions());
			List<OPTION> oCols = efOption.toColumns(question.getOptions());
			return oRows.size()*oCols.size();
		}
		else
		{
			return 1;
		}
	}
}