package org.jeesl.interfaces.cache.module.aom;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.jeesl.interfaces.model.module.aom.company.JeeslAomCompany;
import org.jeesl.interfaces.model.module.aom.company.JeeslAomScope;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.model.ejb.module.aom.CompanyScopeCacheIdentifier;
import org.jeesl.model.ejb.system.tenant.TenantIdentifier;

public interface JeeslAomCompanyCache <REALM extends JeeslTenantRealm<?,?,REALM,?>,
										COMPANY extends JeeslAomCompany<REALM,SCOPE>,
										SCOPE extends JeeslAomScope<?,?,SCOPE,?>>
								extends Serializable
{	
	Map<TenantIdentifier<REALM>,List<COMPANY>> getCachedAllCompanies();
	Map<CompanyScopeCacheIdentifier,List<COMPANY>> getCachedScopeCompanies();
	
//	Map<TenantIdentifier<REALM>,List<COMPANY>> getMapVendor();
//	Map<TenantIdentifier<REALM>,List<COMPANY>> getMapMaintainer();
//	
	void invalidateCompanyCache(TenantIdentifier<REALM> identifier);
}