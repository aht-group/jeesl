package org.jeesl.web.mbean.prototype.module.survey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.jeesl.api.bean.JeeslSurveyBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.io.JeeslIoDomainFacade;
import org.jeesl.api.facade.module.survey.JeeslSurveyAnalysisFacade;
import org.jeesl.api.facade.module.survey.JeeslSurveyCoreFacade;
import org.jeesl.api.facade.module.survey.JeeslSurveyTemplateFacade;
import org.jeesl.api.facade.system.JeeslJobFacade;
import org.jeesl.controller.handler.module.survey.SurveyAnalysisCacheHandler;
import org.jeesl.controller.util.comparator.ejb.io.label.LabelEntityComparator;
import org.jeesl.controller.web.util.AbstractLogMessage;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.module.survey.SurveyAnalysisFactoryBuilder;
import org.jeesl.factory.builder.module.survey.SurveyTemplateFactoryBuilder;
import org.jeesl.factory.ejb.module.survey.EjbSurveyAnalysisFactory;
import org.jeesl.factory.ejb.module.survey.EjbSurveyAnalysisQuestionFactory;
import org.jeesl.factory.ejb.module.survey.EjbSurveyAnalysisToolFactory;
import org.jeesl.interfaces.bean.sb.bean.SbSingleBean;
import org.jeesl.interfaces.bean.sb.handler.SbSingleSelection;
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
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.jsf.handler.PositionListReorderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractAdminSurveyAnalysisBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
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
	final static Logger logger = LoggerFactory.getLogger(AbstractAdminSurveyAnalysisBean.class);
	
	protected final SurveyAnalysisFactoryBuilder<L,D,TEMPLATE,QUESTION,QE,SCORE,ANSWER,MATRIX,DATA,OPTION,CORRELATION,DOMAIN,QUERY,PATH,DENTITY,DATTRIBUTE,ANALYSIS,AQ,AT,ATT,TOOLCACHETEMPLATE> fbAnalysis;
	
	JeeslIoDomainFacade<L,D,DOMAIN,QUERY,PATH,DENTITY,DATTRIBUTE,?,?> fDomain;
	private JeeslSurveyTemplateFacade<SCHEME,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,SCORE,OPTIONS,OPTION> fTemplate;
	protected JeeslSurveyAnalysisFacade<SURVEY,QUESTION,DATA,OPTIONS,OPTION,CORRELATION,DOMAIN,QUERY,PATH,DENTITY,DATTRIBUTE,ANALYSIS,AQ,AT,ATT> fAnalysis;
	
	protected SurveyAnalysisCacheHandler<SURVEY,SECTION,QUESTION,MATRIX,DATA,OPTIONS,OPTION,CORRELATION,DOMAIN,QUERY,PATH,DENTITY,DATTRIBUTE,ANALYSIS,AQ,AT,ATT,TOOLCACHETEMPLATE,CACHE> cacheHandler;
	
	protected List<ANALYSIS> analyses; public List<ANALYSIS> getAnalyses(){return analyses;}
	protected List<DOMAIN> domains; public List<DOMAIN> getDomains(){return domains;}
	protected List<QUESTION> questions; public List<QUESTION> getQuestions(){return questions;}
	protected List<OPTIONS> optionSets; public List<OPTIONS> getOptionSets(){return optionSets;}
	protected List<OPTION> options; public List<OPTION> getOptions(){return options;}
	protected List<SCHEME> schemes; public List<SCHEME> getSchemes() {return schemes;}
	protected List<SCORE> scores; public List<SCORE> getScores() {return scores;}
	private final List<QUERY> queries; public List<QUERY> getQueries() {return queries;}
	private final List<DENTITY> correlations; public List<DENTITY> getCorrelations() {return correlations;}
	private final List<DATTRIBUTE> attributes; public List<DATTRIBUTE> getAttributes() {return attributes;}
	private List<AT> tools; public List<AT> getTools() {return tools;}
	private List<ATT> toolTypes; public List<ATT> getToolTypes() {return toolTypes;}
	private List<QE> questionElements; public List<QE> getQuestionElements() {return questionElements;}
	
	protected VERSION version; public VERSION getVersion() {return version;}public void setVersion(VERSION version) {this.version = version;}
	protected VERSION nestedVersion; public VERSION getNestedVersion() {return nestedVersion;} public void setNestedVersion(VERSION nestedVersion) {this.nestedVersion = nestedVersion;}
	private ANALYSIS analysis; public ANALYSIS getAnalysis() {return analysis;} public void setAnalysis(ANALYSIS analysis) {this.analysis = analysis;}
	protected SECTION section; public SECTION getSection(){return section;} public void setSection(SECTION section){this.section = section;}
	protected QUESTION question; public QUESTION getQuestion(){return question;} public void setQuestion(QUESTION question){this.question = question;}
	private AQ analysisQuestion; public AQ getAnalysisQuestion() {return analysisQuestion;} public void setAnalysisQuestion(AQ analysisQuestion) {this.analysisQuestion = analysisQuestion;}
	private AT tool; public AT getTool() {return tool;} public void setTool(AT tool) {this.tool = tool;}
	
	private final EjbSurveyAnalysisFactory<TEMPLATE,ANALYSIS> efAnalysis;
	private final EjbSurveyAnalysisQuestionFactory <L,D,QUESTION,ANALYSIS,AQ> efAnalysisQuestion;
	private final EjbSurveyAnalysisToolFactory <L,D,AQ,AT,ATT> efAnalysisTool;
	
	private final Comparator<DENTITY> cpDomainEntity;
	
	
	public AbstractAdminSurveyAnalysisBean(SurveyTemplateFactoryBuilder<L,D,LOC,SCHEME,VALGORITHM,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,CONDITION,VALIDATION,QE,SCORE,UNIT,OPTIONS,OPTION> fbTemplate,
											
											SurveyAnalysisFactoryBuilder<L,D,TEMPLATE,QUESTION,QE,SCORE,ANSWER,MATRIX,DATA,OPTION,CORRELATION,DOMAIN,QUERY,PATH,DENTITY,DATTRIBUTE,ANALYSIS,AQ,AT,ATT,TOOLCACHETEMPLATE> fbAnalysis)
	{
		super(fbTemplate);
		this.fbAnalysis=fbAnalysis;
		
		queries = new ArrayList<QUERY>();
		correlations = new ArrayList<DENTITY>();
		attributes = new ArrayList<DATTRIBUTE>();
		
		efAnalysis = fbAnalysis.ejbAnalysis();
		efAnalysisQuestion = fbAnalysis.ejbAnalysisQuestion();
		efAnalysisTool = fbAnalysis.ejbAnalysisTool();
		
		cpDomainEntity = new LabelEntityComparator().factory(LabelEntityComparator.Type.position);
	}
	
	protected void initSuperAnalysis(String userLocale, JeeslLocaleProvider<LOC> lp, JeeslFacesMessageBean bMessage,
			JeeslSurveyTemplateFacade<SCHEME,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,SCORE,OPTIONS,OPTION> fTemplate,
			JeeslSurveyCoreFacade<L,D,SURVEY,SS,TC,SECTION,QUESTION,ANSWER,MATRIX,DATA,CORRELATION> fCore,
			JeeslSurveyAnalysisFacade<SURVEY,QUESTION,DATA,OPTIONS,OPTION,CORRELATION,DOMAIN,QUERY,PATH,DENTITY,DATTRIBUTE,ANALYSIS,AQ,AT,ATT> fAnalysis,
			JeeslIoDomainFacade<L,D,DOMAIN,QUERY,PATH,DENTITY,DATTRIBUTE,?,?> fDomain,
			JeeslJobFacade<TOOLCACHETEMPLATE,?,?,?,?,?,?,?,?,CACHE,?,?,?,?> fJob,
			final JeeslSurveyBean<SURVEY,TEMPLATE,SECTION,QUESTION,CONDITION,VALIDATION,QE,OPTIONS,OPTION,ATT> bSurvey)
	{
		super.initSuperSurvey(lp,bMessage,fCore,bSurvey);
		this.fTemplate=fTemplate;
		this.fAnalysis=fAnalysis;
		this.fDomain=fDomain;
		cacheHandler = new SurveyAnalysisCacheHandler<>(fJob,fAnalysis);
		initPageSettings();
		super.initLocales(userLocale);
		
		domains = fAnalysis.allOrderedPosition(fbAnalysis.getClassDomain());		
		toolTypes = bSurvey.getToolTypes();
		questionElements = fCore.allOrderedPositionVisible(fbTemplate.getClassElement());
		
		versions = new ArrayList<VERSION>();
		
		correlations.addAll(fAnalysis.all(fbAnalysis.getClassDomainEntity()));
		Collections.sort(correlations,cpDomainEntity);
		
		sbhCategory.silentCallback();
	}
	
	@Override public void selectSbSingle(SbSingleSelection handler, EjbWithId ejb)
	{
		logger.info(ejb.toString());
		if(fbTemplate.getClassTemplateCategory().isAssignableFrom(ejb.getClass()))
		{
			reset(true,true,true,true,true,true);
			versions = fTemplate.fVersions2(sbhCategory.getSelection(),refId);
		}
	}
	
	private void reset(boolean rTemplate, boolean rVersion, boolean rSection, boolean rQuestion, boolean rAnalysisQuestion, boolean rTool)
	{
		if(rTemplate){template = null;}
		if(rVersion){version = null;}
		if(rSection){section = null;}
		if(rQuestion){question = null;}
		if(rAnalysisQuestion) {analysisQuestion=null;}
		if(rTool) {tool = null;nnb.reset();}
	}
	
	public void selectVersion() throws JeeslNotFoundException
	{
		reset(false,false,true,true,true,true);
		logger.info(AbstractLogMessage.selectEntity(version));
		version = fCore.find(fbTemplate.getClassVersion(), version);
		reloadAnalyses();
		sections = bSurvey.getMapSection().get(version.getTemplate());
	}
	
	private void reloadAnalyses()
	{
		analyses = fAnalysis.allForParent(fbAnalysis.getClassAnalysis(), version.getTemplate());
	}
		
	public void addAnalysis()
	{
		logger.info(AbstractLogMessage.createEntity(fbAnalysis.getClassAnalysis()));
		analysis = efAnalysis.build(version.getTemplate(),analyses);
		analysis.setName(efLang.buildEmpty(sbhLocale.getList()));
	}
	
	public void saveAnalysis() throws JeeslConstraintViolationException, JeeslLockingException
	{
		logger.info(AbstractLogMessage.saveEntity(analysis));
		analysis.setDomain(fAnalysis.find(fbAnalysis.getClassDomain(),analysis.getDomain()));
		if(analysis.getEntity()!=null) {analysis.setEntity(fAnalysis.find(fbAnalysis.getClassDomainEntity(),analysis.getEntity()));}
		
		analysis = fAnalysis.save(analysis);
		reloadAnalyses();
		reloadAnalysis();
	}
	
	public void selectAnalysis()
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.selectEntity(analysis));}
		analysis = efLang.persistMissingLangs(fCore, sbhLocale.getList(), analysis);
