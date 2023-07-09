package org.jeesl.controller.facade.jx.module;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.jeesl.api.facade.module.JeeslItsFacade;
import org.jeesl.controller.facade.jx.JeeslFacadeBean;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.builder.module.ItsFactoryBuilder;
import org.jeesl.interfaces.model.module.its.config.JeeslItsConfig;
import org.jeesl.interfaces.model.module.its.config.JeeslItsConfigOption;
import org.jeesl.interfaces.model.module.its.issue.JeeslItsIssue;
import org.jeesl.interfaces.model.module.its.issue.JeeslItsIssueStatus;
import org.jeesl.interfaces.model.module.its.task.JeeslItsTask;
import org.jeesl.interfaces.model.module.its.task.JeeslItsTaskType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslItsFacadeBean<L extends JeeslLang, D extends JeeslDescription,
								R extends JeeslTenantRealm<L,D,R,?>,
								C extends JeeslItsConfig<L,D,R,O>,
								O extends JeeslItsConfigOption<L,D,O,?>,
								I extends JeeslItsIssue<R,I,IS>,
								IS extends JeeslItsIssueStatus<L,D,R,IS,?>,
								T extends JeeslItsTask<I,TT,?>,
								TT extends JeeslItsTaskType<L,D,TT,?>>
					extends JeeslFacadeBean
					implements JeeslItsFacade<L,D,R,C,O,I,IS,T,TT>
{	
	private static final long serialVersionUID = 1L;

	final static Logger logger = LoggerFactory.getLogger(JeeslAssetFacadeBean.class);
	
	private final ItsFactoryBuilder<L,D,R,C,O,I,IS,T,TT> fbIssue;
	
	public JeeslItsFacadeBean(EntityManager em, final ItsFactoryBuilder<L,D,R,C,O,I,IS,T,TT> fbIssue)
	{
		super(em);
		this.fbIssue=fbIssue;
	}

	@Override
	public <RREF extends EjbWithId> I fcAItsRoot(R realm, RREF rref, IS status)
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<I> cQ = cB.createQuery(fbIssue.getClassIssue());
		Root<I> root = cQ.from(fbIssue.getClassIssue());
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		Expression<Long> eRefId = root.get(JeeslItsIssue.Attributes.rref.toString());
		Path<R> pRealm = root.get(JeeslItsIssue.Attributes.realm.toString());
		Path<I> pParent = root.get(JeeslItsIssue.Attributes.parent.toString());
		
		predicates.add(cB.equal(eRefId,rref.getId()));
		predicates.add(cB.equal(pRealm,realm));
		predicates.add(cB.isNull(pParent));
		
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.select(root);

		TypedQuery<I> tQ = em.createQuery(cQ);
		try	{return tQ.getSingleResult();}
		catch (NoResultException ex)
		{
			I result = fbIssue.ejbIssue().build(realm,rref, null);
			try {return this.save(result);}
			catch (JeeslConstraintViolationException | JeeslLockingException e)
			{
				return this.fcAItsRoot(realm,rref,status);
			}
		}
	}
}