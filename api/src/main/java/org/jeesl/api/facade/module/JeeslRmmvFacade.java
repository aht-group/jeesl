package org.jeesl.api.facade.module;

import java.util.List;

import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.module.rmmv.JeeslRmmvClassification;
import org.jeesl.interfaces.model.module.rmmv.JeeslRmmvElement;
import org.jeesl.interfaces.model.module.rmmv.JeeslRmmvModule;
import org.jeesl.interfaces.model.module.rmmv.JeeslRmmvModuleConfig;
import org.jeesl.interfaces.model.module.rmmv.JeeslRmmvSubscription;
import org.jeesl.interfaces.model.module.rmmv.JeeslRmmvSubscriptionItem;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface JeeslRmmvFacade <L extends JeeslLang, D extends JeeslDescription,
								R extends JeeslTenantRealm<L,D,R,?>, 
								E extends JeeslRmmvElement<L,R,E,?>,
								EC extends JeeslRmmvClassification<L,R,EC,?>,
								MOD extends JeeslRmmvModule<?,?,MOD,?>,
								MC extends JeeslRmmvModuleConfig<E,MOD>,
								SUB extends JeeslRmmvSubscription<R,MOD,USER>,
								SI extends JeeslRmmvSubscriptionItem<SUB,MC>,
								USER extends EjbWithId>
			extends JeeslFacade
{	
	<RREF extends EjbWithId> List<MC> fRmmvConfigs(R realm, RREF rref, MOD module);
}