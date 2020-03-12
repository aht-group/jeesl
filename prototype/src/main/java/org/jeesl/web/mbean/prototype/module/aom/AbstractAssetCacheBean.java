package org.jeesl.web.mbean.prototype.module.aom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeesl.api.bean.module.aom.JeeslAssetCacheBean;
import org.jeesl.api.facade.module.JeeslAssetFacade;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.module.AomFactoryBuilder;
import org.jeesl.factory.ejb.util.EjbCodeFactory;
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
import org.jeesl.interfaces.model.system.mcs.JeeslMcsRealm;
import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.util.db.cache.EjbCodeCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;

public abstract class AbstractAssetCacheBean <L extends JeeslLang, D extends JeeslDescription,
										REALM extends JeeslMcsRealm<L,D,REALM,?>, RREF extends EjbWithId,
										COMPANY extends JeeslAomCompany<REALM,SCOPE>,
										SCOPE extends JeeslAomScope<L,D,SCOPE,?>,
										ASSET extends JeeslAomAsset<REALM,ASSET,COMPANY,ASTATUS,ATYPE>,
										ASTATUS extends JeeslAomAssetStatus<L,D,ASTATUS,?>,
										ATYPE extends JeeslAomAssetType<L,D,REALM,ATYPE,VIEW,?>,
										VIEW extends JeeslAomView<L,D,REALM,?>,
										EVENT extends JeeslAomEvent<COMPANY,ASSET,ETYPE,ESTATUS,USER,FRC>,
										ETYPE extends JeeslAomEventType<L,D,ETYPE,?>,
										ESTATUS extends JeeslAomEventStatus<L,D,ESTATUS,?>,
										USER extends JeeslSimpleUser,
										FRC extends JeeslFileContainer<?,?>,
										UP extends JeeslAomEventUpload<L,D,UP,?>>
								implements JeeslAssetCacheBean<L,D,REALM,RREF,COMPANY,SCOPE,ASSET,ASTATUS,ATYPE,VIEW,ETYPE,UP>
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractAssetCacheBean.class);
	
