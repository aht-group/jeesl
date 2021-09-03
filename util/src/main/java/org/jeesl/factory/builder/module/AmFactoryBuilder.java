package org.jeesl.factory.builder.module;

import org.jeesl.factory.builder.AbstractFactoryBuilder;
import org.jeesl.factory.ejb.module.am.EjbActivityFactory;
import org.jeesl.factory.ejb.module.am.EjbActivityProjectFactory;
import org.jeesl.interfaces.model.module.am.JeesAmProject;
import org.jeesl.interfaces.model.module.am.JeeslAmActivity;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AmFactoryBuilder<L extends JeeslLang,D extends JeeslDescription,LOC extends JeeslLocale<L,D,LOC,?>,
								REALM extends JeeslTenantRealm<L,D,REALM,?>,
								ACTIVITY extends JeeslAmActivity<L,D,REALM,ACTIVITY,PROJ>,
								PROJ extends JeesAmProject<L,D,REALM,ACTIVITY,PROJ>> extends AbstractFactoryBuilder<L,D>
{

	public AmFactoryBuilder(Class<L> cL, Class<D> cD, Class<LOC> cLoc, Class<REALM> cRealm,  Class<ACTIVITY> cActivity,Class<PROJ> cProject)
	{
		super(cL, cD);
		this.cRealm = cRealm;
		this.cLoc = cLoc;
		this.cActivity = cActivity;
		this.cProject = cProject;
	}
	final static Logger logger = LoggerFactory.getLogger(AomFactoryBuilder.class);

	private final Class<REALM> cRealm; public Class<REALM> getClassRealm() {return cRealm;}
	private final Class<LOC> cLoc; public Class<LOC> getClassLocale() {return cLoc;}
	private final Class<ACTIVITY> cActivity; public Class<ACTIVITY> getClassActivity() {return cActivity;}
	private final Class<PROJ> cProject; public Class<PROJ> getClassProject() {return cProject;}


	public EjbActivityProjectFactory<REALM, ACTIVITY, PROJ> ejbProject()
	{
		return new EjbActivityProjectFactory<>(cProject);
	}

	public EjbActivityFactory<REALM, ACTIVITY, PROJ> ejbActivity()
	{
		return new EjbActivityFactory<>(cActivity);
	}


}
