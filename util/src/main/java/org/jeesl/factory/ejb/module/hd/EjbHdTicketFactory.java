package org.jeesl.factory.ejb.module.hd;

import java.util.UUID;

import org.jeesl.factory.ejb.io.cms.EjbIoCmsMarkupFactory;
import org.jeesl.interfaces.model.io.cms.markup.JeeslIoMarkupType;
import org.jeesl.interfaces.model.io.cms.markup.JeeslIoMarkup;
import org.jeesl.interfaces.model.module.hd.ticket.JeeslHdTicket;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbHdTicketFactory<R extends JeeslTenantRealm<?,?,R,?>,
								TICKET extends JeeslHdTicket<R,?,M,?>,
								M extends JeeslIoMarkup<MT>,
								MT extends JeeslIoMarkupType<?,?,MT,?>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbHdTicketFactory.class);
	
	private final EjbIoCmsMarkupFactory<M,MT> efMarkup;
	
	private final Class<TICKET> cTicket;

    public EjbHdTicketFactory(final Class<TICKET> cTicket, final Class<M> cMarkup)
    {
        this.cTicket = cTicket;
        
        efMarkup = new EjbIoCmsMarkupFactory<>(cMarkup);
    }
	
	public <RREF extends EjbWithId> TICKET build(R realm, RREF rref, MT markupType)
	{
		try
		{
			TICKET ejb = cTicket.newInstance();
			ejb.setRealm(realm);
			ejb.setRref(rref.getId());
			ejb.setCode(UUID.randomUUID().toString());
			
			ejb.setMarkupUser(efMarkup.build(markupType));
			ejb.setMarkupSupport(efMarkup.build(markupType));
			ejb.setMarkupSolution(efMarkup.build(markupType));
			
		    return ejb;
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		return null;
    }
}