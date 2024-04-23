package org.jeesl.util.filter.ejb.module.aom;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.ObjectUtils;
import org.jeesl.interfaces.model.module.aom.asset.JeeslAomAsset;
import org.jeesl.interfaces.model.module.aom.event.JeeslAomEvent;

public class EjbAomEventFilter<ASSET extends JeeslAomAsset<?,ASSET,?,?,?>,
								EVENT extends JeeslAomEvent<?,ASSET,?,?,?,?,?>>
{
	private final EjbAomAssetFilter<ASSET> fiAsset;
	
    public EjbAomEventFilter()
    {
    	fiAsset = new EjbAomAssetFilter<>();
    }
    
    public List<EVENT> toChilds(List<EVENT> list, Set<ASSET> roots)
    {
    	List<EVENT> result = new ArrayList<>();
    	for(EVENT e : list)
    	{
    		if(this.isChild(e, roots)) {result.add(e);}
    	}
    	return result;
    }
	
	public boolean isChild(EVENT event, Set<ASSET> roots)
	{
		if(ObjectUtils.isEmpty(event.getAssets())) {return false;}
		boolean isChild = false;
		for(ASSET asset : event.getAssets())
		{
			isChild = fiAsset.isChild(asset, roots);
			if(isChild) {return true;}
		}
		return isChild;
    }
}