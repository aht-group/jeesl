package org.jeesl.api.facade.module;

import java.util.List;

import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.io.fr.JeeslFileContainer;
import org.jeesl.interfaces.model.module.aom.asset.JeeslAomAsset;
import org.jeesl.interfaces.model.module.aom.asset.JeeslAomView;
import org.jeesl.interfaces.model.module.aom.asset.JeeslAomAssetStatus;
import org.jeesl.interfaces.model.module.aom.asset.JeeslAomAssetType;
import org.jeesl.interfaces.model.module.aom.company.JeeslAomCompany;
import org.jeesl.interfaces.model.module.aom.company.JeeslAomScope;
import org.jeesl.interfaces.model.module.aom.event.JeeslAomEvent;
import org.jeesl.interfaces.model.module.aom.event.JeeslAomEventStatus;
import org.jeesl.interfaces.model.module.aom.event.JeeslAomEventType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.mcs.JeeslMcsRealm;
import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface JeeslAssetFacade <L extends JeeslLang, D extends JeeslDescription,
									REALM extends JeeslMcsRealm<L,D,REALM,?>,
									COMPANY extends JeeslAomCompany<REALM,SCOPE>,
									SCOPE extends JeeslAomScope<L,D,SCOPE,?>,
									ASSET extends JeeslAomAsset<REALM,ASSET,COMPANY,STATUS,ATYPE>,
									STATUS extends JeeslAomAssetStatus<L,D,STATUS,?>,
									ATYPE extends JeeslAomAssetType<L,D,REALM,ATYPE,ALEVEL,?>,
									ALEVEL extends JeeslAomView<L,D,REALM,?>,
									EVENT extends JeeslAomEvent<COMPANY,ASSET,ETYPE,ESTATUS,USER,FRC>,
									ETYPE extends JeeslAomEventType<L,D,ETYPE,?>,
									ESTATUS extends JeeslAomEventStatus<L,D,ESTATUS,?>,
									USER extends JeeslSimpleUser,
									FRC extends JeeslFileContainer<?,?>>
			extends JeeslFacade
{	
	<RREF extends EjbWithId> ASSET fcAssetRoot(REALM realm, RREF rref);
	List<ASSET> allAssets(ASSET root);
	
	<RREF extends EjbWithId> ATYPE fcAssetRootType(REALM realm, RREF rref);
	<RREF extends EjbWithId> List<ALEVEL> fAomLevels(REALM realm, RREF rref);
	
	<RREF extends EjbWithId> List<COMPANY> fAssetCompanies(REALM realm, RREF rref);
	
	List<EVENT> fAssetEvents(ASSET asset);
	<RREF extends EjbWithId> List<EVENT> fAssetEvents(REALM realm, RREF rref, List<ESTATUS> status);
}