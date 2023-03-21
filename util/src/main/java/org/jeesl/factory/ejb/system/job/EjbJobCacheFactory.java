package org.jeesl.factory.ejb.system.job;

import java.util.Date;

import org.jeesl.interfaces.model.system.job.cache.JeeslJobCache;
import org.jeesl.interfaces.model.system.job.template.JeeslJobTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbJobCacheFactory <TEMPLATE extends JeeslJobTemplate<?,?,?,?,?,?>,
									CACHE extends JeeslJobCache<TEMPLATE,?>
									>
{
	final static Logger logger = LoggerFactory.getLogger(EjbJobCacheFactory.class);
	
	private final Class<CACHE> cCache;

	public EjbJobCacheFactory(final Class<CACHE> cCache)
	{
        this.cCache = cCache;
	}
 
	public CACHE build(TEMPLATE template, String code, byte[] data)
	{
		CACHE ejb = null;
		try
		{
			ejb = cCache.newInstance();
			ejb.setTemplate(template);
			ejb.setRecord(new Date());
			ejb.setCode(code);
			ejb.setData(data);
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		
		return ejb;
	}
}