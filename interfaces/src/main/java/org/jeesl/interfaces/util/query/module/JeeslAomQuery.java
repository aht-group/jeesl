package org.jeesl.interfaces.util.query.module;

import java.util.List;

import org.jeesl.interfaces.model.module.aom.asset.JeeslAomAsset;
import org.jeesl.interfaces.model.module.aom.event.JeeslAomEvent;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.util.query.JeeslCoreQuery;

public interface JeeslAomQuery<REALM extends JeeslTenantRealm<?,?,REALM,?>,
							ASSET extends JeeslAomAsset<?,ASSET,?,?,?>,
							EVENT extends JeeslAomEvent<?,ASSET,?,?,?,?,?>
>
			extends JeeslCoreQuery
{
	public List<ASSET> getAssets();
}