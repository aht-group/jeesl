package org.jeesl.factory.ejb.io.db.meta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaColumn;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaConstraint;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaConstraintType;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaTable;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaUnique;
import org.jeesl.model.json.io.db.pg.meta.JsonPostgresMetaColumn;
import org.jeesl.model.json.io.db.pg.meta.JsonPostgresMetaConstraint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbIoDbMetaConstraintFactory<TAB extends JeeslDbMetaTable<?,?>,
										COL extends JeeslDbMetaColumn<?,TAB,?>,
										CON extends JeeslDbMetaConstraint<?,TAB,COL,?,UNQ>,
										CONT extends JeeslDbMetaConstraintType<?,?,CONT,?>,
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
	
	public boolean equals(CON ejb, JsonPostgresMetaConstraint json)
	{
		switch(JeeslDbMetaConstraintType.Code.valueOf(ejb.getType().getCode()))
		{
			case fk: return equalsFk(ejb,json);
//			case uk: break;
			default: logger.warn("NYI "+ejb.getType().getCode()); break;
		}
		return false;
	}
	
	private boolean equalsFk(CON ejb, JsonPostgresMetaConstraint json)
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
	public boolean equalsPk(CON ejb, JsonPostgresMetaConstraint json)
	{
//		JsonUtil.info(json);
		EqualsBuilder eb = new EqualsBuilder();
		eb.append(ejb.getCode(),json.getCode());
		eb.append(ejb.getColumnLocal().getCode(),json.getLocal().getCode());
		eb.append(ejb.getColumnLocal().getTable().getCode(),json.getLocal().getTable().getCode());
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
	
	public boolean equals(CON l, CON r)
	{
		if(!l.getType().equals(r.getType())) {return false;}
		else
		{
			switch(JeeslDbMetaConstraintType.Code.valueOf(l.getType().getCode()))
			{
				case pk: return this.equalsPk(l,r);
				case fk: return this.equalsFk(l,r);
				case uk: return this.equalsUk(l,r);
				default: logger.warn("NYI "+l.getType().getCode()); break;
			}
			return false;
		}
	}
	private boolean equalsPk(CON l, CON r)
	{
		EqualsBuilder eb = new EqualsBuilder();
		eb.append(l.getColumnLocal(),r.getColumnLocal());
		return eb.isEquals();
	}
	private boolean equalsFk(CON l, CON r)
	{
		EqualsBuilder eb = new EqualsBuilder();
		eb.append(l.getColumnLocal(),r.getColumnLocal());
		eb.append(l.getColumnRemote(),r.getColumnRemote());
		return eb.isEquals();
	}
	private boolean equalsUk(CON l, CON r)
	{
		EqualsBuilder eb = new EqualsBuilder();
		eb.append(l.getUniques().size(),r.getUniques().size());
		for(int i=0; i<Math.min(l.getUniques().size(),r.getUniques().size()); i++)
		{
			UNQ u1 = l.getUniques().get(i);
			UNQ u2 = r.getUniques().get(i);
			eb.append(u1.getPosition(),u2.getPosition());
			eb.append(u1.getColumn(),u2.getColumn());
		}
		return eb.isEquals();
	}
}