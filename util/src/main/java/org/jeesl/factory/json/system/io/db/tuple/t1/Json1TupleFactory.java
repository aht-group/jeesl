package org.jeesl.factory.json.system.io.db.tuple.t1;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Tuple;

import org.jeesl.factory.json.io.db.tuple.JsonTupleFactory;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.model.json.db.tuple.t1.Json1Tuple;
import org.jeesl.model.json.io.db.tuple.JsonTuple;
import org.jeesl.util.query.sql.SqlNativeQueryHelper;

@Deprecated //Should be moved to JsonTupleFactory
public class Json1TupleFactory<A extends EjbWithId>
{
	public Json1TupleFactory()
	{
		
	}
	
	public static <T extends EjbWithId> List<Long> toIds(List<Json1Tuple<T>> list)
	{
		List<Long> result = new ArrayList<Long>();
		for(Json1Tuple<T> t : list)
		{
			result.add(t.getId());
		}
		return result;
	}
	
	
	
	public Json1Tuple<A> buildSum(Tuple tuple)
	{
		Json1Tuple<A> json = JsonTupleFactory.build1(tuple);	
		json.setSum((Double)tuple.get(1));
    	return json;
	}
	
	public Json1Tuple<A> buildCount(Tuple tuple)
	{
		Json1Tuple<A> json = JsonTupleFactory.build1(tuple);
		json.setCount((Long)tuple.get(1));
		json.setCount1(json.getCount());
    	return json;
	}
	
	public Json1Tuple<A> buildCountNative(Object object)
	{
		Object[] array = (Object[])object;
		SqlNativeQueryHelper.debugDataTypes(false,this.getClass().getSimpleName()+":buildCountNative", array);
        long id = ((BigInteger)array[0]).longValue();
        long count = ((BigInteger)array[1]).longValue();
        
        Json1Tuple<A> json = new Json1Tuple<A>();
        json.setId(id);
        json.setCount(count);
        return json;
	}
	
	public Json1Tuple<A> build(Tuple tuple, JsonTuple.Field... fields)
	{
		Json1Tuple<A> json = JsonTupleFactory.build1(tuple);
		
		int index=1;
		for(JsonTuple.Field field : fields)
		{
			if(index==1)
			{
				if(field.equals(JsonTuple.Field.sum)) {json.setSum1((Double)tuple.get(index));}
				else if (field.equals(JsonTuple.Field.count)){json.setCount((Long)tuple.get(index));json.setCount1(json.getCount());}
			}
			if(index==2)
			{
				if(field.equals(JsonTuple.Field.sum)) {json.setSum2((Double)tuple.get(index));}
				else if (field.equals(JsonTuple.Field.count)){json.setCount2((Long)tuple.get(index));}
			}
			index++;
		}
    	return json;
	}
}