package org.jeesl.factory.ejb.module.rmmv;

import java.util.List;

import org.jeesl.factory.ejb.util.EjbPositionFactory;
import org.jeesl.interfaces.model.module.rmmv.JeeslRmmvModule;
import org.jeesl.interfaces.model.module.rmmv.JeeslRmmvModuleConfig;
import org.jeesl.interfaces.model.module.rmmv.JeeslRmmvTreeElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbRmmvConfigFactory<TE extends JeeslRmmvTreeElement<?,?,TE>,
									MOD extends JeeslRmmvModule<?,?,MOD,?>,
									MC extends JeeslRmmvModuleConfig<TE,MOD>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbRmmvConfigFactory.class);
	
//	private final EjbIoCmsMarkupFactory<M,MT> efMarkup;
	
	private final Class<MC> cConfig;

    public EjbRmmvConfigFactory(final Class<MC> cConfig)
    {
        this.cConfig = cConfig;
    }
	
	public MC build(TE element, List<MC> list)
	{
		try
		{
			MC ejb = cConfig.newInstance();
			ejb.setElement(element);
			ejb.setVisible(false);
			EjbPositionFactory.next(ejb,list);
		    return ejb;
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		return null;
    }
}