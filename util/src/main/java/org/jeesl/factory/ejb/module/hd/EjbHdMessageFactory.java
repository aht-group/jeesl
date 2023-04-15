package org.jeesl.factory.ejb.module.hd;

import java.util.Date;

import org.jeesl.factory.builder.module.HdFactoryBuilder;
import org.jeesl.factory.ejb.io.cms.EjbIoCmsMarkupFactory;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.io.cms.markup.JeeslIoMarkupType;
import org.jeesl.interfaces.model.io.cms.markup.JeeslIoMarkup;
import org.jeesl.interfaces.model.module.hd.resolution.JeeslHdMessage;
import org.jeesl.interfaces.model.module.hd.resolution.JeeslHdScope;
import org.jeesl.interfaces.model.module.hd.ticket.JeeslHdTicket;
import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbHdMessageFactory<TICKET extends JeeslHdTicket<?,?,M,?>,
								MSG extends JeeslHdMessage<TICKET,M,SCOPE,USER>,
								M extends JeeslIoMarkup<MT>,
								MT extends JeeslIoMarkupType<?,?,MT,?>,
								SCOPE extends JeeslHdScope<?,?,SCOPE,?>,
								USER extends JeeslSimpleUser>
{
	final static Logger logger = LoggerFactory.getLogger(EjbHdMessageFactory.class);
	
	protected final HdFactoryBuilder<?,?,?,?,TICKET,?,?,?,?,?,?,MSG,M,MT,?,SCOPE,?,?,?,?,USER> fbHd;
	
	private final EjbIoCmsMarkupFactory<M,MT> efMarkup;

    public EjbHdMessageFactory(HdFactoryBuilder<?,?,?,?,TICKET,?,?,?,?,?,?,MSG,M,MT,?,SCOPE,?,?,?,?,USER> fbHd)
    {
        this.fbHd = fbHd;
        efMarkup = new EjbIoCmsMarkupFactory<>(fbHd.getClassMarkup());
    }
	
	public MSG build(TICKET ticket, MT markupType, SCOPE scope, USER user)
	{
		try
		{
			MSG ejb = fbHd.getClassMessage().newInstance();
			ejb.setTicket(ticket);
			ejb.setRecord(new Date());
			ejb.setScope(scope);
			ejb.setUser(user);
			ejb.setMarkup(efMarkup.build(markupType));
		    return ejb;
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		return null;
    }
	
	public void converter(JeeslFacade facade, MSG message)
	{
		if(message.getScope()!=null) {message.setScope(facade.find(fbHd.getClassScope(),message.getScope()));}
	}
}