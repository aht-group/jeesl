package org.jeesl.api.bean;

import java.util.List;
import java.util.Map;

import org.jeesl.api.bean.module.survey.JeeslSurveyCache;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurvey;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurveyTemplate;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyCondition;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyOption;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyOptionSet;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyQuestion;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyQuestionElement;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveySection;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyValidation;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;

public interface JeeslSurveyBean<
					SURVEY extends JeeslSurvey<?,?,?,TEMPLATE,?>,
					TEMPLATE extends JeeslSurveyTemplate<?,?,?,TEMPLATE,?,?,?,SECTION,OPTIONS,?>,
					SECTION extends JeeslSurveySection<?,?,TEMPLATE,SECTION,QUESTION>,
					QUESTION extends JeeslSurveyQuestion<?,?,SECTION,CONDITION,VALIDATION,QE,?,?,OPTIONS,OPTION,?>,
					CONDITION extends JeeslSurveyCondition<QUESTION,QE,OPTION>,
					VALIDATION extends JeeslSurveyValidation<?,?,QUESTION,?>, //TODO tk  add VALG
					QE extends JeeslSurveyQuestionElement<?,?,QE,?>,
					OPTIONS extends JeeslSurveyOptionSet<?,?,TEMPLATE,OPTION>,
					OPTION extends JeeslSurveyOption<?,?>,
					ATT extends JeeslStatus<?,?,ATT>>
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