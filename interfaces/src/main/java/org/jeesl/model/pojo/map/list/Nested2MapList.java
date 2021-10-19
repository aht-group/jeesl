package org.jeesl.model.pojo.map.list;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Nested2MapList <L1 extends EjbWithId, L2 extends EjbWithId, VALUE extends EjbWithId>
{
    final static Logger logger = LoggerFactory.getLogger(Nested2MapList.class);

    private Map<L1,Map<L2,List<VALUE>>> m; public Map<L1,Map<L2,List<VALUE>>> getM() {return m;}
    
    public Nested2MapList()
    {
		m = new HashMap<>();
    }
    
    public void clear()
    {
    	for(Map<L2,List<VALUE>> m : m.values())
    	{
    		m.clear();
    	}
    	m.clear();
    }
    
    public void put(L1 l1, L2 l2, VALUE value)
	{
		if(!m.containsKey(l1)){m.put(l1,new HashMap<L2,List<VALUE>>());}
		if(!m.get(l1).containsKey(l2)) {m.get(l1).put(l2,new ArrayList<>());}
		m.get(l1).get(l2).add(value);
	}
    
    public boolean containsKey(L1 l1, L2 l2)
    {
    	return (m.containsKey(l1) && m.get(l1).containsKey(l2));
    }
    
    public void remove(L1 l1, L2 l2)
    {
    	if(m.containsKey(l1))
    	{
    		if(m.get(l1).containsKey(l2))
    		{
    			m.get(l1).remove(l2);
    		}
    	}
    }
    
    public List<VALUE> values()
    {
    	List<VALUE> list = new ArrayList<VALUE>();
    	for(Map<L2,List<VALUE>> map : m.values())
    	{
    		for(L2 l2 : map.keySet())
    		{
    			list.addAll(map.get(l2));
    		}
    		
    	}
    	return list;
    }
    
    public int size()
    {
    	int size = 0;
    	for(L1 key : m.keySet())
    	{
    		size = size + m.get(key).size();
    	}
    	return size;
    }
	
	public List<VALUE> get(L1 l1, L2 l2)
	{
		return m.get(l1).get(l2);
	}
	
	public List<L1> toL1()
	{
		return new ArrayList<L1>(m.keySet());
	}
	
	public List<L2> toL2()
	{
		Set<L2> set = new HashSet<L2>();
		for(L1 k : m.keySet())
		{
			set.addAll(m.get(k).keySet());
		}
		return new ArrayList<L2>(set);
	}
}