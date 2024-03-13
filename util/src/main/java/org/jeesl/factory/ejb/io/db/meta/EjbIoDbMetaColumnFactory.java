package org.jeesl.factory.ejb.io.db.meta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaColumn;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbIoDbMetaColumnFactory<TAB extends JeeslDbMetaTable<?,?,?>,
								COL extends JeeslDbMetaColumn<?,TAB,?>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbIoDbMetaColumnFactory.class);
	
	private final Class<COL> cColumn;
    
	public EjbIoDbMetaColumnFactory(final Class<COL> cColumn)
	{       
        this.cColumn = cColumn;
	}
	
	public COL build(TAB table, String code)
	{
		COL ejb = null;
		try
		{
			 ejb = cColumn.newInstance();
			 ejb.setTable(table);
			 ejb.setCode(code);
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		
		return ejb;
	}
	
	public Map<TAB,List<COL>> toMapColumn(List<COL> list)
	{
		Map<TAB,List<COL>> map = new HashMap<>();
		for(COL c : list)
		{
			if(!map.containsKey(c.getTable())) {map.put(c.getTable(), new ArrayList<>());}
			map.get(c.getTable()).add(c);
		}
		return map;
	}
	
	public Map<String,Map<String,COL>> toMapTableColumn(List<COL> list)
	{
		Map<String,Map<String,COL>> map = new HashMap<>();
		for(COL c : list)
		{
			if(!map.containsKey(c.getTable().getCode())) {map.put(c.getTable().getCode(), new HashMap<>());}
			map.get(c.getTable().getCode()).put(c.getCode(), c);
		}
		return map;
	}
}