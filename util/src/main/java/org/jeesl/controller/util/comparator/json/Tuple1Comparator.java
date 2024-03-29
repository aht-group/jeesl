package org.jeesl.controller.util.comparator.json;

import java.util.Comparator;
import java.util.Map;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.model.json.io.db.tuple.instance.JsonTuple1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Tuple1Comparator<A extends EjbWithId> implements Comparator<A>
{
	final static Logger logger = LoggerFactory.getLogger(Tuple1Comparator.class);

	private Map<A,JsonTuple1<A>> map; public void setMap(Map<A, JsonTuple1<A>> map) {this.map = map;}

	public Tuple1Comparator()
	{
		
	}
	
	public int compare(A a, A b)
    {
		  CompareToBuilder ctb = new CompareToBuilder();
		  if(map!=null && map.containsKey(a) && map.containsKey(b))
		  {
			  JsonTuple1<A> t1 = map.get(a);
			  JsonTuple1<A> t2 = map.get(b);
			  ctb.append(t1.getCount(), t2.getCount());
		  }
		  return ctb.toComparison();
    }
}