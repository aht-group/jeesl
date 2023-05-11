package org.jeesl.util.filter;

import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.model.json.io.db.tuple.special.JsonIdTuple;
import org.jeesl.model.pojo.map.generic.Nested2Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Nested2MapFilter 
{
	final static Logger logger = LoggerFactory.getLogger(Nested2MapFilter.class);
	
	public static <A extends EjbWithId, B extends EjbWithId> void zero2Null(Nested2Map<A,B,JsonIdTuple> n2m)
	{
		for(A a : n2m.toL1())
		{
			for(B b : n2m.toL2())
			{
				if(n2m.containsKey(a,b))
				{
					JsonIdTuple t = n2m.get(a,b);
					{
						if(t.getSum1()!=null && t.getSum1()==0) {n2m.remove(a,b);}
					}
				}
			}
		}
	}
}