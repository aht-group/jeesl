package org.jeesl.util.comparator.json;

import java.util.Comparator;
import java.util.Map;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.model.json.db.tuple.t1.Json1Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Tuple1Comparator<A extends EjbWithId> implements Comparator<A>
{
	final static Logger logger = LoggerFactory.getLogger(Tuple1Comparator.class);

	private Map<A,Json1Tuple<A>> map; public void setMap(Map<A, Json1Tuple<A>> map) {this.map = map;}

	public Tuple1Comparator()
	{
		
	}
	
	public int compare(A a, A b)
    {
		  CompareToBuilder ctb = new CompareToBuilder();
		  if(map!=null && map.containsKey(a) && map.containsKey(b))
		  {
			  Json1Tuple<A> t1 = map.get(a);
			  Json1Tuple<A> t2 = map.get(b);
			  ctb.append(t1.getCount(), t2.getCount());
		  }
		  return ctb.toComparison();
    }
}