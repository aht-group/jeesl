package org.jeesl.controller.facade.system;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.jeesl.api.facade.system.JeeslSystemPropertyFacade;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.model.system.property.JeeslProperty;

import net.sf.ahtutils.controller.facade.UtilsFacadeBean;
import net.sf.ahtutils.interfaces.model.status.UtilsDescription;
import net.sf.ahtutils.interfaces.model.status.UtilsLang;
import net.sf.ahtutils.interfaces.model.status.UtilsStatus;

public class JeeslSystemPropertyFacadeBean<L extends UtilsLang,D extends UtilsDescription,
											C extends UtilsStatus<C,L,D>,
											P extends JeeslProperty<L,D,C,P>>
					extends UtilsFacadeBean
					implements JeeslSystemPropertyFacade<L,D,C,P>
{	
	private final Class<P> cProperty;
	
	public JeeslSystemPropertyFacadeBean(EntityManager em, final Class<P> cProperty)
	{
		super(em);
		this.cProperty=cProperty;
	}
	
	@Override
	public Integer valueIntForKey(String key, Integer defaultValue) throws JeeslNotFoundException
	{
		try
		{
			P t = valueForKey(key);
			return new Integer(t.getValue());
		}
		catch (JeeslNotFoundException e)
		{
			if(defaultValue!=null){return defaultValue;}
			else{throw e;}
		}
	}
	
	@Override
	public Long valueLongForKey(String key, Long defaultValue) throws JeeslNotFoundException
	{
		try
		{
			P t = valueForKey(key);
			return new Long(t.getValue());
		}
		catch (JeeslNotFoundException e)
		{
			if(defaultValue!=null){return defaultValue;}
			else{throw e;}
		}
	}

	@Override
	public Boolean valueBooleanForKey(String key, Boolean defaultValue) throws JeeslNotFoundException
	{
		try
		{
			P t = valueForKey(key);
			return new Boolean(t.getValue());
		}
		catch (JeeslNotFoundException e)
		{
			if(defaultValue!=null){return defaultValue;}
			else{throw e;}
		}
	}

	@Override
	public Date valueDateForKey(String key, Date defaultValue) throws JeeslNotFoundException
	{
		try
		{
			P t = valueForKey(key);
			return new Date(new Long(t.getValue()));
		}
		catch (JeeslNotFoundException e)
		{
			if(defaultValue!=null){return defaultValue;}
			else{throw e;}
		}
	}
	
	@Override
	public String valueStringForKey(String key, String defaultValue) throws JeeslNotFoundException
	{
		try
		{
			P t = valueForKey(key);
			return t.getValue();
		}
		catch (JeeslNotFoundException e)
		{
			if(defaultValue!=null){return defaultValue;}
			else{throw e;}
		}
	}
	
	private P valueForKey(String key) throws JeeslNotFoundException
	{
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<P> criteriaQuery = criteriaBuilder.createQuery(cProperty);
        Root<P> root = criteriaQuery.from(cProperty);
        criteriaQuery = criteriaQuery.where(root.<P>get("key").in(key));

		P result;
		TypedQuery<P> q = em.createQuery(criteriaQuery); 
		try	{result= q.getSingleResult();}
		catch (NoResultException ex){throw new JeeslNotFoundException("Nothing found "+cProperty.getSimpleName()+" for key="+key);}
		return result;
	}
}