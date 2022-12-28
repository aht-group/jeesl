package org.jeesl.api.facade.module;

import java.util.List;

import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.module.aom.asset.JeeslAomAsset;
import org.jeesl.interfaces.model.module.aom.asset.JeeslAomAssetStatus;
import org.jeesl.interfaces.model.module.aom.asset.JeeslAomAssetType;
import org.jeesl.interfaces.model.module.aom.asset.JeeslAomView;
import org.jeesl.interfaces.model.module.aom.company.JeeslAomCompany;
import org.jeesl.interfaces.model.module.aom.event.JeeslAomEvent;
import org.jeesl.interfaces.model.module.aom.event.JeeslAomEventStatus;
import org.jeesl.interfaces.model.module.aom.event.JeeslAomEventType;
import org.jeesl.interfaces.model.module.aom.event.JeeslAomEventUpload;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.model.ejb.system.tenant.TenantIdentifier;
import org.jeesl.model.json.db.tuple.t1.Json1Tuples;

public interface JeeslAomFacade <L extends JeeslLang, D extends JeeslDescription,
									REALM extends JeeslTenantRealm<L,D,REALM,?>,
									COMPANY extends JeeslAomCompany<REALM,?>,
									ASSET extends JeeslAomAsset<REALM,ASSET,COMPANY,STATUS,ATYPE>,
									STATUS extends JeeslAomAssetStatus<L,D,STATUS,?>,
									ATYPE extends JeeslAomAssetType<L,D,REALM,ATYPE,VIEW,?>,
									VIEW extends JeeslAomView<L,D,REALM,?>,
									EVENT extends JeeslAomEvent<COMPANY,ASSET,ETYPE,ESTATUS,?,?,?>,
									ETYPE extends JeeslAomEventType<L,D,ETYPE,?>,
									ESTATUS extends JeeslAomEventStatus<L,D,ESTATUS,?>,
									UP extends JeeslAomEventUpload<L,D,UP,?>>
			extends JeeslFacade
{
	EVENT load(EVENT event);
	
	<RREF extends EjbWithId> ASSET fcAssetRoot(REALM realm, RREF rref);
	<RREF extends EjbWithId> List<ASSET> fAomAssets(REALM realm, RREF rref, ATYPE type1, ATYPE type2);
	List<ASSET> allAssets(ASSET root);
	
	<RREF extends EjbWithId> VIEW fAomView(REALM realm, RREF rref, JeeslAomView.Tree tree) throws JeeslNotFoundException;
	<RREF extends EjbWithId> VIEW fcAomView(REALM realm, RREF rref, JeeslAomView.Tree tree);
	List<VIEW> fAomViews(TenantIdentifier<REALM> identifier);
	
	List<ATYPE> fAomAssetTypes(TenantIdentifier<REALM> identifier, VIEW view);
	
	List<COMPANY> fAomCompanies(TenantIdentifier<REALM> identifier);
	
	List<EVENT> fAssetEvents(ASSET asset);
	<RREF extends EjbWithId> List<EVENT> fAssetEvents(REALM realm, RREF rref, List<ESTATUS> status);
	
	Json1Tuples<VIEW> tpcTypeByView(TenantIdentifier<REALM> identifier);
}