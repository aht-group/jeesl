package org.jeesl.web.mbean.prototype.module.survey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeesl.api.bean.JeeslSurveyBean;
import org.jeesl.api.facade.module.survey.JeeslSurveyCoreFacade;
import org.jeesl.controller.monitoring.counter.ProcessingTimeTracker;
import org.jeesl.factory.builder.module.survey.SurveyAnalysisFactoryBuilder;
import org.jeesl.factory.builder.module.survey.SurveyCoreFactoryBuilder;
import org.jeesl.factory.builder.module.survey.SurveyTemplateFactoryBuilder;
import org.jeesl.factory.ejb.module.survey.EjbSurveyOptionFactory;
import org.jeesl.factory.ejb.module.survey.EjbSurveySectionFactory;
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
import org.jeesl.interfaces.model.system.job.template.JeeslJobTemplate;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;

public abstract class AbstractAppSurveyBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslStatus<L,D,LOC>,
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
						QE extends JeeslSurveyQuestionElement<L,D,QE,?>,
						SCORE extends JeeslSurveyScore<L,D,SCHEME,QUESTION>,
						UNIT extends JeeslSurveyQuestionUnit<L,D,UNIT,?>,
						ANSWER extends JeeslSurveyAnswer<L,D,QUESTION,MATRIX,DATA,OPTION>,
						MATRIX extends JeeslSurveyMatrix<L,D,ANSWER,OPTION>,
						DATA extends JeeslSurveyData<L,D,SURVEY,ANSWER,CORRELATION>,
						OPTIONS extends JeeslSurveyOptionSet<L,D,TEMPLATE,OPTION>,
						OPTION extends JeeslSurveyOption<L,D>,
						CORRELATION extends JeeslSurveyCorrelation<DATA>,
						ATT extends JeeslStatus<L,D,ATT>,
						TOOLCACHETEMPLATE extends JeeslJobTemplate<L,D,?,?,?,?>>
					implements Serializable,
			JeeslSurveyBean<SURVEY,TEMPLATE,SECTION,QUESTION,CONDITION,VALIDATION,QE,OPTIONS,OPTION,ATT>
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractAppSurveyBean.class);

	private JeeslSurveyCoreFacade<L,D,LOC,SURVEY,SS,SCHEME,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,QE,SCORE,ANSWER,MATRIX,DATA,OPTIONS,OPTION,CORRELATION> fSurvey;
	
	private final SurveyTemplateFactoryBuilder<L,D,LOC,SCHEME,VALGORITHM,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,CONDITION,VALIDATION,QE,SCORE,UNIT,OPTIONS,OPTION> fbTemplate;
	private final SurveyCoreFactoryBuilder<L,D,LOC,SURVEY,SS,SCHEME,VALGORITHM,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,CONDITION,VALIDATION,QE,SCORE,UNIT,ANSWER,MATRIX,DATA,OPTIONS,OPTION,CORRELATION,ATT> ffSurvey;
	private final SurveyAnalysisFactoryBuilder<L,D,TEMPLATE,QUESTION,QE,SCORE,ANSWER,MATRIX,DATA,OPTION,CORRELATION,?,?,?,?,?,?,?,?,ATT,TOOLCACHETEMPLATE> ffAnalysis;
	
	protected final EjbSurveyOptionFactory<QUESTION,OPTION> efOption;
	protected final EjbSurveySectionFactory<L,D,TEMPLATE,SECTION> efSection;
	
	protected Map<TEMPLATE,List<SECTION>> mapSection; @Override public Map<TEMPLATE,List<SECTION>> getMapSection() {return mapSection;}
	protected Map<SECTION,List<QUESTION>> mapQuestion; @Override public Map<SECTION,List<QUESTION>> getMapQuestion() {return mapQuestion;}

	protected Map<Long,OPTION> mapOptionId; @Override public Map<Long,OPTION> getMapOptionId(){return mapOptionId;}
	protected Map<QUESTION,List<OPTION>> mapOption; @Override public Map<QUESTION,List<OPTION>> getMapOption() {return mapOption;}
	protected final Map<QUESTION,List<CONDITION>> mapCondition;
	protected final Map<QUESTION,List<VALIDATION>> mapValidation;
	protected Map<OPTIONS,List<OPTION>> mapOptionSet;

	protected Map<QUESTION,List<OPTION>> matrixRows; @Override public Map<QUESTION,List<OPTION>> getMatrixRows() {return matrixRows;}
	protected Map<QUESTION,List<OPTION>> matrixCols; @Override public Map<QUESTION,List<OPTION>> getMatrixCols() {return matrixCols;}
	protected Map<QUESTION,List<OPTION>> matrixCells; public Map<QUESTION,List<OPTION>> getMatrixCells() {return matrixCells;}

	public AbstractAppSurveyBean(SurveyCoreFactoryBuilder<L,D,LOC,SURVEY,SS,SCHEME,VALGORITHM,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,CONDITION,VALIDATION,QE,SCORE,UNIT,ANSWER,MATRIX,DATA,OPTIONS,OPTION,CORRELATION,ATT> ffSurvey,
									SurveyAnalysisFactoryBuilder<L,D,TEMPLATE,QUESTION,QE,SCORE,ANSWER,MATRIX,DATA,OPTION,CORRELATION,?,?,?,?,?,?,?,?,ATT,TOOLCACHETEMPLATE> ffAnalysis,
									SurveyTemplateFactoryBuilder<L,D,LOC,SCHEME,VALGORITHM,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,CONDITION,VALIDATION,QE,SCORE,UNIT,OPTIONS,OPTION> fbTemplate)
	{
		this.fbTemplate=fbTemplate;
		this.ffSurvey=ffSurvey;
		this.ffAnalysis=ffAnalysis;
		efOption = ffSurvey.option();
		efSection = fbTemplate.section();
		
		elements = new ArrayList<QE>();
		
		mapSection = new HashMap<TEMPLATE,List<SECTION>>();
		mapQuestion = new HashMap<SECTION,List<QUESTION>>();
		
		mapOptionId = new HashMap<Long,OPTION>();
		mapOption = new HashMap<QUESTION,List<OPTION>>();
		mapCondition = new HashMap<QUESTION,List<CONDITION>>();
		mapValidation = new HashMap<QUESTION,List<VALIDATION>>();
		mapOptionSet = new HashMap<OPTIONS,List<OPTION>>();
		matrixRows = new HashMap<QUESTION,List<OPTION>>();
		
		matrixCols = new HashMap<QUESTION,List<OPTION>>();
		matrixCells = new HashMap<QUESTION,List<OPTION>>();
	}
	
	public void initSuper(JeeslSurveyCoreFacade<L,D,LOC,SURVEY,SS,SCHEME,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,QE,SCORE,ANSWER,MATRIX,DATA,OPTIONS,OPTION,CORRELATION> fSurvey)
	{
		this.fSurvey=fSurvey;
		refreshUnits();
		this.reloadSurveyStatus();
		reloadToolTypes();
		reloadElements();
		
		//Cache
		mapSection.clear();mapSection.putAll(efSection.loadMap(fSurvey));
		reloadSets();
		reloadQuestions();
//		reloadOptions();
	}
	
	public void reloadQuestionElements() {}
	
	private List<ATT> toolTypes;
	public List<ATT> getToolTypes(){return toolTypes;}
	public void reloadToolTypes(){toolTypes=fSurvey.allOrderedPositionVisible(ffAnalysis.getAttClass());}
	
	private List<UNIT> units;
	public List<UNIT> getUnits(){return units;}
	public void refreshUnits(){units=fSurvey.allOrderedPositionVisible(fbTemplate.getClassUnit());}
	
	private List<SS> surveyStatus;
	public List<SS> getSurveyStatus() {return surveyStatus;}
	public void reloadSurveyStatus() {surveyStatus=fSurvey.allOrderedPositionVisible(ffSurvey.getClassSurveyStatus());}
	
	private final List<QE> elements;
	@Override public List<QE> getElements(){return elements;}
	public void reloadElements()
	{
		elements.clear();
		for(QE type : fSurvey.allOrderedPositionVisible(fbTemplate.getClassElement()))
		{
			boolean add=false;
			for(JeeslSurveyQuestion.Elements t : JeeslSurveyQuestion.Elements.values())
			{
				if(type.getCode().equals(t.toString())) {add=true;}
			}
			if(add) {elements.add(type);}
		}
	}
	
	@Override public void updateTemplate(TEMPLATE template)
	{
		if(!mapSection.containsKey(template)){mapSection.put(template,new ArrayList<SECTION>());}
		mapSection.get(template).clear();
		for(SECTION section : template.getSections())
		{
			if(section.isVisible())
			{
				mapSection.get(template).add(section);
			}
		}
	}
	
	private void reloadSets()
	{
		mapOptionSet.clear();
		for(OPTIONS set : fSurvey.allOrderedPosition(ffSurvey.getOptionSetClass()))
		{
			set = fSurvey.load(set);
			if(!mapOptionSet.containsKey(set)){mapOptionSet.put(set,new ArrayList<OPTION>());}
			for(OPTION o : set.getOptions()) {mapOptionId.put(o.getId(),o);}
			mapOptionSet.get(set).addAll(set.getOptions());
		}
	}
		
	private void reloadQuestions()
	{
		// A similar method is available in EjbQuestionFactory
		ProcessingTimeTracker ptt = ProcessingTimeTracker.instance().start();;
		mapQuestion.clear();
		mapOption.clear();
		for(QUESTION q : fSurvey.allOrderedPosition(ffSurvey.getClassQuestion()))
		{
			q = fSurvey.load(q);
			if(!mapQuestion.containsKey(q.getSection())){mapQuestion.put(q.getSection(),new ArrayList<QUESTION>());}
			mapQuestion.get(q.getSection()).add(q);
			updateQuestion(q);
		}
		logger.info(ffSurvey.getClassQuestion().getSimpleName()+" loaded in "+AbstractLogMessage.postConstruct(ptt));
	}
	
	@Override public void updateSection(SECTION section)
	{
		if(!mapQuestion.containsKey(section)){mapQuestion.put(section,new ArrayList<QUESTION>());}
		mapQuestion.get(section).clear();
		mapQuestion.get(section).addAll(section.getQuestions());
	}
	
	@Override public void updateQuestion(QUESTION question)
	{
		updateQuestionOptions(question);
		updateQuestionCondition(question);
		updateQuestionValidation(question);
	}
	
	private void updateQuestionOptions(QUESTION question)
	{
		if(!mapOption.containsKey(question)){mapOption.put(question,new ArrayList<OPTION>());}
		mapOption.get(question).clear();
		
		if(question.getOptionSet()==null)
		{
			for(OPTION o : question.getOptions()){mapOptionId.put(o.getId(),o);}
			mapOption.get(question).addAll(question.getOptions());
			matrixRows.put(question, efOption.toRows(question.getOptions()));
			matrixCols.put(question, efOption.toColumns(question.getOptions()));
			matrixCells.put(question, efOption.toCells(question.getOptions()));
		}
		else
		{
			mapOption.get(question).addAll(mapOptionSet.get(question.getOptionSet()));
			matrixRows.put(question, efOption.toRows(mapOptionSet.get(question.getOptionSet())));
			matrixCols.put(question, efOption.toColumns(mapOptionSet.get(question.getOptionSet())));
			matrixCells.put(question, efOption.toCells(mapOptionSet.get(question.getOptionSet())));
		}
	}
	
	private void updateQuestionCondition(QUESTION question)
	{
		if(!mapCondition.containsKey(question)){mapCondition.put(question,new ArrayList<CONDITION>());}
		mapCondition.get(question).clear();
		mapCondition.get(question).addAll(question.getConditions());
	}
	
	private void updateQuestionValidation(QUESTION question)
	{
		if(!mapValidation.containsKey(question)){mapValidation.put(question,new ArrayList<VALIDATION>());}
		mapValidation.get(question).clear();
		mapValidation.get(question).addAll(question.getValidations());
	}
	
	@Override public void updateOptions(OPTIONS set)
	{
		for(OPTION o : set.getOptions()){mapOptionId.put(o.getId(),o);}
		if(!mapOptionSet.containsKey(set)){mapOptionSet.put(set,new ArrayList<OPTION>());}
		mapOptionSet.get(set).clear();
		mapOptionSet.get(set).addAll(set.getOptions());
		for(QUESTION question : fSurvey.allForParent(ffSurvey.getClassQuestion(), JeeslSurveyQuestion.Attributes.optionSet, set))
		{
			question = fSurvey.load(question);
			updateQuestion(question);
		}
	}
	
	protected String statistics()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("mapSection: ").append(mapSection.size());
		sb.append(" mapOptionId: ").append(mapOptionId.size());
		return sb.toString();
	}
	
	@Override
	public List<SECTION> getSections(TEMPLATE template)
	{
		if(mapSection.containsKey(template)) {return mapSection.get(template);}
		return new ArrayList<SECTION>();
	}
	
	@Override
	public List<QUESTION> getQuestions(SECTION section)
	{
		if(mapQuestion.containsKey(section)) {return mapQuestion.get(section);}
		return new ArrayList<QUESTION>();
	}
	
	@Override public List<CONDITION> getConditions(QUESTION question)
	{
		if(mapCondition.containsKey(question)) {return mapCondition.get(question);}
		return new ArrayList<CONDITION>();
	}
	
	@Override public List<VALIDATION> getValidations(QUESTION question)
	{
		if(mapValidation.containsKey(question)) {return mapValidation.get(question);}
		return new ArrayList<VALIDATION>();
	}
}