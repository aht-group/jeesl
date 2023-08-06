package org.jeesl.factory.ejb.io.log;

import org.jeesl.interfaces.model.io.logging.JeeslIoLog;
import org.jeesl.interfaces.model.io.logging.JeeslIoLogLoop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbIoLogLoopFactory<LOG extends JeeslIoLog<?,?,?>,
									LOOP extends JeeslIoLogLoop<LOG>
>
{
	final static Logger logger = LoggerFactory.getLogger(EjbIoLogLoopFactory.class);
	
	private final Class<LOOP> cLoop;
    
	public EjbIoLogLoopFactory(final Class<LOOP> cLoop)
	{       
        this.cLoop = cLoop;
	}
	
	public LOOP build(LOG log, String code)
	{
		LOOP ejb = null;
		try
		{
			 ejb = cLoop.newInstance();
			 ejb.setCode(code);
			 ejb.setLog(log);
			 ejb.setCounter(0);
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		
		return ejb;
	}
}