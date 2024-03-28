package org.jeesl.factory.xls.module.survey;

import java.util.List;

import org.jeesl.controller.util.comparator.primitive.BooleanComparator;
import org.jeesl.factory.ejb.module.survey.EjbSurveyOptionFactory;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurvey;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurveyScheme;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurveyScore;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurveyTemplate;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurveyTemplateCategory;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurveyTemplateStatus;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurveyTemplateVersion;
import org.jeesl.interfaces.model.module.survey.correlation.JeeslSurveyCorrelation;
import org.jeesl.interfaces.model.module.survey.data.JeeslSurveyAnswer;
import org.jeesl.interfaces.model.module.survey.data.JeeslSurveyData;
import org.jeesl.interfaces.model.module.survey.data.JeeslSurveyMatrix;
import org.jeesl.interfaces.model.module.survey.data.JeeslSurveyStatus;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyOption;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyOptionSet;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyQuestion;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyQuestionElement;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyQuestionUnit;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveySection;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
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