package org.jeesl.factory.ejb.io.db.meta;

import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaSnapshot;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbIoDbMetaSnapshotFactory<SYSTEM extends JeeslIoSsiSystem<?,?>,
								MS extends JeeslDbMetaSnapshot<SYSTEM,?,?,?>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbIoDbMetaSnapshotFactory.class);
	
	private final Class<MS> cSnapshot;
    
	public EjbIoDbMetaSnapshotFactory(final Class<MS> cSnapshot)
	{       
        this.cSnapshot = cSnapshot;
	}
	
	public MS build(SYSTEM system)
	{
		MS ejb = null;
		try
		{
			 ejb = cSnapshot.newInstance();
			 ejb.setSystem(system);
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		
		return ejb;
	}
}