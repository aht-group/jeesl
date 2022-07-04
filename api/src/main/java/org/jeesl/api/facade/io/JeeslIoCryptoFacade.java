package org.jeesl.api.facade.io;

import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.io.crypto.JeeslIoCryptoKey;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;

public interface JeeslIoCryptoFacade <L extends JeeslLang,D extends JeeslDescription,
									KEY extends JeeslIoCryptoKey<USER>,
									USER extends JeeslSimpleUser>
			extends JeeslFacade
{

}