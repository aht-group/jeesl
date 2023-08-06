package org.jeesl.factory.builder.io;

import org.jeesl.factory.builder.AbstractFactoryBuilder;
import org.jeesl.factory.ejb.io.log.EjbIoLogLoopFactory;
import org.jeesl.factory.ejb.io.log.EjbIoLogMilestoneFactory;
import org.jeesl.interfaces.model.io.logging.JeeslIoLog;
import org.jeesl.interfaces.model.io.logging.JeeslIoLogLoop;
import org.jeesl.interfaces.model.io.logging.JeeslIoLogMilestone;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IoLogFactoryBuilder<L extends JeeslLang,D extends JeeslDescription,
								LOG extends JeeslIoLog<?,?,?>,
								MILESTONE extends JeeslIoLogMilestone<LOG>,
								LOOP extends JeeslIoLogLoop<LOG>>
		extends AbstractFactoryBuilder<L,D>
{
	private static final long serialVersionUID = 1L;

	final static Logger logger = LoggerFactory.getLogger(IoLogFactoryBuilder.class);
	
	private final Class<MILESTONE> cMileStone;  public Class<MILESTONE> getClassRetention(){return cMileStone;}
	private final Class<LOOP> cLoop;  public Class<LOOP> getClassLoop(){return cLoop;}
	
	public IoLogFactoryBuilder(final Class<L> cL, final Class<D> cD,
								final Class<MILESTONE> cMileStone,
								final Class<LOOP> cLoop)
	{
		super(cL,cD);
		this.cMileStone=cMileStone;
		this.cLoop=cLoop;
	}
	
	public EjbIoLogMilestoneFactory<LOG,MILESTONE> ejbMilestone() {return new EjbIoLogMilestoneFactory<>(cMileStone);}
	public EjbIoLogLoopFactory<LOG,LOOP> ejbLoop() {return new EjbIoLogLoopFactory<>(cLoop);}
}