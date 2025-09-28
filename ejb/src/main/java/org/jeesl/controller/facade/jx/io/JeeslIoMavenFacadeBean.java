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
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.jeesl.api.facade.io.JeeslIoMavenFacade;
import org.jeesl.controller.facade.jx.JeeslFacadeBean;
import org.jeesl.controller.facade.jx.predicate.BooleanPredicateBuilder;
import org.jeesl.controller.facade.jx.predicate.LiteralPredicateBuilder;
import org.jeesl.controller.facade.jx.predicate.SortByPredicateBuilder;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.io.IoMavenFactoryBuilder;
import org.jeesl.factory.json.system.io.db.tuple.t1.Json1TuplesFactory;
import org.jeesl.interfaces.model.io.maven.classification.JeeslMavenMaintainer;
import org.jeesl.interfaces.model.io.maven.classification.JeeslMavenOutdate;
import org.jeesl.interfaces.model.io.maven.classification.JeeslMavenStructure;
import org.jeesl.interfaces.model.io.maven.dependency.JeeslIoMavenArtifact;
import org.jeesl.interfaces.model.io.maven.dependency.JeeslIoMavenDependency;
import org.jeesl.interfaces.model.io.maven.dependency.JeeslIoMavenGroup;
import org.jeesl.interfaces.model.io.maven.dependency.JeeslIoMavenVersion;
import org.jeesl.interfaces.model.io.maven.dependency.JeeslMavenScope;
import org.jeesl.interfaces.model.io.maven.ee.JeeslIoMavenEeReferral;
import org.jeesl.interfaces.model.io.maven.module.JeeslMavenType;
import org.jeesl.interfaces.model.io.maven.usage.JeeslIoMavenModule;
import org.jeesl.interfaces.model.io.maven.usage.JeeslIoMavenUsage;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.util.query.io.JeeslIoMavenQuery;
import org.jeesl.model.ejb.io.db.JeeslCq;
import org.jeesl.model.ejb.io.db.JeeslCqBoolean;
import org.jeesl.model.ejb.io.db.JeeslCqLiteral;
import org.jeesl.model.ejb.io.db.JeeslCqOrdering;
import org.jeesl.model.json.io.db.tuple.container.JsonTuples1;
import org.jeesl.util.query.cq.CqBool;
import org.jeesl.util.query.cq.CqLiteral;
import org.jeesl.util.query.cq.CqOrdering;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslIoMavenFacadeBean <L extends JeeslLang,D extends JeeslDescription,
									GROUP extends JeeslIoMavenGroup,
									ARTIFACT extends JeeslIoMavenArtifact<GROUP,?>,
									VERSION extends JeeslIoMavenVersion<ARTIFACT,OUTDATE,MAINTAINER>,
									DEPENDENCY extends JeeslIoMavenDependency<VERSION>,
									SCOPE extends JeeslMavenScope<?,?,SCOPE,?>,
									OUTDATE extends JeeslMavenOutdate<?,?,OUTDATE,?>,
									MAINTAINER extends JeeslMavenMaintainer<?,?,MAINTAINER,?>,
									MODULE extends JeeslIoMavenModule<MODULE,STRUCTURE,TYPE,?,?>,
									STRUCTURE extends JeeslMavenStructure<?,?,STRUCTURE,?>,
									TYPE extends JeeslMavenType<L,D,TYPE,?>,
									USAGE extends JeeslIoMavenUsage<VERSION,SCOPE,MODULE>,
									EER extends JeeslIoMavenEeReferral<VERSION,?,?>>
	extends JeeslFacadeBean implements JeeslIoMavenFacade<GROUP,ARTIFACT,VERSION,DEPENDENCY,SCOPE,OUTDATE,MAINTAINER,MODULE,STRUCTURE,TYPE,USAGE,EER>
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslIoMavenFacadeBean.class);
		
	private final IoMavenFactoryBuilder<L,D,GROUP,ARTIFACT,VERSION,DEPENDENCY,SCOPE,MODULE,USAGE,EER> fbMaven;
	
	public JeeslIoMavenFacadeBean(EntityManager em, final IoMavenFactoryBuilder<L,D,GROUP,ARTIFACT,VERSION,DEPENDENCY,SCOPE,MODULE,USAGE,EER> fbMaven)
	{
		super(em);
		this.fbMaven=fbMaven;
	}

	@Override public ARTIFACT fIoMavenArtifact(GROUP group, String code) throws JeeslNotFoundException
	{
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<ARTIFACT> cQ = cB.createQuery(fbMaven.getClassArtifact());
		Root<ARTIFACT> root = cQ.from(fbMaven.getClassArtifact());
		
		Path<GROUP> pGroup = root.get(JeeslIoMavenArtifact.Attributes.group.toString());
		Path<String> eCode = root.get(JeeslIoMavenArtifact.Attributes.code.toString());
		predicates.add(cB.equal(pGroup,group));
		predicates.add(cB.equal(eCode,code.toString()));
		
		cQ.select(root);
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		
		TypedQuery<ARTIFACT> tQ = em.createQuery(cQ);
		try	{return tQ.getSingleResult();}
		catch (NoResultException ex){throw new JeeslNotFoundException("No "+fbMaven.getClassArtifact().getSimpleName()+" found for group="+group.getCode()+" and code="+code);}
		catch (NonUniqueResultException ex){throw new JeeslNotFoundException("Results for "+fbMaven.getClassArtifact().getSimpleName()+" not unique for group:"+group.getCode()+" and code="+code);}
	}

	@Override public VERSION fIoMavenVersion(ARTIFACT artifact, String code) throws JeeslNotFoundException
	{
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<VERSION> cQ = cB.createQuery(fbMaven.getClassVersion());
		Root<VERSION> root = cQ.from(fbMaven.getClassVersion());
		
		Path<ARTIFACT> pArtifact = root.get(JeeslIoMavenVersion.Attributes.artifact.toString());
		Path<String> eCode = root.get(JeeslIoMavenVersion.Attributes.code.toString());
		predicates.add(cB.equal(pArtifact,artifact));
		predicates.add(cB.equal(eCode,code.toString()));
		
		cQ.select(root);
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		
		TypedQuery<VERSION> tQ = em.createQuery(cQ);
		try	{return tQ.getSingleResult();}
		catch (NoResultException ex){throw new JeeslNotFoundException("No "+fbMaven.getClassVersion().getSimpleName()+" found for artifact="+artifact.getCode()+" and code="+code);}
		catch (NonUniqueResultException ex){throw new JeeslNotFoundException("Results for "+fbMaven.getClassVersion().getSimpleName()+" not unique for artifact:"+artifact.getCode()+" and code="+code);}
	}
	
	@Override public List<ARTIFACT> fIoMavenArtifacts(JeeslIoMavenQuery<ARTIFACT,VERSION,MODULE,STRUCTURE,TYPE> query)
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<ARTIFACT> cQ = cB.createQuery(fbMaven.getClassArtifact());
		Root<ARTIFACT> root = cQ.from(fbMaven.getClassArtifact());
		if(ObjectUtils.isNotEmpty(query.getRootFetches())) {for(String fetch : query.getRootFetches()) {root.fetch(fetch, JoinType.LEFT);}}
		
		cQ.select(root).distinct(query.isDistinct());
