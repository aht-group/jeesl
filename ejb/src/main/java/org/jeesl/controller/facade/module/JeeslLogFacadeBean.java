package org.jeesl.controller.facade.module;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.jeesl.api.facade.module.JeeslLogFacade;
import org.jeesl.factory.builder.module.LogFactoryBuilder;
import org.jeesl.interfaces.model.module.log.JeeslLogBook;
import org.jeesl.interfaces.model.module.log.JeeslLogImpact;
import org.jeesl.interfaces.model.module.log.JeeslLogItem;
import org.jeesl.interfaces.model.module.log.JeeslLogScope;
import org.jeesl.interfaces.model.module.log.JeeslLogConfidentiality;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.controller.facade.UtilsFacadeBean;
import net.sf.ahtutils.interfaces.model.status.UtilsDescription;
import net.sf.ahtutils.interfaces.model.status.UtilsLang;
import net.sf.ahtutils.model.interfaces.with.EjbWithId;

public class JeeslLogFacadeBean<L extends UtilsLang, D extends UtilsDescription,
									LOG extends JeeslLogBook<SCOPE,ITEM>,
									SCOPE extends JeeslLogScope<L,D,SCOPE,?>,
									ITEM extends JeeslLogItem<L,D,?,?,LOG,IMPACT,CONF,USER>,
									IMPACT extends JeeslLogImpact<L,D,IMPACT,?>,
									CONF extends JeeslLogConfidentiality<L,D,CONF,?>,
									USER extends EjbWithId
									>
					extends UtilsFacadeBean
					implements JeeslLogFacade<L,D,LOG,SCOPE,ITEM,IMPACT,CONF,USER>
{	
	private static final long serialVersionUID = 1L;

	final static Logger logger = LoggerFactory.getLogger(JeeslLogFacadeBean.class);
	
	private final LogFactoryBuilder<L,D,LOG,SCOPE,ITEM,IMPACT,CONF,USER> fbLog;
	
	public JeeslLogFacadeBean(EntityManager em, final LogFactoryBuilder<L,D,LOG,SCOPE,ITEM,IMPACT,CONF,USER> fbLog)
	{
		super(em);
		this.fbLog=fbLog;
	}
	
	@Override
	public List<ITEM> fLogItems(List<LOG> logs)
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<ITEM> cQ = cB.createQuery(fbLog.getClassItem());
		Root<ITEM> item = cQ.from(fbLog.getClassItem());
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		Join<ITEM,LOG> jLog = item.join(JeeslLogItem.Attributes.log.toString());
		
		predicates.add(jLog.in(logs));
		
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.select(item);

		TypedQuery<ITEM> tQ = em.createQuery(cQ);
		return tQ.getResultList();
	}
}