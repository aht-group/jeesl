package org.jeesl.controller.web.module.aom;

import java.io.Serializable;
import java.util.List;

import org.jeesl.interfaces.model.module.aom.asset.JeeslAomAssetType;
import org.jeesl.interfaces.model.module.aom.asset.JeeslAomView;
import org.jeesl.interfaces.model.module.aom.company.JeeslAomScope;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.model.ejb.module.aom.AomScopeCacheKey;
import org.jeesl.model.ejb.module.aom.AomTypeCacheKey;
import org.jeesl.model.ejb.system.tenant.TenantIdentifier;

public class JeeslAomCacheKey <REALM extends JeeslTenantRealm<?,?,REALM,?>,
										SCOPE extends JeeslAomScope<?,?,SCOPE,?>,
										ATYPE extends JeeslAomAssetType<?,?,REALM,ATYPE,VIEW,?>,
										VIEW extends JeeslAomView<?,?,REALM,?>
										>
					implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private AomScopeCacheKey scopeManufacturer; public AomScopeCacheKey getScopeManufacturer() {return scopeManufacturer;}
	
	private AomTypeCacheKey typeNatural;
	
	public AomTypeCacheKey getTypeNatural() {
		return typeNatural;
	}

	public void setTypeNatural(AomTypeCacheKey typeNatural) {
		this.typeNatural = typeNatural;
	}

	public JeeslAomCacheKey()
	{
		
	}
	
	public void update(TenantIdentifier<REALM> identifier, List<SCOPE> scopes)
	{
		for(SCOPE scope : scopes)
		{
			if(scope.getCode().equals(JeeslAomScope.Code.manufacturer.toString())) {scopeManufacturer = AomScopeCacheKey.instance(identifier).withScope(scope);}
		}
		
		typeNatural = AomTypeCacheKey.instance(identifier).withView(JeeslAomView.Tree.hierarchy);

	}
}