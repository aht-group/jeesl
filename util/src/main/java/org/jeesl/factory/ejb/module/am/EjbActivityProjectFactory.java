package org.jeesl.factory.ejb.module.am;

import org.jeesl.interfaces.model.module.am.JeesAmProject;
import org.jeesl.interfaces.model.module.am.JeeslAmActivity;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbActivityProjectFactory<REALM extends JeeslTenantRealm<?,?,REALM,?>,
								ACTIVITY extends JeeslAmActivity<?,?,REALM,ACTIVITY,PROJ>,
								PROJ extends JeesAmProject<?,?,REALM,ACTIVITY,PROJ>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbActivityProjectFactory.class);

	private final Class<PROJ> cType;

    public EjbActivityProjectFactory(final Class<PROJ> cType)
    {
        this.cType = cType;
    }

	public <RREF extends EjbWithId> PROJ build()
	{
		try
		{
			PROJ ejb = cType.newInstance();
		    return ejb;
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		return null;
    }
}