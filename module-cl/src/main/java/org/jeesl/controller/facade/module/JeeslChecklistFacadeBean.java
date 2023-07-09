package org.jeesl.controller.facade.module;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.ObjectUtils;
import org.jeesl.api.facade.module.JeeslChecklistFacade;
import org.jeesl.controller.facade.jx.JeeslFacadeBean;
import org.jeesl.factory.builder.module.ChecklistFactoryBuilder;
import org.jeesl.interfaces.model.module.cl.JeeslClCheckItem;
import org.jeesl.interfaces.model.module.cl.JeeslClChecklist;
import org.jeesl.interfaces.model.module.cl.JeeslClTracklist;
import org.jeesl.interfaces.util.query.module.EjbChecklistQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslChecklistFacadeBean<CL extends JeeslClChecklist<?,?,?>,
									CI extends JeeslClCheckItem<?,CL,?>,
									TL extends JeeslClTracklist<?,?,CL>
>
					extends JeeslFacadeBean
					implements JeeslChecklistFacade<CL,CI,TL>
{	
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslChecklistFacadeBean.class);

	private final ChecklistFactoryBuilder<?,?,?,?,CL,CI,TL,?,?,?,?> fbCl;
	
	public JeeslChecklistFacadeBean(EntityManager em, ChecklistFactoryBuilder<?,?,?,?,CL,CI,TL,?,?,?,?> fbCl)
	{
		super(em);
		this.fbCl=fbCl;
	}

	@Override public TL load(TL tracklist)
	{
		tracklist = em.find(fbCl.getClassTrackList(), tracklist.getId());
		tracklist.getChecklists().size();
		return tracklist;
	}

	@Override public List<CI> fCheckItems(EjbChecklistQuery<CL,CI,TL> query)
	{
		if(ObjectUtils.allNull(query.getCheckLists())) {return new ArrayList<>();}
		
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<CI> cQ = cB.createQuery(fbCl.getClassCheckItem());
		Root<CI> root = cQ.from(fbCl.getClassCheckItem());
		
		
		if(ObjectUtils.isNotEmpty(query.getCheckLists()))
		{
			Join<CI,CL> jChecklist = root.join(JeeslClCheckItem.Attributes.checklist.toString());
			predicates.add(jChecklist.in(query.getCheckLists()));
		}
		
		cQ.select(root);	    
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		
		return em.createQuery(cQ).getResultList();
	}
	
}