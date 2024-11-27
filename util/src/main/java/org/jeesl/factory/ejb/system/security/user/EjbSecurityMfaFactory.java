package org.jeesl.factory.ejb.system.security.user;

import java.lang.reflect.InvocationTargetException;

import org.jeesl.interfaces.model.system.security.login.JeeslSecurityMfa;
import org.jeesl.interfaces.model.system.security.login.JeeslSecurityMfaType;
import org.jeesl.interfaces.model.system.security.user.JeeslSecurityUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbSecurityMfaFactory <MFA extends JeeslSecurityMfa<UJ,MFT>,
									MFT extends JeeslSecurityMfaType<?,?,MFT,?>,
									UJ extends JeeslSecurityUser>
{
	final static Logger logger = LoggerFactory.getLogger(EjbSecurityMfaFactory.class);
    
    private final Class<MFA> cMfa;
    
    public EjbSecurityMfaFactory(final Class<MFA> cMfa)
    {
        this.cMfa = cMfa;
    } 
    
    public MFA build(UJ user, MFT type)
    {
    	MFA ejb = null;
    	
    	try
    	{
			ejb = cMfa.getDeclaredConstructor().newInstance();
			ejb.setUser(user);
			ejb.setType(type);
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