package org.jeesl.factory.ejb.module.ts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeesl.factory.ejb.util.EjbIdFactory;
import org.jeesl.interfaces.model.module.ts.core.JeeslTimeSeries;
import org.jeesl.interfaces.model.module.ts.core.JeeslTsEntityClass;
import org.jeesl.interfaces.model.module.ts.data.JeeslTsBridge;
import org.jeesl.interfaces.model.module.ts.data.JeeslTsData;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbTsBridgeFactory<TS extends JeeslTimeSeries<?,TS,BRIDGE,?,?>,
								BRIDGE extends JeeslTsBridge<EC>,
								EC extends JeeslTsEntityClass<?,?,?,?>,
								DATA extends JeeslTsData<TS,?,?,?>
								>
{
	final static Logger logger = LoggerFactory.getLogger(EjbTsBridgeFactory.class);
	
	final Class<BRIDGE> cBridge;
    
	public EjbTsBridgeFactory(final Class<BRIDGE> cBridge)
	{       
        this.cBridge=cBridge;
	}

	public BRIDGE build(EC entityClass, long refId)
	{
		BRIDGE ejb = null;
		try
		{
			ejb = cBridge.newInstance();
			ejb.setEntityClass(entityClass);
			ejb.setRefId(refId);
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		return ejb;
	}
	
	public Map<EC,List<Long>> dataToBridgeIds(List<DATA> datas)
	{
		Map<EC,List<Long>> map = new HashMap<EC,List<Long>>();
		for(DATA data : datas)
		{
			EC ec = data.getTimeSeries().getBridge().getEntityClass();
			Long refId = data.getTimeSeries().getBridge().getRefId();
			
			if(!map.containsKey(ec)){map.put(ec, new ArrayList<Long>());}
			if(!map.get(ec).contains(refId)){map.get(ec).add(refId);}
		}
		return map;
	}
	
	public List<Long> toRefIds(List<BRIDGE> bridges)
	{
		List<Long> list = new ArrayList<Long>();
		for(BRIDGE bridge : bridges)
		{
			list.add(bridge.getRefId());
		}
		return list;
	}
	
	public <T extends EjbWithId> Map<T,BRIDGE> toMapEjbBridge(List<BRIDGE> bridges, List<T> ejbs)
	{
		Map<T,BRIDGE> map = new HashMap<T,BRIDGE>();
		Map<Long,T> idMap = EjbIdFactory.toIdMap(ejbs);
		for(BRIDGE bridge : bridges)
		{
			if(idMap.containsKey(bridge.getRefId())) {map.put(idMap.get(bridge.getRefId()),bridge);}
		}
		return map;
	}
}