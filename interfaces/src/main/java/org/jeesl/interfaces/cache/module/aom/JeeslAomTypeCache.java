package org.jeesl.interfaces.cache.module.aom;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.jeesl.interfaces.model.module.aom.asset.JeeslAomAssetType;
import org.jeesl.interfaces.model.module.aom.asset.JeeslAomView;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.model.ejb.module.aom.AomTypeCacheKey;

public interface JeeslAomTypeCache <REALM extends JeeslTenantRealm<?,?,REALM,?>,
									ATYPE extends JeeslAomAssetType<?,?,REALM,ATYPE,VIEW,?>,
									VIEW extends JeeslAomView<?,?,REALM,?>>
								extends Serializable
{	
	Map<AomTypeCacheKey,List<ATYPE>> getCachedType();
}