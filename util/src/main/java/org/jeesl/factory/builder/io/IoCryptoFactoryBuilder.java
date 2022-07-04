package org.jeesl.factory.builder.io;

import org.jeesl.factory.builder.AbstractFactoryBuilder;
import org.jeesl.factory.ejb.io.crypto.EjbCryptoKeyFactory;
import org.jeesl.interfaces.model.io.crypto.JeeslIoCryptoKey;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IoCryptoFactoryBuilder<L extends JeeslLang, D extends JeeslDescription,
								KEY extends JeeslIoCryptoKey<USER>,
								USER extends JeeslSimpleUser>
				extends AbstractFactoryBuilder<L,D>
{
	final static Logger logger = LoggerFactory.getLogger(IoCryptoFactoryBuilder.class);

	private final Class<KEY> cKey; public Class<KEY> getClassKey() {return cKey;}

	public IoCryptoFactoryBuilder(final Class<L> cL, final Class<D> cD,
									final Class<KEY> cKey
								)
	{
		super(cL,cD);
		this.cKey=cKey;
	}

	public EjbCryptoKeyFactory<KEY,USER> ejbKEy() {return new EjbCryptoKeyFactory<>(cKey);}
}