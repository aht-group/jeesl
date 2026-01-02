package org.jeesl.web.mbean.prototype.module.survey;

import java.util.ArrayList;
import java.util.List;

import org.jeesl.api.bean.JeeslSurveyBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.module.survey.JeeslSurveyCoreFacade;
import org.jeesl.controller.handler.module.survey.SurveyHandler;
import org.jeesl.controller.web.util.AbstractLogMessage;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.module.survey.SurveyCoreFactoryBuilder;
import org.jeesl.factory.builder.module.survey.SurveyTemplateFactoryBuilder;
import org.jeesl.interfaces.bean.sb.handler.SbSingleSelection;
import org.jeesl.interfaces.controller.handler.module.survey.JeeslSurveyHandler;
import org.jeesl.interfaces.controller.handler.module.survey.JeeslSurveyHandlerCallback;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
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
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyQuestionType;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyQuestionUnit;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveySection;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyValidation;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyValidationAlgorithm;
import org.jeesl.interfaces.model.system.job.cache.JeeslJobCache;
import org.jeesl.interfaces.model.system.job.template.JeeslJobTemplate;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.jsf.handler.sb.SbSingleHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractSurveyEntryBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
						SURVEY extends JeeslSurvey<L,D,SS,TEMPLATE,DATA>,
						SS extends JeeslSurveyStatus<L,D,SS,?>,
						SCHEME extends JeeslSurveyScheme<L,D,TEMPLATE,SCORE>,
						VALGORITHM extends JeeslSurveyValidationAlgorithm<L,D>,
						TEMPLATE extends JeeslSurveyTemplate<L,D,SCHEME,TEMPLATE,VERSION,TS,TC,SECTION,OPTIONS,?>,
						VERSION extends JeeslSurveyTemplateVersion<L,D,TEMPLATE>,
						TS extends JeeslSurveyTemplateStatus<L,D,TS,?>,
						TC extends JeeslSurveyTemplateCategory<L,D,TC,?>,
						SECTION extends JeeslSurveySection<L,D,TEMPLATE,SECTION,QUESTION>,
						QUESTION extends JeeslSurveyQuestion<L,D,SECTION,CONDITION,VALIDATION,QE,SCORE,UNIT,OPTIONS,OPTION,?>,
						CONDITION extends JeeslSurveyCondition<QUESTION,QE,OPTION>,
						VALIDATION extends JeeslSurveyValidation<L,D,QUESTION,VALGORITHM>,
						QE extends JeeslSurveyQuestionType<L,D,QE,?>,
						SCORE extends JeeslSurveyScore<L,D,SCHEME,QUESTION>,
						UNIT extends JeeslSurveyQuestionUnit<L,D,UNIT,?>,
						ANSWER extends JeeslSurveyAnswer<L,D,QUESTION,MATRIX,DATA,OPTION>,
						MATRIX extends JeeslSurveyMatrix<L,D,ANSWER,OPTION>,
						DATA extends JeeslSurveyData<L,D,SURVEY,ANSWER,CORRELATION>,
						OPTIONS extends JeeslSurveyOptionSet<L,D,TEMPLATE,OPTION>,
						OPTION extends JeeslSurveyOption<L,D>,
						CORRELATION extends JeeslSurveyCorrelation<DATA>,
						
						ATT extends JeeslStatus<L,D,ATT>,
						TOOLCACHETEMPLATE extends JeeslJobTemplate<L,D,?,?,?,?>,
						CACHE extends JeeslJobCache<TOOLCACHETEMPLATE,?>>
					extends AbstractSurveyBean<L,D,LOC,SURVEY,SS,SCHEME,VALGORITHM,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,CONDITION,VALIDATION,QE,SCORE,UNIT,ANSWER,MATRIX,DATA,OPTIONS,OPTION,ATT>
					implements JeeslSurveyHandlerCallback<SECTION>
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractSurveyEntryBean.class);

	protected final SurveyCoreFactoryBuilder<L,D,SURVEY,SS,SCHEME,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,ANSWER,MATRIX,DATA,OPTION,CORRELATION> fbCore;
	
	private final SbSingleHandler<SURVEY> sbhSurvey; public SbSingleHandler<SURVEY> getSbhSurvey() {return sbhSurvey;}
	private SurveyHandler<L,D,SURVEY,TEMPLATE,TC,SECTION,QUESTION,CONDITION,VALIDATION,ANSWER,MATRIX,DATA,OPTION,CORRELATION> handler; public SurveyHandler<L,D,SURVEY,TEMPLATE,TC,SECTION,QUESTION,CONDITION,VALIDATION,ANSWER,MATRIX,DATA,OPTION,CORRELATION> getHandler() {return handler;}
	
	protected CORRELATION correlation;
	
	public AbstractSurveyEntryBean(SurveyTemplateFactoryBuilder<L,D,LOC,SCHEME,VALGORITHM,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,CONDITION,VALIDATION,QE,SCORE,UNIT,OPTIONS,OPTION> fbTemplate,
			SurveyCoreFactoryBuilder<L,D,SURVEY,SS,SCHEME,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,ANSWER,MATRIX,DATA,OPTION,CORRELATION> fbCore)
	{
		super(fbTemplate);
		this.fbCore=fbCore;
		
		sbhSurvey = new SbSingleHandler<SURVEY>(fbCore.getClassSurvey(),this);
	}
	
	protected void initSuperEntry(String userLocale, JeeslLocaleProvider<LOC> lp, JeeslFacesMessageBean bMessage,
			
			JeeslSurveyCoreFacade<L,D,SURVEY,SS,TC,SECTION,QUESTION,ANSWER,MATRIX,DATA,CORRELATION> fCore,
			final JeeslSurveyBean<SURVEY,TEMPLATE,SECTION,QUESTION,CONDITION,VALIDATION,QE,OPTIONS,OPTION,ATT> bSurvey)
	{
		super.initSuperSurvey(lp,bMessage,fCore,bSurvey);
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
		catch (JeeslNotFoundException e) {e.printStackTrace();}
//		handler = fbCore.handler(this,bMessage,fCore,bSurvey);
		handler = new SurveyHandler<>(this,fbTemplate,fbCore,bMessage,fCore,bSurvey,bSurvey);
	}
	
	protected abstract void prepareCorrelation();
	
	@Override public void selectSbSingle(SbSingleSelection handler, EjbWithId ejb)
	{
		
	}
	
	public void selectSurvey() throws JeeslNotFoundException
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
	
	@Override public void saveSection(JeeslSurveyHandler<SECTION> handler, SECTION section) throws JeeslConstraintViolationException, JeeslLockingException
	{
		this.saveData(section);
		
	}
	
	public void saveData(SECTION section) throws JeeslConstraintViolationException, JeeslLockingException
	{
		logger.info(AbstractLogMessage.saveEntity(null,correlation,section));
		correlation = fCore.saveTransaction(correlation);
		handler.save(correlation,section);
	}

	public void saveData() throws JeeslConstraintViolationException, JeeslLockingException
	{
		logger.info(AbstractLogMessage.saveEntity(null,correlation)+" TEST ONLY");
	}
}