package org.jeesl.factory.ejb.module.ts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeesl.interfaces.model.module.ts.config.JeeslTsCategory;
import org.jeesl.interfaces.model.module.ts.core.JeeslTsScope;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbTsScopeFactory<CAT extends JeeslTsCategory<?,?,CAT,?>,
								SCOPE extends JeeslTsScope<?,?,CAT,?,UNIT,?,?>,
								UNIT extends JeeslStatus<?,?,UNIT>
								>
{
	final static Logger logger = LoggerFactory.getLogger(EjbTsScopeFactory.class);
	
	final Class<SCOPE> cScope;
    
	public EjbTsScopeFactory(final Class<SCOPE> cScope)
	{       
        this.cScope = cScope;
	}
	    
	public SCOPE build(UNIT unit)
	{
		SCOPE ejb = null;
		try
		{
			ejb = cScope.newInstance();
			ejb.setPosition(1);
			ejb.setVisible(true);
			ejb.setUnit(unit);
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		
		return ejb;
	}
	
	public Map<CAT,List<SCOPE>> toMapCategory(List<SCOPE> list)
	{
		Map<CAT,List<SCOPE>> map = new HashMap<>();
		for(SCOPE scope : list)
		{
			if(!map.containsKey(scope.getCategory())) {map.put(scope.getCategory(),new ArrayList<>());}
			map.get(scope.getCategory()).add(scope);
		}
		return map;
	}
}