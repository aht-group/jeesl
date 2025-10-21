package org.jeesl.api.facade.io;

import java.util.List;

import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.controller.handler.system.io.JeeslFileRepositoryStore;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.io.fr.JeeslFileContainer;
import org.jeesl.interfaces.model.io.fr.JeeslFileMeta;
import org.jeesl.interfaces.model.io.fr.JeeslFileReplication;
import org.jeesl.interfaces.model.io.fr.JeeslFileReplicationType;
import org.jeesl.interfaces.model.io.fr.JeeslFileStatus;
import org.jeesl.interfaces.model.io.fr.JeeslFileStorage;
import org.jeesl.interfaces.model.io.fr.JeeslFileStorageEngine;
import org.jeesl.interfaces.model.io.fr.JeeslFileStorageType;
import org.jeesl.interfaces.model.io.fr.JeeslFileType;
import org.jeesl.interfaces.model.io.fr.JeeslWithFileRepositoryContainer;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiSystem;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.util.query.io.JeeslIoFrQuery;
import org.jeesl.model.json.io.db.tuple.container.JsonTuples1;
import org.jeesl.model.json.io.db.tuple.container.JsonTuples2;

public interface JeeslIoFrFacade <L extends JeeslLang, D extends JeeslDescription,
								SYSTEM extends JeeslIoSsiSystem<L,D>,
								STORAGE extends JeeslFileStorage<L,D,SYSTEM,STYPE,ENGINE>,
								STYPE extends JeeslFileStorageType<L,D,STYPE,?>,
								ENGINE extends JeeslFileStorageEngine<L,D,ENGINE,?>,
								CONTAINER extends JeeslFileContainer<STORAGE,META>,
								META extends JeeslFileMeta<D,CONTAINER,FTYPE,?>,
								FTYPE extends JeeslFileType<L,D,FTYPE,?>,
								REPLICATION extends JeeslFileReplication<L,D,SYSTEM,STORAGE,RTYPE>,
								RTYPE extends JeeslFileReplicationType<L,D,RTYPE,?>,
								RSTATUS extends JeeslFileStatus<L,D,RSTATUS,?>>
		extends JeeslFacade,JeeslFileRepositoryStore<META>
{
	CONTAINER moveContainer(CONTAINER container, STORAGE destination) throws JeeslConstraintViolationException, JeeslLockingException, JeeslNotFoundException;
	JsonTuples1<STORAGE> tpsIoFileByStorage();
	JsonTuples2<STORAGE,FTYPE> tpcIoFileByStorageType();
//	Json2Tuples<STORAGE,TYPE> tpcIoFrReplicationInfoByReplicationStatus();

	List<CONTAINER> fIoFrContainer(JeeslIoFrQuery<STORAGE,STYPE,CONTAINER> query);

	<OWNER extends JeeslWithFileRepositoryContainer<CONTAINER>> List<META> fIoFrMetas(Class<OWNER> c, List<OWNER> owners);

	Long cIoFrMetas(JeeslIoFrQuery<STORAGE,STYPE,CONTAINER> query);
	List<META> fIoFrMetas(JeeslIoFrQuery<STORAGE,STYPE,CONTAINER> query);
}