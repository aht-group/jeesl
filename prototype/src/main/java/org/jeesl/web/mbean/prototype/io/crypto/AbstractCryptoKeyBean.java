package org.jeesl.web.mbean.prototype.io.crypto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jeesl.api.bean.JeeslTranslationBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.io.JeeslIoCryptoFacade;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.io.IoCryptoFactoryBuilder;
import org.jeesl.interfaces.model.io.crypto.JeeslIoCryptoKey;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;
import org.jeesl.web.mbean.prototype.system.AbstractAdminBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;

public abstract class AbstractCryptoKeyBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
											KEY extends JeeslIoCryptoKey<USER>,
											USER extends JeeslSimpleUser>
					extends AbstractAdminBean<L,D,LOC>
					implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractCryptoKeyBean.class);

	protected JeeslIoCryptoFacade<L,D,KEY,USER> fCrypto;
	protected final IoCryptoFactoryBuilder<L,D,KEY,USER> fbCrypto;
	
	private final List<KEY> keys; public List<KEY> getKeys() {return keys;}

	private USER user;
	private KEY key; public KEY getKey() {return key;} public void setKey(KEY key) {this.key = key;}

	public AbstractCryptoKeyBean(final IoCryptoFactoryBuilder<L,D,KEY,USER> fbCrypto)
	{
		super(fbCrypto.getClassL(),fbCrypto.getClassD());
		this.fbCrypto=fbCrypto;

		keys = new ArrayList<>();
		
	}

	protected void postConstructCryptoKey(JeeslTranslationBean<L,D,LOC> bTranslation, JeeslFacesMessageBean bMessage,JeeslIoCryptoFacade<L,D,KEY,USER> fCrypto,
										USER user)
	{
		super.initJeeslAdmin(bTranslation,bMessage);
		this.fCrypto=fCrypto;
		this.user=user;

		reloadKeys();
		
	}

	private void reloadKeys()
	{
		keys.clear();
		keys.addAll(fCrypto.all(fbCrypto.getClassKey()));
	}

	public void addKey() throws JeeslNotFoundException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.addEntity(fbCrypto.getClassKey()));}
		key = fbCrypto.ejbKEy().build(user);
//		attribute.setName(efLang.createEmpty(langs));
//		attribute.setDescription(efDescription.createEmpty(langs));
	}

	public void selectKey() throws JeeslNotFoundException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.selectEntity(key));}
//		attribute = fRevision.find(fbRevision.getClassAttribute(), attribute);
//		attribute = efLang.persistMissingLangs(fRevision,langs,attribute);
//		attribute = efDescription.persistMissingLangs(fRevision,langs,attribute);
	}
	
	public void saveKey() throws JeeslNotFoundException, JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.selectEntity(key));}
		fbCrypto.ejbKEy().converter(fCrypto,key);
		key = fCrypto.save(key);
		reloadKeys();
//		attribute = efLang.persistMissingLangs(fRevision,langs,attribute);
//		attribute = efDescription.persistMissingLangs(fRevision,langs,attribute);
	}

	public void cancelAttribute()
	{
//		attribute=null;
	}
}