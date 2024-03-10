package org.jeesl.factory.ejb.io.db.meta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.exlp.util.io.JsonUtil;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaColumn;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaConstraint;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaTable;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaUnique;
import org.jeesl.model.json.io.db.pg.meta.JsonPostgresMetaColumn;
import org.jeesl.model.json.io.db.pg.meta.JsonPostgresMetaConstraint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbIoDbMetaConstraintFactory<TAB extends JeeslDbMetaTable<?,?>,
										COL extends JeeslDbMetaColumn<?,TAB,?>,
										CON extends JeeslDbMetaConstraint<?,TAB,COL,?,UNQ>,
										UNQ extends JeeslDbMetaUnique<COL,CON>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbIoDbMetaConstraintFactory.class);
	
	private final Class<CON> cConstraint;
    
	public EjbIoDbMetaConstraintFactory(final Class<CON> cConstraint)
	{       
        this.cConstraint = cConstraint;
	}
	
	public CON build(TAB table, String code)
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
	
	public Map<TAB,List<CON>> toMapConstraints(List<CON> list)
	{
		Map<TAB,List<CON>> map = new HashMap<>();
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
	
	public boolean equalsPk(CON ejb, JsonPostgresMetaConstraint json)
	{
//		JsonUtil.info(json);
		EqualsBuilder eb = new EqualsBuilder();
		eb.append(ejb.getCode(),json.getCode());
		eb.append(ejb.getColumnLocal().getCode(),json.getLocal().getCode());
		eb.append(ejb.getColumnLocal().getTable().getCode(),json.getLocal().getTable().getCode());
		return eb.isEquals();
	}
	public boolean equalsFk(CON ejb, JsonPostgresMetaConstraint json)
	{
//		JsonUtil.info(json);
		EqualsBuilder eb = new EqualsBuilder();
		eb.append(ejb.getCode(),json.getCode());
		eb.append(ejb.getColumnLocal().getCode(),json.getLocal().getCode());
		eb.append(ejb.getColumnLocal().getTable().getCode(),json.getLocal().getTable().getCode());
		eb.append(ejb.getColumnRemote().getCode(),json.getRemoteColumn());
		eb.append(ejb.getColumnRemote().getTable().getCode(),json.getRemoteTable());
		return eb.isEquals();
	}
	public boolean equalsUk(CON ejb, JsonPostgresMetaConstraint json)
	{
//		JsonUtil.info(json);
		EqualsBuilder eb = new EqualsBuilder();
		eb.append(ejb.getCode(),json.getCode());
		eb.append(ejb.getUniques().size(),json.getColumns().size());
		for(int i=0; i<Math.min(ejb.getUniques().size(),json.getColumns().size()); i++)
		{
			UNQ eU = ejb.getUniques().get(i);
			JsonPostgresMetaColumn jU = json.getColumns().get(i);
			eb.append(eU.getPosition(),jU.getPosition().intValue());
			eb.append(eU.getColumn().getCode(),jU.getCode());
		}
		
		return eb.isEquals();
	}
}