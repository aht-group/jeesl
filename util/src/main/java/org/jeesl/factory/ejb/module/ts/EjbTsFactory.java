package org.jeesl.factory.ejb.module.ts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeesl.interfaces.model.module.ts.core.JeeslTimeSeries;
import org.jeesl.interfaces.model.module.ts.core.JeeslTsEntityClass;
import org.jeesl.interfaces.model.module.ts.core.JeeslTsScope;
import org.jeesl.interfaces.model.module.ts.data.JeeslTsBridge;
import org.jeesl.interfaces.model.module.ts.stat.JeeslTsStatistic;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLangDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbTsFactory<SCOPE extends JeeslTsScope<?,?,?,?,UNIT,EC,INT>,
							UNIT extends JeeslStatus<UNIT,?,?>,
							TS extends JeeslTimeSeries<SCOPE,BRIDGE,INT,STAT>,
							SOURCE extends EjbWithLangDescription<?,?>, 
							BRIDGE extends JeeslTsBridge<EC>,
							EC extends JeeslTsEntityClass<?,?,?,?>,
							INT extends JeeslStatus<INT,?,?>,
							STAT extends JeeslTsStatistic<?,?,STAT,?>
							>
{
	final static Logger logger = LoggerFactory.getLogger(EjbTsFactory.class);
	
	final Class<TS> cTs;
    
	public EjbTsFactory(final Class<TS> cTs)
	{       
        this.cTs=cTs;
	}
	
	public TS build(SCOPE scope, INT interval, STAT stat, BRIDGE bridge)
	{
		TS ejb = null;
		try
		{
			ejb = cTs.newInstance();
			ejb.setScope(scope);
			ejb.setInterval(interval);
			ejb.setStatistic(stat);
			ejb.setBridge(bridge);
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		return ejb;
	}
	
	public List<Long> toBridgeIds(List<TS> list)
	{
		List<Long> result = new ArrayList<Long>();
		for(TS ts : list) {result.add(ts.getBridge().getRefId());}
		return result;
	}
	
	public Map<Long,TS> toMapBridgeRefIdTs(List<TS> list)
	{
		Map<Long,TS> map = new HashMap<Long,TS>();
		for(TS ts : list) {map.put(ts.getBridge().getRefId(),ts);}
		return map;
	}
	
	public Map<BRIDGE,List<TS>> toMapBridgTsList(List<TS> list)
	{
		Map<BRIDGE,List<TS>> map = new HashMap<BRIDGE,List<TS>>();
		for(TS ts : list)
		{
			if(!map.containsKey(ts.getBridge())) {map.put(ts.getBridge(), new ArrayList<TS>());}
			map.get(ts.getBridge()).add(ts);
		}
		return map;
	}
}