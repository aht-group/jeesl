package org.jeesl.api.bean;

import java.util.List;
import java.util.Map;

import org.jeesl.api.bean.module.survey.JeeslSurveyCache;
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
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyCondition;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyOption;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyOptionSet;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyQuestion;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyQuestionElement;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyQuestionUnit;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveySection;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyValidation;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;

public interface JeeslSurveyBean<L extends JeeslLang, D extends JeeslDescription,
					SURVEY extends JeeslSurvey<L,D,SS,TEMPLATE,DATA>,
					SS extends JeeslSurveyStatus<L,D,SS,?>,
					SCHEME extends JeeslSurveyScheme<L,D,TEMPLATE,SCORE>,
					TEMPLATE extends JeeslSurveyTemplate<L,D,SCHEME,TEMPLATE,VERSION,TS,?,SECTION,OPTIONS,?>,
					VERSION extends JeeslSurveyTemplateVersion<L,D,TEMPLATE>,
					TS extends JeeslSurveyTemplateStatus<L,D,TS,?>,
					
					SECTION extends JeeslSurveySection<L,D,TEMPLATE,SECTION,QUESTION>,
					QUESTION extends JeeslSurveyQuestion<L,D,SECTION,CONDITION,VALIDATION,QE,SCORE,UNIT,OPTIONS,OPTION,?>,
					CONDITION extends JeeslSurveyCondition<QUESTION,QE,OPTION>,
					VALIDATION extends JeeslSurveyValidation<L,D,QUESTION,?>, //TODO tk  add VALG
					QE extends JeeslSurveyQuestionElement<L,D,QE,?>,
					SCORE extends JeeslSurveyScore<L,D,SCHEME,QUESTION>,
					UNIT extends JeeslSurveyQuestionUnit<L,D,UNIT,?>,
					ANSWER extends JeeslSurveyAnswer<L,D,QUESTION,MATRIX,DATA,OPTION>,
					MATRIX extends JeeslSurveyMatrix<L,D,ANSWER,OPTION>,
					DATA extends JeeslSurveyData<L,D,SURVEY,ANSWER,CORRELATION>,
					OPTIONS extends JeeslSurveyOptionSet<L,D,TEMPLATE,OPTION>,
					OPTION extends JeeslSurveyOption<L,D>,
					CORRELATION extends JeeslSurveyCorrelation<DATA>,
					ATT extends JeeslStatus<L,D,ATT>>
				extends JeeslSurveyCache<TEMPLATE,SECTION,QUESTION,CONDITION,VALIDATION>
{	
	List<ATT> getToolTypes();
	List<QE> getElements();
	
	Map<TEMPLATE,List<SECTION>> getMapSection();
	Map<SECTION,List<QUESTION>> getMapQuestion();
	
	Map<Long,OPTION> getMapOptionId();
	Map<QUESTION,List<OPTION>> getMapOption();
	Map<QUESTION,List<OPTION>> getMatrixRows();
	Map<QUESTION,List<OPTION>> getMatrixCols();
		
	void updateTemplate(TEMPLATE template);
	void updateSection(SECTION section);
	void updateOptions(OPTIONS set);
	void updateQuestion(QUESTION question);
}