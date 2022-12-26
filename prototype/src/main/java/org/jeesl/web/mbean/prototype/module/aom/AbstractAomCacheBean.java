package org.jeesl.web.mbean.prototype.module.aom;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.infinispan.Cache;
import org.jeesl.api.facade.module.JeeslAssetFacade;
import org.jeesl.interfaces.cache.module.aom.JeeslAomCompanyCache;
import org.jeesl.interfaces.model.module.aom.asset.JeeslAomAsset;
import org.jeesl.interfaces.model.module.aom.asset.JeeslAomAssetStatus;
import org.jeesl.interfaces.model.module.aom.asset.JeeslAomAssetType;
import org.jeesl.interfaces.model.module.aom.asset.JeeslAomView;
import org.jeesl.interfaces.model.module.aom.company.JeeslAomCompany;
import org.jeesl.interfaces.model.module.aom.company.JeeslAomScope;
import org.jeesl.interfaces.model.module.aom.event.JeeslAomEvent;
import org.jeesl.interfaces.model.module.aom.event.JeeslAomEventStatus;
import org.jeesl.interfaces.model.module.aom.event.JeeslAomEventType;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.qualifier.cache.local.AomCompanyCache;
import org.jeesl.model.ejb.module.aom.CompanyScopeCacheIdentifier;
import org.jeesl.model.ejb.system.tenant.TenantIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractAomCacheBean <REALM extends JeeslTenantRealm<?,?,REALM,?>,
										COMPANY extends JeeslAomCompany<REALM,SCOPE>,
										SCOPE extends JeeslAomScope<?,?,SCOPE,?>,
										ASSET extends JeeslAomAsset<REALM,ASSET,COMPANY,ASTATUS,ATYPE>,
										ASTATUS extends JeeslAomAssetStatus<?,?,ASTATUS,?>,
										ATYPE extends JeeslAomAssetType<?,?,REALM,ATYPE,VIEW,?>,
										VIEW extends JeeslAomView<?,?,REALM,?>,
										EVENT extends JeeslAomEvent<COMPANY,ASSET,ETYPE,ESTATUS,?,?,?>,
										ETYPE extends JeeslAomEventType<?,?,ETYPE,?>,
										ESTATUS extends JeeslAomEventStatus<?,?,ESTATUS,?>>
								implements JeeslAomCompanyCache<REALM,COMPANY,SCOPE>
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractAomCacheBean.class);
	
	protected JeeslAssetFacade<?,?,REALM,COMPANY,ASSET,ASTATUS,?,?,?,ETYPE,ESTATUS,?,?,?> fAom;
	
	@Inject @AomCompanyCache protected Cache<TenantIdentifier<REALM>,List<COMPANY>> cacheAllCompanies;
	@Inject @AomCompanyCache protected Cache<CompanyScopeCacheIdentifier,List<COMPANY>> cacheCompanyScope;

	protected final Map<TenantIdentifier<REALM>,List<COMPANY>> cachedCompanies; @Override public Map<TenantIdentifier<REALM>, List<COMPANY>> getCachedAllCompanies() {return cachedCompanies;}
	protected final Map<CompanyScopeCacheIdentifier,List<COMPANY>> cachedCompanyScope; @Override public Map<CompanyScopeCacheIdentifier,List<COMPANY>> getCachedScopeCompanies() {return cachedCompanyScope;}
	
	public AbstractAomCacheBean()
	{
		cachedCompanies = new CacheMapCompany();
		cachedCompanyScope = new CacheMapCompanyScope();
	}
	
	public void postConstruct(JeeslAssetFacade<?,?,REALM,COMPANY,ASSET,ASTATUS,?,?,?,ETYPE,ESTATUS,?,?,?> fAom)
	{
		this.fAom=fAom;
	}
	
	private class CacheMapCompany extends HashMap<TenantIdentifier<REALM>,List<COMPANY>>
	{
		private static final long serialVersionUID = 1L;
		@SuppressWarnings("unchecked")
		@Override public List<COMPANY> get(Object key)
		{
			TenantIdentifier<REALM> identifier = (TenantIdentifier<REALM>)key;
			return cacheAllCompanies.computeIfAbsent(identifier, i -> fAom.fAomCompanies(identifier));
		}
	}
	
	private class CacheMapCompanyScope extends HashMap<CompanyScopeCacheIdentifier,List<COMPANY>>
	{
		private static final long serialVersionUID = 1L;
		@Override public List<COMPANY> get(Object key)
		{
			CompanyScopeCacheIdentifier identifier = (CompanyScopeCacheIdentifier)key;
			return cachedCompanyScope.computeIfAbsent(identifier, i -> scope(i));
		}
	}
	@SuppressWarnings("unchecked")
	private  List<COMPANY> scope(CompanyScopeCacheIdentifier identifier)
	{
		TenantIdentifier<REALM> id = TenantIdentifier.instance((REALM)identifier.getRealm()).withRref(identifier);
		return cacheAllCompanies.get(id).stream().filter(c -> c.getScopes().contains(identifier.getScope())).collect(Collectors.toList());
	}

	@Override public void invalidateCompanyCache(TenantIdentifier<REALM> identifier)
	{
		cacheAllCompanies.remove(identifier);
	}
}