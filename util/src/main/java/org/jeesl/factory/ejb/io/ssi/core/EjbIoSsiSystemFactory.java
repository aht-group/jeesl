package org.jeesl.factory.ejb.io.ssi.core;

import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiSystem;

public class EjbIoSsiSystemFactory <SYSTEM extends JeeslIoSsiSystem<?,?>>
{
	private final Class<SYSTEM> cSystem;

	public EjbIoSsiSystemFactory(final Class<SYSTEM> cSystem)
	{
        this.cSystem = cSystem;
	}
	
	public SYSTEM build(org.jeesl.model.xml.io.ssi.core.System system)
    {
        return create(system.getCode(),system.getName());
    }

	public SYSTEM build()
	{
		return create(null,null);
	}
	
	public SYSTEM create(String code, String name)
	{
		SYSTEM ejb = null;
		try
		{
			ejb = cSystem.newInstance();
	        ejb.setCode(code);
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		
		return ejb;
	}    
}