package org.jeesl.controller.facade.jx.module;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.jeesl.api.facade.module.JeeslAomFacade;
import org.jeesl.controller.facade.jx.JeeslFacadeBean;
import org.jeesl.controller.facade.jx.predicate.DatePredicateBuilder;
import org.jeesl.controller.facade.jx.predicate.SortByPredicateBuilder;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.module.AomFactoryBuilder;
import org.jeesl.factory.json.system.io.db.tuple.t1.Json1TuplesFactory;
import org.jeesl.interfaces.model.module.aom.asset.JeeslAomAsset;
import org.jeesl.interfaces.model.module.aom.asset.JeeslAomAssetStatus;
import org.jeesl.interfaces.model.module.aom.asset.JeeslAomAssetType;
import org.jeesl.interfaces.model.module.aom.asset.JeeslAomView;
import org.jeesl.interfaces.model.module.aom.company.JeeslAomCompany;
import org.jeesl.interfaces.model.module.aom.company.JeeslAomScope;
import org.jeesl.interfaces.model.module.aom.event.JeeslAomEvent;
import org.jeesl.interfaces.model.module.aom.event.JeeslAomEventStatus;
import org.jeesl.interfaces.model.module.aom.event.JeeslAomEventType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.util.query.module.JeeslAomQuery;
import org.jeesl.model.ejb.io.db.JeeslCq;
import org.jeesl.model.ejb.io.db.JeeslCqDate;
import org.jeesl.model.ejb.io.db.JeeslCqOrdering;
import org.jeesl.model.ejb.system.tenant.TenantIdentifier;
import org.jeesl.model.json.io.db.tuple.container.JsonTuples1;
import org.jeesl.util.query.cq.CqDate;
import org.jeesl.util.query.cq.CqOrdering;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslAssetFacadeBean<L extends JeeslLang, D extends JeeslDescription,
										REALM extends JeeslTenantRealm<L,D,REALM,?>,
										COMPANY extends JeeslAomCompany<REALM,SCOPE>,
										SCOPE extends JeeslAomScope<L,D,SCOPE,?>,
										ASSET extends JeeslAomAsset<REALM,ASSET,COMPANY,STATUS,ATYPE>,
										STATUS extends JeeslAomAssetStatus<L,D,STATUS,?>,
										ATYPE extends JeeslAomAssetType<L,D,REALM,ATYPE,VIEW,?>,
										VIEW extends JeeslAomView<L,D,REALM,?>,
										EVENT extends JeeslAomEvent<COMPANY,ASSET,ETYPE,ESTATUS,?,?,?>,
										ETYPE extends JeeslAomEventType<L,D,ETYPE,?>,
										ESTATUS extends JeeslAomEventStatus<L,D,ESTATUS,?>
										>
					extends JeeslFacadeBean
					implements JeeslAomFacade<L,D,REALM,COMPANY,SCOPE,ASSET,STATUS,ATYPE,VIEW,EVENT,ESTATUS>
{	
	private static final long serialVersionUID = 1L;

	final static Logger logger = LoggerFactory.getLogger(JeeslAssetFacadeBean.class);
	
	private final AomFactoryBuilder<L,D,REALM,COMPANY,SCOPE,ASSET,STATUS,ATYPE,VIEW,EVENT,ETYPE,ESTATUS,?,?,?,?,?> fbAsset;
	
	public JeeslAssetFacadeBean(EntityManager em, final AomFactoryBuilder<L,D,REALM,COMPANY,SCOPE,ASSET,STATUS,ATYPE,VIEW,EVENT,ETYPE,ESTATUS,?,?,?,?,?> fbAsset)
	{
		super(em);
		this.fbAsset=fbAsset;
	}
	
	@Override public <RREF extends EjbWithId> VIEW fAomView(REALM realm, RREF rref, JeeslAomView.Tree tree) throws JeeslNotFoundException
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<VIEW> cQ = cB.createQuery(fbAsset.getClassAssetLevel());
		Root<VIEW> root = cQ.from(fbAsset.getClassAssetLevel());
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		Expression<Long> eRefId = root.get(JeeslAomView.Attributes.rref.toString());
		Path<REALM> pRealm = root.get(JeeslAomView.Attributes.realm.toString());
		Expression<String> eTree = root.get(JeeslAomView.Attributes.tree.toString());
		
		predicates.add(cB.equal(eRefId,rref.getId()));
		predicates.add(cB.equal(pRealm,realm));
		predicates.add(cB.equal(eTree,tree.toString()));
		
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.select(root);

		TypedQuery<VIEW> tQ = em.createQuery(cQ);
		try	{return tQ.getSingleResult();}
		catch (NoResultException ex){throw new JeeslNotFoundException(ex.getMessage());}
	}
	
	@Override public List<ASSET> fAomAssets(JeeslAomQuery<REALM,SCOPE,ASSET,ATYPE,EVENT,ESTATUS> query)
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<ASSET> cQ = cB.createQuery(fbAsset.getClassAsset());
		Root<ASSET> root = cQ.from(fbAsset.getClassAsset());
		
		cQ.select(root);
		cQ.where(cB.and(this.pAsset(cB,query,root)));
		this.assetSortBy(cB, cQ, query, root);
		
		TypedQuery<ASSET> tQ = em.createQuery(cQ);
		super.pagination(tQ,query);
		return tQ.getResultList();
	}
	
	@Override public <RREF extends EjbWithId> VIEW fcAomView(REALM realm, RREF rref, JeeslAomView.Tree tree)
	{
		try	{return this.fAomView(realm,rref,tree);}
		catch (JeeslNotFoundException ex)
		{
			VIEW result = fbAsset.ejbLevel().build(realm,rref,null);
			result.setTree(tree.toString());
			try {return this.save(result);}
			catch (JeeslConstraintViolationException | JeeslLockingException e)
			{
				return this.fcAomView(realm,rref,tree);
			}
		}
	}
	
	@Override public List<ASSET> allAssets(ASSET root)
	{
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<ASSET> cQ = cB.createQuery(fbAsset.getClassAsset());
		Root<ASSET> asset = cQ.from(fbAsset.getClassAsset());
		
		Expression<Long> eRefId = asset.get(JeeslAomAsset.Attributes.realmIdentifier.toString());
		Path<REALM> pRealm = asset.get(JeeslAomAsset.Attributes.realm.toString());
		Path<ASSET> pParent = asset.get(JeeslAomAsset.Attributes.parent.toString());
		
		predicates.add(cB.equal(eRefId,root.getRealmIdentifier()));
		predicates.add(cB.equal(pRealm,root.getRealm()));
		predicates.add(cB.isNotNull(pParent));
		
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.select(asset);

		TypedQuery<ASSET> tQ = em.createQuery(cQ);
		return tQ.getResultList();
	}
	
	@Override
	public List<ATYPE> fAomAssetTypes(TenantIdentifier<REALM> identifier, VIEW view)
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<ATYPE> cQ = cB.createQuery(fbAsset.getClassAssetType());
		Root<ATYPE> root = cQ.from(fbAsset.getClassAssetType());
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		Path<REALM> pRealm = root.get(JeeslAomAssetType.Attributes.realm.toString());
		Expression<Long> eRefId = root.get(JeeslAomAssetType.Attributes.realmIdentifier.toString());
		Path<VIEW> pView = root.get(JeeslAomAssetType.Attributes.view.toString());
		Expression<Integer> ePosition = root.get(JeeslAomView.Attributes.position.toString());
		
		predicates.add(cB.equal(pRealm,identifier.getRealm()));
		predicates.add(cB.equal(eRefId,identifier.getId()));
		predicates.add(cB.equal(pView,view));
		
		cQ.select(root);
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.orderBy(cB.asc(ePosition));
		
		return em.createQuery(cQ).getResultList();
	}
	
	@Override public List<VIEW> fAomViews(TenantIdentifier<REALM> identifier)
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<VIEW> cQ = cB.createQuery(fbAsset.getClassAssetLevel());
		Root<VIEW> level = cQ.from(fbAsset.getClassAssetLevel());
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		Expression<Long> eRefId = level.get(JeeslAomView.Attributes.rref.toString());
		Path<REALM> pRealm = level.get(JeeslAomView.Attributes.realm.toString());
		Expression<Integer> ePosition = level.get(JeeslAomView.Attributes.position.toString());
		
		predicates.add(cB.equal(eRefId,identifier.getId()));
		predicates.add(cB.equal(pRealm,identifier.getRealm()));
		
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.select(level);
		cQ.orderBy(cB.asc(ePosition));
		return em.createQuery(cQ).getResultList();
	}
	
	@Override public List<COMPANY> fAomCompanies(TenantIdentifier<REALM> identifier)
	{
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<COMPANY> cQ = cB.createQuery(fbAsset.getClassCompany());
		Root<COMPANY> company = cQ.from(fbAsset.getClassCompany());
		company.fetch(JeeslAomCompany.Attributes.scopes.toString(), JoinType.LEFT);
		
		Expression<Long> eRefId = company.get(JeeslAomCompany.Attributes.realmIdentifier.toString());
		predicates.add(cB.equal(eRefId,identifier.getId()));
		
		Path<REALM> pRealm = company.get(JeeslAomCompany.Attributes.realm.toString());
		predicates.add(cB.equal(pRealm,identifier.getRealm()));
		
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.select(company);
		cQ.distinct(true);

		return em.createQuery(cQ).getResultList();
	}
	@Override public List<COMPANY> fAomCompanies(JeeslAomQuery<REALM,SCOPE,ASSET,ATYPE,EVENT,ESTATUS> query)
	{
		if(Objects.isNull(query.getTenantIdentifier())) {return new ArrayList<>();}
		
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<COMPANY> cQ = cB.createQuery(fbAsset.getClassCompany());
		Root<COMPANY> root = cQ.from(fbAsset.getClassCompany());
		root.fetch(JeeslAomCompany.Attributes.scopes.toString(), JoinType.LEFT);
		
		cQ.select(root);
		cQ.where(cB.and(pCompany(cB, query, root)));
		cQ.distinct(true);
		this.sortByCompany(cB, cQ, query, root);

		TypedQuery<COMPANY> tQ = em.createQuery(cQ);
		super.pagination(tQ,query);
		return tQ.getResultList();
	}

	@Override public List<EVENT> fAomEvents(JeeslAomQuery<REALM,SCOPE,ASSET,ATYPE,EVENT,ESTATUS> query)
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<EVENT> cQ = cB.createQuery(fbAsset.getClassEvent());
		Root<EVENT> sort = cQ.from(fbAsset.getClassEvent());
		super.rootFetch(sort, query);
		
		cQ.select(sort).distinct(true);
		cQ.where(cB.and(this.pEvent(cB, query, sort)));
		this.eventSortBy(cB,cQ,query,sort);

		TypedQuery<EVENT> tQ = em.createQuery(cQ);
		super.pagination(tQ,query);
		return tQ.getResultList();
	}

	@Override public EVENT load(EVENT event)
	{
		event = this.find(fbAsset.getClassEvent(),event);
		if(event.getMarkup()!=null) {event.getMarkup().getId();}
		return event;
	}

	@Override public JsonTuples1<VIEW> tpcTypeByView(TenantIdentifier<REALM> identifier)
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cQ = cB.createTupleQuery();
		Root<ATYPE> item = cQ.from(fbAsset.getClassAssetType());
		
		Expression<Long> eCount = cB.count(item.<Long>get("id"));
		Path<VIEW> pView = item.get(JeeslAomAssetType.Attributes.view.toString());
		
		cQ.groupBy(pView.get("id"));
		cQ.multiselect(pView.get("id"),eCount);
	       
		TypedQuery<Tuple> tQ = em.createQuery(cQ);
		Json1TuplesFactory<VIEW> jtf = Json1TuplesFactory.instance(fbAsset.getClassAssetLevel()).tupleLoad(this,true);
        return jtf.buildV2(tQ.getResultList(),JeeslCq.Agg.count);
	}

	public Predicate[] pCompany(CriteriaBuilder cB, JeeslAomQuery<REALM,SCOPE,ASSET,ATYPE,EVENT,ESTATUS> query, Root<COMPANY> root)
	{
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		if(Objects.nonNull(query.getTenantIdentifier()))
		{
			Expression<Long> eRefId = root.get(JeeslAomCompany.Attributes.realmIdentifier.toString());
			predicates.add(cB.equal(eRefId,query.getTenantIdentifier().getId()));
			
			Path<REALM> pRealm = root.get(JeeslAomCompany.Attributes.realm.toString());
			predicates.add(cB.equal(pRealm,query.getTenantIdentifier().getRealm()));
		}
		if(ObjectUtils.isNotEmpty(query.getAomCompanyScopes()))
		{
			ListJoin<COMPANY,SCOPE> jScope = root.joinList(JeeslAomCompany.Attributes.scopes.toString());
			predicates.add(jScope.in(query.getAomCompanyScopes()));
		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}
	public void sortByCompany(CriteriaBuilder cB, CriteriaQuery<COMPANY> cQ, JeeslAomQuery<REALM,SCOPE,ASSET,ATYPE,EVENT,ESTATUS> query, Root<COMPANY> root)
	{
		List<Order> orders = new ArrayList<>();
		for(JeeslCqOrdering cq : ListUtils.emptyIfNull(query.getCqOrderings()))
		{
			logger.info("SORT "+cq.toString());
			if(cq.getPath().equals(CqOrdering.path(JeeslAomCompany.Attributes.code)))
			{
				Expression<String> e = root.get(JeeslAomCompany.Attributes.code.toString());
				SortByPredicateBuilder.addByString(cB,orders,cq,e);
			}
			else if(cq.getPath().equals(CqOrdering.path(JeeslAomCompany.Attributes.name)))
			{
				Expression<String> e = root.get(JeeslAomCompany.Attributes.name.toString());
				SortByPredicateBuilder.addByString(cB,orders,cq,e);
			}
			else {logger.warn("No Handling for "+cq.toString());}
		}
		if(!orders.isEmpty()) {cQ.orderBy(orders);}
	}
	
	public Predicate[] pAsset(CriteriaBuilder cB, JeeslAomQuery<REALM,SCOPE,ASSET,ATYPE,EVENT,ESTATUS> query, Root<ASSET> root)
	{
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		if(Objects.nonNull(query.getTenantIdentifier()))
		{
			Expression<Long> eRefId = root.get(JeeslAomCompany.Attributes.realmIdentifier.toString());
			predicates.add(cB.equal(eRefId,query.getTenantIdentifier().getId()));
			
			Path<REALM> pRealm = root.get(JeeslAomCompany.Attributes.realm.toString());
			predicates.add(cB.equal(pRealm,query.getTenantIdentifier().getRealm()));
		}
		
		if(ObjectUtils.isNotEmpty(query.getAomAssetTypes()))
		{
			Join<ASSET,ATYPE> jType = root.join(JeeslAomAsset.Attributes.type1.toString());
			predicates.add(jType.in(query.getAomAssetTypes()));
		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}
	public void assetSortBy(CriteriaBuilder cB, CriteriaQuery<ASSET> cQ, JeeslAomQuery<REALM,SCOPE,ASSET,ATYPE,EVENT,ESTATUS> query, Root<ASSET> ejb)
	{
		List<Order> orders = new ArrayList<>();
		for(JeeslCqOrdering el : ListUtils.emptyIfNull(query.getCqOrderings()))
		{
			if(el.getPath().equals(CqOrdering.path(JeeslAomAsset.Attributes.position)))
			{
				Expression<Integer> e = ejb.get(JeeslAomAsset.Attributes.position.toString());
				SortByPredicateBuilder.addByInteger(cB,orders,el,e);
			}
			else {logger.warn("No Handling for "+el.toString());}
		}
		if(!orders.isEmpty()) {cQ.orderBy(orders);}
	}
	
	public Predicate[] pEvent(CriteriaBuilder cB, JeeslAomQuery<REALM,SCOPE,ASSET,ATYPE,EVENT,ESTATUS> query, Root<EVENT> root)
	{
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		if(Objects.nonNull(query.getTenantIdentifier()))
		{
			
		}
		
		for(JeeslCqDate cq : ListUtils.emptyIfNull(query.getCqDates()))
		{
			if(cq.getPath().equals(CqDate.path(JeeslAomEvent.Attributes.record)))
			{
				Expression<Date> e = root.get(JeeslAomEvent.Attributes.record.toString());
				DatePredicateBuilder.juDate(cB,predicates, cq, e);
			}
			else {logger.warn(cq.nyi(JeeslAomEvent.class));}
		}
		
		if(ObjectUtils.isNotEmpty(query.getAssets()))
		{
			ListJoin<EVENT,ASSET> jAsset = root.joinList(JeeslAomEvent.Attributes.assets.toString());
			predicates.add(jAsset.in(query.getAssets()));
		}
		if(ObjectUtils.isNotEmpty(query.getAomEventStatus()))
		{
			Join<EVENT,ESTATUS> jStatus = root.join(JeeslAomEvent.Attributes.status.toString());
			predicates.add(jStatus.in(query.getAomEventStatus()));
		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}
	public void eventSortBy(CriteriaBuilder cB, CriteriaQuery<EVENT> cQ, JeeslAomQuery<REALM,SCOPE,ASSET,ATYPE,EVENT,ESTATUS> query, Root<EVENT> ejb)
	{
		List<Order> orders = new ArrayList<>();
		for(JeeslCqOrdering el : ListUtils.emptyIfNull(query.getCqOrderings()))
		{
			if(el.getPath().equals(CqOrdering.path(JeeslAomEvent.Attributes.record)))
			{
				Expression<Date> e = ejb.get(JeeslAomEvent.Attributes.record.toString());
				SortByPredicateBuilder.juDate(cB,orders,el,e);
			}
			else {logger.warn("No Handling for "+el.toString());}
		}
		if(!orders.isEmpty()) {cQ.orderBy(orders);}
	}
}