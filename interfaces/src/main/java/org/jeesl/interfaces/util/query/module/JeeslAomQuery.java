package org.jeesl.interfaces.util.query.module;

import java.util.List;

import org.jeesl.interfaces.model.module.aom.asset.JeeslAomAsset;
import org.jeesl.interfaces.model.module.aom.asset.JeeslAomAssetType;
import org.jeesl.interfaces.model.module.aom.event.JeeslAomEvent;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.util.query.JeeslCoreQuery;
import org.jeesl.interfaces.util.query.jpa.JeeslPaginationQuery;
import org.jeesl.interfaces.util.query.jpa.JeeslTenantQuery;

public interface JeeslAomQuery<REALM extends JeeslTenantRealm<?,?,REALM,?>,
							ASSET extends JeeslAomAsset<?,ASSET,?,?,?>,
							ATYPE extends JeeslAomAssetType<?,?,REALM,ATYPE,?,?>,
							EVENT extends JeeslAomEvent<?,ASSET,?,?,?,?,?>
>
			extends JeeslCoreQuery,JeeslPaginationQuery,JeeslTenantQuery<REALM>
{
	public List<ASSET> getAssets();
	public List<ATYPE> getAomAssetTypes();
}