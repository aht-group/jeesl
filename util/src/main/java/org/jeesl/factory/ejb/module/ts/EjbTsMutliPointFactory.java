package org.jeesl.factory.ejb.module.ts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeesl.interfaces.model.module.ts.core.JeeslTsMultiPoint;
import org.jeesl.interfaces.model.module.ts.core.JeeslTsScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbTsMutliPointFactory<SCOPE extends JeeslTsScope<?,?,?,?,?,?,?>,
									MP extends JeeslTsMultiPoint<?,?,SCOPE,?,?>
									>
{
	final static Logger logger = LoggerFactory.getLogger(EjbTsMutliPointFactory.class);
	
	private final Class<MP> cMp;
    
	public EjbTsMutliPointFactory(final Class<MP> cMp)
	{       
        this.cMp=cMp;
	}
	
	public MP build(SCOPE scope, List<MP> list)
	{
		MP ejb = null;
		try
		{
			ejb = cMp.newInstance();
			ejb.setScope(scope);
			if(list==null) {ejb.setPosition(1);}
			else {ejb.setPosition(list.size()+1);}
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		return ejb;
	}
	
	public Map<SCOPE,List<MP>> toMapScope(List<MP> list)
	{
		Map<SCOPE,List<MP>> map = new HashMap<>();
		for(MP mp : list)
		{
			if(!map.containsKey(mp.getScope())) {map.put(mp.getScope(),new ArrayList<>());}
			map.get(mp.getScope()).add(mp);
		}
		return map;
	}
}