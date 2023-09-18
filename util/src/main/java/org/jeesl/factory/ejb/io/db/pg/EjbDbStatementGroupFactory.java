package org.jeesl.factory.ejb.io.db.pg;

import java.util.List;
import java.util.UUID;

import org.jeesl.factory.ejb.util.EjbPositionFactory;
import org.jeesl.interfaces.model.io.db.pg.statement.JeeslDbStatementGroup;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbDbStatementGroupFactory <SYSTEM extends JeeslIoSsiSystem<?,?>,
									SG extends JeeslDbStatementGroup<SYSTEM>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbDbStatementGroupFactory.class);
	
	private final Class<SG> cGroup;
    
	public EjbDbStatementGroupFactory(final Class<SG> cGroup)
	{       
        this.cGroup = cGroup;
	}
	
	public SG build(SYSTEM system, List<SG> list)
	{
		SG ejb = null;
		try
		{
			 ejb = cGroup.newInstance();
			 ejb.setCode(UUID.randomUUID().toString());
			 ejb.setSystem(system);
			 EjbPositionFactory.next(ejb,list);
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		
		return ejb;
	}
}