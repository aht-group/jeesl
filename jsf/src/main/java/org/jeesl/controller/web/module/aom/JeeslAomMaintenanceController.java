package org.jeesl.controller.web.module.aom;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.module.JeeslAomFacade;
import org.jeesl.controller.handler.NullNumberBinder;
import org.jeesl.controller.util.comparator.ejb.module.aom.EjbAssetComparator;
import org.jeesl.controller.util.comparator.ejb.module.aom.EjbEventComparator;
import org.jeesl.controller.web.AbstractJeeslWebController;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.builder.module.AomFactoryBuilder;
import org.jeesl.factory.ejb.module.asset.EjbAssetEventFactory;
import org.jeesl.interfaces.bean.sb.bean.SbDateSelectionBean;
import org.jeesl.interfaces.bean.sb.bean.SbToggleBean;
import org.jeesl.interfaces.bean.sb.handler.SbDateSelection;
import org.jeesl.interfaces.bean.sb.handler.SbToggleSelection;
import org.jeesl.interfaces.cache.module.aom.JeeslAomCache;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
import org.jeesl.interfaces.model.io.cms.markup.JeeslIoMarkupType;
import org.jeesl.interfaces.model.io.cms.markup.JeeslIoMarkup;
import org.jeesl.interfaces.model.io.fr.JeeslFileContainer;
import org.jeesl.interfaces.model.module.aom.asset.JeeslAomAsset;
import org.jeesl.interfaces.model.module.aom.asset.JeeslAomAssetStatus;
import org.jeesl.interfaces.model.module.aom.asset.JeeslAomAssetType;
import org.jeesl.interfaces.model.module.aom.asset.JeeslAomView;
import org.jeesl.interfaces.model.module.aom.company.JeeslAomCompany;
import org.jeesl.interfaces.model.module.aom.company.JeeslAomScope;
import org.jeesl.interfaces.model.module.aom.event.JeeslAomEvent;
import org.jeesl.interfaces.model.module.aom.event.JeeslAomEventStatus;
import org.jeesl.interfaces.model.module.aom.event.JeeslAomEventType;
import org.jeesl.interfaces.model.module.aom.event.JeeslAomEventUpload;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.jsf.handler.sb.SbDateHandler;
import org.jeesl.jsf.handler.sb.SbMultiHandler;
import org.jeesl.jsf.handler.ui.UiSlotWidthHandler;
import org.jeesl.model.ejb.system.tenant.TenantIdentifier;
import org.jeesl.web.ui.module.aom.UiHelperAsset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;

