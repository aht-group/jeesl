package org.jeesl.controller.facade.system;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.jeesl.api.facade.system.JeeslJobFacade;
import org.jeesl.controller.facade.JeeslFacadeBean;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.system.JobFactoryBuilder;
import org.jeesl.factory.ejb.system.job.EjbJobCacheFactory;
import org.jeesl.factory.ejb.system.job.EjbJobFactory;
import org.jeesl.factory.json.system.io.db.tuple.JsonTupleFactory;
import org.jeesl.factory.json.system.io.db.tuple.t1.Json1TuplesFactory;
import org.jeesl.interfaces.model.io.fr.JeeslFileContainer;
import org.jeesl.interfaces.model.system.job.JeeslJob;
import org.jeesl.interfaces.model.system.job.JeeslJobCategory;
import org.jeesl.interfaces.model.system.job.JeeslJobPriority;
import org.jeesl.interfaces.model.system.job.JeeslJobRobot;
import org.jeesl.interfaces.model.system.job.JeeslJobStatus;
import org.jeesl.interfaces.model.system.job.JeeslJobTemplate;
import org.jeesl.interfaces.model.system.job.JeeslJobType;
import org.jeesl.interfaces.model.system.job.cache.JeeslJobCache;
import org.jeesl.interfaces.model.system.job.cache.JeeslJobExpiration;
import org.jeesl.interfaces.model.system.job.feedback.JeeslJobFeedback;
import org.jeesl.interfaces.model.system.job.feedback.JeeslJobFeedbackType;
import org.jeesl.interfaces.model.system.job.mnt.JeeslJobMaintenance;
import org.jeesl.interfaces.model.system.job.mnt.JeeslJobMaintenanceInfo;
import org.jeesl.interfaces.model.system.job.with.EjbWithMigrationJob1;
import org.jeesl.interfaces.model.system.job.with.EjbWithMigrationJob2;
import org.jeesl.interfaces.model.system.job.with.EjbWithMigrationJob3;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.with.primitive.text.EjbWithEmail;
import org.jeesl.model.json.db.tuple.t1.Json1Tuples;

import net.sf.exlp.util.DateUtil;

