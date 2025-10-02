package org.jeesl.interfaces.model.module.aom.asset;

import java.io.Serializable;
import java.util.List;

import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.module.aom.company.JeeslAomCompany;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.parent.EjbWithParentAttributeResolver;
import org.jeesl.interfaces.model.with.parent.EjbWithParentId;
import org.jeesl.interfaces.model.with.parent.EjbWithParentPosition;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithNonUniqueCode;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPosition;
import org.jeesl.interfaces.model.with.primitive.text.EjbWithName;
import org.jeesl.interfaces.model.with.primitive.text.EjbWithRemark;
import org.jeesl.interfaces.model.with.system.status.JeeslWithStatus;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslAomAsset <REALM extends JeeslTenantRealm<?,?,REALM,?>,
							ASSET extends JeeslAomAsset<REALM,ASSET,COMPANY,STATUS,ATYPE>,
							COMPANY extends JeeslAomCompany<REALM,?>,
							STATUS extends JeeslAomAssetStatus<?,?,STATUS,?>,
							ATYPE extends JeeslAomAssetType<?,?,REALM,ATYPE,?,?>>
			extends Serializable,EjbSaveable,
					EjbWithPosition,EjbWithParentAttributeResolver,EjbWithParentId<ASSET>,EjbWithParentPosition<ASSET>,
					EjbWithNonUniqueCode,EjbWithName,EjbWithRemark,
//					JeeslWithTenantSupport<REALM>,
					JeeslWithStatus<STATUS>
					
{
	public enum Attributes{realm,realmIdentifier,parent,type1,type2,position}
	
	REALM getRealm();
	void setRealm(REALM realm);
	
	long getRealmIdentifier();
	void setRealmIdentifier(long realmIdentifier);
	
	ASSET getParent();
	void setParent(ASSET parent);
	
	ATYPE getType1();
	void setType1(ATYPE type1);
	
//	ATYPE getType2();
//	void setType2(ATYPE type2);
	
	COMPANY getManufacturer();
	void setManufacturer(COMPANY manufacturer);
	
	List<ASSET> getAssets();
	void setAssets(List<ASSET> assets);
}