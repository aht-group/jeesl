package org.jeesl.factory.ejb.io.db.meta;

import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaSchema;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbIoDbMetaSchemaFactory<SYSTEM extends JeeslIoSsiSystem<?,?>,
								SCHEMA extends JeeslDbMetaSchema<SYSTEM,?>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbIoDbMetaSchemaFactory.class);
	
	private final Class<SCHEMA> cSchema;
    
	public EjbIoDbMetaSchemaFactory(final Class<SCHEMA> cSchema)
	{       
        this.cSchema = cSchema;
	}
	
	public SCHEMA build(SYSTEM system, String code)
	{
		SCHEMA ejb = null;
		try
		{
			 ejb = cSchema.newInstance();
			 ejb.setSystem(system);
			 ejb.setCode(code);
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		
		return ejb;
	}
}