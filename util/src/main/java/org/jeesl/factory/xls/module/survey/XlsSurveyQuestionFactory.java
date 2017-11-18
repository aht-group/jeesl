package org.jeesl.factory.xls.module.survey;

import java.util.List;

import org.jeesl.factory.ejb.module.survey.EjbSurveyOptionFactory;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurvey;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurveyScheme;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurveyScore;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurveyTemplate;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurveyTemplateVersion;
import org.jeesl.interfaces.model.module.survey.correlation.JeeslSurveyCorrelation;
import org.jeesl.interfaces.model.module.survey.data.JeeslSurveyAnswer;
import org.jeesl.interfaces.model.module.survey.data.JeeslSurveyData;
import org.jeesl.interfaces.model.module.survey.data.JeeslSurveyMatrix;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyOption;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyOptionSet;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyQuestion;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveySection;
import org.jeesl.util.comparator.pojo.BooleanComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.interfaces.model.status.UtilsDescription;
import net.sf.ahtutils.interfaces.model.status.UtilsLang;
import net.sf.ahtutils.interfaces.model.status.UtilsStatus;

public class XlsSurveyQuestionFactory <L extends UtilsLang, D extends UtilsDescription,
							SURVEY extends JeeslSurvey<L,D,SS,TEMPLATE,DATA>,
							SS extends UtilsStatus<SS,L,D>,
							SCHEME extends JeeslSurveyScheme<L,D,TEMPLATE,SCORE>,
							TEMPLATE extends JeeslSurveyTemplate<L,D,SCHEME,TEMPLATE,VERSION,TS,TC,SECTION,OPTIONS,?>,
							VERSION extends JeeslSurveyTemplateVersion<L,D,TEMPLATE>,
							TS extends UtilsStatus<TS,L,D>,
							TC extends UtilsStatus<TC,L,D>,
							SECTION extends JeeslSurveySection<L,D,TEMPLATE,SECTION,QUESTION>,
							QUESTION extends JeeslSurveyQuestion<L,D,SECTION,QE,SCORE,UNIT,OPTIONS,OPTION,?>,
							QE extends UtilsStatus<QE,L,D>,
							SCORE extends JeeslSurveyScore<L,D,SCHEME,QUESTION>,
							UNIT extends UtilsStatus<UNIT,L,D>,
							ANSWER extends JeeslSurveyAnswer<L,D,QUESTION,MATRIX,DATA,OPTION>,
							MATRIX extends JeeslSurveyMatrix<L,D,ANSWER,OPTION>,
							DATA extends JeeslSurveyData<L,D,SURVEY,ANSWER,CORRELATION>,
							OPTIONS extends JeeslSurveyOptionSet<L,D,TEMPLATE,OPTION>,
							OPTION extends JeeslSurveyOption<L,D>,
							CORRELATION extends JeeslSurveyCorrelation<L,D,DATA>>
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