//	private JeeslAssetFacade<L,D,REALM,COMPANY,SCOPE,ASSET,STATUS,TYPE> fAsset;
	
	private final AomFactoryBuilder<L,D,REALM,COMPANY,SCOPE,ASSET,ASTATUS,ATYPE,VIEW,EVENT,ETYPE,ESTATUS,USER,FRC,UP> fbAsset;

	private EjbCodeCache<SCOPE> cacheScope;
	
	private final Map<String,UP> mapUpload; public Map<String,UP> getMapUpload() {return mapUpload;}
	private final List<UP> uploads; public List<UP> getUploads() {return uploads;}
	
	private final Map<REALM,Map<RREF,List<ATYPE>>> mapAssetType1; @Override public Map<REALM,Map<RREF,List<ATYPE>>> getMapAssetType1() {return mapAssetType1;}
	private final Map<REALM,Map<RREF,List<ATYPE>>> mapAssetType2; @Override public Map<REALM,Map<RREF,List<ATYPE>>> getMapAssetType2() {return mapAssetType2;}
	
	private final Map<RREF,List<COMPANY>> mapCompany; @Override public Map<RREF,List<COMPANY>> cachedCompany() {return mapCompany;}
	private final Map<RREF,List<COMPANY>> mapManufacturer; public Map<RREF,List<COMPANY>> getMapManufacturer() {return mapManufacturer;}
	private final Map<RREF,List<COMPANY>> mapVendor; @Override public Map<RREF,List<COMPANY>> getMapVendor() {return mapVendor;}
	private final Map<RREF,List<COMPANY>> mapMaintainer; @Override public Map<RREF,List<COMPANY>> getMapMaintainer() {return mapMaintainer;}
	


	private final List<ASTATUS> assetStatus; public List<ASTATUS> getAssetStatus() {return assetStatus;}
    private final List<VIEW> assetLevel; public List<VIEW> getAssetLevel() {return assetLevel;}
    private final List<ETYPE> eventType; @Override public List<ETYPE> getEventType() {return eventType;}
    private final List<ESTATUS> eventStatus; public List<ESTATUS> getEventStatus() {return eventStatus;}
    
	public AbstractAssetCacheBean(AomFactoryBuilder<L,D,REALM,COMPANY,SCOPE,ASSET,ASTATUS,ATYPE,VIEW,EVENT,ETYPE,ESTATUS,USER,FRC,UP> fbAsset)
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
	
	public void postConstruct(JeeslAssetFacade<L,D,REALM,COMPANY,SCOPE,ASSET,ASTATUS,ATYPE,VIEW,EVENT,ETYPE,ESTATUS,USER,FRC,UP> fAsset)
	{
		if(cacheScope==null) {cacheScope = new EjbCodeCache<SCOPE>(fAsset,fbAsset.getClassScope());}
		
		if(assetStatus.isEmpty()) {assetStatus.addAll(fAsset.allOrderedPositionVisible(fbAsset.getClassStatus()));}
		if(eventType.isEmpty()) {eventType.addAll(fAsset.allOrderedPositionVisible(fbAsset.getClassEventType()));}
		if(eventStatus.isEmpty()) {eventStatus.addAll(fAsset.allOrderedPositionVisible(fbAsset.getClassEventStatus()));}
		
		uploads.addAll(fAsset.allOrderedPositionVisible(fbAsset.getClassUpload()));
		mapUpload.putAll(EjbCodeFactory.toMapCode(uploads));
		logger.info(fbAsset.getClassUpload().getSimpleName()+" "+mapUpload.size());
	}
	
	public void reloadRealm(JeeslAssetFacade<L,D,REALM,COMPANY,SCOPE,ASSET,ASTATUS,ATYPE,VIEW,EVENT,ETYPE,ESTATUS,USER,FRC,UP> fAsset, REALM realm, RREF rref)
	{
		reloadAssetTypes(fAsset,realm,rref,false);
		reloadCompanies(fAsset,realm,rref);
	}
	
	private void reloadAssetTypes(JeeslAssetFacade<L,D,REALM,COMPANY,SCOPE,ASSET,ASTATUS,ATYPE,VIEW,EVENT,ETYPE,ESTATUS,USER,FRC,UP> fAsset, REALM realm, RREF rref, boolean force)
	{		
		reloadAssetTypes1(fAsset,realm,rref,force);
		reloadAssetTypes2(fAsset,realm,rref,force);
	}
	
	private void reloadAssetTypes1(JeeslAssetFacade<L,D,REALM,COMPANY,SCOPE,ASSET,ASTATUS,ATYPE,VIEW,EVENT,ETYPE,ESTATUS,USER,FRC,UP> fAsset, REALM realm, RREF rref, boolean force)
	{		
		if(!mapAssetType1.containsKey(realm)) {mapAssetType1.put(realm,new HashMap<>());}	
		if(!mapAssetType1.get(realm).containsKey(rref)) {mapAssetType1.get(realm).put(rref,new ArrayList<>());}

		if(force || mapAssetType1.get(realm).get(rref).isEmpty())
		{
			mapAssetType1.get(realm).get(rref).clear();
			VIEW view = fAsset.fcAomView(realm,rref,JeeslAomView.Tree.hierarchy);
			ATYPE root = fAsset.fcAomRootType(realm,rref,view);
			reloadTypes1(fAsset,realm,rref,fAsset.allForParent(fbAsset.getClassAssetType(),root));
			logger.info(AbstractLogMessage.reloaded(fbAsset.getClassAssetType(), mapAssetType1.get(realm).get(rref), rref)+" in realm "+realm.toString());
		}
	}
	private void reloadTypes1(JeeslAssetFacade<L,D,REALM,COMPANY,SCOPE,ASSET,ASTATUS,ATYPE,VIEW,EVENT,ETYPE,ESTATUS,USER,FRC,UP> fAsset, REALM realm, RREF rref, List<ATYPE> types)
	{
		for(ATYPE type : types)
		{
			mapAssetType1.get(realm).get(rref).add(type);
			reloadTypes1(fAsset,realm,rref,fAsset.allForParent(fbAsset.getClassAssetType(),type));
		}
	}
	
	private void reloadAssetTypes2(JeeslAssetFacade<L,D,REALM,COMPANY,SCOPE,ASSET,ASTATUS,ATYPE,VIEW,EVENT,ETYPE,ESTATUS,USER,FRC,UP> fAsset, REALM realm, RREF rref, boolean force)
	{		
		if(!mapAssetType2.containsKey(realm)) {mapAssetType2.put(realm,new HashMap<>());}	
		if(!mapAssetType2.get(realm).containsKey(rref)) {mapAssetType2.get(realm).put(rref,new ArrayList<>());}

		if(force || mapAssetType2.get(realm).get(rref).isEmpty())
		{
			mapAssetType2.get(realm).get(rref).clear();
			try
			{
				VIEW view = fAsset.fAomView(realm,rref,JeeslAomView.Tree.type1);
				ATYPE root = fAsset.fcAomRootType(realm,rref,view);
				reloadTypes2(fAsset,realm,rref,fAsset.allForParent(fbAsset.getClassAssetType(),root));
				logger.info(AbstractLogMessage.reloaded(fbAsset.getClassAssetType(), mapAssetType2.get(realm).get(rref), rref)+" in realm "+realm.toString());
			}
			catch (JeeslNotFoundException e) {}
		}
	}
	private void reloadTypes2(JeeslAssetFacade<L,D,REALM,COMPANY,SCOPE,ASSET,ASTATUS,ATYPE,VIEW,EVENT,ETYPE,ESTATUS,USER,FRC,UP> fAsset, REALM realm, RREF rref, List<ATYPE> types)
	{
		for(ATYPE type : types)
		{
			mapAssetType2.get(realm).get(rref).add(type);
			reloadTypes2(fAsset,realm,rref,fAsset.allForParent(fbAsset.getClassAssetType(),type));
		}
	}
	
	
	private void reloadCompanies(JeeslAssetFacade<L,D,REALM,COMPANY,SCOPE,ASSET,ASTATUS,ATYPE,VIEW,EVENT,ETYPE,ESTATUS,USER,FRC,UP> fAsset, REALM realm, RREF rref)
	{
		if(!mapCompany.containsKey(rref)) {mapCompany.put(rref,new ArrayList<>());}
		mapCompany.get(rref).clear();
		mapCompany.get(rref).addAll(fAsset.fAssetCompanies(realm,rref));
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
	
	@Override public void update(REALM realm, RREF rref, COMPANY company)
	{
		if(!Collections.replaceAll(mapCompany.get(rref),company,company)){mapCompany.get(rref).add(company);}
		reloadCompanies(realm,rref);
	}
	@Override public void update(REALM realm, RREF rref, VIEW view, ATYPE type)
	{
		if(!Collections.replaceAll(mapAssetType1.get(realm).get(rref),type,type)){mapAssetType1.get(realm).get(rref).add(type);}
	}
	@Override public void delete(REALM realm, RREF rref, VIEW view, ATYPE type)
	{
		if(mapAssetType1.get(realm).get(rref).contains(type)){mapAssetType1.get(realm).get(rref).remove(type);}
	}
}