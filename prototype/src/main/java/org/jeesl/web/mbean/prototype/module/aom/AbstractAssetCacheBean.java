package org.jeesl.web.mbean.prototype.module.aom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeesl.api.facade.module.JeeslAomFacade;
import org.jeesl.controller.web.util.AbstractLogMessage;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.module.AomFactoryBuilder;
import org.jeesl.factory.ejb.util.EjbCodeFactory;
import org.jeesl.interfaces.cache.module.aom.JeeslAssetCacheBean;
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
import org.jeesl.interfaces.model.system.security.user.JeeslSecurityUser;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.model.ejb.system.tenant.TenantIdentifier;
import org.jeesl.util.db.cache.EjbCodeCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractAssetCacheBean <L extends JeeslLang, D extends JeeslDescription,
										REALM extends JeeslTenantRealm<L,D,REALM,?>, RREF extends EjbWithId,
										COMPANY extends JeeslAomCompany<REALM,SCOPE>,
										SCOPE extends JeeslAomScope<L,D,SCOPE,?>,
										ASSET extends JeeslAomAsset<REALM,ASSET,COMPANY,ASTATUS,ATYPE>,
										ASTATUS extends JeeslAomAssetStatus<L,D,ASTATUS,?>,
										ATYPE extends JeeslAomAssetType<L,D,REALM,ATYPE,VIEW,?>,
										VIEW extends JeeslAomView<L,D,REALM,?>,
										EVENT extends JeeslAomEvent<COMPANY,ASSET,ETYPE,ESTATUS,?,USER,?>,
										ETYPE extends JeeslAomEventType<L,D,ETYPE,?>,
										ESTATUS extends JeeslAomEventStatus<L,D,ESTATUS,?>,
										USER extends JeeslSecurityUser,
										UP extends JeeslAomEventUpload<L,D,UP,?>>
								implements JeeslAssetCacheBean<REALM,RREF,COMPANY,SCOPE,ASSET,ASTATUS,ATYPE,VIEW,ETYPE,UP>
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractAssetCacheBean.class);
	
	private final AomFactoryBuilder<L,D,REALM,COMPANY,SCOPE,ASSET,ASTATUS,ATYPE,VIEW,EVENT,ETYPE,ESTATUS,?,?,USER,?,UP> fbAsset;

	private EjbCodeCache<SCOPE> cacheScope;
	
	private final Map<String,UP> mapUpload; public Map<String,UP> getMapUpload() {return mapUpload;}
	private final List<UP> uploads; public List<UP> getUploads() {return uploads;}
	
	private final Map<REALM,Map<RREF,List<ATYPE>>> mapAssetType1; @Override public Map<REALM,Map<RREF,List<ATYPE>>> getMapAssetType1() {return mapAssetType1;}
	private final Map<REALM,Map<RREF,List<ATYPE>>> mapAssetType2; @Override public Map<REALM,Map<RREF,List<ATYPE>>> getMapAssetType2() {return mapAssetType2;}
	
	private final Map<RREF,List<COMPANY>> mapCompany;
	private final Map<RREF,List<COMPANY>> mapManufacturer; public Map<RREF,List<COMPANY>> getMapManufacturer() {return mapManufacturer;}
	private final Map<RREF,List<COMPANY>> mapVendor; @Override public Map<RREF,List<COMPANY>> getMapVendor() {return mapVendor;}
	private final Map<RREF,List<COMPANY>> mapMaintainer; @Override public Map<RREF,List<COMPANY>> getMapMaintainer() {return mapMaintainer;}
	
	private final List<ASTATUS> assetStatus; public List<ASTATUS> getAssetStatus() {return assetStatus;}
    private final List<VIEW> assetLevel; public List<VIEW> getAssetLevel() {return assetLevel;}
    private final List<ETYPE> eventType; @Override public List<ETYPE> getEventType() {return eventType;}
    private final List<ESTATUS> eventStatus; public List<ESTATUS> getEventStatus() {return eventStatus;}
    
	public AbstractAssetCacheBean(AomFactoryBuilder<L,D,REALM,COMPANY,SCOPE,ASSET,ASTATUS,ATYPE,VIEW,EVENT,ETYPE,ESTATUS,?,?,USER,?,UP> fbAsset)
	{
		this.fbAsset=fbAsset;
		
		mapUpload = new HashMap<>();
		uploads = new ArrayList<>();
		
		mapAssetType1 = new HashMap<>();
		mapAssetType2 = new HashMap<>();
		
		mapCompany = new HashMap<>();
		mapManufacturer = new HashMap<>();
		mapVendor = new HashMap<>();
		mapMaintainer = new HashMap<>();
		
		assetStatus = new ArrayList<>();
		assetLevel = new ArrayList<>();
		eventType = new ArrayList<>();
		eventStatus = new ArrayList<>();
	}
	
	public void postConstruct(JeeslAomFacade<L,D,REALM,COMPANY,SCOPE,ASSET,ASTATUS,ATYPE,VIEW,EVENT,ESTATUS> fAsset)
	{
		if(cacheScope==null) {cacheScope = EjbCodeCache.instance(fbAsset.getClassScope()).facade(fAsset);}
		
		if(assetStatus.isEmpty()) {assetStatus.addAll(fAsset.allOrderedPositionVisible(fbAsset.getClassStatus()));}
		if(eventType.isEmpty()) {eventType.addAll(fAsset.allOrderedPositionVisible(fbAsset.getClassEventType()));}
		if(eventStatus.isEmpty()) {eventStatus.addAll(fAsset.allOrderedPositionVisible(fbAsset.getClassEventStatus()));}
		
		uploads.addAll(fAsset.allOrderedPositionVisible(fbAsset.getClassUpload()));
		mapUpload.putAll(EjbCodeFactory.toMapCode(uploads));
		logger.info(fbAsset.getClassUpload().getSimpleName()+" "+mapUpload.size());
	}
	
	public void reloadRealm(JeeslAomFacade<L,D,REALM,COMPANY,SCOPE,ASSET,ASTATUS,ATYPE,VIEW,EVENT,ESTATUS> fAsset, REALM realm, RREF rref)
	{
		reloadAssetTypes(fAsset,realm,rref,false);
		reloadCompanies(fAsset,realm,rref);
	}
	
	private void reloadAssetTypes(JeeslAomFacade<L,D,REALM,COMPANY,SCOPE,ASSET,ASTATUS,ATYPE,VIEW,EVENT,ESTATUS> fAsset, REALM realm, RREF rref, boolean force)
	{		
		reloadAssetTypes1(fAsset,realm,rref,force);
		reloadAssetTypes2(fAsset,realm,rref,force);
	}
	
	private void reloadAssetTypes1(JeeslAomFacade<L,D,REALM,COMPANY,SCOPE,ASSET,ASTATUS,ATYPE,VIEW,EVENT,ESTATUS> fAsset, REALM realm, RREF rref, boolean force)
	{		
		initMap(realm,rref,mapAssetType1);
		if(force || mapAssetType1.get(realm).get(rref).isEmpty())
		{
			mapAssetType1.get(realm).get(rref).clear();
			VIEW view = fAsset.fcAomView(realm,rref,JeeslAomView.Tree.hierarchy);
			
			mapAssetType1.get(realm).get(rref).addAll(fAsset.fAomAssetTypes(TenantIdentifier.instance(realm).withRref(rref),view));
//			ATYPE root = fAsset.fcAomRootType(realm,rref,view);
//			reloadTypes1(fAsset,realm,rref,fAsset.allForParent(fbAsset.getClassAssetType(),root));
			logger.info(AbstractLogMessage.reloaded(fbAsset.getClassAssetType(), mapAssetType1.get(realm).get(rref), rref)+" in realm "+realm.toString());
		}
	}
