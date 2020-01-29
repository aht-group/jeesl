package org.jeesl.controller.facade.system;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;

import org.jeesl.api.facade.core.JeeslUserFacade;
import org.jeesl.controller.facade.JeeslFacadeBean;
import org.jeesl.factory.builder.system.SecurityFactoryBuilder;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;

public class JeeslUserFacadeBean<USER extends JeeslUser<?>>
					extends JeeslFacadeBean
					implements JeeslUserFacade<USER>
{	
	private static final long serialVersionUID = 1L;
	
	private final SecurityFactoryBuilder<?,?,?,?,?,?,?,?,?,?,?,USER> fbSecurity;
	
	public JeeslUserFacadeBean(EntityManager em, SecurityFactoryBuilder<?,?,?,?,?,?,?,?,?,?,?,USER> fbSecurity)
	{
		super(em);
		this.fbSecurity=fbSecurity;
	}
	
	@Override public USER load(USER user)
	{
		user = em.find(fbSecurity.getClassUser(), user.getId());
		user.getRoles().size();
		return user;
	}

	@Override public  List<USER> likeNameFirstLast(String query)
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
	    CriteriaQuery<USER> criteriaQuery = cB.createQuery(fbSecurity.getClassUser());
	    
	    Root<USER> fromType = criteriaQuery.from(fbSecurity.getClassUser());
	    
	    Expression<String> literal = cB.upper(cB.literal("%"+query+"%"));
	    Expression<String> eFirst = fromType.get("firstName");
	    Expression<String> eLast = fromType.get("lastName");
	    
	    CriteriaQuery<USER> select = criteriaQuery.select(fromType);
	    select.where(cB.or(cB.like(cB.upper(eFirst),literal),cB.like(cB.upper(eLast),literal)));
	    
	    TypedQuery<USER> q = em.createQuery(select);
		return q.getResultList();
	}
}