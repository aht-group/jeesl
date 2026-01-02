package org.jeesl.web.mbean.prototype.module.survey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.jeesl.api.bean.JeeslSurveyBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.module.survey.JeeslSurveyCoreFacade;
import org.jeesl.api.facade.module.survey.JeeslSurveyTemplateFacade;
import org.jeesl.controller.handler.ui.helper.UiHelperSurvey;
import org.jeesl.controller.provider.ListLocaleProvider;
import org.jeesl.controller.web.util.AbstractLogMessage;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.module.survey.SurveyTemplateFactoryBuilder;
import org.jeesl.factory.ejb.module.survey.EjbSurveyOptionFactory;
import org.jeesl.factory.ejb.module.survey.EjbSurveyOptionSetFactory;
import org.jeesl.factory.ejb.util.EjbIdFactory;
import org.jeesl.interfaces.bean.sb.bean.SbSingleBean;
import org.jeesl.interfaces.bean.sb.handler.SbSingleSelection;
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
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.jsf.handler.PositionListReorderer;
import org.jeesl.model.json.module.survey.JsonSurveyValue;
import org.jeesl.util.comparator.ejb.PositionComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractAdminSurveyTemplateBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
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
	final static Logger logger = LoggerFactory.getLogger(AbstractAdminSurveyTemplateBean.class);
	
	private JeeslSurveyTemplateFacade<SCHEME,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,SCORE,OPTIONS,OPTION> fTemplate;
	
	protected final EjbSurveyOptionSetFactory<TEMPLATE,OPTIONS> efOptionSet;
	protected final EjbSurveyOptionFactory<QUESTION,OPTION> efOption;
	
	protected List<VERSION> nestedVersions; public List<VERSION> getNestedVersions(){return nestedVersions;}
	protected List<OPTIONS> optionSets; public List<OPTIONS> getOptionSets(){return optionSets;}
	private final List<CONDITION> conditions; public List<CONDITION> getConditions() {return conditions;}
	private final List<VALIDATION> validations; public List<VALIDATION> getValidations() {return validations;}
	private final List<QUESTION> triggerQuestions; public List<QUESTION> getTriggerQuestions() {return triggerQuestions;}
	private final  List<OPTION> triggerOptions; public List<OPTION> getTriggerOptions(){return triggerOptions;}
	protected List<OPTION> options; public List<OPTION> getOptions(){return options;}
	protected List<SCHEME> schemes; public List<SCHEME> getSchemes() {return schemes;}
	protected List<SCORE> scores; public List<SCORE> getScores() {return scores;}
	private final List<VALGORITHM> algorithms; public List<VALGORITHM> getAlgorithms() {return algorithms;}
	
	private TC category; public TC getCategory() {return category;} public void setCategory(TC category) {this.category = category;}
	protected VERSION version; public VERSION getVersion() {return version;}public void setVersion(VERSION version) {this.version = version;}
	protected VERSION nestedVersion; public VERSION getNestedVersion() {return nestedVersion;} public void setNestedVersion(VERSION nestedVersion) {this.nestedVersion = nestedVersion;}
	
	private SCHEME scheme; public SCHEME getScheme() {return scheme;} public void setScheme(SCHEME scheme) {this.scheme = scheme;}
	protected SECTION section; public SECTION getSection(){return section;} public void setSection(SECTION section){this.section = section;}
	protected QUESTION question; public QUESTION getQuestion(){return question;} public void setQuestion(QUESTION question){this.question = question;}
	private CONDITION condition; public CONDITION getCondition() {return condition;} public void setCondition(CONDITION condition) {this.condition = condition;}
	private VALIDATION validation; public VALIDATION getValidation() {return validation;}public void setValidation(VALIDATION validation) {this.validation = validation;}
	
	protected OPTIONS optionSet; public OPTIONS getOptionSet() {return optionSet;} public void setOptionSet(OPTIONS optionSet) {this.optionSet = optionSet;}
	private SCORE score; public SCORE getScore() {return score;} public void setScore(SCORE score) {this.score = score;}
	protected OPTION option; public OPTION getOption(){return option;} public void setOption(OPTION option){this.option = option;}
	
	private JsonSurveyValue questionCount; public JsonSurveyValue getQuestionCount() {return questionCount;}
	
	private UiHelperSurvey<VERSION,SECTION> uiHelper; public UiHelperSurvey<VERSION,SECTION> getUiHelper() {return uiHelper;}
	private Comparator<OPTION> cmpOption;
	
	public AbstractAdminSurveyTemplateBean(SurveyTemplateFactoryBuilder<L,D,LOC,SCHEME,VALGORITHM,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,CONDITION,VALIDATION,QE,SCORE,UNIT,OPTIONS,OPTION> fbTemplate)
	{
		super(fbTemplate);
		
		efOption = fbTemplate.efOption();
		efOptionSet = fbTemplate.efOptionSet();

		cmpOption = new PositionComparator<OPTION>();
		options = new ArrayList<OPTION>();

		conditions = new ArrayList<CONDITION>();
		validations = new ArrayList<VALIDATION>();
		triggerQuestions = new ArrayList<QUESTION>();
		triggerOptions = new ArrayList<OPTION>();
		algorithms = new ArrayList<VALGORITHM>();
		
		uiHelper = new UiHelperSurvey<VERSION,SECTION>();
	}
	
	protected void postConstructTemplate(String userLocale,
			JeeslLocaleProvider<LOC> bTranslation, JeeslFacesMessageBean bMessage,
			JeeslSurveyTemplateFacade<SCHEME,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,SCORE,OPTIONS,OPTION> fTemplate,
			JeeslSurveyCoreFacade<L,D,SURVEY,SS,TC,SECTION,QUESTION,ANSWER,MATRIX,DATA,CORRELATION> fCore,
			final JeeslSurveyBean<SURVEY,TEMPLATE,SECTION,QUESTION,CONDITION,VALIDATION,QE,OPTIONS,OPTION,ATT> bSurvey)
	{
		super.initSuperSurvey(bTranslation,bMessage,fCore,bSurvey);
		this.fTemplate=fTemplate;
		initPageSettings();
		super.initLocales(userLocale);

		algorithms.addAll(fTemplate.allOrderedPositionVisible(fbTemplate.getClassValidationAlgorithm()));
		
		versions = new ArrayList<VERSION>();
		sbhCategory.silentCallback();
	}
	
	@Override public void selectSbSingle(SbSingleSelection handler, EjbWithId ejb)
	{
		logger.info(ejb.toString());
		if(fbTemplate.getClassTemplateCategory().isAssignableFrom(ejb.getClass()))
		{
			try
			{
				clearSelection();
				initTemplate();
				reloadVersions();
			}
			catch (JeeslNotFoundException e) {e.printStackTrace();}
		}
	}
	
	protected void cancelAll() {clear(true,true,true,true,true,true,true,true,true,true);}
	public void cancelScheme(){clear(false,false,false,false,false,true,false,true,true,true);}
	public void calcelScore(){clear(false,false,false,false,false,false,true,true,true,true);}
	public void cancelCondition() {clear(false,false,false,false,false,false,true,true,true,true);}
	protected void clear(boolean cTemplate, boolean cVersion, boolean cSection, boolean cQuestion, boolean cOption, boolean rScheme, boolean rScore, boolean rSet,
						boolean rCondition, boolean rValidation)
	{
		if(cTemplate){template = null;}
		if(cVersion){version = null;}
		if(cSection){section = null;}
		if(cQuestion){question = null;options.clear();}	
		if(cOption){option=null;}
		if(rScheme){scheme = null;}
		if(rScore){score = null;}
		if(rSet){optionSet = null;}
		if(rCondition) {condition=null;}
		if(rValidation) {validation=null;}
	}
	
	protected void clearSelection()
	{
		if(sections!=null){sections.clear();}
		template = null;
		section=null;
		question=null;
	}
		
	//Template
	protected void reloadTemplate()
	{
		template = fTemplate.load(template,false,false);
		bSurvey.updateTemplate(template);
		version = template.getVersion();
		sections = template.getSections();
		schemes = template.getSchemes();
		optionSets = template.getOptionSets();
		reloadTemplateQuestions();
	}
	
	private void reloadTemplateQuestions()
	{
		triggerQuestions.clear();
		for(SECTION section : sections)
		{
			if(bSurvey.getMapQuestion().containsKey(section))
			{
				triggerQuestions.addAll(bSurvey.getMapQuestion().get(section));
			}
		}
	}
	
	protected void initTemplate() throws JeeslNotFoundException{}
	
	protected <E extends Enum<E>> void initTemplate(boolean withVersion, E statusCode)
	{
		if(sbhCategory.isSelected() && version!=null)
		{
			try
			{
				TS status = fTemplate.fByCode(fbTemplate.getClassTemplateStatus(),statusCode);
				template = fTemplate.fcSurveyTemplate(sbhCategory.getSelection(),version,status,nestedVersion);
				logger.info("Resolved "+fbTemplate.getClassTemplate().getSimpleName()+": "+template.toString());
				version = template.getVersion();
				reloadTemplate();
				section=null;
				question=null;
			}
			catch (JeeslNotFoundException e) {e.printStackTrace();}
		}
	}
	
	//Version
	public void addVersion()
	{
		logger.info(AbstractLogMessage.createEntity(fbTemplate.getClassVersion()));
		version = efVersion.build(refId);
		version.setName(efLang.buildEmpty(sbhLocale.getList()));
		version.setDescription(efDescription.buildEmpty(sbhLocale.getList()));
	}
	
	protected void reloadVersions()
	{
		if(refId!=null && refId<=0) {versions = new ArrayList<VERSION>();}
		else{versions = fTemplate.fVersions2(sbhCategory.getSelection(),refId);}
		if(debugOnInfo) {logger.info(AbstractLogMessage.reloaded(fbTemplate.getClassVersion(), versions));}
		
		nestedVersions = new ArrayList<VERSION>();
		for(TC c : sbhCategory.getList())
		{
			if(debugOnInfo) {logger.info("Testing nested for "+c.toString());}
			if(!c.equals(sbhCategory.getSelection()))
			{
				if(refId!=null && refId<=0) {}
				else{nestedVersions.addAll(fTemplate.fVersions2(c,refId));}
			}
		}
	}
	
	public void selectVersion() throws JeeslNotFoundException
	{
		clearSelection();
		logger.info(AbstractLogMessage.selectEntity(version));
		efLang.persistMissingLangs(fCore, sbhLocale.getList(), version);
		efDescription.persistMissingLangs(fCore, sbhLocale.getList(), version);
		version = fCore.find(fbTemplate.getClassVersion(), version);
		initTemplate();
		if(version.getTemplate()!=null && version.getTemplate().getNested()!=null){nestedVersion = version.getTemplate().getNested().getVersion();}
		uiHelper.check(version,sections);
	}
	
	public void saveVersion() throws JeeslConstraintViolationException, JeeslLockingException, JeeslNotFoundException
	{
		logger.info(AbstractLogMessage.saveEntity(version));
		if(nestedVersion!=null)
		{
			nestedVersion = fCore.find(fbTemplate.getClassVersion(),nestedVersion);
		}
		
		if(EjbIdFactory.isSaved(version))
		{
			if(nestedVersion!=null){version.getTemplate().setNested(nestedVersion.getTemplate());}
			version = fCore.save(version);
		}
		
		initTemplate();
		reloadVersions();
	}
	
	public void deleteVersion() throws JeeslConstraintViolationException, JeeslLockingException, JeeslNotFoundException
	{
		logger.info(AbstractLogMessage.deleteEntity(version));
		fTemplate.rmVersion2(version);
		clear(true,true,true,true,true,true,true,true,true,true);
		reloadVersions();
	}
	
	//Section
	public void addSection()
	{
		logger.info(AbstractLogMessage.createEntity(fbTemplate.getClassSection()));
		section = efSection.build(template,0);
		section.setName(efLang.buildEmpty(sbhLocale.getList()));
		section.setDescription(efDescription.buildEmpty(sbhLocale.getList()));
		nnb.doubleToA(section.getScoreLimit());
		nnb.doubleToB(section.getScoreNormalize());
	}
	
	public void selectSection()
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.selectEntity(section));}
		efLang.persistMissingLangs(fCore, sbhLocale.getList(), section);
		efDescription.persistMissingLangs(fCore, sbhLocale.getList(), section);
		loadSection();
		nnb.doubleToA(section.getScoreLimit());
		nnb.doubleToB(section.getScoreNormalize());
	}
	
	protected void loadSection()
	{
		section = fCore.load(section);
		questions.clear();
		questions.addAll(section.getQuestions());
		bSurvey.updateSection(section);
	}
		
	public void saveSection() throws JeeslConstraintViolationException, JeeslLockingException
	{
		logger.info(AbstractLogMessage.saveEntity(section));
		section.setScoreLimit(nnb.aToDouble());
		section.setScoreNormalize(nnb.bToDouble());
		section = fCore.save(section);
		reloadTemplate();
		loadSection();
	}
	
	public void rmSection() throws JeeslConstraintViolationException, JeeslLockingException
	{
		logger.info(AbstractLogMessage.deleteEntity(section));
		fCore.rm(section);
		section=null;
		question=null;
		reloadTemplate();
	}
	
	//Option Sets
	public void addSet()
	{
		logger.info(AbstractLogMessage.createEntity(fbTemplate.getOptionSetClass()));
		clear(false,false,true,true,true,true,true,false,true,true);
		optionSet = efOptionSet.build(template,optionSets);
		optionSet.setName(efLang.buildEmpty(sbhLocale.getList()));
	}
	
	public void saveSet() throws JeeslConstraintViolationException, JeeslLockingException
	{
		logger.info(AbstractLogMessage.saveEntity(optionSet));
		optionSet = fCore.save(optionSet);
		reloadTemplate();
		reloadOptionSet(true);
	}
	
	public void selectSet()
	{
		logger.info(AbstractLogMessage.selectEntity(optionSet));
		clear(false,false,true,true,true,true,true,false,true,true);
		optionSet = efLang.persistMissingLangs(fCore, sbhLocale.getList(), optionSet);
		reloadOptionSet(false);
	}
