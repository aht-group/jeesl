package org.jeesl.factory.ejb.system.job;

import org.jeesl.interfaces.model.system.job.maintenance.JeeslJobRobot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbJobRobotFactory <ROBOT extends JeeslJobRobot<?,?>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbJobRobotFactory.class);
	
	private final Class<ROBOT> cRobot;

	public EjbJobRobotFactory(final Class<ROBOT> cRobot)
	{
        this.cRobot = cRobot;
	}
 
	public ROBOT build()
	{
		ROBOT ejb = null;
		try
		{
			ejb = cRobot.newInstance();

		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		
		return ejb;
	}
}