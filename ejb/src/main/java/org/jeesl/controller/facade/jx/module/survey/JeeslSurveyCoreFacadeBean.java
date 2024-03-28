package org.jeesl.controller.facade.jx.module.survey;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.jeesl.api.facade.module.survey.JeeslSurveyCoreFacade;
import org.jeesl.controller.facade.jx.JeeslFacadeBean;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.module.survey.SurveyCoreFactoryBuilder;
import org.jeesl.factory.builder.module.survey.SurveyTemplateFactoryBuilder;
import org.jeesl.factory.ejb.module.survey.EjbSurveyAnswerFactory;
import org.jeesl.factory.ejb.util.EjbIdFactory;
import org.jeesl.factory.txt.system.status.TxtStatusFactory;
import org.jeesl.interfaces.model.module.survey.JeeslWithSurvey;
import org.jeesl.interfaces.model.module.survey.JeeslWithSurveyType;
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
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.system.status.JeeslWithType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslSurveyCoreFacadeBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslStatus<L,D,LOC>,
									SURVEY extends JeeslSurvey<L,D,SS,TEMPLATE,DATA>,
									SS extends JeeslSurveyStatus<L,D,SS,?>,
									SCHEME extends JeeslSurveyScheme<L,D,TEMPLATE,SCORE>,
									VALGORITHM extends JeeslSurveyValidationAlgorithm<L,D>,
									TEMPLATE extends JeeslSurveyTemplate<L,D,SCHEME,TEMPLATE,VERSION,TS,TC,SECTION,OPTIONS,?>,
									VERSION extends JeeslSurveyTemplateVersion<L,D,TEMPLATE>,
									TS extends JeeslSurveyTemplateStatus<L,D,TS,?>,
									TC extends JeeslSurveyTemplateCategory<L,D,TC,?>,
									SECTION extends JeeslSurveySection<L,D,TEMPLATE,SECTION,QUESTION>,
									QUESTION extends JeeslSurveyQuestion<L,D,SECTION,CONDITION,VALIDATION,?,SCORE,?,OPTIONS,OPTION,?>,
									CONDITION extends JeeslSurveyCondition<QUESTION,?,OPTION>,
									VALIDATION extends JeeslSurveyValidation<L,D,QUESTION,VALGORITHM>,
									
									SCORE extends JeeslSurveyScore<L,D,SCHEME,QUESTION>,
									
									ANSWER extends JeeslSurveyAnswer<L,D,QUESTION,MATRIX,DATA,OPTION>,
									MATRIX extends JeeslSurveyMatrix<L,D,ANSWER,OPTION>,
									DATA extends JeeslSurveyData<L,D,SURVEY,ANSWER,CORRELATION>,
									OPTIONS extends JeeslSurveyOptionSet<L,D,TEMPLATE,OPTION>,
									OPTION extends JeeslSurveyOption<L,D>,
									CORRELATION extends JeeslSurveyCorrelation<DATA>>
	extends JeeslFacadeBean implements JeeslSurveyCoreFacade<L,D,SURVEY,SS,SCHEME,VERSION,TC,SECTION,QUESTION,ANSWER,MATRIX,DATA,CORRELATION>
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslSurveyCoreFacadeBean.class);
	
	private final SurveyTemplateFactoryBuilder<L,D,LOC,SCHEME,VALGORITHM,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,CONDITION,VALIDATION,?,SCORE,?,OPTIONS,OPTION> fbTemplate;
	private final SurveyCoreFactoryBuilder<L,D,LOC,SURVEY,SS,SCHEME,VALGORITHM,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,CONDITION,VALIDATION,?,SCORE,?,ANSWER,MATRIX,DATA,OPTIONS,OPTION,CORRELATION> fbCore;
	
	private EjbSurveyAnswerFactory<SECTION,QUESTION,ANSWER,MATRIX,DATA,OPTION> efAnswer;

	
	public JeeslSurveyCoreFacadeBean(EntityManager em,
			SurveyTemplateFactoryBuilder<L,D,LOC,SCHEME,VALGORITHM,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,CONDITION,VALIDATION,?,SCORE,?,OPTIONS,OPTION> fbTemplate,
			SurveyCoreFactoryBuilder<L,D,LOC,SURVEY,SS,SCHEME,VALGORITHM,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,CONDITION,VALIDATION,?,SCORE,?,ANSWER,MATRIX,DATA,OPTIONS,OPTION,CORRELATION> fbCore)
	{
		super(em);
		this.fbTemplate=fbTemplate;
		this.fbCore=fbCore;

		efAnswer = fbCore.answer();
	}
	
	@Override public SECTION load(SECTION section)
	{
		section = em.find(fbTemplate.getClassSection(),section.getId());
		for(SECTION sub : section.getSections())
		{
			sub.getQuestions().size();
		}
		section.getQuestions().size();
		return section;
	}
	
	@Override public SURVEY load(SURVEY survey)
	{
		survey = em.find(fbCore.getClassSurvey(),survey.getId());
		survey.getSurveyData().size();
		return survey;
	}
	
	@Override public QUESTION load(QUESTION question)
	{
		question = em.find(fbTemplate.getClassQuestion(),question.getId());
		question.getScores().size();
		question.getOptions().size();
		question.getConditions().size();
		question.getValidations().size();
		return question;
	}
	
	@Override public ANSWER load(ANSWER answer)
	{
		answer = em.find(fbCore.getClassAnswer(),answer.getId());
		answer.getMatrix().size();
		answer.getOptions().size();
		return answer;
	}

	@Override public DATA load(DATA data)
	{
		data = em.find(fbCore.getClassData(),data.getId());
		data.getAnswers().size();
		return data;
	}
	
