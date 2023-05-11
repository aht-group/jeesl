package org.jeesl.factory.json.system.io.db.tuple.t1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Tuple;

import org.jeesl.factory.ejb.util.EjbIdFactory;
import org.jeesl.factory.json.io.db.tuple.JsonTupleFactory;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.model.json.io.db.tuple.JsonTuple;
import org.jeesl.model.json.io.db.tuple.container.JsonTuples1;
import org.jeesl.model.json.io.db.tuple.instance.JsonTuple1;

public class Json1TuplesFactory <A extends EjbWithId>
{
	private final Class<A> cA; public Class<A> getClassA() {return cA;}

	private JeeslFacade fUtils; public void setfUtils(JeeslFacade fUtils) {this.fUtils = fUtils;}
	public Json1TuplesFactory<A> facade(JeeslFacade facade){this.fUtils=facade; return this;}

	private final Set<Long> setA;
	private final Json1TupleFactory<A> jtf;
	
	protected final Map<Long,A> mapA; public Map<Long,A> getMapA() {return mapA;}
	
	private JsonTuples1<A> tuples; public JsonTuples1<A> get1Tuples() {return tuples;} public void set1Tuples(JsonTuples1<A> tuples) {this.tuples = tuples;}

	public static <A extends EjbWithId> Json1TuplesFactory<A> instance(Class<A> cA) {return new Json1TuplesFactory<>(cA);}
	public Json1TuplesFactory(Class<A> cA) {this(null,cA);}
	
	@Deprecated //This should not be used, the Facade should be activated manually (via the query.isTupleLoad() or later in the TupleHandler
	public Json1TuplesFactory(JeeslFacade fUtils, Class<A> cA)
	{
		this.cA=cA;
		this.fUtils=fUtils;
		setA = new HashSet<Long>();
		mapA = new HashMap<Long,A>();
		
		jtf = new Json1TupleFactory<A>();
	}
	
	public Json1TuplesFactory<A> tupleLoad(JeeslFacade facade, boolean load)
	{
		if(load) {this.fUtils=facade;}
		return this;
	}
	
	public void init(JeeslFacade fUtils, JsonTuples1<A> json)
	{
		clear();
		this.tuples = json;
		
		for(JsonTuple1<A> t : json.getTuples())
		{
			setA.add(t.getId1());
		}
		
		mapA.putAll(EjbIdFactory.toIdMap(fUtils.find(cA, setA)));
	}
	
	protected void clear()
	{
		setA.clear();
		mapA.clear();
	}
	
	public List<A> toListA()
	{
		return new ArrayList<A>(mapA.values());
	}
	
	// Deprecated?
	public List<JsonTuple1<A>> add(List<JsonTuple1<A>> list)
	{
		for(JsonTuple1<A> t : list)
		{
			if(!setA.contains(t.getId1())) {setA.add(t.getId1());}
		}
		return list;
	}
	
	public List<A> toTuple1List(JeeslFacade fUtils)
	{
		return fUtils.find(cA,setA);
	}
	
	public List<A> toListA(JsonTuples1<A> tuples)
	{
		Set<A> set = new HashSet<A>();
		for(JsonTuple1<A> t : tuples.getTuples())
		{
			if(!set.contains(t.getEjb1())) {set.add(t.getEjb1());}
		}
		return new ArrayList<A>(set);
	}
	
	public JsonTuples1<A> build(List<Tuple> tuples)
	{
		JsonTuples1<A> json = new JsonTuples1<A>();
		
		for(Tuple t : tuples)
        {
			JsonTuple1<A> j = JsonTupleFactory.build1(t);
			setA.add(j.getId1());
        	json.getTuples().add(j);
        }
		ejb1Load(json);	
		return json;
	}
	
	public JsonTuples1<A> buildSum(List<Tuple> tuples)
	{
		JsonTuples1<A> json = new JsonTuples1<A>();
		
		for(Tuple t : tuples)
        {
			JsonTuple1<A> j = jtf.buildSum(t);
			setA.add(j.getId1());
        	json.getTuples().add(j);
        }
		ejb1Load(json);
		return json;
	}
	
	@Deprecated
	public JsonTuples1<A> buildCount1(List<Tuple> tuples)
	{
		JsonTuples1<A> json = new JsonTuples1<A>();
		
		for(Tuple t : tuples)
        {
			JsonTuple1<A> j = jtf.buildCount(t);
			setA.add(j.getId1());
        	json.getTuples().add(j);
        }
		ejb1Load(json);
		return json;
	}
	
	public JsonTuples1<A> buildCountNative(List<Object> list)
	{
		JsonTuples1<A> json = new JsonTuples1<A>();
		for(Object o : list)
		{
			JsonTuple1<A> j = jtf.buildCountNative(o);
			setA.add(j.getId1());
        	json.getTuples().add(j);
		}
		ejb1Load(json);
		return json;
	}
	
	public JsonTuples1<A> buildSumNative(List<Object> list)
	{
		JsonTuples1<A> json = new JsonTuples1<A>();
		for(Object o : list)
		{
			JsonTuple1<A> j = jtf.buildCountNative(o);
			setA.add(j.getId1());
        	json.getTuples().add(j);
		}
		ejb1Load(json);
		return json;
	}
	
	public JsonTuples1<A> buildV1(List<Tuple> tuples, JsonTuple.Field... fields)
	{
		JsonTuples1<A> json = new JsonTuples1<A>();
		
		for(Tuple t : tuples)
        {
			JsonTuple1<A> j = jtf.build(t,fields);
			setA.add(j.getId1());
        	json.getTuples().add(j);
        }
		ejb1Load(json);
		return json;
	}
	
	public JsonTuples1<A> buildV2(List<Tuple> tuples, JsonTupleFactory.Type...types)
	{
		JsonTuples1<A> json = new JsonTuples1<A>();
		for(Tuple t : tuples){json.getTuples().add(JsonTupleFactory.build1(t,types));}
		ejb1Load(json);
		return json;
	}
	
	private void ejb1Load(JsonTuples1<A> json)
	{
		for(JsonTuple1<A> t : json.getTuples())
		{
			setA.add(t.getId1());
		}
		
		if(fUtils==null)
		{	// A object is created and the corresponding id is set
			for(JsonTuple1<A> t : json.getTuples())
			{
				try
				{
					t.setEjb1(cA.newInstance()); t.getEjb1().setId(t.getId1());
				}
				catch (InstantiationException | IllegalAccessException e) {e.printStackTrace();}
			}
		}
		else
		{	// Here we really load the objects from the DB
			Map<Long,A> map = EjbIdFactory.toIdMap(fUtils.find(cA,setA));
			for(JsonTuple1<A> t : json.getTuples())
			{
				t.setEjb1(map.get(t.getId1()));
			}
		}
	}
	
	@Deprecated
	public Map<A,JsonTuple1<A>> toMap(JsonTuples1<A> tuples)
	{
		Map<A,JsonTuple1<A>> map = new HashMap<A,JsonTuple1<A>>();
		
		for(JsonTuple1<A> t : tuples.getTuples())
		{
			if(!map.containsKey(t.getEjb1())) {map.put(t.getEjb1(), t);}
		}
		
		return map;
	}
}