//		cQ.where(cB.and(this.pReferrals(cB,query,root)));
		
		TypedQuery<ARTIFACT> tQ = em.createQuery(cQ);
		super.pagination(tQ, query);
		return tQ.getResultList();
	}
	
	@Override public List<MODULE> fIoMavenModules(JeeslIoMavenQuery<ARTIFACT,VERSION,MODULE,STRUCTURE,TYPE> query)
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<MODULE> cQ = cB.createQuery(fbMaven.getClassModule());
		Root<MODULE> root = cQ.from(fbMaven.getClassModule());
		if(ObjectUtils.isNotEmpty(query.getRootFetches())) {for(String fetch : query.getRootFetches()) {root.fetch(fetch, JoinType.LEFT);}}

		cQ.select(root);
		cQ.where(cB.and(pModules(cB,query,root)));
		cQ.distinct(query.isDistinct());
		
		TypedQuery<MODULE> tQ = em.createQuery(cQ);
		return tQ.getResultList();
	}

	@Override public List<USAGE> fIoMavenUsages(JeeslIoMavenQuery<ARTIFACT,VERSION,MODULE,STRUCTURE,TYPE> query)
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<USAGE> cQ = cB.createQuery(fbMaven.getClassUsage());
		Root<USAGE> root = cQ.from(fbMaven.getClassUsage());
		if(ObjectUtils.isNotEmpty(query.getRootFetches())) {for(String fetch : query.getRootFetches()) {root.fetch(fetch, JoinType.LEFT);}}

		cQ.select(root);
		cQ.where(cB.and(pUsages(cB,query,root)));
		
		TypedQuery<USAGE> tQ = em.createQuery(cQ);
		return tQ.getResultList();
	}

	@Override public List<DEPENDENCY> fIoMavenDependencies(JeeslIoMavenQuery<ARTIFACT,VERSION,MODULE,STRUCTURE,TYPE> query)
	{
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<DEPENDENCY> cQ = cB.createQuery(fbMaven.getClassDependency());
		Root<DEPENDENCY> root = cQ.from(fbMaven.getClassDependency());
		if(ObjectUtils.isNotEmpty(query.getRootFetches())) {for(String fetch : query.getRootFetches()) {root.fetch(fetch, JoinType.LEFT);}}
		
		if(ObjectUtils.isNotEmpty(query.getIoMavenVersions()))
		{
			Path<VERSION> pArtifact = root.get(JeeslIoMavenDependency.Attributes.artifact.toString());
			predicates.add(pArtifact.in(query.getIoMavenVersions()));
		}

		cQ.select(root);
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		
		TypedQuery<DEPENDENCY> tQ = em.createQuery(cQ);
		super.pagination(tQ,query);
		return tQ.getResultList();
	}
	
	@Override public List<EER> fIoMavenEeReferrals(JeeslIoMavenQuery<ARTIFACT,VERSION,MODULE,STRUCTURE,TYPE> query)
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<EER> cQ = cB.createQuery(fbMaven.getClassEeReferral());
		Root<EER> root = cQ.from(fbMaven.getClassEeReferral());
		if(ObjectUtils.isNotEmpty(query.getRootFetches())) {for(String fetch : query.getRootFetches()) {root.fetch(fetch, JoinType.LEFT);}}
		
		cQ.select(root).distinct(query.isDistinct());
		cQ.where(cB.and(this.pReferrals(cB,query,root)));
		
		TypedQuery<EER> tQ = em.createQuery(cQ);
		if(Objects.nonNull(query.getFirstResult())) {tQ.setFirstResult(query.getFirstResult());}
		if(Objects.nonNull(query.getMaxResults())) {tQ.setMaxResults(query.getMaxResults());}
		return tQ.getResultList();
	}
	public Predicate[] pReferrals(CriteriaBuilder cB, JeeslIoMavenQuery<ARTIFACT,VERSION,MODULE,STRUCTURE,TYPE> query, Root<EER> root)
	{
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		if(ObjectUtils.isNotEmpty(query.getIoMavenArtifacts()))
		{
			Join<EER,VERSION> jVersion = root.join(JeeslIoMavenEeReferral.Attributes.artifact.toString());
			Join<VERSION,ARTIFACT> jArtifact = jVersion.join(JeeslIoMavenEeReferral.Attributes.artifact.toString());
			predicates.add(jArtifact.in(query.getIoMavenArtifacts()));
		}
		
		if(ObjectUtils.isNotEmpty(query.getCqBooleans()))
		{
			for(JeeslCqBoolean cqb : query.getCqBooleans())
			{
				if(cqb.getPath().equals(CqBool.path(JeeslIoMavenEeReferral.Attributes.recommendation)))
				{
					Expression<Boolean> eBool = root.get(JeeslIoMavenEeReferral.Attributes.recommendation.toString());
					BooleanPredicateBuilder.add(cB,predicates,cqb,eBool);
				}
				else {logger.warn("NYI Path: "+cqb.toString());}
			}
		}
		return predicates.toArray(new Predicate[predicates.size()]);
	}

	@Override public JsonTuples1<VERSION> tpUsageByVersion(JeeslIoMavenQuery<ARTIFACT,VERSION,MODULE,STRUCTURE,TYPE> query)
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cQ = cB.createTupleQuery();
		Root<USAGE> root = cQ.from(fbMaven.getClassUsage());
		
		Expression<Long> eCount = cB.count(root.<Long>get("id"));
		Path<VERSION> pVersion = root.join(JeeslIoMavenUsage.Attributes.version.toString());
		
		cQ.multiselect(pVersion.get("id"),eCount);
		cQ.groupBy(pVersion.get("id"));
	       
		TypedQuery<Tuple> tQ = em.createQuery(cQ);
		Json1TuplesFactory<VERSION> jtf = Json1TuplesFactory.instance(fbMaven.getClassVersion()).tupleLoad(this,query.getTupleLoad());
		return jtf.buildV2(tQ.getResultList(),JeeslCq.Agg.count);
	}

	@Override public Long cIoMavenVersions(JeeslIoMavenQuery<ARTIFACT,VERSION,MODULE,STRUCTURE,TYPE> query)
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<Long> cQ = cB.createQuery(Long.class);
		Root<VERSION> root = cQ.from(fbMaven.getClassVersion());

		cQ.select(cB.count(root));
		cQ.where(cB.and(this.pVersions(cB,query,root)));
		cQ.distinct(query.isDistinct());

		return em.createQuery(cQ).getSingleResult();
	}
	@Override public List<VERSION> fIoMavenVersions(JeeslIoMavenQuery<ARTIFACT,VERSION,MODULE,STRUCTURE,TYPE> query)
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<VERSION> cQ = cB.createQuery(fbMaven.getClassVersion());
		Root<VERSION> root = cQ.from(fbMaven.getClassVersion());
		if(ObjectUtils.isNotEmpty(query.getRootFetches())) {for(String fetch : query.getRootFetches()) {root.fetch(fetch, JoinType.LEFT);}}

		cQ.select(root).distinct(query.isDistinct());
		cQ.where(cB.and(this.pVersions(cB,query,root)));
		this.sortBy(cB,cQ,query,root);
		
		TypedQuery<VERSION> tQ = em.createQuery(cQ);
		if(Objects.nonNull(query.getFirstResult())) {tQ.setFirstResult(query.getFirstResult());}
		if(Objects.nonNull(query.getMaxResults())) {tQ.setMaxResults(query.getMaxResults());}
		return tQ.getResultList();
	}
	
	public void sortBy(CriteriaBuilder cB, CriteriaQuery<VERSION> cQ, JeeslIoMavenQuery<ARTIFACT,VERSION,MODULE,STRUCTURE,TYPE> query, Root<VERSION> root)
	{
		if(ObjectUtils.isNotEmpty(query.getCqOrderings()))
		{
			List<Order> orders = new ArrayList<>();
			for(JeeslCqOrdering cqo : query.getCqOrderings())
			{
				if(cqo.getPath().equals(CqOrdering.path(JeeslIoMavenVersion.Attributes.position)))
				{
					Expression<Integer> ePosition = root.get(JeeslIoMavenVersion.Attributes.position.toString());
					SortByPredicateBuilder.addByInteger(cB,orders,cqo,ePosition);
				}
				else if(cqo.getPath().equals(CqOrdering.path(JeeslIoMavenVersion.Attributes.artifact,JeeslIoMavenArtifact.Attributes.code)))
				{
					Path<ARTIFACT> pArtifact = root.get(JeeslIoMavenVersion.Attributes.artifact.toString());
					Expression<String> eCode = pArtifact.get(JeeslIoMavenArtifact.Attributes.code.toString());
					SortByPredicateBuilder.addByString(cB,orders,cqo,eCode);
				}
				else if(cqo.getPath().equals(CqOrdering.path(JeeslIoMavenVersion.Attributes.artifact,JeeslIoMavenArtifact.Attributes.group,JeeslIoMavenGroup.Attributes.code)))
				{
					Path<ARTIFACT> pArtifact = root.get(JeeslIoMavenVersion.Attributes.artifact.toString());
 					Path<GROUP> pGroup = pArtifact.get(JeeslIoMavenArtifact.Attributes.group.toString());
 					Expression<String> eCode = cB.upper(pGroup.get(JeeslIoMavenGroup.Attributes.code.toString()));
					SortByPredicateBuilder.addByString(cB,orders,cqo,eCode);
				}
				else {logger.warn("No SortBy Handling for "+cqo.toString());}
			}
			if(!orders.isEmpty()) {cQ.orderBy(orders);}
		}
	}

	
	// Predicates
	private Predicate[] pModules(CriteriaBuilder cB, JeeslIoMavenQuery<ARTIFACT,VERSION,MODULE,STRUCTURE,TYPE> query, Root<MODULE> root)
	{
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		if(ObjectUtils.isNotEmpty(query.getIdList()))
		{
			Expression<Long> eId = root.get(JeeslIoMavenModule.Attributes.id.toString());
			predicates.add(eId.in(query.getIdList()));
		}
		if(ObjectUtils.isNotEmpty(query.getIoMavenTypes()))
		{
			Path<TYPE> pType = root.get(JeeslIoMavenModule.Attributes.type.toString());
			predicates.add(pType.in(query.getIoMavenTypes()));
		}
		
		return predicates.toArray(new Predicate[predicates.size()]);
	}
	
	public Predicate[] pVersions(CriteriaBuilder cB, JeeslIoMavenQuery<ARTIFACT,VERSION,MODULE,STRUCTURE,TYPE> query, Root<VERSION> ejb)
	{
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		for(JeeslCqLiteral cql : ListUtils.emptyIfNull(query.getCqLiterals()))
		{
			if(cql.getPath().equals(CqLiteral.path(JeeslIoMavenVersion.Attributes.code)))
			{
				Expression<String> eCode = cB.upper(ejb.get(JeeslIoMavenVersion.Attributes.code.toString()));
				LiteralPredicateBuilder.add(cB,predicates,cql,eCode);
			}
			else if(cql.getPath().equals(CqLiteral.path(JeeslIoMavenVersion.Attributes.artifact,JeeslIoMavenGroup.Attributes.code)))
			{
					Path<ARTIFACT> pArtifact = ejb.get(JeeslIoMavenVersion.Attributes.artifact.toString());
					Expression<String> eCode = cB.upper(pArtifact.get(JeeslIoMavenGroup.Attributes.code.toString()));
					LiteralPredicateBuilder.add(cB,predicates,cql,eCode);
			}
			else if(cql.getPath().equals(CqLiteral.path(JeeslIoMavenVersion.Attributes.artifact,JeeslIoMavenArtifact.Attributes.group,JeeslIoMavenGroup.Attributes.code)))
			{
					Path<ARTIFACT> pArtifact = ejb.get(JeeslIoMavenVersion.Attributes.artifact.toString());
					Path<GROUP> pGroup = pArtifact.get(JeeslIoMavenArtifact.Attributes.group.toString());
					Expression<String> eCode = cB.upper(pGroup.get(JeeslIoMavenGroup.Attributes.code.toString()));
					LiteralPredicateBuilder.add(cB,predicates,cql,eCode);
			}
			else {logger.warn("NYI Path: "+cql.toString());}
		}
		return predicates.toArray(new Predicate[predicates.size()]);
	}
	
	public Predicate[] pUsages(CriteriaBuilder cB, JeeslIoMavenQuery<ARTIFACT,VERSION,MODULE,STRUCTURE,TYPE> query, Root<USAGE> root)
	{
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		if(ObjectUtils.isNotEmpty(query.getIoMavenVersions()))
		{
			Path<VERSION> pVersion = root.get(JeeslIoMavenUsage.Attributes.version.toString());
			predicates.add(pVersion.in(query.getIoMavenVersions()));
		}
		if(ObjectUtils.isNotEmpty(query.getIoMavenModules()))
		{
			Path<MODULE> pModule = root.get(JeeslIoMavenUsage.Attributes.module.toString());
			predicates.add(pModule.in(query.getIoMavenModules()));
		}
		if(ObjectUtils.isNotEmpty(query.getIoMavenTypes()))
		{
			Join<USAGE,MODULE> jModule = root.join(JeeslIoMavenUsage.Attributes.module.toString());
			Path<TYPE> pType = jModule.get(JeeslIoMavenModule.Attributes.type.toString());
			predicates.add(pType.in(query.getIoMavenTypes()));
		}
		
		return predicates.toArray(new Predicate[predicates.size()]);
	}
}