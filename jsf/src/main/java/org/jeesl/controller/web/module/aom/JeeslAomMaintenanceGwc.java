package org.jeesl.controller.web.module.aom;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.exlp.util.system.DateUtil;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.module.JeeslAomFacade;
import org.jeesl.controller.handler.NullNumberBinder;
import org.jeesl.controller.util.comparator.ejb.module.aom.EjbAssetComparator;
import org.jeesl.controller.util.comparator.ejb.module.aom.EjbEventComparator;
import org.jeesl.controller.web.AbstractJeeslLocaleWebController;
import org.jeesl.controller.web.util.AbstractLogMessage;
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
import org.jeesl.interfaces.model.io.cms.css.JeeslIoCmsCssStyle;
import org.jeesl.interfaces.model.io.cms.markup.JeeslIoMarkup;
import org.jeesl.interfaces.model.io.cms.markup.JeeslIoMarkupType;
import org.jeesl.interfaces.model.io.fr.JeeslFileContainer;
import org.jeesl.interfaces.model.io.fr.JeeslFileMeta;
import org.jeesl.interfaces.model.io.fr.JeeslFileStorage;
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
import org.jeesl.interfaces.model.system.security.user.JeeslSecurityUser;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.jsf.handler.sb.SbDateHandler;
import org.jeesl.jsf.handler.sb.SbMultiHandler;
import org.jeesl.jsf.handler.ui.UiSlotWidthHandler;
import org.jeesl.model.ejb.system.tenant.TenantIdentifier;
import org.jeesl.util.db.cache.EjbCodeCache;
import org.jeesl.util.query.cq.CqDate;
import org.jeesl.util.query.cq.CqLiteral;
import org.jeesl.util.query.cq.CqOrdering;
import org.jeesl.util.query.ejb.io.EjbIoFrQuery;
import org.jeesl.util.query.ejb.module.EjbAomQuery;
import org.jeesl.web.ui.module.aom.UiHelperAsset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslAomMaintenanceGwc <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
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
										CSS extends JeeslIoCmsCssStyle<L,D,CSS,?>,
										USER extends JeeslSecurityUser,
										FRS extends JeeslFileStorage<L,D,?,?,?>,
										FRC extends JeeslFileContainer<?,?>,
										FRM extends JeeslFileMeta<D,FRC,?,?>,
										UP extends JeeslAomEventUpload<L,D,UP,?>>
					extends AbstractJeeslLocaleWebController<L,D,LOC>
					implements Serializable,SbToggleBean,SbDateSelectionBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslAomMaintenanceGwc.class);
	
	protected JeeslAomFacade<L,D,REALM,COMPANY,SCOPE,ASSET,ASTATUS,ATYPE,ALEVEL,EVENT,ESTATUS> fAsset;
	
	private final AomFactoryBuilder<L,D,REALM,COMPANY,SCOPE,ASSET,ASTATUS,ATYPE,ALEVEL,EVENT,ETYPE,ESTATUS,M,MT,USER,FRC,UP> fbAsset;
	
	private final EjbAssetEventFactory<COMPANY,ASSET,EVENT,ETYPE,ESTATUS,M,MT> efEvent;
	
	private final Comparator<ASSET> cpAsset;
	private final Comparator<EVENT> cpEvent;
	
	private final SbDateHandler sbDateHandler; public SbDateHandler getSbDateHandler() {return sbDateHandler;}
	private final SbMultiHandler<ESTATUS> sbhEventStatus; public SbMultiHandler<ESTATUS> getSbhEventStatus() {return sbhEventStatus;}
	private final UiSlotWidthHandler slotHandler; public UiSlotWidthHandler getSlotHandler() {return slotHandler;}
	private final UiHelperAsset<L,D,REALM,COMPANY,SCOPE,EVENT,ETYPE> uiHelper; public UiHelperAsset<L,D,REALM,COMPANY,SCOPE,EVENT,ETYPE> getUiHelper() {return uiHelper;}
	private NullNumberBinder nnb; public NullNumberBinder getNnb() {return nnb;} public void setNnb(NullNumberBinder nnb) {this.nnb = nnb;}

	private final EjbCodeCache<CSS> cacheStyle;
	
	private final Map<EVENT,String> mapCss; public Map<EVENT,String> getMapCss() {return mapCss;}
	
	private final List<EVENT> events; public List<EVENT> getEvents() {return events;}
	private final List<EVENT> history; public List<EVENT> getHistory() {return history;}

	private TenantIdentifier<REALM> identifier; public TenantIdentifier<REALM> getIdentifier() {return identifier;}
	private final JeeslAomCacheKey<REALM,SCOPE> key; public JeeslAomCacheKey<REALM,SCOPE> getKey() {return key;}
	
	private TenantIdentifier<REALM> tenant;
	private REALM realm; public REALM getRealm() {return realm;}
	private RREF rref; public RREF getRref() {return rref;}
	private MT markupType;
	private FRM preview; public FRM getPreview() {return preview;}
	
	private EVENT event; public EVENT getEvent() {return event;} public void setEvent(EVENT event) {this.event = event;}

	public JeeslAomMaintenanceGwc(AomFactoryBuilder<L,D,REALM,COMPANY,SCOPE,ASSET,ASTATUS,ATYPE,ALEVEL,EVENT,ETYPE,ESTATUS,M,MT,USER,FRC,UP> fbAsset)
	{
		super(fbAsset.getClassL(),fbAsset.getClassD());
		this.fbAsset=fbAsset;
		
		uiHelper = new UiHelperAsset<>();
		nnb = new NullNumberBinder();
		
		efEvent = fbAsset.ejbEvent();
		
		cpAsset = fbAsset.cpAsset(EjbAssetComparator.Type.position);
		cpEvent = fbAsset.cpEvent(EjbEventComparator.Type.recordAsc);
		
		slotHandler = new UiSlotWidthHandler();
		slotHandler.set(12);
		
		sbhEventStatus = new SbMultiHandler<>(fbAsset.getClassEventStatus(),this);
		sbDateHandler = SbDateHandler.instance(this);
		sbDateHandler.initWeeks(0,4);
		
		cacheStyle = EjbCodeCache.instance(null);
		
		mapCss = new HashMap<>();
		
		events = new ArrayList<>();
		history = new ArrayList<>();
		key = new JeeslAomCacheKey<>();
	}
	
	public <E extends Enum<E>> void postConstructAssetMaintenance(JeeslLocaleProvider<LOC> lp, JeeslFacesMessageBean bMessage,
									JeeslAomFacade<L,D,REALM,COMPANY,SCOPE,ASSET,ASTATUS,ATYPE,ALEVEL,EVENT,ESTATUS> fAsset,
									JeeslAomCache<REALM,COMPANY,SCOPE,ASTATUS,ATYPE,ALEVEL,ETYPE> cache,
									REALM realm)
	{
		super.postConstructLocaleWebController(lp,bMessage);
		this.fAsset = fAsset;
		this.realm = realm;
		tenant = TenantIdentifier.instance(realm);
		
		uiHelper.setCacheBean(cache);
		
		markupType = fAsset.fByEnum(fbAsset.getClassMarkupType(),JeeslIoMarkupType.Code.xhtml);
		
		identifier = TenantIdentifier.instance(realm);
		key.update(identifier,cache.getScopes());
		
		sbhEventStatus.setList(fAsset.all(fbAsset.getClassEventStatus()));
		sbhEventStatus.preSelect(JeeslAomEventStatus.Code.planned);
		sbhEventStatus.preSelect(JeeslAomEventStatus.Code.date);
		sbhEventStatus.preSelect(JeeslAomEventStatus.Code.postponed);
	}
	
	public void updateRealmReference(RREF rref)
	{
		tenant.withRref(rref);
		this.rref = rref;
		this.reloadEvents();
	}
	
	@Override public void toggled(SbToggleSelection handler, Class<?> c){this.reloadEvents();}
	@Override public void callbackDateChanged(SbDateSelection handler) {this.reloadEvents();}
	
	public void cancelEvent() {this.reset(true, true); slotHandler.set(12);}
	private void reset(boolean rEvent, boolean rPreview)
	{
		if(rEvent) {event=null;}
		if(rPreview) {preview=null;}
	}
	
	private void reloadEvents()
	{
		events.clear();
		mapCss.clear();

		EjbAomQuery<REALM,SCOPE,ASSET,ATYPE,EVENT,ESTATUS> query = new EjbAomQuery<>();
		query.tenant(tenant);
		query.addAomEventStatus(sbhEventStatus.getSelected());
		query.addCqDate(CqDate.isBeforeOrAt(sbDateHandler.getDateTo(), CqDate.path(JeeslAomEvent.Attributes.record)));
		
		events.addAll(fAsset.fAomEvents(query));
		Collections.sort(events,cpEvent);
		
		LocalDate now = LocalDate.now();
		for(EVENT e : events)
		{
			if(DateUtil.toLocalDate(e.getRecord()).isBefore(now))
			{
				mapCss.put(e,"jeesl-red");
			}
		}		
	}
        
    public void selectEvent()
    {
    	this.reset(false,true);
    	history.clear();
    	
    	logger.info(AbstractLogMessage.selectEntity(event));
    	event = fAsset.load(event);
    	uiHelper.update(key,event);
    	efEvent.ejb2nnb(event,nnb);
    	Collections.sort(event.getAssets(),cpAsset);
    	slotHandler.set(8,4);
    	
    	EjbAomQuery<REALM,SCOPE,ASSET,ATYPE,EVENT,ESTATUS> query = new EjbAomQuery<>();
		query.addAssets(event.getAssets());
		query.add(CqOrdering.desending(CqOrdering.path(JeeslAomEvent.Attributes.record)));
		
    	history.addAll(fAsset.fAomEvents(query));
    	logger.info(fbAsset.getClassEvent().getSimpleName()+": "+history.size());
    	
    	this.reloadPreview();
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
    
    private void reloadPreview()
	{
		preview=null;

//		if(ObjectUtils.isNotEmpty(event.getFrContainer()))
//		{
//			EjbIoFrQuery<FRS,FRC> query = new EjbIoFrQuery<>();
//			query.addIoFrContainer(event.getFrContainer());
//			query.addCqLiteral(CqLiteral.exact(JeeslAomEventUpload.Code.preview,CqLiteral.path(JeeslFileMeta.Attributes.category)));
//			List<FRM> metas = fFr.fIoFrMetas(query);
//
//			if(!metas.isEmpty()) {preview = metas.get(0);}
//		}
	}
}