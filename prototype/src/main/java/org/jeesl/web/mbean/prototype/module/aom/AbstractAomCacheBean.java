package org.jeesl.web.mbean.prototype.module.aom;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.infinispan.Cache;
import org.jeesl.api.facade.module.JeeslAssetFacade;
import org.jeesl.factory.ejb.util.EjbIdFactory;
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
import org.jeesl.interfaces.model.module.aom.event.JeeslAomEventUpload;
import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.qualifier.cache.local.AomCompanyCache;
import org.jeesl.model.ejb.system.tenant.TenantIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractAomCacheBean <REALM extends JeeslTenantRealm<?,?,REALM,?>, RREF extends EjbWithId,
										COMPANY extends JeeslAomCompany<REALM,SCOPE>,
										SCOPE extends JeeslAomScope<?,?,SCOPE,?>,
										ASSET extends JeeslAomAsset<REALM,ASSET,COMPANY,ASTATUS,ATYPE>,
										ASTATUS extends JeeslAomAssetStatus<?,?,ASTATUS,?>,
										ATYPE extends JeeslAomAssetType<?,?,REALM,ATYPE,VIEW,?>,
										VIEW extends JeeslAomView<?,?,REALM,?>,
										EVENT extends JeeslAomEvent<COMPANY,ASSET,ETYPE,ESTATUS,?,USER,?>,
										ETYPE extends JeeslAomEventType<?,?,ETYPE,?>,
										ESTATUS extends JeeslAomEventStatus<?,?,ESTATUS,?>,
										USER extends JeeslSimpleUser,
										UP extends JeeslAomEventUpload<?,?,UP,?>>
								implements JeeslAomCompanyCache<REALM,COMPANY>
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractAomCacheBean.class);
	
	protected JeeslAssetFacade<?,?,REALM,COMPANY,ASSET,ASTATUS,?,?,?,ETYPE,ESTATUS,USER,?,UP> fAom;
	
	@Inject @AomCompanyCache protected Cache<TenantIdentifier<REALM>,List<COMPANY>> cacheCompanies;
	
	protected final Map<TenantIdentifier<REALM>,List<COMPANY>> cachedCompanies;
	
	public AbstractAomCacheBean()
	{
		cachedCompanies = new CacheMapCompany();
	}
	
	public void postConstruct(JeeslAssetFacade<?,?,REALM,COMPANY,ASSET,ASTATUS,?,?,?,ETYPE,ESTATUS,USER,?,UP> fAom)
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
			return cacheCompanies.computeIfAbsent(identifier, i -> fAom.fAomCompanies(identifier));
		}
	}
	
	@Override public Map<TenantIdentifier<REALM>, List<COMPANY>> getCachedCompanies() {return cachedCompanies;}
	@Override public void update(TenantIdentifier<REALM> identifier, COMPANY company)	{EjbIdFactory.replaceOrAdd(cacheCompanies.get(identifier),company);}
}