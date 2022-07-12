package org.jeesl.web.mbean.prototype.io.crypto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.SecretKey;

import org.jeesl.api.facade.io.JeeslIoCryptoFacade;
import org.jeesl.factory.builder.io.IoCryptoFactoryBuilder;
import org.jeesl.interfaces.bean.system.JeeslIoCryptoBean;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.io.crypto.JeeslIoCryptoKey;
import org.jeesl.interfaces.model.io.crypto.JeeslIoCryptoKeyState;
import org.jeesl.interfaces.model.io.crypto.JeeslIoCryptoKeyStatus;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.security.user.JeeslKeyStore;
import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractAppCryptoBean <L extends JeeslLang, D extends JeeslDescription,
											KEY extends JeeslIoCryptoKey<USER,KS>,
											KS extends JeeslIoCryptoKeyStatus<L,D,KS,?>,
											KT extends JeeslIoCryptoKeyState<L,D,KT,?>,
											USER extends JeeslSimpleUser>
					implements JeeslIoCryptoBean<L,D,KEY,KS,KT,USER>,JeeslKeyStore<KEY>
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractAppCryptoBean.class);

	protected final IoCryptoFactoryBuilder<L,D,KEY,KS,KT,USER> fbCrypto;
	private JeeslIoCryptoFacade<L,D,KEY,KS,KT,USER> fCrypto;
	
	private final Map<KEY,String> mapPwd;
	private final Map<KEY,KT> mapState; public Map<KEY,KT> getMapState() {return mapState;}

	public AbstractAppCryptoBean(final IoCryptoFactoryBuilder<L,D,KEY,KS,KT,USER> fbCrypto)
	{
		this.fbCrypto=fbCrypto;

		keyStatus = new ArrayList<>();
		
		mapPwd = new HashMap<>();
		mapState = new HashMap<>();
	}
	
	protected void postConstructCrypto(JeeslIoCryptoFacade<L,D,KEY,KS,KT,USER> fCrypto)
	{
		this.fCrypto=fCrypto;
		reload(fCrypto);
	}
	
	private void reload(JeeslFacade facade)
	{
		reloadKeyStatus(facade);
	}
	
	private final List<KS> keyStatus; public List<KS> getKeyStatus() {return keyStatus;}
	protected void reloadKeyStatus(JeeslFacade facade)
	{
		keyStatus.clear();
		keyStatus.addAll(facade.allOrderedPositionVisible(fbCrypto.getClassKeyStatus()));
	}
	
	@Override public void initKeys(List<KEY> keys)
	{
		for(KEY k : keys)
		{
			if(!mapState.containsKey(k))
			{
				mapState.put(k,fCrypto.fByEnum(fbCrypto.getClassKeyState(),JeeslIoCryptoKeyState.Code.locked));
			}
		}	
	}

	@Override public void unlock(KEY key)
	{
		mapState.put(key,fCrypto.fByEnum(fbCrypto.getClassKeyState(),JeeslIoCryptoKeyState.Code.unlocked));
	}
	
	@Override public void lock(KEY key)
	{
		mapState.put(key,fCrypto.fByEnum(fbCrypto.getClassKeyState(),JeeslIoCryptoKeyState.Code.locked));
		if(mapPwd.containsKey(key)) {mapPwd.remove(key);}
	}
	
	@Override public void unlock(KEY key, String pwd)
	{
		this.unlock(key);
	}

	@Override
	public SecretKey getSecretKey(KEY key)
	{
		// TODO Auto-generated method stub
		return null;
	}
}