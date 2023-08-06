package org.jeesl.factory.ejb.io.log;

import org.jeesl.interfaces.model.io.logging.JeeslIoLog;
import org.jeesl.interfaces.model.io.logging.JeeslIoLogMilestone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbIoLogMilestoneFactory<LOG extends JeeslIoLog<?,?,?>,
										MILESTONE extends JeeslIoLogMilestone<LOG>
>
{
	final static Logger logger = LoggerFactory.getLogger(EjbIoLogMilestoneFactory.class);
	
	private final Class<MILESTONE> cMilestone;
    
	public EjbIoLogMilestoneFactory(final Class<MILESTONE> cMilestone)
	{       
        this.cMilestone = cMilestone;
	}
	
	public MILESTONE build(LOG log)
	{
		MILESTONE ejb = null;
		try
		{
			 ejb = cMilestone.newInstance();
			 ejb.setLog(log);
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		
		return ejb;
	}
}