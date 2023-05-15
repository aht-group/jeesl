package org.jeesl.controller.web.g.io.crypto;

import java.io.Serializable;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import org.jeesl.api.bean.JeeslTranslationBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.io.JeeslIoCryptoFacade;
import org.jeesl.controller.web.AbstractJeeslWebController;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.io.IoCryptoFactoryBuilder;
import org.jeesl.factory.ejb.util.EjbIdFactory;
import org.jeesl.factory.txt.io.crypto.TxtCryptoFactory;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
import org.jeesl.interfaces.model.io.crypto.JeeslIoCryptoKey;
import org.jeesl.interfaces.model.io.crypto.JeeslIoCryptoKeyState;
import org.jeesl.interfaces.model.io.crypto.JeeslIoCryptoKeyStatus;
import org.jeesl.interfaces.model.io.crypto.JeeslIoCryptoStore;
import org.jeesl.interfaces.model.io.crypto.JeeslIoCryptoStoreType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;
import org.jeesl.jsf.handler.PositionListReorderer;
import org.jeesl.model.ejb.system.security.AbstractSessionKeystore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;

public class JeeslIoCryptoKeyGwc <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
											KEY extends JeeslIoCryptoKey<USER,KS>,
											KS extends JeeslIoCryptoKeyStatus<L,D,KS,?>,
											KT extends JeeslIoCryptoKeyState<L,D,KT,?>,
											ST extends JeeslIoCryptoStoreType<L,D,ST,?>,
											USER extends JeeslSimpleUser>
					extends AbstractJeeslWebController<L,D,LOC>
					implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslIoCryptoKeyGwc.class);

	protected JeeslIoCryptoFacade<L,D,KEY,KS,KT,ST,USER> fCrypto;
	protected final IoCryptoFactoryBuilder<L,D,KEY,KS,KT,ST,USER> fbCrypto;
	
	private final List<KEY> keys; public List<KEY> getKeys() {return keys;}
	private final List<JeeslIoCryptoStore<KEY,KT,ST>> stores; public List<JeeslIoCryptoStore<KEY, KT, ST>> getStores() {return stores;}
	
	private USER user;
	private KEY key; public KEY getKey() {return key;} public void setKey(KEY key) {this.key = key;}
	private JeeslIoCryptoStore<KEY,KT,ST> sessionKeystore; public JeeslIoCryptoStore<KEY, KT, ST> getSessionKeystore() {return sessionKeystore;}
	
	private String pwd; public String getPwd() {return pwd;} public void setPwd(String pwd) {this.pwd = pwd;}
	private String clearVerification; public String getClearVerification() {return clearVerification;} public void setClearVerification(String clearVerification) {this.clearVerification = clearVerification;}
	
	public JeeslIoCryptoKeyGwc(final IoCryptoFactoryBuilder<L,D,KEY,KS,KT,ST,USER> fbCrypto)
	{
		super(fbCrypto.getClassL(),fbCrypto.getClassD());
		this.fbCrypto=fbCrypto;

		keys = new ArrayList<>();
		stores = new ArrayList<>();
		
	}

	public void postConstructCryptoKey(JeeslLocaleProvider<LOC> lp, JeeslFacesMessageBean bMessage,
										JeeslIoCryptoFacade<L,D,KEY,KS,KT,ST,USER> fCrypto,
										USER user,
										JeeslIoCryptoStore<KEY,KT,ST> sessionKeystore)
	{
		super.postConstructWebController(lp,bMessage);
		this.fCrypto=fCrypto;
		this.user=user;
		this.sessionKeystore=sessionKeystore;
		stores.add(sessionKeystore);
		reloadKeys();
	}
	
	public void addStore(JeeslIoCryptoStore<KEY,KT,ST> store)
	{
		stores.add(store);
	}
	
	public void cancelKey() {reset(true,true);}
	private void reset(boolean rKey,  boolean rPwd)
	{
		if(rKey) {key = null;}
		if(rPwd) {pwd = null;}
	}

	private void reloadKeys()
	{
		keys.clear();
		keys.addAll(fCrypto.all(fbCrypto.getClassKey()));
	}

	public void addKey() throws JeeslNotFoundException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.createEntity(fbCrypto.getClassKey()));}
		this.reset(true,true);
		key = fbCrypto.ejbKey().build(user);
	}

	public void selectKey() throws JeeslNotFoundException
	{
		this.reset(false,true);
		if(debugOnInfo){logger.info(AbstractLogMessage.selectEntity(key));}
//		attribute = fRevision.find(fbRevision.getClassAttribute(), attribute);
//		attribute = efLang.persistMissingLangs(fRevision,langs,attribute);
//		attribute = efDescription.persistMissingLangs(fRevision,langs,attribute);
	}
	
	public void saveKey() throws JeeslNotFoundException, JeeslConstraintViolationException, JeeslLockingException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, NoSuchPaddingException, InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.selectEntity(key));}
		fbCrypto.ejbKey().converter(fCrypto,key);

		logger.info("Unsaved: "+EjbIdFactory.isUnSaved(key));
		if(EjbIdFactory.isUnSaved(key))
		{
			if(pwd!=null && pwd.length()>0)
			{
				 SecretKey secret = AbstractSessionKeystore.getKeyFromPassword(pwd,key.getSalt());
				 IvParameterSpec iv = TxtCryptoFactory.buildIv(key.getIv());
				 key.setCipherVerification(TxtCryptoFactory.encrypt(TxtCryptoFactory.encrpytionAlgorithm,clearVerification,secret,iv));
				 key = fCrypto.save(key);
				 
				 sessionKeystore.update(key,fCrypto.fByEnum(fbCrypto.getClassKeyState(),JeeslIoCryptoKeyState.Code.unlocked), secret);
			}
			else
			{
				return;
			}
		}
		else
		{
			SecretKey secret = AbstractSessionKeystore.getKeyFromPassword(pwd,key.getSalt());
			IvParameterSpec iv = TxtCryptoFactory.buildIv(key.getIv());
//			String cipherText = TxtCryptoFactory.encrypt(TxtCryptoFactory.encrpytionAlgorithm,key.getMemoText(),secret,iv);
			
			sessionKeystore.update(key,fCrypto.fByEnum(fbCrypto.getClassKeyState(),JeeslIoCryptoKeyState.Code.unlocked), secret);
		}
		this.reset(false,true);
		reloadKeys();
	}
	
	public void reorderKeys() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fCrypto,keys); if(EjbIdFactory.isSaved(key)) {key = fCrypto.find(fbCrypto.getClassKey(), key);}}
}