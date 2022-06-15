package org.jeesl.controller.facade.io;

import java.util.ArrayList;
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

import org.jeesl.api.facade.io.JeeslIoSsiFacade;
import org.jeesl.controller.facade.JeeslFacadeBean;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.io.ssi.IoSsiDataFactoryBuilder;
import org.jeesl.factory.json.system.io.db.tuple.JsonTupleFactory;
import org.jeesl.factory.json.system.io.db.tuple.t1.Json1TuplesFactory;
import org.jeesl.factory.json.system.io.db.tuple.t2.Json2TuplesFactory;
import org.jeesl.interfaces.model.io.revision.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiCredential;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiHost;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiSystem;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiAttribute;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiCleaning;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiData;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiLink;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiMapping;
import org.jeesl.interfaces.model.io.ssi.maintenance.EjbWithSsiDataCleaning;
import org.jeesl.interfaces.model.system.job.EjbWithMigrationJob1;
import org.jeesl.interfaces.model.system.job.JeeslJobStatus;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.model.json.db.tuple.t1.Json1Tuples;
import org.jeesl.model.json.db.tuple.two.Json2Tuples;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslIoSsiFacadeBean<L extends JeeslLang,D extends JeeslDescription,
									SYSTEM extends JeeslIoSsiSystem<L,D>,
									CRED extends JeeslIoSsiCredential<SYSTEM>,
									MAPPING extends JeeslIoSsiMapping<SYSTEM,ENTITY>,
									ATTRIBUTE extends JeeslIoSsiAttribute<MAPPING,ENTITY>,
									DATA extends JeeslIoSsiData<MAPPING,LINK,JOB>,
									LINK extends JeeslIoSsiLink<L,D,LINK,?>,
									ENTITY extends JeeslRevisionEntity<?,?,?,?,?,?>,
									CLEANING extends JeeslIoSsiCleaning<L,D,CLEANING,?>,
									JOB extends JeeslJobStatus<L,D,JOB,?>,
									HOST extends JeeslIoSsiHost<L,D,SYSTEM>>
					extends JeeslFacadeBean
					implements JeeslIoSsiFacade<L,D,SYSTEM,CRED,MAPPING,ATTRIBUTE,DATA,LINK,ENTITY,CLEANING,JOB,HOST>
{	
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslIoSsiFacadeBean.class);
		
	private final IoSsiDataFactoryBuilder<L,D,SYSTEM,MAPPING,ATTRIBUTE,DATA,LINK,ENTITY,CLEANING,JOB> fbSsi;
	
	public JeeslIoSsiFacadeBean(EntityManager em,
								IoSsiDataFactoryBuilder<L,D,SYSTEM,MAPPING,ATTRIBUTE,DATA,LINK,ENTITY,CLEANING,JOB> fbSsi)
	{
		super(em);
		this.fbSsi = fbSsi;
	}
	
	@Override
	public MAPPING fMapping(Class<?> json, Class<?> ejb) throws JeeslNotFoundException
	{
		ENTITY eEjb = this.fByCode(fbSsi.getClassEntity(), ejb.getName());
		ENTITY eJson = this.fByCode(fbSsi.getClassEntity(), json.getName());
		return this.oneForParents(fbSsi.getClassMapping(), JeeslIoSsiMapping.Attributes.entity.toString(), eEjb, JeeslIoSsiMapping.Attributes.json.toString(), eJson);
	}

	@Override public List<DATA> fIoSsiData(MAPPING mapping, List<LINK> links){return fIoSsiData(mapping,links,null,null,null);}
	@Override public <A extends EjbWithId> List<DATA> fIoSsiData(MAPPING mapping, List<LINK> links, A a){return fIoSsiData(mapping,links,a,null,null);}
	@Override public <A extends EjbWithId, B extends EjbWithId> List<DATA> fIoSsiData(MAPPING mapping, List<LINK> links, A a, B b, Integer maxResults)
	{
		if(links!=null && links.isEmpty()) {return new ArrayList<DATA>();}
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<DATA> cQ = cB.createQuery(fbSsi.getClassData());
		Root<DATA> data = cQ.from(fbSsi.getClassData());
		
		Path<MAPPING> pMapping = data.get(JeeslIoSsiData.Attributes.mapping.toString());
		predicates.add(cB.equal(pMapping,mapping));
		
		if(links!=null)
		{
			Join<DATA,LINK> jLink = data.join(JeeslIoSsiData.Attributes.link.toString());
			predicates.add(jLink.in(links));
		}
		
		Path<Long> pA = data.get(JeeslIoSsiData.Attributes.refA.toString());
		if(a==null) {predicates.add(cB.isNull(pA));}
		else {predicates.add(cB.equal(pA,a.getId()));}
		
		Path<Long> pB = data.get(JeeslIoSsiData.Attributes.refB.toString());
		if(b==null) {predicates.add(cB.isNull(pB));}
		else {predicates.add(cB.equal(pB,b.getId()));}
		
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.select(data);

		TypedQuery<DATA> tQ = em.createQuery(cQ);
		if(maxResults!=null) {tQ.setMaxResults(maxResults);}
		return tQ.getResultList();
	}

	@Override public <A extends EjbWithId> DATA fIoSsiData(MAPPING mapping, String code, A a) throws JeeslNotFoundException
	{
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<DATA> cQ = cB.createQuery(fbSsi.getClassData());
		Root<DATA> data = cQ.from(fbSsi.getClassData());
		
		Join<DATA,MAPPING> jMapping = data.join(JeeslIoSsiData.Attributes.mapping.toString());
		predicates.add(jMapping.in(mapping));
		
		Expression<String> eCode = data.get(JeeslIoSsiData.Attributes.code.toString());
		predicates.add(cB.equal(eCode,code));
		
		Expression<Long> eRefA = data.get(JeeslIoSsiData.Attributes.refA.toString());
		predicates.add(cB.equal(eRefA,a.getId()));

		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.select(data);

		TypedQuery<DATA> tQ = em.createQuery(cQ);
		
		try	{return tQ.getSingleResult();}
		catch (NoResultException ex){throw new JeeslNotFoundException("Nothing found "+fbSsi.getClassData().getSimpleName()+" for "+mapping.toString()+" for code="+code);}
		catch (NonUniqueResultException ex){throw new JeeslNotFoundException("Results for "+fbSsi.getClassData().getSimpleName()+" and code="+code+" not unique");}
	}
	@Override public DATA fIoSsiData(MAPPING mapping, String code) throws JeeslNotFoundException
	{
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<DATA> cQ = cB.createQuery(fbSsi.getClassData());
		Root<DATA> data = cQ.from(fbSsi.getClassData());
		
		Join<DATA,MAPPING> jMapping = data.join(JeeslIoSsiData.Attributes.mapping.toString());
		predicates.add(jMapping.in(mapping));
		
		Expression<String> eCode = data.get(JeeslIoSsiData.Attributes.code.toString());
		predicates.add(cB.equal(eCode,code));

		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.select(data);

		TypedQuery<DATA> tQ = em.createQuery(cQ);
		
		try	{return tQ.getSingleResult();}
		catch (NoResultException ex){throw new JeeslNotFoundException("Nothing found "+fbSsi.getClassData().getSimpleName()+" for "+mapping.toString()+" for code="+code);}
		catch (NonUniqueResultException ex){throw new JeeslNotFoundException("Results for "+fbSsi.getClassData().getSimpleName()+" and code="+code+" not unique");}
	}
	
	@Override
	public <T extends EjbWithId> DATA fIoSsiData(MAPPING mapping, T ejb) throws JeeslNotFoundException
	{
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<DATA> cQ = cB.createQuery(fbSsi.getClassData());
		Root<DATA> data = cQ.from(fbSsi.getClassData());
		
		Join<DATA,MAPPING> jMapping = data.join(JeeslIoSsiData.Attributes.mapping.toString());
		predicates.add(jMapping.in(mapping));
		
		Expression<Long> eId = data.get(JeeslIoSsiData.Attributes.localId.toString());
		predicates.add(cB.equal(eId,ejb.getId()));

		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.select(data);

		TypedQuery<DATA> tQ = em.createQuery(cQ);
		
		try	{return tQ.getSingleResult();}
		catch (NoResultException ex){throw new JeeslNotFoundException("Nothing found "+fbSsi.getClassData().getSimpleName()+" for "+mapping.toString()+" for id="+ejb.getId());}
		catch (NonUniqueResultException ex){throw new JeeslNotFoundException("Results for "+fbSsi.getClassData().getSimpleName()+" and id="+ejb.getId()+" not unique");}
	}
	
	@Override public Json1Tuples<LINK> tpIoSsiLinkForMapping(MAPPING mapping){return tpIoSsiLinkForMapping(mapping,null,null);}
	@Override public <A extends EjbWithId> Json1Tuples<LINK> tpIoSsiLinkForMapping(MAPPING mapping, A a){return tpIoSsiLinkForMapping(mapping,a,null);}
	@Override public <A extends EjbWithId, B extends EjbWithId> Json1Tuples<LINK> tpIoSsiLinkForMapping(MAPPING mapping, A a, B b)
	{
		Json1TuplesFactory<LINK> jtf = new Json1TuplesFactory<LINK>(this,fbSsi.getClassLink());
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cQ = cB.createTupleQuery();
		Root<DATA> data = cQ.from(fbSsi.getClassData());
		
		Expression<Long> cCount = cB.count(data);
		
		Path<LINK> pLink = data.get(JeeslIoSsiData.Attributes.link.toString());
		Join<DATA,MAPPING> jMapping = data.join(JeeslIoSsiData.Attributes.mapping.toString());
		predicates.add(jMapping.in(mapping));
		
		Path<Long> pA = data.get(JeeslIoSsiData.Attributes.refA.toString());
		if(a==null) {predicates.add(cB.isNull(pA));}
		else {predicates.add(cB.equal(pA,a.getId()));}
		
		Path<Long> pB = data.get(JeeslIoSsiData.Attributes.refB.toString());
		if(b==null) {predicates.add(cB.isNull(pB));}
		else {predicates.add(cB.equal(pB,b.getId()));}
		
		cQ.groupBy(pLink.get("id"));
		cQ.multiselect(pLink.get("id"),cCount);
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
	       
		TypedQuery<Tuple> tQ = em.createQuery(cQ);
        return jtf.buildCount(tQ.getResultList());
	}
	
	@Override
	public <A extends EjbWithId, B extends EjbWithId> Json2Tuples<LINK, JOB> tpcIoSsiLinkJobForMapping(MAPPING mapping, A a, B b)
	{
		Json2TuplesFactory<LINK,JOB> jtf = new Json2TuplesFactory<>(this,fbSsi.getClassLink(),fbSsi.getClassJob());
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cQ = cB.createTupleQuery();
		Root<DATA> data = cQ.from(fbSsi.getClassData());
		
		Expression<Long> cCount = cB.count(data);
		
		Path<LINK> pLink = data.get(JeeslIoSsiData.Attributes.link.toString());
		Path<LINK> pJob = data.get(JeeslIoSsiData.Attributes.job1.toString());
		Join<DATA,MAPPING> jMapping = data.join(JeeslIoSsiData.Attributes.mapping.toString());
		predicates.add(jMapping.in(mapping));
		
		Path<Long> pA = data.get(JeeslIoSsiData.Attributes.refA.toString());
		if(a==null) {predicates.add(cB.isNull(pA));}
		else {predicates.add(cB.equal(pA,a.getId()));}
		
		Path<Long> pB = data.get(JeeslIoSsiData.Attributes.refB.toString());
		if(b==null) {predicates.add(cB.isNull(pB));}
		else {predicates.add(cB.equal(pB,b.getId()));}
		
		cQ.groupBy(pLink.get("id"),pJob.get("id"));
		cQ.multiselect(pLink.get("id"),pJob.get("id"),cCount);
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
	       
		TypedQuery<Tuple> tQ = em.createQuery(cQ);
        return jtf.build(tQ.getResultList(),JsonTupleFactory.Type.count);
	}
	
	@Override public Json1Tuples<MAPPING> tpMapping()
	{
		Json1TuplesFactory<MAPPING> jtf = new Json1TuplesFactory<>(this,fbSsi.getClassMapping());
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cQ = cB.createTupleQuery();
		Root<DATA> item = cQ.from(fbSsi.getClassData());
		
		Expression<Long> eCount = cB.count(item.<Long>get("id"));
		Join<DATA,MAPPING> jData = item.join(JeeslIoSsiData.Attributes.mapping.toString());
		
		cQ.groupBy(jData.get("id"));
		cQ.multiselect(jData.get("id"),eCount);
	       
		TypedQuery<Tuple> tQ = em.createQuery(cQ);
        return jtf.buildCount(tQ.getResultList());
	}
	
	@Override public Json2Tuples<MAPPING,LINK> tpMappingLink(List<MAPPING> list)
	{
		Json2TuplesFactory<MAPPING,LINK> jtf = new Json2TuplesFactory<>(this,fbSsi.getClassMapping(),fbSsi.getClassLink());
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cQ = cB.createTupleQuery();
		Root<DATA> item = cQ.from(fbSsi.getClassData());
		
		Expression<Long> eCount = cB.count(item.<Long>get("id"));
		Join<DATA,MAPPING> jMapping = item.join(JeeslIoSsiData.Attributes.mapping.toString());
		Join<DATA,LINK> jLink = item.join(JeeslIoSsiData.Attributes.link.toString());
		
		List<Predicate> predicates = new ArrayList<Predicate>();
		predicates.add(jMapping.in(list));
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		
		cQ.groupBy(jMapping.get("id"),jLink.get("id"));
		cQ.multiselect(jMapping.get("id"),jLink.get("id"),eCount);
	       
		TypedQuery<Tuple> tQ = em.createQuery(cQ);
        return jtf.build(tQ.getResultList(),JsonTupleFactory.Type.count);
	}
	
	@Override public <A extends EjbWithId, B extends EjbWithId> Json2Tuples<LINK,B> tpMappingB(Class<B> classB, MAPPING mapping, A a)
	{
		List<Predicate> predicates = new ArrayList<Predicate>();
		Json2TuplesFactory<LINK,B> jtf = new Json2TuplesFactory<>(this,fbSsi.getClassLink(),classB);
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cQ = cB.createTupleQuery();
		Root<DATA> item = cQ.from(fbSsi.getClassData());
		
		Expression<Long> eA = item.get(JeeslIoSsiData.Attributes.refA.toString());
		Expression<Long> pB = item.get(JeeslIoSsiData.Attributes.refB.toString());
		Expression<Long> eCount = cB.count(item.<Long>get("id"));
		
		predicates.add(cB.equal(eA,a.getId()));
		
		Join<DATA,MAPPING> jMapping = item.join(JeeslIoSsiData.Attributes.mapping.toString());
		predicates.add(jMapping.in(mapping));
		
		Join<DATA,LINK> jLink = item.join(JeeslIoSsiData.Attributes.link.toString());
	
		cQ.groupBy(jLink.get("id"),pB);
		cQ.multiselect(jLink.get("id"),pB,eCount);
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
	       
		TypedQuery<Tuple> tQ = em.createQuery(cQ);
        return jtf.build(tQ.getResultList(),JsonTupleFactory.Type.count);
	}

	@Override public <T extends EjbWithSsiDataCleaning<CLEANING>> List<T> fEntitiesWithoutSsiDataCleaning(Class<T> c, int maxResult)
	{
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<T> cQ = cB.createQuery(c);
		Root<T> item = cQ.from(c);
		
		Expression<CLEANING> eMigration = item.get(EjbWithSsiDataCleaning.Attributes.cleaning.toString());
		predicates.add(cB.isNull(eMigration));

		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.select(item);

		TypedQuery<T> tQ = em.createQuery(cQ);
		tQ.setMaxResults(maxResult);
		return tQ.getResultList();
	}
	@Override public <T extends EjbWithSsiDataCleaning<CLEANING>> Json1Tuples<CLEANING> tpcSsiDataCleaning(Class<T> c)
	{
		Json1TuplesFactory<CLEANING> jtf = new Json1TuplesFactory<CLEANING>(fbSsi.getClassCleaning());
		jtf.setfUtils(this);
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cQ = cB.createTupleQuery();
		Root<T> item = cQ.from(c);
		
		Expression<Long> eCount = cB.count(item.<Long>get("id"));
		Path<CLEANING> pCleaning = item.get(EjbWithSsiDataCleaning.Attributes.cleaning.toString());
		
		cQ.groupBy(pCleaning.get("id"));
		cQ.multiselect(pCleaning.get("id"),eCount);
	       
		TypedQuery<Tuple> tQ = em.createQuery(cQ);
        return jtf.buildCount(tQ.getResultList());
	}

	@Override public List<DATA> fSsiDataWithPendingJob1(MAPPING mapping, LINK link, int maxResult, boolean includeNull, Long refA, Long refB, Long refC)
	{
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<DATA> cQ = cB.createQuery(fbSsi.getClassData());
		Root<DATA> item = cQ.from(fbSsi.getClassData());
		
		predicates.add(cB.equal(item.get(JeeslIoSsiData.Attributes.mapping.toString()),mapping));
		
		Expression<JOB> eJob = item.get(EjbWithMigrationJob1.Attributes.job1.toString());
		JOB queue = this.fByEnum(fbSsi.getClassJob(),JeeslJobStatus.Code.queue);
		if(includeNull) {predicates.add(cB.or(cB.isNull(eJob),cB.equal(eJob, queue)));}
		else {predicates.add(cB.equal(eJob,queue));}
		
		if(link!=null)
		{
			Expression<LINK> eLink = item.get(JeeslIoSsiData.Attributes.link.toString());
			predicates.add(cB.equal(eLink, link));
		}
		
		if(refA!=null) {predicates.add(cB.equal(item.get(JeeslIoSsiData.Attributes.refA.toString()),refA));}
		if(refB!=null) {predicates.add(cB.equal(item.get(JeeslIoSsiData.Attributes.refB.toString()),refB));}
		if(refC!=null) {predicates.add(cB.equal(item.get(JeeslIoSsiData.Attributes.refC.toString()),refC));}
		

		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.select(item);

		TypedQuery<DATA> tQ = em.createQuery(cQ);
		tQ.setMaxResults(maxResult);
		return tQ.getResultList();
	}

	
}