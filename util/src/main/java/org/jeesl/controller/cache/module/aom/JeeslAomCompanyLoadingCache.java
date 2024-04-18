package org.jeesl.controller.cache.module.aom;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jeesl.api.facade.module.JeeslAomFacade;
import org.jeesl.factory.builder.module.AomFactoryBuilder;
import org.jeesl.interfaces.cache.module.aom.JeeslAomCompanyCache;
import org.jeesl.interfaces.model.module.aom.company.JeeslAomCompany;
import org.jeesl.interfaces.model.module.aom.company.JeeslAomScope;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.model.ejb.module.aom.AomScopeCacheKey;
import org.jeesl.model.ejb.system.tenant.TenantIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;

public class JeeslAomCompanyLoadingCache <REALM extends JeeslTenantRealm<?,?,REALM,?>,
									COMPANY extends JeeslAomCompany<REALM,SCOPE>,
									SCOPE extends JeeslAomScope<?,?,SCOPE,?>>
						implements JeeslAomCompanyCache<REALM,COMPANY,SCOPE>
{
	final static Logger logger = LoggerFactory.getLogger(JeeslAomCompanyLoadingCache.class);
	public static final long serialVersionUID=1;
	
	private final JeeslAomFacade<?,?,REALM,COMPANY,?,?,?,?,?,?,?> fAom;
	private final AomFactoryBuilder<?,?,REALM,COMPANY,SCOPE,?,?,?,?,?,?,?,?,?,?,?,?> fbAom;
	
	private final LoadingCache<TenantIdentifier<REALM>,List<COMPANY>> cacheCompanies;

	private final Map<TenantIdentifier<REALM>,List<COMPANY>> cachedAllCompanies;
	private Map<AomScopeCacheKey,List<COMPANY>> cachedScopeCompanies;
	
	public JeeslAomCompanyLoadingCache(AomFactoryBuilder<?,?,REALM,COMPANY,SCOPE,?,?,?,?,?,?,?,?,?,?,?,?> fbAom,
										JeeslAomFacade<?,?,REALM,COMPANY,?,?,?,?,?,?,?> fAom)
	{
		this.fbAom=fbAom;
		this.fAom=fAom;
		cacheCompanies = Caffeine.newBuilder()
			    .maximumSize(10_000)
			    .expireAfterWrite(Duration.ofMinutes(5))
			    .refreshAfterWrite(Duration.ofMinutes(1))
			    .build(key -> fAom.fAomCompanies(key));
		
		cachedAllCompanies = new CacheAllCompanyMap();
		cachedScopeCompanies = new CacheScopeCompanyMap();
	}
	
	private class CacheAllCompanyMap extends HashMap<TenantIdentifier<REALM>,List<COMPANY>>
	{
		private static final long serialVersionUID = 1L;
		@SuppressWarnings("unchecked")
		@Override public List<COMPANY> get(Object key) {return cacheCompanies.get(((TenantIdentifier<REALM>)key));}
	}
	private class CacheScopeCompanyMap extends HashMap<AomScopeCacheKey,List<COMPANY>>
	{
		private static final long serialVersionUID = 1L;
		@SuppressWarnings("unchecked")
		@Override public List<COMPANY> get(Object key)
		{
			AomScopeCacheKey identifier = (AomScopeCacheKey)key;
			TenantIdentifier<REALM> id = TenantIdentifier.instance((REALM)identifier.getRealm()).withRref(identifier);
//			logger.info(identifier.getScope().getCode());
			return cachedAllCompanies.get(id).stream().filter(c -> c.getScopes().contains(identifier.getScope())).collect(Collectors.toList());
		}
	}

	@Override public Map<TenantIdentifier<REALM>, List<COMPANY>> getCachedAllCompanies() {return cachedAllCompanies;}
	@Override public Map<AomScopeCacheKey, List<COMPANY>> getCachedScopeCompanies() {return cachedScopeCompanies;}
	
	@Override public List<SCOPE> getScopes() {return fAom.allOrderedPositionVisible(fbAom.getClassScope());}
	
	@Override public void invalidateCompanyCache(TenantIdentifier<REALM> identifier) {cacheCompanies.invalidate(identifier);}
	
	
}