package org.jeesl.factory.json.io.db.tuple;

import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.model.json.io.db.tuple.MongoTuple;
import org.jeesl.model.json.io.db.tuple.instance.JsonTuple2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MongoTupleFactory
{
	final static Logger logger = LoggerFactory.getLogger(MongoTupleFactory.class);
	
	public static <A extends EjbWithId, B extends EjbWithId> JsonTuple2<A,B> build(MongoTuple tuple)
	{
		JsonTuple2<A,B> json = new JsonTuple2<A,B>();
		json.setId1(tuple.getTuple().getId1());
		json.setId2(tuple.getTuple().getId2());
		
		json.setSum1(tuple.getSum1());
		json.setSum2(tuple.getSum2());
		json.setSum3(tuple.getSum3());
		json.setSum4(tuple.getSum4());
		json.setSum5(tuple.getSum5());		
		
		json.setCount1(tuple.getCount1());
		json.setCount2(tuple.getCount2());
		return json;
	}
}