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
import org.jeesl.interfaces.util.query.module.JeeslAomQuery;
import org.jeesl.model.ejb.system.tenant.TenantIdentifier;
import org.jeesl.model.json.io.db.tuple.container.JsonTuples1;

public interface JeeslAomFacade <L extends JeeslLang, D extends JeeslDescription,
									REALM extends JeeslTenantRealm<L,D,REALM,?>,
									COMPANY extends JeeslAomCompany<REALM,?>,
									ASSET extends JeeslAomAsset<REALM,ASSET,COMPANY,STATUS,ATYPE>,
									STATUS extends JeeslAomAssetStatus<L,D,STATUS,?>,
									ATYPE extends JeeslAomAssetType<L,D,REALM,ATYPE,VIEW,?>,
									VIEW extends JeeslAomView<L,D,REALM,?>,
									EVENT extends JeeslAomEvent<COMPANY,ASSET,?,ESTATUS,?,?,?>,
									
									ESTATUS extends JeeslAomEventStatus<L,D,ESTATUS,?>,
									UP extends JeeslAomEventUpload<L,D,UP,?>>
			extends JeeslFacade
{
	EVENT load(EVENT event);
	
	<RREF extends EjbWithId> List<ASSET> fAomAssets(REALM realm, RREF rref, ATYPE type1, ATYPE type2);
	List<ASSET> fAomAssets(TenantIdentifier<REALM> identifier);
	List<ASSET> allAssets(ASSET root);
	
	
	<RREF extends EjbWithId> VIEW fAomView(REALM realm, RREF rref, JeeslAomView.Tree tree) throws JeeslNotFoundException;
	<RREF extends EjbWithId> VIEW fcAomView(REALM realm, RREF rref, JeeslAomView.Tree tree);
	List<VIEW> fAomViews(TenantIdentifier<REALM> identifier);
	
	List<ATYPE> fAomAssetTypes(TenantIdentifier<REALM> identifier, VIEW view);
	
	List<COMPANY> fAomCompanies(TenantIdentifier<REALM> identifier);
	
//	List<EVENT> fAssetEvents(ASSET asset);
	<RREF extends EjbWithId> List<EVENT> fAssetEvents(REALM realm, RREF rref, List<ESTATUS> status);
	List<EVENT> fAomEvents(JeeslAomQuery<REALM,ASSET,EVENT> query);
	
	JsonTuples1<VIEW> tpcTypeByView(TenantIdentifier<REALM> identifier);
}