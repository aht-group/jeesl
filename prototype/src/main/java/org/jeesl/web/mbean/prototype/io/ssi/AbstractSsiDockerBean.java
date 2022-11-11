package org.jeesl.web.mbean.prototype.io.ssi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jeesl.api.facade.io.JeeslIoSsiDockerFacade;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.builder.io.ssi.IoSsiDockerFactoryBuilder;
import org.jeesl.interfaces.bean.sb.bean.SbToggleBean;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiHost;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiSystem;
import org.jeesl.interfaces.model.io.ssi.docker.JeeslIoDockerContainer;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.jsf.handler.sb.SbMultiHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;

public class AbstractSsiDockerBean <L extends JeeslLang,D extends JeeslDescription,
										SYSTEM extends JeeslIoSsiSystem<L,D>,
										INSTANCE extends JeeslIoDockerContainer<SYSTEM,HOST>,
										HOST extends JeeslIoSsiHost<L,D,SYSTEM>>
						implements Serializable,SbToggleBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractSsiDockerBean.class);
	
	private final IoSsiDockerFactoryBuilder<L,D,SYSTEM,INSTANCE,HOST> fbSsi;
	
	private JeeslIoSsiDockerFacade<L,D,SYSTEM> fSsi;
	
	private final SbMultiHandler<SYSTEM> sbhSystem; public SbMultiHandler<SYSTEM> getSbhSystem() {return sbhSystem;}
	
	private final List<HOST> hosts; public List<HOST> getHosts() {return hosts;}

	private final List<INSTANCE> instances; public List<INSTANCE> getInstances() {return instances;}

	
	private INSTANCE instance; public INSTANCE getInstance() {return instance;} public void setInstance(INSTANCE instance) {this.instance = instance;}

	public AbstractSsiDockerBean(final IoSsiDockerFactoryBuilder<L,D,SYSTEM,INSTANCE,HOST> fbSsi)
	{
		this.fbSsi=fbSsi;
		
		sbhSystem = new SbMultiHandler<>(fbSsi.getClassSystem(),this);

		instances = new ArrayList<>();
		hosts = new ArrayList<>();
	}

	public void postConstructSsiDocker(JeeslIoSsiDockerFacade<L,D,SYSTEM> fSsi)
	{
		this.fSsi=fSsi;
		hosts.addAll(fSsi.all(fbSsi.getClassHost()));
		postConstructInitSystems();
	}
	protected void postConstructInitSystems()
	{
		sbhSystem.setList(fSsi.all(fbSsi.getClassSystem()));
		sbhSystem.selectAll();
	}
	
	@Override
	public void toggled(Class<?> c) throws JeeslLockingException, JeeslConstraintViolationException
	{
		reset(true);
		reload();
		
	}
	
	public void cancelInstance() {reset(true);}
	private void reset(boolean rInstance)
	{
		if(rInstance) {instance=null;}
	}

	private void reload()
	{
		instances.clear();
		
	}
	

	
	public void selectInstance()
	{
		logger.info(AbstractLogMessage.selectEntity(instance));
	}
	
	public void addInstance()
	{
		logger.info(AbstractLogMessage.createEntity(fbSsi.getClassInstance()));
		instance = fbSsi.ejbInstance().build(null);
	}
}