package org.jeesl.web.mbean.prototype.io.ssi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jeesl.api.bean.JeeslTranslationBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.io.JeeslIoSsiFacade;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.builder.io.ssi.IoSsiCoreFactoryBuilder;
import org.jeesl.interfaces.model.io.revision.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiCredential;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiHost;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiSystem;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiAttribute;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiCleaning;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiData;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiLink;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiMapping;
import org.jeesl.interfaces.model.system.job.JeeslJobStatus;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.web.mbean.prototype.system.AbstractAdminBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;

public class AbstractSsiSystemBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
										SYSTEM extends JeeslIoSsiSystem<L,D>,
										CRED extends JeeslIoSsiCredential<SYSTEM>,
										MAPPING extends JeeslIoSsiMapping<SYSTEM,ENTITY>,
										ATTRIBUTE extends JeeslIoSsiAttribute<MAPPING,ENTITY>,
										DATA extends JeeslIoSsiData<MAPPING,LINK,JOB>,
										LINK extends JeeslIoSsiLink<L,D,LINK,?>,
										ENTITY extends JeeslRevisionEntity<?,?,?,?,?,?>,
										CLEANING extends JeeslIoSsiCleaning<L,D,CLEANING,?>,
										JOB extends JeeslJobStatus<L,D,JOB,?>,
										HOST extends JeeslIoSsiHost<L,D,SYSTEM>>
						extends AbstractAdminBean<L,D,LOC>
						implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractSsiSystemBean.class);
	
	private JeeslIoSsiFacade<L,D,SYSTEM,CRED,MAPPING,ATTRIBUTE,DATA,LINK,ENTITY,CLEANING,JOB,HOST> fSsi;
	
	private final IoSsiCoreFactoryBuilder<L,D,SYSTEM,CRED,HOST> fbSsiCore;
	
	private final List<SYSTEM> systems; public List<SYSTEM> getSystems() {return systems;}
	private final List<CRED> credentials; public List<CRED> getCredentials() {return credentials;}

	private SYSTEM system; public SYSTEM getSystem() {return system;} public void setSystem(SYSTEM system) {this.system = system;}
	private CRED credential; public CRED getCredential() {return credential;} public void setCredential(CRED credential) {this.credential = credential;}
	
	public AbstractSsiSystemBean(IoSsiCoreFactoryBuilder<L,D,SYSTEM,CRED,HOST> fbSsiCore)
	{
		super(fbSsiCore.getClassL(),fbSsiCore.getClassD());
		this.fbSsiCore=fbSsiCore;
		systems = new ArrayList<>();
		credentials = new ArrayList<>();
	}

	protected void postConstructSsiSystem(JeeslTranslationBean<L,D,LOC> bTranslation, JeeslFacesMessageBean bMessage,
										JeeslIoSsiFacade<L,D,SYSTEM,CRED,MAPPING,ATTRIBUTE,DATA,LINK,ENTITY,CLEANING,JOB,HOST> fSsi)
	{
		super.initJeeslAdmin(bTranslation,bMessage);
		this.fSsi=fSsi;
		reloadSystems();
	}
	
	private void reset(boolean rCredential)
	{
		if(rCredential) {credential=null;}
	}

	private void reloadSystems()
	{
		systems.clear();
		systems.addAll(fSsi.all(fbSsiCore.getClassSystem()));
	}
	
	public void selectSystem()
	{
		logger.info(AbstractLogMessage.selectEntity(system));
		system = efLang.persistMissingLangs(fSsi, localeCodes, system);
		system = efDescription.persistMissingLangs(fSsi, localeCodes, system);
		reloadCredentials();
		reset(true);
	}
	
	public void addSystem()
	{
		reset(true);
		system = fbSsiCore.ejbSystem().build();
		system.setName(efLang.createEmpty(localeCodes));
		system.setDescription(efDescription.createEmpty(localeCodes));
	}
	
	public void saveSystem() throws JeeslConstraintViolationException, JeeslLockingException
	{
		system = fSsi.save(system);
		reloadSystems();
		reloadCredentials();
	}
	
	private void reloadCredentials()
	{
		credentials.clear();
		credentials.addAll(fSsi.allForParent(fbSsiCore.getClassCredential(),system));
	}
	
	public void selectCredential()
	{
		logger.info(AbstractLogMessage.selectEntity(credential));
	}
	
	public void addCredential()
	{
		reset(true);
		credential = fbSsiCore.ejbCredential().build(system,credentials);
	}
	
	public void saveCredential() throws JeeslConstraintViolationException, JeeslLockingException
	{
		credential = fSsi.save(credential);
		reloadCredentials();
	}
	
	public void deleteCredential() throws JeeslConstraintViolationException, JeeslLockingException
	{
		fSsi.rm(credential);
		reloadCredentials();
		reset(true);
	}
}