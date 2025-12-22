package org.jeesl.controller.facade.jx.system;

import java.util.Date;
import java.util.Objects;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.jeesl.api.facade.system.JeeslSystemPropertyFacade;
import org.jeesl.controller.facade.jx.JeeslFacadeBean;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.system.property.JeeslProperty;

public class JeeslSystemPropertyFacadeBean<L extends JeeslLang,D extends JeeslDescription,
											C extends JeeslStatus<L,D,C>,
											P extends JeeslProperty<L,D,C,P>>
					extends JeeslFacadeBean
					implements JeeslSystemPropertyFacade<L,D,C,P>
{	
	private static final long serialVersionUID = 1L;
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
			return Integer.valueOf(t.getValue());
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

	@Override public Boolean systemPropertyBoolean(String key, Boolean defaultValue)
	{
		try
		{
			P t = valueForKey(key);
			return Boolean.valueOf(t.getValue());
		}
		catch (JeeslNotFoundException e)
		{
			if(Objects.nonNull(defaultValue)) {return defaultValue;}
			else{return null;}
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