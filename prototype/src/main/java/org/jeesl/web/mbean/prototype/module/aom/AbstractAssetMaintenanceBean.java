package org.jeesl.web.mbean.prototype.module.aom;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.jeesl.api.bean.JeeslTranslationBean;
import org.jeesl.api.bean.module.aom.JeeslAssetCacheBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.module.JeeslAssetFacade;
import org.jeesl.api.handler.sb.SbDateIntervalSelection;
import org.jeesl.controller.handler.sb.SbDateIntervalHandler;
import org.jeesl.controller.handler.sb.SbMultiHandler;
import org.jeesl.controller.handler.ui.UiSlotWidthHandler;
import org.jeesl.controller.handler.ui.helper.UiHelperAsset;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.builder.module.AomFactoryBuilder;
import org.jeesl.factory.ejb.module.asset.EjbAssetEventFactory;
import org.jeesl.interfaces.bean.sb.SbToggleBean;
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
import org.jeesl.util.comparator.ejb.module.asset.EjbAssetComparator;
import org.jeesl.util.comparator.ejb.module.asset.EjbEventComparator;
import org.jeesl.web.mbean.prototype.system.AbstractAdminBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;

public abstract class AbstractAssetMaintenanceBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
										REALM extends JeeslTenantRealm<L,D,REALM,?>, RREF extends EjbWithId,
										COMPANY extends JeeslAomCompany<REALM,SCOPE>,
										SCOPE extends JeeslAomScope<L,D,SCOPE,?>,
										ASSET extends JeeslAomAsset<REALM,ASSET,COMPANY,ASTATUS,ATYPE>,
										ASTATUS extends JeeslAomAssetStatus<L,D,ASTATUS,?>,
										ATYPE extends JeeslAomAssetType<L,D,REALM,ATYPE,ALEVEL,?>,
										ALEVEL extends JeeslAomView<L,D,REALM,?>,
										EVENT extends JeeslAomEvent<COMPANY,ASSET,ETYPE,ESTATUS,USER,FRC>,
										ETYPE extends JeeslAomEventType<L,D,ETYPE,?>,
										ESTATUS extends JeeslAomEventStatus<L,D,ESTATUS,?>,
										USER extends JeeslSimpleUser,
										FRC extends JeeslFileContainer<?,?>,
										UP extends JeeslAomEventUpload<L,D,UP,?>>
					extends AbstractAdminBean<L,D,LOC>
					implements Serializable,SbToggleBean,SbDateIntervalSelection
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractAssetMaintenanceBean.class);
	
	protected JeeslAssetFacade<L,D,REALM,COMPANY,SCOPE,ASSET,ASTATUS,ATYPE,ALEVEL,EVENT,ETYPE,ESTATUS,USER,FRC,UP> fAsset;
	
	private final AomFactoryBuilder<L,D,REALM,COMPANY,SCOPE,ASSET,ASTATUS,ATYPE,ALEVEL,EVENT,ETYPE,ESTATUS,USER,FRC,UP> fbAsset;
	
	private final EjbAssetEventFactory<COMPANY,ASSET,EVENT,ETYPE,ESTATUS,USER,FRC> efEvent;
	
	private final Comparator<ASSET> cpAsset;
	private final Comparator<EVENT> cpEvent;
	
	private SbDateIntervalHandler sbDateHandler; public SbDateIntervalHandler getSbDateHandler() {return sbDateHandler;}
	private final SbMultiHandler<ESTATUS> sbhEventStatus; public SbMultiHandler<ESTATUS> getSbhEventStatus() {return sbhEventStatus;}
	private final UiSlotWidthHandler slotHandler; public UiSlotWidthHandler getSlotHandler() {return slotHandler;}
	private final UiHelperAsset<L,D,REALM,RREF,COMPANY,SCOPE,ASSET,ASTATUS,ATYPE,ALEVEL,EVENT,ETYPE,ESTATUS,USER,FRC,UP> uiHelper; public UiHelperAsset<L,D,REALM,RREF,COMPANY,SCOPE,ASSET,ASTATUS,ATYPE,ALEVEL,EVENT,ETYPE,ESTATUS,USER,FRC,UP> getUiHelper() {return uiHelper;}
	
	private final List<EVENT> events; public List<EVENT> getEvents() {return events;}
	    
	private REALM realm; public REALM getRealm() {return realm;}
	private RREF rref; public RREF getRref() {return rref;}

	private EVENT event; public EVENT getEvent() {return event;} public void setEvent(EVENT event) {this.event = event;}

	public AbstractAssetMaintenanceBean(AomFactoryBuilder<L,D,REALM,COMPANY,SCOPE,ASSET,ASTATUS,ATYPE,ALEVEL,EVENT,ETYPE,ESTATUS,USER,FRC,UP> fbAsset)
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
		sbDateHandler = new SbDateIntervalHandler(this);
		sbDateHandler.setEnforceStartOfDay(true);
		sbDateHandler.initWeeks(0,4);
		
		events = new ArrayList<>();
	}
	
	protected <E extends Enum<E>> void postConstructAssetMaintenance(JeeslTranslationBean<L,D,LOC> bTranslation, JeeslFacesMessageBean bMessage,
									JeeslAssetFacade<L,D,REALM,COMPANY,SCOPE,ASSET,ASTATUS,ATYPE,ALEVEL,EVENT,ETYPE,ESTATUS,USER,FRC,UP> fAsset,
									JeeslAssetCacheBean<L,D,REALM,RREF,COMPANY,SCOPE,ASSET,ASTATUS,ATYPE,ALEVEL,ETYPE,UP> bCache,
									E eRealm, RREF rref
									)
	{
		super.initJeeslAdmin(bTranslation,bMessage);
		this.fAsset=fAsset;
		
		uiHelper.setCacheBean(bCache);
		
		realm = fAsset.fByEnum(fbAsset.getClassRealm(),eRealm);
		this.rref=rref;
		
		sbhEventStatus.setList(fAsset.all(fbAsset.getClassEventStatus()));
		sbhEventStatus.preSelect(JeeslAomEventStatus.Code.planned);
		sbhEventStatus.preSelect(JeeslAomEventStatus.Code.date);
		sbhEventStatus.preSelect(JeeslAomEventStatus.Code.postponed);
		
		reloadEvents();
	}
	
	@Override public void toggled(Class<?> c){reloadEvents();}
	@Override public void callbackDateChanged(){reloadEvents();}
	
	private void reloadEvents()
	{
		events.clear();
		events.addAll(fAsset.fAssetEvents(realm, rref, sbhEventStatus.getSelected()));
		Collections.sort(events,cpEvent);
	}
        
    public void selectEvent()
    {
    	logger.info(AbstractLogMessage.selectEntity(event));
    	event = fAsset.find(fbAsset.getClassEvent(),event);
    	uiHelper.update(realm,rref,event);
    	efEvent.ejb2nnb(event,nnb);
    	Collections.sort(event.getAssets(),cpAsset);
    	slotHandler.set(8,4);
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