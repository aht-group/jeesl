package org.jeesl.controller.web.g.io.fr;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.io.JeeslIoFrFacade;
import org.jeesl.controller.web.AbstractJeeslLocaleWebController;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.builder.io.IoFileRepositoryFactoryBuilder;
import org.jeesl.factory.ejb.io.fr.EjbIoFrReplicationFactory;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
import org.jeesl.interfaces.controller.web.io.fr.JeeslIoFrReplicationCallback;
import org.jeesl.interfaces.model.io.fr.JeeslFileReplication;
import org.jeesl.interfaces.model.io.fr.JeeslFileReplicationType;
import org.jeesl.interfaces.model.io.fr.JeeslFileStatus;
import org.jeesl.interfaces.model.io.fr.JeeslFileStorage;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiSystem;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslFrReplicationGwc <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
									SYSTEM extends JeeslIoSsiSystem<L,D>,
									STORAGE extends JeeslFileStorage<L,D,SYSTEM,?,?>,
									REPLICATION extends JeeslFileReplication<L,D,SYSTEM,STORAGE,RTYPE>,
									RTYPE extends JeeslFileReplicationType<L,D,RTYPE,?>,
									RSTATUS extends JeeslFileStatus<L,D,RSTATUS,?>
>
					extends AbstractJeeslLocaleWebController<L,D,LOC>
					implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslFrReplicationGwc.class);

	private JeeslIoFrFacade<SYSTEM,STORAGE,?,?,?,?,?,REPLICATION,RTYPE,RSTATUS> fFr;

	private IoFileRepositoryFactoryBuilder<L,D,LOC,SYSTEM,STORAGE,?,?,?,?,?,REPLICATION,RTYPE,RSTATUS> fbFr;

	private final EjbIoFrReplicationFactory<REPLICATION,RTYPE> efReplication;
	
	private final List<STORAGE> storages; public List<STORAGE> getStorages() {return storages;}
	private final List<REPLICATION> replications; public List<REPLICATION> getReplications() {return replications;}

	private REPLICATION replication; public REPLICATION getReplication() {return replication;} public void setReplication(REPLICATION replication) {this.replication = replication;}

	public JeeslFrReplicationGwc(JeeslIoFrReplicationCallback callback, IoFileRepositoryFactoryBuilder<L,D,LOC,SYSTEM,STORAGE,?,?,?,?,?,REPLICATION,RTYPE,RSTATUS> fbFr)
	{
		super(fbFr.getClassL(),fbFr.getClassD());
		this.fbFr=fbFr;
		
		efReplication = fbFr.ejbReplication();
		
		storages = new ArrayList<>();
		replications = new ArrayList<>();
	}

	public void postConstruct(JeeslLocaleProvider<LOC> lp, JeeslFacesMessageBean bMessage, JeeslIoFrFacade<SYSTEM,STORAGE,?,?,?,?,?,REPLICATION,RTYPE,RSTATUS> fFr)
	{
		super.postConstructLocaleWebController(lp,bMessage);
		this.fFr = fFr;
		
		storages.addAll(fFr.allOrderedPosition(fbFr.getClassStorage()));
		
		this.reloadReplications();
	}
	
	private void reloadReplications()
	{
		replications.clear();
		replications.addAll(fFr.all(fbFr.getClassReplication()));
	}
	
	public void addReplication()
	{
		RTYPE type = fFr.fByEnum(fbFr.getClassReplicationType(), JeeslFileReplicationType.Code.manual);
		replication = efReplication.build(type);
		replication.setStorageSrc(storages.get(0));
		replication.setStorageDst(storages.get(storages.size()-9));
	}
	
	public void saveReplication() throws JeeslConstraintViolationException, JeeslLockingException
	{
		replication = fFr.save(replication);
		this.reloadReplications();
	}
}