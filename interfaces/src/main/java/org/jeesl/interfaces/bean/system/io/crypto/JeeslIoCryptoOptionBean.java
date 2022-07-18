package org.jeesl.interfaces.bean.system.io.crypto;

import java.io.Serializable;

import org.jeesl.interfaces.model.io.crypto.JeeslIoCryptoKeyState;
import org.jeesl.interfaces.model.io.crypto.JeeslIoCryptoStoreType;

public interface JeeslIoCryptoOptionBean <KT extends JeeslIoCryptoKeyState<?,?,KT,?>,
											ST extends JeeslIoCryptoStoreType<?,?,ST,?>> extends Serializable
{
	KT getStateUnlocked();
	KT getStateLocked();
	
	ST getTypeMemory();
}