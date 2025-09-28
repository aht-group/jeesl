package org.jeesl.factory.json.io.db.tuple;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

import javax.persistence.Tuple;

import org.exlp.util.system.DateUtil;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.model.ejb.io.db.JeeslCq;
import org.jeesl.model.json.io.db.tuple.AbstractJsonTuple;
import org.jeesl.model.json.io.db.tuple.JsonTuple;
import org.jeesl.model.json.io.db.tuple.container.JsonTuples2;
import org.jeesl.model.json.io.db.tuple.instance.JsonTuple1;
import org.jeesl.model.json.io.db.tuple.instance.JsonTuple2;
import org.jeesl.model.json.io.db.tuple.instance.JsonTuple3;
import org.jeesl.model.json.io.db.tuple.instance.JsonTuple4;
import org.jeesl.model.json.io.db.tuple.special.JsonIdTuple;
import org.jeesl.model.pojo.map.generic.Nested2Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonTupleFactory
{
	final static Logger logger = LoggerFactory.getLogger(JsonTupleFactory.class);
	

	
	public static JsonTuple build() {return new JsonTuple();}
	
	public static <X extends EjbWithId> JsonTuple build(JsonTuple1<X> tuple)
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
		
		json.setId1(tuple.getId1());
		return json;
	}
	
	public static <A extends EjbWithId, B extends EjbWithId> JsonTuple build(JsonTuple2<A,B> tuple)
	{
		JsonTuple json = build();
		if(Objects.nonNull(tuple.getCount1())) {json.setCount(tuple.getCount1());}
		else {json.setCount(tuple.getCount());}
		
		if(Objects.isNull(json.getCount1()) && Objects.nonNull(tuple.getCount())) {json.setCount1(tuple.getCount());}
		
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
	
	public static <A extends EjbWithId, B extends EjbWithId, C extends EjbWithId> JsonTuple build(JsonTuple3<A,B,C> tuple)
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
	
	public static <A extends EjbWithId> JsonTuple1<A> build1(JsonTuple1<A> t)
	{
		JsonTuple1<A> json = new JsonTuple1<A>();
		json.setId1(t.getId1());
		json.setEjb1(t.getEjb1());		
    	return json;
	}
	public static <A extends EjbWithId> JsonTuple1<A> build1(A a, long value, JeeslCq.Agg...types)
	{
		JsonTuple1<A> json = new JsonTuple1<A>();
		json.setId1(a.getId());
		json.setEjb1(a);

		JsonTupleFactory.build(value,json,types);
		
    	return json;
	}
	public static <A extends EjbWithId> JsonTuple1<A> build1(Tuple tuple, JeeslCq.Agg...types)
	{
		JsonTuple1<A> json = new JsonTuple1<A>();
		json.setId1((Long)tuple.get(0));

		JsonTupleFactory.build(tuple,0,json,types);
		
    	return json;
	}
	
	public static <A extends EjbWithId> JsonTuple1<A> buildO1(Object[] o, List<JeeslCq.Agg> aggregations)
	{
		JsonTuple1<A> json = new JsonTuple1<A>();
		
		json.setRecord(DateUtil.toLocalDateTime((java.sql.Timestamp)o[0]));
		json.setId1(((BigInteger)o[1]).longValue());
		
		for(int i=1;i<=aggregations.size();i++)
		{
			int index=1+i;
			if(i==1 && Objects.nonNull(o[index])) {json.setV1((Double)o[index]);}
			if(i==2 && Objects.nonNull(o[index])) {json.setV2((Double)o[index]);}
			if(i==3 && Objects.nonNull(o[index])) {json.setV3((Double)o[index]);}
		}
		
		return json;
	}
	public static <A extends EjbWithId, B extends EjbWithId> JsonTuple2<A,B> buildO2(Object[] o, List<JeeslCq.Agg> aggregations)
	{
		JsonTuple2<A,B> json = new JsonTuple2<A,B>();
		
		json.setRecord(DateUtil.toLocalDateTime((java.sql.Timestamp)o[0]));
		json.setId1(((BigInteger)o[1]).longValue());
		json.setId2(((BigInteger)o[2]).longValue());
		
		for(int i=1;i<=aggregations.size();i++)
		{
			int index=2+i;
			if(i==1 && Objects.nonNull(o[index])) {json.setV1((Double)o[index]);}
			if(i==2 && Objects.nonNull(o[index])) {json.setV2((Double)o[index]);}
			if(i==3 && Objects.nonNull(o[index])) {json.setV3((Double)o[index]);}
		}
		
		return json;
	}
	
	public static <A extends EjbWithId, B extends EjbWithId> JsonTuple2<A,B> build2(Tuple tuple, JeeslCq.Agg...types)
	{
		JsonTuple2<A,B> json = new JsonTuple2<A,B>();
		json.setId1((Long)tuple.get(0));
		json.setId2((Long)tuple.get(1));

		JsonTupleFactory.build(tuple,1,json,types);
		
    	return json;
	}
	
	public static <A extends EjbWithId, B extends EjbWithId, C extends EjbWithId> JsonTuple3<A,B,C> build3(Tuple tuple, JeeslCq.Agg...types)
	{
		JsonTuple3<A,B,C> json = new JsonTuple3<A,B,C>();
		json.setId1((Long)tuple.get(0));
		json.setId2((Long)tuple.get(1));
		json.setId3((Long)tuple.get(2));
		JsonTupleFactory.build(tuple,2,json,types);
    	return json;
	}
	
	public static <A extends EjbWithId, B extends EjbWithId, C extends EjbWithId, D extends EjbWithId> JsonTuple4<A,B,C,D> build4(Tuple tuple, JeeslCq.Agg...types)
	{
		JsonTuple4<A,B,C,D> json = new JsonTuple4<A,B,C,D>();
		json.setId1((Long)tuple.get(0));
		json.setId2((Long)tuple.get(1));
		json.setId3((Long)tuple.get(2));
		json.setId4((Long)tuple.get(3));
		JsonTupleFactory.build(tuple,3,json,types);
    	return json;
	}
	
	private static void build(Tuple tuple, int offset, AbstractJsonTuple json, JeeslCq.Agg...types)
	{
		int index=1;
		for(JeeslCq.Agg type : types)
		{
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
	
	private static void build(long value, AbstractJsonTuple json, JeeslCq.Agg...types)
	{
		int index=1;
		for(JeeslCq.Agg type : types)
		{
			switch(type)
			{
				case count: if(index==1) {json.setCount1(value);}
							else if(index==2) {json.setCount2(value);}
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
	
	public static long toCount1(JsonTuples2<?,?> tuples)
	{
		long result = 0;
		for(JsonTuple2<?,?> t : tuples.getTuples())
		{
			if(Objects.nonNull(t.getCount1())) {result = result+t.getCount();}
		}
		return result;
	}
}