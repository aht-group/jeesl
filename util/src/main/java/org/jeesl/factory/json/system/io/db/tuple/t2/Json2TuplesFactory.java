package org.jeesl.factory.json.system.io.db.tuple.t2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Tuple;

import org.apache.commons.collections.keyvalue.MultiKey;
import org.jeesl.factory.ejb.util.EjbIdFactory;
import org.jeesl.factory.json.io.db.tuple.JsonTupleFactory;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.model.ejb.io.db.JeeslCq;
import org.jeesl.model.json.io.db.tuple.container.JsonTuples1;
import org.jeesl.model.json.io.db.tuple.container.JsonTuples2;
import org.jeesl.model.json.io.db.tuple.instance.JsonTuple2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Json2TuplesFactory <A extends EjbWithId, B extends EjbWithId>
{
	final static Logger logger = LoggerFactory.getLogger(Json2TuplesFactory.class);
	
	private JeeslFacade fUtils; public JeeslFacade getfUtils() {return fUtils;} public void setfUtils(JeeslFacade fUtils) {this.fUtils = fUtils;}
	public Json2TuplesFactory<A,B> facade(JeeslFacade facade) {this.fUtils=facade; return this;}
	
	private final Class<A> cA;
	private final Class<B> cB;
	
	private final Set<Long> setA;
	private final Set<Long> setB;
	
	protected final Map<Long,A> mapA; public Map<Long,A> getMapA() {return mapA;}
	protected final Map<Long,B> mapB; public Map<Long,B> getMapB() {return mapB;}
	
	protected final Map<A,Map<B,JsonTuple2<A,B>>> map2; public Map<A,Map<B,JsonTuple2<A, B>>> getMap2() {return map2;}
	private JsonTuples2<A,B> tuples; public JsonTuples2<A,B> get2Tuples() {return tuples;} public void set2Tuples(JsonTuples2<A,B> tuples) {this.tuples = tuples;}

	private boolean debugOnInfo = false;

	public static <A extends EjbWithId, B extends EjbWithId> Json2TuplesFactory<A,B> instance(Class<A> cA, Class<B> cB) {return new Json2TuplesFactory<>(cA,cB);}
	public Json2TuplesFactory(Class<A> cA, Class<B> cY)
	{
		this.cA=cA;
		this.cB=cY;
		
		setA = new HashSet<Long>();
		setB = new HashSet<Long>();
		
		mapA = new HashMap<Long,A>();
		mapB = new HashMap<Long,B>();
		
		map2 = new HashMap<A,Map<B,JsonTuple2<A,B>>>();
	}
	
	@Deprecated //use instanace().facade
	public Json2TuplesFactory(JeeslFacade fUtils, Class<A> cA, Class<B> cY)
	{
		this.fUtils=fUtils;
		this.cA=cA;
		this.cB=cY;
		
		setA = new HashSet<Long>();
		setB = new HashSet<Long>();
		
		mapA = new HashMap<Long,A>();
		mapB = new HashMap<Long,B>();
		
		map2 = new HashMap<A,Map<B,JsonTuple2<A,B>>>();
	}
	
	public Json2TuplesFactory<A,B> tupleLoad(JeeslFacade facade, boolean load)
	{
		if(load) {this.fUtils=facade;}
		return this;
	}
	
	protected void clear()
	{
		setA.clear();
		setB.clear();
		
		mapA.clear();
		mapB.clear();
		
		map2.clear();
	}
	
	public JsonTuples2<A,B> build2(){return new JsonTuples2<A,B>();}
	
	
	
	private void ejb2Load(JsonTuples2<A,B> json)
	{
		for(JsonTuple2<A,B> t : json.getTuples())
		{
			setA.add(t.getId1());
			setB.add(t.getId2());
		}
		
		if(fUtils==null)
		{	// A object is created and the corresponding id is set
			for(JsonTuple2<A,B> t : json.getTuples())
			{
				try
				{
					t.setEjb1(cA.newInstance());t.getEjb1().setId(t.getId1());
					t.setEjb2(cB.newInstance());if(t.getId2()!=null) {t.getEjb2().setId(t.getId2());}
				}
				catch (InstantiationException | IllegalAccessException e) {e.printStackTrace();}
			}
		}
		else
		{	// Here we really load the objects from the DB
			Map<Long,A> map1 = EjbIdFactory.toIdMap(fUtils.find(cA,setA));
			Map<Long,B> map2 = EjbIdFactory.toIdMap(fUtils.find(cB,setB));
			
			for(JsonTuple2<A,B> t : json.getTuples())
			{
				t.setEjb1(map1.get(t.getId1()));
				t.setEjb2(map2.get(t.getId2()));
			}
		}
	}
	
	public List<A> idToListX(JsonTuples2<A,B> tuples)
	{
		Set<Long> set = new HashSet<Long>();
		for(JsonTuple2<A,B> t : tuples.getTuples())
		{
			if(!set.contains(t.getId1())) {set.add(t.getId1());}
		}
		return fUtils.find(cA, set);
	}
	public List<A> toListX(JsonTuples2<A,B> tuples)
	{
		Set<A> set = new HashSet<A>();
		for(JsonTuple2<A,B> t : tuples.getTuples())
		{
			if(!set.contains(t.getEjb1())) {set.add(t.getEjb1());}
		}
		return new ArrayList<A>(set);
	}
	
	public List<B> toListY(JsonTuples2<A,B> tuples)
	{
		Set<B> set = new HashSet<B>();
		for(JsonTuple2<A,B> t : tuples.getTuples())
		{
			if(!set.contains(t.getEjb2())) {set.add(t.getEjb2());}
		}
		return new ArrayList<B>(set);
	}
	public List<B> toListB()
	{
		Set<B> set = new HashSet<B>();
		for(JsonTuple2<A,B> t : tuples.getTuples())
		{
			if(!set.contains(t.getEjb2())) {set.add(t.getEjb2());}
		}
		return new ArrayList<B>(set);
	}
	
	public Map<A,Map<B,JsonTuple2<A,B>>> toMap(JsonTuples2<A,B> tuples)
	{
		Map<A,Map<B,JsonTuple2<A,B>>> map = new HashMap<A,Map<B,JsonTuple2<A,B>>>();
		
		for(JsonTuple2<A,B> t : tuples.getTuples())
		{
			if(!map.containsKey(t.getEjb1())) {map.put(t.getEjb1(), new HashMap<B,JsonTuple2<A,B>>());}
			map.get(t.getEjb1()).put(t.getEjb2(),t);
		}
		
		return map;
	}
	
	public static <A extends EjbWithId, B extends EjbWithId> Map<Long,Map<Long,JsonTuple2<A,B>>> toIdMap(JsonTuples2<A,B> tuples)
	{
		Map<Long,Map<Long,JsonTuple2<A,B>>> map = new HashMap<Long,Map<Long,JsonTuple2<A,B>>>();
		
		for(JsonTuple2<A,B> t : tuples.getTuples())
		{
			if(!map.containsKey(t.getId1())) {map.put(t.getId1(), new HashMap<Long,JsonTuple2<A,B>>());}
			map.get(t.getId1()).put(t.getId2(),t);
		}
		
		return map;
	}
	
	public boolean map2Contains(A a, B b)
	{
		return map2.containsKey(a) && map2.get(a).containsKey(b);
	}
	
	public JsonTuples2<A,B> build(List<Tuple> tuples, JeeslCq.Agg...types)
	{
		JsonTuples2<A,B> json = new JsonTuples2<>();
		for(Tuple t : tuples)
		{
			if(debugOnInfo)
			{
				Object[] a = t.toArray();
				logger.info("Tuple Lenght: "+a.length);
				for(int i=0;i<a.length;i++)
				{
					if(Objects.isNull(a[i])) {logger.info("\t"+i+": null");}
					else {logger.info("\t"+i+": "+a[i].getClass().getSimpleName()+": "+a[i].toString());}
				}
			}
			json.getTuples().add(JsonTupleFactory.build2(t,types));
		}
		ejb2Load(json);
		return json;
	}
	
	public JsonTuples2<A,B> buildO(List<Object[]> objects , List<JeeslCq.Agg> aggregations)
	{
		JsonTuples2<A,B> json = new JsonTuples2<>();
		for(Object[] o : objects)
		{
			json.getTuples().add(JsonTupleFactory.buildO2(o,aggregations));	
		}
		this.ejb2Load(json);
		return json;
	}
	
	
	// This method is used if a third grouping is added to the query. Then it's counted on the unique id1-id2 combination
	public JsonTuples2<A,B> countOnIds(List<Tuple> tuples)
	{
		Map<MultiKey,JsonTuple2<A,B>> mapTuples = new HashMap<MultiKey,JsonTuple2<A,B>>();
		Map<MultiKey,Long> mapCount = new HashMap<MultiKey,Long>();
		
		for(Tuple t : tuples)
        {
			JsonTuple2<A,B> j = JsonTupleFactory.build2(t,JeeslCq.Agg.count);
			
			MultiKey key = new MultiKey(j.getId1(),j.getId2());
			if(!mapTuples.containsKey(key)) {mapTuples.put(key, j);}
			if(mapCount.containsKey(key))
			{
				mapCount.put(key, 1+mapCount.get(key));
			}
			else
			{
				mapCount.put(key, 1l);
			}
        }
		
		JsonTuples2<A,B> json = new JsonTuples2<A,B>();
		for(MultiKey key : mapTuples.keySet())
		{
			JsonTuple2<A,B> t = mapTuples.get(key);
			t.setCount(mapCount.get(key));
			t.setCount1(t.getCount());
			json.getTuples().add(t);
		}
		
		ejb2Load(json);
		
		return json;
	}
}