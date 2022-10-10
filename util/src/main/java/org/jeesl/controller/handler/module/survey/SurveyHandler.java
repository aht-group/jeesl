package org.jeesl.controller.handler.module.survey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jeesl.api.bean.JeeslSurveyBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.module.survey.JeeslSurveyCoreFacade;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.module.survey.SurveyCoreFactoryBuilder;
import org.jeesl.factory.ejb.module.survey.EjbSurveyAnswerFactory;
import org.jeesl.factory.ejb.module.survey.EjbSurveyDataFactory;
import org.jeesl.factory.ejb.module.survey.EjbSurveyMatrixFactory;
import org.jeesl.factory.ejb.util.EjbIdFactory;
import org.jeesl.factory.txt.module.survey.TxtSurveyAnswerFactory;
import org.jeesl.factory.txt.module.survey.TxtSurveySectionFactory;
import org.jeesl.interfaces.controller.handler.system.io.JeeslLogger;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurvey;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurveyTemplate;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurveyTemplateCategory;
import org.jeesl.interfaces.model.module.survey.correlation.JeeslSurveyCorrelation;
import org.jeesl.interfaces.model.module.survey.data.JeeslSurveyAnswer;
import org.jeesl.interfaces.model.module.survey.data.JeeslSurveyData;
import org.jeesl.interfaces.model.module.survey.data.JeeslSurveyMatrix;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyCondition;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyOption;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyQuestion;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveySection;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyValidation;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.model.pojo.map.id.Nested3IdMap;
import org.jeesl.util.comparator.pojo.BooleanComparator;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SurveyHandler<L extends JeeslLang, D extends JeeslDescription,
							SURVEY extends JeeslSurvey<L,D,?,TEMPLATE,DATA>,
							TEMPLATE extends JeeslSurveyTemplate<L,D,?,TEMPLATE,?,?,TC,SECTION,?,?>,
							TC extends JeeslSurveyTemplateCategory<L,D,TC,?>,
							SECTION extends JeeslSurveySection<L,D,TEMPLATE,SECTION,QUESTION>,
							QUESTION extends JeeslSurveyQuestion<L,D,SECTION,CONDITION,VALIDATION,?,?,?,?,OPTION,?>,
							CONDITION extends JeeslSurveyCondition<QUESTION,?,OPTION>,
							VALIDATION extends JeeslSurveyValidation<L,D,QUESTION,?>,
							ANSWER extends JeeslSurveyAnswer<L,D,QUESTION,MATRIX,DATA,OPTION>,
							MATRIX extends JeeslSurveyMatrix<L,D,ANSWER,OPTION>,
							DATA extends JeeslSurveyData<L,D,SURVEY,ANSWER,CORRELATION>,
							OPTION extends JeeslSurveyOption<L,D>,
							CORRELATION extends JeeslSurveyCorrelation<DATA>>
	implements Serializable
{
	final static Logger logger = LoggerFactory.getLogger(SurveyHandler.class);
	private static final long serialVersionUID = 1L;
	
	private JeeslLogger jogger; public void setJogger(JeeslLogger jogger) {this.jogger = jogger;}
	
	private final JeeslFacesMessageBean bMessage;
	private final JeeslSurveyBean<?,?,SURVEY,?,?,TEMPLATE,?,?,TC,SECTION,QUESTION,CONDITION,VALIDATION,?,?,?,ANSWER,MATRIX,DATA,?,OPTION,CORRELATION,?> bSurvey;
	
	private final SurveyConditionalHandler<TEMPLATE,SECTION,QUESTION,CONDITION,ANSWER,OPTION> condition; public SurveyConditionalHandler<TEMPLATE, SECTION, QUESTION, CONDITION, ANSWER, OPTION> getCondition() {return condition;}
	private final SurveyValidationHandler<L,D,TEMPLATE,SECTION,QUESTION,VALIDATION,ANSWER,OPTION> validation; public SurveyValidationHandler<L,D,TEMPLATE,SECTION,QUESTION,VALIDATION,ANSWER,OPTION> getValidation() {return validation;}

	private final Class<SECTION> cSection;

	private final JeeslSurveyCoreFacade<?,?,?,SURVEY,?,?,TEMPLATE,?,?,TC,SECTION,QUESTION,?,?,?,ANSWER,MATRIX,DATA,?,OPTION,CORRELATION> fSurvey;
	
	private final EjbSurveyAnswerFactory<SECTION,QUESTION,ANSWER,MATRIX,DATA,OPTION> efAnswer;
	private final EjbSurveyMatrixFactory<ANSWER,MATRIX,OPTION> efMatrix;
	private final EjbSurveyDataFactory<SURVEY,DATA,CORRELATION> efData;
	private final TxtSurveySectionFactory<?,?,SECTION> tfSection;
	private final TxtSurveyAnswerFactory<?,?,ANSWER,MATRIX,OPTION> tfAnswer;
	
	private List<OPTION> districts; public List<OPTION> getDistricts() {return districts;} public void setDistricts(List<OPTION> districts) {this.districts = districts;}

	private Map<QUESTION,ANSWER> answers; public Map<QUESTION,ANSWER> getAnswers() {return answers;}
	private Map<QUESTION,Object> multiOptions; public Map<QUESTION,Object> getMultiOptions() {return multiOptions;}
	private Nested3IdMap<MATRIX> matrix; public Nested3IdMap<MATRIX> getMatrix() {return matrix;}

	private TEMPLATE template; public TEMPLATE getTemplate() {return template;}
	private DATA surveyData; public DATA getSurveyData(){return surveyData;} public void setSurveyData(DATA surveyData) {this.surveyData = surveyData;}
	private TC category; public TC getCategory() {return category;} public void setCategory(TC category) {this.category = category;}
	
	private Set<SECTION> activeSections;
	private SECTION activeSection; public SECTION getActiveSection() {return activeSection;} public void setActiveSection(SECTION activeSection) {this.activeSection = activeSection;}

	private boolean showDataSave; public boolean isShowDataSave() {return showDataSave;}
	private boolean showDataFields; public boolean isShowDataFields() {return showDataFields;}

	private boolean showAssessment; public boolean isShowAssessment() {return showAssessment;}
	private boolean allowAssessment; public boolean isAllowAssessment() {return allowAssessment;} //public void setAllowAssessment(boolean allowAssessment) {this.allowAssessment = allowAssessment;}
	
	public static boolean debug = true;
	public static int debugDelay = 1000;
	
	public SurveyHandler(JeeslFacesMessageBean bMessage,
			final JeeslSurveyCoreFacade<?,?,?,SURVEY,?,?,TEMPLATE,?,?,TC,SECTION,QUESTION,?,?,?,ANSWER,MATRIX,DATA,?,OPTION,CORRELATION> fSurvey,
			JeeslSurveyBean<?,?,SURVEY,?,?,TEMPLATE,?,?,TC,SECTION,QUESTION,CONDITION,VALIDATION,?,?,?,ANSWER,MATRIX,DATA,?,OPTION,CORRELATION,?> bSurvey,
			
			final SurveyCoreFactoryBuilder<L,D,?,SURVEY,?,?,?,TEMPLATE,?,?,TC,SECTION,QUESTION,CONDITION,VALIDATION,?,?,?,ANSWER,MATRIX,DATA,?,OPTION,CORRELATION,?> fBSurvey)
	{
		this.bMessage=bMessage;
		this.fSurvey=fSurvey;
		this.bSurvey=bSurvey;
		
		showDataSave = false;
		showDataFields = false;
		
		showAssessment = false;
		allowAssessment = true;

		condition = new SurveyConditionalHandler<TEMPLATE,SECTION,QUESTION,CONDITION,ANSWER,OPTION>(fBSurvey,bSurvey);
		validation = new SurveyValidationHandler<L,D,TEMPLATE,SECTION,QUESTION,VALIDATION,ANSWER,OPTION>(bSurvey);
		
		answers = new HashMap<QUESTION,ANSWER>();
		matrix = new Nested3IdMap<MATRIX>();
		multiOptions = new HashMap<QUESTION,Object>();
		
		cSection = fBSurvey.getClassSection();
		
		efData = fBSurvey.data();
		efAnswer = fBSurvey.answer();
		efMatrix = fBSurvey.ejbMatrix();
		tfSection = fBSurvey.txtSection();
		tfAnswer = fBSurvey.txtAnswer();
		
		activeSections = new HashSet<SECTION>();
	}
	
	public void reset()
	{
		answers.clear();
		matrix.clear();
		multiOptions.clear();
		activeSections.clear();
		condition.clear();
		
		showDataSave = false;
		surveyData = null;
		showAssessment = false;
	}
	
	public void prepare(SURVEY survey, CORRELATION correlation)
	{
		buildControls(survey);
		if(jogger!=null) {jogger.milestone(SurveyHandler.class.getSimpleName(),"buildControls");}
		
		showAssessment = true;
		if(SurveyHandler.debug){logger.warn("prepare fData()");try {Thread.sleep(SurveyHandler.debugDelay);} catch (InterruptedException e) {e.printStackTrace();}}
		try
		{
			surveyData = fSurvey.fData(correlation);
			if(jogger!=null) {jogger.milestone(SurveyHandler.class.getSimpleName(),"fSurvey.fData(..)");}
		}
		catch (JeeslNotFoundException e)
		{
			surveyData = efData.build(survey,correlation);
			if(jogger!=null) {jogger.milestone(SurveyHandler.class.getSimpleName(),"efData.build(..)");}
		}
		jogger.ofxMilestones(System.out);
		
		template = survey.getTemplate();
		condition.init(template);
		if(jogger!=null) {jogger.milestone(SurveyHandler.class.getSimpleName(),"condition.init(..)");}
		
		validation.init(template);
		if(jogger!=null) {jogger.milestone(SurveyHandler.class.getSimpleName(),"validation.init(..)");}
		
		if(bSurvey.getMapSection().containsKey(template)){activeSections.addAll(bSurvey.getMapSection().get(template));}
		if(SurveyHandler.debug){logger.warn("Preparing Survey for correlation:"+correlation.toString()+" and data:"+surveyData.toString());}
	}
	
	public void prepareNested(SURVEY survey, CORRELATION correlation)
	{
		buildControls(survey);
		
		showAssessment = true;
		try {surveyData = fSurvey.fData(correlation);}
		catch (JeeslNotFoundException e){surveyData = efData.build(survey,correlation);}
		template = survey.getTemplate().getNested();
		condition.init(template);
		validation.init(template);
		activeSections.addAll(bSurvey.getMapSection().get(template));
	}
	
	private void buildControls(SURVEY survey)
	{
		boolean surveyOpen = survey.getStatus().getCode().equals(JeeslSurvey.Status.open.toString());
		boolean surveyPreparation = survey.getStatus().getCode().equals(JeeslSurvey.Status.preparation.toString());
		boolean surveyInPeriod = (new Interval(new DateTime(survey.getStartDate()), new DateTime(survey.getEndDate()))).containsNow();
		
		logger.info("surveyOpen:"+surveyOpen+" surveyDate:"+surveyInPeriod);
		showDataSave = surveyOpen && surveyInPeriod;
		showDataFields = surveyPreparation || (surveyOpen && surveyInPeriod);
	}
	
	public void reloadAnswers(){reloadAnswers(true);}
	public void reloadAnswers(boolean withMatrix)
	{
		boolean dbLookup = EjbIdFactory.isSaved(surveyData);
		answers.clear();
		multiOptions.clear();
		
		if(SurveyHandler.debug){logger.warn("Reloading Answers (dbLookup:"+dbLookup+")");}
		if(dbLookup)
		{
			List<SECTION> filterSection = null;
			if(activeSections!=null && !activeSections.isEmpty()){filterSection = new ArrayList<SECTION>(activeSections);}
			if(SurveyHandler.debug){logger.warn("fAnswers for "+filterSection.size()+" "+JeeslSurveySection.class.getSimpleName()+": "+tfSection.codes(filterSection));try {Thread.sleep(SurveyHandler.debugDelay);} catch (InterruptedException e) {e.printStackTrace();}}
			answers = efAnswer.toMapQuestion(fSurvey.fAnswers(surveyData, true, filterSection));
		}

		List<ANSWER> loadMatrix = new ArrayList<ANSWER>();
		if(bSurvey.getMapSection().get(template)!=null)
		{
			for(SECTION s : bSurvey.getMapSection().get(template))
			{
				boolean isProcessSection = processSection(s);
				boolean hasQuestions = (bSurvey.getMapQuestion()!=null) && (bSurvey.getMapQuestion().containsKey(s));
				if(SurveyHandler.debug){logger.warn("Processing Section "+s.toString()+" process:"+isProcessSection+" hasQuestions:"+hasQuestions);}
				
				if(isProcessSection && hasQuestions)
				{		
					for(QUESTION q : bSurvey.getMapQuestion().get(s))
					{	
						if(q.isVisible())
						{
							ANSWER a;
							if(!answers.containsKey(q))
							{
								if(SurveyHandler.debug){logger.warn("Building new Answer for Question"+q.toString());}
								a = efAnswer.build(q, surveyData);
								if(BooleanComparator.active(q.getShowSelectOne()) && BooleanComparator.inactive(q.getShowEmptyOption()) && bSurvey.getMapOption().containsKey(q) && !bSurvey.getMapOption().get(q).isEmpty())
								{
									a.setOption(bSurvey.getMapOption().get(q).get(0));
								}
							}
							else
							{
								a = answers.get(q);
								if(SurveyHandler.debug){logger.warn("Using Answer "+a.getId()+" for Question "+q.toString()+" "+tfAnswer.build(a));}
							}
							if(BooleanComparator.active(q.getShowMatrix())){loadMatrix.add(a);}
							answers.put(q,a);
						}
					}
				}
			}
		}

		if(withMatrix){matrixLoader(loadMatrix);}
		
		condition.evaluteMap(answers);
		logger.trace("Answers loaded: " + answers.size());
	}
	
	private void matrixLoader(List<ANSWER> loadMatrix)
	{
		matrix = null;
		matrix = new Nested3IdMap<MATRIX>();
		
		List<ANSWER> savedAnswer = new ArrayList<ANSWER>();
		for(ANSWER a : loadMatrix){if(EjbIdFactory.isSaved(a)){savedAnswer.add(a);}}
		
		List<MATRIX> list = fSurvey.fCells(savedAnswer);
		logger.info("MATRIX.Cells: "+list.size());
		for(MATRIX m : list)
		{
			matrix.put(m.getAnswer().getQuestion().getId(),m.getRow().getId(),m.getColumn().getId(),m);
		}
		for(ANSWER a : loadMatrix)
		{
			if(bSurvey.getMatrixRows().get(a.getQuestion())!=null && bSurvey.getMatrixCols().get(a.getQuestion())!=null)
			{
				for(OPTION row : bSurvey.getMatrixRows().get(a.getQuestion()))
				{
					for(OPTION column : bSurvey.getMatrixCols().get(a.getQuestion()))
					{
						if(!matrix.containsKey(a.getQuestion().getId(),row.getId(),column.getId()))
						{
							matrix.put(a.getQuestion().getId(),row.getId(),column.getId(),efMatrix.build(a,row,column));
						}
					}
				}
			}
		}
	}
	
	public void save(CORRELATION correlation) throws JeeslConstraintViolationException, JeeslLockingException
	{
		save(correlation,null);
	}
	public void save(CORRELATION correlation, SECTION section) throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(activeSection!=null) {activeSection=fSurvey.find(cSection,activeSection);}
		if(SurveyHandler.debug){logger.warn("save");try {Thread.sleep(SurveyHandler.debugDelay);} catch (InterruptedException e) {e.printStackTrace();}}
		logger.info("Saving "+correlation.toString()+ " "+answers.size()+" answers  CORR.saved: "+EjbIdFactory.isSaved(correlation));
		
		surveyData.setCorrelation(correlation);
		surveyData = fSurvey.saveData(surveyData);
		
		List<ANSWER> answersToSave = new ArrayList<ANSWER>();
		for(ANSWER a : answers.values())
		{
			a.setData(surveyData);
			if(efAnswer.belongsToSection(a, section, true));
			{
				answersToSave.add(a);
				if(SurveyHandler.debug){logger.warn("\tQueing "+JeeslSurveyAnswer.class.getSimpleName()+" for Save: "+tfAnswer.build(a));}
			}
			
		}
		
		validation.evaluateList(answersToSave);
		if(validation.isHasErrors())
		{
			if(SurveyHandler.debug){logger.warn("Has Errors "+validation.getErrors().size());try {Thread.sleep(SurveyHandler.debugDelay);} catch (InterruptedException e) {e.printStackTrace();}}
			return ;
		}
		
		if(SurveyHandler.debug){logger.warn("Starting to save "+answersToSave.size()+" "+JeeslSurveyAnswer.class.getSimpleName());try {Thread.sleep(SurveyHandler.debugDelay);} catch (InterruptedException e) {e.printStackTrace();}}
		fSurvey.save(answersToSave);
		
		reloadAnswers(false);
		
		List<MATRIX> matrixToSave = new ArrayList<MATRIX>();
		for(ANSWER a : answers.values())
		{
			if(BooleanComparator.active(a.getQuestion().getShowMatrix()) && efAnswer.belongsToSection(a, section, true))
			{
				for(MATRIX m : matrix.values(a.getQuestion().getId()))
				{
					if(m.getOption()!=null){m.setOption(bSurvey.getMapOptionId().get(m.getOption().getId()));}
					m.setAnswer(a);
					matrixToSave.add(m);
				}
			}
		}
		if(SurveyHandler.debug){logger.warn("Save Matrix");try {Thread.sleep(SurveyHandler.debugDelay);} catch (InterruptedException e) {e.printStackTrace();}}
		fSurvey.save(matrixToSave);
		
		if(SurveyHandler.debug){logger.warn("Load Matrix");try {Thread.sleep(SurveyHandler.debugDelay);} catch (InterruptedException e) {e.printStackTrace();}}
		
		List<ANSWER> listAnswers = new ArrayList<ANSWER>(answers.values());
		logger.info("Loading ... for "+listAnswers.size());
		List<MATRIX> cellList = fSurvey.fCells(listAnswers);
		logger.info("Got: "+cellList.size());
		
		for(MATRIX m : cellList)
		{
			matrix.put(m.getAnswer().getQuestion().getId(),m.getRow().getId(),m.getColumn().getId(),m);
		}
		
		if(bMessage!=null){bMessage.growlSuccessSaved();}
	}
	
	public void updateAnswer(ANSWER answer)
	{
		if(debug) {logger.info("Updating ... "+answer);}
		
		if(answer.getOption()!=null)
		{
			if(debug) {logger.info("Settings OPTION with "+answer.getOption().getId());}
			OPTION o = bSurvey.getMapOptionId().get(answer.getOption().getId());
			answer.setOption(o);
			if(debug) {logger.info("Set to "+answer.getOption().toString()+" "+answer.getOption().getCode());}
		}
		condition.update(answer);
	}
	
	public void onSectionChange()
	{
		if(SurveyHandler.debug){logger.warn("onSectionChange "+(activeSection!=null));try {Thread.sleep(SurveyHandler.debugDelay);} catch (InterruptedException e) {e.printStackTrace();}}
		activeSection = fSurvey.find(cSection,activeSection);
		activeSections.clear();
		activeSections.add(activeSection);
		logger.info("Section "+activeSection.toString());
		reloadAnswers();
	}
	
	private boolean processSection(SECTION s)
	{
		if(activeSection==null) {return s.isVisible();}
		
		boolean processActiveSection = activeSection!=null && s.equals(activeSection);
		return s.isVisible() && processActiveSection;
	}
	
	public String multiKey(ANSWER a)
	{
		return "x";
	}
}