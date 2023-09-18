package org.jeesl.factory.builder.io.ssi;

import org.jeesl.factory.builder.AbstractFactoryBuilder;
import org.jeesl.factory.ejb.io.ssi.core.EjbIoSsiCredentialFactory;
import org.jeesl.factory.ejb.io.ssi.core.EjbIoSsiHostFactory;
import org.jeesl.factory.ejb.io.ssi.core.EjbIoSsiSystemFactory;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiCredential;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiHost;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiSystem;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IoSsiCoreFactoryBuilder<L extends JeeslLang, D extends JeeslDescription,
								SYSTEM extends JeeslIoSsiSystem<L,D>,
								CRED extends JeeslIoSsiCredential<SYSTEM>,
								HOST extends JeeslIoSsiHost<L,D,SYSTEM>>
		extends AbstractFactoryBuilder<L,D>
{
	private static final long serialVersionUID = 1L;

	final static Logger logger = LoggerFactory.getLogger(IoSsiCoreFactoryBuilder.class);
	
	private final Class<SYSTEM> cSystem; public Class<SYSTEM> getClassSystem(){return cSystem;}
	private final Class<CRED> cCredential; public Class<CRED> getClassCredential(){return cCredential;}
	private final Class<HOST> cHost; public Class<HOST> getClassHost(){return cHost;}
	
	public IoSsiCoreFactoryBuilder(final Class<L> cL, final Class<D> cD,
								final Class<SYSTEM> cSystem,
								final Class<CRED> cCredential,
								final Class<HOST> cHost)
	{
		super(cL,cD);
		this.cSystem=cSystem;
		this.cCredential=cCredential;
		this.cHost=cHost;
	}
	
	public EjbIoSsiSystemFactory<SYSTEM> ejbSystem() {return new EjbIoSsiSystemFactory<>(cSystem);}
	public EjbIoSsiCredentialFactory<SYSTEM,CRED> ejbCredential() {return new EjbIoSsiCredentialFactory<>(cCredential);}
	public EjbIoSsiHostFactory<SYSTEM,HOST> ejbHost() {return new EjbIoSsiHostFactory<>(cSystem,cHost);}
	
}