package org.jeesl.api.facade.module;

import java.util.List;

import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.module.am.JeesAmProject;
import org.jeesl.interfaces.model.module.am.JeeslAmActivity;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;

public interface JeeslAmFacade <L extends JeeslLang,D extends JeeslDescription,LOC extends JeeslLocale<L,D,LOC,?>,
								REALM extends JeeslTenantRealm<L,D,REALM,?>,
								ACTIVITY extends JeeslAmActivity<L,D,REALM,ACTIVITY,PROJ>,
								PROJ extends JeesAmProject<L,D,REALM,ACTIVITY,PROJ>>
			extends JeeslFacade
{

	void deleteActivityTree(ACTIVITY activity);

	List<ACTIVITY> getChildActivity(ACTIVITY activity);

}