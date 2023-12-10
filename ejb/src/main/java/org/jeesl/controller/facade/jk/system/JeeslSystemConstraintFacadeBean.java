package org.jeesl.controller.facade.jk.system;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import org.jeesl.api.facade.system.JeeslSystemConstraintFacade;
import org.jeesl.controller.facade.jk.JeeslFacadeBean;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.system.ConstraintFactoryBuilder;
import org.jeesl.interfaces.model.system.constraint.algorithm.JeeslConstraintAlgorithm;
import org.jeesl.interfaces.model.system.constraint.algorithm.JeeslConstraintAlgorithmGroup;
import org.jeesl.interfaces.model.system.constraint.core.JeeslConstraint;
import org.jeesl.interfaces.model.system.constraint.core.JeeslConstraintCategory;
import org.jeesl.interfaces.model.system.constraint.core.JeeslConstraintLevel;
import org.jeesl.interfaces.model.system.constraint.core.JeeslConstraintResolution;
import org.jeesl.interfaces.model.system.constraint.core.JeeslConstraintScope;
import org.jeesl.interfaces.model.system.constraint.core.JeeslConstraintType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;

public class JeeslSystemConstraintFacadeBean<L extends JeeslLang, D extends JeeslDescription,
												GROUP extends JeeslConstraintAlgorithmGroup<L,D,GROUP,?>,
												ALGORITHM extends JeeslConstraintAlgorithm<L,D,GROUP>,
												SCOPE extends JeeslConstraintScope<L,D,CATEGORY>,
												CATEGORY extends JeeslConstraintCategory<L,D,CATEGORY,?>,
												CONSTRAINT extends JeeslConstraint<L,D,SCOPE,CATEGORY,CONSTRAINT,LEVEL,TYPE,RESOLUTION>,
												LEVEL extends JeeslConstraintLevel<L,D,LEVEL,?>,
												TYPE extends JeeslConstraintType<L,D,TYPE,?>,
												RESOLUTION extends JeeslConstraintResolution<L,D,CONSTRAINT>>
					extends JeeslFacadeBean
					implements JeeslSystemConstraintFacade<L,D,ALGORITHM,GROUP,SCOPE,CONSTRAINT,CATEGORY,LEVEL,TYPE,RESOLUTION>
{	
	private static final long serialVersionUID = 1L;
	private final ConstraintFactoryBuilder<L,D,GROUP,ALGORITHM,SCOPE,CATEGORY,CONSTRAINT,LEVEL,TYPE,RESOLUTION> fbConstraint;

	
	public JeeslSystemConstraintFacadeBean(EntityManager em, ConstraintFactoryBuilder<L,D,GROUP,ALGORITHM,SCOPE,CATEGORY,CONSTRAINT,LEVEL,TYPE,RESOLUTION> fbConstraint, final Class<SCOPE> cScope, final Class<CONSTRAINT> cConstraint)
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