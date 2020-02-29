package org.jeesl.controller.facade.module;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.jeesl.api.facade.module.JeeslAssetFacade;
import org.jeesl.controller.facade.JeeslFacadeBean;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.builder.module.AssetFactoryBuilder;
import org.jeesl.interfaces.model.module.aom.asset.JeeslAomAsset;
import org.jeesl.interfaces.model.module.aom.asset.JeeslAomLevel;
import org.jeesl.interfaces.model.module.aom.asset.JeeslAomStatus;
import org.jeesl.interfaces.model.module.aom.asset.JeeslAomType;
import org.jeesl.interfaces.model.module.aom.company.JeeslAomCompany;
import org.jeesl.interfaces.model.module.aom.company.JeeslAomScope;
import org.jeesl.interfaces.model.module.aom.event.JeeslAomEvent;
import org.jeesl.interfaces.model.module.aom.event.JeeslAomEventStatus;
import org.jeesl.interfaces.model.module.aom.event.JeeslAomEventType;
import org.jeesl.interfaces.model.system.io.fr.JeeslFileContainer;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.mcs.JeeslMcsRealm;
import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslAssetFacadeBean<L extends JeeslLang, D extends JeeslDescription,
										REALM extends JeeslMcsRealm<L,D,REALM,?>,
										COMPANY extends JeeslAomCompany<REALM,SCOPE>,
										SCOPE extends JeeslAomScope<L,D,SCOPE,?>,
										ASSET extends JeeslAomAsset<REALM,ASSET,COMPANY,STATUS,ATYPE>,
										STATUS extends JeeslAomStatus<L,D,STATUS,?>,
										ATYPE extends JeeslAomType<L,D,REALM,ATYPE,ALEVEL,?>,
										ALEVEL extends JeeslAomLevel<L,D,REALM,?>,
										EVENT extends JeeslAomEvent<COMPANY,ASSET,ETYPE,ESTATUS,USER,FRC>,
										ETYPE extends JeeslAomEventType<L,D,ETYPE,?>,
										ESTATUS extends JeeslAomEventStatus<L,D,ESTATUS,?>,
										USER extends JeeslSimpleUser,
										FRC extends JeeslFileContainer<?,?>>
					extends JeeslFacadeBean
					implements JeeslAssetFacade<L,D,REALM,COMPANY,SCOPE,ASSET,STATUS,ATYPE,ALEVEL,EVENT,ETYPE,ESTATUS,USER,FRC>
{	
	private static final long serialVersionUID = 1L;

	final static Logger logger = LoggerFactory.getLogger(JeeslAssetFacadeBean.class);
	
	private final AssetFactoryBuilder<L,D,REALM,COMPANY,SCOPE,ASSET,STATUS,ATYPE,ALEVEL,EVENT,ETYPE,ESTATUS,USER,FRC> fbAsset;
	
	public JeeslAssetFacadeBean(EntityManager em, final AssetFactoryBuilder<L,D,REALM,COMPANY,SCOPE,ASSET,STATUS,ATYPE,ALEVEL,EVENT,ETYPE,ESTATUS,USER,FRC> fbAsset)
	{
		super(em);
		this.fbAsset=fbAsset;
	}

	@Override
	public <RREF extends EjbWithId> ASSET fcAssetRoot(REALM realm, RREF realmReference)
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<ASSET> cQ = cB.createQuery(fbAsset.getClassAsset());
		Root<ASSET> root = cQ.from(fbAsset.getClassAsset());
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		Expression<Long> eRefId = root.get(JeeslAomAsset.Attributes.realmIdentifier.toString());
		Path<REALM> pRealm = root.get(JeeslAomAsset.Attributes.realm.toString());
		Path<ASSET> pParent = root.get(JeeslAomAsset.Attributes.parent.toString());
		
		predicates.add(cB.equal(eRefId,realmReference.getId()));
		predicates.add(cB.equal(pRealm,realm));
		predicates.add(cB.isNull(pParent));
		
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.select(root);

		TypedQuery<ASSET> tQ = em.createQuery(cQ);
		try	{return tQ.getSingleResult();}
		catch (NoResultException ex)
		{
			ATYPE type = this.fcAssetRootType(realm,realmReference);
			STATUS status = this.fByEnum(fbAsset.getClassStatus(), JeeslAomStatus.Code.na);
			ASSET result = fbAsset.ejbAsset().build(realm,realmReference, null, status, type);
			try
			{
				return this.save(result);
			}
			catch (JeeslConstraintViolationException | JeeslLockingException e)
			{
				return this.fcAssetRoot(realm,realmReference);
			}
		}
	}
	
	@Override public List<ASSET> allAssets(ASSET root)
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<ASSET> cQ = cB.createQuery(fbAsset.getClassAsset());
		Root<ASSET> asset = cQ.from(fbAsset.getClassAsset());
		List<Predicate> predicates = new ArrayList<Predicate>();
		
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
	public <RREF extends EjbWithId> ATYPE fcAssetRootType(REALM realm, RREF rref)
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<ATYPE> cQ = cB.createQuery(fbAsset.getClassAssetType());
		Root<ATYPE> root = cQ.from(fbAsset.getClassAssetType());
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		Expression<Long> eRefId = root.get(JeeslAomAsset.Attributes.realmIdentifier.toString());
		Path<REALM> pRealm = root.get(JeeslAomAsset.Attributes.realm.toString());
		Path<ASSET> pParent = root.get(JeeslAomAsset.Attributes.parent.toString());
		
		predicates.add(cB.equal(eRefId,rref.getId()));
		predicates.add(cB.equal(pRealm,realm));
		predicates.add(cB.isNull(pParent));
		
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.select(root);

		TypedQuery<ATYPE> tQ = em.createQuery(cQ);
		try	{return tQ.getSingleResult();}
		catch (NoResultException ex)
		{
			ATYPE result = fbAsset.ejbType().build(realm, rref, null, "root");
			try
			{
				return this.save(result);
			}
			catch (JeeslConstraintViolationException | JeeslLockingException e)
			{
				return this.fcAssetRootType(realm,rref);
			}
		}
	}
	
	@Override public <RREF extends EjbWithId> List<ALEVEL> fAomLevels(REALM realm, RREF rref)
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<ALEVEL> cQ = cB.createQuery(fbAsset.getClassAssetLevel());
		Root<ALEVEL> level = cQ.from(fbAsset.getClassAssetLevel());
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		Expression<Long> eRefId = level.get(JeeslAomLevel.Attributes.rref.toString());
		Path<REALM> pRealm = level.get(JeeslAomLevel.Attributes.realm.toString());
		Expression<Integer> ePosition = level.get(JeeslAomLevel.Attributes.position.toString());
		
		predicates.add(cB.equal(eRefId,rref.getId()));
		predicates.add(cB.equal(pRealm,realm));
		
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.select(level);
		cQ.orderBy(cB.asc(ePosition));
		return em.createQuery(cQ).getResultList();
	}

	@Override public <RREF extends EjbWithId> List<COMPANY> fAssetCompanies(REALM realm, RREF realmReference)
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<COMPANY> cQ = cB.createQuery(fbAsset.getClassCompany());
		Root<COMPANY> company = cQ.from(fbAsset.getClassCompany());
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		Expression<Long> eRefId = company.get(JeeslAomCompany.Attributes.realmIdentifier.toString());
		predicates.add(cB.equal(eRefId,realmReference.getId()));
		
		Path<REALM> pRealm = company.get(JeeslAomCompany.Attributes.realm.toString());
		predicates.add(cB.equal(pRealm,realm));
		
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.select(company);

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
}