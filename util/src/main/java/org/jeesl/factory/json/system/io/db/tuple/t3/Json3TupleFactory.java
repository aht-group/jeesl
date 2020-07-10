package org.jeesl.factory.json.system.io.db.tuple.t3;

import javax.persistence.Tuple;

import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.model.json.db.tuple.t3.Json3Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//@Deprecated //Should be moved to JsonTupleFactory
public class Json3TupleFactory<A extends EjbWithId, B extends EjbWithId, C extends EjbWithId>
{
	final static Logger logger = LoggerFactory.getLogger(Json3TupleFactory.class);
	
	public Json3TupleFactory()
	{
		
	}
	
	public Json3Tuple<A,B,C> buildSum(Tuple tuple)
	{
		Json3Tuple<A,B,C> json = build(tuple);
		json.setSum((Double)tuple.get(3));
    	return json;
	}
	
	public Json3Tuple<A,B,C> buildCount(Tuple tuple)
	{
		Json3Tuple<A,B,C> json = build(tuple);
		json.setCount((Long)tuple.get(3));
    	return json;
	}
	
	public Json3Tuple<A,B,C> buildCountInteger4(Tuple tuple)
	{
		Json3Tuple<A,B,C> json = build(tuple);
		json.setGi1((Integer)tuple.get(3));
		json.setCount((Long)tuple.get(4));
    	return json;
	}
	
	private Json3Tuple<A,B,C> build(Tuple tuple)
	{
		Json3Tuple<A,B,C> json = new Json3Tuple<A,B,C>();
		json.setId1((Long)tuple.get(0));
		json.setId2((Long)tuple.get(1));
		json.setId3((Long)tuple.get(2));
		return json;
	}
}