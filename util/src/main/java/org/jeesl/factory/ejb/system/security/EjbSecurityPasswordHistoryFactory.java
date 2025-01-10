package org.jeesl.factory.ejb.system.security;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.interfaces.model.system.security.user.pwd.JeeslPasswordHistory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbSecurityPasswordHistoryFactory <USER extends JeeslUser<?>,
												HISTORY extends JeeslPasswordHistory<USER>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbSecurityPasswordHistoryFactory.class);
	
    final Class<HISTORY> cHistory;
    
    public EjbSecurityPasswordHistoryFactory(final Class<HISTORY> cHistory)
    {
        this.cHistory = cHistory;
    } 
    
	public HISTORY build(USER user, String password)
	{
		HISTORY ejb = null;
    	try
    	{
			ejb = cHistory.getDeclaredConstructor().newInstance();
			ejb.setUser(user);
			ejb.setRecord(new Date());
			ejb.setPwd(password);
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