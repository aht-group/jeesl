package org.jeesl.factory.ejb.io.attribute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeesl.interfaces.model.module.attribute.JeeslAttributeCriteria;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbAttributeOptionFactory<CRITERIA extends JeeslAttributeCriteria<?,?,?,?,?,OPTION,?>,
										OPTION extends JeeslAttributeOption<?,?,CRITERIA>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbAttributeOptionFactory.class);
	
	private final Class<OPTION> cOption;
    
	public EjbAttributeOptionFactory(final Class<OPTION> cOption)
	{       
        this.cOption = cOption;
	}
    
	public OPTION build(CRITERIA criteria, List<OPTION> list)
	{
		OPTION ejb = null;
		try
		{
			ejb = cOption.newInstance();
			ejb.setCriteria(criteria);
			if(list==null) {ejb.setPosition(1);}
			else {ejb.setPosition(list.size()+1);}
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		
		return ejb;
	}
	
	public Map<CRITERIA,List<OPTION>> toMapCriteria(List<OPTION> list)
	{
		Map<CRITERIA,List<OPTION>> map = new HashMap<>();
		for(OPTION o : list)
		{
			if(!map.containsKey(o.getCriteria())) {map.put(o.getCriteria(),new ArrayList<>());}
			map.get(o.getCriteria()).add(o);
		}
		return map;
	}
}