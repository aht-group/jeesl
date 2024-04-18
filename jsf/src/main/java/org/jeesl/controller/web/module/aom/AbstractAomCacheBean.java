package org.jeesl.controller.web.module.aom;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.jeesl.api.facade.module.JeeslAomFacade;
import org.jeesl.factory.builder.module.AomFactoryBuilder;
import org.jeesl.factory.ejb.util.EjbCodeFactory;
import org.jeesl.interfaces.cache.module.aom.JeeslAomCache;
import org.jeesl.interfaces.cache.module.aom.JeeslAomTypeCache;
import org.jeesl.interfaces.model.module.aom.asset.JeeslAomAsset;
import org.jeesl.interfaces.model.module.aom.asset.JeeslAomAssetStatus;
import org.jeesl.interfaces.model.module.aom.asset.JeeslAomAssetType;
import org.jeesl.interfaces.model.module.aom.asset.JeeslAomView;
import org.jeesl.interfaces.model.module.aom.company.JeeslAomCompany;
import org.jeesl.interfaces.model.module.aom.company.JeeslAomScope;
import org.jeesl.interfaces.model.module.aom.event.JeeslAomEventStatus;
import org.jeesl.interfaces.model.module.aom.event.JeeslAomEventType;
import org.jeesl.interfaces.model.module.aom.event.JeeslAomEventUpload;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.model.ejb.module.aom.AomScopeCacheKey;
import org.jeesl.model.ejb.module.aom.AomTypeCacheKey;
import org.jeesl.model.ejb.system.tenant.TenantIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.benmanes.caffeine.cache.Caffeine;

public abstract class AbstractAomCacheBean <REALM extends JeeslTenantRealm<?,?,REALM,?>,
										COMPANY extends JeeslAomCompany<REALM,SCOPE>,
										SCOPE extends JeeslAomScope<?,?,SCOPE,?>,
										ASSET extends JeeslAomAsset<REALM,ASSET,COMPANY,ASTATUS,ATYPE>,
										ASTATUS extends JeeslAomAssetStatus<?,?,ASTATUS,?>,
										ATYPE extends JeeslAomAssetType<?,?,REALM,ATYPE,VIEW,?>,
										VIEW extends JeeslAomView<?,?,REALM,?>,				
										ETYPE extends JeeslAomEventType<?,?,ETYPE,?>,
										ESTATUS extends JeeslAomEventStatus<?,?,ESTATUS,?>,
										UC extends JeeslAomEventUpload<?,?,UC,?>>
								implements JeeslAomCache<REALM,COMPANY,SCOPE,ATYPE,VIEW,ETYPE>,
											JeeslAomTypeCache<REALM,ATYPE,VIEW>
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractAomCacheBean.class);
	
	private final AomFactoryBuilder<?,?,REALM,COMPANY,SCOPE,ASSET,ASTATUS,ATYPE,VIEW,?,ETYPE,ESTATUS,?,?,?,?,UC> fbAom;
	protected JeeslAomFacade<?,?,REALM,COMPANY,ASSET,ASTATUS,ATYPE,VIEW,?,ESTATUS,?> fAom;

