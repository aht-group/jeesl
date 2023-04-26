package org.jeesl.controller.cache.module.aom;

import java.util.List;
import java.util.Map;

import org.jeesl.api.facade.module.JeeslAomFacade;
import org.jeesl.factory.builder.module.AomFactoryBuilder;
import org.jeesl.interfaces.cache.module.aom.JeeslAomCache;
import org.jeesl.interfaces.cache.module.aom.JeeslAomCompanyCache;
import org.jeesl.interfaces.cache.module.aom.JeeslAomEventCache;
import org.jeesl.interfaces.cache.module.aom.JeeslAomTypeCache;
import org.jeesl.interfaces.model.module.aom.asset.JeeslAomAssetType;
import org.jeesl.interfaces.model.module.aom.asset.JeeslAomView;
import org.jeesl.interfaces.model.module.aom.company.JeeslAomCompany;
import org.jeesl.interfaces.model.module.aom.company.JeeslAomScope;
import org.jeesl.interfaces.model.module.aom.event.JeeslAomEventType;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.model.ejb.module.aom.AomScopeCacheKey;
import org.jeesl.model.ejb.module.aom.AomTypeCacheKey;
import org.jeesl.model.ejb.system.tenant.TenantIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslAomLoadingCache <REALM extends JeeslTenantRealm<?,?,REALM,?>,
									COMPANY extends JeeslAomCompany<REALM,SCOPE>,
									SCOPE extends JeeslAomScope<?,?,SCOPE,?>,
									ATYPE extends JeeslAomAssetType<?,?,REALM,ATYPE,VIEW,?>,
									VIEW extends JeeslAomView<?,?,REALM,?>,
									ETYPE extends JeeslAomEventType<?,?,ETYPE,?>>
						implements JeeslAomCache<REALM,COMPANY,SCOPE,ATYPE,VIEW,ETYPE>
{
	final static Logger logger = LoggerFactory.getLogger(JeeslAomLoadingCache.class);
	public static final long serialVersionUID=1;
	
	private final JeeslAomCompanyCache<REALM,COMPANY,SCOPE> company;
	private final JeeslAomTypeCache<REALM,ATYPE,VIEW> type;
	private final JeeslAomEventCache<REALM,ETYPE> event;

	public JeeslAomLoadingCache(AomFactoryBuilder<?,?,REALM,COMPANY,SCOPE,?,?,ATYPE,VIEW,?,ETYPE,?,?,?,?,?,?> fbAom,
										JeeslAomFacade<?,?,REALM,COMPANY,?,?,ATYPE,VIEW,?,ETYPE,?,?> fAom)
	{
		company = new JeeslAomCompanyLoadingCache<>(fbAom,fAom);
		type = new JeeslAomTypeLoadingCache<>(fAom);
		event = new JeeslAomEventLoadingCache<>(fbAom,fAom);
	}

	//Company
	@Override public Map<TenantIdentifier<REALM>, List<COMPANY>> getCachedAllCompanies() {return company.getCachedAllCompanies();}
	@Override public Map<AomScopeCacheKey, List<COMPANY>> getCachedScopeCompanies() {return company.getCachedScopeCompanies();}
	@Override public List<SCOPE> getScopes() {return company.getScopes();}
	@Override public void invalidateCompanyCache(TenantIdentifier<REALM> identifier) {company.invalidateCompanyCache(identifier);}
	
	//Type
	@Override public Map<AomTypeCacheKey, List<ATYPE>> getCachedType() {return type.getCachedType();}
	
	//Event
	@Override public List<ETYPE> getEventType() {return event.getEventType();}
}