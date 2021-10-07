package org.jeesl.factory.builder.module;

import org.jeesl.factory.builder.AbstractFactoryBuilder;
import org.jeesl.factory.ejb.module.tafu.EjbTaskFactory;
import org.jeesl.interfaces.model.module.tafu.JeeslTafuStatus;
import org.jeesl.interfaces.model.module.tafu.JeeslTafuTask;
import org.jeesl.interfaces.model.module.tafu.JeeslTafuViewport;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.system.time.JeeslTimeDayOfWeek;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TafuFactoryBuilder<L extends JeeslLang,D extends JeeslDescription,
								R extends JeeslTenantRealm<L,D,R,?>,
								T extends JeeslTafuTask<R,TS>,
								TS extends JeeslTafuStatus<L,D,TS,?>,
								VP extends JeeslTafuViewport<L,D,VP,?>,
								DOW extends JeeslTimeDayOfWeek<L,D,DOW,?>>
		extends AbstractFactoryBuilder<L,D>
{
	final static Logger logger = LoggerFactory.getLogger(TafuFactoryBuilder.class);
	
	private final Class<R> cRealm; public Class<R> getClassRealm() {return cRealm;}
	
	private final Class<T> cTask; public Class<T> getClassTask() {return cTask;}
	private final Class<TS> cStatus; public Class<TS> getClassStatus() {return cStatus;}
	private final Class<VP> cVp; public Class<VP> getClassViewport() {return cVp;}
	private final Class<DOW> cDow; public Class<DOW> getClassDayOfWeek() {return cDow;}
	
	public TafuFactoryBuilder(final Class<L> cL,final Class<D> cD,
								final Class<R> cRealm,
								final Class<T> cTask,
								final Class<TS> cStatus,
								final Class<VP> cVp,
								final Class<DOW> cDow)
	{       
		super(cL,cD);
		this.cRealm=cRealm;
		this.cTask=cTask;
		this.cStatus=cStatus;
		this.cVp=cVp;
		this.cDow=cDow;
	}
	
	public EjbTaskFactory<R,T> ejbTask() {return new EjbTaskFactory<>(cTask);}
//	public EjbItsFactory<R,I,IS> ejbIssue() {return new EjbItsFactory<>(this);}
}