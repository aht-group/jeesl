package org.jeesl.controller.facade.io;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.ObjectUtils;
import org.jeesl.api.facade.io.JeeslIoAttributeFacade;
import org.jeesl.controller.facade.JeeslFacadeBean;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.io.IoAttributeFactoryBuilder;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeCategory;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeContainer;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeCriteria;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeData;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeItem;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeOption;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeSet;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.system.tenant.JeeslWithTenantSupport;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslIoAttributeFacadeBean<L extends JeeslLang, D extends JeeslDescription,
										R extends JeeslTenantRealm<L,D,R,?>,
										CAT extends JeeslAttributeCategory<L,D,R,CAT,?>,
										CATEGORY extends JeeslStatus<L,D,CATEGORY>,
										CRITERIA extends JeeslAttributeCriteria<L,D,R,CAT,CATEGORY,TYPE,OPTION>,
										TYPE extends JeeslStatus<L,D,TYPE>,
										OPTION extends JeeslAttributeOption<L,D,CRITERIA>,
										SET extends JeeslAttributeSet<L,D,R,CAT,CATEGORY,ITEM>,
										ITEM extends JeeslAttributeItem<CRITERIA,SET>,
										CONTAINER extends JeeslAttributeContainer<SET,DATA>,
										DATA extends JeeslAttributeData<CRITERIA,OPTION,CONTAINER>>
					extends JeeslFacadeBean
					implements JeeslIoAttributeFacade<L,D,R,CAT,CATEGORY,CRITERIA,TYPE,OPTION,SET,ITEM,CONTAINER,DATA>
{	
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslIoAttributeFacadeBean.class);
	
	private final IoAttributeFactoryBuilder<L,D,R,CAT,CATEGORY,CRITERIA,TYPE,OPTION,SET,ITEM,CONTAINER,DATA> fbAttribute;
	
	public JeeslIoAttributeFacadeBean(EntityManager em, IoAttributeFactoryBuilder<L,D,R,CAT,CATEGORY,CRITERIA,TYPE,OPTION,SET,ITEM,CONTAINER,DATA> fbAttribute)
	{
		super(em);
		this.fbAttribute=fbAttribute;
	}
	
	@Override public SET load(SET set)
	{
		set = em.find(fbAttribute.getClassSet(), set.getId());
		set.getItems().size();
		return set;
	}
	
	@Override
	public List<CRITERIA> fAttributeCriteria(List<CATEGORY> categories, long refId)
	{
		if(categories==null || categories.isEmpty()){return new ArrayList<CRITERIA>();}
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<CRITERIA> cQ = cB.createQuery(fbAttribute.getClassCriteria());
		Root<CRITERIA> root = cQ.from(fbAttribute.getClassCriteria());
		
		Join<CRITERIA,CATEGORY> jCategory = root.join(JeeslAttributeCriteria.Attributes.category.toString());
		predicates.add(jCategory.in(categories));
		
		if(refId>0)
		{
			Expression<Long> eRefId = root.get(JeeslAttributeCriteria.Attributes.refId.toString());
			predicates.add(cB.equal(eRefId,refId));
		}
		
		Path<Integer> pPosition = root.get(JeeslAttributeCriteria.Attributes.position.toString());
		Path<Integer> pCategoryPosition = jCategory.get(JeeslAttributeCriteria.Attributes.position.toString());
		
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.orderBy(cB.asc(pCategoryPosition),cB.asc(pPosition));
		cQ.select(root);

		return em.createQuery(cQ).getResultList();
	}
	
	@Override
	public <RREF extends EjbWithId> List<CRITERIA> fAttributeCriteria(R realm, RREF rref, List<CAT> categories)
	{
		if(categories==null || categories.isEmpty()) {return new ArrayList<CRITERIA>();}
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<CRITERIA> cQ = cB.createQuery(fbAttribute.getClassCriteria());
		Root<CRITERIA> root = cQ.from(fbAttribute.getClassCriteria());
		
		Expression<R> rRealm = root.get(JeeslWithTenantSupport.Attributes.realm.toString());
		predicates.add(cB.equal(rRealm,realm));
		
		Expression<Long> eRref = root.get(JeeslWithTenantSupport.Attributes.rref.toString());
		predicates.add(cB.equal(eRref,rref.getId()));
		
		Join<CRITERIA,CAT> jCategory = root.join(JeeslAttributeCriteria.Attributes.category2.toString());
		predicates.add(jCategory.in(categories));
		
		Path<Integer> pPosition = root.get(JeeslAttributeCriteria.Attributes.position.toString());
		Path<Integer> pCategoryPosition = jCategory.get(JeeslAttributeCriteria.Attributes.position.toString());
		
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.orderBy(cB.asc(pCategoryPosition),cB.asc(pPosition));
		cQ.select(root);

		return em.createQuery(cQ).getResultList();
	}
	
	@Override public List<CRITERIA> fAttributeCriteria(SET set)
	{
		List<CRITERIA> result = new ArrayList<>();
 		if(set==null){return result;}
 		
 		set = this.find(fbAttribute.getClassSet(),set);
 		for(ITEM item : set.getItems())
 		{
 			result.add(item.getCriteria());
 		}
		return result;
	}
	
	@Override
	public List<OPTION> fAttributeOption(SET set)
	{
		List<OPTION> result = new ArrayList<>();
 		if(set==null){return result;}
 		
 		set = this.find(fbAttribute.getClassSet(),set);
 		for(ITEM item : set.getItems())
 		{
 			result.addAll(item.getCriteria().getOptions());
 		}
		return result;
	}
	
	@Override public List<SET> fAttributeSets(List<CATEGORY> categories, long refId)
	{
		if(categories==null || categories.isEmpty()){return new ArrayList<SET>();}
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<SET> cQ = cB.createQuery(fbAttribute.getClassSet());
		Root<SET> root = cQ.from(fbAttribute.getClassSet());
		
		Join<SET,CATEGORY> jCategory = root.join(JeeslAttributeSet.Attributes.category.toString());
		predicates.add(jCategory.in(categories));
		
		if(refId>0)
		{
			Expression<Long> eRefId = root.get(JeeslAttributeSet.Attributes.refId.toString());
			predicates.add(cB.equal(eRefId,refId));
		}
		
		Path<Integer> pPosition = root.get(JeeslAttributeSet.Attributes.position.toString());
		Path<Integer> pCategoryPosition = jCategory.get(JeeslAttributeSet.Attributes.position.toString());
		
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.orderBy(cB.asc(pCategoryPosition),cB.asc(pPosition));
		cQ.select(root);

		return em.createQuery(cQ).getResultList();
	}
	
	@Override public <RREF extends EjbWithId> List<SET> fAttributeSets(R realm, RREF rref, List<CAT> categories)
	{
		if(categories==null || categories.isEmpty()){return new ArrayList<SET>();}
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<SET> cQ = cB.createQuery(fbAttribute.getClassSet());
		Root<SET> root = cQ.from(fbAttribute.getClassSet());
		
		Expression<R> rRealm = root.get(JeeslWithTenantSupport.Attributes.realm.toString());
		predicates.add(cB.equal(rRealm,realm));
		
		Expression<Long> eRref = root.get(JeeslWithTenantSupport.Attributes.rref.toString());
		predicates.add(cB.equal(eRref,rref.getId()));
		
		Join<SET,CAT> jCategory = root.join(JeeslAttributeSet.Attributes.category2.toString());
		predicates.add(jCategory.in(categories));
		
		Path<Integer> pPosition = root.get(JeeslAttributeSet.Attributes.position.toString());
		Path<Integer> pCategoryPosition = jCategory.get(JeeslAttributeSet.Attributes.position.toString());
		
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.orderBy(cB.asc(pCategoryPosition),cB.asc(pPosition));
		cQ.select(root);

		return em.createQuery(cQ).getResultList();
	}

	
	@Override public DATA fAttributeData(CRITERIA criteria, CONTAINER container) throws JeeslNotFoundException
	{
		List<DATA> datas = fAttributeData(container);
		for(DATA data : datas) {if(data.getCriteria().equals(criteria)) {return data;}}
		throw new JeeslNotFoundException("no data for container");
	}
	
	@Override public List<DATA> fAttributeData(CONTAINER container)
	{
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<DATA> cQ = cB.createQuery(fbAttribute.getClassData());
		Root<DATA> data = cQ.from(fbAttribute.getClassData());
		
		Path<CONTAINER> pContainer = data.join(JeeslAttributeData.Attributes.container.toString());
		predicates.add(cB.equal(pContainer,container));
		
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.select(data);

		TypedQuery<DATA> tQ = em.createQuery(cQ);
		return tQ.getResultList();
	}
	
	@Override public List<DATA> fAttributeData(CRITERIA criteria, List<CONTAINER> containers)
	{
		if(ObjectUtils.isEmpty(containers)) {return new ArrayList<>();}
		
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<DATA> cQ = cB.createQuery(fbAttribute.getClassData());
		Root<DATA> data = cQ.from(fbAttribute.getClassData());
		
		data.fetch(JeeslAttributeData.Attributes.valueOption.toString(), JoinType.LEFT);
		data.fetch(JeeslAttributeData.Attributes.valueOptions.toString(), JoinType.LEFT);
		data.fetch(JeeslAttributeData.Attributes.container.toString(), JoinType.LEFT);
		
		Path<CONTAINER> pContainer = data.join(JeeslAttributeData.Attributes.container.toString());
		predicates.add(pContainer.in(containers));
		
		Path<CRITERIA> pCriteria = data.join(JeeslAttributeData.Attributes.criteria.toString());
		predicates.add(cB.equal(pCriteria,criteria));
		
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.select(data);

		TypedQuery<DATA> tQ = em.createQuery(cQ);
		tQ.setHint("javax.persistence.query.timeout",200);
		return tQ.getResultList();
	}

	@Override
	public CONTAINER copy(CONTAINER container) throws JeeslConstraintViolationException, JeeslLockingException
	{
		CONTAINER c = this.save(fbAttribute.ejbContainer().build(container.getSet()));
		for(DATA data : this.fAttributeData(container))
		{
			this.save(fbAttribute.ejbData().copy(c,data));
		}
		return c;
	}
}