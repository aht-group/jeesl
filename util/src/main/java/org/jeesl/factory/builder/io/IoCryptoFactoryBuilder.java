package org.jeesl.factory.builder.io;

import org.jeesl.factory.builder.AbstractFactoryBuilder;
import org.jeesl.factory.ejb.io.crypto.EjbCryptoKeyFactory;
import org.jeesl.interfaces.model.io.crypto.JeeslIoCryptoKey;
import org.jeesl.interfaces.model.io.crypto.JeeslIoCryptoKeyState;
import org.jeesl.interfaces.model.io.crypto.JeeslIoCryptoKeyStatus;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IoCryptoFactoryBuilder<L extends JeeslLang, D extends JeeslDescription,
								KEY extends JeeslIoCryptoKey<USER,KS>,
								KS extends JeeslIoCryptoKeyStatus<L,D,KS,?>,
								KT extends JeeslIoCryptoKeyState<L,D,KT,?>,
								USER extends JeeslSimpleUser>
				extends AbstractFactoryBuilder<L,D>
{
	final static Logger logger = LoggerFactory.getLogger(IoCryptoFactoryBuilder.class);

	private final Class<KEY> cKey; public Class<KEY> getClassKey() {return cKey;}
	private final Class<KS> cKeyStatus; public Class<KS> getClassKeyStatus() {return cKeyStatus;}
	private final Class<KT> cKeyState; public Class<KT> getClassKeyState() {return cKeyState;}

	public IoCryptoFactoryBuilder(final Class<L> cL, final Class<D> cD,
									final Class<KEY> cKey,
									final Class<KS> cKeyStatus,
									final Class<KT> cKeyState
								)
	{
		super(cL,cD);
		this.cKey=cKey;
		this.cKeyStatus=cKeyStatus;
		this.cKeyState=cKeyState;
	}

	public EjbCryptoKeyFactory<KEY,KS,USER> ejbKEy() {return new EjbCryptoKeyFactory<>(cKey);}
}