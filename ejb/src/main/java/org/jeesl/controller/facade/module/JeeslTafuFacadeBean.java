package org.jeesl.controller.facade.module;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.jeesl.api.facade.module.JeeslTafuFacade;
import org.jeesl.controller.facade.JeeslFacadeBean;
import org.jeesl.factory.builder.module.TafuFactoryBuilder;
import org.jeesl.interfaces.model.module.tafu.JeeslTafuStatus;
import org.jeesl.interfaces.model.module.tafu.JeeslTafuTask;
import org.jeesl.interfaces.model.module.tafu.JeeslTafuViewport;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.system.tenant.JeeslWithTenantSupport;
import org.jeesl.interfaces.model.system.time.JeeslTimeDayOfWeek;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslTafuFacadeBean<L extends JeeslLang, D extends JeeslDescription,
								R extends JeeslTenantRealm<L,D,R,?>,
								T extends JeeslTafuTask<R,TS>,
								TS extends JeeslTafuStatus<L,D,TS,?>,
								VP extends JeeslTafuViewport<L,D,VP,?>,
								DOW extends JeeslTimeDayOfWeek<L,D,DOW,?>>
					extends JeeslFacadeBean
					implements JeeslTafuFacade<L,D,R,T,TS,VP,DOW>
{	
	private static final long serialVersionUID = 1L;

	final static Logger logger = LoggerFactory.getLogger(JeeslAssetFacadeBean.class);
	
	private final TafuFactoryBuilder<L,D,R,T,TS,VP,DOW> fbTafu;
	
	public JeeslTafuFacadeBean(EntityManager em, final TafuFactoryBuilder<L,D,R,T,TS,VP,DOW> fbTafu)
	{
		super(em);
		this.fbTafu=fbTafu;
	}

	@Override public <RREF extends EjbWithId> List<T> fTafuBacklog(R realm, RREF rref, LocalDate date)
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<T> cQ = cB.createQuery(fbTafu.getClassTask());
		Root<T> task = cQ.from(fbTafu.getClassTask());

		Path<R> pRealm = task.get(JeeslWithTenantSupport.Attributes.realm.toString());
		Expression<Long> eRref = task.get(JeeslWithTenantSupport.Attributes.rref.toString());
		Predicate pTenant = cB.and(cB.equal(pRealm,realm),cB.equal(eRref,rref.getId()));
		
		List<TS> listStatus = new ArrayList<>();
		listStatus.add(this.fByEnum(fbTafu.getClassStatus(),JeeslTafuStatus.Code.closed));
		listStatus.add(this.fByEnum(fbTafu.getClassStatus(),JeeslTafuStatus.Code.discarded));
		Path<TS> pathStatus = task.get(JeeslTafuTask.Attributes.status.toString());
		Predicate pStatus = cB.not(pathStatus.in(listStatus));
		
		Expression<LocalDate> eShow = task.get(JeeslTafuTask.Attributes.recordShow.toString());
		Predicate pDate = cB.lessThan(eShow, date);
		
		CriteriaQuery<T> select = cQ.select(task);
		select.where(cB.and(pTenant,pStatus,pDate));

//		if(EjbWithPosition.class.isAssignableFrom(c)){select.orderBy(cB.asc(from.get(EjbWithPosition.attributePosition)));}
//		else if(EjbWithRecord.class.isAssignableFrom(c)){select.orderBy(cB.asc(from.get(EjbWithRecord.attributeRecord)));}

		return em.createQuery(select).getResultList();
	}

	@Override public <RREF extends EjbWithId> List<T> fTafuActive(R realm, RREF rref, LocalDate from, LocalDate to)
	{
		return this.all(fbTafu.getClassTask(),realm,rref);
	}

	
}