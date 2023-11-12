package org.jeesl.factory.ejb.module.ts;

import org.jeesl.interfaces.model.module.ts.config.JeeslTsInterval;
import org.jeesl.interfaces.model.module.ts.core.JeeslTsScope;
import org.jeesl.interfaces.model.module.ts.stat.JeeslTsCron;
import org.jeesl.interfaces.model.module.ts.stat.JeeslTsStatistic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class EjbTsCronFactory <	SCOPE extends JeeslTsScope<?,?,?,?,?,?,INT>,
								INT extends JeeslTsInterval<?,?,INT,?>,
								STAT extends JeeslTsStatistic<?,?,STAT,?>,
								CRON extends JeeslTsCron<SCOPE,INT,STAT>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbTsCronFactory.class);
	
	final Class<CRON> cCron;
	
	public EjbTsCronFactory(final Class<CRON> cCron)
	{
		this.cCron = cCron;
	}
	
	public CRON build(STAT statDst, SCOPE scope)
	{
		CRON ejb = null;
		try
		{
			ejb = cCron.newInstance();
			ejb.setScope(scope);
			ejb.setStatisticDst(statDst);
		} 
		catch (InstantiationException e) {e.printStackTrace();} 
		catch (IllegalAccessException e) {e.printStackTrace();}
		return ejb;
	}
	
	public CRON build()
	{
		CRON ejb = null;
		try 
		{
			ejb = cCron.newInstance();
		} 
		catch (InstantiationException e) {e.printStackTrace();} 
		catch (IllegalAccessException e) {e.printStackTrace();}
		return ejb;
	}
}
