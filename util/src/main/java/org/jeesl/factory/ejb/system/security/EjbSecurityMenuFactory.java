package org.jeesl.factory.ejb.system.security;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityContext;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityMenu;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityView;
import org.jeesl.util.comparator.ejb.PositionComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbSecurityMenuFactory <V extends JeeslSecurityView<?,?,?,?,?,?>,
									CTX extends JeeslSecurityContext<?,?>,
									M extends JeeslSecurityMenu<V,CTX,M>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbSecurityMenuFactory.class);
	
	private final Class<M> cM;
	private final Comparator<M> comparator;
    
    public EjbSecurityMenuFactory(final Class<M> cM)
    {
		this.cM = cM;
		comparator = new PositionComparator<M>();
    } 
    
    
    public M build()
    {
	    	M ejb = null;
	    	
	    	try
	    	{
			ejb = cM.newInstance();
		}
	    	catch (InstantiationException e) {e.printStackTrace();}
	    	catch (IllegalAccessException e) {e.printStackTrace();}
	    	
	    	return ejb;
    }
    
    public M build(V view)
    {
		M ejb = null;
    	
    	try
    	{
			ejb = cM.newInstance();
			ejb.setView(view);
		}
    	catch (InstantiationException e) {e.printStackTrace();}
    	catch (IllegalAccessException e) {e.printStackTrace();}
    	
    	return ejb;
    }
    
    public Map<V,M> toMapView(List<M> list)
    {
		Map<V,M> map = new HashMap<V,M>();
		for(M m : list) {map.put(m.getView(),m);}
		return map;
    }
    public List<V> toListView(List<M> list)
    {
    	List<V> result = new ArrayList<V>();
		for(M m : list) {result.add(m.getView());}
		return result;
    }
    
    public List<M> toListRoot(List<M> list)
    {
    		List<M> result = new ArrayList<M>();
    		for(M m : list) {if(m.getParent()==null) {result.add(m);}}
    		Collections.sort(result,comparator);
    		return result;
    }
    
    public Map<M,List<M>> toMapChild(List<M> list)
	{
		Map<M,List<M>> map = new HashMap<M,List<M>>();
		for(M m : list)
		{
			if(m.getParent()!=null)
			{
				if(!map.containsKey(m.getParent())) {map.put(m.getParent(), new ArrayList<M>());}
				map.get(m.getParent()).add(m);
			}
		}
		for(M m : map.keySet())
		{
			Collections.sort(map.get(m),comparator);
		}
		return map;
    }
    
    public Map<V,V> toMapParent(List<M> list)
	{
		Map<V,V> map = new HashMap<V,V>();
		for(M m : list)
		{
			if(m.getParent()!=null)
			{
				map.put(m.getView(),m.getParent().getView());
			}
		}
		return map;
    }
}