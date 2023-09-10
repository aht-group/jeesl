package org.jeesl.factory.ejb.io.db.meta;

import java.util.ArrayList;
import java.util.List;

import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaColumn;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaConstraint;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaUnique;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbIoDbMetaUniqueFactory<COL extends JeeslDbMetaColumn<?,?,?>,
										CON extends JeeslDbMetaConstraint<?,?,COL,?,CUN>,
										CUN extends JeeslDbMetaUnique<COL,CON>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbIoDbMetaUniqueFactory.class);
	
	private final Class<CUN> cUnique;
    
	public EjbIoDbMetaUniqueFactory(final Class<CUN> cUnique)
	{       
        this.cUnique = cUnique;
	}
	
	public CUN build(CON constraint, COL column, int position)
	{
		CUN ejb = null;
		try
		{
			 ejb = cUnique.newInstance();
			 ejb.setConstraint(constraint);
			 ejb.setColumn(column);
			 ejb.setPosition(position);
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		
		return ejb;
	}
	
//	public Map<MT,List<CON>> toMapConstraints(List<CON> list)
//	{
//		Map<MT,List<CON>> map = new HashMap<>();
//		for(CON c : list)
//		{
//			if(!map.containsKey(c.getTable())) {map.put(c.getTable(), new ArrayList<>());}
//			map.get(c.getTable()).add(c);
//		}
//		return map;
//	}
//	
//	public Map<String,Map<String,CON>> toMapTableConstraint(List<CON> list)
//	{
//		Map<String,Map<String,CON>> map = new HashMap<>();
//		for(CON c : list)
//		{
//			if(!map.containsKey(c.getTable().getCode())) {map.put(c.getTable().getCode(), new HashMap<>());}
//			map.get(c.getTable().getCode()).put(c.getCode(), c);
//		}
//		return map;
//	}
	
	public List<COL> toColumns(List<CUN> list)
	{
		List<COL> result = new ArrayList<>();
		for(CUN u : list) {result.add(u.getColumn());}
		return result;
	}
}