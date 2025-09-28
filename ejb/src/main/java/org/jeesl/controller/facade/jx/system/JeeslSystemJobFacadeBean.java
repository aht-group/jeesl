package org.jeesl.controller.facade.jx.system;

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

import org.apache.commons.lang3.ObjectUtils;
import org.exlp.util.system.DateUtil;
import org.jeesl.api.facade.system.JeeslJobFacade;
import org.jeesl.controller.facade.jx.JeeslFacadeBean;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.system.JobFactoryBuilder;
import org.jeesl.factory.ejb.system.job.EjbJobCacheFactory;
import org.jeesl.factory.ejb.system.job.EjbJobFactory;
import org.jeesl.factory.json.system.io.db.tuple.t1.Json1TuplesFactory;
import org.jeesl.interfaces.model.io.fr.JeeslFileContainer;
import org.jeesl.interfaces.model.system.feedback.JeeslJobFeedback;
import org.jeesl.interfaces.model.system.feedback.JeeslJobFeedbackType;
import org.jeesl.interfaces.model.system.job.cache.JeeslJobCache;
import org.jeesl.interfaces.model.system.job.cache.JeeslJobExpiration;
import org.jeesl.interfaces.model.system.job.core.JeeslJob;
import org.jeesl.interfaces.model.system.job.core.JeeslJobPriority;
import org.jeesl.interfaces.model.system.job.core.JeeslJobStatus;
import org.jeesl.interfaces.model.system.job.maintenance.JeeslJobMaintenance;
import org.jeesl.interfaces.model.system.job.maintenance.JeeslJobMaintenanceInfo;
import org.jeesl.interfaces.model.system.job.maintenance.JeeslJobRobot;
import org.jeesl.interfaces.model.system.job.template.JeeslJobCategory;
import org.jeesl.interfaces.model.system.job.template.JeeslJobTemplate;
import org.jeesl.interfaces.model.system.job.template.JeeslJobType;
import org.jeesl.interfaces.model.system.job.with.EjbWithMigrationJob1;
import org.jeesl.interfaces.model.system.job.with.EjbWithMigrationJob2;
import org.jeesl.interfaces.model.system.job.with.EjbWithMigrationJob3;
import org.jeesl.interfaces.model.system.job.with.EjbWithMigrationJob4;
import org.jeesl.interfaces.model.system.job.with.EjbWithMigrationJob5;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;
import org.jeesl.interfaces.util.query.system.JeeslJobQuery;
import org.jeesl.model.ejb.io.db.JeeslCq;
import org.jeesl.model.json.io.db.tuple.container.JsonTuples1;

