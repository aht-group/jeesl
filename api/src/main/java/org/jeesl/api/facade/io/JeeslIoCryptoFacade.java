package org.jeesl.api.facade.io;

import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.io.crypto.JeeslIoCryptoKey;
import org.jeesl.interfaces.model.io.crypto.JeeslIoCryptoKeyState;
import org.jeesl.interfaces.model.io.crypto.JeeslIoCryptoKeyLifetime;
import org.jeesl.interfaces.model.io.crypto.JeeslIoCryptoStoreType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;

public interface JeeslIoCryptoFacade <L extends JeeslLang,D extends JeeslDescription,
									KEY extends JeeslIoCryptoKey<USER,KS>,
									KS extends JeeslIoCryptoKeyLifetime<?,?,KS,?>,
									KT extends JeeslIoCryptoKeyState<L,D,KT,?>,
									ST extends JeeslIoCryptoStoreType<L,D,ST,?>,
									USER extends JeeslSimpleUser>
			extends JeeslFacade
{
	void cryptoMemoryStoreUnlockKey(KEY key, String pwd);
}