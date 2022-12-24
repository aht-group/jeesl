package org.jeesl.factory.ejb.module.asset;

import org.jeesl.interfaces.model.module.aom.company.JeeslAomCompany;
import org.jeesl.interfaces.model.module.aom.company.JeeslAomScope;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.model.ejb.system.tenant.TenantIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbAssetCompanyFactory<REALM extends JeeslTenantRealm<?,?,REALM,?>,
									COMPANY extends JeeslAomCompany<REALM,SCOPE>,
									SCOPE extends JeeslAomScope<?,?,SCOPE,?>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbAssetCompanyFactory.class);
	
	private final Class<COMPANY> cCompany;
	
    public EjbAssetCompanyFactory(final Class<COMPANY> cCompany)
    {
        this.cCompany = cCompany;
    }
	
	public <RREF extends EjbWithId> COMPANY build(TenantIdentifier<REALM> identifier)
	{
		try
		{
			COMPANY ejb = cCompany.newInstance();
			ejb.setRealm(identifier.getRealm());
			ejb.setRealmIdentifier(identifier.getId());
		    return ejb;
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		return null;
    }
}