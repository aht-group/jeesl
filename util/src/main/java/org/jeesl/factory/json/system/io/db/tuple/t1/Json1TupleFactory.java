package org.jeesl.factory.json.system.io.db.tuple.t1;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Tuple;

import org.jeesl.factory.json.io.db.tuple.JsonTupleFactory;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.model.ejb.io.db.JeeslCq;
import org.jeesl.model.json.io.db.tuple.JsonTuple;
import org.jeesl.model.json.io.db.tuple.instance.JsonTuple1;
import org.jeesl.util.query.sql.SqlNativeQueryHelper;

@Deprecated //Should be moved to JsonTupleFactory
public class Json1TupleFactory<A extends EjbWithId>
{
	public Json1TupleFactory()
	{
		
	}
	
	public static <T extends EjbWithId> List<Long> toIds(List<JsonTuple1<T>> list)
	{
		List<Long> result = new ArrayList<Long>();
		for(JsonTuple1<T> t : list)
		{
			result.add(t.getId1());
		}
		return result;
	}
	
	
	
	public JsonTuple1<A> buildSum(Tuple tuple)
	{
		JsonTuple1<A> json = JsonTupleFactory.build1(tuple);	
		json.setSum((Double)tuple.get(1));
    	return json;
	}
	
	public JsonTuple1<A> buildCount(Tuple tuple)
	{
		JsonTuple1<A> json = JsonTupleFactory.build1(tuple);
		json.setCount((Long)tuple.get(1));
		json.setCount1(json.getCount());
    	return json;
	}
	
	public JsonTuple1<A> buildCountNative(Object object)
	{
		Object[] array = (Object[])object;
		SqlNativeQueryHelper.debugDataTypes(false,this.getClass().getSimpleName()+":buildCountNative", array);
        long id = ((BigInteger)array[0]).longValue();
        long count = ((BigInteger)array[1]).longValue();
        
        JsonTuple1<A> json = new JsonTuple1<A>();
        json.setId1(id);
        json.setCount(count);
        return json;
	}
	
	public JsonTuple1<A> build(Tuple tuple, JeeslCq.Agg... fields)
	{
		JsonTuple1<A> json = JsonTupleFactory.build1(tuple);
		
		int index=1;
		for(JeeslCq.Agg field : fields)
		{
			if(index==1)
			{
				if(field.equals(JeeslCq.Agg.sum)) {json.setSum1((Double)tuple.get(index));}
				else if (field.equals(JeeslCq.Agg.count)){json.setCount((Long)tuple.get(index));json.setCount1(json.getCount());}
			}
			if(index==2)
			{
				if(field.equals(JeeslCq.Agg.sum)) {json.setSum2((Double)tuple.get(index));}
				else if (field.equals(JeeslCq.Agg.count)){json.setCount2((Long)tuple.get(index));}
			}
			index++;
		}
    	return json;
	}
}