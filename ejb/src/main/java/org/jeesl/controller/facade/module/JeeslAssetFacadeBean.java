package org.jeesl.controller.facade.module;

import java.util.ArrayList;
import java.util.List;

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
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.jeesl.api.facade.module.JeeslAomFacade;
import org.jeesl.controller.facade.JeeslFacadeBean;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.module.AomFactoryBuilder;
import org.jeesl.factory.json.io.db.tuple.JsonTupleFactory;
import org.jeesl.factory.json.system.io.db.tuple.t1.Json1TuplesFactory;
import org.jeesl.interfaces.model.io.fr.JeeslFileContainer;
import org.jeesl.interfaces.model.module.aom.asset.JeeslAomAsset;
import org.jeesl.interfaces.model.module.aom.asset.JeeslAomAssetStatus;
import org.jeesl.interfaces.model.module.aom.asset.JeeslAomAssetType;
import org.jeesl.interfaces.model.module.aom.asset.JeeslAomView;
import org.jeesl.interfaces.model.module.aom.company.JeeslAomCompany;
import org.jeesl.interfaces.model.module.aom.company.JeeslAomScope;
import org.jeesl.interfaces.model.module.aom.event.JeeslAomEvent;
import org.jeesl.interfaces.model.module.aom.event.JeeslAomEventStatus;
import org.jeesl.interfaces.model.module.aom.event.JeeslAomEventType;
import org.jeesl.interfaces.model.module.aom.event.JeeslAomEventUpload;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.model.ejb.system.tenant.TenantIdentifier;
import org.jeesl.model.json.db.tuple.t1.Json1Tuples;
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
										EVENT extends JeeslAomEvent<COMPANY,ASSET,ETYPE,ESTATUS,?,USER,FRC>,
										ETYPE extends JeeslAomEventType<L,D,ETYPE,?>,
										ESTATUS extends JeeslAomEventStatus<L,D,ESTATUS,?>,
										USER extends JeeslSimpleUser,
										FRC extends JeeslFileContainer<?,?>,
										UP extends JeeslAomEventUpload<L,D,UP,?>>
					extends JeeslFacadeBean
					implements JeeslAomFacade<L,D,REALM,COMPANY,ASSET,STATUS,ATYPE,VIEW,EVENT,ETYPE,ESTATUS,UP>
{	
	private static final long serialVersionUID = 1L;

	final static Logger logger = LoggerFactory.getLogger(JeeslAssetFacadeBean.class);
	
	private final AomFactoryBuilder<L,D,REALM,COMPANY,SCOPE,ASSET,STATUS,ATYPE,VIEW,EVENT,ETYPE,ESTATUS,?,?,USER,FRC,UP> fbAsset;
	
	public JeeslAssetFacadeBean(EntityManager em, final AomFactoryBuilder<L,D,REALM,COMPANY,SCOPE,ASSET,STATUS,ATYPE,VIEW,EVENT,ETYPE,ESTATUS,?,?,USER,FRC,UP> fbAsset)
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
	
	@Override
	public <RREF extends EjbWithId> List<ASSET> fAomAssets(REALM realm, RREF rref, ATYPE type1, ATYPE type2)
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<ASSET> cQ = cB.createQuery(fbAsset.getClassAsset());
		Root<ASSET> root = cQ.from(fbAsset.getClassAsset());
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		Expression<Long> eRref = root.get(JeeslAomAsset.Attributes.realmIdentifier.toString());
		Path<REALM> pRealm = root.get(JeeslAomAsset.Attributes.realm.toString());
		
		predicates.add(cB.equal(eRref,rref.getId()));
		predicates.add(cB.equal(pRealm,realm));
		
		if(type1!=null)
		{
			Path<ATYPE> pType = root.get(JeeslAomAsset.Attributes.type1.toString());
			predicates.add(cB.equal(pType,type1));
		}
		if(type2!=null)
		{
			Path<ATYPE> pType = root.get(JeeslAomAsset.Attributes.type2.toString());
			predicates.add(cB.equal(pType,type2));
		}
		
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.select(root);

		TypedQuery<ASSET> tQ = em.createQuery(cQ);
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

//	@Override public <RREF extends EjbWithId> ASSET fcAssetRoot(REALM realm, RREF rref)
//	{
//		CriteriaBuilder cB = em.getCriteriaBuilder();
//		CriteriaQuery<ASSET> cQ = cB.createQuery(fbAsset.getClassAsset());
//		Root<ASSET> root = cQ.from(fbAsset.getClassAsset());
//		List<Predicate> predicates = new ArrayList<Predicate>();
//		
//		Expression<Long> eRefId = root.get(JeeslAomAsset.Attributes.realmIdentifier.toString());
//		Path<REALM> pRealm = root.get(JeeslAomAsset.Attributes.realm.toString());
//		Path<ASSET> pParent = root.get(JeeslAomAsset.Attributes.parent.toString());
//		
//		predicates.add(cB.equal(eRefId,rref.getId()));
//		predicates.add(cB.equal(pRealm,realm));
//		predicates.add(cB.isNull(pParent));
//		
//		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
//		cQ.select(root);
//
//		TypedQuery<ASSET> tQ = em.createQuery(cQ);
//		try	{return tQ.getSingleResult();}
//		catch (NoResultException ex)
//		{
//			VIEW view = this.fcAomView(realm, rref, JeeslAomView.Tree.hierarchy);
//			
//			List<ATYPE> list = this.fAomAssetTypes(TenantIdentifier.instance(realm).withRref(rref),view);
//			ATYPE type = list.get(0);
//			STATUS status = this.fByEnum(fbAsset.getClassStatus(), JeeslAomAssetStatus.Code.na);
//			ASSET result = fbAsset.ejbAsset().build(realm,rref, null, status, type);
//			try {return this.save(result);}
//			catch (JeeslConstraintViolationException | JeeslLockingException e)
//			{
//				return this.fcAssetRoot(realm,rref);
//			}
//		}
//	}
	
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
	
	@Override public List<ASSET> fAomAssets(TenantIdentifier<REALM> identifier)
	{
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<ASSET> cQ = cB.createQuery(fbAsset.getClassAsset());
		Root<ASSET> root = cQ.from(fbAsset.getClassAsset());
		
		Expression<Long> eRefId = root.get(JeeslAomAsset.Attributes.realmIdentifier.toString());
		Path<REALM> pRealm = root.get(JeeslAomAsset.Attributes.realm.toString());
		
		predicates.add(cB.equal(eRefId,identifier.getId()));
		predicates.add(cB.equal(pRealm,identifier.getRealm()));
		
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.select(root);

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

//	@Override public <RREF extends EjbWithId> ATYPE fcAomRootType(REALM realm, RREF rref, VIEW view)
//	{
//		CriteriaBuilder cB = em.getCriteriaBuilder();
//		CriteriaQuery<ATYPE> cQ = cB.createQuery(fbAsset.getClassAssetType());
//		Root<ATYPE> root = cQ.from(fbAsset.getClassAssetType());
//		List<Predicate> predicates = new ArrayList<Predicate>();
//		
//		Expression<Long> eRefId = root.get(JeeslAomAssetType.Attributes.realmIdentifier.toString());
//		Path<REALM> pRealm = root.get(JeeslAomAssetType.Attributes.realm.toString());
//		Path<VIEW> pView = root.get(JeeslAomAssetType.Attributes.view.toString());
//		Path<ASSET> pParent = root.get(JeeslAomAssetType.Attributes.parent.toString());
//		
//		predicates.add(cB.equal(eRefId,rref.getId()));
//		predicates.add(cB.equal(pRealm,realm));
//		predicates.add(cB.equal(pView,view));
//		predicates.add(cB.isNull(pParent));
//		
//		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
//		cQ.select(root);
//
//		TypedQuery<ATYPE> tQ = em.createQuery(cQ);
//		try	{return tQ.getSingleResult();}
//		catch (NoResultException e1)
//		{
//			ATYPE result = fbAsset.ejbType().build(realm,rref,view, null, "root");
//			try {return this.save(result);}
//			catch (JeeslConstraintViolationException | JeeslLockingException e2)
//			{
//				return this.fcAomRootType(realm,rref,view);
//			}
//		}
//		catch (NonUniqueResultException e)
//		{
//			e.printStackTrace();
//			logger.error(StringUtil.stars());
//			logger.error("Realm: "+realm.toString());
//			logger.error("Rref: "+rref.toString());
//			logger.error("View: "+view.toString());
//			logger.error("Parent: null");
//			
//			logger.error(StringUtil.stars());
//			logger.warn("Return null");
//			return null;
//		}
//	}
	
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

	@Override public List<EVENT> fAssetEvents(ASSET asset)
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<EVENT> cQ = cB.createQuery(fbAsset.getClassEvent());
		Root<EVENT> event = cQ.from(fbAsset.getClassEvent());
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		ListJoin<EVENT,ASSET> jAsset = event.joinList(JeeslAomEvent.Attributes.assets.toString());
		predicates.add(jAsset.in(asset));
		
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.select(event);

		return em.createQuery(cQ).getResultList();
	}

	@Override
	public <RREF extends EjbWithId> List<EVENT> fAssetEvents(REALM realm, RREF rref, List<ESTATUS> status)
	{
		if(status==null || status.isEmpty()) {return new ArrayList<>();}
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<EVENT> cQ = cB.createQuery(fbAsset.getClassEvent());
		Root<EVENT> event = cQ.from(fbAsset.getClassEvent());
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		ListJoin<EVENT,ASSET> jAsset = event.joinList(JeeslAomEvent.Attributes.assets.toString());
		Expression<Long> eRefId = jAsset.get(JeeslAomAsset.Attributes.realmIdentifier.toString());
		Path<REALM> pRealm = jAsset.get(JeeslAomAsset.Attributes.realm.toString());
		
		predicates.add(cB.equal(eRefId,rref.getId()));
		predicates.add(cB.equal(pRealm,realm));
		
		Join<EVENT,ESTATUS> jStatus = event.join(JeeslAomEvent.Attributes.status.toString());
		predicates.add(jStatus.in(status));
		
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.select(event);
		cQ.distinct(true);

		TypedQuery<EVENT> tQ = em.createQuery(cQ);
		return tQ.getResultList();
	}

	@Override public EVENT load(EVENT event)
	{
		event = this.find(fbAsset.getClassEvent(),event);
		if(event.getMarkup()!=null) {event.getMarkup().getId();}
		return event;
	}

	@Override public Json1Tuples<VIEW> tpcTypeByView(TenantIdentifier<REALM> identifier)
	{
		Json1TuplesFactory<VIEW> jtf = new Json1TuplesFactory<>(this,fbAsset.getClassAssetLevel());
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cQ = cB.createTupleQuery();
		Root<ATYPE> item = cQ.from(fbAsset.getClassAssetType());
		
		Expression<Long> eCount = cB.count(item.<Long>get("id"));
		Path<VIEW> pView = item.get(JeeslAomAssetType.Attributes.view.toString());
		
		cQ.groupBy(pView.get("id"));
		cQ.multiselect(pView.get("id"),eCount);
	       
		TypedQuery<Tuple> tQ = em.createQuery(cQ);
        return jtf.buildV2(tQ.getResultList(),JsonTupleFactory.Type.count);
	}
}