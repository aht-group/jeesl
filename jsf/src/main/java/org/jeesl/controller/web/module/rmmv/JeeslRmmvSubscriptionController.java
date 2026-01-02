package org.jeesl.controller.web.module.rmmv;

import java.util.ArrayList;
import java.util.List;

import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.module.JeeslRmmvFacade;
import org.jeesl.controller.web.AbstractJeeslLocaleWebController;
import org.jeesl.controller.web.util.AbstractLogMessage;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.builder.module.RmmvFactoryBuilder;
import org.jeesl.interfaces.bean.sb.bean.SbSingleBean;
import org.jeesl.interfaces.bean.sb.handler.SbSingleSelection;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
import org.jeesl.interfaces.controller.web.module.rmmv.JeeslRmmvSubscriptionCallback;
import org.jeesl.interfaces.model.module.rmmv.JeeslRmmvModule;
import org.jeesl.interfaces.model.module.rmmv.JeeslRmmvModuleConfig;
import org.jeesl.interfaces.model.module.rmmv.JeeslRmmvSubscription;
import org.jeesl.interfaces.model.module.rmmv.JeeslRmmvSubscriptionItem;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.jsf.handler.sb.SbSingleHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslRmmvSubscriptionController <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
											R extends JeeslTenantRealm<L,D,R,?>, RREF extends EjbWithId,
											MOD extends JeeslRmmvModule<L,D,MOD,?>,
											MC extends JeeslRmmvModuleConfig<?,MOD>,
											SUB extends JeeslRmmvSubscription<R,MOD,USER>,
											SI extends JeeslRmmvSubscriptionItem<SUB,MC>,
											USER extends EjbWithId>
		extends AbstractJeeslLocaleWebController<L,D,LOC>
		implements SbSingleBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslRmmvSubscriptionController.class);
	
	private JeeslRmmvFacade<L,D,R,?,?,MOD,MC,SUB,SI,USER> fRmmv;
	
	@SuppressWarnings("unused")
	private final JeeslRmmvSubscriptionCallback callback;

	private final RmmvFactoryBuilder<L,D,LOC,R,?,?,MOD,MC,SUB,SI,USER> fbRmmv;

	protected final SbSingleHandler<LOC> sbhLocale; public SbSingleHandler<LOC> getSbhLocale() {return sbhLocale;}
	protected final SbSingleHandler<MOD> sbhModule; public SbSingleHandler<MOD> getSbhModule() {return sbhModule;}
	
	private final List<MC> configs; public List<MC> getConfigs() {return configs;}
	private final List<SUB> subscriptions; public List<SUB> getSubscriptions() {return subscriptions;}
	private final List<SI> items; public List<SI> getItems() {return items;}

	private R realm;
	private RREF rref;
	private USER user;
	private SUB subscription; public SUB getSubscription() {return subscription;} public void setSubscription(SUB subscription) {this.subscription = subscription;}
	private SI item; public SI getItem() {return item;} public void setItem(SI item) {this.item = item;}
	
	public JeeslRmmvSubscriptionController(final JeeslRmmvSubscriptionCallback callback,
											final RmmvFactoryBuilder<L,D,LOC,R,?,?,MOD,MC,SUB,SI,USER> fbRmmv)
	{
		super(fbRmmv.getClassL(),fbRmmv.getClassD());
		this.callback=callback;
		this.fbRmmv=fbRmmv;
		
		sbhLocale = new SbSingleHandler<>(fbRmmv.getClassLocale(),this);
		sbhModule = new SbSingleHandler<>(fbRmmv.getClassModule(),this);
		
		subscriptions = new ArrayList<>();
		items = new ArrayList<>();
		configs = new ArrayList<>();
	}
	
	public void postConstructSubscription(JeeslRmmvFacade<L,D,R,?,?,MOD,MC,SUB,SI,USER> fRmmv,
									JeeslLocaleProvider<LOC> lp, JeeslFacesMessageBean bMessage,
									R realm)
	{
		super.postConstructLocaleWebController(lp,bMessage);
		this.fRmmv=fRmmv;
		this.realm=realm;
		
		sbhLocale.setList(lp.getLocales());
		sbhLocale.setDefault();
		
		sbhModule.add(fRmmv, fbRmmv.getClassModule(),JeeslRmmvModule.Code.calendar);
		sbhModule.setDefault();
	}
	
	public void updateRealmReference(RREF rref, USER user)
	{
		this.rref=rref;
		this.user=user;
		
		configs.addAll(fRmmv.fRmmvConfigs(realm,rref,sbhModule.getSelection()));
		logger.info("Configs "+configs.size());
		
		this.reset(true,true,true,true);
		
		this.reloadSubscriptions();
	}
	
	@Override public void selectSbSingle(SbSingleSelection handler, EjbWithId item) throws JeeslLockingException, JeeslConstraintViolationException
	{
		// TODO Auto-generated method stub
	}
	
	private void reset(boolean rSubscriptions, boolean rSubscription, boolean rItems, boolean rItem)
	{
		if(rSubscriptions) {subscriptions.clear();}
		if(rSubscription) {subscription=null;}
		if(rItems) {items.clear();}
		if(rItem) {item=null;}
	}
	
	private void reloadSubscriptions()
	{
		this.reset(true,false,false,false);
		subscriptions.addAll(fRmmv.fRmmvSubscriptions(realm, rref, sbhModule.getSelection(), user));
	}

	public void addSubscription()
	{
		this.reset(false,true,true,true);
		subscription = fbRmmv.ejbSubscription().build(realm, rref, sbhModule.getSelection(), user);
	}
	
	public void saveSubscription() throws JeeslConstraintViolationException, JeeslLockingException
	{
		logger.info(AbstractLogMessage.saveEntity(subscription));
		subscription = fRmmv.save(subscription);
		this.reloadSubscriptions();
	}	
	
	public void selectSubscription()
	{
		logger.info(AbstractLogMessage.selectEntity(subscription));
		reloadItems();
	}
	
	public void deleteSubscription() throws JeeslConstraintViolationException, JeeslLockingException
	{
		logger.info(AbstractLogMessage.saveEntity(subscription));
		fRmmv.rm(subscription);
		this.reset(false, true, true, true);
		this.reloadSubscriptions();
	}
	
	private void reloadItems()
	{
		this.reset(false, false, true, false);
		items.addAll(fRmmv.allForParent(fbRmmv.getClassSubscriptionItem(),subscription));
	}
	
	public void addItem()
	{
		this.reset(false, false, false, true);
		item = fbRmmv.ejbSubscriptionItem().build(subscription, items);
		if(!configs.isEmpty()) {item.setConfig(configs.get(0));}
	}
	
	public void saveItem() throws JeeslConstraintViolationException, JeeslLockingException
	{
		logger.info(AbstractLogMessage.saveEntity(item));
		item = fRmmv.save(item);
		this.reloadItems();
	}
	
	public void selectItem()
	{
		logger.info(AbstractLogMessage.selectEntity(item));
	}
	
	public void deleteItem() throws JeeslConstraintViolationException, JeeslLockingException
	{
		logger.info(AbstractLogMessage.deleteEntity(item));
		fRmmv.rm(item);
		this.reset(false, false, false, true);
		this.reloadItems();
	}
}