//	private void reloadTypes1(JeeslAssetFacade<L,D,REALM,COMPANY,ASSET,ASTATUS,ATYPE,VIEW,EVENT,ETYPE,ESTATUS,USER,?,UP> fAsset, REALM realm, RREF rref, List<ATYPE> types)
//	{
//		for(ATYPE type : types)
//		{
//			mapAssetType1.get(realm).get(rref).add(type);
//			reloadTypes1(fAsset,realm,rref,fAsset.allForParent(fbAsset.getClassAssetType(),type));
//		}
//	}
	
	private void reloadAssetTypes2(JeeslAomFacade<L,D,REALM,COMPANY,SCOPE,ASSET,ASTATUS,ATYPE,VIEW,EVENT,ESTATUS> fAsset, REALM realm, RREF rref, boolean force)
	{		
		initMap(realm,rref,mapAssetType2);
		if(force || mapAssetType2.get(realm).get(rref).isEmpty())
		{
			mapAssetType2.get(realm).get(rref).clear();
			try
			{
				VIEW view = fAsset.fAomView(realm,rref,JeeslAomView.Tree.type1);
				mapAssetType2.get(realm).get(rref).addAll(fAsset.fAomAssetTypes(TenantIdentifier.instance(realm).withRref(rref),view));
//				ATYPE root = fAsset.fcAomRootType(realm,rref,view);
//				reloadTypes2(fAsset,realm,rref,fAsset.allForParent(fbAsset.getClassAssetType(),root));
				logger.info(AbstractLogMessage.reloaded(fbAsset.getClassAssetType(), mapAssetType2.get(realm).get(rref), rref)+" in realm "+realm.toString());
			}
			catch (JeeslNotFoundException e) {}
		}
	}