public class JeeslSystemJobFacadeBean<L extends JeeslLang,D extends JeeslDescription,
									TEMPLATE extends JeeslJobTemplate<L,D,CATEGORY,TYPE,PRIORITY,EXPIRE>,
									CATEGORY extends JeeslJobCategory<L,D,CATEGORY,?>,
									TYPE extends JeeslJobType<L,D,TYPE,?>,
									EXPIRE extends JeeslJobExpiration<L,D,EXPIRE,?>,
									JOB extends JeeslJob<TEMPLATE,PRIORITY,FEEDBACK,STATUS,USER>,
									PRIORITY extends JeeslJobPriority<L,D,PRIORITY,?>,
									FEEDBACK extends JeeslJobFeedback<JOB,FT,USER>,
									FT extends JeeslJobFeedbackType<L,D,FT,?>,
									STATUS extends JeeslJobStatus<L,D,STATUS,?>,
									ROBOT extends JeeslJobRobot<L,D>,
									CACHE extends JeeslJobCache<TEMPLATE,CONTAINER>,
									MNT extends JeeslJobMaintenance<L,D,MNT,?>,
									MNI extends JeeslJobMaintenanceInfo<D,STATUS,MNT>,
									CONTAINER extends JeeslFileContainer<?,?>,
									USER extends EjbWithEmail
									>
					extends JeeslFacadeBean
					implements JeeslJobFacade<L,D,TEMPLATE,CATEGORY,TYPE,EXPIRE,JOB,PRIORITY,FEEDBACK,FT,STATUS,ROBOT,CACHE,MNT,MNI,CONTAINER,USER>
{
	private static final long serialVersionUID = 1L;

	private final JobFactoryBuilder<L,D,TEMPLATE,CATEGORY,TYPE,EXPIRE,JOB,PRIORITY,FEEDBACK,FT,STATUS,ROBOT,CACHE,MNT,MNI,USER> fbJob;
	
	private EjbJobCacheFactory<TEMPLATE,CACHE> efCache;
	private EjbJobFactory<L,D,TEMPLATE,CATEGORY,TYPE,JOB,PRIORITY,FEEDBACK,FT,STATUS,ROBOT,CACHE,USER> efJob;
	
	public JeeslSystemJobFacadeBean(EntityManager em, final JobFactoryBuilder<L,D,TEMPLATE,CATEGORY,TYPE,EXPIRE,JOB,PRIORITY,FEEDBACK,FT,STATUS,ROBOT,CACHE,MNT,MNI,USER> fbJob)
	{
		super(em);
		this.fbJob=fbJob;
		
		efCache = fbJob.cache();
		efJob = fbJob.job();
	}
	
	@Override public <E extends Enum<E>> TEMPLATE fJobTemplate(E type, String code) throws JeeslNotFoundException
	{
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<TEMPLATE> cQ = cB.createQuery(fbJob.getClassTemplate());
		Root<TEMPLATE> template = cQ.from(fbJob.getClassTemplate());
		
		Join<TEMPLATE,TYPE> jType = template.join(JeeslJobTemplate.Attributes.type.toString());
		Expression<String> eType = jType.get(JeeslStatus.EjbAttributes.code.toString());
		Expression<String> eCode = template.get(JeeslJobTemplate.Attributes.code.toString());
		
		predicates.add(cB.equal(eType,type.toString()));
		predicates.add(cB.equal(eCode,code));
		
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.select(template);

		TypedQuery<TEMPLATE> tQ = em.createQuery(cQ);
		try	{return tQ.getSingleResult();}
		catch (NoResultException ex){throw new JeeslNotFoundException("No "+fbJob.getClassTemplate().getSimpleName()+" found for type="+type.toString()+" and code="+code);}
		catch (NonUniqueResultException ex){throw new JeeslNotFoundException("No unique results in "+fbJob.getClassTemplate().getSimpleName()+" for type="+type.toString()+" and code="+code);}
	}
	
	@Override
	public List<JOB> fJobs(TEMPLATE template, String code)
	{
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<JOB> cQ = cB.createQuery(fbJob.getClassJob());
		Root<JOB> job = cQ.from(fbJob.getClassJob());
		
		Join<JOB,TEMPLATE> jTemplate = job.join(JeeslJob.Attributes.template.toString());
		Path<Date> pRecordCreation = job.get(JeeslJob.Attributes.recordCreation.toString());
		Path<String> pCode = job.get(JeeslJob.Attributes.code.toString());
		
		predicates.add(jTemplate.in(template));
		predicates.add(cB.equal(pCode,code));
		
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.orderBy(cB.asc(pRecordCreation));
		cQ.select(job);

		TypedQuery<JOB> tQ = em.createQuery(cQ);
		return tQ.getResultList();
	}
	
	@Override public List<JOB> fJobs(List<CATEGORY> categories, List<TYPE> types, List<STATUS> status, Date from, Date to)
	{
		if(categories==null || categories.isEmpty()){return new ArrayList<JOB>();}
		if(types==null || types.isEmpty()){return new ArrayList<JOB>();}
		if(status==null || status.isEmpty()){return new ArrayList<JOB>();}
		
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<JOB> cQ = cB.createQuery(fbJob.getClassJob());
		Root<JOB> job = cQ.from(fbJob.getClassJob());
		
		Join<JOB,TEMPLATE> jTemplate = job.join(JeeslJob.Attributes.template.toString());
		Path<CATEGORY> pCategory = jTemplate.get(JeeslJobTemplate.Attributes.category.toString());
		Path<TYPE> pType = jTemplate.get(JeeslJobTemplate.Attributes.type.toString());
		
		Path<Date> pRecordCreation = job.get(JeeslJob.Attributes.recordCreation.toString());
		Path<STATUS> pStatus = job.get(JeeslJob.Attributes.status.toString());
		
		Path<PRIORITY> pPriority = job.get(JeeslJob.Attributes.priority.toString());
		Path<Integer> pPosition = pPriority.get(JeeslJobPriority.Attributes.position.toString());
		
		if(from!=null)
		{
			LocalDateTime ldt = DateUtil.toLocalDate(from).atStartOfDay();
			predicates.add(cB.greaterThanOrEqualTo(pRecordCreation, DateUtil.toDate(ldt)));
		}
		if(to!=null)
		{
			LocalDateTime ldt = DateUtil.toLocalDate(to).atStartOfDay().plusDays(1);
			predicates.add(cB.lessThan(pRecordCreation, DateUtil.toDate(ldt)));
		}
		
		predicates.add(pCategory.in(categories));
		predicates.add(pType.in(types));
		predicates.add(pStatus.in(status));
		
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.orderBy(cB.desc(pPosition),cB.asc(pRecordCreation));
		cQ.select(job);

		TypedQuery<JOB> tQ = em.createQuery(cQ);
		return tQ.getResultList();
	}
	
	@Override public JOB fActiveJob(TEMPLATE template, String code) throws JeeslNotFoundException
	{
		List<STATUS> statuses = new ArrayList<STATUS>();
		try
		{
			statuses.add(this.fByCode(fbJob.getClassStatus(),JeeslJobStatus.Code.queue));
			statuses.add(this.fByCode(fbJob.getClassStatus(),JeeslJobStatus.Code.working));
		}
		catch (JeeslNotFoundException e) {e.printStackTrace();}
		
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<JOB> cQ = cB.createQuery(fbJob.getClassJob());
		Root<JOB> job = cQ.from(fbJob.getClassJob());
		
		Join<CACHE,TEMPLATE> jTemplate = job.join(JeeslJob.Attributes.template.toString());
		Path<STATUS> pStatus = job.get(JeeslJob.Attributes.status.toString());
		Expression<String> pCode = job.get(JeeslJob.Attributes.code.toString());
		
		predicates.add(cB.equal(jTemplate,template));
		predicates.add(pStatus.in(statuses));
		predicates.add(cB.equal(pCode,code));
		
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.select(job);

		TypedQuery<JOB> tQ = em.createQuery(cQ);
		List<JOB> results = tQ.getResultList();
		if(results.isEmpty()){throw new JeeslNotFoundException("Nothing found for template:"+template.getCode()+" and job.code="+code);}
		else{return results.get(0);}
	}

	@Override public CACHE fJobCache(TEMPLATE template, String code) throws JeeslNotFoundException
	{
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<CACHE> cQ = cB.createQuery(fbJob.getClassCache());
		Root<CACHE> cache = cQ.from(fbJob.getClassCache());
		
		Join<CACHE,TEMPLATE> jTemplate = cache.join(JeeslJobCache.Attributes.template.toString());
		Expression<String> pCode = cache.get(JeeslJobCache.Attributes.code.toString());
		
		predicates.add(cB.equal(jTemplate,template));
		predicates.add(cB.equal(pCode,code));
		
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.select(cache);

		TypedQuery<CACHE> tQ = em.createQuery(cQ);
		try	{return tQ.getSingleResult();}
		catch (NoResultException ex){throw new JeeslNotFoundException("Nothing found for template:"+template.getCode()+"/"+template.getType().getCode()+" and cache.code="+code);}
		catch (NonUniqueResultException ex){throw new JeeslNotFoundException("Results not unique for template:"+template.getCode()+"/"+template.getType().getCode()+" and cache.code="+code);}
	}

	@Override
	public CACHE uJobCache(TEMPLATE template, String code, byte[] data) throws JeeslConstraintViolationException, JeeslLockingException
	{
		CACHE cache = null;
		
		try {cache = fJobCache(template,code);}
		catch (JeeslNotFoundException e){cache = efCache.build(template, code, data);}
		
		cache.setRecord(new Date());
		cache.setData(data);
		cache = this.save(cache);
		
		return cache;
	}

	@Override
	public JOB cJob(USER user, List<FEEDBACK> feedbacks, TEMPLATE template, String code, String name, String jsonFilter) throws JeeslNotFoundException, JeeslConstraintViolationException, JeeslLockingException
	{
		STATUS status = this.fByCode(fbJob.getClassStatus(),JeeslJobStatus.Code.queue);
		JOB job = efJob.build(user,template,status,code,name,jsonFilter);
		return this.save(job);
	}
	
	
	@Override public <T extends EjbWithMigrationJob1<STATUS>> List<T> fEntitiesWithPendingJob1(Class<T> c, int maxResult, boolean includeNull)
	{
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<T> cQ = cB.createQuery(c);
		Root<T> item = cQ.from(c);
		
		Expression<STATUS> eStatus = item.get(EjbWithMigrationJob1.Tmp.job1.toString());
		STATUS queue = this.fByEnum(fbJob.getClassStatus(),JeeslJobStatus.Code.queue);
		if(includeNull) {predicates.add(cB.or(cB.isNull(eStatus),cB.equal(eStatus, queue)));}
		else {predicates.add(cB.equal(eStatus,queue));}

		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.select(item);

		TypedQuery<T> tQ = em.createQuery(cQ);
		tQ.setMaxResults(maxResult);
		return tQ.getResultList();
	}
	@Override public <T extends EjbWithMigrationJob2<STATUS>> List<T> fEntitiesWithPendingJob2(Class<T> c, int maxResult, boolean includeNull)
	{
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<T> cQ = cB.createQuery(c);
		Root<T> item = cQ.from(c);
		
		Expression<STATUS> eStatus = item.get(EjbWithMigrationJob2.Tmp.job2.toString());
		STATUS queue = this.fByEnum(fbJob.getClassStatus(),JeeslJobStatus.Code.queue);
		if(includeNull) {predicates.add(cB.or(cB.isNull(eStatus),cB.equal(eStatus, queue)));}
		else {predicates.add(cB.equal(eStatus,queue));}

		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.select(item);

		TypedQuery<T> tQ = em.createQuery(cQ);
		tQ.setMaxResults(maxResult);
		return tQ.getResultList();
	}
	@Override public <T extends EjbWithMigrationJob3<STATUS>> List<T> fEntitiesWithPendingJob3(Class<T> c, int maxResult, boolean includeNull)
	{
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<T> cQ = cB.createQuery(c);
		Root<T> item = cQ.from(c);
		
		Expression<STATUS> eStatus = item.get(EjbWithMigrationJob3.Tmp.job3.toString());
		STATUS queue = this.fByEnum(fbJob.getClassStatus(),JeeslJobStatus.Code.queue);
		if(includeNull) {predicates.add(cB.or(cB.isNull(eStatus),cB.equal(eStatus, queue)));}
		else {predicates.add(cB.equal(eStatus,queue));}

		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.select(item);

		TypedQuery<T> tQ = em.createQuery(cQ);
		tQ.setMaxResults(maxResult);
		return tQ.getResultList();
	}
	
	@Override public <T extends EjbWithMigrationJob1<STATUS>> Json1Tuples<STATUS> tpcJob1Status(Class<T> c)
	{
		Json1TuplesFactory<STATUS> jtf = new Json1TuplesFactory<>(fbJob.getClassStatus());
		jtf.setfUtils(this);
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cQ = cB.createTupleQuery();
		Root<T> item = cQ.from(c);
		
		Expression<Long> eCount = cB.count(item.<Long>get("id"));
		Path<STATUS> pStatus = item.get(EjbWithMigrationJob1.Tmp.job1.toString());
		
		cQ.groupBy(pStatus.get("id"));
		cQ.multiselect(pStatus.get("id"),eCount);
	       
		TypedQuery<Tuple> tQ = em.createQuery(cQ);
		return jtf.buildV2(tQ.getResultList(),JsonTupleFactory.Type.count);
	}
	@Override public <T extends EjbWithMigrationJob2<STATUS>> Json1Tuples<STATUS> tpcJob2Status(Class<T> c)
	{
		Json1TuplesFactory<STATUS> jtf = new Json1TuplesFactory<>(fbJob.getClassStatus());
		jtf.setfUtils(this);
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cQ = cB.createTupleQuery();
		Root<T> item = cQ.from(c);
		
		Expression<Long> eCount = cB.count(item.<Long>get("id"));
		Path<STATUS> pStatus = item.get(EjbWithMigrationJob2.Tmp.job2.toString());
		
		cQ.groupBy(pStatus.get("id"));
		cQ.multiselect(pStatus.get("id"),eCount);
	       
		TypedQuery<Tuple> tQ = em.createQuery(cQ);
		return jtf.buildV2(tQ.getResultList(),JsonTupleFactory.Type.count);
	}
	@Override public <T extends EjbWithMigrationJob3<STATUS>> Json1Tuples<STATUS> tpcJob3Status(Class<T> c)
	{
		Json1TuplesFactory<STATUS> jtf = new Json1TuplesFactory<>(fbJob.getClassStatus());
		jtf.setfUtils(this);
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cQ = cB.createTupleQuery();
		Root<T> item = cQ.from(c);
		
		Expression<Long> eCount = cB.count(item.<Long>get("id"));
		Path<STATUS> pStatus = item.get(EjbWithMigrationJob3.Tmp.job3.toString());
		
		cQ.groupBy(pStatus.get("id"));
		cQ.multiselect(pStatus.get("id"),eCount);
	       
		TypedQuery<Tuple> tQ = em.createQuery(cQ);
		return jtf.buildV2(tQ.getResultList(),JsonTupleFactory.Type.count);
	}

	@Override public <T extends EjbWithMigrationJob2<STATUS>> List<T> fEntitiesWithJob2In(Class<T> c, List<STATUS> list, Integer maxResults)
	{
		if(list==null || list.isEmpty()){return new ArrayList<>();}
		
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<T> cQ = cB.createQuery(c);
		Root<T> root = cQ.from(c);
		
		Path<STATUS> pStatus = root.get(EjbWithMigrationJob2.Tmp.job2.toString());
		predicates.add(pStatus.in(list));
		
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
//		cQ.orderBy(cB.desc(pPosition),cB.asc(pRecordCreation));
		cQ.select(root);

		TypedQuery<T> tQ = em.createQuery(cQ);
		if(maxResults!=null) {tQ.setMaxResults(maxResults);}
		return tQ.getResultList();
	}
	
}