public class JeeslAomMaintenanceController <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
										REALM extends JeeslTenantRealm<L,D,REALM,?>, RREF extends EjbWithId,
										COMPANY extends JeeslAomCompany<REALM,SCOPE>,
										SCOPE extends JeeslAomScope<L,D,SCOPE,?>,
										ASSET extends JeeslAomAsset<REALM,ASSET,COMPANY,ASTATUS,ATYPE>,
										ASTATUS extends JeeslAomAssetStatus<L,D,ASTATUS,?>,
										ATYPE extends JeeslAomAssetType<L,D,REALM,ATYPE,ALEVEL,?>,
										ALEVEL extends JeeslAomView<L,D,REALM,?>,
										EVENT extends JeeslAomEvent<COMPANY,ASSET,ETYPE,ESTATUS,M,USER,FRC>,
										ETYPE extends JeeslAomEventType<L,D,ETYPE,?>,
										ESTATUS extends JeeslAomEventStatus<L,D,ESTATUS,?>,
										M extends JeeslIoMarkup<MT>,
										MT extends JeeslIoMarkupType<L,D,MT,?>,
										USER extends JeeslSimpleUser,
										FRC extends JeeslFileContainer<?,?>,
										UP extends JeeslAomEventUpload<L,D,UP,?>>
					extends AbstractJeeslWebController<L,D,LOC>
					implements Serializable,SbToggleBean,SbDateSelectionBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslAomMaintenanceController.class);
	
	protected JeeslAomFacade<L,D,REALM,COMPANY,ASSET,ASTATUS,ATYPE,ALEVEL,EVENT,ETYPE,ESTATUS,UP> fAsset;
	
	private final AomFactoryBuilder<L,D,REALM,COMPANY,SCOPE,ASSET,ASTATUS,ATYPE,ALEVEL,EVENT,ETYPE,ESTATUS,M,MT,USER,FRC,UP> fbAsset;
	
	private final EjbAssetEventFactory<COMPANY,ASSET,EVENT,ETYPE,ESTATUS,M,MT,USER,FRC> efEvent;
	
	private final Comparator<ASSET> cpAsset;
	private final Comparator<EVENT> cpEvent;
	
	private final SbDateHandler sbDateHandler; public SbDateHandler getSbDateHandler() {return sbDateHandler;}
	private final SbMultiHandler<ESTATUS> sbhEventStatus; public SbMultiHandler<ESTATUS> getSbhEventStatus() {return sbhEventStatus;}
	private final UiSlotWidthHandler slotHandler; public UiSlotWidthHandler getSlotHandler() {return slotHandler;}
	private final UiHelperAsset<L,D,REALM,COMPANY,SCOPE,EVENT,ETYPE> uiHelper; public UiHelperAsset<L,D,REALM,COMPANY,SCOPE,EVENT,ETYPE> getUiHelper() {return uiHelper;}
	private NullNumberBinder nnb; public NullNumberBinder getNnb() {return nnb;} public void setNnb(NullNumberBinder nnb) {this.nnb = nnb;}

	private final List<EVENT> events; public List<EVENT> getEvents() {return events;}

	 private TenantIdentifier<REALM> identifier; public TenantIdentifier<REALM> getIdentifier() {return identifier;}
	private JeeslAomCacheKey<REALM,SCOPE> key; public JeeslAomCacheKey<REALM,SCOPE> getKey() {return key;}
	private REALM realm; public REALM getRealm() {return realm;}
	private RREF rref; public RREF getRref() {return rref;}
	private MT markupType;
	
	private EVENT event; public EVENT getEvent() {return event;} public void setEvent(EVENT event) {this.event = event;}

	public JeeslAomMaintenanceController(AomFactoryBuilder<L,D,REALM,COMPANY,SCOPE,ASSET,ASTATUS,ATYPE,ALEVEL,EVENT,ETYPE,ESTATUS,M,MT,USER,FRC,UP> fbAsset)
	{
		super(fbAsset.getClassL(),fbAsset.getClassD());
		this.fbAsset=fbAsset;
		
		uiHelper = new UiHelperAsset<>();
		
		efEvent = fbAsset.ejbEvent();
		
		cpAsset = fbAsset.cpAsset(EjbAssetComparator.Type.position);
		cpEvent = fbAsset.cpEvent(EjbEventComparator.Type.recordAsc);
		
		slotHandler = new UiSlotWidthHandler();
		slotHandler.set(12);
		
		sbhEventStatus = new SbMultiHandler<>(fbAsset.getClassEventStatus(),this);
		sbDateHandler = SbDateHandler.instance(this);
		sbDateHandler.initWeeks(0,4);
		
		events = new ArrayList<>();
	}
	
	public <E extends Enum<E>> void postConstructAssetMaintenance(JeeslLocaleProvider<LOC> lp, JeeslFacesMessageBean bMessage,
									JeeslAomFacade<L,D,REALM,COMPANY,ASSET,ASTATUS,ATYPE,ALEVEL,EVENT,ETYPE,ESTATUS,UP> fAsset,
									JeeslAomCache<REALM,COMPANY,SCOPE,ATYPE,ALEVEL,ETYPE> cache,
									E eRealm, RREF rref
									)
	{
		super.postConstructWebController(lp,bMessage);
		this.fAsset=fAsset;
		
		uiHelper.setCacheBean(cache);
		
		markupType = fAsset.fByEnum(fbAsset.getClassMarkupType(),JeeslIoMarkupType.Code.xhtml);
		realm = fAsset.fByEnum(fbAsset.getClassRealm(),eRealm);
		
		identifier = TenantIdentifier.instance(realm);
		key.update(identifier,cache.getScopes());
		this.rref=rref;
		
		sbhEventStatus.setList(fAsset.all(fbAsset.getClassEventStatus()));
		sbhEventStatus.preSelect(JeeslAomEventStatus.Code.planned);
		sbhEventStatus.preSelect(JeeslAomEventStatus.Code.date);
		sbhEventStatus.preSelect(JeeslAomEventStatus.Code.postponed);
		
		reloadEvents();
	}
	
	@Override public void toggled(SbToggleSelection handler, Class<?> c){reloadEvents();}
	@Override public void callbackDateChanged(SbDateSelection handler) {reloadEvents();}
	
	private void reloadEvents()
	{
		events.clear();
		events.addAll(fAsset.fAssetEvents(realm, rref, sbhEventStatus.getSelected()));
		Collections.sort(events,cpEvent);
	}
        
    public void selectEvent()
    {
    	logger.info(AbstractLogMessage.selectEntity(event));
    	event = fAsset.load(event);
    	uiHelper.update(key,event);
    	efEvent.ejb2nnb(event,nnb);
    	Collections.sort(event.getAssets(),cpAsset);
    	slotHandler.set(8,4);
    }
    
    public void cloneEvent()
    {
    	efEvent.converter(fAsset,event);
    	event = efEvent.clone(event,markupType);
    	efEvent.ejb2nnb(event,nnb);
    	uiHelper.update(key,event);
    }
    
    public void saveEvent() throws JeeslConstraintViolationException, JeeslLockingException
    {
    	logger.info(AbstractLogMessage.saveEntity(event));
    	efEvent.converter(fAsset,event);
    	efEvent.nnb2ejb(event,nnb);
    	event = fAsset.save(event);
    	reloadEvents();
    }
    
    public void cancelEvent()
    {
    	event=null;
    	slotHandler.set(12);
    }
}