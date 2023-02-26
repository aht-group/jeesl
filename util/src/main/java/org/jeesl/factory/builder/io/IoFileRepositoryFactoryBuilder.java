package org.jeesl.factory.builder.io;

import org.jeesl.api.bean.callback.JeeslFileRepositoryCallback;
import org.jeesl.api.facade.io.JeeslIoFrFacade;
import org.jeesl.controller.handler.system.io.fr.DefaultFileRepositoryHandler;
import org.jeesl.controller.handler.system.io.fr.JeeslFileStatusHandler;
import org.jeesl.factory.builder.AbstractFactoryBuilder;
import org.jeesl.factory.ejb.io.fr.EjbIoFrContainerFactory;
import org.jeesl.factory.ejb.io.fr.EjbIoFrMetaFactory;
import org.jeesl.factory.ejb.io.fr.EjbIoFrReplicationFactory;
import org.jeesl.factory.ejb.io.fr.EjbIoFrStorageFactory;
import org.jeesl.interfaces.model.io.fr.JeeslFileContainer;
import org.jeesl.interfaces.model.io.fr.JeeslFileMeta;
import org.jeesl.interfaces.model.io.fr.JeeslFileReplication;
import org.jeesl.interfaces.model.io.fr.JeeslFileReplicationType;
import org.jeesl.interfaces.model.io.fr.JeeslFileStatus;
import org.jeesl.interfaces.model.io.fr.JeeslFileStorage;
import org.jeesl.interfaces.model.io.fr.JeeslFileStorageEngine;
import org.jeesl.interfaces.model.io.fr.JeeslFileStorageType;
import org.jeesl.interfaces.model.io.fr.JeeslFileType;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiSystem;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IoFileRepositoryFactoryBuilder<L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
											SYSTEM extends JeeslIoSsiSystem<L,D>,
											STORAGE extends JeeslFileStorage<L,D,SYSTEM,STYPE,SENGINE>,
											STYPE extends JeeslFileStorageType<L,D,STYPE,?>,
											SENGINE extends JeeslFileStorageEngine<L,D,SENGINE,?>,
											CONTAINER extends JeeslFileContainer<STORAGE,META>,
											META extends JeeslFileMeta<D,CONTAINER,TYPE,RSTATUS>,
											TYPE extends JeeslFileType<L,D,TYPE,?>,
											REPLICATION extends JeeslFileReplication<L,D,SYSTEM,STORAGE,RTYPE>,
											RTYPE extends JeeslFileReplicationType<L,D,RTYPE,?>,
											RSTATUS extends JeeslFileStatus<L,D,RSTATUS,?>>
				extends AbstractFactoryBuilder<L,D>
{
	final static Logger logger = LoggerFactory.getLogger(IoFileRepositoryFactoryBuilder.class);

	private final Class<STORAGE> cStorage; public Class<STORAGE> getClassStorage() {return cStorage;}
	private final Class<STYPE> cStorageType; public Class<STYPE> getClassStorageType() {return cStorageType;}
	private final Class<SENGINE> cEngine; public Class<SENGINE> getClassEngine() {return cEngine;}
	private final Class<CONTAINER> cContainer; public Class<CONTAINER> getClassContainer() {return cContainer;}
	private final Class<META> cMeta; public Class<META> getClassMeta() {return cMeta;}
	private final Class<TYPE> cType; public Class<TYPE> getClassType() {return cType;}
	private final Class<REPLICATION> cReplication; public Class<REPLICATION> getClassReplication() {return cReplication;}
	private final Class<RTYPE> cRtype; public Class<RTYPE> getClassReplicationType() {return cRtype;}
	private final Class<RSTATUS> cStatus; public Class<RSTATUS> getClassStatus() {return cStatus;}
	
	public IoFileRepositoryFactoryBuilder(final Class<L> cL, final Class<D> cD,
								final Class<STORAGE> cStorage,
								final Class<STYPE> cStorageType,
								final Class<SENGINE> cEngine,
								final Class<CONTAINER> cContainer, final Class<META> cMeta,
								final Class<TYPE> cType,
								final Class<REPLICATION> cReplication,
								final Class<RTYPE> cRtype,
								final Class<RSTATUS> cStatus)
	{
		super(cL,cD);
		this.cStorage=cStorage;
		this.cStorageType=cStorageType;
		this.cEngine=cEngine;
		this.cContainer=cContainer;
		this.cMeta=cMeta;
		this.cType=cType;
		this.cReplication=cReplication;
		this.cRtype=cRtype;
		this.cStatus=cStatus;
	}
	
	public EjbIoFrStorageFactory<SYSTEM,STORAGE,STYPE,SENGINE> ejbStorage() {return new EjbIoFrStorageFactory<>(this);}
	public EjbIoFrContainerFactory<STORAGE,CONTAINER> ejbContainer() {return new EjbIoFrContainerFactory<>(cContainer);}
	public EjbIoFrMetaFactory<CONTAINER,META,TYPE> ejbMeta() {return new EjbIoFrMetaFactory<>(cMeta);}
	public EjbIoFrReplicationFactory<REPLICATION,RTYPE> ejbReplication() {return new EjbIoFrReplicationFactory<>(this);}
	
	public DefaultFileRepositoryHandler<L,D,LOC,SYSTEM,STORAGE,STYPE,SENGINE,CONTAINER,META,TYPE,REPLICATION,RTYPE,RSTATUS> handler(JeeslIoFrFacade<L,D,SYSTEM,STORAGE,STYPE,SENGINE,CONTAINER,META,TYPE,REPLICATION,RTYPE,RSTATUS> fFr, JeeslFileRepositoryCallback callback)
	{
		return new DefaultFileRepositoryHandler<>(fFr,this,callback);
	}
	
	public JeeslFileStatusHandler<META,RSTATUS> handlerStatus(JeeslIoFrFacade<L,D,SYSTEM,STORAGE,STYPE,SENGINE,CONTAINER,META,TYPE,REPLICATION,RTYPE,RSTATUS> fFr){return new JeeslFileStatusHandler<META,RSTATUS>(fFr,this);}
}