public class JeeslSystemJobFacadeBean<L extends JeeslLang,D extends JeeslDescription,
									TEMPLATE extends JeeslJobTemplate<L,D,CATEGORY,TYPE,PRIORITY,EXPIRE>,
									CATEGORY extends JeeslJobCategory<L,D,CATEGORY,?>,
									TYPE extends JeeslJobType<L,D,TYPE,?>,
									EXPIRE extends JeeslJobExpiration<L,D,EXPIRE,?>,
									JOB extends JeeslJob<TEMPLATE,PRIORITY,STATUS,USER>,
									PRIORITY extends JeeslJobPriority<L,D,PRIORITY,?>,
									FEEDBACK extends JeeslJobFeedback<FT,USER>,
									FT extends JeeslJobFeedbackType<L,D,FT,?>,
									STATUS extends JeeslJobStatus<L,D,STATUS,?>,
									ROBOT extends JeeslJobRobot<L,D>,
									CACHE extends JeeslJobCache<TEMPLATE,CONTAINER>,
									MNT extends JeeslJobMaintenance<L,D,MNT,?>,
									MNI extends JeeslJobMaintenanceInfo<D,STATUS,MNT>,
									CONTAINER extends JeeslFileContainer<?,?>,
									USER extends JeeslSimpleUser
									>
					extends JeeslFacadeBean
					implements JeeslJobFacade<TEMPLATE,CATEGORY,TYPE,JOB,PRIORITY,FEEDBACK,FT,STATUS,ROBOT,CACHE,MNT,MNI,CONTAINER,USER>
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
	
	@Override public List<JOB> fJobs(JeeslJobQuery<TEMPLATE,STATUS> query)
	{
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<JOB> cQ = cB.createQuery(fbJob.getClassJob());
		Root<JOB> job = cQ.from(fbJob.getClassJob());
		
		if(ObjectUtils.isNotEmpty(query.getSystemJobTemplates()))
		{
			Path<TEMPLATE> pTemplate = job.get(JeeslJob.Attributes.template.toString());
			predicates.add(pTemplate.in(query.getSystemJobTemplates()));
		}
			
//		predicates.add(pType.in(types));
//		predicates.add(pStatus.in(status));
		
		cQ.select(job);
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
//		cQ.orderBy(cB.desc(pPosition),cB.asc(pRecordCreation));

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
		
		cQ.select(job);
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.orderBy(cB.desc(pPosition),cB.asc(pRecordCreation));

		TypedQuery<JOB> tQ = em.createQuery(cQ);
		return tQ.getResultList();
	}
	
	@Override public JOB fActiveJob(TEMPLATE template, String code) throws JeeslNotFoundException
	{
		List<STATUS> statuses = new ArrayList<STATUS>();
		statuses.add(this.fByEnum(fbJob.getClassStatus(),JeeslJobStatus.Code.queue));
		statuses.add(this.fByEnum(fbJob.getClassStatus(),JeeslJobStatus.Code.working));
		statuses.add(this.fByEnum(fbJob.getClassStatus(),JeeslJobStatus.Code.deferred));
		
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<JOB> cQ = cB.createQuery(fbJob.getClassJob());
		Root<JOB> job = cQ.from(fbJob.getClassJob());
		
		Join<JOB,TEMPLATE> jTemplate = job.join(JeeslJob.Attributes.template.toString());
		Path<STATUS> pStatus = job.get(JeeslJob.Attributes.status.toString());
		Expression<String> pCode = job.get(JeeslJob.Attributes.code.toString());
		
		predicates.add(cB.equal(jTemplate,template));
		predicates.add(pStatus.in(statuses));
		predicates.add(cB.equal(pCode,code));
		
		cQ.select(job);
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));

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
	@Override public <T extends EjbWithMigrationJob4<STATUS>> List<T> fEntitiesWithPendingJob4(Class<T> c, int maxResult, boolean includeNull)
	{
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<T> cQ = cB.createQuery(c);
		Root<T> item = cQ.from(c);
		
		Expression<STATUS> eStatus = item.get(EjbWithMigrationJob4.Attributes.job4.toString());
		STATUS queue = this.fByEnum(fbJob.getClassStatus(),JeeslJobStatus.Code.queue);
		if(includeNull) {predicates.add(cB.or(cB.isNull(eStatus),cB.equal(eStatus, queue)));}
		else {predicates.add(cB.equal(eStatus,queue));}

		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.select(item);

		TypedQuery<T> tQ = em.createQuery(cQ);
		tQ.setMaxResults(maxResult);
		return tQ.getResultList();
	}
	@Override public <T extends EjbWithMigrationJob5<STATUS>> List<T> fEntitiesWithPendingJob5(Class<T> c, int maxResult, boolean includeNull)
	{
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<T> cQ = cB.createQuery(c);
		Root<T> item = cQ.from(c);
		
		Expression<STATUS> eStatus = item.get(EjbWithMigrationJob5.Attributes.job5.toString());
		STATUS queue = this.fByEnum(fbJob.getClassStatus(),JeeslJobStatus.Code.queue);
		if(includeNull) {predicates.add(cB.or(cB.isNull(eStatus),cB.equal(eStatus, queue)));}
		else {predicates.add(cB.equal(eStatus,queue));}

		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.select(item);

		TypedQuery<T> tQ = em.createQuery(cQ);
		tQ.setMaxResults(maxResult);
		return tQ.getResultList();
	}
	
	@Override public <T extends EjbWithMigrationJob1<STATUS>> JsonTuples1<STATUS> tpcJob1Status(Class<T> c)
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
		return jtf.buildV2(tQ.getResultList(),JeeslCq.Agg.count);
	}
	@Override public <T extends EjbWithMigrationJob2<STATUS>> JsonTuples1<STATUS> tpcJob2Status(Class<T> c)
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
		return jtf.buildV2(tQ.getResultList(),JeeslCq.Agg.count);
	}
	@Override public <T extends EjbWithMigrationJob3<STATUS>> JsonTuples1<STATUS> tpcJob3Status(Class<T> c)
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cQ = cB.createTupleQuery();
		Root<T> item = cQ.from(c);
		
		Expression<Long> eCount = cB.count(item.<Long>get("id"));
		Path<STATUS> pStatus = item.get(EjbWithMigrationJob3.Tmp.job3.toString());
		
		cQ.groupBy(pStatus.get("id"));
		cQ.multiselect(pStatus.get("id"),eCount);
		
		return Json1TuplesFactory.instance(fbJob.getClassStatus()).tupleLoad(this,true).buildV2(em.createQuery(cQ).getResultList(),JeeslCq.Agg.count);
	}
	@Override public <T extends EjbWithMigrationJob4<STATUS>> JsonTuples1<STATUS> tpcJob4Status(Class<T> c)
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cQ = cB.createTupleQuery();
		Root<T> item = cQ.from(c);		
		Path<STATUS> pStatus = item.get(EjbWithMigrationJob4.Attributes.job4.toString());

		cQ.multiselect(pStatus.get("id"),cB.count(item.<Long>get("id")));
		cQ.groupBy(pStatus.get("id"));

		return Json1TuplesFactory.instance(fbJob.getClassStatus()).tupleLoad(this,true).buildV2(em.createQuery(cQ).getResultList(),JeeslCq.Agg.count);
	}
	@Override public <T extends EjbWithMigrationJob5<STATUS>> JsonTuples1<STATUS> tpcJob5Status(Class<T> c)
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cQ = cB.createTupleQuery();
		Root<T> item = cQ.from(c);
		Path<STATUS> pStatus = item.get(EjbWithMigrationJob5.Attributes.job5.toString());
		
		cQ.multiselect(pStatus.get("id"),cB.count(item.<Long>get("id")));
		cQ.groupBy(pStatus.get("id"));
	       
		TypedQuery<Tuple> tQ = em.createQuery(cQ);
		return Json1TuplesFactory.instance(fbJob.getClassStatus()).tupleLoad(this,true).buildV2(tQ.getResultList(),JeeslCq.Agg.count);
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
	
	@Override public JsonTuples1<TEMPLATE> tpJobJobByTemplate(JeeslJobQuery<TEMPLATE,STATUS> query)
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cQ = cB.createTupleQuery();
		Root<JOB> item = cQ.from(fbJob.getClassJob());
		
		Path<TEMPLATE> pTemplate = item.get(JeeslJob.Attributes.template.toString());
		
		cQ.multiselect(pTemplate.get("id"),cB.count(item.<Long>get("id")));
		cQ.groupBy(pTemplate.get("id"));
	       
		TypedQuery<Tuple> tQ = em.createQuery(cQ);
		return Json1TuplesFactory.instance(fbJob.getClassTemplate()).tupleLoad(this,query.getTupleLoad()).buildV2(tQ.getResultList(),JeeslCq.Agg.count);
	}
	@Override public JsonTuples1<TEMPLATE> tpJobCacheByTemplate(JeeslJobQuery<TEMPLATE,STATUS> query)
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cQ = cB.createTupleQuery();
		Root<CACHE> item = cQ.from(fbJob.getClassCache());
		
		Path<TEMPLATE> pTemplate = item.get(JeeslJobCache.Attributes.template.toString());
		
		cQ.multiselect(pTemplate.get("id"),cB.count(item.<Long>get("id")));
		cQ.groupBy(pTemplate.get("id"));
	       
		TypedQuery<Tuple> tQ = em.createQuery(cQ);
		return Json1TuplesFactory.instance(fbJob.getClassTemplate()).tupleLoad(this,query.getTupleLoad()).buildV2(tQ.getResultList(),JeeslCq.Agg.count);
	}
}