//	@Override public OPTIONS load(OPTIONS optionSet)
//	{
//		optionSet = em.find(fbCore.getOptionSetClass(),optionSet.getId());
//		optionSet.getOptions().size();
//		return optionSet;
//	}
	
	@Override public SURVEY fSurvey(CORRELATION correlation) throws JeeslNotFoundException
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<SURVEY> cQ = cB.createQuery(fbCore.getClassSurvey());
		Root<DATA> data = cQ.from(fbCore.getClassData());
		
		Path<SURVEY> pathSurvey = data.get(JeeslSurveyData.Attributes.survey.toString());
		Join<DATA,CORRELATION> jCorrelation = data.join(JeeslSurveyData.Attributes.correlation.toString());
		
		cQ.where(cB.equal(jCorrelation,correlation));
		cQ.select(pathSurvey);
		
		try	{return em.createQuery(cQ).getSingleResult();}
		catch (NoResultException e) {throw new JeeslNotFoundException("No survey found for this correlation");}
		catch (NonUniqueResultException e) {throw new JeeslNotFoundException("Multiple surveys found for this correlation");}
	}
	
	@Override public void deleteSurvey(SURVEY survey) throws JeeslConstraintViolationException, JeeslLockingException
	{
//		survey = em.find(cSurvey, survey.getId());
		this.rmProtected(survey);
	}
	
	@Override public List<SURVEY> fSurveysForCategories(List<TC> categories)
	{
		if(categories==null || categories.isEmpty()){return new ArrayList<SURVEY>();}
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<SURVEY> cQ = cB.createQuery(fbCore.getClassSurvey());
		Root<SURVEY> survey = cQ.from(fbCore.getClassSurvey());
		
		Join<SURVEY,TEMPLATE> jTemplate = survey.join(JeeslSurvey.Attributes.template.toString());
		Path<TC> pCategory = jTemplate.get(JeeslSurveyTemplate.Attributes.category.toString());
		
		predicates.add(pCategory.in(categories));
		
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.select(survey);

		return em.createQuery(cQ).getResultList();
	}
	@Override public List<SURVEY> fSurveys(TC category, SS status, Date date)
	{
		List<TC> categories = new ArrayList<TC>();categories.add(category);
		return this.fSurveys(categories, status, date);
	}
	@Override public List<SURVEY> fSurveys(List<TC> categories, SS status, Date date)
	{
		if(categories==null || categories.isEmpty()) {return new ArrayList<SURVEY>();}
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<SURVEY> cQ = cB.createQuery(fbCore.getClassSurvey());
		Root<SURVEY> survey = cQ.from(fbCore.getClassSurvey());
		
		Join<SURVEY,TEMPLATE> jTemplate = survey.join(JeeslSurvey.Attributes.template.toString());
		Path<TC> pCategory = jTemplate.get(JeeslSurveyTemplate.Attributes.category.toString());
		Path<SS> pStatus = survey.get(JeeslSurvey.Attributes.status.toString());
		
		predicates.add(pCategory.in(categories));
		predicates.add(cB.equal(pStatus, status));
		
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.select(survey);

		return em.createQuery(cQ).getResultList();
	}
	
	@Override public void rmVersion(VERSION version) throws JeeslConstraintViolationException
	{
		version = em.find(fbTemplate.getClassVersion(), version.getId());
		this.rmProtected(version);
	}
	
