package org.jeesl.web.mbean.prototype.module.survey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jeesl.api.bean.JeeslSurveyBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.module.survey.JeeslSurveyAnalysisFacade;
import org.jeesl.api.facade.module.survey.JeeslSurveyCoreFacade;
import org.jeesl.api.facade.module.survey.JeeslSurveyTemplateFacade;
import org.jeesl.controller.handler.module.survey.SurveyHandler;
import org.jeesl.factory.builder.module.survey.SurveyAnalysisFactoryBuilder;
import org.jeesl.factory.builder.module.survey.SurveyCoreFactoryBuilder;
import org.jeesl.factory.builder.module.survey.SurveyTemplateFactoryBuilder;
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
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyValidation;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyValidationAlgorithm;
import org.jeesl.interfaces.model.system.io.domain.JeeslDomain;
import org.jeesl.interfaces.model.system.io.domain.JeeslDomainPath;
import org.jeesl.interfaces.model.system.io.domain.JeeslDomainQuery;
import org.jeesl.interfaces.model.system.io.revision.entity.JeeslRevisionAttribute;
import org.jeesl.interfaces.model.system.io.revision.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.system.job.JeeslJobCache;
import org.jeesl.interfaces.model.system.job.JeeslJobTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.exception.ejb.UtilsConstraintViolationException;
import net.sf.ahtutils.exception.ejb.UtilsLockingException;
import net.sf.ahtutils.exception.ejb.UtilsNotFoundException;
import net.sf.ahtutils.interfaces.model.status.UtilsDescription;
import net.sf.ahtutils.interfaces.model.status.UtilsLang;
import net.sf.ahtutils.interfaces.model.status.UtilsStatus;
import net.sf.ahtutils.model.interfaces.with.EjbWithId;
import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;

