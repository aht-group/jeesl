package org.jeesl.api.facade.module;

import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.module.mmg.JeeslMmgClassification;
import org.jeesl.interfaces.model.module.mmg.JeeslMmgGallery;
import org.jeesl.interfaces.model.module.mmg.JeeslMmgItem;
import org.jeesl.interfaces.model.module.mmg.JeeslMmgQuality;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;

public interface JeeslMmgFacade <L extends JeeslLang, D extends JeeslDescription,
									R extends JeeslTenantRealm<L,D,R,?>, 
									MG extends JeeslMmgGallery<L>,
									MI extends JeeslMmgItem<L,D,R>,
									MC extends JeeslMmgClassification<L,R,MC,?>,
									MQ extends JeeslMmgQuality<L,D,MQ,?>>
			extends JeeslFacade
{	
//	<RREF extends EjbWithId> List<MC> fRmmvConfigs(R realm, RREF rref, MOD module);
}