//		analysis = efDescription.persistMissingLangs(fSurvey, localeCodes, analysis);
		reset(false,false,true,true,true,true);
		reloadAnalysis();
	}
	
	private void reloadAnalysis()
	{
		queries.clear();
		queries.addAll(fAnalysis.allForParent(fbAnalysis.getClassDomainQuery(), analysis.getDomain()));
		
		changeCorrelation();
	}
	
	public void changeCorrelation()
	{
		if(analysis.getEntity()!=null)
		{
			attributes.clear();
			attributes.addAll(fDomain.fDomainAttributes(analysis.getEntity()));
		}
	}
	
	public void deleteAnalysis() throws JeeslConstraintViolationException
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.deleteEntity(analysis));}
		fCore.rm(analysis);
		reset(true,false,false,true,true,true);
	}

	public void selectSection()
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.selectEntity(section));}
		questions = bSurvey.getMapQuestion().get(section);
		reset(false,false,false,true,true,true);
	}
	
	public void selectQuestion()
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.selectEntity(question));}
		try
		{
			analysisQuestion = fAnalysis.fAnalysis(analysis, question);
			analysisQuestion = efLang.persistMissingLangs(fCore, sbhLocale.getList(), analysisQuestion);
			reloadTools();
		}
		catch (JeeslNotFoundException e)
		{
			analysisQuestion = efAnalysisQuestion.build(analysis, question);
			analysisQuestion.setName(efLang.buildEmpty(sbhLocale.getList()));
		}
		reset(false,false,false,false,false,true);
	}
	
	public void saveAnalysisQuestion() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.saveEntity(analysisQuestion));}
		analysisQuestion = fAnalysis.save(analysisQuestion);
		reloadTools();
	}
	
	private void reloadTools()
	{
		tools = fAnalysis.allForParent(fbAnalysis.getClassAnalysisTool(), analysisQuestion);
	}
	
	public void addTool()
	{
		tool = efAnalysisTool.build(analysisQuestion, toolTypes.get(0),tools);
		nnb.integerToA(tool.getCacheExpire());
	}
	
	public void saveTool() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.saveEntity(tool));}
		tool.setType(fAnalysis.find(fbAnalysis.getAttClass(),tool.getType()));
		tool.setElement(fAnalysis.find(fbTemplate.getClassElement(),tool.getElement()));
		tool.setCacheExpire(nnb.aToInteger());
		tool = fAnalysis.save(tool);
		cacheHandler.remove(tool);
		reloadTools();
	}
	
	public void selectTool()
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.selectEntity(tool));}
		tool = fCore.find(fbAnalysis.getClassAnalysisTool(),tool);
		nnb.integerToA(tool.getCacheExpire());
	}
	
	public void deleteTool() throws JeeslConstraintViolationException
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.deleteEntity(tool));}
		cacheHandler.remove(tool);
		
		fAnalysis.rm(tool);
		reloadTools();
		reset(false,false,false,false,false,true);
	}
	
	public void reorderAnalyses() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fCore, analyses);}
}