package org.jeesl.factory.json.system.io.db.tuple.t2;

import javax.persistence.Tuple;

import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.model.json.db.tuple.two.Json2Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Deprecated
public class Json2TupleFactory<A extends EjbWithId, B extends EjbWithId>
{
	final static Logger logger = LoggerFactory.getLogger(Json2TupleFactory.class);
	
	public Json2TupleFactory()
	{
		 
	}
	
	/*
	* Build Json2Tuples from jpa.Tupes
	* @deprecated
	* <p> Use {@link JsonTupleFactory.build2} instead.
	*/
	public Json2Tuple<A,B> buildSum(Tuple tuple)
	{
		Json2Tuple<A,B> json = new Json2Tuple<A,B>();
		json.setId1((Long)tuple.get(0));
		json.setId2((Long)tuple.get(1));
		json.setSum((Double)tuple.get(2));
		json.setSum1((Double)tuple.get(2));
    	return json;
	}
	
	/*
	* Build Json2Tuples from jpa.Tupes
	* @deprecated
	* <p> Use {@link JsonTupleFactory.build2} instead.
	*/
	public Json2Tuple<A,B> buildCount(Tuple tuple)
	{
		Json2Tuple<A,B> json = new Json2Tuple<A,B>();
		json.setId1((Long)tuple.get(0));
		json.setId2((Long)tuple.get(1));
		json.setCount((Long)tuple.get(2));
    	return json;
	}
}