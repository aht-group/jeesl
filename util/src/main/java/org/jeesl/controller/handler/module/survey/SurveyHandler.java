package org.jeesl.controller.handler.module.survey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.jeesl.api.bean.JeeslSurveyBean;
import org.jeesl.api.bean.module.survey.JeeslSurveyCache;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.module.survey.JeeslSurveyCoreFacade;
import org.jeesl.controller.util.comparator.primitive.BooleanComparator;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.module.survey.SurveyCoreFactoryBuilder;
import org.jeesl.factory.builder.module.survey.SurveyTemplateFactoryBuilder;
import org.jeesl.factory.ejb.module.survey.EjbSurveyAnswerFactory;
import org.jeesl.factory.ejb.module.survey.EjbSurveyDataFactory;
import org.jeesl.factory.ejb.module.survey.EjbSurveyMatrixFactory;
import org.jeesl.factory.ejb.util.EjbIdFactory;
import org.jeesl.factory.txt.module.survey.TxtSurveyAnswerFactory;
import org.jeesl.factory.txt.module.survey.TxtSurveySectionFactory;
import org.jeesl.interfaces.controller.handler.module.survey.JeeslSurveyHandler;
import org.jeesl.interfaces.controller.handler.module.survey.JeeslSurveyHandlerCallback;
import org.jeesl.interfaces.controller.handler.system.io.JeeslLogger;
import org.jeesl.interfaces.controller.processor.module.survey.JeeslScoreProcessor;
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
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SurveyHandler<D extends JeeslDescription,
							SURVEY extends JeeslSurvey<?,D,?,TEMPLATE,DATA>,
							TEMPLATE extends JeeslSurveyTemplate<?,D,?,TEMPLATE,?,?,TC,SECTION,?,?>,
							TC extends JeeslSurveyTemplateCategory<?,D,TC,?>,
							SECTION extends JeeslSurveySection<?,D,TEMPLATE,SECTION,QUESTION>,
							QUESTION extends JeeslSurveyQuestion<?,D,SECTION,CONDITION,VALIDATION,?,?,?,?,OPTION,?>,
							CONDITION extends JeeslSurveyCondition<QUESTION,?,OPTION>,
							VALIDATION extends JeeslSurveyValidation<?,D,QUESTION,?>,
							ANSWER extends JeeslSurveyAnswer<?,D,QUESTION,MATRIX,DATA,OPTION>,
							MATRIX extends JeeslSurveyMatrix<?,D,ANSWER,OPTION>,
							DATA extends JeeslSurveyData<?,D,SURVEY,ANSWER,CORRELATION>,
							OPTION extends JeeslSurveyOption<?,D>,
							CORRELATION extends JeeslSurveyCorrelation<DATA>>
	implements JeeslSurveyHandler<SECTION>
{
	final static Logger logger = LoggerFactory.getLogger(SurveyHandler.class);
	private static final long serialVersionUID = 1L;
	
	private JeeslLogger jogger; public void setJogger(JeeslLogger jogger) {this.jogger = jogger;}
	
	private final JeeslSurveyCoreFacade<?,?,SURVEY,?,?,?,TC,SECTION,QUESTION,ANSWER,MATRIX,DATA,CORRELATION> fSurvey;
	
	private final JeeslFacesMessageBean bMessage;
	private final JeeslSurveyBean<SURVEY,TEMPLATE,SECTION,QUESTION,CONDITION,VALIDATION,?,?,OPTION,?> bSurvey;
	private final JeeslSurveyCache<TEMPLATE,SECTION,QUESTION,CONDITION,VALIDATION> cache;
	
	private final JeeslSurveyHandlerCallback<SECTION> callback;
	private JeeslScoreProcessor<ANSWER> scoreProcessor; public void setScoreProcessor(JeeslScoreProcessor<ANSWER> scoreProcessor) {this.scoreProcessor = scoreProcessor;}

	private final SurveyConditionalHandler<TEMPLATE,SECTION,QUESTION,CONDITION,ANSWER,OPTION> condition; public SurveyConditionalHandler<TEMPLATE, SECTION, QUESTION, CONDITION, ANSWER, OPTION> getCondition() {return condition;}
	private final SurveyValidationHandler<D,TEMPLATE,SECTION,QUESTION,VALIDATION,ANSWER> validation; public SurveyValidationHandler<D,TEMPLATE,SECTION,QUESTION,VALIDATION,ANSWER> getValidation() {return validation;}

	private final Class<SECTION> cSection;
	
	private final EjbSurveyAnswerFactory<SECTION,QUESTION,ANSWER,MATRIX,DATA,OPTION> efAnswer;
	private final EjbSurveyMatrixFactory<ANSWER,MATRIX,OPTION> efMatrix;
	private final EjbSurveyDataFactory<SURVEY,DATA,CORRELATION> efData;
	private final TxtSurveySectionFactory<?,?,SECTION> tfSection;
	private final TxtSurveyAnswerFactory<?,?,QUESTION,ANSWER,MATRIX,OPTION> tfAnswer;
	
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
	private boolean debugOnInfo;
	
	public SurveyHandler(JeeslSurveyHandlerCallback<SECTION> callback,
//				final SurveyTemplateFactoryBuilder<L,D,?,?,?,?,?,?,?,SECTION,?,?,?,?,?,?,?,?> fbTemplate,
				final SurveyCoreFactoryBuilder<?,D,?,SURVEY,?,?,TEMPLATE,?,?,TC,SECTION,QUESTION,CONDITION,?,?,?,ANSWER,MATRIX,DATA,?,OPTION,CORRELATION> fBSurvey,
				JeeslFacesMessageBean bMessage,
				final JeeslSurveyCoreFacade<?,?,SURVEY,?,?,?,TC,SECTION,QUESTION,ANSWER,MATRIX,DATA,CORRELATION> fSurvey,
				JeeslSurveyBean<SURVEY,TEMPLATE,SECTION,QUESTION,CONDITION,VALIDATION,?,?,OPTION,?> bSurvey,
				JeeslSurveyCache<TEMPLATE,SECTION,QUESTION,CONDITION,VALIDATION> cache)
	{
		this.callback=callback;
		this.bMessage=bMessage;
		this.fSurvey=fSurvey;
		
		this.bSurvey=bSurvey;
		this.cache=cache;
		
		showDataSave = false;
		showDataFields = false;
		
		showAssessment = false;
		allowAssessment = true;

		condition = new SurveyConditionalHandler<>(fBSurvey,bSurvey);
		validation = new SurveyValidationHandler<>(bSurvey);
		
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
		
		debugOnInfo = true;
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
		surveyData = null;
		
		showAssessment = true;
		if(debugOnInfo){logger.info("prepare fData()");}
		try
		{
			if(Objects.nonNull(correlation))
			{
				surveyData = fSurvey.fData(correlation);
				if(jogger!=null) {jogger.milestone(SurveyHandler.class.getSimpleName(),"fSurvey.fData(..)");}
			}
		}
		catch (JeeslNotFoundException e) {}
		
		if(Objects.isNull(surveyData))
		{
			surveyData = efData.build(survey,correlation);
			if(jogger!=null) {jogger.milestone(SurveyHandler.class.getSimpleName(),"efData.build(..)");}
		}
		
		template = survey.getTemplate();
		condition.init(template);
		if(jogger!=null) {jogger.milestone(SurveyHandler.class.getSimpleName(),"condition.init(..)");}
		
		validation.init(template);
		if(jogger!=null) {jogger.milestone(SurveyHandler.class.getSimpleName(),"validation.init(..)");}
		
		if(bSurvey.getMapSection().containsKey(template)){activeSections.addAll(bSurvey.getMapSection().get(template));}
		if(debugOnInfo){logger.warn("Preparing Survey for correlation:"+correlation.toString()+" and data:"+surveyData.toString());}
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
		
		if(debugOnInfo){logger.warn("Reloading Answers (dbLookup:"+dbLookup+")");}
		if(dbLookup)
		{
			List<SECTION> filterSection = null;
			if(activeSections!=null && !activeSections.isEmpty()){filterSection = new ArrayList<SECTION>(activeSections);}
			if(debugOnInfo){logger.warn("fAnswers for "+filterSection.size()+" "+JeeslSurveySection.class.getSimpleName()+": "+tfSection.codes(filterSection));}
			answers = efAnswer.toMapQuestion(fSurvey.fAnswers(surveyData, true, filterSection));
		}

		List<ANSWER> loadMatrix = new ArrayList<ANSWER>();
		if(bSurvey.getMapSection().get(template)!=null)
		{
			for(SECTION s : bSurvey.getMapSection().get(template))
			{
				boolean isProcessSection = processSection(s);
				boolean hasQuestions = (bSurvey.getMapQuestion()!=null) && (bSurvey.getMapQuestion().containsKey(s));
				if(debugOnInfo){logger.warn("Processing Section "+s.toString()+" process:"+isProcessSection+" hasQuestions:"+hasQuestions);}
				
				if(isProcessSection && hasQuestions)
				{		
					for(QUESTION q : bSurvey.getMapQuestion().get(s))
					{	
						if(q.isVisible())
						{
							ANSWER a;
							if(!answers.containsKey(q))
							{
								if(debugOnInfo){logger.warn("Building new Answer for Question"+q.toString());}
								a = efAnswer.build(q, surveyData);
								if(BooleanComparator.active(q.getShowSelectOne()) && BooleanComparator.inactive(q.getShowEmptyOption()) && bSurvey.getMapOption().containsKey(q) && !bSurvey.getMapOption().get(q).isEmpty())
								{
									a.setOption(bSurvey.getMapOption().get(q).get(0));
								}
							}
							else
							{
								a = answers.get(q);
								if(debugOnInfo){logger.warn("Using Answer "+a.getId()+" for Question "+q.toString()+" "+tfAnswer.build(a));}
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
		
		StringBuilder sb = new StringBuilder();
		sb.append("Saving ");
		sb.append(" Correlation:").append(correlation.getId()).append(" (saved?").append(EjbIdFactory.isSaved(correlation)).append(")");
		sb.append(" with "); if(Objects.nonNull(answers)) {sb.append(answers.size());} sb.append(" answers");
		sb.append(" data:"); if(Objects.nonNull(surveyData)) {sb.append(surveyData.getId());} else {sb.append("NULL");}
		sb.append(" section:"); if(Objects.nonNull(section)) {sb.append(section.getId());} else {sb.append("NULL");}
		sb.append(" ").append(this.toString());
		logger.info(sb.toString());
		
		surveyData.setCorrelation(correlation);
		surveyData = fSurvey.saveData(surveyData);
		
		List<ANSWER> answersToSave = new ArrayList<ANSWER>();
		for(ANSWER a : answers.values())
		{
			a.setData(surveyData);
			if(efAnswer.belongsToSection(a,section, true))
			{
				if(Objects.nonNull(a.getOption())) {a.setOption(bSurvey.getMapOptionId().get(a.getOption().getId()));}
				if(Objects.nonNull(scoreProcessor)) {scoreProcessor.calculateScore(a);}
				answersToSave.add(a);
//				if(debugOnInfo)
				{
					logger.warn("\tQueing "+JeeslSurveyAnswer.class.getSimpleName()+" for Save: "+tfAnswer.build(a));
				}
			}
		}
		
		validation.evaluateList(answersToSave);
		if(validation.isHasErrors())
		{
			if(debugOnInfo) {logger.warn("Has Errors "+validation.getErrors().size()); }
			return ;
		}
		
		if(debugOnInfo){logger.warn("Starting to save "+answersToSave.size()+" "+JeeslSurveyAnswer.class.getSimpleName());}
		fSurvey.save(answersToSave);
		
//		this.reloadAnswers(false);
		// Deactivated the reload in favor of just putting the answers to the map
		for(ANSWER a : answersToSave) {answers.put(a.getQuestion(), a);}

		List<MATRIX> matrixToSave = new ArrayList<>();
		for(ANSWER a : answers.values())
		{
			if(BooleanComparator.active(a.getQuestion().getShowMatrix()) && efAnswer.belongsToSection(a, section, true))
			{
				for(MATRIX m : matrix.values(a.getQuestion().getId()))
				{
					if(Objects.nonNull(m.getOption())) {m.setOption(bSurvey.getMapOptionId().get(m.getOption().getId()));}
					m.setAnswer(a);
					matrixToSave.add(m);
				}
			}
		}
		if(!matrixToSave.isEmpty())
		{
			if(debugOnInfo){logger.warn("Save Matrix");}
			fSurvey.save(matrixToSave);
			if(debugOnInfo){logger.warn("Load Matrix");}
		}
		
		List<ANSWER> listAnswers = new ArrayList<>(answers.values());
		logger.info("Loading ... for "+listAnswers.size());
		List<MATRIX> cellList = fSurvey.fCells(EjbIdFactory.toSaved(listAnswers));
		logger.info("Got: "+cellList.size());
		
		for(MATRIX m : cellList)
		{
			matrix.put(m.getAnswer().getQuestion().getId(),m.getRow().getId(),m.getColumn().getId(),m);
		}
		
		if(Objects.nonNull(bMessage)) {bMessage.growlSaved(section);}
	}
	
	public void updateAnswer(ANSWER answer)
	{
		if(debugOnInfo) {logger.info("Updating ... "+answer);}
		
		if(answer.getOption()!=null)
		{
			if(debugOnInfo) {logger.info("Settings OPTION with "+answer.getOption().getId());}
			OPTION o = bSurvey.getMapOptionId().get(answer.getOption().getId());
			answer.setOption(o);
			if(debugOnInfo) {logger.info("Set to "+answer.getOption().toString()+" "+answer.getOption().getCode());}
		}
		condition.update(answer);
	}
	
	public void onSectionChange()
	{
		if(debugOnInfo){logger.warn("onSectionChange "+(activeSection!=null));}
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
	
	public void saveSection(SECTION section) throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(Objects.isNull(callback)) {logger.error("Callback is NULL");}
		else {callback.saveSection(this,section);}
	}
	
	@Override public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("Handler: ");
		if(Objects.nonNull(template)) {sb.append("Template: ").append(template.getId()).append("-").append(template.getVersion().getId());}
		return sb.toString();
	}
}