//	@Override public OPTION saveOption(QUESTION question, OPTION option) throws JeeslConstraintViolationException, JeeslLockingException
//	{
//		question = em.find(fbTemplate.getClassQuestion(), question.getId());
//		option = this.saveProtected(option);
//		if(!question.getOptions().contains(option))
//		{
//			question.getOptions().add(option);
//			this.save(question);
//		}
//		return option;
//	}
//	@Override public OPTION saveOption(OPTIONS set, OPTION option) throws JeeslConstraintViolationException, JeeslLockingException
//	{
//		set = em.find(fbCore.getOptionSetClass(), set.getId());
//		option = this.saveProtected(option);
//		if(!set.getOptions().contains(option))
//		{
//			set.getOptions().add(option);
//			this.save(set);
//		}
//		return option;
//	}
	
//	@Override public void rmOption(QUESTION question, OPTION option) throws JeeslConstraintViolationException, JeeslLockingException
//	{
//		question = em.find(fbTemplate.getClassQuestion(), question.getId());
//		option = em.find(fbTemplate.getClassOption(), option.getId());
//		if(question.getOptions().contains(option))
//		{
//			question.getOptions().remove(option);
//			this.save(question);
//		}
//		this.rmProtected(option);
//	}
//	@Override public void rmOption(OPTIONS set, OPTION option) throws JeeslConstraintViolationException, JeeslLockingException
//	{
//		set = em.find(fbCore.getOptionSetClass(), set.getId());
//		option = em.find(fbTemplate.getClassOption(), option.getId());
//		if(set.getOptions().contains(option))
//		{
//			set.getOptions().remove(option);
//			this.save(set);
//		}
//		this.rmProtected(option);
//	}
	
	@Override public <TYPE extends JeeslStatus<L,D,TYPE>, WT extends JeeslWithType<TYPE>, W extends JeeslWithSurveyType<SURVEY,WT,TYPE>>
		List<W> fWithSurveys(Class<W> c, List<SS> status, TYPE type, Date date)
	{
		logger.info("Looking für wSurvey for type="+type.getCode()+" and status="+TxtStatusFactory.toCodes(status)+" and c="+c.getSimpleName());
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<W> cQ = cB.createQuery(c);
		Root<W> with = cQ.from(c);
		Join<W,SURVEY> jSurvey = with.join(JeeslWithSurvey.Attributes.survey.toString());
		
		Join<SURVEY,SS> jStatus = jSurvey.join(JeeslSurvey.Attributes.status.toString());
		predicates.add(jStatus.in(status));
		
		Path<TYPE> pType = with.get(JeeslWithType.attributeType);
		predicates.add(cB.equal(pType,type));
		
		Expression<Date> dStart = jSurvey.get(JeeslSurvey.Attributes.startDate.toString());
		Expression<Date> dEnd   = jSurvey.get(JeeslSurvey.Attributes.endDate.toString());
		predicates.add(cB.lessThanOrEqualTo(dStart,date));
		predicates.add(cB.greaterThan(dEnd,date));
		
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.select(with);
		return em.createQuery(cQ).getResultList();
	}
	
	@Override public <W extends JeeslWithSurvey<SURVEY>> List<W> fSurveys(Class<W> c, List<SS> status, Date date)
	{
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<W> cQ = cB.createQuery(c);
		Root<W> root = cQ.from(c);
		Join<W,SURVEY> jSurvey = root.join(JeeslWithSurvey.Attributes.survey.toString());
		
		Path<SS> pStatus = jSurvey.get(JeeslSurvey.Attributes.status.toString());
		predicates.add(pStatus.in(status));
		
		Expression<Date> dStart = jSurvey.get(JeeslSurvey.Attributes.startDate.toString());
		Expression<Date> dEnd   = jSurvey.get(JeeslSurvey.Attributes.endDate.toString());
		predicates.add(cB.lessThanOrEqualTo(dStart,date));
		predicates.add(cB.greaterThan(dEnd,date));
		
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.select(root);

		return em.createQuery(cQ).getResultList();
	}
	
	@Override public <W extends JeeslWithSurvey<SURVEY>> W fWithSurvey(Class<W> c, long surveyId) throws JeeslNotFoundException
	{
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<W> cQ = cB.createQuery(c);
		Root<W> root = cQ.from(c);
		
		Join<W,SURVEY> jSurvey = root.join(JeeslWithSurvey.Attributes.survey.toString());
		Expression<Long> eId = jSurvey.get(JeeslSurvey.Attributes.id.toString());
	
		predicates.add(cB.equal(eId,surveyId));
		
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.select(root);
	
		TypedQuery<W> tQ = em.createQuery(cQ);
		try	{return tQ.getSingleResult();}
		catch (NoResultException ex){throw new JeeslNotFoundException(c.getSimpleName()+" not found for "+JeeslSurvey.class.getSimpleName()+"."+JeeslSurvey.Attributes.id+"="+surveyId);}
	}
	
	@Override public List<VERSION> fVersions(TC category, Long refId)
	{
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<VERSION> cQ = cB.createQuery(fbTemplate.getClassVersion());
		Root<VERSION> root = cQ.from(fbTemplate.getClassVersion());
		
		Join<VERSION,TEMPLATE> jTemplate = root.join(JeeslSurveyTemplateVersion.Attributes.template.toString());
		Path<TC> pCategory = jTemplate.get(JeeslSurveyTemplate.Attributes.category.toString());
		Path<Date> pRecord = root.get(JeeslSurveyTemplateVersion.Attributes.record.toString());
		predicates.add(cB.equal(pCategory,category));
		
		if(refId!=null && refId>0)
		{
			Expression<Long> eRefId = root.get(JeeslSurveyTemplateVersion.Attributes.refId.toString());
			predicates.add(cB.equal(eRefId,refId));
		}	
		
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.orderBy(cB.desc(pRecord));
		cQ.select(root);

		return em.createQuery(cQ).getResultList();
	}
	
	@Override public List<ANSWER> fAnswers(DATA data, Boolean visible, List<SECTION> sections)
	{
		if(sections!=null && sections.isEmpty()){return new ArrayList<ANSWER>();}
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<ANSWER> cQ = cB.createQuery(fbCore.getClassAnswer());
		Root<ANSWER> answer = cQ.from(fbCore.getClassAnswer());
		Expression<Long> eAnswerId = answer.get(EjbWithId.attribute);
		
		Join<ANSWER,DATA> jData = answer.join(JeeslSurveyAnswer.Attributes.data.toString());
		predicates.add(cB.equal(jData,data));
		
		Join<ANSWER,QUESTION> jQuestion = answer.join(JeeslSurveyAnswer.Attributes.question.toString());
		if(visible!=null){predicates.add(cB.equal(jQuestion.<Boolean>get(JeeslSurveyQuestion.Attributes.visible.toString()),visible));}
		Expression<Integer> eQuestionPosition = jQuestion.get(JeeslSurveyQuestion.Attributes.position.toString());
		
		Join<QUESTION,SECTION> jSection = jQuestion.join(JeeslSurveyQuestion.Attributes.section.toString());
		if(visible!=null){predicates.add(cB.equal(jSection.<Boolean>get(JeeslSurveySection.Attributes.visible.toString()),visible));}
		Expression<Integer> eSectionPosition = jSection.get(JeeslSurveySection.Attributes.position.toString());
		if(sections!=null){predicates.add(jSection.in(sections));}
		
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.select(answer);
		cQ.orderBy(cB.asc(eSectionPosition),cB.asc(eQuestionPosition),cB.asc(eAnswerId));
		
		TypedQuery<ANSWER> tQ = em.createQuery(cQ);
		return tQ.getResultList();
	}
	
	@Override public List<ANSWER> fAnswers(List<DATA> datas)
	{
		if(datas==null || datas.isEmpty()) {return new ArrayList<ANSWER>();}
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<ANSWER> cQ = cB.createQuery(fbCore.getClassAnswer());
		Root<ANSWER> answer = cQ.from(fbCore.getClassAnswer());
		
		Join<ANSWER,DATA> jData = answer.join(JeeslSurveyAnswer.Attributes.data.toString());
		predicates.add(jData.in(datas));
		
		cQ.select(answer);
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
//		cQ.orderBy(cB.asc(eSectionPosition),cB.asc(eQuestionPosition),cB.asc(eAnswerId));
		
		TypedQuery<ANSWER> tQ = em.createQuery(cQ);
		return tQ.getResultList();
	}
	
	@Override public List<ANSWER> fAnswers(SURVEY survey, QUESTION question)
	{
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<ANSWER> cQ = cB.createQuery(fbCore.getClassAnswer());
		Root<ANSWER> answer = cQ.from(fbCore.getClassAnswer());
		
		Join<ANSWER,DATA> jData = answer.join(JeeslSurveyAnswer.Attributes.data.toString());
		Join<DATA,SURVEY> jSurvey = jData.join(JeeslSurveyData.Attributes.survey.toString());
		predicates.add(cB.equal(jSurvey,survey));
		
		Join<ANSWER,QUESTION> jQuestion = answer.join(JeeslSurveyAnswer.Attributes.question.toString());
		predicates.add(cB.equal(jQuestion,question));
	
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.select(answer);
		
		TypedQuery<ANSWER> tQ = em.createQuery(cQ);
		return tQ.getResultList();
	}
	
	@Override public List<MATRIX> fCells(List<ANSWER> answers) 
	{
		if(answers!=null && answers.isEmpty()){return new ArrayList<MATRIX>();}
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<MATRIX> cQ = cB.createQuery(fbCore.getClassMatrix());
		Root<MATRIX> matrix = cQ.from(fbCore.getClassMatrix());
		
		Join<MATRIX,ANSWER> jAnswer = matrix.join(JeeslSurveyMatrix.Attributes.answer.toString());
		predicates.add(jAnswer.in(answers));

		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.select(matrix);
		
		TypedQuery<MATRIX> tQ = em.createQuery(cQ);
		return tQ.getResultList();
	}
	
	@Override public List<ANSWER> fcAnswers(DATA data)
	{
		data = em.find(fbCore.getClassData(),data.getId());
		TEMPLATE template = em.find(fbTemplate.getClassTemplate(),data.getSurvey().getTemplate().getId());
		List<ANSWER> result = new ArrayList<ANSWER>();

		Set<Long> existing = new HashSet<Long>();
		for(ANSWER a : data.getAnswers()){existing.add(a.getQuestion().getId());result.add(a);}
		createAnswers(existing,template.getSections(),data,result);
		
		return result;
	}
	
	private void createAnswers(Set<Long> existing, List<SECTION> sections, DATA data, List<ANSWER> result)
	{
		for(SECTION s : sections)
		{
			for(QUESTION q : s.getQuestions())
			{
				if(!existing.contains(q.getId()))
				{
					try
					{
						ANSWER answer = this.persist(efAnswer.build(q,data));
						result.add(answer);
					}
					catch (JeeslConstraintViolationException e) {e.printStackTrace();}
				}
			}
			createAnswers(existing,s.getSections(),data,result);
		}
	}

	@Override public DATA saveData(DATA data) throws JeeslConstraintViolationException, JeeslLockingException
	{
		logger.trace("Testing Correlation: not null? "+(data.getCorrelation()!=null));
		logger.trace("Testing Correlation: Saved? "+EjbIdFactory.isSaved(data.getCorrelation()));
		
		if(EjbIdFactory.isSaved(data.getCorrelation()))
		{
			data.setCorrelation(em.find(fbCore.getClassCorrelation(),data.getCorrelation().getId()));
		}
		logger.trace("Now Saving ...");
		data = this.saveProtected(data);
		logger.trace("Saved "+data.toString());
		return data;
	}

	@Override public List<ANSWER> fAnswers(SURVEY survey)
	{
		return this.allForGrandParent(fbCore.getClassAnswer(), fbCore.getClassData(), "data", survey, "survey");
	}

	@Override public DATA fData(CORRELATION correlation) throws JeeslNotFoundException
	{
		return this.oneForParent(fbCore.getClassData(),JeeslSurveyData.Attributes.correlation,correlation);
	}
	
	@Override public List<DATA> fDatas(List<CORRELATION> correlations)
	{
		if(correlations!=null && correlations.isEmpty()){return new ArrayList<DATA>();}
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<DATA> cQ = cB.createQuery(fbCore.getClassData());
		Root<DATA> data = cQ.from(fbCore.getClassData());
		
		Join<DATA,CORRELATION> jCorrelation = data.join(JeeslSurveyData.Attributes.correlation.toString());
		predicates.add(jCorrelation.in(correlations));

		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.select(data);
		
		TypedQuery<DATA> tQ = em.createQuery(cQ);
		return tQ.getResultList();
	}
	
	@Override public ANSWER saveAnswer(ANSWER answer) throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(answer.getOption()!=null){answer.setOption(this.find(fbTemplate.getClassOption(),answer.getOption()));}
		return this.saveProtected(answer);
	}

	@Override public void rmAnswer(ANSWER answer) throws JeeslConstraintViolationException
	{
		answer = em.find(fbCore.getClassAnswer(), answer.getId());
		answer.getData().getAnswers().remove(answer);
		this.rmProtected(answer);
	}
}