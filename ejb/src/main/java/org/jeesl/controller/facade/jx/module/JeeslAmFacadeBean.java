package org.jeesl.controller.facade.jx.module;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Stack;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

import org.jeesl.api.facade.module.JeeslAmFacade;
import org.jeesl.controller.facade.jx.JeeslFacadeBean;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.factory.builder.module.AmFactoryBuilder;
import org.jeesl.interfaces.model.module.am.JeesAmProject;
import org.jeesl.interfaces.model.module.am.JeeslAmActivity;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPositionParent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslAmFacadeBean<L extends JeeslLang,D extends JeeslDescription,LOC extends JeeslLocale<L,D,LOC,?>,
								REALM extends JeeslTenantRealm<L,D,REALM,?>,
								ACTIVITY extends JeeslAmActivity<L,D,REALM,ACTIVITY,PROJ>,
								PROJ extends JeesAmProject<L,D,REALM,ACTIVITY,PROJ>
							>
					extends JeeslFacadeBean
					implements JeeslAmFacade<L,D,LOC,REALM,ACTIVITY,PROJ>
{
	private static final long serialVersionUID = 1L;

	final static Logger logger = LoggerFactory.getLogger(JeeslAmFacadeBean.class);

	private final AmFactoryBuilder<L,D,LOC,REALM,ACTIVITY,PROJ> fbAm;

	public JeeslAmFacadeBean(EntityManager em, final AmFactoryBuilder<L,D,LOC,REALM,ACTIVITY,PROJ> fbAm)
	{
		super(em);
		this.fbAm=fbAm;
	}

	@Override
	public void deleteActivityTree(ACTIVITY activity)
	{
		logger.info("called... remove.." + activity.getCode());
		Stack<ACTIVITY> stack = new Stack<ACTIVITY>();
		stack.push(activity);
		prepareDeleteStack(activity,stack);
		while(stack.size() > 0)
		{
			try
			{
				activity = stack.pop();
				logger.info("removed.." + activity.getCode());
				this.rm(activity);
			}
			catch (JeeslConstraintViolationException e) {e.printStackTrace();}
		}
	}

	private void prepareDeleteStack(ACTIVITY activity, Stack<ACTIVITY> stack)
	{
		List<ACTIVITY> activities = this.allOrderedPositionParent(fbAm.getClassActivity(),activity);

		logger.info("childrens of... remove..." + activity.getCode());
		if(Objects.nonNull(activities) && activities.size() > 0)
		{
			for(ACTIVITY childActivity :activities)
			{
				logger.info("childrens of... remove..." + activity.getCode());
				stack.push(childActivity);
				prepareDeleteStack(childActivity,stack);
			}
		}
	}

	@Override
	public <T extends EjbWithPositionParent, P extends EjbWithId> List<T> allOrderedPositionParent(Class<T> cl, P parent, boolean structural)
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<T> cQ = cB.createQuery(cl);
		Root<T> from = cQ.from(cl);

		CriteriaQuery<T> select = cQ.select(from);

		Path<Object> pathVisible = from.get("visible");
		Path<Boolean> pathStructural = from.get("structural");
		Expression<Date> eOrder = from.get("position");
		select.orderBy(cB.asc(eOrder));

		try
		{
			T prototype = cl.newInstance();
			Path<Object> p1Path = from.get(prototype.resolveParentAttribute());
			select.where(cB.and(cB.equal(p1Path, parent.getId()),cB.equal(pathVisible, true), cB.equal(pathStructural, structural)));
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}

		return em.createQuery(select).getResultList();
	}

	@Override
	public List<ACTIVITY> getChildActivity(ACTIVITY activity)
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<ACTIVITY> cQ = cB.createQuery(fbAm.getClassActivity());

		Root<ACTIVITY> item = cQ.from(fbAm.getClassActivity());
		CriteriaQuery<ACTIVITY> select = cQ.select(item);
		Path<ACTIVITY> pParent = item.get(JeeslAmActivity.Attributes.parent.toString());
		select.where(cB.equal(pParent, activity.getId()));

		return em.createQuery(select).getResultList();
	}

}