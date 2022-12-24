package org.jeesl.controller.cache;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeesl.api.facade.module.JeeslAssetFacade;
import org.jeesl.factory.ejb.util.EjbIdFactory;
import org.jeesl.interfaces.cache.module.aom.JeeslAomCompanyCache;
import org.jeesl.interfaces.model.module.aom.company.JeeslAomCompany;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.model.ejb.system.tenant.TenantIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;

public class JeeslAomLoadingCache <REALM extends JeeslTenantRealm<?,?,REALM,?>,
									COMPANY extends JeeslAomCompany<REALM,?>>
						implements JeeslAomCompanyCache<REALM,COMPANY>
{
	final static Logger logger = LoggerFactory.getLogger(JeeslAomLoadingCache.class);
	public static final long serialVersionUID=1;
	
	private JeeslAssetFacade<?,?,REALM,COMPANY,?,?,?,?,?,?,?,?,?,?> fAom;
	
	private LoadingCache<TenantIdentifier<REALM>,List<COMPANY>> cacheCompanies;
	private Map<TenantIdentifier<REALM>,List<COMPANY>> cachedCompanies;
	
	public JeeslAomLoadingCache(JeeslAssetFacade<?,?,REALM,COMPANY,?,?,?,?,?,?,?,?,?,?> fAom)
	{
		this.fAom=fAom;
		cacheCompanies = Caffeine.newBuilder()
			    .maximumSize(10_000)
			    .expireAfterWrite(Duration.ofMinutes(5))
			    .refreshAfterWrite(Duration.ofMinutes(1))
			    .build(key -> fAom.fAomCompanies(key));
		
		cachedCompanies = new CacheMapCompany();
	}
	
	private class CacheMapCompany extends HashMap<TenantIdentifier<REALM>,List<COMPANY>>
	{
		private static final long serialVersionUID = 1L;
		@SuppressWarnings("unchecked")
		@Override public List<COMPANY> get(Object key) {return cacheCompanies.get(((TenantIdentifier<REALM>)key));}
	}

	@Override public Map<TenantIdentifier<REALM>, List<COMPANY>> getCachedCompanies() {return cachedCompanies;}
	@Override public void update(TenantIdentifier<REALM> identifier, COMPANY company) {EjbIdFactory.replaceOrAdd(cacheCompanies.get(identifier),company);}
}