package org.jeesl.controller.facade.jx.system;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.jeesl.api.facade.system.JeeslSystemConstraintFacade;
import org.jeesl.controller.facade.jx.JeeslFacadeBean;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.system.ConstraintFactoryBuilder;
import org.jeesl.interfaces.model.system.constraint.algorithm.JeeslConstraintAlgorithm;
import org.jeesl.interfaces.model.system.constraint.core.JeeslConstraint;
import org.jeesl.interfaces.model.system.constraint.core.JeeslConstraintResolution;
import org.jeesl.interfaces.model.system.constraint.core.JeeslConstraintScope;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;

public class JeeslSystemConstraintFacadeBean<L extends JeeslLang, D extends JeeslDescription,
												ALGCAT extends JeeslStatus<L,D,ALGCAT>,
												ALGO extends JeeslConstraintAlgorithm<L,D,ALGCAT>,
												SCOPE extends JeeslConstraintScope<L,D,CONCAT>,
												CONCAT extends JeeslStatus<L,D,CONCAT>,
												CONSTRAINT extends JeeslConstraint<L,D,SCOPE,CONCAT,CONSTRAINT,LEVEL,TYPE,RESOLUTION>,
												LEVEL extends JeeslStatus<L,D,LEVEL>,
												TYPE extends JeeslStatus<L,D,TYPE>,
												RESOLUTION extends JeeslConstraintResolution<L,D,CONSTRAINT>>
					extends JeeslFacadeBean
					implements JeeslSystemConstraintFacade<L,D,ALGCAT,ALGO,SCOPE,CONCAT,CONSTRAINT,LEVEL,TYPE,RESOLUTION>
{	
	private static final long serialVersionUID = 1L;
	private final ConstraintFactoryBuilder<L,D,ALGCAT,ALGO,SCOPE,CONCAT,CONSTRAINT,LEVEL,TYPE,RESOLUTION> fbConstraint;

	
	public JeeslSystemConstraintFacadeBean(EntityManager em, ConstraintFactoryBuilder<L,D,ALGCAT,ALGO,SCOPE,CONCAT,CONSTRAINT,LEVEL,TYPE,RESOLUTION> fbConstraint, final Class<SCOPE> cScope, final Class<CONSTRAINT> cConstraint)
	{
		super(em);
		this.fbConstraint=fbConstraint;
	}
	
	@Override public <E extends Enum<E>> CONSTRAINT fSystemConstraint(Class<?> c, E code)
	{
		try
		{
			SCOPE scope = this.fByCode(fbConstraint.getClassScope(),c.getSimpleName());
			return this.fSystemConstraint(scope,code.toString());
		}
		catch (JeeslNotFoundException e) {e.printStackTrace();return null;}
	}
	
	@Override public CONSTRAINT fSystemConstraint(SCOPE scope, String code) throws JeeslNotFoundException
	{
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<CONSTRAINT> cQ = cB.createQuery(fbConstraint.getClassConstraint());
		Root<CONSTRAINT> constraint = cQ.from(fbConstraint.getClassConstraint());
		
		Join<CONSTRAINT,SCOPE> jScope = constraint.join(JeeslConstraint.Attributes.scope.toString());
		Expression<String> eCode = constraint.get(JeeslConstraint.Attributes.code.toString());
		
		predicates.add(cB.equal(jScope,scope));
		predicates.add(cB.equal(eCode,code));
		
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.select(constraint);

		TypedQuery<CONSTRAINT> tQ = em.createQuery(cQ);
		try	{return tQ.getSingleResult();}
		catch (NoResultException ex){throw new JeeslNotFoundException("No "+fbConstraint.getClassConstraint().getSimpleName()+" found for scope="+scope.toString()+" and code="+code);}
		catch (NonUniqueResultException ex){throw new JeeslNotFoundException("No unique results in "+fbConstraint.getClassConstraint().getSimpleName()+" for type="+scope.toString()+" and code="+code);}
	}

	
}