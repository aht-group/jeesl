package org.jeesl.controller.facade.jx.module.survey;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.jeesl.api.facade.module.survey.JeeslSurveyAnalysisFacade;
import org.jeesl.api.facade.system.JeeslJobFacade;
import org.jeesl.controller.facade.jx.JeeslFacadeBean;
import org.jeesl.controller.io.db.NativeQueryDebugger;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.module.survey.SurveyAnalysisFactoryBuilder;
import org.jeesl.factory.builder.module.survey.SurveyCoreFactoryBuilder;
import org.jeesl.factory.ejb.module.survey.EjbSurveyAnalysisToolFactory;
import org.jeesl.factory.json.module.survey.JsonSurveyValueFactory;
import org.jeesl.factory.json.module.survey.JsonSurveyValuesFactory;
import org.jeesl.factory.json.system.io.report.JsonFlatFigureFactory;
import org.jeesl.factory.json.system.io.report.JsonFlatFiguresFactory;
import org.jeesl.factory.sql.module.SqlSurveyAnalysisFactory;
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
import org.jeesl.interfaces.model.module.survey.core.JeeslSurveyTemplateVersion;
import org.jeesl.interfaces.model.module.survey.correlation.JeeslSurveyCorrelation;
import org.jeesl.interfaces.model.module.survey.data.JeeslSurveyAnswer;
import org.jeesl.interfaces.model.module.survey.data.JeeslSurveyData;
import org.jeesl.interfaces.model.module.survey.data.JeeslSurveyMatrix;
import org.jeesl.interfaces.model.module.survey.data.JeeslSurveyStatus;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyOption;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyOptionSet;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyQuestion;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyQuestionType;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyQuestionUnit;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveySection;
import org.jeesl.interfaces.model.system.job.cache.JeeslJobCache;
import org.jeesl.interfaces.model.system.job.template.JeeslJobTemplate;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.model.json.JsonFlatFigure;
import org.jeesl.model.json.JsonFlatFigures;
import org.jeesl.model.json.module.survey.JsonSurveyValue;
import org.jeesl.model.json.module.survey.JsonSurveyValues;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslSurveyAnalysisFacadeBean <L extends JeeslLang, D extends JeeslDescription, 
				SURVEY extends JeeslSurvey<L,D,SS,TEMPLATE,DATA>,
				SS extends JeeslSurveyStatus<L,D,SS,?>,
				SCHEME extends JeeslSurveyScheme<L,D,TEMPLATE,SCORE>,
				TEMPLATE extends JeeslSurveyTemplate<L,D,SCHEME,TEMPLATE,VERSION,?,?,SECTION,OPTIONS,ANALYSIS>,
				VERSION extends JeeslSurveyTemplateVersion<L,D,TEMPLATE>,
				SECTION extends JeeslSurveySection<L,D,TEMPLATE,SECTION,QUESTION>,
				QUESTION extends JeeslSurveyQuestion<L,D,SECTION,?,?,QE,SCORE,UNIT,OPTIONS,OPTION,AQ>,
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
				TOOL extends JeeslSurveyAnalysisTool<L,D,QE,QUERY,DATTRIBUTE,AQ,TOOLT>,
				TOOLT extends JeeslStatus<L,D,TOOLT>,
				TOOLCACHETEMPLATE extends JeeslJobTemplate<L,D,?,?,?,?>,
				TOOLCACHE extends JeeslJobCache<TOOLCACHETEMPLATE,?>>
	extends JeeslFacadeBean implements JeeslSurveyAnalysisFacade<SURVEY,QUESTION,DATA,OPTIONS,OPTION,CORRELATION,DOMAIN,QUERY,PATH,DENTITY,DATTRIBUTE,ANALYSIS,AQ,TOOL,TOOLT>
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslSurveyAnalysisFacadeBean.class);
	
	@SuppressWarnings("unused")
	private JeeslJobFacade<TOOLCACHETEMPLATE,?,?,?,?,?,?,?,?,?,TOOLCACHE,?,?,?,?> fJob;
	
	private final SurveyAnalysisFactoryBuilder<L,D,TEMPLATE,QUESTION,QE,SCORE,ANSWER,MATRIX,DATA,OPTION,CORRELATION,DOMAIN,QUERY,PATH,DENTITY,DATTRIBUTE,ANALYSIS,AQ,TOOL,TOOLT,TOOLCACHETEMPLATE> fbAnalyis;
	private final EjbSurveyAnalysisToolFactory <L,D,AQ,TOOL,TOOLT> efTool;
	private final SqlSurveyAnalysisFactory<SURVEY,QUESTION,ANSWER,DATA,CORRELATION,DOMAIN,QUERY,PATH,DENTITY,DATTRIBUTE,ANALYSIS,AQ,TOOL> sqlFactory;
	
	public JeeslSurveyAnalysisFacadeBean(EntityManager em,
			final SurveyCoreFactoryBuilder<L,D,SURVEY,SS,SCHEME,?,VERSION,?,?,SECTION,QUESTION,ANSWER,MATRIX,DATA,OPTION,CORRELATION> fbCore,
			final SurveyAnalysisFactoryBuilder<L,D,TEMPLATE,QUESTION,QE,SCORE,ANSWER,MATRIX,DATA,OPTION,CORRELATION,DOMAIN,QUERY,PATH,DENTITY,DATTRIBUTE,ANALYSIS,AQ,TOOL,TOOLT,TOOLCACHETEMPLATE> fbAnalyis)
	{
		super(em);
		this.fbAnalyis=fbAnalyis;
		
		sqlFactory = new SqlSurveyAnalysisFactory<SURVEY,QUESTION,ANSWER,DATA,CORRELATION,DOMAIN,QUERY,PATH,DENTITY,DATTRIBUTE,ANALYSIS,AQ,TOOL>(fbCore,fbAnalyis);
		efTool = fbAnalyis.ejbAnalysisTool();
	}
	
	@Override public TOOL load(TOOL tool)
	{
		tool = em.find(fbAnalyis.getClassAnalysisTool(),tool.getId());
		if(tool.getQuery()!=null)
		{
			tool.getQuery().getPaths().size();
		}
		return tool;
	}
	
	@Override public AQ fAnalysis(ANALYSIS analysis, QUESTION question) throws JeeslNotFoundException
	{
		return this.oneForParents(fbAnalyis.getClassAnalysisQuestion(), JeeslSurveyAnalysisQuestion.Attributes.analysis,analysis, JeeslSurveyAnalysisQuestion.Attributes.question,question);
	}
