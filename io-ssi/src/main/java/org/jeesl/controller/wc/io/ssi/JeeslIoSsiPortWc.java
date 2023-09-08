package org.jeesl.controller.wc.io.ssi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.io.JeeslIoSsiFacade;
import org.jeesl.controller.web.AbstractJeeslWebController;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.ejb.io.ssi.core.EjbIoSsiPortFactory;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
import org.jeesl.model.ejb.io.label.entity.IoLabelEntity;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoLocale;
import org.jeesl.model.ejb.io.ssi.core.IoSsiCredential;
import org.jeesl.model.ejb.io.ssi.core.IoSsiHost;
import org.jeesl.model.ejb.io.ssi.core.IoSsiSystem;
import org.jeesl.model.ejb.io.ssi.data.IoSsiCleaning;
import org.jeesl.model.ejb.io.ssi.data.IoSsiContext;
import org.jeesl.model.ejb.io.ssi.data.IoSsiData;
import org.jeesl.model.ejb.io.ssi.data.IoSsiMapping;
import org.jeesl.model.ejb.io.ssi.data.IoSsiStatus;
import org.jeesl.model.ejb.io.ssi.nat.IoSsiNatService;
import org.jeesl.model.ejb.io.ssi.nat.IoSsiNat;
import org.jeesl.model.ejb.system.job.core.SystemJobStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;

public class JeeslIoSsiPortWc extends AbstractJeeslWebController<IoLang,IoDescription,IoLocale>
									implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslIoSsiPortWc.class);
	
	private JeeslIoSsiFacade<IoSsiSystem,IoSsiCredential,IoSsiContext,IoSsiMapping,IoSsiData,IoSsiStatus,IoLabelEntity,IoSsiCleaning,SystemJobStatus,IoSsiHost> fSsi;
//	private final IoSsiCoreFactoryBuilder<IoLang,IoDescription,IoSsiSystem,IoSsiCredential,IoSsiHost> fbSsi;
	
	private final EjbIoSsiPortFactory<IoSsiHost,IoSsiNat> efPort;
	
	private final List<IoSsiNatService> services; public List<IoSsiNatService> getServices() {return services;}
	private final List<IoSsiNat> ports; public List<IoSsiNat> getPorts() {return ports;}
	private final List<IoSsiHost> hosts; public List<IoSsiHost> getHosts() {return hosts;}
	
	private IoSsiNat port; public IoSsiNat getPort() {return port;} public void setPort(IoSsiNat port) {this.port = port;}

	public JeeslIoSsiPortWc()
	{
		super(IoLang.class,IoDescription.class);
		
//		fbSsi = new IoSsiCoreFactoryBuilder<>(IoLang.class,IoDescription.class,IoSsiSystem.class,IoSsiCredential.class,IoSsiHost.class);
		efPort = new EjbIoSsiPortFactory<>(IoSsiNat.class);
		
//		cpArtifact = EjbMavenArtifactComparator.instance(EjbMavenArtifactComparator.Type.code);
//		cpVersion = new PositionComparator<>();

//		mapVersion = new HashMap<>();
//		mapModule = new HashMap<>();

		services = new ArrayList<>();
		ports = new ArrayList<>();
		hosts = new ArrayList<>();
	
//		versions = new ArrayList<>();
//		suitabilities = new ArrayList<>();
//		outdates = new ArrayList<>();
//		maintainers = new ArrayList<>();
	}
	
	public void postConstruct(JeeslLocaleProvider<IoLocale> lp, JeeslFacesMessageBean bMessage,
			JeeslIoSsiFacade<IoSsiSystem,IoSsiCredential,IoSsiContext,IoSsiMapping,IoSsiData,IoSsiStatus,IoLabelEntity,IoSsiCleaning,SystemJobStatus,IoSsiHost> fSsi)
	{
		super.postConstructWebController(lp,bMessage);
		this.fSsi=fSsi;

		services.addAll(fSsi.all(IoSsiNatService.class));
		hosts.addAll(fSsi.all(IoSsiHost.class));
		
//		suitabilities.addAll(fMaven.allOrderedPositionVisible(IoMavenSuitability.class));
//		outdates.addAll(fMaven.allOrderedPositionVisible(IoMavenOutdate.class));
//		maintainers.addAll(fMaven.allOrderedPositionVisible(IoMavenMaintainer.class));
		
		this.reloadPorts();
	}
	
	public void cancelPort() {this.reset(false, true);}
	private void reset(boolean rVersions, boolean rVersion)
	{
//		if(rVersions) {versions.clear();}
//		if(rVersion) {version=null;}
	}
	
	private void reloadPorts()
	{
		ports.clear();
		ports.addAll(fSsi.all(IoSsiNat.class));
	}
	
	public void selectPort()
	{
		this.reset(true, true);
		if(debugOnInfo) {logger.info(AbstractLogMessage.selectEntity(port));}
//		this.reloadVersions();
//		this.reloadUsages();
	}
	
	public void addPort()
	{
		port = efPort.build(ports);
		port.setName(efLang.buildEmpty(lp.getLocales()));
		port.setDescription(efDescription.buildEmpty(lp.getLocales()));
	}
	
	public void savePort() throws JeeslNotFoundException, JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.saveEntity(port));}
		port = fSsi.save(port);
		this.reloadPorts();
	}
	
	
	
	
//	public void reorderVersions() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fMaven,versions); this.reloadVersions();}
}