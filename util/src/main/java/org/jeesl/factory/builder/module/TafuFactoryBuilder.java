package org.jeesl.factory.builder.module;

import org.jeesl.factory.builder.AbstractFactoryBuilder;
import org.jeesl.factory.ejb.io.cms.EjbIoCmsMarkupFactory;
import org.jeesl.factory.ejb.module.tafu.EjbTaskFactory;
import org.jeesl.interfaces.model.io.cms.markup.JeeslIoMarkupType;
import org.jeesl.interfaces.model.io.cms.markup.JeeslIoMarkup;
import org.jeesl.interfaces.model.module.calendar.unit.JeeslCalendarDayOfWeek;
import org.jeesl.interfaces.model.module.tafu.JeeslTafuScope;
import org.jeesl.interfaces.model.module.tafu.JeeslTafuStatus;
import org.jeesl.interfaces.model.module.tafu.JeeslTafuTask;
import org.jeesl.interfaces.model.module.tafu.JeeslTafuViewport;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TafuFactoryBuilder<L extends JeeslLang,D extends JeeslDescription,
								R extends JeeslTenantRealm<L,D,R,?>,
								T extends JeeslTafuTask<R,TS,SC,M>,
								TS extends JeeslTafuStatus<L,D,TS,?>,
								SC extends JeeslTafuScope<?,?,R,SC,?>,
								VP extends JeeslTafuViewport<L,D,VP,?>,
								DOW extends JeeslCalendarDayOfWeek<L,D,DOW,?>,
								M extends JeeslIoMarkup<MT>,
								MT extends JeeslIoMarkupType<L,D,MT,?>>
		extends AbstractFactoryBuilder<L,D>
{
	final static Logger logger = LoggerFactory.getLogger(TafuFactoryBuilder.class);
	
	private final Class<R> cRealm; public Class<R> getClassRealm() {return cRealm;}
	
	private final Class<T> cTask; public Class<T> getClassTask() {return cTask;}
	private final Class<TS> cStatus; public Class<TS> getClassStatus() {return cStatus;}
	private final Class<SC> cScope; public Class<SC> getClassScope() {return cScope;}
	private final Class<VP> cVp; public Class<VP> getClassViewport() {return cVp;}
	private final Class<DOW> cDow; public Class<DOW> getClassDayOfWeek() {return cDow;}
	private final Class<M> cMarkup; public Class<M> getClassMarkup() {return cMarkup;}
	private final Class<MT> cMarkupType; public Class<MT> getClassMarkupType() {return cMarkupType;}
	
	public TafuFactoryBuilder(final Class<L> cL,final Class<D> cD,
								final Class<R> cRealm,
								final Class<T> cTask,
								final Class<TS> cStatus,
								final Class<SC> cScope,
								final Class<VP> cVp,
								final Class<DOW> cDow,
								final Class<M> cMarkup,
								final Class<MT> cMarkupType)
	{       
		super(cL,cD);
		this.cRealm=cRealm;
		this.cTask=cTask;
		this.cStatus=cStatus;
		this.cScope=cScope;
		this.cVp=cVp;
		this.cDow=cDow;
		this.cMarkup=cMarkup;
		this.cMarkupType=cMarkupType;
	}
	
	public EjbIoCmsMarkupFactory<M,MT> ejbMarkup() {return new EjbIoCmsMarkupFactory<>(cMarkup);}
	public EjbTaskFactory<R,T,TS,SC,M,MT> ejbTask() {return new EjbTaskFactory<>(this);}
//	public EjbItsFactory<R,I,IS> ejbIssue() {return new EjbItsFactory<>(this);}
}