/*	
	public void rmSection() throws UtilsConstraintViolationException, UtilsLockingException
	{
		logger.info(AbstractLogMessage.rmEntity(section));
		fSurvey.rm(section);
		section=null;
		question=null;
		reloadTemplate();
	}
*/	
	//Question
	public void addQuestion()
	{
		logger.info(AbstractLogMessage.createEntity(fbTemplate.getClassQuestion()));
		question = efQuestion.build(section,null);
		question.setName(efLang.buildEmpty(sbhLocale.getList()));
		question.setText(efDescription.buildEmpty(sbhLocale.getList()));
		question.setDescription(efDescription.buildEmpty(sbhLocale.getList()));
	}
	
	public void selectQuestion()
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.selectEntity(question));}
		question = efLang.persistMissingLangs(fCore, sbhLocale.getList(), question);
		question = efDescription.persistMissingLangs(fCore, sbhLocale.getList(), question);
		if(question.getText()==null) {question.setText(efDescription.buildEmpty(sbhLocale.getList()));}
		for(LOC loc : sbhLocale.getList())
		{
			if(!question.getText().containsKey(loc.getCode()))
			{
				try
				{
					D d = fCore.persist(efDescription.create(loc.getCode(), ""));
					question.getText().put(loc.getCode(), d);
					question = fCore.update(question);
				}
				catch (JeeslConstraintViolationException e) {e.printStackTrace();}
				catch (JeeslLockingException e) {e.printStackTrace();}
			}
		}
		
		reloadQuestion();
		clear(false,false,false,false,true,true,true,true,true,true);
	}

	private void reloadQuestion()
	{
		question = fCore.find(fbTemplate.getClassQuestion(),question);
		question = fCore.load(question);
//		questionCount = fAnalysis.surveyCountAnswers(question);
		Collections.sort(question.getOptions(),cmpOption);
		options.clear(); options.addAll(question.getOptions());
		reloadConditions();
		reloadValidations();
		bSurvey.updateQuestion(question);
	}
	
	public void saveQuestion() throws JeeslConstraintViolationException, JeeslLockingException
	{
		logger.info(AbstractLogMessage.saveEntity(question));
		if(question.getUnit()!=null){question.setUnit(fCore.find(fbTemplate.getClassUnit(),question.getUnit()));}
		if(question.getOptionSet()!=null){question.setOptionSet(fCore.find(fbTemplate.getOptionSetClass(),question.getOptionSet()));}
		question = fCore.save(question);
		loadSection();
		reloadQuestion();
		reloadTemplateQuestions();
	}
	
	public void rmQuestion() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.deleteEntity(question));}
		fCore.rm(question);
		clear(false,false,false,true,true,true,true,true,true,true);
		loadSection();
	}
	
	//Option
	private void reloadOptionSet(boolean updateBean)
	{
		optionSet = fTemplate.find(fbTemplate.getOptionSetClass(),optionSet);
		optionSet = fTemplate.loadSurveyOptions(optionSet);
		Collections.sort(optionSet.getOptions(),cmpOption);
		options.clear();
		options.addAll(optionSet.getOptions());
		if(updateBean) {bSurvey.updateOptions(optionSet);}
	}
	
	public void addOption()
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.createEntity(fbTemplate.getClassOption()));}
		option = efOption.build(question,"");
		option.setName(efLang.buildEmpty(sbhLocale.getList()));
		option.setDescription(efDescription.buildEmpty(sbhLocale.getList()));
	}
	
	public void selectOption()
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.selectEntity(option));}
		ListLocaleProvider<LOC> lp = ListLocaleProvider.instance(sbhLocale.getList());
		option = efLang.persistMissingLangs(fCore, lp, option);
		option = efDescription.persistMissingLangs(fCore, sbhLocale.getList(), option);
	}
	
	public void saveQuestionOption() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.saveEntity(option));}
		option = fTemplate.saveOption2(question,option);
		reloadQuestion();
		bMessage.growlSaved(option);
	}
	public void saveSetOption() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.saveEntity(option));}
		option = fTemplate.saveOption2(optionSet,option);
		reloadOptionSet(true);
		bMessage.growlSaved(option);
	}
	
	public void delQuestionOption() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.deleteEntity(option));}
		fTemplate.rmOption2(question,option);
		bMessage.growlDeleted(option);
		reloadQuestion();
		
	}
	public void rmSetOption() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.deleteEntity(option));}
		fTemplate.rmOption2(optionSet,option);
		bMessage.growlDeleted(option);
		clear(false,false,true,true,true,true,true,false,true,true);
		reloadOptionSet(true);
		
	}
	
	//Scheme
	public void addScheme()
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.createEntity(fbTemplate.getClassScheme()));}
		scheme = efScheme.build(template, "", schemes);
		scheme.setName(efLang.buildEmpty(lp.getLocales()));
		scheme.setDescription(efDescription.buildEmpty(lp.getLocales()));
	}
	
	public void selectScheme()
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.selectEntity(scheme));}
	}
	
	public void saveScheme() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.saveEntity(scheme));}
		scheme = fCore.save(scheme);
		reloadTemplate();
	}
	
	public void addScore()
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.createEntity(fbTemplate.getClassScore()));}
		score = efScore.build(question);
	}
	
	public void saveScore() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.saveEntity(score));}
		score = fCore.save(score);
		reloadQuestion();
		bMessage.growlSaved(score);
	}
	
	private void reloadConditions()
	{
		conditions.clear();
		conditions.addAll(fTemplate.allForParent(fbTemplate.getClassCondition(), question));
	}
	
	public void addCondition()
	{
		clear(false,false,false,false,true,true,true,true,false,true);
		if(debugOnInfo){logger.info(AbstractLogMessage.createEntity(fbTemplate.getClassCondition()));}
		QUESTION triggerQuestion = null; if(!triggerQuestions.isEmpty()) {triggerQuestion = triggerQuestions.get(0);}
		QE element = null; if(!bSurvey.getElements().isEmpty()){element = bSurvey.getElements().get(0);}
		condition = efCondition.build(question, element,triggerQuestion, conditions);
		triggerChanged();
	}
	
	public void triggerChanged()
	{
		triggerOptions.clear();
		if(condition.getTriggerQuestion()!=null && bSurvey.getMapOption().containsKey(condition.getTriggerQuestion())) 
		{
			triggerOptions.addAll(bSurvey.getMapOption().get(condition.getTriggerQuestion()));
		}
	}
	
	public void saveCondition() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.saveEntity(condition));}
		condition.setTriggerQuestion(fTemplate.find(fbTemplate.getClassQuestion(), condition.getTriggerQuestion()));
		condition.setElement(fTemplate.find(fbTemplate.getClassElement(), condition.getElement()));
		if(condition.getOption()!=null) {condition.setOption(fTemplate.find(fbTemplate.getClassOption(), condition.getOption()));}
		condition = fTemplate.save(condition);
		reloadConditions();
	}
	
	public void selectCondition()
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.selectEntity(condition));}
		condition = fTemplate.find(fbTemplate.getClassCondition(), condition);
		triggerChanged();
	}
	
	public void deleteCondition() throws JeeslConstraintViolationException
	{
		fTemplate.rm(condition);
		clear(false,false,false,false,true,true,true,true,true,true);
		reloadConditions();
	}
	
	
	private void reloadValidations()
	{
		validations.clear();
		validations.addAll(fTemplate.allForParent(fbTemplate.getClassValidation(), question));
	}
	
	public void addValidation()
	{
		clear(false,false,false,false,true,true,true,true,true,true);
		if(debugOnInfo){logger.info(AbstractLogMessage.createEntity(fbTemplate.getClassValidation()));}

		validation = efValidation.build(question,validations);
		validation.setDescription(efDescription.buildEmpty(sbhLocale.getList()));
	}
	
	public void saveValidation() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.saveEntity(validation));}
		validation.setAlgorithm(fTemplate.find(fbTemplate.getClassValidationAlgorithm(), validation.getAlgorithm()));
		validation = fTemplate.save(validation);
		reloadValidations();
	}
	
	public void selectValidation()
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.selectEntity(validation));}
		validation = fTemplate.find(fbTemplate.getClassValidation(), validation);
		validation = efDescription.persistMissingLangs(fTemplate,sbhLocale.getList(), validation);
	}
	
	public void reorderSections() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fCore, sections);}
	public void reorderQuestions() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fCore, questions);}
	public void reorderConditions() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fTemplate, conditions);}
	public void reorderValidations() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fTemplate, validations);}
	public void reorderOptions() throws JeeslConstraintViolationException, JeeslLockingException
	{
		PositionListReorderer.reorder(fCore, options);
		if(questions!=null) {reloadQuestion();}
		if(optionSet!=null) {reloadOptionSet(true);}
	}
}