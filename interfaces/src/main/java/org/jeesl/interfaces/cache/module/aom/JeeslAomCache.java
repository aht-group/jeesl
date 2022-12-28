package org.jeesl.interfaces.cache.module.aom;

import java.util.List;

import org.jeesl.interfaces.model.module.aom.asset.JeeslAomAssetType;
import org.jeesl.interfaces.model.module.aom.asset.JeeslAomView;
import org.jeesl.interfaces.model.module.aom.company.JeeslAomCompany;
import org.jeesl.interfaces.model.module.aom.company.JeeslAomScope;
import org.jeesl.interfaces.model.module.aom.event.JeeslAomEventType;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;

public interface JeeslAomCache <REALM extends JeeslTenantRealm<?,?,REALM,?>,
										COMPANY extends JeeslAomCompany<REALM,SCOPE>,
										SCOPE extends JeeslAomScope<?,?,SCOPE,?>,
										ATYPE extends JeeslAomAssetType<?,?,REALM,ATYPE,VIEW,?>,
										VIEW extends JeeslAomView<?,?,REALM,?>,
										ETYPE extends JeeslAomEventType<?,?,ETYPE,?>>
								extends JeeslAomCompanyCache<REALM,COMPANY,SCOPE>
{
	List<SCOPE> getScopes();
	List<ETYPE> getEventType();
}