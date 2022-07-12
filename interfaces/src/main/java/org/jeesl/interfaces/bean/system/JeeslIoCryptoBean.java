package org.jeesl.interfaces.bean.system;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.jeesl.interfaces.model.io.crypto.JeeslIoCryptoKey;
import org.jeesl.interfaces.model.io.crypto.JeeslIoCryptoKeyState;
import org.jeesl.interfaces.model.io.crypto.JeeslIoCryptoKeyStatus;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;

public interface JeeslIoCryptoBean <L extends JeeslLang, D extends JeeslDescription,
									KEY extends JeeslIoCryptoKey<USER,KS>,
									KS extends JeeslIoCryptoKeyStatus<L,D,KS,?>,
									KT extends JeeslIoCryptoKeyState<L,D,KT,?>,
									USER extends JeeslSimpleUser>
		extends Serializable
{
	Map<KEY,KT> getMapState();
	
	void initKeys(List<KEY> keys);
	void unlock(KEY key);
	void lock(KEY key);
}