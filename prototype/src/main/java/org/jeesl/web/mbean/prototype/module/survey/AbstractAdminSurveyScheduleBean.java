package org.jeesl.web.mbean.prototype.module.survey;

import java.io.Serializable;

import org.jeesl.api.bean.JeeslSurveyBean;
import org.jeesl.api.bean.JeeslTranslationBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.module.survey.JeeslSurveyCoreFacade;
import org.jeesl.api.facade.module.survey.JeeslSurveyTemplateFacade;
import org.jeesl.controller.web.util.AbstractLogMessage;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.module.survey.SurveyCoreFactoryBuilder;
import org.jeesl.factory.builder.module.survey.SurveyTemplateFactoryBuilder;
import org.jeesl.factory.ejb.module.survey.EjbSurveyFactory;
import org.jeesl.interfaces.bean.sb.bean.SbSingleBean;
import org.jeesl.interfaces.bean.sb.handler.SbSingleSelection;
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
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.jsf.handler.sb.SbSingleHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractAdminSurveyScheduleBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
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
						
						ATT extends JeeslStatus<L,D,ATT>>
					extends AbstractSurveyBean<L,D,LOC,SURVEY,SS,SCHEME,VALGORITHM,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,CONDITION,VALIDATION,QE,SCORE,UNIT,ANSWER,MATRIX,DATA,OPTIONS,OPTION,ATT>
					implements Serializable,SbSingleBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractAdminSurveyScheduleBean.class);

	private final SurveyCoreFactoryBuilder<L,D,SURVEY,SS,SCHEME,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,ANSWER,MATRIX,DATA,OPTION,CORRELATION> fbCore;
	
	private JeeslSurveyTemplateFacade<SCHEME,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,SCORE,OPTIONS,OPTION> fTemplate;
	
	private final SbSingleHandler<SURVEY> sbhSurvey; public SbSingleHandler<SURVEY> getSbhSurvey() {return sbhSurvey;}
	
	private final EjbSurveyFactory<L,D,SURVEY,SS,TEMPLATE> efSurvey;
	
	public AbstractAdminSurveyScheduleBean(SurveyTemplateFactoryBuilder<L,D,LOC,SCHEME,VALGORITHM,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,CONDITION,VALIDATION,QE,SCORE,UNIT,OPTIONS,OPTION> fbTemplate,
			SurveyCoreFactoryBuilder<L,D,SURVEY,SS,SCHEME,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,ANSWER,MATRIX,DATA,OPTION,CORRELATION> fbCore)
	{
		super(fbTemplate);
		this.fbCore=fbCore;
		
		sbhSurvey = new SbSingleHandler<SURVEY>(fbCore.getClassSurvey(),this);
		
		efSurvey = fbCore.survey();
	}
	
	protected void initSuperSchedule(String userLocale, JeeslTranslationBean<L,D,LOC> bTranslation, JeeslFacesMessageBean bMessage,
			JeeslSurveyTemplateFacade<SCHEME,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,SCORE,OPTIONS,OPTION> fTemplate,
			JeeslSurveyCoreFacade<L,D,SURVEY,SS,TC,SECTION,QUESTION,ANSWER,MATRIX,DATA,CORRELATION> fCore,
			final JeeslSurveyBean<SURVEY,TEMPLATE,SECTION,QUESTION,CONDITION,VALIDATION,QE,OPTIONS,OPTION,ATT> bSurvey)
	{
		super.initSuperSurvey(bTranslation,bMessage,fCore,bSurvey);
		this.fTemplate=fTemplate;
		initPageSettings();
		
		sbhCategory.silentCallback();
	}
	
	@Override public void selectSbSingle(SbSingleSelection handler, EjbWithId ejb)
	{
		if(ejb!=null)
		{
			if(fbTemplate.getClassTemplateCategory().isAssignableFrom(ejb.getClass()))
			{
				reloadSurveys();
			}
		}
	}
	
	protected void reloadSurveys()
	{
		sbhSurvey.setList(fCore.fSurveysForCategories(sbhCategory.getList()));
	}
	
	public void addSurvey() throws JeeslNotFoundException
	{
		logger.info(AbstractLogMessage.createEntity(fbCore.getClassSurvey()));
		TC category = sbhCategory.getList().get(0);
		TS templateStatus = fCore.fByCode(fbTemplate.getClassTemplateStatus(),JeeslSurveyOption.Status.open);
		template = efTemplate.build(category, templateStatus, "");
		reloadAvailableSurveVersions();
		
		SS surveystatus = fCore.fByCode(fbCore.getClassSurveyStatus(),JeeslSurvey.Status.preparation);
		
		survey = efSurvey.build(localeCodes,template,surveystatus);
	}
	
	public void saveSurvey() throws JeeslLockingException, JeeslConstraintViolationException, JeeslNotFoundException
	{
		logger.info(AbstractLogMessage.saveEntity(survey));
		VERSION version = fCore.find(fbTemplate.getClassVersion(),template.getVersion());
		survey.setTemplate(version.getTemplate());

		survey.setStatus(fCore.find(fbCore.getClassSurveyStatus(),survey.getStatus()));
		survey = fCore.save(survey);
		reloadSurveys();
		bMessage.growlSaved(survey);
	}
	
	public void deleteSurvey() throws JeeslLockingException, JeeslConstraintViolationException, JeeslNotFoundException
	{
		logger.info(AbstractLogMessage.deleteEntity(survey));
		fCore.deleteSurvey(survey);
		bMessage.growlDeleted(survey);
		survey=null;
		reloadSurveys();
		
	}
	
	public void selectSurvey() throws JeeslNotFoundException
	{
		logger.info(AbstractLogMessage.selectEntity(survey));
		survey = fCore.find(fbCore.getClassSurvey(),survey);
		survey = efLang.persistMissingLangs(fCore, localeCodes, survey);
		survey = efDescription.persistMissingLangs(fCore, localeCodes, survey);
		template = survey.getTemplate();
		reloadAvailableSurveVersions();
	}
	
	private void reloadAvailableSurveVersions() throws JeeslNotFoundException
	{
		versions = fTemplate.fVersions2(template.getCategory(),refId);
		logger.info(AbstractLogMessage.reloaded(fbTemplate.getClassVersion(), versions)+" for category:"+template.getCategory().toString());
	}
}