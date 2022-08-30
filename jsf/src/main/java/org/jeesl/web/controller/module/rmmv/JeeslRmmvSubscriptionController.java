package org.jeesl.web.controller.module.rmmv;

import java.util.ArrayList;
import java.util.List;

import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.module.JeeslRmmvFacade;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.builder.module.RmmvFactoryBuilder;
import org.jeesl.interfaces.bean.sb.bean.SbSingleBean;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
import org.jeesl.interfaces.controller.web.module.rmmv.JeeslRmmvSubscriptionCallback;
import org.jeesl.interfaces.model.module.rmmv.JeeslRmmvModuleConfig;
import org.jeesl.interfaces.model.module.rmmv.JeeslRmmvSubscription;
import org.jeesl.interfaces.model.module.rmmv.JeeslRmmvSubscriptionItem;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.web.AbstractJeeslWebController;
import org.primefaces.model.DefaultTreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslRmmvSubscriptionController <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
											R extends JeeslTenantRealm<L,D,R,?>, RREF extends EjbWithId,
											MC extends JeeslRmmvModuleConfig<?,?>,
											SUB extends JeeslRmmvSubscription<R,USER>,
											SI extends JeeslRmmvSubscriptionItem<SUB,MC>,
											USER extends EjbWithId>
		extends AbstractJeeslWebController<L,D,LOC>
		implements SbSingleBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslRmmvSubscriptionController.class);
	
	private JeeslRmmvFacade<L,D,R,?,?,?,MC> fRmmv;
	
	@SuppressWarnings("unused")
	private final JeeslRmmvSubscriptionCallback callback;

	private final RmmvFactoryBuilder<L,D,LOC,R,?,?,?,MC,?,?,?> fbRmmv;

	private final List<SUB> subscriptions; public List<SUB> getSubscriptions() {return subscriptions;}

	private R realm;
	private RREF rref;
	private SUB subscription; public SUB getSubscription() {return subscription;} public void setSubscription(SUB subscription) {this.subscription = subscription;}

	public JeeslRmmvSubscriptionController(final JeeslRmmvSubscriptionCallback callback,
											final RmmvFactoryBuilder<L,D,LOC,R,?,?,?,MC,?,?,?> fbRmmv)
	{
		super(fbRmmv.getClassL(),fbRmmv.getClassD());
		this.callback=callback;
		this.fbRmmv=fbRmmv;
		
		subscriptions = new ArrayList<>();
	}
	
	public void postConstructSubscription(JeeslRmmvFacade<L,D,R,?,?,?,MC> fRmmv,
									JeeslLocaleProvider<LOC> lp, JeeslFacesMessageBean bMessage,
									R realm)
	{
		super.postConstructWebController(lp);
		this.fRmmv=fRmmv;
		this.realm=realm;
	}
	
	public void updateRealmReference(RREF rref, USER user)
	{
		this.rref=rref;
		
		subscriptions.clear();
	}
	
	@Override public void selectSbSingle(EjbWithId item) throws JeeslLockingException, JeeslConstraintViolationException
	{
		// TODO Auto-generated method stub
	}
	
	private void reset(boolean rSubscription)
	{
		if(rSubscription) {subscription=null;}
	}

	public void addSubscription()
	{
		this.reset(true);
		
	}	
}