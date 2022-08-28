package org.jeesl.factory.ejb.module.mmg;

import java.util.UUID;

import org.jeesl.interfaces.model.module.mmg.JeeslMmgClassification;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbMmgClassificationFactory<L extends JeeslLang,
								R extends JeeslTenantRealm<L,?,R,?>, 
								EC extends JeeslMmgClassification<L,R,EC,?>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbMmgClassificationFactory.class);
	
	private final Class<EC> cClassification;

    public EjbMmgClassificationFactory(final Class<EC> cClassification)
    {
        this.cClassification = cClassification;
    }
	
	public <RREF extends EjbWithId> EC build(R realm, RREF rref)
	{
		try
		{
			EC ejb = cClassification.newInstance();
			ejb.setRealm(realm);
			ejb.setRref(rref.getId());
			ejb.setCode(UUID.randomUUID().toString());
		
		    return ejb;
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		return null;
    }
}