//	@Inject @AomCompanyCache private Cache<TenantIdentifier<REALM>,List<COMPANY>> cacheCdiAllCompanies;
//	@Inject @AomCompanyCache private Cache<AomScopeCacheKey,List<COMPANY>> cacheCdiCompanyScope;
//	@Inject @AomCompanyCache private Cache<AomTypeCacheKey,List<ATYPE>> cacheCdiType;
	
	private final com.github.benmanes.caffeine.cache.Cache<TenantIdentifier<REALM>,List<COMPANY>> cacheLocalAllCompanies;
	private final com.github.benmanes.caffeine.cache.Cache<TenantIdentifier<REALM>,List<COMPANY>> cacheLocalCompanyScope;

	protected final Map<TenantIdentifier<REALM>,List<COMPANY>> cachedCompanies; @Override public Map<TenantIdentifier<REALM>, List<COMPANY>> getCachedAllCompanies() {return cachedCompanies;}
	protected final Map<AomScopeCacheKey,List<COMPANY>> cachedCompanyScope; @Override public Map<AomScopeCacheKey,List<COMPANY>> getCachedScopeCompanies() {return cachedCompanyScope;}
	protected final Map<AomTypeCacheKey,List<ATYPE>> cachedType; @Override public Map<AomTypeCacheKey, List<ATYPE>> getCachedType() {return cachedType;}
	
	private final Map<String,UC> mapUploadCategory; public Map<String,UC> getMapUploadCategory() {return mapUploadCategory;}
	
	private final List<SCOPE> scopes; @Override public List<SCOPE> getScopes() {return scopes;}
	private final List<ASTATUS> assetStatus; public List<ASTATUS> getAssetStatus() {return assetStatus;}
	private final List<ETYPE> eventType; @Override public List<ETYPE> getEventType() {return eventType;}
	private final List<ESTATUS> eventStatus; public List<ESTATUS> getEventStatus() {return eventStatus;}
	
	public AbstractAomCacheBean(AomFactoryBuilder<?,?,REALM,COMPANY,SCOPE,ASSET,ASTATUS,ATYPE,VIEW,?,ETYPE,ESTATUS,?,?,?,?,UC> fbAom)
	{
		this.fbAom=fbAom;
		
		cacheLocalAllCompanies = Caffeine.newBuilder().maximumSize(500).expireAfterWrite(Duration.ofMinutes(2)).build();
		cacheLocalCompanyScope = Caffeine.newBuilder().maximumSize(500).expireAfterWrite(Duration.ofMinutes(2)).build();
		
		cachedCompanies = new CacheMapCompany();
		cachedCompanyScope = new CacheMapCompanyScope();
		cachedType = new CacheMapType();
		
		mapUploadCategory = new HashMap<>();
		
		scopes = new ArrayList<>();
		assetStatus = new ArrayList<>();
		eventType = new ArrayList<>();
		eventStatus = new ArrayList<>();
	}
	
	public void postConstruct(JeeslAomFacade<?,?,REALM,COMPANY,ASSET,ASTATUS,ATYPE,VIEW,?,ESTATUS,?> fAom)
	{
		this.fAom=fAom;
		
		mapUploadCategory.putAll(EjbCodeFactory.toMapCode(fAom.allOrderedPositionVisible(fbAom.getClassUpload())));
		
		scopes.addAll(fAom.allOrderedPositionVisible(fbAom.getClassScope()));
		assetStatus.addAll(fAom.allOrderedPositionVisible(fbAom.getClassStatus()));
		eventType.addAll(fAom.allOrderedPositionVisible(fbAom.getClassEventType()));
		eventStatus.addAll(fAom.allOrderedPositionVisible(fbAom.getClassEventStatus()));
	}
	
	private class CacheMapCompany extends HashMap<TenantIdentifier<REALM>,List<COMPANY>>
	{
		private static final long serialVersionUID = 1L;
		@SuppressWarnings("unchecked")
		@Override public List<COMPANY> get(Object key)
		{
			TenantIdentifier<REALM> identifier = (TenantIdentifier<REALM>)key;
			
			List<COMPANY> list = cacheLocalAllCompanies.getIfPresent(identifier);
			if(Objects.isNull(list)) {list = fAom.fAomCompanies(identifier); cacheLocalAllCompanies.put(identifier,list);}
			return list;
//			return cacheCdiAllCompanies.computeIfAbsent(identifier, i -> fAom.fAomCompanies(identifier));
		}
	}
	@Override public void invalidateCompanyCache(TenantIdentifier<REALM> identifier)
	{
//		cacheCdiAllCompanies.remove(identifier);
		cacheLocalAllCompanies.invalidate(identifier);
	}
	
	private class CacheMapCompanyScope extends HashMap<AomScopeCacheKey,List<COMPANY>>
	{
		private static final long serialVersionUID = 1L;
		@Override public List<COMPANY> get(Object key)
		{
			AomScopeCacheKey identifier = (AomScopeCacheKey)key;
			return cachedCompanyScope.computeIfAbsent(identifier, i -> scope(i));
		}
	}
	@SuppressWarnings("unchecked")
	private List<COMPANY> scope(AomScopeCacheKey identifier)
	{
		TenantIdentifier<REALM> id = TenantIdentifier.instance((REALM)identifier.getRealm()).withRref(identifier);
		return cachedCompanies.get(id).stream().filter(c -> c.getScopes().contains(identifier.getScope())).collect(Collectors.toList());
	}


	
	private class CacheMapType extends HashMap<AomTypeCacheKey,List<ATYPE>>
	{
		private static final long serialVersionUID = 1L;
		@Override public List<ATYPE> get(Object key)
		{
			AomTypeCacheKey identifier = (AomTypeCacheKey)key;
			return cachedType.computeIfAbsent(identifier, i -> load(i));
		}
	}
	@SuppressWarnings("unchecked")
	private List<ATYPE> load(AomTypeCacheKey key)
	{
		TenantIdentifier<REALM> identifier = TenantIdentifier.instance((REALM)key.getRealm()).withRref(key);
		VIEW view = fAom.fcAomView(identifier.getRealm(),identifier,key.getTree());
		return fAom.fAomAssetTypes(identifier,view);
	}
}