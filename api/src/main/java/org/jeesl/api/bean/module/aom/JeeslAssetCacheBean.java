package org.jeesl.api.bean.module.aom;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.jeesl.interfaces.model.module.aom.asset.JeeslAomAsset;
import org.jeesl.interfaces.model.module.aom.asset.JeeslAomView;
import org.jeesl.interfaces.model.module.aom.asset.JeeslAomStatus;
import org.jeesl.interfaces.model.module.aom.asset.JeeslAomType;
import org.jeesl.interfaces.model.module.aom.company.JeeslAomCompany;
import org.jeesl.interfaces.model.module.aom.company.JeeslAomScope;
import org.jeesl.interfaces.model.module.aom.event.JeeslAomEventType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.mcs.JeeslMcsRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface JeeslAssetCacheBean <L extends JeeslLang, D extends JeeslDescription,
										REALM extends JeeslMcsRealm<L,D,REALM,?>, RREF extends EjbWithId,
										COMPANY extends JeeslAomCompany<REALM,SCOPE>,
										SCOPE extends JeeslAomScope<L,D,SCOPE,?>,
										ASSET extends JeeslAomAsset<REALM,ASSET,COMPANY,ASTATUS,ATYPE>,
										ASTATUS extends JeeslAomStatus<L,D,ASTATUS,?>,
										ATYPE extends JeeslAomType<L,D,REALM,ATYPE,ALEVEL,?>,
										ALEVEL extends JeeslAomView<L,D,REALM,?>,
										ETYPE extends JeeslAomEventType<L,D,ETYPE,?>>
								extends Serializable
{
//	Map<RREF,List<COMPANY>> cachedLevel();
	
	
//	void reloadAssetTypes(JeeslAssetFacade<L,D,REALM,COMPANY,SCOPE,ASSET,STATUS,TYPE> fAsset, REALM realm, RREF rref);
	Map<REALM,Map<RREF,List<ATYPE>>> getMapAssetType();
	List<ETYPE> getEventType();
	
	Map<RREF,List<COMPANY>> cachedCompany();
	Map<RREF,List<COMPANY>> getMapVendor();
	Map<RREF,List<COMPANY>> getMapMaintainer();
	
	void update(REALM realm, RREF rref, COMPANY company);
	void update(REALM realm, RREF rref, ATYPE type);
	void delete(REALM realm, RREF rref, ATYPE type);
}