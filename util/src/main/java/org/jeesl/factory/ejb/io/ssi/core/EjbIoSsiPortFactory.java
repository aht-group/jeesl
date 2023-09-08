package org.jeesl.factory.ejb.io.ssi.core;

import java.util.List;

import org.jeesl.factory.ejb.util.EjbPositionFactory;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiHost;
import org.jeesl.interfaces.model.io.ssi.nat.JeeslIoSsiNat;

public class EjbIoSsiPortFactory <HOST extends JeeslIoSsiHost<?,?,?>,
									PORT extends JeeslIoSsiNat<?,?,HOST>>
{
	private final Class<PORT> cPort;

	public EjbIoSsiPortFactory(final Class<PORT> cPort)
	{
        this.cPort = cPort;
	}
		
	public PORT build(List<PORT> list)
	{
		PORT ejb = null;
		try
		{
			ejb = cPort.newInstance();
	        ejb.setPosition(0);
	        EjbPositionFactory.calcNext(ejb,list);
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		
		return ejb;
	}
	
	public void converter(JeeslFacade facade, HOST host)
	{
//		if(host.getSystem()!=null) {host.setSystem(facade.find(cSystem,host.getSystem()));}
	}
}