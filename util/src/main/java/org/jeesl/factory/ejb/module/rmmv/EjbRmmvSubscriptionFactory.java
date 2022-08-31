package org.jeesl.factory.ejb.module.rmmv;

import java.util.UUID;

import org.jeesl.interfaces.model.module.rmmv.JeeslRmmvModule;
import org.jeesl.interfaces.model.module.rmmv.JeeslRmmvModuleConfig;
import org.jeesl.interfaces.model.module.rmmv.JeeslRmmvSubscription;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbRmmvSubscriptionFactory<R extends JeeslTenantRealm<?,?,R,?>,
										MOD extends JeeslRmmvModule<?,?,MOD,?>,
										MC extends JeeslRmmvModuleConfig<?,MOD>,
										SUB extends JeeslRmmvSubscription<R,MOD,USER>,
										USER extends EjbWithId>
{
	final static Logger logger = LoggerFactory.getLogger(EjbRmmvSubscriptionFactory.class);
	
	private final Class<SUB> cSubscription;

    public EjbRmmvSubscriptionFactory(final Class<SUB> cSubscription)
    {
        this.cSubscription = cSubscription;
    }
	
	public <RREF extends EjbWithId> SUB build(R realm, RREF rref, MOD module, USER user)
	{
		try
		{
			SUB ejb = cSubscription.newInstance();
			ejb.setRealm(realm);
			ejb.setRref(rref.getId());
			ejb.setCode(UUID.randomUUID().toString());
			ejb.setModule(module);
			ejb.setUser(user);
		
		    return ejb;
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		return null;
    }
}