
package org.jeesl.controller.facade.jx.io;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKey;
import javax.persistence.EntityManager;

import org.jeesl.api.facade.io.JeeslIoCryptoFacade;
import org.jeesl.controller.facade.jx.JeeslFacadeBean;
import org.jeesl.controller.handler.io.crypto.JeeslMemoryKeyStore;
import org.jeesl.factory.builder.io.IoCryptoFactoryBuilder;
import org.jeesl.factory.txt.io.crypto.TxtCryptoFactory;
import org.jeesl.interfaces.model.io.crypto.JeeslIoCryptoKey;
import org.jeesl.interfaces.model.io.crypto.JeeslIoCryptoKeyLifetime;
import org.jeesl.interfaces.model.io.crypto.JeeslIoCryptoKeyState;
import org.jeesl.interfaces.model.io.crypto.JeeslIoCryptoStore;
import org.jeesl.interfaces.model.io.crypto.JeeslIoCryptoStoreType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;

public class JeeslIoCryptoFacadeBean <L extends JeeslLang,D extends JeeslDescription,
										KEY extends JeeslIoCryptoKey<USER,KS>,
										KS extends JeeslIoCryptoKeyLifetime<L,D,KS,?>,
										KT extends JeeslIoCryptoKeyState<L,D,KT,?>,
										ST extends JeeslIoCryptoStoreType<L,D,ST,?>,
										USER extends JeeslSimpleUser>
		extends JeeslFacadeBean implements JeeslIoCryptoFacade<L,D,KEY,KS,KT,ST,USER>
{
	private static final long serialVersionUID = 1L;

	private final IoCryptoFactoryBuilder<L,D,KEY,KS,KT,ST,USER> fbCrypto;
	
	public JeeslIoCryptoFacadeBean(EntityManager em, IoCryptoFactoryBuilder<L,D,KEY,KS,KT,ST,USER> fbCrypto)
	{
		super(em);
		this.fbCrypto=fbCrypto;
	}
	
	@Override public void cryptoMemoryStoreUnlockKey(KEY key, String pwd)
	{
		SecretKey secret;
		KT state = null;
		try
		{
			secret = TxtCryptoFactory.getKeyFromPassword(pwd,key.getSalt());
			state = this.fByEnum(fbCrypto.getClassKeyState(),JeeslIoCryptoKeyState.Code.unlocked);
		}
		catch (NoSuchAlgorithmException | InvalidKeySpecException e)
		{
			secret = null;
			state = this.fByEnum(fbCrypto.getClassKeyState(),JeeslIoCryptoKeyState.Code.locked);
		}
		JeeslIoCryptoStore<KEY,KT,ST> memory = JeeslMemoryKeyStore.instance();
		memory.enable(key,state,secret);
	}
}