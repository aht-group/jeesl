
package org.jeesl.controller.facade.io;

import javax.persistence.EntityManager;

import org.jeesl.api.facade.io.JeeslIoCryptoFacade;
import org.jeesl.controller.facade.JeeslFacadeBean;
import org.jeesl.controller.handler.system.io.crypto.JeeslMemoryKeyStore;
import org.jeesl.factory.builder.io.IoCryptoFactoryBuilder;
import org.jeesl.interfaces.model.io.crypto.JeeslIoCryptoKey;
import org.jeesl.interfaces.model.io.crypto.JeeslIoCryptoKeyState;
import org.jeesl.interfaces.model.io.crypto.JeeslIoCryptoKeyStatus;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;

public class JeeslIoCryptoFacadeBean <L extends JeeslLang,D extends JeeslDescription,
KEY extends JeeslIoCryptoKey<USER,KS>,
KS extends JeeslIoCryptoKeyStatus<L,D,KS,?>,
KT extends JeeslIoCryptoKeyState<L,D,KT,?>,
USER extends JeeslSimpleUser>
		extends JeeslFacadeBean implements JeeslIoCryptoFacade<L,D,KEY,KS,KT,USER>
{
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private final IoCryptoFactoryBuilder<L,D,KEY,KS,KT,USER> fbCrypto;
	
	public JeeslIoCryptoFacadeBean(EntityManager em, IoCryptoFactoryBuilder<L,D,KEY,KS,KT,USER> fbCrypto)
	{
		super(em);
		this.fbCrypto=fbCrypto;
	}
	
	@Override public void cryptoMemoryStoreUnlockKey(KEY key, String pwd) {JeeslMemoryKeyStore.instance().unlock(key,pwd);}
}