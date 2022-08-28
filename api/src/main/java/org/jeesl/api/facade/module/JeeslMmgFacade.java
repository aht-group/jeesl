package org.jeesl.api.facade.module;

import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.module.mmg.JeeslMmgClassification;
import org.jeesl.interfaces.model.module.mmg.JeeslMmgGallery;
import org.jeesl.interfaces.model.module.mmg.JeeslMmgItem;
import org.jeesl.interfaces.model.module.mmg.JeeslMmgQuality;
import org.jeesl.interfaces.model.module.mmg.JeeslWithMmgGallery;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;

public interface JeeslMmgFacade <L extends JeeslLang, D extends JeeslDescription,
									R extends JeeslTenantRealm<L,D,R,?>, 
									MG extends JeeslMmgGallery<L>,
									MI extends JeeslMmgItem<L,MG,?,USER>,
									MC extends JeeslMmgClassification<L,R,MC,?>,
									MQ extends JeeslMmgQuality<L,D,MQ,?>,
									USER extends JeeslSimpleUser>
			extends JeeslFacade
{	
	<OWNER extends JeeslWithMmgGallery<MG>> MG fMmgGallery(Class<OWNER> cOwner, OWNER owner) throws JeeslNotFoundException;
}