public abstract class AbstractSurveyEntryBean <L extends UtilsLang, D extends UtilsDescription, LOC extends UtilsStatus<LOC,L,D>,
						SURVEY extends JeeslSurvey<L,D,SS,TEMPLATE,DATA>,
						SS extends JeeslSurveyStatus<L,D,SS,?>,
						SCHEME extends JeeslSurveyScheme<L,D,TEMPLATE,SCORE>,
						VALGORITHM extends JeeslSurveyValidationAlgorithm<L,D>,
						TEMPLATE extends JeeslSurveyTemplate<L,D,SCHEME,TEMPLATE,VERSION,TS,TC,SECTION,OPTIONS,ANALYSIS>,
						VERSION extends JeeslSurveyTemplateVersion<L,D,TEMPLATE>,
						TS extends JeeslSurveyTemplateStatus<L,D,TS,?>,
						TC extends JeeslSurveyTemplateCategory<L,D,TC,?>,
						SECTION extends JeeslSurveySection<L,D,TEMPLATE,SECTION,QUESTION>,
						QUESTION extends JeeslSurveyQuestion<L,D,SECTION,CONDITION,VALIDATION,QE,SCORE,UNIT,OPTIONS,OPTION,AQ>,
						CONDITION extends JeeslSurveyCondition<QUESTION,QE,OPTION>,
						VALIDATION extends JeeslSurveyValidation<L,D,QUESTION,VALGORITHM>,
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
						AT extends JeeslSurveyAnalysisTool<L,D,QE,QUERY,DATTRIBUTE,AQ,ATT>,
						ATT extends UtilsStatus<ATT,L,D>,
						TOOLCACHETEMPLATE extends JeeslJobTemplate<L,D,?,?,?,?>,
						CACHE extends JeeslJobCache<TOOLCACHETEMPLATE,?>>
					extends AbstractSurveyBean<L,D,LOC,SURVEY,SS,SCHEME,VALGORITHM,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,CONDITION,VALIDATION,QE,SCORE,UNIT,ANSWER,MATRIX,DATA,OPTIONS,OPTION,CORRELATION,DOMAIN,QUERY,PATH,DENTITY,DATTRIBUTE,ANALYSIS,AQ,AT,ATT,TOOLCACHETEMPLATE,CACHE>
					implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractSurveyEntryBean.class);

	private SurveyHandler<L,D,SURVEY,TEMPLATE,TC,SECTION,QUESTION,CONDITION,VALIDATION,ANSWER,MATRIX,DATA,OPTION,CORRELATION> handler; public SurveyHandler<L,D,SURVEY,TEMPLATE,TC,SECTION,QUESTION,CONDITION,VALIDATION,ANSWER,MATRIX,DATA,OPTION,CORRELATION> getHandler() {return handler;}
	
	protected CORRELATION correlation;
	
	public AbstractSurveyEntryBean(SurveyTemplateFactoryBuilder<L,D,LOC,SCHEME,VALGORITHM,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,CONDITION,VALIDATION,QE,SCORE,UNIT,OPTIONS,OPTION> fbTemplate,
			SurveyCoreFactoryBuilder<L,D,LOC,SURVEY,SS,SCHEME,VALGORITHM,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,CONDITION,VALIDATION,QE,SCORE,UNIT,ANSWER,MATRIX,DATA,OPTIONS,OPTION,CORRELATION,ATT> fbCore,
			SurveyAnalysisFactoryBuilder<L,D,TEMPLATE,QUESTION,QE,SCORE,ANSWER,MATRIX,DATA,OPTION,CORRELATION,DOMAIN,QUERY,PATH,DENTITY,DATTRIBUTE,ANALYSIS,AQ,AT,ATT,TOOLCACHETEMPLATE> fbAnalysis)
	{
		super(fbTemplate,fbCore,fbAnalysis);
	}
	
	protected void initSuperEntry(String userLocale, String[] localeCodes, JeeslFacesMessageBean bMessage,
			JeeslSurveyTemplateFacade<L,D,SCHEME,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,QE,SCORE,UNIT,OPTIONS,OPTION> fTemplate,
			JeeslSurveyCoreFacade<L,D,LOC,SURVEY,SS,SCHEME,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,QE,SCORE,UNIT,ANSWER,MATRIX,DATA,OPTIONS,OPTION,CORRELATION> fCore,
			JeeslSurveyAnalysisFacade<L,D,SURVEY,SS,SCHEME,TEMPLATE,VERSION,SECTION,QUESTION,QE,SCORE,UNIT,ANSWER,MATRIX,DATA,OPTIONS,OPTION,CORRELATION,DOMAIN,QUERY,PATH,DENTITY,DATTRIBUTE,ANALYSIS,AQ,AT,ATT> fAnalysis,
			final JeeslSurveyBean<L,D,SURVEY,SS,SCHEME,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,CONDITION,VALIDATION,QE,SCORE,UNIT,ANSWER,MATRIX,DATA,OPTIONS,OPTION,CORRELATION,ATT> bSurvey)
	{
		super.initSuperSurvey(new ArrayList<String>(Arrays.asList(localeCodes)),bMessage,fTemplate,fCore,fAnalysis,bSurvey);
		initPageSettings();
		try
		{
			List<SS> allowed = new ArrayList<SS>();
			allowed.add(fCore.fByCode(fbCore.getClassSurveyStatus(), JeeslSurvey.Status.preparation));
			allowed.add(fCore.fByCode(fbCore.getClassSurveyStatus(), JeeslSurvey.Status.open));
			allowed.add(fCore.fByCode(fbCore.getClassSurveyStatus(), JeeslSurvey.Status.closed));
			List<SURVEY> list = new ArrayList<>();
			for(SURVEY s : fCore.fSurveysForCategories(sbhCategory.getList()))
			{
				if(allowed.contains(s.getStatus())){list.add(s);}
			}
			
			sbhSurvey.setList(list);
			logger.info(AbstractLogMessage.reloaded(fbCore.getClassSurvey(), sbhSurvey.getList()));
		}
		catch (UtilsNotFoundException e) {e.printStackTrace();}
		handler = fbCore.handler(bMessage,fCore,bSurvey);
	}
	
	protected abstract void prepareCorrelation();
	
	@Override public void selectSbSingle(EjbWithId ejb)
	{
		
	}
	
	public void selectSurvey() throws UtilsNotFoundException
	{
		logger.info(AbstractLogMessage.selectEntity(survey));
		handler.reset();
		correlation=null;
		prepareCorrelation();
		
		if(correlation!=null)
		{
			handler.prepare(survey,correlation);
			handler.reloadAnswers();

			logger.info("Sections: "+bSurvey.getMapSection().get(handler.getTemplate()).size());
		}
		
		logger.info("Show Assessment: Allow:"+handler.isAllowAssessment()+" Show:"+handler.isShowAssessment());
	}
	
	public void saveData(SECTION section) throws UtilsConstraintViolationException, UtilsLockingException
	{
		logger.info(AbstractLogMessage.saveEntity(null,correlation,section));
		correlation = fCore.saveTransaction(correlation);
		handler.save(correlation,section);
	}

	public void saveData() throws UtilsConstraintViolationException, UtilsLockingException
	{
		logger.info(AbstractLogMessage.saveEntity(null,correlation)+" TEST ONLY");
	}
}