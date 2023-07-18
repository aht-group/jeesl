package org.jeesl.factory.ejb.io.db.meta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaConstraint;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbIoDbMetaConstraintFactory<MT extends JeeslDbMetaTable<?,?>,
								CON extends JeeslDbMetaConstraint<?,MT,?,?>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbIoDbMetaConstraintFactory.class);
	
	private final Class<CON> cConstraint;
    
	public EjbIoDbMetaConstraintFactory(final Class<CON> cConstraint)
	{       
        this.cConstraint = cConstraint;
	}
	
	public CON build(MT table, String code)
	{
		CON ejb = null;
		try
		{
			 ejb = cConstraint.newInstance();
			 ejb.setTable(table);
			 ejb.setCode(code);
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		
		return ejb;
	}
	
	public Map<MT,List<CON>> toMapConstraints(List<CON> list)
	{
		Map<MT,List<CON>> map = new HashMap<>();
		for(CON c : list)
		{
			if(!map.containsKey(c.getTable())) {map.put(c.getTable(), new ArrayList<>());}
			map.get(c.getTable()).add(c);
		}
		return map;
	}
	
	public Map<String,Map<String,CON>> toMapTableConstraint(List<CON> list)
	{
		Map<String,Map<String,CON>> map = new HashMap<>();
		for(CON c : list)
		{
			if(!map.containsKey(c.getTable().getCode())) {map.put(c.getTable().getCode(), new HashMap<>());}
			map.get(c.getTable().getCode()).put(c.getCode(), c);
		}
		return map;
	}
}