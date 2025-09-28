package org.jeesl.factory.ejb.module.ts;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.ListUtils;
import org.jeesl.interfaces.model.module.ts.core.JeeslTimeSeries;
import org.jeesl.interfaces.model.module.ts.core.JeeslTsMultiPoint;
import org.jeesl.interfaces.model.module.ts.data.JeeslTsData;
import org.jeesl.interfaces.model.module.ts.data.JeeslTsDataPoint;
import org.jeesl.model.pojo.map.generic.Nested2List;
import org.jeesl.model.pojo.map.generic.Nested2Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbTsDataPointFactory< TS extends JeeslTimeSeries<?,TS,?,?,?>,
								MP extends JeeslTsMultiPoint<?,?,?,?,?>,
								DATA extends JeeslTsData<TS,?,?,POINT,?>,
								POINT extends JeeslTsDataPoint<DATA,MP>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbTsDataPointFactory.class);
	
	private final Class<POINT> cPoint;
    
	public EjbTsDataPointFactory(final Class<POINT> cPoint)
	{       
        this.cPoint=cPoint;
	}
	
	public POINT build(DATA data, MP multiPoint, Double value)
	{
		POINT ejb = null;
		try
		{
			ejb = cPoint.getDeclaredConstructor().newInstance();
			ejb.setData(data);
			ejb.setMultiPoint(multiPoint);
			ejb.setValue(value);
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		catch (IllegalArgumentException e) {e.printStackTrace();}
		catch (InvocationTargetException e) {e.printStackTrace();}
		catch (NoSuchMethodException e) {e.printStackTrace();}
		catch (SecurityException e) {e.printStackTrace();}
		return ejb;
	}
	
	public Map<DATA,List<POINT>> toMapData(List<POINT> list)
	{
		Map<DATA,List<POINT>> map = new HashMap<DATA,List<POINT>>();
		for(POINT p : list)
		{
			if(!map.containsKey(p.getData())) {map.put(p.getData(),new ArrayList<>());}
			map.get(p.getData()).add(p);
			
		}
		return map;
	}
	
	public Map<MP,List<POINT>> toMapMultiPoint(List<POINT> list)
	{
		Map<MP,List<POINT>> map = new HashMap<MP,List<POINT>>();
		for(POINT p : list)
		{
			if(!map.containsKey(p.getMultiPoint())) {map.put(p.getMultiPoint(),new ArrayList<>());}
			map.get(p.getMultiPoint()).add(p);
			
		}
		return map;
	}
	
	public Map<MP,POINT> toMapMultiPointUnique(List<POINT> list)
	{
		Map<MP,POINT> map = new HashMap<MP,POINT>();
		for(POINT p : list)
		{
			map.put(p.getMultiPoint(),p);
		}
		return map;
	}
	public Map<DATA,POINT> toMapDataUnique(List<POINT> list)
	{
		Map<DATA,POINT> map = new HashMap<DATA,POINT>();
		for(POINT p : list)
		{
			map.put(p.getData(),p);
		}
		return map;
	}
	
	public Nested2Map<DATA,MP,POINT> toN2Map(List<POINT> list)
	{
		Nested2Map<DATA,MP,POINT> map = new Nested2Map<>();
		for(POINT p : list)
		{
			map.put(p.getData(),p.getMultiPoint(),p);
		}
		
		return map;
	}
	
	public Nested2List<TS,MP,POINT> toN2List(List<DATA> list)
	{
		Nested2List<TS,MP,POINT> n2l = new Nested2List<>();
		for(DATA d : list)
		{
			for(POINT p : ListUtils.emptyIfNull(d.getDataPoints()))
			{
				n2l.put(p.getData().getTimeSeries(),p.getMultiPoint(),p);
			}
		}
		return n2l;
	}
	public Nested2List<TS,MP,POINT> n2lFromPoints(List<POINT> list)
	{
		Nested2List<TS,MP,POINT> n2l = new Nested2List<>();
		for(POINT p : ListUtils.emptyIfNull(list))
		{
			n2l.put(p.getData().getTimeSeries(),p.getMultiPoint(),p);
		}
		return n2l;
	}
}