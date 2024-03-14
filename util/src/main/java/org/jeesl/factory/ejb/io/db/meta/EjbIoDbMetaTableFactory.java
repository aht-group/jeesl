package org.jeesl.factory.ejb.io.db.meta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.collections4.keyvalue.MultiKey;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaSchema;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaTable;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbIoDbMetaTableFactory<SYSTEM extends JeeslIoSsiSystem<?,?>,
								SCHEMA extends JeeslDbMetaSchema<SYSTEM,?>,
								TAB extends JeeslDbMetaTable<SYSTEM,?,SCHEMA>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbIoDbMetaTableFactory.class);
	
	private final Class<TAB> cTable;
    
	public EjbIoDbMetaTableFactory(final Class<TAB> cTable)
	{       
        this.cTable = cTable;
	}
	
	public TAB build(SYSTEM system, SCHEMA schema, String code)
	{
		TAB ejb = null;
		try
		{
			 ejb = cTable.newInstance();
			 ejb.setSystem(system);
			 ejb.setSchema(schema);
			 ejb.setCode(code);
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		
		return ejb;
	}
	
	public Map<MultiKey<String>,TAB> toMultiKeyMap(List<TAB> list)
	{
		Map<MultiKey<String>,TAB> map = new HashMap<>();
		for(TAB t : list)
		{
			String schema = "null"; if(Objects.nonNull(t.getSchema())) {schema=t.getSchema().getCode();}
			MultiKey<String> key = new MultiKey<String>(schema,t.getCode());
			map.put(key,t);
		}
		return map;
				
	}
}