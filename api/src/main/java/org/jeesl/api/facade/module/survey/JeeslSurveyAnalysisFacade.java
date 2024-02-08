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
import org.jeesl.interfaces.model.module.survey.correlation.JeeslSurveyCorrelation;
import org.jeesl.interfaces.model.module.survey.data.JeeslSurveyData;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyOption;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyOptionSet;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyQuestion;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.model.json.JsonFlatFigures;
import org.jeesl.model.json.module.survey.JsonSurveyValue;
import org.jeesl.model.json.module.survey.JsonSurveyValues;

public interface JeeslSurveyAnalysisFacade <SURVEY extends JeeslSurvey<?,?,?,?,DATA>,		
											QUESTION extends JeeslSurveyQuestion<?,?,?,?,?,?,?,?,OPTIONS,OPTION,AQ>,
											DATA extends JeeslSurveyData<?,?,SURVEY,?,CORRELATION>,
											OPTIONS extends JeeslSurveyOptionSet<?,?,?,OPTION>,
											OPTION extends JeeslSurveyOption<?,?>,
											CORRELATION extends JeeslSurveyCorrelation<DATA>,
											DOMAIN extends JeeslDomain<?,DENTITY>,
											QUERY extends JeeslDomainQuery<?,?,DOMAIN,PATH>,
											PATH extends JeeslDomainPath<?,?,QUERY,DENTITY,DATTRIBUTE>,
											DENTITY extends JeeslRevisionEntity<?,?,?,?,DATTRIBUTE,?>,
											DATTRIBUTE extends JeeslRevisionAttribute<?,?,DENTITY,?,?>,
											ANALYSIS extends JeeslSurveyAnalysis<?,?,?,DOMAIN,DENTITY,DATTRIBUTE>,
											AQ extends JeeslSurveyAnalysisQuestion<?,?,QUESTION,ANALYSIS>,
											TOOL extends JeeslSurveyAnalysisTool<?,?,?,QUERY,DATTRIBUTE,AQ,TOOLT>,
											TOOLT extends JeeslStatus<?,?,TOOLT>>
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