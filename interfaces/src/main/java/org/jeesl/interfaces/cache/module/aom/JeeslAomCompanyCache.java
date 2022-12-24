package org.jeesl.interfaces.cache.module.aom;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.jeesl.interfaces.model.module.aom.company.JeeslAomCompany;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.model.ejb.system.tenant.TenantIdentifier;

public interface JeeslAomCompanyCache <REALM extends JeeslTenantRealm<?,?,REALM,?>,
										COMPANY extends JeeslAomCompany<REALM,?>>
								extends Serializable
{	
	Map<TenantIdentifier<REALM>,List<COMPANY>> getCachedCompanies();
//	Map<TenantIdentifier<REALM>,List<COMPANY>> getMapVendor();
//	Map<TenantIdentifier<REALM>,List<COMPANY>> getMapMaintainer();
//	
	void update(TenantIdentifier<REALM> identifier, COMPANY company);
}