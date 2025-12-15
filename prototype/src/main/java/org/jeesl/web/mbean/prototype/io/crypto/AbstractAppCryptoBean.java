package org.jeesl.web.mbean.prototype.io.crypto;

import java.util.ArrayList;
import java.util.List;

import org.jeesl.api.facade.io.JeeslIoCryptoFacade;
import org.jeesl.factory.builder.io.IoCryptoFactoryBuilder;
import org.jeesl.interfaces.bean.system.io.crypto.JeeslIoCryptoBean;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.io.crypto.JeeslIoCryptoKey;
import org.jeesl.interfaces.model.io.crypto.JeeslIoCryptoKeyState;
import org.jeesl.interfaces.model.io.crypto.JeeslIoCryptoKeyLifetime;
import org.jeesl.interfaces.model.io.crypto.JeeslIoCryptoStoreType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractAppCryptoBean <L extends JeeslLang, D extends JeeslDescription,
											KEY extends JeeslIoCryptoKey<USER,LIFE>,
											LIFE extends JeeslIoCryptoKeyLifetime<L,D,LIFE,?>,
											KT extends JeeslIoCryptoKeyState<L,D,KT,?>,
											ST extends JeeslIoCryptoStoreType<L,D,ST,?>,
											USER extends JeeslSimpleUser>
					implements JeeslIoCryptoBean<L,D,KEY,LIFE,KT,ST,USER>
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractAppCryptoBean.class);

	protected final IoCryptoFactoryBuilder<L,D,KEY,LIFE,KT,ST,USER> fbCrypto;
	
	private ST typeMemory; public @Override ST getTypeMemory() {return typeMemory;}
	private ST typeSession; public ST getTypeSession() {return typeSession;}
	
	private KT stateUnlocked; @Override public KT getStateUnlocked() {return stateUnlocked;}
	private KT stateLocked; @Override public KT getStateLocked() {return stateLocked;}

	public AbstractAppCryptoBean(final IoCryptoFactoryBuilder<L,D,KEY,LIFE,KT,ST,USER> fbCrypto)
	{
		this.fbCrypto=fbCrypto;

		storeType = new ArrayList<>();
		keyLifetime = new ArrayList<>();
		keyState = new ArrayList<>();
	}
	
	protected void postConstructCrypto(JeeslIoCryptoFacade<L,D,KEY,LIFE,KT,ST,USER> fCrypto)
	{
		reloadOption(fCrypto);
	}
	
	private void reloadOption(JeeslFacade facade)
	{
		reloadStoreType(facade);
		reloadKeyStatus(facade);
		reloadKeyState(facade);
	}
	
	private final List<ST> storeType; public List<ST> getStoreType() {return storeType;}
	protected void reloadStoreType(JeeslFacade facade)
	{
		storeType.clear();
		storeType.addAll(facade.allOrderedPositionVisible(fbCrypto.getClassStoreType()));
		
		typeMemory = null;
		typeSession = null;
		for(ST st : storeType)
		{
			if(st.getCode().equals(JeeslIoCryptoStoreType.Code.memory.toString())) {typeMemory = st;}
			else if(st.getCode().equals(JeeslIoCryptoStoreType.Code.session.toString())) {typeSession = st;}
		}
	}
	
	private final List<LIFE> keyLifetime; public List<LIFE> getKeyLifetime() {return keyLifetime;}
	protected void reloadKeyStatus(JeeslFacade facade)
	{
		keyLifetime.clear();
		keyLifetime.addAll(facade.allOrderedPositionVisible(fbCrypto.getClassKeyStatus()));
	}
	
	private final List<KT> keyState; public List<KT> getKeyState() {return keyState;}
	protected void reloadKeyState(JeeslFacade facade)
	{
		keyState.clear();
		keyState.addAll(facade.allOrderedPositionVisible(fbCrypto.getClassKeyState()));
		
		stateLocked = null;
		stateUnlocked = null;
		for(KT kt : keyState)
		{
			if(kt.getCode().equals(JeeslIoCryptoKeyState.Code.locked.toString())) {stateLocked = kt;}
			else if(kt.getCode().equals(JeeslIoCryptoKeyState.Code.unlocked.toString())) {stateUnlocked = kt;}
		}
	}
}