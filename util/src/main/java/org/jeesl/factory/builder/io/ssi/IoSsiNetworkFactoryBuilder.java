package org.jeesl.factory.builder.io.ssi;

import org.jeesl.factory.builder.AbstractFactoryBuilder;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiHost;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiSystem;
import org.jeesl.interfaces.model.io.ssi.nat.JeeslIoSsiNat;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IoSsiNetworkFactoryBuilder<L extends JeeslLang, D extends JeeslDescription,
								SYSTEM extends JeeslIoSsiSystem<L,D>,		
								HOST extends JeeslIoSsiHost<L,D,SYSTEM>,
								NAT extends JeeslIoSsiNat<L,D,HOST>>
		extends AbstractFactoryBuilder<L,D>
{
	private static final long serialVersionUID = 1L;

	final static Logger logger = LoggerFactory.getLogger(IoSsiCoreFactoryBuilder.class);
	
	private final Class<SYSTEM> cSystem; public Class<SYSTEM> getClassSystem(){return cSystem;}
	private final Class<NAT> cNat; public Class<NAT> getClassCredential(){return cNat;}
	private final Class<HOST> cHost; public Class<HOST> getClassHost(){return cHost;}
	
	public IoSsiNetworkFactoryBuilder(final Class<L> cL, final Class<D> cD,
								final Class<SYSTEM> cSystem,
								final Class<HOST> cHost,
								final Class<NAT> cNat)
	{
		super(cL,cD);
		this.cSystem=cSystem;
		this.cHost=cHost;
		this.cNat=cNat;
	}
}