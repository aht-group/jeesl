package org.jeesl.factory.json.io.db.tuple;

import javax.persistence.Tuple;

import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.model.json.db.tuple.JsonIdTuple;
import org.jeesl.model.json.db.tuple.t1.Json1Tuple;
import org.jeesl.model.json.db.tuple.t3.Json3Tuple;
import org.jeesl.model.json.db.tuple.t4.Json4Tuple;
import org.jeesl.model.json.db.tuple.two.Json2Tuple;
import org.jeesl.model.json.io.db.tuple.AbstractJsonTuple;
import org.jeesl.model.json.io.db.tuple.JsonTuple;
import org.jeesl.model.pojo.map.generic.Nested2Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonTupleFactory
{
	final static Logger logger = LoggerFactory.getLogger(JsonTupleFactory.class);
	
	public enum Type{count,sum}
	
	public static JsonTuple build() {return new JsonTuple();}
	
	public static <X extends EjbWithId> JsonTuple build(Json1Tuple<X> tuple)
	{
		JsonTuple json = build();
		
		json.setCount(tuple.getCount());
		json.setCount1(tuple.getCount1());
		
		json.setSum(tuple.getSum());
		json.setSum1(tuple.getSum1());
		json.setSum2(tuple.getSum2());
		json.setSum3(tuple.getSum3());
		json.setSum4(tuple.getSum4());
		json.setSum5(tuple.getSum5());
		
		json.setId1(tuple.getId());
		return json;
	}
	
	public static <A extends EjbWithId, B extends EjbWithId> JsonTuple build(Json2Tuple<A,B> tuple)
	{
		JsonTuple json = build();
		if(tuple.getCount1()!=null) {json.setCount(tuple.getCount1());}
		else {json.setCount(tuple.getCount());}
		
		json.setSum(tuple.getSum());
		json.setSum1(tuple.getSum1());
		json.setSum2(tuple.getSum2());
		json.setSum3(tuple.getSum3());
		json.setSum4(tuple.getSum4());
		json.setSum5(tuple.getSum5());
		
		json.setId1(tuple.getId1());
		json.setId2(tuple.getId2());
		return json;
	}
	
	public static <A extends EjbWithId, B extends EjbWithId, C extends EjbWithId> JsonTuple build(Json3Tuple<A,B,C> tuple)
	{
		JsonTuple json = build();
		json.setL1(tuple.getCount());
		
		json.setSum(tuple.getSum());
		json.setSum1(tuple.getSum1());
		json.setSum2(tuple.getSum2());
		json.setSum3(tuple.getSum3());
		json.setSum4(tuple.getSum4());
		json.setSum5(tuple.getSum5());		
		
		json.setId1(tuple.getId1());
		json.setId2(tuple.getId2());
		json.setId3(tuple.getId3());
		return json;
	}
	
//	public static <A extends EjbWithId> Json1Tuple<A> build11(Tuple tuple)
//	{
//		Json1Tuple<A> json = new Json1Tuple<A>();
//		json.setId((Long)tuple.get(0));		
//    	return json;
//	}
	
	public static <A extends EjbWithId> Json1Tuple<A> build1(Tuple tuple, JsonTupleFactory.Type...types)
	{
		Json1Tuple<A> json = new Json1Tuple<A>();
		json.setId((Long)tuple.get(0));

		JsonTupleFactory.build(tuple,0,json,types);
		
    	return json;
	}
	
	public static <A extends EjbWithId, B extends EjbWithId> Json2Tuple<A,B> build2(Tuple tuple, JsonTupleFactory.Type...types)
	{
		Json2Tuple<A,B> json = new Json2Tuple<A,B>();
		json.setId1((Long)tuple.get(0));
		json.setId2((Long)tuple.get(1));

		JsonTupleFactory.build(tuple,1,json,types);
		
    	return json;
	}
	
	public static <A extends EjbWithId, B extends EjbWithId, C extends EjbWithId> Json3Tuple<A,B,C> build3(Tuple tuple, JsonTupleFactory.Type...types)
	{
		Json3Tuple<A,B,C> json = new Json3Tuple<A,B,C>();
		json.setId1((Long)tuple.get(0));
		json.setId2((Long)tuple.get(1));
		json.setId3((Long)tuple.get(2));
		JsonTupleFactory.build(tuple,2,json,types);
    	return json;
	}
	
	public static <A extends EjbWithId, B extends EjbWithId, C extends EjbWithId, D extends EjbWithId> Json4Tuple<A,B,C,D> build4(Tuple tuple, JsonTupleFactory.Type...types)
	{
		Json4Tuple<A,B,C,D> json = new Json4Tuple<A,B,C,D>();
		json.setId1((Long)tuple.get(0));
		json.setId2((Long)tuple.get(1));
		json.setId3((Long)tuple.get(2));
		json.setId4((Long)tuple.get(3));
		JsonTupleFactory.build(tuple,3,json,types);
    	return json;
	}
	
	private static void build(Tuple tuple, int offset, AbstractJsonTuple json, JsonTupleFactory.Type...types)
	{
//		logger.info(StringUtil.stars());
		int index=1;
		for(JsonTupleFactory.Type type : types)
		{
//			logger.info("building Type:"+type);
			switch(type)
			{
				case sum: 	if(index==1)      {json.setSum1(JsonTupleFactory.toDouble(tuple,offset+index));json.setSum(json.getSum1());}
							else if(index==2) {json.setSum2(JsonTupleFactory.toDouble(tuple,offset+index));}
							else if(index==3) {json.setSum3(JsonTupleFactory.toDouble(tuple,offset+index));}
							else if(index==4) {json.setSum4(JsonTupleFactory.toDouble(tuple,offset+index));}
							else if(index==5) {json.setSum5(JsonTupleFactory.toDouble(tuple,offset+index));}
							else {logger.warn("NYI "+type+" for index="+index);}
							break;
				case count: if(index==1) {json.setCount1((Long)tuple.get(offset+index));json.setCount(json.getCount1());}
							else if(index==2) {json.setCount2((Long)tuple.get(offset+index));}
							else {logger.warn("NYI "+type+" for index="+index);}
							break;
			}
			index++;
		}
	}
	
	private static Double toDouble(Tuple tuple, int index)
	{
		Object o = tuple.get(index);
		
		if(o==null) {return null;}
		else if(o instanceof Double) {return ((Double)o);}
		else if(o instanceof Long) {return (((Long)o).doubleValue());}
		else
		{
			logger.warn("Type "+o.getClass().getSimpleName()+" NYI "+o.toString());
			return (Double)o;
		}
	}
	
	public static JsonIdTuple idSum1(Double value)
	{
		JsonIdTuple json = new JsonIdTuple();
		json.setSum1(value);
		return json;
	}
	
	public static <A extends EjbWithId, B extends EjbWithId> Double toSum1(Nested2Map<A,B,JsonIdTuple> n2m, A a, B b)
	{
		if(n2m.containsKey(a,b)) {return n2m.get(a,b).getSum1();}
		else {return null;}
	}
}