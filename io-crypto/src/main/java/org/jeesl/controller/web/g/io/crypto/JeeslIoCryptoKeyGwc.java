package org.jeesl.controller.web.g.io.crypto;

import java.io.Serializable;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import org.apache.commons.lang3.ObjectUtils;
import org.exlp.util.io.StringUtil;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.io.JeeslIoCryptoFacade;
import org.jeesl.controller.web.AbstractJeeslLocaleWebController;
import org.jeesl.controller.web.util.AbstractLogMessage;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.io.IoCryptoFactoryBuilder;
import org.jeesl.factory.ejb.util.EjbIdFactory;
import org.jeesl.factory.txt.io.crypto.TxtCryptoFactory;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
import org.jeesl.interfaces.model.io.crypto.JeeslIoCryptoKey;
import org.jeesl.interfaces.model.io.crypto.JeeslIoCryptoKeyLifetime;
import org.jeesl.interfaces.model.io.crypto.JeeslIoCryptoKeyState;
import org.jeesl.interfaces.model.io.crypto.JeeslIoCryptoStore;
import org.jeesl.interfaces.model.io.crypto.JeeslIoCryptoStoreType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;
import org.jeesl.jsf.handler.PositionListReorderer;
import org.jeesl.util.db.cache.EjbCodeCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslIoCryptoKeyGwc <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
											KEY extends JeeslIoCryptoKey<USER,KS>,
											KS extends JeeslIoCryptoKeyLifetime<L,D,KS,?>,
											KT extends JeeslIoCryptoKeyState<L,D,KT,?>,
											ST extends JeeslIoCryptoStoreType<L,D,ST,?>,
											USER extends JeeslSimpleUser>
					extends AbstractJeeslLocaleWebController<L,D,LOC>
					implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslIoCryptoKeyGwc.class);

	protected JeeslIoCryptoFacade<L,D,KEY,KS,KT,ST,USER> fCrypto;
	protected final IoCryptoFactoryBuilder<L,D,KEY,KS,KT,ST,USER> fbCrypto;
	
	private EjbCodeCache<KT> cacheState;
	
	private final Map<JeeslIoCryptoStore<KEY,KT,ST>,Boolean> mapUnlock; public Map<JeeslIoCryptoStore<KEY, KT, ST>, Boolean> getMapUnlock() {return mapUnlock;}
	private final List<KEY> keys; public List<KEY> getKeys() {return keys;}
	private final List<JeeslIoCryptoStore<KEY,KT,ST>> stores; public List<JeeslIoCryptoStore<KEY, KT, ST>> getStores() {return stores;}
	
	private USER user;
	private KEY key; public KEY getKey() {return key;} public void setKey(KEY key) {this.key = key;}
	private JeeslIoCryptoStore<KEY,KT,ST> sessionKeystore; public JeeslIoCryptoStore<KEY, KT, ST> getSessionKeystore() {return sessionKeystore;}
	private SecretKey secret;
	
	private String pwd; public String getPwd() {return pwd;} public void setPwd(String pwd) {this.pwd = pwd;}
	private String memo; public String getMemo() {return memo;} public void setMemo(String memo) {this.memo = memo;}
	
	public JeeslIoCryptoKeyGwc(final IoCryptoFactoryBuilder<L,D,KEY,KS,KT,ST,USER> fbCrypto)
	{
		super(fbCrypto.getClassL(),fbCrypto.getClassD());
		this.fbCrypto=fbCrypto;

		mapUnlock = new HashMap<>();
		
		keys = new ArrayList<>();
		stores = new ArrayList<>();
		
	}

	public void postConstructCryptoKey(JeeslLocaleProvider<LOC> lp, JeeslFacesMessageBean bMessage,
										JeeslIoCryptoFacade<L,D,KEY,KS,KT,ST,USER> fCrypto,
										USER user,
										JeeslIoCryptoStore<KEY,KT,ST> sessionKeystore)
	{
		super.postConstructLocaleWebController(lp,bMessage);
		this.fCrypto=fCrypto;
		this.user=user;
		this.sessionKeystore=sessionKeystore;
		stores.add(sessionKeystore);
		
		cacheState = EjbCodeCache.instance(fbCrypto.getClassKeyState()).facade(fCrypto);
		
		reloadKeys();
	}
	
	public void reorderKeys() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fCrypto,keys); if(EjbIdFactory.isSaved(key)) {key = fCrypto.find(fbCrypto.getClassKey(), key);}}
	
	public void addStore(JeeslIoCryptoStore<KEY,KT,ST> store)
	{
		stores.add(store);
	}
	
	public void cancelKey() {reset(true,true,true);}
	private void reset(boolean rKey, boolean rPwd, boolean rMemo)
	{
		if(rKey) {key = null; secret=null;}
		if(rPwd) {pwd = null;}
		if(rMemo) {memo = null;}
	}

	private void reloadKeys()
	{
		keys.clear();
		keys.addAll(fCrypto.all(fbCrypto.getClassKey()));
		
		
	}

	public void addKey() throws JeeslNotFoundException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.createEntity(fbCrypto.getClassKey()));}
		this.reset(true,true,true);
		key = fbCrypto.ejbKey().build(user);
	}

	public void selectKey() throws JeeslNotFoundException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException
	{
		this.reset(false,true,true);
		if(debugOnInfo){logger.info(AbstractLogMessage.selectEntity(key));}
//		attribute = fRevision.find(fbRevision.getClassAttribute(), attribute);
//		attribute = efLang.persistMissingLangs(fRevision,langs,attribute);
//		attribute = efDescription.persistMissingLangs(fRevision,langs,attribute);
		
		if(sessionKeystore.isUnlocked(key))
		{
			secret = sessionKeystore.getSecretKey(key);
			IvParameterSpec iv = TxtCryptoFactory.buildIv(key.getIv());
			memo = TxtCryptoFactory.decrypt(TxtCryptoFactory.encrpytionAlgorithm,key.getVerification(),secret,iv);
		}
		this.updateMapUnlock();
	}
	
	public void saveKey() throws JeeslNotFoundException, JeeslConstraintViolationException, JeeslLockingException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.selectEntity(key));}
		fbCrypto.ejbKey().converter(fCrypto,key);

		logger.info("Unsaved: "+EjbIdFactory.isUnSaved(key));
		if(ObjectUtils.isEmpty(pwd)) {return;}
		
		secret = TxtCryptoFactory.getKeyFromPassword(pwd,key.getSalt());
		 
		if(EjbIdFactory.isUnSaved(key))
		{
			 IvParameterSpec iv = TxtCryptoFactory.buildIv(key.getIv());
			 key.setVerification(TxtCryptoFactory.encrypt(secret,iv,memo));
			 key.setHash(TxtCryptoFactory.toHash(memo));
			 key = fCrypto.save(key);
			 sessionKeystore.enable(key,fCrypto.fByEnum(fbCrypto.getClassKeyState(),JeeslIoCryptoKeyState.Code.unlocked), secret);
			 this.reloadKeys();
			 this.reset(false,true,false);
		}
		else
		{
			memo=null;
			IvParameterSpec iv = TxtCryptoFactory.buildIv(key.getIv());
			try
			{
				memo = TxtCryptoFactory.decrypt(secret,iv,key.getVerification());
			}
			catch (BadPaddingException e) {}
		}	
		this.updateMapUnlock();
	}
	
	private void updateMapUnlock()
	{
		mapUnlock.clear();
		for(JeeslIoCryptoStore<KEY,KT,ST> store : stores)
		{
			boolean unlocked = store.isUnlocked(key);
			logger.trace(store.getClass().getSimpleName()+" "+unlocked);
			mapUnlock.put(store,unlocked);
		}
	}
	
	public void toggleKey(JeeslIoCryptoStore<KEY,KT,ST> store)
	{
		boolean unlocked = mapUnlock.get(store);
		boolean isSessionStore = store.equals(sessionKeystore);
		logger.info(StringUtil.stars());
		logger.info("Toggle to "+unlocked+" sessionStore:"+isSessionStore);
		if(unlocked)
		{
			logger.debug("Enable: "+store.getClass().getSimpleName());
			store.enable(key, cacheState.ejb(JeeslIoCryptoKeyState.Code.unlocked), secret);
		}
		else
		{
			store.disable(key,cacheState.ejb(JeeslIoCryptoKeyState.Code.locked));
		}
		
		this.updateMapUnlock();
	}
}