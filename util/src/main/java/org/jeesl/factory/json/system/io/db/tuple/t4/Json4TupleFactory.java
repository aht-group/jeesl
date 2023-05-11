package org.jeesl.factory.json.system.io.db.tuple.t4;

import javax.persistence.Tuple;

import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.model.json.io.db.tuple.instance.JsonTuple4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//@Deprecated //Should be moved to JsonTupleFactory
public class Json4TupleFactory<A extends EjbWithId, B extends EjbWithId, C extends EjbWithId, D extends EjbWithId>
{
	final static Logger logger = LoggerFactory.getLogger(Json4TupleFactory.class);
	
	public Json4TupleFactory()
	{
		
	}
	
	public JsonTuple4<A,B,C,D> buildSum(Tuple tuple)
	{
		JsonTuple4<A,B,C,D> json = build(tuple);
		json.setSum((Double)tuple.get(4));
    	return json;
	}
	
	public JsonTuple4<A,B,C,D> buildCount(Tuple tuple)
	{
		JsonTuple4<A,B,C,D> json = build(tuple);
		json.setCount((Long)tuple.get(4));
    	return json;
	}
	
	private JsonTuple4<A,B,C,D> build(Tuple tuple)
	{
		JsonTuple4<A,B,C,D> json = new JsonTuple4<A,B,C,D>();
		json.setId1((Long)tuple.get(0));
		json.setId2((Long)tuple.get(1));
		json.setId3((Long)tuple.get(2));
		json.setId4((Long)tuple.get(3));
		return json;
	}
}