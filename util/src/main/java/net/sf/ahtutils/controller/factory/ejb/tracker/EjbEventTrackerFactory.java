package net.sf.ahtutils.controller.factory.ejb.tracker;

import java.util.Date;

import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.interfaces.model.system.security.util.JeeslSecurityCategory;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.security.access.JeeslSecurityRole;
import org.jeesl.interfaces.model.system.security.access.JeeslSecurityUsecase;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityAction;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityTemplate;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.model.interfaces.tracker.UtilsEventTracker;

public class EjbEventTrackerFactory<L extends JeeslLang,
				D extends JeeslDescription,
				C extends JeeslSecurityCategory<L,D>,
				R extends JeeslSecurityRole<L,D,C,V,U,A>,
				V extends JeeslSecurityView<L,D,C,R,U,A>,
				U extends JeeslSecurityUsecase<L,D,C,R,V,A>,
				A extends JeeslSecurityAction<L,D,R,V,U,AT>,
				AT extends JeeslSecurityTemplate<L,D,C>,
				USER extends JeeslUser<R>,
				T extends UtilsEventTracker<L,D,C,R,V,U,A,AT,USER,E>,
				E extends EjbWithId>
{
	final static Logger logger = LoggerFactory.getLogger(EjbEventTrackerFactory.class);
	
    final Class<T> clTracker;
	
    public static <L extends JeeslLang,
				D extends JeeslDescription,
				C extends JeeslSecurityCategory<L,D>,
				R extends JeeslSecurityRole<L,D,C,V,U,A>,
				V extends JeeslSecurityView<L,D,C,R,U,A>,
				U extends JeeslSecurityUsecase<L,D,C,R,V,A>,
				A extends JeeslSecurityAction<L,D,R,V,U,AT>,
				AT extends JeeslSecurityTemplate<L,D,C>,
				USER extends JeeslUser<R>,
				T extends UtilsEventTracker<L,D,C,R,V,U,A,AT,USER,E>,
				E extends EjbWithId>
    	EjbEventTrackerFactory<L,D,C,R,V,U,A,AT,USER,T,E> createFactory(final Class<T> clTracker)
    {
        return new EjbEventTrackerFactory<L,D,C,R,V,U,A,AT,USER,T,E>(clTracker);
    }
    
    public EjbEventTrackerFactory(final Class<T> clTracker)
    {
        this.clTracker = clTracker;
    } 
    
    public T create(E event)
    {
    	T ejb = null;
    	
    	try
    	{
			ejb = clTracker.newInstance();
			ejb.setEvent(event);
			ejb.setRecord(new Date());
		}
    	catch (InstantiationException e) {e.printStackTrace();}
    	catch (IllegalAccessException e) {e.printStackTrace();}
    	
    	return ejb;
    }
}