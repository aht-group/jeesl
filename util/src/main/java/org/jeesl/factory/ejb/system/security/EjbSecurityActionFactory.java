package org.jeesl.factory.ejb.system.security;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeesl.api.facade.system.JeeslSecurityFacade;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityAction;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityView;
import org.jeesl.util.query.ejb.system.EjbSecurityQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbSecurityActionFactory <V extends JeeslSecurityView<?,?,?,?,?,A>,
										 A extends JeeslSecurityAction<?,?,?,V,?,?>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbSecurityActionFactory.class);
    
    private final Class<A> cAction;
    
    public EjbSecurityActionFactory(final Class<A> cAction)
    {
        this.cAction = cAction;
    } 
    
    public A build(V view, String code, List<A> list)
    {
    	A ejb = null;
    	
    	try
    	{
			ejb = cAction.getDeclaredConstructor().newInstance();
			
			ejb.setView(view);
			ejb.setCode(code);
			if(list==null){ejb.setPosition(1);}
			else{ejb.setPosition(list.size()+1);}
		}
    	catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		catch (IllegalArgumentException e) {e.printStackTrace();}
		catch (InvocationTargetException e) {e.printStackTrace();}
		catch (NoSuchMethodException e) {e.printStackTrace();}
		catch (SecurityException e) {e.printStackTrace();}
    	
    	return ejb;
    }
    
    public Map<V,List<A>> toMapView(List<A> list)
    {
    	Map<V,List<A>> map = new HashMap<>();
    	for(A a : list)
    	{
    		if(!map.containsKey(a.getView())) {map.put(a.getView(),new ArrayList<>());}
    		map.get(a.getView()).add(a);
    	}
    	return map;
	}
}