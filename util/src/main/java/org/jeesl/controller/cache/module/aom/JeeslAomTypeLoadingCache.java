package org.jeesl.controller.cache.module.aom;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeesl.api.facade.module.JeeslAomFacade;
import org.jeesl.interfaces.cache.module.aom.JeeslAomTypeCache;
import org.jeesl.interfaces.model.module.aom.asset.JeeslAomAssetType;
import org.jeesl.interfaces.model.module.aom.asset.JeeslAomView;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.model.ejb.module.aom.AomTypeCacheKey;
import org.jeesl.model.ejb.system.tenant.TenantIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;

public class JeeslAomTypeLoadingCache <REALM extends JeeslTenantRealm<?,?,REALM,?>,
										ATYPE extends JeeslAomAssetType<?,?,REALM,ATYPE,VIEW,?>,
										VIEW extends JeeslAomView<?,?,REALM,?>>
						implements JeeslAomTypeCache<REALM,ATYPE,VIEW>
{
	final static Logger logger = LoggerFactory.getLogger(JeeslAomTypeLoadingCache.class);
	public static final long serialVersionUID=1;
	
	private JeeslAomFacade<?,?,REALM,?,?,?,ATYPE,VIEW,?,?,?,?> fAom;
	
	private LoadingCache<AomTypeCacheKey,List<ATYPE>> cacheType;
	
	private Map<AomTypeCacheKey,List<ATYPE>> cachedType; @Override public Map<AomTypeCacheKey,List<ATYPE>> getCachedType() {return cachedType;}
	
	public JeeslAomTypeLoadingCache(JeeslAomFacade<?,?,REALM,?,?,?,ATYPE,VIEW,?,?,?,?> fAom)
	{
		this.fAom=fAom;
		cacheType = Caffeine.newBuilder()
			    .maximumSize(10_000)
			    .expireAfterWrite(Duration.ofMinutes(5))
			    .refreshAfterWrite(Duration.ofMinutes(1))
			    .build(key -> load(key));
		
		cachedType = new CacheMapCompany();
	}
	@SuppressWarnings("unchecked")
	private List<ATYPE> load(Object oKey)
	{
		AomTypeCacheKey key = (AomTypeCacheKey)oKey; 
		TenantIdentifier<REALM> identifier = TenantIdentifier.instance((REALM)key.getRealm()).withRref(key);
		VIEW view = fAom.fcAomView((REALM)key.getRealm(),key,key.getTree());
		return fAom.fAomAssetTypes(identifier,view);
	}
	
	private class CacheMapCompany extends HashMap<AomTypeCacheKey,List<ATYPE>>
	{
		private static final long serialVersionUID = 1L;
		@Override public List<ATYPE> get(Object key) {return cacheType.get(((AomTypeCacheKey)key));}
	}

//	@Override public void invalidateCompanyCache(TenantIdentifier<REALM> identifier) {cacheCompanies.invalidate(identifier);}
}