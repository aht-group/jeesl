package org.jeesl.factory.ejb.io.db.meta;

import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaTable;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbIoDbMetaTableFactory<SYSTEM extends JeeslIoSsiSystem<?,?>,
								MT extends JeeslDbMetaTable<SYSTEM,?>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbIoDbMetaTableFactory.class);
	
	private final Class<MT> cTable;
    
	public EjbIoDbMetaTableFactory(final Class<MT> cTable)
	{       
        this.cTable = cTable;
	}
	
	public MT build(SYSTEM system, String code)
	{
		MT ejb = null;
		try
		{
			 ejb = cTable.newInstance();
			 ejb.setSystem(system);
			 ejb.setCode(code);
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		
		return ejb;
	}
}