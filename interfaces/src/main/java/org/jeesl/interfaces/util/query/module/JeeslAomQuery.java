package org.jeesl.interfaces.util.query.module;

import java.util.List;

import org.jeesl.interfaces.model.module.aom.asset.JeeslAomAsset;
import org.jeesl.interfaces.model.module.aom.asset.JeeslAomAssetType;
import org.jeesl.interfaces.model.module.aom.company.JeeslAomScope;
import org.jeesl.interfaces.model.module.aom.event.JeeslAomEvent;
import org.jeesl.interfaces.model.module.aom.event.JeeslAomEventStatus;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.util.query.JeeslCoreQuery;
import org.jeesl.interfaces.util.query.jpa.JeeslOrderingQuery;
import org.jeesl.interfaces.util.query.jpa.JeeslPaginationQuery;
import org.jeesl.interfaces.util.query.jpa.JeeslTenantQuery;

public interface JeeslAomQuery<REALM extends JeeslTenantRealm<?,?,REALM,?>,
							SCOPE extends JeeslAomScope<?,?,SCOPE,?>,
							ASSET extends JeeslAomAsset<?,ASSET,?,?,?>,
							ATYPE extends JeeslAomAssetType<?,?,REALM,ATYPE,?,?>,
							EVENT extends JeeslAomEvent<?,ASSET,?,?,?,?,?>,
							ESTATUS extends JeeslAomEventStatus<?,?,ESTATUS,?>
							>
			extends JeeslCoreQuery,JeeslTenantQuery<REALM>,
					JeeslPaginationQuery,JeeslOrderingQuery
{
//	void x();
	
	JeeslAomQuery<REALM,SCOPE,ASSET,ATYPE,EVENT,ESTATUS> maxResults(Integer maxResults);
	
	public List<ASSET> getAssets();
	public List<ATYPE> getAomAssetTypes();
	public List<ESTATUS> getAomEventStatus();
	public List<SCOPE> getAomCompanyScopes();
}