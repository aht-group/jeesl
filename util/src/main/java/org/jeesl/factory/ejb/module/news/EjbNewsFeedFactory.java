package org.jeesl.factory.ejb.module.news;

import java.util.Objects;

import org.jeesl.interfaces.model.module.news.JeeslNewsFeed;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbNewsFeedFactory<L extends JeeslLang, D extends JeeslDescription,
								R extends JeeslTenantRealm<L,D,R,?>,
								FEED extends JeeslNewsFeed<L,D,R>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbNewsFeedFactory.class);
	
	final Class<FEED> cFeed;
	
    public EjbNewsFeedFactory(Class<FEED> cFeed)
    {
        this.cFeed = cFeed;
    }
	
	public <RREF extends EjbWithId> FEED build(R realm, RREF rref)
	{
		try
		{
			FEED ejb = cFeed.newInstance();
			ejb.setRealm(realm);
			if(Objects.nonNull(rref)) { ejb.setRref(rref.getId());}
			ejb.setVisible(true);
		    return ejb;
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		return null;
    }
}