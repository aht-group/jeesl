package org.jeesl.factory.ejb.g.system.filter;

import java.time.LocalDateTime;
import java.util.List;

import org.jeesl.factory.ejb.util.EjbPositionFactory;
import org.jeesl.interfaces.model.system.filter.JeeslFilter;
import org.jeesl.interfaces.model.system.filter.JeeslFilterScope;
import org.jeesl.interfaces.model.system.filter.JeeslFilterType;
import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbFilterFactory <FILTER extends JeeslFilter<?,FILTER,TYPE,SCOPE,USER>,
								TYPE extends JeeslFilterType<?,?,TYPE,?>,
								SCOPE extends JeeslFilterScope<?,?,SCOPE,?>,
								USER extends JeeslSimpleUser>
{
	final static Logger logger = LoggerFactory.getLogger(EjbFilterFactory.class);
	
	private final Class<FILTER> cFilter;
	
	public EjbFilterFactory(final Class<FILTER> cFilter)
	{
        this.cFilter = cFilter;
	}
	
	public FILTER build(SCOPE scope, TYPE type, USER user, List<FILTER> list)
	{
		FILTER ejb = null;
		try
		{
			ejb = cFilter.newInstance();
			ejb.setUser(user);
			EjbPositionFactory.next(ejb,list);
			
			ejb.setScope(scope);
			ejb.setType(type);

			ejb.setLastModifiedBy(user);
			ejb.setLastModifiedAt(LocalDateTime.now());
			
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		
		return ejb;
	}
}