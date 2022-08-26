package org.jeesl.factory.ejb.module.rmmv;

import java.util.UUID;

import org.jeesl.interfaces.model.module.rmmv.JeeslRmmvElement;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbRmmvTreeElementFactory<L extends JeeslLang,
								R extends JeeslTenantRealm<L,?,R,?>, 
								EH extends JeeslRmmvElement<L,R,EH,?>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbRmmvTreeElementFactory.class);
	
//	private final EjbIoCmsMarkupFactory<M,MT> efMarkup;
	
	private final Class<EH> cElement;

    public EjbRmmvTreeElementFactory(final Class<EH> cElement)
    {
        this.cElement = cElement;
    }
	
	public <RREF extends EjbWithId> EH build(R realm, RREF rref)
	{
		try
		{
			EH ejb = cElement.newInstance();
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