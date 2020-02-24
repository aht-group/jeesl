package org.jeesl.factory.json.system.io.db.tuple;

import org.jeesl.model.json.db.tuple.JsonTuple;
import org.jeesl.model.json.db.tuple.t1.Json1Tuple;
import org.jeesl.model.json.db.tuple.t3.Json3Tuple;
import org.jeesl.model.json.db.tuple.two.Json2Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.model.interfaces.with.EjbWithId;

public class JsonTupleFactory
{
	final static Logger logger = LoggerFactory.getLogger(JsonTupleFactory.class);
	
	public static JsonTuple build() {return new JsonTuple();}
	
	public static <X extends EjbWithId> JsonTuple build(Json1Tuple<X> tuple)
	{
		JsonTuple json = build();
		json.setCount(tuple.getCount());
		json.setSum(tuple.getSum());
		
		json.setId1(tuple.getId());
		return json;
	}
	
	public static <X extends EjbWithId, Y extends EjbWithId> JsonTuple build(Json2Tuple<X,Y> tuple)
	{
		JsonTuple json = build();
		json.setCount(tuple.getCount());
		json.setSum(tuple.getSum());
		
		json.setId1(tuple.getId1());
		json.setId2(tuple.getId2());
		return json;
	}
	
	public static <A extends EjbWithId, B extends EjbWithId, C extends EjbWithId> JsonTuple build(Json3Tuple<A,B,C> tuple)
	{
		JsonTuple json = build();
		json.setL1(tuple.getCount());
		json.setSum(tuple.getSum());
		
		json.setId1(tuple.getId1());
		json.setId2(tuple.getId2());
		json.setId3(tuple.getId3());
		return json;
	}
}