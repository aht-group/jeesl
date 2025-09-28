package org.jeesl.factory.ejb.module.ts;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.jeesl.factory.ejb.util.EjbIdFactory;
import org.jeesl.interfaces.model.module.ts.core.JeeslTimeSeries;
import org.jeesl.interfaces.model.module.ts.core.JeeslTsEntityClass;
import org.jeesl.interfaces.model.module.ts.core.JeeslTsMultiPoint;
import org.jeesl.interfaces.model.module.ts.core.JeeslTsScope;
import org.jeesl.interfaces.model.module.ts.data.JeeslTsBridge;
import org.jeesl.interfaces.model.module.ts.data.JeeslTsData;
import org.jeesl.interfaces.model.module.ts.data.JeeslTsDataPoint;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.model.pojo.map.generic.Nested2Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbTsBridgeFactory<SCOPE extends JeeslTsScope<?,?,?,?,?,EC,?>,
								MP extends JeeslTsMultiPoint<?,?,?,?,?>,
								TS extends JeeslTimeSeries<SCOPE,TS,BRIDGE,?,?>,
								BRIDGE extends JeeslTsBridge<EC>,
								EC extends JeeslTsEntityClass<?,?,?,?>,
								DATA extends JeeslTsData<TS,?,?,POINT,?>,
								POINT extends JeeslTsDataPoint<DATA,MP>
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
			ejb = cBridge.getDeclaredConstructor().newInstance();
			ejb.setEntityClass(entityClass);
			ejb.setRefId(refId);
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		catch (IllegalArgumentException e) {e.printStackTrace();}
		catch (InvocationTargetException e) {e.printStackTrace();}
		catch (NoSuchMethodException e) {e.printStackTrace();}
		catch (SecurityException e) {e.printStackTrace();}
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
		Map<T,BRIDGE> map = new HashMap<>();
		Map<Long,T> idMap = EjbIdFactory.toIdMap(ejbs);
		for(BRIDGE bridge : bridges)
		{
			if(idMap.containsKey(bridge.getRefId())) {map.put(idMap.get(bridge.getRefId()),bridge);}
		}
		return map;
	}
	public <T extends EjbWithId> BidiMap<T,BRIDGE> toBidiMap(List<BRIDGE> bridges, List<T> ejbs)
	{
		Map<Long,T> mapEjbs = EjbIdFactory.toIdMap(ejbs);
		BidiMap<T,BRIDGE> map = new DualHashBidiMap<>();
		for(BRIDGE b : bridges)
		{
			if(mapEjbs.containsKey(b.getRefId()))
			{
				map.put(mapEjbs.get(b.getRefId()),b);
			}
		}
		return map;
	}
	
	public <T extends EjbWithId> Map<T,DATA> toMapEntityData(List<T> ejbs, List<DATA> datas)
	{
		Map<T,DATA> map = new HashMap<>();
		Map<Long,T> idMap = EjbIdFactory.toIdMap(ejbs);
		for(DATA data : datas)
		{
			long refId = data.getTimeSeries().getBridge().getRefId();
			if(idMap.containsKey(refId)) {map.put(idMap.get(refId),data);}
		}
		return map;
	}
	
	public <T extends EjbWithId> Map<T,List<DATA>> toMapEntityDatas(List<T> ejbs, List<DATA> datas)
	{
		Map<T,List<DATA>> map = new HashMap<>();
		Map<Long,T> idMap = EjbIdFactory.toIdMap(ejbs);
		for(DATA data : datas)
		{
			long refId = data.getTimeSeries().getBridge().getRefId();
			if(idMap.containsKey(refId))
			{
				T t = idMap.get(refId);
				if(!map.containsKey(t)) {map.put(t,new ArrayList<>());}
				map.get(t).add(data);
			}
		}
		return map;
	}
	
	public <T extends EjbWithId> Nested2Map<T,SCOPE,DATA> toN2mEntityScopeData(List<T> ejbs, List<DATA> datas)
	{
		Nested2Map<T,SCOPE,DATA> n2m = new Nested2Map<>();
		Map<Long,T> idMap = EjbIdFactory.toIdMap(ejbs);
		for(DATA d : datas)
		{
			long refId = d.getTimeSeries().getBridge().getRefId();
			if(idMap.containsKey(refId)) {n2m.put(idMap.get(refId),d.getTimeSeries().getScope(),d);}
		}
		return n2m;
	}
	
	public <T extends EjbWithId> Nested2Map<T,MP,POINT> toN2mPoint(List<T> ejbs, List<POINT> points)
	{
		Nested2Map<T,MP,POINT> n2m = new Nested2Map<>();
		Map<Long,T> idMap = EjbIdFactory.toIdMap(ejbs);
		for(POINT p : points)
		{
			long refId = p.getData().getTimeSeries().getBridge().getRefId();
			if(idMap.containsKey(refId)) {n2m.put(idMap.get(refId),p.getMultiPoint(),p);}
		}
		return n2m;
	}
}