package org.jeesl.controller.handler.module.survey;

import java.io.IOException;
import java.io.Serializable;

import org.exlp.util.io.JsonUtil;
import org.jeesl.api.facade.module.survey.JeeslSurveyAnalysisFacade;
import org.jeesl.api.facade.system.JeeslJobFacade;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
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
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyValidationAlgorithm;
import org.jeesl.interfaces.model.system.job.cache.JeeslJobCache;
import org.jeesl.interfaces.model.system.job.template.JeeslJobTemplate;
import org.jeesl.interfaces.model.system.job.template.JeeslJobType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.model.json.JsonFlatFigures;
import org.jeesl.model.json.module.survey.JsonSurveyValues;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class SurveyAnalysisCacheHandler<SURVEY extends JeeslSurvey<?,?,?,?,DATA>,
										SECTION extends JeeslSurveySection<?,?,?,SECTION,QUESTION>,
										QUESTION extends JeeslSurveyQuestion<?,?,SECTION,?,?,?,?,?,OPTIONS,OPTION,AQ>,
										MATRIX extends JeeslSurveyMatrix<?,?,?,OPTION>,
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
										TOOL extends JeeslSurveyAnalysisTool<?,?,?,QUERY,DATTRIBUTE,AQ,ATT>,
										ATT extends JeeslStatus<?,?,ATT>,
										TOOLCACHETEMPLATE extends JeeslJobTemplate<?,?,?,?,?,?>,
										CACHE extends JeeslJobCache<TOOLCACHETEMPLATE,?>>
	implements Serializable
{
	final static Logger logger = LoggerFactory.getLogger(SurveyAnalysisCacheHandler.class);
	private static final long serialVersionUID = 1L;

	private static boolean debugOnInfo = true;
	
	private final JeeslJobFacade<TOOLCACHETEMPLATE,?,?,?,?,?,?,?,?,?,CACHE,?,?,?,?> fJob;
	private final JeeslSurveyAnalysisFacade<SURVEY,QUESTION,DATA,OPTIONS,OPTION,CORRELATION,DOMAIN,QUERY,PATH,DENTITY,DATTRIBUTE,ANALYSIS,AQ,TOOL,ATT> fAnalysis;
	
	private TOOLCACHETEMPLATE jobTemplate;

	public SurveyAnalysisCacheHandler(JeeslJobFacade<TOOLCACHETEMPLATE,?,?,?,?,?,?,?,?,?,CACHE,?,?,?,?> fJob,
			JeeslSurveyAnalysisFacade<SURVEY,QUESTION,DATA,OPTIONS,OPTION,CORRELATION,DOMAIN,QUERY,PATH,DENTITY,DATTRIBUTE,ANALYSIS,AQ,TOOL,ATT> fAnalysis)
	{
		this.fJob=fJob;
		this.fAnalysis=fAnalysis;
	}
	
	public <E extends Enum<E>> void activateTemplate(E code)
	{
		try
		{
			jobTemplate = fJob.fJobTemplate(JeeslJobType.Code.json, JeeslJobTemplate.Code.surveyAnalysis.toString());
		}
		catch (JeeslNotFoundException e) {e.printStackTrace();}
	}
	
	public void remove(TOOL tool)
	{
		if(jobTemplate!=null)
		{
			if(jobTemplate!=null)
			{
				try
				{
					CACHE cache = fJob.fJobCache(jobTemplate, Long.valueOf(tool.getId()).toString());
					if(debugOnInfo) {logger.info("Deleting Cache "+cache.toString());}
					fJob.rm(cache);
				}
				catch (JeeslNotFoundException e)
				{
					if(debugOnInfo) {logger.info("No cache availabe for "+tool.getClass().getSimpleName()+" "+tool.getId());}
				}
				catch (JeeslConstraintViolationException e) {logger.error(e.getMessage());}
			}
		}
	}
	
	public JsonSurveyValues surveyStatisticBoolean(QUESTION question, SURVEY survey, TOOL tool)
	{
		JsonSurveyValues ff=null;
		if(jobTemplate!=null)
		{
			try
			{
				CACHE cache = fJob.fJobCache(jobTemplate, Long.valueOf(tool.getId()).toString());
				ff = JsonUtil.read(JsonSurveyValues.class,cache.getData());
			}
			catch (JeeslNotFoundException e) {}
			catch (JsonParseException e) {e.printStackTrace();}
			catch (JsonMappingException e) {e.printStackTrace();}
			catch (IOException e) {e.printStackTrace();}
		}
		if(ff==null)
		{	
			ff = fAnalysis.surveyStatisticBoolean(question,survey,tool);
			if(jobTemplate!=null)
			{
				try
				{
					fJob.uJobCache(jobTemplate, Long.valueOf(tool.getId()).toString(), JsonUtil.toBytes(ff));
				}
				catch (JsonProcessingException e) {e.printStackTrace();}
				catch (JeeslConstraintViolationException e) {e.printStackTrace();}
				catch (JeeslLockingException e) {e.printStackTrace();}
			}
		}
		return ff;
	}
	
	public JsonFlatFigures surveyStatisticOption(QUESTION question, SURVEY survey, TOOL tool)
	{
		JsonFlatFigures ff=null;
		if(jobTemplate!=null)
		{
			try
			{
				CACHE cache = fJob.fJobCache(jobTemplate, Long.valueOf(tool.getId()).toString());
				ff = JsonUtil.read(JsonFlatFigures.class,cache.getData());
			}
			catch (JeeslNotFoundException e) {}
			catch (JsonParseException e) {e.printStackTrace();}
			catch (JsonMappingException e) {e.printStackTrace();}
			catch (IOException e) {e.printStackTrace();}
		}
		if(ff==null)
		{	
			ff = fAnalysis.surveyStatisticOption(question,survey,tool);
			if(jobTemplate!=null)
			{
				try
				{
					fJob.uJobCache(jobTemplate, Long.valueOf(tool.getId()).toString(), JsonUtil.toBytes(ff));
				}
				catch (JsonProcessingException e) {e.printStackTrace();}
				catch (JeeslConstraintViolationException e) {e.printStackTrace();}
				catch (JeeslLockingException e) {e.printStackTrace();}
			}
		}
		return ff;
	}
}