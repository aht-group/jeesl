package org.jeesl.interfaces.bean.system.io.crypto;

import org.jeesl.interfaces.model.io.crypto.JeeslIoCryptoKey;
import org.jeesl.interfaces.model.io.crypto.JeeslIoCryptoKeyState;
import org.jeesl.interfaces.model.io.crypto.JeeslIoCryptoKeyStatus;
import org.jeesl.interfaces.model.io.crypto.JeeslIoCryptoStoreType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;

public interface JeeslIoCryptoBean <L extends JeeslLang, D extends JeeslDescription,
									KEY extends JeeslIoCryptoKey<USER,KS>,
									KS extends JeeslIoCryptoKeyStatus<L,D,KS,?>,
									KT extends JeeslIoCryptoKeyState<L,D,KT,?>,
									ST extends JeeslIoCryptoStoreType<L,D,ST,?>,
									USER extends JeeslSimpleUser>
		extends JeeslIoCryptoOptionBean<KT,ST>
{

}