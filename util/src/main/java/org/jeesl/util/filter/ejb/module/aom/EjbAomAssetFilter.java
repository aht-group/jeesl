package org.jeesl.util.filter.ejb.module.aom;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang3.ObjectUtils;
import org.jeesl.interfaces.model.module.aom.asset.JeeslAomAsset;

public class EjbAomAssetFilter<ASSET extends JeeslAomAsset<?,ASSET,?,?,?>>
{
	
    public EjbAomAssetFilter()
    {

    }
	
    public boolean isChild(ASSET asset, ASSET parent)
    {
    	Set<ASSET> set = new HashSet<ASSET>();
    	set.add(parent);
    	return this.isChild(asset, set);
    }
	public boolean isChild(ASSET asset, Set<ASSET> roots)
	{
		if(ObjectUtils.isEmpty(roots)) {return false;}
		else if(roots.contains(asset)) {return true;}
		else
		{
			if(Objects.isNull(asset.getParent())) {return false;}
			else {return this.isChild(asset.getParent(), roots);}
		}
    }
}