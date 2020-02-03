package org.jeesl.api.facade.module;

import java.util.List;

import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.module.aom.JeeslAomAsset;
import org.jeesl.interfaces.model.module.aom.JeeslAomStatus;
import org.jeesl.interfaces.model.module.aom.JeeslAomType;
import org.jeesl.interfaces.model.module.aom.company.JeeslAomCompany;
import org.jeesl.interfaces.model.module.aom.company.JeeslAomScope;
import org.jeesl.interfaces.model.module.aom.core.JeeslAomRealm;
import org.jeesl.interfaces.model.module.aom.event.JeeslAomEvent;
import org.jeesl.interfaces.model.module.aom.event.JeeslAomEventType;

import net.sf.ahtutils.interfaces.model.status.UtilsDescription;
import net.sf.ahtutils.interfaces.model.status.UtilsLang;
import net.sf.ahtutils.model.interfaces.with.EjbWithId;

public interface JeeslAssetFacade <L extends UtilsLang, D extends UtilsDescription,
									REALM extends JeeslAomRealm<L,D,REALM,?>,
									COMPANY extends JeeslAomCompany<REALM,SCOPE>,
									SCOPE extends JeeslAomScope<L,D,SCOPE,?>,
									ASSET extends JeeslAomAsset<REALM,ASSET,COMPANY,STATUS,ATYPE>,
									STATUS extends JeeslAomStatus<L,D,STATUS,?>,
									ATYPE extends JeeslAomType<L,D,REALM,ATYPE,?>,
									EVENT extends JeeslAomEvent<COMPANY,ASSET,ETYPE>,
									ETYPE extends JeeslAomEventType<L,D,ETYPE,?>>
			extends JeeslFacade
{	
	<RREF extends EjbWithId> ASSET fcAssetRoot(REALM realm, RREF rref);
	<RREF extends EjbWithId> ATYPE fcAssetRootType(REALM realm, RREF rref);
	<RREF extends EjbWithId> List<COMPANY> fAssetCompanies(REALM realm, RREF rref, SCOPE scope);
	List<EVENT> fAssetEvents(ASSET asset);
}