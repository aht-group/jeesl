package org.jeesl.factory.json.system.io.db.tuple.t2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Tuple;

import org.apache.commons.collections.keyvalue.MultiKey;
import org.jeesl.factory.ejb.util.EjbIdFactory;
import org.jeesl.factory.json.system.io.db.tuple.JsonTupleFactory;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.model.json.db.tuple.two.Json2Tuple;
import org.jeesl.model.json.db.tuple.two.Json2Tuples;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Json2TuplesFactory <A extends EjbWithId, B extends EjbWithId>
{
	final static Logger logger = LoggerFactory.getLogger(Json2TuplesFactory.class);
	
	private JeeslFacade fUtils; public JeeslFacade getfUtils() {return fUtils;} public void setfUtils(JeeslFacade fUtils) {this.fUtils = fUtils;}
	private final Json2TupleFactory<A,B> jtf;
	
	private final Class<A> cA;
	private final Class<B> cB;
	
	private final Set<Long> setA;
	private final Set<Long> setB;
	
	protected final Map<Long,A> mapA; public Map<Long,A> getMapA() {return mapA;}
	protected final Map<Long,B> mapB; public Map<Long,B> getMapB() {return mapB;}
	
	protected final Map<A,Map<B,Json2Tuple<A,B>>> map2; public Map<A,Map<B,Json2Tuple<A, B>>> getMap2() {return map2;}
	private Json2Tuples<A,B> tuples; public Json2Tuples<A,B> get2Tuples() {return tuples;} public void set2Tuples(Json2Tuples<A,B> tuples) {this.tuples = tuples;}


	public Json2TuplesFactory(Class<A> cA, Class<B> cY) {this(null,cA,cY);}
	public Json2TuplesFactory(JeeslFacade fUtils, Class<A> cA, Class<B> cY)
	{
		this.fUtils=fUtils;
		this.cA=cA;
		this.cB=cY;
		
		setA = new HashSet<Long>();
		setB = new HashSet<Long>();
		
		mapA = new HashMap<Long,A>();
		mapB = new HashMap<Long,B>();
		
		map2 = new HashMap<A,Map<B,Json2Tuple<A,B>>>();
		jtf = new Json2TupleFactory<A,B>();
	}
	
	protected void clear()
	{
		setA.clear();
		setB.clear();
		
		mapA.clear();
		mapB.clear();
		
		map2.clear();
	}
	
	public Json2Tuples<A,B> build2(){return new Json2Tuples<A,B>();}
	
	private void ejb2Load(Json2Tuples<A,B> json)
	{
		for(Json2Tuple<A,B> t : json.getTuples())
		{
			setA.add(t.getId1());
			setB.add(t.getId2());
		}
		
		if(fUtils==null)
		{	// A object is created and the corresponding id is set
			for(Json2Tuple<A,B> t : json.getTuples())
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
			
			for(Json2Tuple<A,B> t : json.getTuples())
			{
				t.setEjb1(map1.get(t.getId1()));
				t.setEjb2(map2.get(t.getId2()));
			}
		}
	}
	
	public List<A> idToListX(Json2Tuples<A,B> tuples)
	{
		Set<Long> set = new HashSet<Long>();
		for(Json2Tuple<A,B> t : tuples.getTuples())
		{
			if(!set.contains(t.getId1())) {set.add(t.getId1());}
		}
		return fUtils.find(cA, set);
	}
	public List<A> toListX(Json2Tuples<A,B> tuples)
	{
		Set<A> set = new HashSet<A>();
		for(Json2Tuple<A,B> t : tuples.getTuples())
		{
			if(!set.contains(t.getEjb1())) {set.add(t.getEjb1());}
		}
		return new ArrayList<A>(set);
	}
	
	public List<B> toListY(Json2Tuples<A,B> tuples)
	{
		Set<B> set = new HashSet<B>();
		for(Json2Tuple<A,B> t : tuples.getTuples())
		{
			if(!set.contains(t.getEjb2())) {set.add(t.getEjb2());}
		}
		return new ArrayList<B>(set);
	}
	public List<B> toListB()
	{
		Set<B> set = new HashSet<B>();
		for(Json2Tuple<A,B> t : tuples.getTuples())
		{
			if(!set.contains(t.getEjb2())) {set.add(t.getEjb2());}
		}
		return new ArrayList<B>(set);
	}
	
	public Map<A,Map<B,Json2Tuple<A,B>>> toMap(Json2Tuples<A,B> tuples)
	{
		Map<A,Map<B,Json2Tuple<A,B>>> map = new HashMap<A,Map<B,Json2Tuple<A,B>>>();
		
		for(Json2Tuple<A,B> t : tuples.getTuples())
		{
			if(!map.containsKey(t.getEjb1())) {map.put(t.getEjb1(), new HashMap<B,Json2Tuple<A,B>>());}
			map.get(t.getEjb1()).put(t.getEjb2(),t);
		}
		
		return map;
	}
	
	public static <A extends EjbWithId, B extends EjbWithId> Map<Long,Map<Long,Json2Tuple<A,B>>> toIdMap(Json2Tuples<A,B> tuples)
	{
		Map<Long,Map<Long,Json2Tuple<A,B>>> map = new HashMap<Long,Map<Long,Json2Tuple<A,B>>>();
		
		for(Json2Tuple<A,B> t : tuples.getTuples())
		{
			if(!map.containsKey(t.getId1())) {map.put(t.getId1(), new HashMap<Long,Json2Tuple<A,B>>());}
			map.get(t.getId1()).put(t.getId2(),t);
		}
		
		return map;
	}
	
	public boolean map2Contains(A a, B b)
	{
		return map2.containsKey(a) && map2.get(a).containsKey(b);
	}
	
	public Json2Tuples<A,B> build(List<Tuple> tuples, JsonTupleFactory.Type...types)
	{
		Json2Tuples<A,B> json = new Json2Tuples<A,B>();
		for(Tuple t : tuples){json.getTuples().add(JsonTupleFactory.build2(t,types));}
		ejb2Load(json);
		return json;
	}
	
	/*
	* Build Json2Tuples from jpa.Tupes
	* @deprecated
	* <p> Use {@link build(List<Tuple> tuples, JsonTupleFactory.Type...types)} instead.
	*/
    @Deprecated public Json2Tuples<A,B> buildSum(List<Tuple> tuples)
	{
		Json2Tuples<A,B> json = new Json2Tuples<A,B>();
		
		for(Tuple t : tuples)
        {
        	json.getTuples().add(jtf.buildSum(t));
        }
		
		ejb2Load(json);
		
		return json;
	}
	
	/*
	* Build Json2Tuples from jpa.Tupes
	* @deprecated
	* <p> Use {@link build(List<Tuple> tuples, JsonTupleFactory.Type...types)} instead.
	*/
	public Json2Tuples<A,B> buildCount(List<Tuple> tuples)
	{
		Json2Tuples<A,B> json = new Json2Tuples<A,B>();
		
		for(Tuple t : tuples)
        {
        	json.getTuples().add(jtf.buildCount(t));
        }
		
		ejb2Load(json);
		
		return json;
	}
	
	// This method is used if a third grouping is added to the query. Then it's counted on the unique id1-id2 combination
	public Json2Tuples<A,B> countOnIds(List<Tuple> tuples)
	{
		Map<MultiKey,Json2Tuple<A,B>> mapTuples = new HashMap<MultiKey,Json2Tuple<A,B>>();
		Map<MultiKey,Long> mapCount = new HashMap<MultiKey,Long>();
		
		for(Tuple t : tuples)
        {
			Json2Tuple<A,B> j = jtf.buildCount(t);
			
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
		
		Json2Tuples<A,B> json = new Json2Tuples<A,B>();
		for(MultiKey key : mapTuples.keySet())
		{
			Json2Tuple<A,B> t = mapTuples.get(key);
			t.setCount(mapCount.get(key));
			json.getTuples().add(t);
		}
		
		ejb2Load(json);
		
		return json;
	}
}