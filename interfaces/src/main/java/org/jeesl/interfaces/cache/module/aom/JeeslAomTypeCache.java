package org.jeesl.interfaces.cache.module.aom;

import java.io.Serializable;

import org.jeesl.interfaces.model.module.aom.company.JeeslAomCompany;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;

public interface JeeslAomTypeCache <REALM extends JeeslTenantRealm<?,?,REALM,?>,
										COMPANY extends JeeslAomCompany<REALM,?>>
								extends Serializable
{	
	
}