package org.jeesl.factory.ejb.system.security.user;

import java.util.UUID;

import org.jeesl.interfaces.model.system.security.login.JeeslRememberMe;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.joda.time.DateTime;

public class EjbRememberMeFactory <USER extends JeeslUser<?>, REM extends JeeslRememberMe<USER>>
{		
	private final Class<REM> cRem;
	
	private EjbRememberMeFactory(final Class<REM> cRem)
	{
		this.cRem=cRem;
	}
	
	private static <USER extends JeeslUser<?>, REM extends JeeslRememberMe<USER>>
		EjbRememberMeFactory<USER,REM> factory(final Class<REM> cRem)
	{
		return new EjbRememberMeFactory<USER,REM>(cRem);
	}
	
	private REM create(USER user, int validDays)
	{
		DateTime dt = new DateTime();
		REM ejb = null;
		
		try
		{
			ejb = cRem.newInstance();
			ejb.setUser(user);
			ejb.setCode(UUID.randomUUID().toString());
			ejb.setValidUntil(dt.plusDays(validDays).toDate());
		}
	    	catch (InstantiationException e) {e.printStackTrace();}
	    	catch (IllegalAccessException e) {e.printStackTrace();}
		return ejb;
	}
}