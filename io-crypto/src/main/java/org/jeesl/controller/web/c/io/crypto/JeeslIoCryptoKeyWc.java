package org.jeesl.controller.web.c.io.crypto;

import org.jeesl.controller.web.g.io.crypto.JeeslIoCryptoKeyGwc;
import org.jeesl.factory.builder.io.IoCryptoFactoryBuilder;
import org.jeesl.model.ejb.io.crypto.IoCryptoKey;
import org.jeesl.model.ejb.io.crypto.IoCryptoKeyLifetime;
import org.jeesl.model.ejb.io.crypto.IoCryptoKeyState;
import org.jeesl.model.ejb.io.crypto.IoCryptoStoreType;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoLocale;
import org.jeesl.model.ejb.system.security.user.SecurityUser;

public class JeeslIoCryptoKeyWc extends JeeslIoCryptoKeyGwc<IoLang,IoDescription,IoLocale,IoCryptoKey,IoCryptoKeyLifetime,IoCryptoKeyState,IoCryptoStoreType,SecurityUser>
{
	private static final long serialVersionUID = 1L;
	
	public JeeslIoCryptoKeyWc(IoCryptoFactoryBuilder<IoLang,IoDescription,IoCryptoKey,IoCryptoKeyLifetime,IoCryptoKeyState,IoCryptoStoreType,SecurityUser> fbCrypto)
	{
		super(fbCrypto);	
	}
}