package org.jeesl.factory.ejb.module.asset;

import java.util.UUID;

import org.jeesl.factory.builder.module.AomFactoryBuilder;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.module.aom.asset.JeeslAomAsset;
import org.jeesl.interfaces.model.module.aom.asset.JeeslAomAssetStatus;
import org.jeesl.interfaces.model.module.aom.asset.JeeslAomAssetType;
import org.jeesl.interfaces.model.module.aom.company.JeeslAomCompany;
import org.jeesl.interfaces.model.module.aom.company.JeeslAomScope;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbAssetFactory<REALM extends JeeslTenantRealm<?,?,REALM,?>,
							COMPANY extends JeeslAomCompany<REALM,SCOPE>,
							SCOPE extends JeeslAomScope<?,?,SCOPE,?>,
							ASSET extends JeeslAomAsset<REALM,ASSET,COMPANY,STATUS,TYPE>,
							STATUS extends JeeslAomAssetStatus<?,?,STATUS,?>,
							TYPE extends JeeslAomAssetType<?,?,REALM,TYPE,?,?>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbAssetFactory.class);
	
	private final AomFactoryBuilder<?,?,?,COMPANY,SCOPE,ASSET,STATUS,TYPE,?,?,?,?,?,?,?,?,?> fbAsset;
	
    public EjbAssetFactory(final AomFactoryBuilder<?,?,?,COMPANY,SCOPE,ASSET,STATUS,TYPE,?,?,?,?,?,?,?,?,?> fbAsset)
    {
        this.fbAsset = fbAsset;
    }
	
	public <RREF extends EjbWithId> ASSET build(REALM realm, RREF ref, ASSET parent, STATUS status, TYPE type1)
	{
		try
		{
			ASSET ejb = fbAsset.getClassAsset().newInstance();
			ejb.setRealm(realm);
			ejb.setRealmIdentifier(ref.getId());
			ejb.setCode(UUID.randomUUID().toString());
			ejb.setParent(parent);
			ejb.setStatus(status);
			ejb.setType1(type1);
			ejb.setName("");
			ejb.setRemark("");
		    return ejb;
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		return null;
    }
	
	public void converter(JeeslFacade facade, ASSET asset)
	{
		if(asset.getManufacturer()!=null) {asset.setManufacturer(facade.find(fbAsset.getClassCompany(),asset.getManufacturer()));}
	}
}