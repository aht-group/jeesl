package org.jeesl.factory.ejb.io.fr;

import org.jeesl.interfaces.model.io.fr.JeeslFileContainer;
import org.jeesl.interfaces.model.io.fr.JeeslFileStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbIoFrContainerFactory<STORAGE extends JeeslFileStorage<?,?,?,?,?>,
									CONTAINER extends JeeslFileContainer<STORAGE,?>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbIoFrContainerFactory.class);
	
	private final Class<CONTAINER> cContainer;
    
	public EjbIoFrContainerFactory(final Class<CONTAINER> cContainer)
	{       
        this.cContainer = cContainer;
	}
	
	public CONTAINER build(long id)
	{
		CONTAINER ejb = null;
		try
		{
			 ejb = cContainer.newInstance();
			 ejb.setId(id);
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		
		return ejb;
	} 
	
	public CONTAINER build(STORAGE storage)
	{
		CONTAINER ejb = null;
		try
		{
			 ejb = cContainer.newInstance();
			 ejb.setStorage(storage);
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		
		return ejb;
	}
}