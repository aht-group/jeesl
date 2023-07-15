package org.jeesl.factory.ejb.io.db.meta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaConstraint;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaTable;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbIoDbMetaConstraintFactory<SYSTEM extends JeeslIoSsiSystem<?,?>,
								MT extends JeeslDbMetaTable<SYSTEM,?>,
								MC extends JeeslDbMetaConstraint<SYSTEM,?,MT>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbIoDbMetaConstraintFactory.class);
	
	private final Class<MC> cConstraint;
    
	public EjbIoDbMetaConstraintFactory(final Class<MC> cConstraint)
	{       
        this.cConstraint = cConstraint;
	}
	
	public MC build(SYSTEM system, MT table, String code)
	{
		MC ejb = null;
		try
		{
			 ejb = cConstraint.newInstance();
			 ejb.setSystem(system);
			 ejb.setTable(table);
			 ejb.setCode(code);
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		
		return ejb;
	}
	
	public Map<MT,List<MC>> toMapConstraints(List<MC> list)
	{
		Map<MT,List<MC>> map = new HashMap<>();
		for(MC c : list)
		{
			if(!map.containsKey(c.getTable())) {map.put(c.getTable(), new ArrayList<>());}
			map.get(c.getTable()).add(c);
		}
		return map;
	}
	
	public Map<String,Map<String,MC>> toMapTableConstraint(List<MC> list)
	{
		Map<String,Map<String,MC>> map = new HashMap<>();
		for(MC c : list)
		{
			if(!map.containsKey(c.getTable().getCode())) {map.put(c.getTable().getCode(), new HashMap<>());}
			map.get(c.getTable().getCode()).put(c.getCode(), c);
		}
		return map;
	}
}