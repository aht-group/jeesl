package org.jeesl.factory.ejb.module.rmmv;

import java.util.ArrayList;
import java.util.List;

import org.jeesl.factory.ejb.util.EjbPositionFactory;
import org.jeesl.interfaces.model.module.rmmv.JeeslRmmvModuleConfig;
import org.jeesl.interfaces.model.module.rmmv.JeeslRmmvSubscription;
import org.jeesl.interfaces.model.module.rmmv.JeeslRmmvSubscriptionItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbRmmvSubscriptionItemFactory<MC extends JeeslRmmvModuleConfig<?,?>,
											SUB extends JeeslRmmvSubscription<?,?,?>,
											SI extends JeeslRmmvSubscriptionItem<SUB,MC>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbRmmvSubscriptionItemFactory.class);
	
	private final Class<SI> cItem;

    public EjbRmmvSubscriptionItemFactory(final Class<SI> cItem)
    {
        this.cItem = cItem;
    }
	
	public SI build(SUB subscription, List<SI> list)
	{
		try
		{
			SI ejb = cItem.newInstance();
			ejb.setSubscription(subscription);
			EjbPositionFactory.next(ejb,list);
		
		    return ejb;
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		return null;
    }
	
	public List<MC> toConfigs(List<SI> items)
	{
		List<MC> result = new ArrayList<>();
		for(SI item : items) {result.add(item.getConfig());}
		return result;
	}
}