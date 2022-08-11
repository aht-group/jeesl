package org.jeesl.api.facade.module.survey;

import java.util.List;

import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.io.domain.JeeslDomain;
import org.jeesl.interfaces.model.io.domain.JeeslDomainPath;
import org.jeesl.interfaces.model.io.domain.JeeslDomainQuery;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionAttribute;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.module.survey.analysis.JeeslSurveyAnalysis;
import org.jeesl.interfaces.model.module.survey.analysis.JeeslSurveyAnalysisQuestion;
import org.jeesl.interfaces.model.module.survey.analysis.JeeslSurveyAnalysisTool;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurvey;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurveyScheme;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurveyScore;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurveyTemplate;
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
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.model.json.JsonFlatFigures;
import org.jeesl.model.json.module.survey.JsonSurveyValue;
import org.jeesl.model.json.module.survey.JsonSurveyValues;

public interface JeeslSurveyAnalysisFacade <L extends JeeslLang, D extends JeeslDescription,
											SURVEY extends JeeslSurvey<L,D,SS,TEMPLATE,DATA>,
											SS extends JeeslSurveyStatus<L,D,SS,?>,
											SCHEME extends JeeslSurveyScheme<L,D,TEMPLATE,SCORE>,
											TEMPLATE extends JeeslSurveyTemplate<L,D,SCHEME,TEMPLATE,VERSION,?,?,SECTION,OPTIONS,ANALYSIS>,
											VERSION extends JeeslSurveyTemplateVersion<L,D,TEMPLATE>,
											SECTION extends JeeslSurveySection<L,D,TEMPLATE,SECTION,QUESTION>,
											QUESTION extends JeeslSurveyQuestion<L,D,SECTION,?,?,QE,SCORE,UNIT,OPTIONS,OPTION,AQ>,
											QE extends JeeslSurveyQuestionElement<L,D,QE,?>,
											SCORE extends JeeslSurveyScore<L,D,SCHEME,QUESTION>,
											UNIT extends JeeslSurveyQuestionUnit<L,D,UNIT,?>,
											ANSWER extends JeeslSurveyAnswer<L,D,QUESTION,MATRIX,DATA,OPTION>,
											MATRIX extends JeeslSurveyMatrix<L,D,ANSWER,OPTION>,
											DATA extends JeeslSurveyData<L,D,SURVEY,ANSWER,CORRELATION>,
											OPTIONS extends JeeslSurveyOptionSet<L,D,TEMPLATE,OPTION>,
											OPTION extends JeeslSurveyOption<L,D>,
											CORRELATION extends JeeslSurveyCorrelation<DATA>,
											DOMAIN extends JeeslDomain<L,DENTITY>,
											QUERY extends JeeslDomainQuery<L,D,DOMAIN,PATH>,
											PATH extends JeeslDomainPath<L,D,QUERY,DENTITY,DATTRIBUTE>,
											DENTITY extends JeeslRevisionEntity<L,D,?,?,DATTRIBUTE,?>,
											DATTRIBUTE extends JeeslRevisionAttribute<L,D,DENTITY,?,?>,
											ANALYSIS extends JeeslSurveyAnalysis<L,D,TEMPLATE,DOMAIN,DENTITY,DATTRIBUTE>,
											AQ extends JeeslSurveyAnalysisQuestion<L,D,QUESTION,ANALYSIS>,
											TOOL extends JeeslSurveyAnalysisTool<L,D,QE,QUERY,DATTRIBUTE,AQ,TOOLT>,
											TOOLT extends JeeslStatus<L,D,TOOLT>>
	extends JeeslFacade
{
	TOOL load(TOOL tool);
	
	AQ fAnalysis(ANALYSIS analysis, QUESTION question) throws JeeslNotFoundException;
	
//	List<DATTRIBUTE> fDomainAttributes(DENTITY entity);
	
	JsonFlatFigures surveyCountRecords(List<SURVEY> surveys);
	JsonSurveyValue surveyCountAnswers(QUESTION question);
	
	JsonFlatFigures surveyStatisticOption(QUESTION question, SURVEY survey, TOOL tool);
	JsonSurveyValues surveyStatisticBoolean(QUESTION question, SURVEY survey, TOOL tool);
	
	JsonFlatFigures surveyCountOption(List<QUESTION> questions, SURVEY survey, List<CORRELATION> correlations);
	JsonFlatFigures surveyCountAnswer(List<QUESTION> questions, SURVEY survey, List<CORRELATION> correlations);
}