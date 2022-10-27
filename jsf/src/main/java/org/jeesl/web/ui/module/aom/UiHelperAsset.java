package org.jeesl.web.ui.module.aom;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jeesl.api.bean.module.aom.JeeslAssetCacheBean;
import org.jeesl.factory.ejb.util.EjbIdFactory;
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
import org.jeesl.interfaces.model.module.aom.event.JeeslAomEventUpload;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslMarkup;
import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UiHelperAsset <L extends JeeslLang, D extends JeeslDescription,
								REALM extends JeeslTenantRealm<L,D,REALM,?>, RREF extends EjbWithId,
								COMPANY extends JeeslAomCompany<REALM,SCOPE>,
								SCOPE extends JeeslAomScope<L,D,SCOPE,?>,
								ASSET extends JeeslAomAsset<REALM,ASSET,COMPANY,ASTATUS,ATYPE>,
								ASTATUS extends JeeslAomAssetStatus<L,D,ASTATUS,?>,
								ATYPE extends JeeslAomAssetType<L,D,REALM,ATYPE,ALEVEL,?>,
								ALEVEL extends JeeslAomView<L,D,REALM,?>,
								EVENT extends JeeslAomEvent<COMPANY,ASSET,ETYPE,ESTATUS,M,USER,FRC>,
								ETYPE extends JeeslAomEventType<L,D,ETYPE,?>,
								ESTATUS extends JeeslAomEventStatus<L,D,ESTATUS,?>,
								M extends JeeslMarkup<?>,
								USER extends JeeslSimpleUser,
								FRC extends JeeslFileContainer<?,?>,
								UP extends JeeslAomEventUpload<L,D,UP,?>>
					implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(UiHelperAsset.class);
		
	private JeeslAssetCacheBean<L,D,REALM,RREF,COMPANY,SCOPE,ASSET,ASTATUS,ATYPE,ALEVEL,ETYPE,UP> bCache; public void setCacheBean(JeeslAssetCacheBean<L,D,REALM,RREF,COMPANY,SCOPE,ASSET,ASTATUS,ATYPE,ALEVEL,ETYPE,UP> bCache) {this.bCache = bCache;}
	
	private final List<COMPANY> companies; public List<COMPANY> getCompanies() {return companies;}
	
	private boolean showAmount; public boolean isShowAmount() {return showAmount;}	
	private boolean showCompany; public boolean isShowCompany() {return showCompany;}
	
	public UiHelperAsset()
	{
		companies = new ArrayList<>();
		reset();
	}
	
	private void reset()
	{
		companies.clear();
		
		showAmount = false;
		showCompany = false;
	}

	public void update(REALM realm, RREF rref, EVENT event)
	{
		reset();
		boolean isQuote = event.getType().getCode().equals(JeeslAomEventType.Code.quote.toString());
		boolean isProcurement = event.getType().getCode().equals(JeeslAomEventType.Code.procurement.toString());
		boolean isDeployment = event.getType().getCode().equals(JeeslAomEventType.Code.deployment.toString());
		boolean isMaintenance = event.getType().getCode().equals(JeeslAomEventType.Code.maintenance.toString());
		boolean isRenew = event.getType().getCode().equals(JeeslAomEventType.Code.renew.toString());
		
		showAmount = EjbIdFactory.isSaved(event) && (isQuote || isProcurement || isDeployment || isMaintenance || isRenew);
		
		if(EjbIdFactory.isSaved(event) && bCache!=null)
		{
			if(isQuote)
			{
				companies.addAll(bCache.cachedCompany().get(rref));
			}
			else if(isProcurement)
			{
				companies.addAll(bCache.getMapVendor().get(rref));
			}
			else if(isDeployment)
			{
				companies.addAll(bCache.getMapMaintainer().get(rref));
			}
			else if(isMaintenance || isRenew)
			{
				companies.addAll(bCache.getMapMaintainer().get(rref));
			}
			showCompany = isQuote || isProcurement || isDeployment || isMaintenance || isRenew; 
		}
	}
}