package org.jeesl.controller.facade.module.survey;

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
import org.jeesl.factory.builder.module.survey.SurveyTemplateFactoryBuilder;
import org.jeesl.factory.ejb.module.survey.EjbSurveyTemplateFactory;
import org.jeesl.factory.ejb.util.EjbIdFactory;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurveyScheme;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurveyScore;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurveyTemplate;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurveyTemplateVersion;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyCondition;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyOption;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyOptionSet;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyQuestion;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveySection;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyValidation;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyValidationAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.controller.facade.UtilsFacadeBean;
import net.sf.ahtutils.interfaces.model.status.UtilsDescription;
import net.sf.ahtutils.interfaces.model.status.UtilsLang;
import net.sf.ahtutils.interfaces.model.status.UtilsStatus;

public class JeeslSurveyTemplateFacadeBean <L extends UtilsLang, D extends UtilsDescription,LOC extends UtilsStatus<LOC,L,D>,
											SCHEME extends JeeslSurveyScheme<L,D,TEMPLATE,SCORE>,
											VALGORITHM extends JeeslSurveyValidationAlgorithm<L,D>,
											TEMPLATE extends JeeslSurveyTemplate<L,D,SCHEME,TEMPLATE,VERSION,TS,TC,SECTION,OPTIONS,?>,
											VERSION extends JeeslSurveyTemplateVersion<L,D,TEMPLATE>,
											TS extends UtilsStatus<TS,L,D>,
											TC extends UtilsStatus<TC,L,D>,
											SECTION extends JeeslSurveySection<L,D,TEMPLATE,SECTION,QUESTION>,
											QUESTION extends JeeslSurveyQuestion<L,D,SECTION,CONDITION,VALIDATION,QE,SCORE,UNIT,OPTIONS,OPTION,?>,
											CONDITION extends JeeslSurveyCondition<QUESTION,QE,OPTION>,
											VALIDATION extends JeeslSurveyValidation<QUESTION,VALGORITHM>,
											QE extends UtilsStatus<QE,L,D>,
											SCORE extends JeeslSurveyScore<L,D,SCHEME,QUESTION>,
											UNIT extends UtilsStatus<UNIT,L,D>,
											OPTIONS extends JeeslSurveyOptionSet<L,D,TEMPLATE,OPTION>,
											OPTION extends JeeslSurveyOption<L,D>>
	extends UtilsFacadeBean implements JeeslSurveyTemplateFacade<L,D,SCHEME,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,QE,SCORE,UNIT,OPTIONS,OPTION>
{
	final static Logger logger = LoggerFactory.getLogger(JeeslSurveyTemplateFacadeBean.class);
	
	private final SurveyTemplateFactoryBuilder<L,D,LOC,SCHEME,VALGORITHM,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,CONDITION,VALIDATION,QE,SCORE,UNIT,OPTIONS,OPTION> fbTemplate;
	
	private final EjbSurveyTemplateFactory<L,D,TEMPLATE,TS,TC,SECTION,QUESTION> eTemplate;
	
	public JeeslSurveyTemplateFacadeBean(EntityManager em, SurveyTemplateFactoryBuilder<L,D,LOC,SCHEME,VALGORITHM,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,CONDITION,VALIDATION,QE,SCORE,UNIT,OPTIONS,OPTION> fbTemplate)
	{
		super(em);
		this.fbTemplate=fbTemplate;
		
		eTemplate = fbTemplate.template();
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
}