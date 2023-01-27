package org.jeesl.controller.facade.module;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.jeesl.api.facade.module.JeeslRmmvFacade;
import org.jeesl.controller.facade.JeeslFacadeBean;
import org.jeesl.factory.builder.module.RmmvFactoryBuilder;
import org.jeesl.interfaces.model.module.rmmv.JeeslRmmvClassification;
import org.jeesl.interfaces.model.module.rmmv.JeeslRmmvElement;
import org.jeesl.interfaces.model.module.rmmv.JeeslRmmvModule;
import org.jeesl.interfaces.model.module.rmmv.JeeslRmmvModuleConfig;
import org.jeesl.interfaces.model.module.rmmv.JeeslRmmvSubscription;
import org.jeesl.interfaces.model.module.rmmv.JeeslRmmvSubscriptionItem;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.system.tenant.JeeslWithTenantSupport;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslRmmvFacadeBean<L extends JeeslLang, D extends JeeslDescription,
									R extends JeeslTenantRealm<L,D,R,?>, 
									E extends JeeslRmmvElement<L,R,E,EC>,
									EC extends JeeslRmmvClassification<L,R,EC,?>,
									MOD extends JeeslRmmvModule<L,D,MOD,?>,
									MC extends JeeslRmmvModuleConfig<E,MOD>,
									SUB extends JeeslRmmvSubscription<R,MOD,USER>,
									SI extends JeeslRmmvSubscriptionItem<SUB,MC>,
									USER extends EjbWithId>
					extends JeeslFacadeBean
					implements JeeslRmmvFacade<L,D,R,E,EC,MOD,MC,SUB,SI,USER>
{	
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslRmmvFacadeBean.class);

	private final RmmvFactoryBuilder<L,D,?,R,E,EC,MOD,MC,SUB,SI,USER> fbRmmv;
	
	public JeeslRmmvFacadeBean(EntityManager em, RmmvFactoryBuilder<L,D,?,R,E,EC,MOD,MC,SUB,SI,USER> fbRmmv)
	{
		super(em);
		this.fbRmmv=fbRmmv;
	}

	@Override
	public <RREF extends EjbWithId> List<MC> fRmmvConfigs(R realm, RREF rref, MOD module)
	{
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<MC> cQ = cB.createQuery(fbRmmv.getClassConfig());
		Root<MC> root = cQ.from(fbRmmv.getClassConfig());

		Join<MC,E> jElement = root.join(JeeslRmmvModuleConfig.Attributes.element.toString());
		Path<R> pRealm = jElement.get(JeeslWithTenantSupport.Attributes.realm.toString());
		Expression<Long> eRref = jElement.get(JeeslWithTenantSupport.Attributes.rref.toString());
		predicates.add(cB.and(cB.equal(pRealm,realm),cB.equal(eRref,rref.getId())));
		
		Path<R> pModule = root.get(JeeslRmmvModuleConfig.Attributes.module.toString());
		predicates.add(cB.equal(pModule,module));
	    
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.select(root);
		
		return em.createQuery(cQ).getResultList();
	}

	@Override
	public <RREF extends EjbWithId> List<SUB> fRmmvSubscriptions(R realm, RREF rref, MOD module, USER user)
	{
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<SUB> cQ = cB.createQuery(fbRmmv.getClassSubscription());
		Root<SUB> subscription = cQ.from(fbRmmv.getClassSubscription());

		Path<R> pRealm = subscription.get(JeeslWithTenantSupport.Attributes.realm.toString());
		Expression<Long> eRref = subscription.get(JeeslWithTenantSupport.Attributes.rref.toString());
		Path<MOD> pModule = subscription.join(JeeslRmmvSubscription.Attributes.module.toString());
		Path<USER> pUser = subscription.join(JeeslRmmvSubscription.Attributes.user.toString());
		
		predicates.add(cB.and(cB.equal(pRealm,realm),cB.equal(eRref,rref.getId())));
		predicates.add(cB.equal(pModule,module));
		predicates.add(cB.equal(pUser,user));
	    
		cQ.select(subscription);
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		
		
		return em.createQuery(cQ).getResultList();
	}
}