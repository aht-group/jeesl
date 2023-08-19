package org.jeesl.factory.ejb.system.security;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeesl.interfaces.model.system.security.access.JeeslSecurityRole;
import org.jeesl.interfaces.model.system.security.util.JeeslSecurityCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbSecurityRoleFactory <C extends JeeslSecurityCategory<?,?>,
										 R extends JeeslSecurityRole<?,?,C,?,?,?,?>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbSecurityRoleFactory.class);
	
    private final Class<R> cRole;
    
    public EjbSecurityRoleFactory(final Class<R> cRole)
    {
        this.cRole = cRole;
    } 
    
    public R build(C category, String code)
    {
    	R ejb = null;
    	try
    	{
			ejb = cRole.newInstance();
			ejb.setPosition(1);
			ejb.setCategory(category);
			ejb.setCode(code);
			ejb.setVisible(true);
		}
    	catch (InstantiationException e) {e.printStackTrace();}
    	catch (IllegalAccessException e) {e.printStackTrace();}
    	return ejb;
    }
    
    public Map<C,List<R>> toMapCategory(List<R> roles)
    {
    	Map<C,List<R>> map = new HashMap<>();
    	for(R r : roles)
    	{
    		if(!map.containsKey(r.getCategory())) {map.put(r.getCategory(),new ArrayList<>());}
    		map.get(r.getCategory()).add(r);
    	}
    	return map;
    }
}