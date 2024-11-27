package org.jeesl.factory.ejb.system.security;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeesl.factory.ejb.util.EjbPositionFactory;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityArea;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbSecurityAreaFactory <V extends JeeslSecurityView<?,?,?,?,?,?>,
										AR extends JeeslSecurityArea<?,?,V>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbSecurityAreaFactory.class);
	
	private final Class<AR> cAr;
    
    public EjbSecurityAreaFactory(final Class<AR> cAr)
    {
		this.cAr = cAr;
    } 
      
    public AR build(V view, List<AR> list)
    {
    	AR ejb = null;
    	
    	try
    	{
			ejb = cAr.getDeclaredConstructor().newInstance();
			ejb.setView(view);
			EjbPositionFactory.next(ejb,list);
		}
    	catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		catch (IllegalArgumentException e) {e.printStackTrace();}
		catch (InvocationTargetException e) {e.printStackTrace();}
		catch (NoSuchMethodException e) {e.printStackTrace();}
		catch (SecurityException e) {e.printStackTrace();}
    	
    	return ejb;
    }
    
    public Map<V,List<AR>> toMapView(List<AR> list)
    {
    	Map<V,List<AR>> map = new HashMap<>();
    	for(AR a : list)
    	{
    		if(!map.containsKey(a.getView())) {map.put(a.getView(),new ArrayList<>());}
    		map.get(a.getView()).add(a);
    	}
    	return map;
    }
}