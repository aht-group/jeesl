package org.jeesl.controller.facade.jx.module.survey;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.jeesl.api.facade.module.survey.JeeslSurveyTemplateFacade;
import org.jeesl.controller.facade.jx.JeeslFacadeBean;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.builder.module.survey.SurveyTemplateFactoryBuilder;
import org.jeesl.factory.ejb.module.survey.EjbSurveyTemplateFactory;
import org.jeesl.factory.ejb.util.EjbIdFactory;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurveyScheme;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurveyScore;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurveyTemplate;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurveyTemplateCategory;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurveyTemplateStatus;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurveyTemplateVersion;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslSurveyTemplateFacadeBean <L extends JeeslLang, D extends JeeslDescription,LOC extends JeeslStatus<L,D,LOC>,
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
											OPTIONS extends JeeslSurveyOptionSet<L,D,TEMPLATE,OPTION>,
											OPTION extends JeeslSurveyOption<L,D>>
	extends JeeslFacadeBean implements JeeslSurveyTemplateFacade<SCHEME,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,QE,SCORE,OPTIONS,OPTION>
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslSurveyTemplateFacadeBean.class);
	
	private final SurveyTemplateFactoryBuilder<L,D,LOC,SCHEME,VALGORITHM,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,CONDITION,VALIDATION,QE,SCORE,UNIT,OPTIONS,OPTION> fbTemplate;
	
	private final EjbSurveyTemplateFactory<L,D,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION> eTemplate;
	
	public JeeslSurveyTemplateFacadeBean(EntityManager em, SurveyTemplateFactoryBuilder<L,D,LOC,SCHEME,VALGORITHM,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,CONDITION,VALIDATION,QE,SCORE,UNIT,OPTIONS,OPTION> fbTemplate)
	{
		super(em);
		this.fbTemplate=fbTemplate;
		
		eTemplate = fbTemplate.template();
	}
	
	@Override public QUESTION loadSurveyQuersion(QUESTION question)
	{
		question = em.find(fbTemplate.getClassQuestion(),question.getId());
		question.getScores().size();
		question.getOptions().size();
		question.getConditions().size();
		question.getValidations().size();
		return question;
	}
	
	@Override public OPTIONS loadSurveyOptions(OPTIONS optionSet)
	{
		optionSet = em.find(fbTemplate.getOptionSetClass(),optionSet.getId());
		optionSet.getOptions().size();
		return optionSet;
	}
	
	@Override public TEMPLATE load(TEMPLATE template,boolean withQuestions, boolean withOptions)
	{
		template = em.find(fbTemplate.getClassTemplate(),template.getId());
		
		template.getSchemes().size();
		template.getOptionSets().size();
		if(withQuestions)
		{
			for(SECTION section : template.getSections())
			{
				if(withOptions)
				{
					for(QUESTION question : section.getQuestions())
					{
						question.getOptions().size();
					}
				}
				else
				{
					section.getQuestions().size();
				}
			}
		}
		else
		{
			template.getSections().size();
		}
		return template;
	}
	
	@Override public TEMPLATE fcSurveyTemplate(TC category, TS status){return fcSurveyTemplate(category,null,status,null);}
	@Override public TEMPLATE fcSurveyTemplate(TC category, VERSION version, TS status, VERSION nestedVersion)
	{
		if(logger.isInfoEnabled())
		{
			logger.info("Query:");
			logger.info("\tCategory: "+category.getCode());
			if(version!=null) {logger.info("\tVersion: "+version.toString()+" (unsaved:"+EjbIdFactory.isUnSaved(version)+")");}
			logger.info("\tStatus: "+status.getCode());
		}
		
		if(version!=null && EjbIdFactory.isUnSaved(version))
		{
			TEMPLATE template = eTemplate.build(category,status,"");
			template.setVersion(version);
			template.getVersion().setTemplate(template);
			if(nestedVersion!=null){template.setNested(nestedVersion.getTemplate());}
			em.persist(template);
			return template;
		}
		
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<TEMPLATE> cQ = cB.createQuery(fbTemplate.getClassTemplate());
		Root<TEMPLATE> template = cQ.from(fbTemplate.getClassTemplate());
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		Path<TC> pCategory = template.get(JeeslSurveyTemplate.Attributes.category.toString());
		predicates.add(cB.equal(pCategory,category));
		
		if(version!=null)
		{
			logger.info("Using Version: "+version.toString());
			Join<TEMPLATE,VERSION> jVersion = template.join(JeeslSurveyTemplate.Attributes.version.toString());
//			predicates.add(cB.equal(jVersion,version));
			predicates.add(cB.isTrue(jVersion.in(version.getId())));
		}
		
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.select(template);

		List<TEMPLATE> list = em.createQuery(cQ).getResultList();
		if(logger.isInfoEnabled())
		{
			logger.info("Results: "+list.size());
			for(TEMPLATE t : list)
			{
				logger.info("\t"+t.toString());
			}
		}
		
		if(list.isEmpty())
		{
			TEMPLATE t = eTemplate.build(category,status,"");
			if(nestedVersion!=null){t.setNested(nestedVersion.getTemplate());}
			em.persist(t);
			return t;
		}
		else{return list.get(0);}
	}
	
	@Override public OPTION saveOption2(QUESTION question, OPTION option) throws JeeslConstraintViolationException, JeeslLockingException
	{
		question = em.find(fbTemplate.getClassQuestion(), question.getId());
		option = this.saveProtected(option);
		if(!question.getOptions().contains(option))
		{
			question.getOptions().add(option);
			this.save(question);
		}
		return option;
	}
	@Override public OPTION saveOption2(OPTIONS set, OPTION option) throws JeeslConstraintViolationException, JeeslLockingException
	{
		set = em.find(fbTemplate.getOptionSetClass(), set.getId());
		option = this.saveProtected(option);
		if(!set.getOptions().contains(option))
		{
			set.getOptions().add(option);
			this.save(set);
		}
		return option;
	}
	
	@Override public void rmOption2(QUESTION question, OPTION option) throws JeeslConstraintViolationException, JeeslLockingException
	{
		question = em.find(fbTemplate.getClassQuestion(), question.getId());
		option = em.find(fbTemplate.getClassOption(), option.getId());
		if(question.getOptions().contains(option))
		{
			question.getOptions().remove(option);
			this.save(question);
		}
		this.rmProtected(option);
	}
	@Override public void rmOption2(OPTIONS set, OPTION option) throws JeeslConstraintViolationException, JeeslLockingException
	{
		set = em.find(fbTemplate.getOptionSetClass(), set.getId());
		option = em.find(fbTemplate.getClassOption(), option.getId());
		if(set.getOptions().contains(option))
		{
			set.getOptions().remove(option);
			this.save(set);
		}
		this.rmProtected(option);
	}
}