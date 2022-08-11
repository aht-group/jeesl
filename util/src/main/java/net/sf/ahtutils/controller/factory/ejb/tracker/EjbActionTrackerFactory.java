package net.sf.ahtutils.controller.factory.ejb.tracker;

import java.util.Date;

import org.jeesl.interfaces.model.io.label.revision.tracker.JeeslActionTracker;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbActionTrackerFactory<USER extends JeeslUser<?>, T extends JeeslActionTracker<USER>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbActionTrackerFactory.class);
	
    final Class<T> clTracker;
	
    public static <USER extends JeeslUser<?>, T extends JeeslActionTracker<USER>>
    	EjbActionTrackerFactory<USER,T> createFactory(final Class<T> clTracker)
    {
        return new EjbActionTrackerFactory<USER,T>(clTracker);
    }
    
    public EjbActionTrackerFactory(final Class<T> clTracker)
    {
        this.clTracker = clTracker;
    } 
    
    public T create(USER user)
    {
    	T ejb = null;
    	
    	try
    	{
			ejb = clTracker.newInstance();
			ejb.setUser(user);
			ejb.setRecord(new Date());
		}
    	catch (InstantiationException e) {e.printStackTrace();}
    	catch (IllegalAccessException e) {e.printStackTrace();}
    	
    	return ejb;
    }
}