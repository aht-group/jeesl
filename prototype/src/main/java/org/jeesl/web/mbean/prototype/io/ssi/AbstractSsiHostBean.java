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
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiCredential;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiHost;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiSystem;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiAttribute;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiCleaning;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiData;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiStatus;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiContext;
import org.jeesl.interfaces.model.system.job.core.JeeslJobStatus;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.jsf.handler.PositionListReorderer;
import org.jeesl.web.mbean.prototype.system.AbstractAdminBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;

public class AbstractSsiHostBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
										SYSTEM extends JeeslIoSsiSystem<L,D>,
										CRED extends JeeslIoSsiCredential<SYSTEM>,
										MAPPING extends JeeslIoSsiContext<SYSTEM,ENTITY>,
										ATTRIBUTE extends JeeslIoSsiAttribute<MAPPING,ENTITY>,
										DATA extends JeeslIoSsiData<MAPPING,LINK,?,JOB>,
										LINK extends JeeslIoSsiStatus<L,D,LINK,?>,
										ENTITY extends JeeslRevisionEntity<?,?,?,?,?,?>,
										CLEANING extends JeeslIoSsiCleaning<L,D,CLEANING,?>,
										JOB extends JeeslJobStatus<L,D,JOB,?>,
										HOST extends JeeslIoSsiHost<L,D,SYSTEM>>
						extends AbstractAdminBean<L,D,LOC>
						implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractSsiHostBean.class);
	
	private JeeslIoSsiFacade<SYSTEM,CRED,MAPPING,ATTRIBUTE,DATA,LINK,?,ENTITY,CLEANING,JOB,HOST> fSsi;
	
	private final IoSsiCoreFactoryBuilder<L,D,SYSTEM,CRED,HOST> fbSsiCore;
	
	private final List<SYSTEM> systems; public List<SYSTEM> getSystems() {return systems;}
	private final List<HOST> hosts; public List<HOST> getHosts() {return hosts;}

	private HOST host; public HOST getHost() {return host;} public void setHost(HOST host) {this.host = host;}
	
	public AbstractSsiHostBean(IoSsiCoreFactoryBuilder<L,D,SYSTEM,CRED,HOST> fbSsiCore)
	{
		super(fbSsiCore.getClassL(),fbSsiCore.getClassD());
		this.fbSsiCore=fbSsiCore;
		systems = new ArrayList<>();
		hosts = new ArrayList<>();
	}

	protected void postConstructSsiHost(JeeslTranslationBean<L,D,LOC> bTranslation, JeeslFacesMessageBean bMessage,
										JeeslIoSsiFacade<SYSTEM,CRED,MAPPING,ATTRIBUTE,DATA,LINK,?,ENTITY,CLEANING,JOB,HOST> fSsi)
	{
		super.initJeeslAdmin(bTranslation,bMessage);
		this.fSsi=fSsi;
		systems.addAll(fSsi.all(fbSsiCore.getClassSystem()));
		reloadHosts();
	}
	
	private void reset(boolean rHost)
	{
		if(rHost) {host=null;}
	}

	private void reloadHosts()
	{
		hosts.clear();
		for(HOST h : fSsi.allOrderedPosition(fbSsiCore.getClassHost()))
		{
			hosts.add(h);
		}
	}
	
	public void selectHost()
	{
		logger.info(AbstractLogMessage.selectEntity(host));
		host = efLang.persistMissingLangs(fSsi, localeCodes, host);
		host = efDescription.persistMissingLangs(fSsi, localeCodes, host);
		reset(false);
	}
	
	public void addHost()
	{
		reset(true);
		host = fbSsiCore.ejbHost().build(systems.get(0),"",hosts);
		host.setName(efLang.createEmpty(localeCodes));
		host.setDescription(efDescription.createEmpty(localeCodes));
	}
	
	public void saveHost() throws JeeslConstraintViolationException, JeeslLockingException
	{
		logger.info(AbstractLogMessage.saveEntity(host)+" "+host.getSystem().getCode());
		fbSsiCore.ejbHost().converter(fSsi,host);
		host = fSsi.save(host);
		reloadHosts();
	}
	
	public void reorderHosts() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fSsi,hosts);}
}