/*	
	@Override
	public List<DATTRIBUTE> fDomainAttributes(DENTITY entity)
	{
		entity = em.find(fbAnalyis.getClassDomainEntity(), entity.getId());
		entity.getAttributes().size();
		return entity.getAttributes();
	}
*/	
	@Override public JsonFlatFigures surveyCountRecords(List<SURVEY> surveys)
	{
		JsonFlatFigures result = JsonFlatFiguresFactory.build();
		if(surveys==null || surveys.isEmpty()) {return result;} 
		 
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
        CriteriaQuery<Tuple> cQ = cB.createTupleQuery();
        Root<DATA> data = cQ.from(fbAnalyis.getClassData());
        
        Path<SURVEY> pSurvey = data.get(JeeslSurveyData.Attributes.survey.toString());
        predicates.add(pSurvey.in(surveys));
        
        Expression<Long> eTa = cB.count(data.<Long>get("id"));
      
        cQ.groupBy(pSurvey.get("id"));
        cQ.multiselect(pSurvey.get("id"),eTa);
        
        cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
        TypedQuery<Tuple> tQ = em.createQuery(cQ);
        List<Tuple> tuples = tQ.getResultList();
           
        for(Tuple t : tuples)
        {
	        	JsonFlatFigure f = JsonFlatFigureFactory.build();
	        	f.setL1((Long)t.get(0));
	        	f.setL2((Long)t.get(1));
	        	result.getFigures().add(f);
        }
        
        return result;
	}
	
	@Override public JsonSurveyValue surveyCountAnswers(QUESTION question)
	{
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
        CriteriaQuery<Tuple> cQ = cB.createTupleQuery();
        Root<ANSWER> answer = cQ.from(fbAnalyis.getClassAnswer());
        
        Path<QUESTION> pQuestion = answer.get(JeeslSurveyAnswer.Attributes.question.toString());
        predicates.add(cB.equal(pQuestion, question));
        
        Expression<Long> eTa = cB.count(answer.<Long>get("id"));
        
        cQ.groupBy(pQuestion.get("id"));
        cQ.multiselect(pQuestion.get("id"),eTa);
        
        cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
        TypedQuery<Tuple> tQ = em.createQuery(cQ);
        List<Tuple> tuples = tQ.getResultList();
        
        JsonSurveyValues values = JsonSurveyValuesFactory.build();
        for(Tuple t : tuples)
        {
        		JsonSurveyValue v = JsonSurveyValueFactory.build((Long)t.get(0), (Long)t.get(1));
	        	values.getValues().add(v);
        }
        return values.getValues().get(0);
	}
	
	@Override public JsonFlatFigures surveyCountAnswer(List<QUESTION> questions, SURVEY survey, List<CORRELATION> correlations)
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
        CriteriaQuery<Tuple> cQ = cB.createTupleQuery();
        
        Root<ANSWER> answer = cQ.from(fbAnalyis.getClassAnswer());
        
        Path<QUESTION> pQuestion = answer.get(JeeslSurveyAnswer.Attributes.question.toString());
     
        Expression<Long> eTa = cB.count(answer.<Long>get("id"));
      
        cQ.groupBy(pQuestion.get("id"));
        cQ.multiselect(pQuestion.get("id"),eTa);
        cQ.where(pQuestion.in(questions));
        
        TypedQuery<Tuple> tQ = em.createQuery(cQ);
        List<Tuple> tuples = tQ.getResultList();
        
        JsonFlatFigures result = JsonFlatFiguresFactory.build();
        for(Tuple t : tuples)
        {
	        	JsonFlatFigure f = JsonFlatFigureFactory.build();
	        	f.setL1((Long)t.get(0));
	        	f.setL2((Long)t.get(1));
	        	result.getFigures().add(f);
        }
        
        return result;
	}
	
	@Override public JsonFlatFigures surveyStatisticOption(QUESTION question, SURVEY survey, TOOL tool)
	{
		JsonFlatFigures result = JsonFlatFiguresFactory.build();
		
		String sql = sqlFactory.option(question,survey,tool);
		NativeQueryDebugger.debug(null, sql, false, false);
		
		for(Object o : em.createNativeQuery(sql).getResultList())
        {
            Object[] array = (Object[])o;

    		JsonFlatFigure f = JsonFlatFigureFactory.build();
    		f.setL1(((BigInteger)array[0]).longValue());										// ID Question
    		f.setL2(((BigInteger)array[1]).longValue());										// ID Option
    		f.setL3(((BigInteger)array[2]).longValue());										// Count
    		if(efTool.withDomainQuery(tool)){f.setL4(((BigInteger)array[3]).longValue());}	// ID Path}
    		
    	 	result.getFigures().add(f);
        }
        return result;
	}
	
	@Override public JsonSurveyValues surveyStatisticBoolean(QUESTION question, SURVEY survey, TOOL tool)
	{
		JsonSurveyValues values = JsonSurveyValuesFactory.build();
		
		String sql = sqlFactory.bool(question,survey,tool);
		NativeQueryDebugger.debug(null, sql, false, false);
		
		for(Object o : em.createNativeQuery(sql).getResultList())
        {
            Object[] array = (Object[])o;

            JsonSurveyValue v = JsonSurveyValueFactory.build();
            v.setQuestionId(((BigInteger)array[0]).longValue());
            v.setBool(((Boolean)array[1]).booleanValue());
            v.setCount(((BigInteger)array[2]).longValue());
            if(efTool.withDomainQuery(tool)){v.setPathId(((BigInteger)array[3]).longValue());}
            values.getValues().add(v);
        }
        return values;
	}
	
	@Override public JsonFlatFigures surveyCountOption(List<QUESTION> questions, SURVEY survey, List<CORRELATION> correlations)
	{
		if(questions==null || questions.isEmpty() || correlations==null || correlations.isEmpty()) {return JsonFlatFiguresFactory.build();} 
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
        CriteriaQuery<Tuple> cQ = cB.createTupleQuery();
        Root<ANSWER> answer = cQ.from(fbAnalyis.getClassAnswer());
        
        Path<QUESTION> pQuestion = answer.get(JeeslSurveyAnswer.Attributes.question.toString());
        predicates.add(pQuestion.in(questions));
        
        Join<ANSWER,DATA> jData = answer.join(JeeslSurveyAnswer.Attributes.data.toString());
        Join<DATA,CORRELATION> jCorrelation = jData.join(JeeslSurveyData.Attributes.correlation.toString());
        predicates.add(jCorrelation.in(correlations));
        
        Path<OPTION> pOption = answer.get(JeeslSurveyAnswer.Attributes.option.toString());
        
        Expression<Long> eTa = cB.count(answer.<Long>get(JeeslSurveyAnswer.Attributes.option.toString()));
      
        cQ.groupBy(pQuestion.get("id"),pOption.get("id"));
        cQ.multiselect(pQuestion.get("id"),pOption.get("id"),eTa);
        
        cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
        TypedQuery<Tuple> tQ = em.createQuery(cQ);
        List<Tuple> tuples = tQ.getResultList();
        
        JsonFlatFigures result = JsonFlatFiguresFactory.build();
        for(Tuple t : tuples)
        {
	        	JsonFlatFigure f = JsonFlatFigureFactory.build();
	        	f.setL1((Long)t.get(0));
	        	f.setL2((Long)t.get(1));
	        	f.setL3((Long)t.get(2));
	        	result.getFigures().add(f);
        }
        
        return result;
	}
}