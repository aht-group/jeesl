package org.jeesl.factory.ejb.io.ssi.data;

import java.util.List;

import org.jeesl.factory.ejb.util.EjbPositionFactory;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiContext;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiError;

public class EjbIoSsiErrorFactory <CTX extends JeeslIoSsiContext<?,?>,
									ERROR extends JeeslIoSsiError<?,?,CTX,?>>
{
	private final Class<ERROR> cError;

	public EjbIoSsiErrorFactory(final Class<ERROR> cError)
	{
        this.cError = cError;
	}
	
	public ERROR build(CTX context, List<ERROR> list)
	{
		ERROR ejb = null;
		try
		{
			ejb = cError.newInstance();
			ejb.setContext(context);
			EjbPositionFactory.next(ejb,list);
	       
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		return ejb;
	}
}