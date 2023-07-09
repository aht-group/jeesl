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
import org.jeesl.controller.facade.jx.JeeslFacadeBean;
import org.jeesl.factory.builder.module.TafuFactoryBuilder;
import org.jeesl.interfaces.model.io.cms.markup.JeeslIoMarkupType;
import org.jeesl.interfaces.model.io.cms.markup.JeeslIoMarkup;
import org.jeesl.interfaces.model.module.calendar.unit.JeeslCalendarDayOfWeek;
import org.jeesl.interfaces.model.module.tafu.JeeslTafuScope;
import org.jeesl.interfaces.model.module.tafu.JeeslTafuStatus;
import org.jeesl.interfaces.model.module.tafu.JeeslTafuTask;
import org.jeesl.interfaces.model.module.tafu.JeeslTafuViewport;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.system.tenant.JeeslWithTenantSupport;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslTafuFacadeBean<L extends JeeslLang, D extends JeeslDescription,
								R extends JeeslTenantRealm<L,D,R,?>,
								T extends JeeslTafuTask<R,TS,SC,M>,
								TS extends JeeslTafuStatus<L,D,TS,?>,
								SC extends JeeslTafuScope<L,D,R,SC,?>,
								VP extends JeeslTafuViewport<L,D,VP,?>,
								DOW extends JeeslCalendarDayOfWeek<L,D,DOW,?>,
								M extends JeeslIoMarkup<MT>,
								MT extends JeeslIoMarkupType<L,D,MT,?>>
					extends JeeslFacadeBean
					implements JeeslTafuFacade<L,D,R,T,TS,SC,VP,DOW,M>
{	
	private static final long serialVersionUID = 1L;

	final static Logger logger = LoggerFactory.getLogger(JeeslAssetFacadeBean.class);
	
	private final TafuFactoryBuilder<L,D,R,T,TS,SC,VP,DOW,M,MT> fbTafu;
	
	public JeeslTafuFacadeBean(EntityManager em, final TafuFactoryBuilder<L,D,R,T,TS,SC,VP,DOW,M,MT> fbTafu)
	{
		super(em);
		this.fbTafu=fbTafu;
	}

	@Override public <RREF extends EjbWithId> List<T> fTafuBacklog(R realm, RREF rref, LocalDate vpStart, LocalDate vpEnd)
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
		Predicate pStatusActive = cB.not(pathStatus.in(listStatus));
		
		Expression<LocalDate> eShow = task.get(JeeslTafuTask.Attributes.recordShow.toString());
		Expression<LocalDate> eDue = task.get(JeeslTafuTask.Attributes.recordDue.toString());
		
		Predicate pShowBeforeVpStartWithoutDue = cB.and(cB.lessThan(eShow,vpStart),cB.isNull(eDue));
		
		Predicate pShowOutsideVp = cB.or(cB.lessThan(eShow,vpStart),cB.greaterThan(eShow,vpEnd));
		Predicate pShowOutsideVpDue = cB.and(cB.lessThanOrEqualTo(eDue,vpEnd),pShowOutsideVp);
		
		Predicate pBacklog = cB.or(pShowBeforeVpStartWithoutDue,pShowOutsideVpDue);
		
		CriteriaQuery<T> select = cQ.select(task);
		select.where(cB.and(pTenant,pStatusActive,pBacklog));

		return em.createQuery(select).getResultList();
	}

	@Override public <RREF extends EjbWithId> List<T> fTafuActive(R realm, RREF rref, LocalDate vpStart, LocalDate vpEnd)
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<T> cQ = cB.createQuery(fbTafu.getClassTask());
		Root<T> task = cQ.from(fbTafu.getClassTask());

		Path<R> pRealm = task.get(JeeslWithTenantSupport.Attributes.realm.toString());
		Expression<Long> eRref = task.get(JeeslWithTenantSupport.Attributes.rref.toString());
		Predicate pTenant = cB.and(cB.equal(pRealm,realm),cB.equal(eRref,rref.getId()));
		
		Expression<LocalDate> eShow = task.get(JeeslTafuTask.Attributes.recordShow.toString());
		Predicate pFrom = cB.greaterThanOrEqualTo(eShow,vpStart);
		Predicate pTo = cB.lessThanOrEqualTo(eShow,vpEnd);
		
		CriteriaQuery<T> select = cQ.select(task);
		select.where(cB.and(pTenant,pFrom,pTo));

		return em.createQuery(select).getResultList();
	}

	
}