//	private void reloadTypes2(JeeslAssetFacade<L,D,REALM,COMPANY,ASSET,ASTATUS,ATYPE,VIEW,EVENT,ETYPE,ESTATUS,USER,?,UP> fAsset, REALM realm, RREF rref, List<ATYPE> types)
//	{
//		for(ATYPE type : types)
//		{
//			mapAssetType2.get(realm).get(rref).add(type);
//			reloadTypes2(fAsset,realm,rref,fAsset.allForParent(fbAsset.getClassAssetType(),type));
//		}
//	}
	
	private void initMap(REALM realm, RREF rref, Map<REALM,Map<RREF,List<ATYPE>>> map)
	{
		if(!map.containsKey(realm)) {map.put(realm,new HashMap<>());}	
		if(!map.get(realm).containsKey(rref)) {map.get(realm).put(rref,new ArrayList<>());}
	}
	
	private void reloadCompanies(JeeslAomFacade<L,D,REALM,COMPANY,SCOPE,ASSET,ASTATUS,ATYPE,VIEW,EVENT,ESTATUS> fAsset, REALM realm, RREF rref)
	{
		if(!mapCompany.containsKey(rref)) {mapCompany.put(rref,new ArrayList<>());}
		mapCompany.get(rref).clear();
		mapCompany.get(rref).addAll(fAsset.fAomCompanies(TenantIdentifier.instance(realm).withRref(rref)));
		logger.info(AbstractLogMessage.reloaded(fbAsset.getClassCompany(),mapCompany.get(rref)));
		reloadCompanies(realm,rref);
	}
	
	private void reloadCompanies(REALM realm, RREF rref)
	{	
		reloadCompanies(realm,rref,cacheScope.ejb(JeeslAomScope.Code.manufacturer),mapManufacturer,mapCompany.get(rref));
		reloadCompanies(realm,rref,cacheScope.ejb(JeeslAomScope.Code.vendor),mapVendor,mapCompany.get(rref));
		reloadCompanies(realm,rref,cacheScope.ejb(JeeslAomScope.Code.maintainer),mapMaintainer,mapCompany.get(rref));
	}
	
	private void reloadCompanies(REALM realm, RREF rref, SCOPE scope, Map<RREF,List<COMPANY>> map, List<COMPANY> companies)
	{		
		if(!map.containsKey(rref)) {map.put(rref,new ArrayList<>());}
		map.get(rref).clear();
		
		for(COMPANY c : companies)
		{
			if(c.getScopes().contains(scope)) {map.get(rref).add(c);}
		}		
	}
	
	@Override public void update(REALM realm, RREF rref, VIEW view, ATYPE type)
	{
		if(view.getTree().equals(JeeslAomView.Tree.hierarchy.toString()))
		{
			initMap(realm,rref,mapAssetType1);
			if(!Collections.replaceAll(mapAssetType1.get(realm).get(rref),type,type)){mapAssetType1.get(realm).get(rref).add(type);}
		}
		else if(view.getTree().equals(JeeslAomView.Tree.type2.toString()))
		{
			initMap(realm,rref,mapAssetType2);
			if(!Collections.replaceAll(mapAssetType2.get(realm).get(rref),type,type)){mapAssetType2.get(realm).get(rref).add(type);}
		}
		else
		{
			logger.warn("NYI !!");
		}
	}
	@Override public void delete(REALM realm, RREF rref, VIEW view, ATYPE type)
	{
		if(view.getTree().equals(JeeslAomView.Tree.hierarchy.toString()))
		{
			if(mapAssetType1.get(realm).get(rref).contains(type)){mapAssetType1.get(realm).get(rref).remove(type);}
		}
		else if(view.getTree().equals(JeeslAomView.Tree.type2.toString()))
		{
			if(mapAssetType2.get(realm).get(rref).contains(type)){mapAssetType2.get(realm).get(rref).remove(type);}
		}
		else
		{
			logger.warn("NYI !!");
		}
	}
}