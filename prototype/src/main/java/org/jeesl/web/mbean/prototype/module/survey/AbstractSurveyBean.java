package org.jeesl.web.mbean.prototype.module.survey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.jeesl.api.bean.JeeslSurveyBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.module.survey.JeeslSurveyAnalysisFacade;
import org.jeesl.api.facade.module.survey.JeeslSurveyCoreFacade;
import org.jeesl.api.facade.module.survey.JeeslSurveyTemplateFacade;
import org.jeesl.controller.handler.module.survey.SurveyAnalysisCacheHandler;
import org.jeesl.controller.util.comparator.ejb.io.label.LabelEntityComparator;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.module.survey.SurveyAnalysisFactoryBuilder;
import org.jeesl.factory.builder.module.survey.SurveyCoreFactoryBuilder;
import org.jeesl.factory.builder.module.survey.SurveyTemplateFactoryBuilder;
import org.jeesl.factory.ejb.module.survey.EjbSurveyAnalysisToolFactory;
import org.jeesl.factory.ejb.module.survey.EjbSurveyConditionFactory;
import org.jeesl.factory.ejb.module.survey.EjbSurveyFactory;
import org.jeesl.factory.ejb.module.survey.EjbSurveyOptionFactory;
import org.jeesl.factory.ejb.module.survey.EjbSurveyOptionSetFactory;
import org.jeesl.factory.ejb.module.survey.EjbSurveyQuestionFactory;
import org.jeesl.factory.ejb.module.survey.EjbSurveySchemeFactory;
import org.jeesl.factory.ejb.module.survey.EjbSurveyScoreFactory;
import org.jeesl.factory.ejb.module.survey.EjbSurveySectionFactory;
import org.jeesl.factory.ejb.module.survey.EjbSurveyTemplateFactory;
import org.jeesl.factory.ejb.module.survey.EjbSurveyTemplateVersionFactory;
import org.jeesl.factory.ejb.module.survey.EjbSurveyValidationFactory;
import org.jeesl.interfaces.bean.sb.bean.SbSingleBean;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
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
import org.jeesl.jsf.handler.sb.SbSingleHandler;
import org.jeesl.web.mbean.prototype.system.AbstractAdminBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractSurveyBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
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
						DATA extends JeeslSurveyData<L,D,SURVEY,ANSWER,?>,
						OPTIONS extends JeeslSurveyOptionSet<L,D,TEMPLATE,OPTION>,
						OPTION extends JeeslSurveyOption<L,D>,
						
						ATT extends JeeslStatus<L,D,ATT>>
					extends AbstractAdminBean<L,D,LOC>
					implements Serializable,SbSingleBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractSurveyBean.class);
	
	protected final SurveyTemplateFactoryBuilder<L,D,LOC,SCHEME,VALGORITHM,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,CONDITION,VALIDATION,QE,SCORE,UNIT,OPTIONS,OPTION> fbTemplate;
	
	protected JeeslSurveyCoreFacade<L,D,SURVEY,SS,TC,SECTION,QUESTION,ANSWER,MATRIX,DATA,?> fCore;

	protected JeeslSurveyBean<SURVEY,TEMPLATE,SECTION,QUESTION,CONDITION,VALIDATION,QE,OPTIONS,OPTION,ATT> bSurvey;
	protected Long refId;

	protected final EjbSurveyTemplateVersionFactory<VERSION> efVersion;
	protected final EjbSurveySectionFactory<L,D,TEMPLATE,SECTION> efSection;
	protected final EjbSurveyQuestionFactory<SECTION,QUESTION,UNIT,OPTIONS,OPTION> efQuestion;
	protected final EjbSurveyConditionFactory<QUESTION,CONDITION,QE> efCondition;
	protected final EjbSurveyValidationFactory<QUESTION,VALIDATION> efValidation;

	protected final EjbSurveySchemeFactory<SCHEME,TEMPLATE> efScheme;
	protected final EjbSurveyScoreFactory<QUESTION,SCORE> efScore;
	protected final EjbSurveyTemplateFactory<TEMPLATE,TS,TC,SECTION,QUESTION> efTemplate;

	
	protected final SbSingleHandler<TC> sbhCategory; public SbSingleHandler<TC> getSbhCategory() {return sbhCategory;}
	
	protected final SbSingleHandler<LOC> sbhLocale; public SbSingleHandler<LOC> getSbhLocale() {return sbhLocale;}

	protected List<VERSION> versions; public List<VERSION> getVersions(){return versions;}
	protected List<SECTION> sections; public List<SECTION> getSections(){return sections;}
	protected final List<QUESTION> questions; public List<QUESTION> getQuestions(){return questions;}
	
	protected SURVEY survey; public SURVEY getSurvey() {return survey;} public void setSurvey(SURVEY survey) {this.survey = survey;}
	protected TEMPLATE template; public TEMPLATE getTemplate(){return template;} public void setTemplate(TEMPLATE template){this.template = template;}
	
	public AbstractSurveyBean(SurveyTemplateFactoryBuilder<L,D,LOC,SCHEME,VALGORITHM,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,CONDITION,VALIDATION,QE,SCORE,UNIT,OPTIONS,OPTION> fbTemplate
//			,SurveyCoreFactoryBuilder<L,D,LOC,SURVEY,SS,SCHEME,VALGORITHM,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,CONDITION,VALIDATION,QE,SCORE,UNIT,ANSWER,MATRIX,DATA,OPTIONS,OPTION,?> fbCore
			)
	{
		super(fbTemplate.getClassL(),fbTemplate.getClassD());
		this.fbTemplate=fbTemplate;
		
		efTemplate = fbTemplate.template();
		efSection = fbTemplate.section();
		

		efVersion = fbTemplate.version();
		efQuestion = fbTemplate.question();
		efCondition = fbTemplate.ejbCondition();
		efValidation = fbTemplate.ejbValidation();
		efScore = fbTemplate.score();

		efScheme = fbTemplate.efScheme();
		
		sbhCategory = new SbSingleHandler<TC>(fbTemplate.getClassTemplateCategory(),this);
		sbhLocale = new SbSingleHandler<LOC>(fbTemplate.getClassLocale(),this);
		
		questions = new ArrayList<QUESTION>();
	}
	
	protected abstract void initPageSettings();
	
	//JeeslTranslationBean<L,D,LOC> bTranslation,
	protected void initSuperSurvey(JeeslLocaleProvider<LOC> lp, JeeslFacesMessageBean bMessage,
			
			JeeslSurveyCoreFacade<L,D,SURVEY,SS,TC,SECTION,QUESTION,ANSWER,MATRIX,DATA,?> fCore,
			final JeeslSurveyBean<SURVEY,TEMPLATE,SECTION,QUESTION,CONDITION,VALIDATION,QE,OPTIONS,OPTION,ATT> bSurvey)
	{
//		super.initJeeslAdmin(bTranslation, bMessage);
		super.initJeeslAdmin(lp,bMessage);

		this.fCore = fCore;
		this.bSurvey = bSurvey;
	}
	
	protected void initLocales(String userLocale)
	{
		if(sbhLocale.getList().isEmpty())
		{
			List<LOC> list = new ArrayList<LOC>();
			try
			{
				list.add(fCore.fByCode(fbTemplate.getClassLocale(), localeCodes[0]));
			}
			catch (JeeslNotFoundException e) {e.printStackTrace();}
			sbhLocale.setList(list);
		}
		for(LOC loc : sbhLocale.getList())
		{
			if(loc.getCode().equals(userLocale)) {sbhLocale.setSelection(loc);}
		}

		sbhLocale.setDefault();
	}
}