package org.jeesl.factory.ejb.module.am;

import java.util.UUID;

import org.jeesl.interfaces.model.module.am.JeesAmProject;
import org.jeesl.interfaces.model.module.am.JeeslAmActivity;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbActivityFactory<REALM extends JeeslTenantRealm<?,?,REALM,?>,
								ACTIVITY extends JeeslAmActivity<?,?,REALM,ACTIVITY,PROJ>,
								PROJ extends JeesAmProject<?,?,REALM,ACTIVITY,PROJ>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbActivityFactory.class);

	private final Class<ACTIVITY> cType;

    public EjbActivityFactory(final Class<ACTIVITY> cType)
    {
        this.cType = cType;
    }

	public ACTIVITY build(PROJ project, ACTIVITY parent)
	{
		UUID uuid = UUID.randomUUID();
        String uuidAsString = uuid.toString();
		try
		{
			ACTIVITY ejb = cType.newInstance();
			ejb.setProject(project);
			ejb.setParent(parent);
			ejb.setCode(uuidAsString);
		    return ejb;
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		return null;
    }

	public void update(ACTIVITY src, ACTIVITY dst)
	{
		dst.setParent(src.getParent());
		dst.setPosition(src.getPosition());
		//dst.setName(src.getName());
	}
	public ACTIVITY build(ACTIVITY parent)
	{
		return build(parent.getProject(),parent);
    }
}