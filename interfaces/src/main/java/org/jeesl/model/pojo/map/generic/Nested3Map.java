package org.jeesl.model.pojo.map.generic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Nested3Map <L1 extends EjbWithId, L2 extends EjbWithId, L3 extends EjbWithId, VALUE extends EjbWithId>
{
    final static Logger logger = LoggerFactory.getLogger(Nested3Map.class);

    private Map<L1,Nested2Map<L2,L3,VALUE>> m; public Map<L1, Nested2Map<L2,L3,VALUE>> getM() {return m;}

	public Nested3Map()
    {
		m = new HashMap<L1,Nested2Map<L2,L3,VALUE>>();
    }
	
	public void clear()
	{
		for(Nested2Map<L2,L3,VALUE> n2 : m.values())
		{
			n2.clear();
		}
		m.clear();
		
	}
	
	public void put(L1 l1, L2 l2, L3 l3, VALUE value)
	{
		if(!m.containsKey(l1)){m.put(l1,new Nested2Map<L2,L3,VALUE>());}
		m.get(l1).put(l2,l3,value);
	}
	
	public void putIfEmpty(L1 l1, L2 l2, L3 l3, VALUE value)
	{
		if(!this.containsKey(l1,l2,l3))
		{
			this.put(l1,l2,l3,value);
		}
	}
	
	public boolean containsKey(L1 l1, L2 l2, L3 l3)
    {
    	return (m.containsKey(l1) && m.get(l1).containsKey(l2,l3));
    }
	
	public List<VALUE> values(L1 l1)
	{
		List<VALUE> list = new ArrayList<VALUE>();
		
		if(m.containsKey(l1))
		{
			Nested2Map<L2,L3,VALUE> n2 = m.get(l1);
			list.addAll(n2.values());
		}
		
		return list;
	}
	
	public VALUE get(L1 l1, L2 l2, L3 l3)
    {
    	return m.get(l1).get(l2,l3);
    }
}