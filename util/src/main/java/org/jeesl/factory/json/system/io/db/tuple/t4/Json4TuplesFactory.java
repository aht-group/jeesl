package org.jeesl.factory.json.system.io.db.tuple.t4;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Tuple;

import org.jeesl.factory.ejb.util.EjbIdFactory;
import org.jeesl.factory.json.io.db.tuple.JsonTupleFactory;
import org.jeesl.factory.json.system.io.db.tuple.t3.Json3TuplesFactory;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.model.ejb.io.db.JeeslCq;
import org.jeesl.model.json.io.db.tuple.container.JsonTuples4;
import org.jeesl.model.json.io.db.tuple.instance.JsonTuple4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Json4TuplesFactory <A extends EjbWithId, B extends EjbWithId, C extends EjbWithId, D extends EjbWithId>
			extends Json3TuplesFactory<A,B,C>
{
	final static Logger logger = LoggerFactory.getLogger(Json4TuplesFactory.class);
	
	private JeeslFacade facade;
	
	private final Json4TupleFactory<A,B,C,D> jtf;
	
	private final Class<D> cD;
	
	private final Set<Long> setId4;
	private final Map<Long,D> mapD; public Map<Long,D> getMapD() {return mapD;}

	private JsonTuples4<A,B,C,D> tuples; public JsonTuples4<A,B,C,D> getTuples4() {return tuples;} public void setTuples4(JsonTuples4<A,B,C,D> tuples) {this.tuples = tuples;}

	public static <A extends EjbWithId, B extends EjbWithId, C extends EjbWithId, D extends EjbWithId> Json4TuplesFactory<A,B,C,D> instance(Class<A> cA, Class<B> cB, Class<C> cC, Class<D> cD) {return new Json4TuplesFactory<>(cA,cB,cC,cD);}
	public Json4TuplesFactory(Class<A> cA, Class<B> cB, Class<C> cC, Class<D> cD)
	{
		super(cA,cB,cC);

		this.cD=cD;
		setId4 = new HashSet<Long>();
		mapD = new HashMap<Long,D>();
		
		jtf = new Json4TupleFactory<A,B,C,D>();
	}
	
	public Json4TuplesFactory<A,B,C,D> tupleLoad(JeeslFacade facade, boolean load)
	{
		if(load) {this.facade=facade;}
		return this;
	}
	
	protected void clear()
	{
		super.clear();
		setId4.clear();
		mapD.clear();
	}
	
	public void init(JeeslFacade fUtils, JsonTuples4<A,B,C,D> json)
	{
		clear();
		this.tuples = json;
		
		for(JsonTuple4<A,B,C,D> t : json.getTuples())
		{
			setA.add(t.getId1());
			setB.add(t.getId2());
			setC.add(t.getId3());
			setId4.add(t.getId4());
		}
		
		logger.info("Futils:"+(fUtils!=null));
		
		mapA.putAll(EjbIdFactory.toIdMap(fUtils.find(cA, setA)));
		mapB.putAll(EjbIdFactory.toIdMap(fUtils.find(cB, setB)));
		mapC.putAll(EjbIdFactory.toIdMap(fUtils.find(cC, setC)));
		mapD.putAll(EjbIdFactory.toIdMap(fUtils.find(cD, setId4)));
	}
	
	public JsonTuples4<A,B,C,D> build4(List<Tuple> tuples, JeeslCq.Agg...types)
	{
		JsonTuples4<A,B,C,D> json = new JsonTuples4<A,B,C,D>();
		for(Tuple t : tuples){json.getTuples().add(JsonTupleFactory.build4(t,types));}
		this.ejb4Load(json);
		return json;
	}
	
	private void ejb4Load(JsonTuples4<A,B,C,D> json)
	{ 
		clear();
		for(JsonTuple4<A,B,C,D> t : json.getTuples())
		{
			setA.add(t.getId1());
			setB.add(t.getId2());
			setC.add(t.getId3());
			setId4.add(t.getId3());
		}
		
		if(facade==null)
		{	// A object is created and the corresponding id is set
			for(JsonTuple4<A,B,C,D> t : json.getTuples())
			{
				try
				{
					t.setEjb1(cA.newInstance());t.getEjb1().setId(t.getId1());
					t.setEjb2(cB.newInstance());t.getEjb2().setId(t.getId2());
					t.setEjb3(cC.newInstance());t.getEjb3().setId(t.getId3());
					t.setEjb4(cD.newInstance());t.getEjb4().setId(t.getId4());
				}
				catch (InstantiationException | IllegalAccessException e) {e.printStackTrace();}
			}
		}
		else
		{
			// Here we really load the objects from the DB
			mapA.putAll(EjbIdFactory.toIdMap(facade.find(cA,setA)));
			mapB.putAll(EjbIdFactory.toIdMap(facade.find(cB,setB)));
			mapC.putAll(EjbIdFactory.toIdMap(facade.find(cC,setC)));
			mapD.putAll(EjbIdFactory.toIdMap(facade.find(cD,setId4)));
			
			for(JsonTuple4<A,B,C,D> t : json.getTuples())
			{
				t.setEjb1(mapA.get(t.getId1()));
				t.setEjb2(mapB.get(t.getId2()));
				t.setEjb3(mapC.get(t.getId3()));
				t.setEjb4(mapD.get(t.getId4()));
			}
		}
		this.tuples=json;
	}
	
	public JsonTuples4<A,B,C,D> build4Sum(List<Tuple> tuples)
	{
		JsonTuples4<A,B,C,D> json = new JsonTuples4<A,B,C,D>();
		for(Tuple t : tuples)
        {
        	json.getTuples().add(jtf.buildSum(t));
        }
		return json;
	}
	
//	private Json4Tuples<A,B,C,D> build4Count(List<Tuple> tuples)
//	{
//		Json4Tuples<A,B,C,D> json = new Json4Tuples<A,B,C,D>();
//		for(Tuple t : tuples)
//        {
//        	json.getTuples().add(jtf.buildCount(t));
//        }
//		return json;
//	}
}