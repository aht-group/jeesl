package org.jeesl.factory.builder.io.ssi;

import org.jeesl.factory.builder.AbstractFactoryBuilder;
import org.jeesl.factory.ejb.io.ssi.core.EjbIoSsiSystemFactory;
import org.jeesl.factory.ejb.io.ssi.docker.EjbIoSsiContainerFactory;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiHost;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiSystem;
import org.jeesl.interfaces.model.io.ssi.docker.JeeslIoDockerContainer;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IoSsiDockerFactoryBuilder<L extends JeeslLang,D extends JeeslDescription,
										SYSTEM extends JeeslIoSsiSystem<L,D>,
										INSTANCE extends JeeslIoDockerContainer<SYSTEM,HOST>,
										HOST extends JeeslIoSsiHost<L,D,SYSTEM>>
		extends AbstractFactoryBuilder<L,D>
{
	private static final long serialVersionUID = 1L;

	final static Logger logger = LoggerFactory.getLogger(IoSsiDockerFactoryBuilder.class);
	
	private final Class<SYSTEM> cSystem; public Class<SYSTEM> getClassSystem(){return cSystem;}
	private final Class<INSTANCE> cInstance; public Class<INSTANCE> getClassInstance(){return cInstance;}
	private final Class<HOST> cHost; public Class<HOST> getClassHost(){return cHost;}
	
	public IoSsiDockerFactoryBuilder(final Class<L> cL, final Class<D> cD,
								final Class<SYSTEM> cSystem,
								final Class<INSTANCE> cInstance,
								final Class<HOST> cHost)
	{
		super(cL,cD);
		this.cSystem=cSystem;
		this.cInstance=cInstance;
		this.cHost=cHost;
	}
	
	public EjbIoSsiSystemFactory<SYSTEM> ejbSystem() {return new EjbIoSsiSystemFactory<>(cSystem);}
	public EjbIoSsiContainerFactory<SYSTEM,INSTANCE,HOST> ejbInstance() {return new EjbIoSsiContainerFactory<>(cInstance);}
}