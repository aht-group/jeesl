package org.jeesl.factory.ejb.system.security;

import java.lang.reflect.InvocationTargetException;

import org.jeesl.factory.txt.system.security.TxtUserFactory;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbSecurityUserFactory <USER extends JeeslUser<?>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbSecurityUserFactory.class);
	
    final Class<USER> cUser;
    
    public EjbSecurityUserFactory(final Class<USER> cUser)
    {
        this.cUser = cUser;
    } 
    
	public USER build()
	{
		USER ejb = null;
    	try
    	{
			ejb = cUser.getDeclaredConstructor().newInstance();
			ejb.setPermitLogin(false);
			ejb.setSalt(TxtUserFactory.buildSalt());
		}
    	catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		catch (IllegalArgumentException e) {e.printStackTrace();}
		catch (InvocationTargetException e) {e.printStackTrace();}
		catch (NoSuchMethodException e) {e.printStackTrace();}
		catch (SecurityException e) {e.printStackTrace();}
    	
    	return ejb;
	}
}