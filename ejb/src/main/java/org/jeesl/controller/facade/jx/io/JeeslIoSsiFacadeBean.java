package org.jeesl.controller.facade.jx.io;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.jeesl.api.facade.io.JeeslIoSsiFacade;
import org.jeesl.controller.facade.jx.JeeslFacadeBean;
import org.jeesl.controller.facade.jx.predicate.LiteralPredicateBuilder;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.io.ssi.IoSsiCoreFactoryBuilder;
import org.jeesl.factory.builder.io.ssi.IoSsiDataFactoryBuilder;
import org.jeesl.factory.json.io.db.tuple.JsonTupleFactory;
import org.jeesl.factory.json.system.io.db.tuple.t1.Json1TuplesFactory;
import org.jeesl.factory.json.system.io.db.tuple.t2.Json2TuplesFactory;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiCredential;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiHost;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiSystem;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiAttribute;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiCleaning;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiContext;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiData;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiError;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiStatus;
import org.jeesl.interfaces.model.io.ssi.maintenance.EjbWithSsiDataCleaning;
import org.jeesl.interfaces.model.system.job.core.JeeslJobStatus;
import org.jeesl.interfaces.model.system.job.with.EjbWithMigrationJob1;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.util.query.io.JeeslIoSsiQuery;
import org.jeesl.model.ejb.io.db.JeeslCqLiteral;
import org.jeesl.model.ejb.io.db.JeeslCqLong;
import org.jeesl.model.json.io.db.tuple.container.JsonTuples1;
import org.jeesl.model.json.io.db.tuple.container.JsonTuples2;
import org.jeesl.util.query.cq.CqLiteral;
import org.jeesl.util.query.cq.CqLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslIoSsiFacadeBean<L extends JeeslLang,D extends JeeslDescription,
									SYSTEM extends JeeslIoSsiSystem<L,D>,
									CRED extends JeeslIoSsiCredential<SYSTEM>,
									CTX extends JeeslIoSsiContext<SYSTEM,ENTITY>,
									ATTRIBUTE extends JeeslIoSsiAttribute<CTX,ENTITY>,
									DATA extends JeeslIoSsiData<CTX,STATUS,ERROR,JOB>,
									STATUS extends JeeslIoSsiStatus<L,D,STATUS,?>,
									ERROR extends JeeslIoSsiError<L,D,CTX,?>,
									ENTITY extends JeeslRevisionEntity<?,?,?,?,?,?>,
									CLEANING extends JeeslIoSsiCleaning<L,D,CLEANING,?>,
									JOB extends JeeslJobStatus<L,D,JOB,?>,
									HOST extends JeeslIoSsiHost<L,D,SYSTEM>>
					extends JeeslFacadeBean
					implements JeeslIoSsiFacade<SYSTEM,CRED,CTX,ATTRIBUTE,DATA,STATUS,ERROR,ENTITY,CLEANING,JOB,HOST>
{	
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslIoSsiFacadeBean.class);

	private final IoSsiCoreFactoryBuilder<L,D,SYSTEM,CRED,HOST> fbSsiCore;
	private final IoSsiDataFactoryBuilder<L,D,SYSTEM,CTX,ATTRIBUTE,DATA,STATUS,ERROR,ENTITY,CLEANING,JOB> fbSsi;
	
	public JeeslIoSsiFacadeBean(EntityManager em,
								IoSsiCoreFactoryBuilder<L,D,SYSTEM,CRED,HOST> fbSsiCore,
								IoSsiDataFactoryBuilder<L,D,SYSTEM,CTX,ATTRIBUTE,DATA,STATUS,ERROR,ENTITY,CLEANING,JOB> fbSsi)
	{
		super(em);
		this.fbSsiCore = fbSsiCore;
		this.fbSsi = fbSsi;
	}
	
	@Override
	public HOST fSsiHost(SYSTEM system, String code) throws JeeslNotFoundException
	{
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<HOST> cQ = cB.createQuery(fbSsiCore.getClassHost());
		Root<HOST> data = cQ.from(fbSsiCore.getClassHost());
		
		Path<SYSTEM> pSystem = data.get(JeeslIoSsiCredential.Attributes.system.toString());
		Path<String> eCode = data.get(JeeslIoSsiCredential.Attributes.code.toString());
		predicates.add(cB.equal(pSystem,system));
		predicates.add(cB.equal(eCode,code.toString()));
		
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.select(data);

		TypedQuery<HOST> tQ = em.createQuery(cQ);
		try	{return tQ.getSingleResult();}
		catch (NoResultException ex){throw new JeeslNotFoundException("No "+fbSsiCore.getClassHost().getSimpleName()+" found for system"+system.toString()+" and code="+code);}
		catch (NonUniqueResultException ex){throw new JeeslNotFoundException("Results for "+fbSsiCore.getClassHost().getSimpleName()+" not unique for system"+system.toString()+" and code="+code);}
	}
	
	@Override
	public <E extends Enum<E>> CRED fSsiCredential(SYSTEM system, E code)
	{
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<CRED> cQ = cB.createQuery(fbSsiCore.getClassCredential());
		Root<CRED> data = cQ.from(fbSsiCore.getClassCredential());
		
		Path<SYSTEM> pSystem = data.get(JeeslIoSsiCredential.Attributes.system.toString());
		Path<String> eCode = data.get(JeeslIoSsiCredential.Attributes.code.toString());
		predicates.add(cB.equal(pSystem,system));
		predicates.add(cB.equal(eCode,code.toString()));
		
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.select(data);

		TypedQuery<CRED> tQ = em.createQuery(cQ);
		try	{return tQ.getSingleResult();}
		catch (NoResultException ex){logger.error("No "+fbSsiCore.getClassCredential().getSimpleName()+" found for system"+system.toString()+" and code="+code);}
		catch (NonUniqueResultException ex){logger.error("Results for "+fbSsiCore.getClassHost().getSimpleName()+" not unique for system"+system.toString()+" and code="+code);}
		return null;
	}
	
	@Override public CTX fMapping(Class<?> json, Class<?> ejb) throws JeeslNotFoundException
	{
		ENTITY eEjb = this.fByCode(fbSsi.getClassEntity(), ejb.getName());
		ENTITY eJson = this.fByCode(fbSsi.getClassEntity(), json.getName());
		return this.oneForParents(fbSsi.getClassMapping(), JeeslIoSsiContext.Attributes.entity,eEjb, JeeslIoSsiContext.Attributes.json,eJson);
	}

	@Override public List<DATA> fIoSsiData(CTX mapping, List<STATUS> links){return fIoSsiData(mapping,links,null,null,null);}
	@Override public <A extends EjbWithId> List<DATA> fIoSsiData(CTX mapping, List<STATUS> links, A a){return fIoSsiData(mapping,links,a,null,null);}
	@Override public <A extends EjbWithId, B extends EjbWithId> List<DATA> fIoSsiData(CTX mapping, List<STATUS> links, A a, B b, Integer maxResults)
	{
		if(links!=null && links.isEmpty()) {return new ArrayList<DATA>();}
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<DATA> cQ = cB.createQuery(fbSsi.getClassData());
		Root<DATA> data = cQ.from(fbSsi.getClassData());
		
		Path<CTX> pMapping = data.get(JeeslIoSsiData.Attributes.mapping.toString());
		predicates.add(cB.equal(pMapping,mapping));
		
		if(links!=null)
		{
			Join<DATA,STATUS> jLink = data.join(JeeslIoSsiData.Attributes.link.toString());
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

	@Override public <A extends EjbWithId> DATA fIoSsiData(CTX mapping, String code, A a) throws JeeslNotFoundException
	{
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<DATA> cQ = cB.createQuery(fbSsi.getClassData());
		Root<DATA> data = cQ.from(fbSsi.getClassData());
		
		Join<DATA,CTX> jContext = data.join(JeeslIoSsiData.Attributes.mapping.toString());
		predicates.add(jContext.in(mapping));
		
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
	
	@Override public <A extends EjbWithId, B extends EjbWithId> DATA fIoSsiData(CTX context, String code, A a, B b) throws JeeslNotFoundException
	{
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<DATA> cQ = cB.createQuery(fbSsi.getClassData());
		Root<DATA> data = cQ.from(fbSsi.getClassData());
		
		Path<CTX> pContext = data.get(JeeslIoSsiData.Attributes.mapping.toString());
		predicates.add(pContext.in(context));
		
		Expression<String> eCode = data.get(JeeslIoSsiData.Attributes.code.toString());
		predicates.add(cB.equal(eCode,code));
		
		Expression<Long> eRefA = data.get(JeeslIoSsiData.Attributes.refA.toString());
		predicates.add(cB.equal(eRefA,a.getId()));
		
		Expression<Long> eRefB = data.get(JeeslIoSsiData.Attributes.refB.toString());
		predicates.add(cB.equal(eRefB,b.getId()));

		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.select(data);

		TypedQuery<DATA> tQ = em.createQuery(cQ);
		try	{return tQ.getSingleResult();}
		catch (NoResultException ex){throw new JeeslNotFoundException("Nothing found "+fbSsi.getClassData().getSimpleName()+" for "+context.toString()+" for code="+code);}
		catch (NonUniqueResultException ex){throw new JeeslNotFoundException("Results for "+fbSsi.getClassData().getSimpleName()+" and code="+code+" not unique");}
	}
	
	@Override public DATA fIoSsiData(CTX mapping, String code) throws JeeslNotFoundException
	{
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<DATA> cQ = cB.createQuery(fbSsi.getClassData());
		Root<DATA> data = cQ.from(fbSsi.getClassData());
		
		Join<DATA,CTX> jMapping = data.join(JeeslIoSsiData.Attributes.mapping.toString());
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
	public <T extends EjbWithId> DATA fIoSsiData(CTX mapping, T ejb) throws JeeslNotFoundException
	{
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<DATA> cQ = cB.createQuery(fbSsi.getClassData());
		Root<DATA> data = cQ.from(fbSsi.getClassData());
		
		Join<DATA,CTX> jMapping = data.join(JeeslIoSsiData.Attributes.mapping.toString());
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
	
//	@Override public JsonTuples1<STATUS> tpIoSsiLinkForMapping(CTX mapping){return tpcIoSsiStatusForContext(mapping,null,null);}
//	@Override public <A extends EjbWithId> JsonTuples1<STATUS> tpIoSsiLinkForMapping(CTX mapping, A a){return tpcIoSsiStatusForContext(mapping,a,null);}
//	@Override public <A extends EjbWithId, B extends EjbWithId> JsonTuples1<STATUS> tpcIoSsiStatusForContext(CTX mapping, A a, B b)
//	{
//		List<Predicate> predicates = new ArrayList<Predicate>();
//		CriteriaBuilder cB = em.getCriteriaBuilder();
//		CriteriaQuery<Tuple> cQ = cB.createTupleQuery();
//		Root<DATA> data = cQ.from(fbSsi.getClassData());
//		
//		Expression<Long> cCount = cB.count(data);
//		
//		Path<STATUS> pLink = data.get(JeeslIoSsiData.Attributes.link.toString());
//		Join<DATA,CTX> jMapping = data.join(JeeslIoSsiData.Attributes.mapping.toString());
//		predicates.add(jMapping.in(mapping));
//		
//		Path<Long> pA = data.get(JeeslIoSsiData.Attributes.refA.toString());
//		if(a==null) {predicates.add(cB.isNull(pA));}
//		else {predicates.add(cB.equal(pA,a.getId()));}
//		
//		Path<Long> pB = data.get(JeeslIoSsiData.Attributes.refB.toString());
//		if(b==null) {predicates.add(cB.isNull(pB));}
//		else {predicates.add(cB.equal(pB,b.getId()));}
//		
//		cQ.groupBy(pLink.get("id"));
//		cQ.multiselect(pLink.get("id"),cCount);
//		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
//	       
//		TypedQuery<Tuple> tQ = em.createQuery(cQ);
//		Json1TuplesFactory<STATUS> jtf = Json1TuplesFactory.instance(fbSsi.getClassStatus()).tupleLoad(this,true);
//		return jtf.buildV2(tQ.getResultList(),JsonTupleFactory.Type.count);
//	}
	
	@Override public JsonTuples1<STATUS> tpIoSsiDataByStatus(JeeslIoSsiQuery<CTX,STATUS,ERROR> query)
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cQ = cB.createTupleQuery();
		Root<DATA> ejb = cQ.from(fbSsi.getClassData());
		
		Expression<Long> cCount = cB.count(ejb);
		Path<STATUS> pStatus = ejb.get(JeeslIoSsiData.Attributes.link.toString());
		
		cQ.multiselect(pStatus.get("id"),cCount);
		cQ.where(cB.and(pSsiData(cB,query,ejb)));
		cQ.groupBy(pStatus.get("id"));
		
		TypedQuery<Tuple> tQ = em.createQuery(cQ);
		Json1TuplesFactory<STATUS> jtf = Json1TuplesFactory.instance(fbSsi.getClassStatus()).tupleLoad(this,query.getTupleLoad());
		return jtf.buildV2(tQ.getResultList(),JsonTupleFactory.Type.count);
	}
	
	@Override public JsonTuples2<STATUS, ERROR> tpIoSsiDataByStatusError(JeeslIoSsiQuery<CTX,STATUS,ERROR> query)
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cQ = cB.createTupleQuery();
		Root<DATA> ejb = cQ.from(fbSsi.getClassData());
		
		Expression<Long> cCount = cB.count(ejb);
		Path<STATUS> pStatus = ejb.get(JeeslIoSsiData.Attributes.link.toString());
		Path<ERROR> pError = ejb.get(JeeslIoSsiData.Attributes.error.toString());
		
		cQ.multiselect(pStatus.get("id"),pError.get("id"),cCount);
		cQ.where(cB.and(pSsiData(cB,query,ejb)));
		cQ.groupBy(pStatus.get("id"),pError.get("id"));
		
		TypedQuery<Tuple> tQ = em.createQuery(cQ);
		Json2TuplesFactory<STATUS,ERROR> jtf = Json2TuplesFactory.instance(fbSsi.getClassStatus(),fbSsi.getClassError()).tupleLoad(this,query.getTupleLoad());
		return jtf.build(tQ.getResultList(),JsonTupleFactory.Type.count);
	}
	
	@Override public <A extends EjbWithId, B extends EjbWithId> JsonTuples1<ERROR> tpcIoSsiErrorContext(CTX context, A a, B b)
	{
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cQ = cB.createTupleQuery();
		Root<DATA> data = cQ.from(fbSsi.getClassData());
		
		Expression<Long> cCount = cB.count(data);
		
		Path<ERROR> pError = data.get(JeeslIoSsiData.Attributes.error.toString());
		Path<CTX> pContext = data.get(JeeslIoSsiData.Attributes.mapping.toString());
		predicates.add(pContext.in(context));
		
		if(Objects.nonNull(a))
		{
			Path<Long> pA = data.get(JeeslIoSsiData.Attributes.refA.toString());
			predicates.add(cB.equal(pA,a.getId()));
		}
		
		if(Objects.nonNull(b))
		{
			Path<Long> pB = data.get(JeeslIoSsiData.Attributes.refB.toString());
			predicates.add(cB.equal(pB,b.getId()));
		}
		
		cQ.groupBy(pError.get("id"));
		cQ.multiselect(pError.get("id"),cCount);
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
	       
		TypedQuery<Tuple> tQ = em.createQuery(cQ);
		Json1TuplesFactory<ERROR> jtf = Json1TuplesFactory.instance(fbSsi.getClassError()).tupleLoad(this,true);
		return jtf.buildV2(tQ.getResultList(),JsonTupleFactory.Type.count);
	}
	
	@Override
	public <A extends EjbWithId, B extends EjbWithId> JsonTuples2<STATUS,JOB> tpcIoSsiStatusJobForContext(CTX mapping, A a, B b)
	{
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cQ = cB.createTupleQuery();
		Root<DATA> data = cQ.from(fbSsi.getClassData());
		
		Expression<Long> cCount = cB.count(data);
		
		Path<STATUS> pLink = data.get(JeeslIoSsiData.Attributes.link.toString());
		Path<STATUS> pJob = data.get(JeeslIoSsiData.Attributes.job1.toString());
		Join<DATA,CTX> jMapping = data.join(JeeslIoSsiData.Attributes.mapping.toString());
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
		Json2TuplesFactory<STATUS,JOB> jtf = Json2TuplesFactory.instance(fbSsi.getClassStatus(),fbSsi.getClassJob()).tupleLoad(this,true);
        return jtf.build(tQ.getResultList(),JsonTupleFactory.Type.count);
	}
	
	@Override public JsonTuples1<CTX> tpMapping()
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cQ = cB.createTupleQuery();
		Root<DATA> item = cQ.from(fbSsi.getClassData());
		
		Expression<Long> eCount = cB.count(item.<Long>get("id"));
		Join<DATA,CTX> jData = item.join(JeeslIoSsiData.Attributes.mapping.toString());
		
		cQ.groupBy(jData.get("id"));
		cQ.multiselect(jData.get("id"),eCount);
	       
		TypedQuery<Tuple> tQ = em.createQuery(cQ);
		Json1TuplesFactory<CTX> jtf = Json1TuplesFactory.instance(fbSsi.getClassMapping()).tupleLoad(this,true);
		return jtf.buildV2(tQ.getResultList(),JsonTupleFactory.Type.count);
	}
	
	@Override public JsonTuples2<CTX,STATUS> tpcContextStatus(List<CTX> list)
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cQ = cB.createTupleQuery();
		Root<DATA> item = cQ.from(fbSsi.getClassData());
		
		Expression<Long> eCount = cB.count(item.<Long>get("id"));
		Path<CTX> pContext = item.get(JeeslIoSsiData.Attributes.mapping.toString());
		Path<STATUS> pStatus = item.get(JeeslIoSsiData.Attributes.link.toString());
		
		List<Predicate> predicates = new ArrayList<Predicate>();
		if(Objects.isNull(list) || list.size()==0) {}
		else if(list.size()==1) {predicates.add(cB.equal(pContext,list.get(0)));}
		else {predicates.add(pContext.in(list));}
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		
		cQ.groupBy(pContext.get("id"),pStatus.get("id"));
		cQ.multiselect(pContext.get("id"),pStatus.get("id"),eCount);
	       
		TypedQuery<Tuple> tQ = em.createQuery(cQ);
		Json2TuplesFactory<CTX,STATUS> jtf = Json2TuplesFactory.instance(fbSsi.getClassMapping(),fbSsi.getClassStatus()).tupleLoad(this,true);
        return jtf.build(tQ.getResultList(),JsonTupleFactory.Type.count);
	}
	
	@Override public <A extends EjbWithId, B extends EjbWithId> JsonTuples2<STATUS,B> tpMappingB(Class<B> classB, CTX mapping, A a)
	{
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cQ = cB.createTupleQuery();
		Root<DATA> item = cQ.from(fbSsi.getClassData());
		
		Expression<Long> eA = item.get(JeeslIoSsiData.Attributes.refA.toString());
		Expression<Long> pB = item.get(JeeslIoSsiData.Attributes.refB.toString());
		Expression<Long> eCount = cB.count(item.<Long>get("id"));
		
		predicates.add(cB.equal(eA,a.getId()));
		
		Join<DATA,CTX> jMapping = item.join(JeeslIoSsiData.Attributes.mapping.toString());
		predicates.add(jMapping.in(mapping));
		
		Join<DATA,STATUS> jLink = item.join(JeeslIoSsiData.Attributes.link.toString());
	
		cQ.groupBy(jLink.get("id"),pB);
		cQ.multiselect(jLink.get("id"),pB,eCount);
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
	       
		TypedQuery<Tuple> tQ = em.createQuery(cQ);
		Json2TuplesFactory<STATUS,B> jtf = Json2TuplesFactory.instance(fbSsi.getClassStatus(),classB).tupleLoad(this,true);
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
	@Override public <T extends EjbWithSsiDataCleaning<CLEANING>> JsonTuples1<CLEANING> tpcSsiDataCleaning(Class<T> c)
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
		return jtf.buildV2(tQ.getResultList(),JsonTupleFactory.Type.count);
	}

	@Override public List<DATA> fSsiDataWithJob1(CTX mapping, STATUS status, JOB job, int maxResult, boolean includeNull, Long refA, Long refB, Long refC)
	{
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<DATA> cQ = cB.createQuery(fbSsi.getClassData());
		Root<DATA> ejb = cQ.from(fbSsi.getClassData());
		
		predicates.add(cB.equal(ejb.get(JeeslIoSsiData.Attributes.mapping.toString()),mapping));
		
		Expression<JOB> eJob = ejb.get(EjbWithMigrationJob1.Tmp.job1.toString());
		
		if(includeNull) {predicates.add(cB.or(cB.isNull(eJob),cB.equal(eJob,job)));}
		else {predicates.add(cB.equal(eJob,job));}
		
		if(Objects.nonNull(status))
		{
			Expression<STATUS> eLink = ejb.get(JeeslIoSsiData.Attributes.link.toString());
			predicates.add(cB.equal(eLink, status));
		}
		
		if(Objects.nonNull(refA)) {predicates.add(cB.equal(ejb.get(JeeslIoSsiData.Attributes.refA.toString()),refA));}
		if(Objects.nonNull(refB)) {predicates.add(cB.equal(ejb.get(JeeslIoSsiData.Attributes.refB.toString()),refB));}
		if(Objects.nonNull(refC)) {predicates.add(cB.equal(ejb.get(JeeslIoSsiData.Attributes.refC.toString()),refC));}
		
		cQ.select(ejb);
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		

		TypedQuery<DATA> tQ = em.createQuery(cQ);
		tQ.setMaxResults(maxResult);
		return tQ.getResultList();
	}

	@Override public Long cSsiData(JeeslIoSsiQuery<CTX,STATUS,ERROR> query)
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<Long> cQ = cB.createQuery(Long.class);
		Root<DATA> ejb = cQ.from(fbSsi.getClassData());

		cQ.select(cB.count(ejb));
		cQ.where(cB.and(pSsiData(cB,query,ejb)));

		return em.createQuery(cQ).getSingleResult();
	}
	@Override public List<DATA> fSsiData(JeeslIoSsiQuery<CTX,STATUS,ERROR> query)
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<DATA> cQ = cB.createQuery(fbSsi.getClassData());
		Root<DATA> root = cQ.from(fbSsi.getClassData());
		
		cQ.select(root);
		cQ.where(cB.and(pSsiData(cB,query,root)));
		
		TypedQuery<DATA> tQ = em.createQuery(cQ);
		super.pagination(tQ,query);
		return tQ.getResultList();
	}
	private Predicate[] pSsiData(CriteriaBuilder cB, JeeslIoSsiQuery<CTX,STATUS,ERROR> query, Root<DATA> root)
	{
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		for(JeeslCqLiteral c : ListUtils.emptyIfNull(query.getCqLiterals()))
		{
			if(c.getPath().equals(CqLiteral.path(JeeslIoSsiData.Attributes.job1,JeeslJobStatus.Att.code)))
			{
				Path<JOB> pJob = root.get(JeeslIoSsiData.Attributes.job1.toString());
				Expression<String> e = pJob.get(JeeslJobStatus.Att.code.toString());
				LiteralPredicateBuilder.add(cB,predicates,c,e);
			}
			else {logger.warn("NYI Path: "+c.toString());}
		}
		for(JeeslCqLong cq : ListUtils.emptyIfNull(query.getCqLongs()))
		{
			Expression<Long> eId = null;
			if(cq.getPath().equals(CqLong.path(JeeslIoSsiData.Attributes.refA))) {eId = root.get(JeeslIoSsiData.Attributes.refA.toString());}
			else if(cq.getPath().equals(CqLong.path(JeeslIoSsiData.Attributes.refB))) {eId = root.get(JeeslIoSsiData.Attributes.refB.toString());}
			else if(cq.getPath().equals(CqLong.path(JeeslIoSsiData.Attributes.refC))) {eId = root.get(JeeslIoSsiData.Attributes.refC.toString());}
			if(Objects.isNull(eId)) {logger.error("NYI "+cq.toString());}
			else
			{
			
				switch(cq.getType())
				{
					case IsValue: predicates.add(cB.equal(eId,cq.getValue())); break;
					case IsNull: predicates.add(cB.isNull(eId)); break;
					default: logger.error("NYI "+cq.toString());
				}
			}
		}
		
		if(ObjectUtils.isNotEmpty(query.getIoSsiContexts())) {predicates.add(root.<CTX>get(JeeslIoSsiData.Attributes.mapping.toString()).in(query.getIoSsiContexts()));}
		if(ObjectUtils.isNotEmpty(query.getIoSsiStatus())) {predicates.add(root.<STATUS>get(JeeslIoSsiData.Attributes.link.toString()).in(query.getIoSsiStatus()));}
		if(ObjectUtils.isNotEmpty(query.getIoSsiErrors())){predicates.add(root.<ERROR>get(JeeslIoSsiData.Attributes.error.toString()).in(query.getIoSsiErrors()));}
		
		return predicates.toArray(new Predicate[predicates.size()]);
	}

	@Override
	public List<ATTRIBUTE> fSsiAttributes(JeeslIoSsiQuery<CTX, STATUS, ERROR> query)
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<ATTRIBUTE> cQ = cB.createQuery(fbSsi.getClassAttribute());
		Root<ATTRIBUTE> root = cQ.from(fbSsi.getClassAttribute());
		
		cQ.select(root);
		cQ.where(cB.and(this.pSsiAttribute(cB,query,root)));
		
		TypedQuery<ATTRIBUTE> tQ = em.createQuery(cQ);
		super.pagination(tQ,query);
		return tQ.getResultList();
	}
	private Predicate[] pSsiAttribute(CriteriaBuilder cB, JeeslIoSsiQuery<CTX,STATUS,ERROR> query, Root<ATTRIBUTE> root)
	{
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		if(ObjectUtils.isNotEmpty(query.getIoSsiContexts())) {predicates.add(root.<CTX>get(JeeslIoSsiAttribute.Attributes.mapping.toString()).in(query.getIoSsiContexts()));}
			
		return predicates.toArray(new Predicate[predicates.size()]);
	}
}