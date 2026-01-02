package org.jeesl.web.mbean.prototype.module.survey;

import java.io.Serializable;
import java.util.List;

import org.jeesl.api.bean.JeeslSurveyBean;
import org.jeesl.api.bean.JeeslTranslationBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.module.survey.JeeslSurveyCoreFacade;
import org.jeesl.controller.web.util.AbstractLogMessage;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.builder.module.survey.SurveyAnalysisFactoryBuilder;
import org.jeesl.factory.builder.module.survey.SurveyTemplateFactoryBuilder;
import org.jeesl.factory.ejb.module.survey.EjbSurveyValidationAlgorithmFactory;
import org.jeesl.interfaces.bean.sb.bean.SbSingleBean;
import org.jeesl.interfaces.bean.sb.handler.SbSingleSelection;
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
import org.jeesl.jsf.handler.PositionListReorderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractAdminSurveyValidationBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
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
						QE extends JeeslSurveyQuestionType<L,D,QE,?>,
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
						ATT extends JeeslStatus<L,D,ATT>,
						TOOLCACHETEMPLATE extends JeeslJobTemplate<L,D,?,?,?,?>,
						CACHE extends JeeslJobCache<TOOLCACHETEMPLATE,?>>
					extends AbstractSurveyBean<L,D,LOC,SURVEY,SS,SCHEME,VALGORITHM,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,CONDITION,VALIDATION,QE,SCORE,UNIT,ANSWER,MATRIX,DATA,OPTIONS,OPTION,ATT>
					implements Serializable,SbSingleBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractAdminSurveyValidationBean.class);
		
	protected final SurveyAnalysisFactoryBuilder<L,D,TEMPLATE,QUESTION,QE,SCORE,ANSWER,MATRIX,DATA,OPTION,CORRELATION,DOMAIN,QUERY,PATH,DENTITY,DATTRIBUTE,ANALYSIS,AQ,AT,ATT,TOOLCACHETEMPLATE> fbAnalysis;
	
	protected List<VALGORITHM> algorithms; public List<VALGORITHM> getAlgorithms(){return algorithms;}
	
	protected VALGORITHM algorithm; public VALGORITHM getAlgorithm() {return algorithm;} public void setAlgorithm(VALGORITHM algorithm) {this.algorithm = algorithm;}

	private final EjbSurveyValidationAlgorithmFactory<VALGORITHM> efValidationAlgorithm;
	
	public AbstractAdminSurveyValidationBean(SurveyTemplateFactoryBuilder<L,D,LOC,SCHEME,VALGORITHM,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,CONDITION,VALIDATION,QE,SCORE,UNIT,OPTIONS,OPTION> fbTemplate,
											
											SurveyAnalysisFactoryBuilder<L,D,TEMPLATE,QUESTION,QE,SCORE,ANSWER,MATRIX,DATA,OPTION,CORRELATION,DOMAIN,QUERY,PATH,DENTITY,DATTRIBUTE,ANALYSIS,AQ,AT,ATT,TOOLCACHETEMPLATE> fbAnalysis)
	{
		super(fbTemplate);
		this.fbAnalysis=fbAnalysis;
		efValidationAlgorithm = fbTemplate.ejbAlgorithm();
	}
	
	protected void initSuperValidation(String userLocale,  JeeslTranslationBean<L,D,LOC> bTranslation, JeeslFacesMessageBean bMessage,
			
			JeeslSurveyCoreFacade<L,D,SURVEY,SS,TC,SECTION,QUESTION,ANSWER,MATRIX,DATA,CORRELATION> fCore,
			final JeeslSurveyBean<SURVEY,TEMPLATE,SECTION,QUESTION,CONDITION,VALIDATION,QE,OPTIONS,OPTION,ATT> bSurvey)
	{
		super.initSuperSurvey(bTranslation,bMessage,fCore,bSurvey);
		initPageSettings();
		super.initLocales(userLocale);
		reloadAlgorithms();
	}
	
	@Override public void selectSbSingle(SbSingleSelection handler, EjbWithId ejb)
	{
		
	}
	
	private void reset(boolean rAlgorihtm)
	{
		if(rAlgorihtm){algorithm = null;}
	}
	
	private void reloadAlgorithms()
	{
		algorithms = fCore.all(fbTemplate.getClassValidationAlgorithm());
		if(debugOnInfo) {logger.info(AbstractLogMessage.reloaded(fbTemplate.getClassValidationAlgorithm(), algorithms));}
	}
			
	public void addAlgorithm()
	{
		logger.info(AbstractLogMessage.createEntity(fbAnalysis.getClassAnalysis()));
		algorithm = efValidationAlgorithm.build(algorithms);
		algorithm.setName(efLang.createEmpty(localeCodes));
		algorithm.setDescription(efDescription.createEmpty(localeCodes));
	}
	
	public void saveAlgorithm() throws JeeslConstraintViolationException, JeeslLockingException
	{
		logger.info(AbstractLogMessage.saveEntity(algorithm));
		algorithm = fCore.save(algorithm);
		reloadAlgorithms();
	}
	
	public void selectAlgorithm()
	{
		logger.info(AbstractLogMessage.selectEntity(algorithm));
		algorithm = efLang.persistMissingLangs(fCore, localeCodes, algorithm);
		algorithm = efDescription.persistMissingLangs(fCore, localeCodes, algorithm);
	}
	
	public void reorderAlgorithms() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fCore,algorithms);}
}