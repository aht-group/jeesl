package org.jeesl.web.mbean.prototype.io.fr;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jeesl.api.bean.JeeslTranslationBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.io.JeeslIoFrFacade;
import org.jeesl.controller.handler.tuple.JsonTuple2Handler;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.builder.io.IoFileRepositoryFactoryBuilder;
import org.jeesl.interfaces.controller.report.JeeslComparatorProvider;
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
import org.jeesl.util.db.cache.EjbCodeCache;
import org.jeesl.web.mbean.prototype.system.AbstractAdminBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;

public class AbstractFrReplicationBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
									SYSTEM extends JeeslIoSsiSystem<L,D>,
									STORAGE extends JeeslFileStorage<L,D,SYSTEM,STYPE,ENGINE>,
									STYPE extends JeeslFileStorageType<L,D,STYPE,?>,
									ENGINE extends JeeslFileStorageEngine<L,D,ENGINE,?>,
									CONTAINER extends JeeslFileContainer<STORAGE,META>,
									META extends JeeslFileMeta<D,CONTAINER,FTYPE,RSTATUS>,
									FTYPE extends JeeslFileType<L,D,FTYPE,?>,
									REPLICATION extends JeeslFileReplication<L,D,SYSTEM,STORAGE,RTYPE>,
									RTYPE extends JeeslFileReplicationType<L,D,RTYPE,?>,
									RSTATUS extends JeeslFileStatus<L,D,RSTATUS,?>>
						extends AbstractAdminBean<L,D,LOC>
						implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractFrReplicationBean.class);
	
	private JeeslIoFrFacade<L,D,SYSTEM,STORAGE,STYPE,ENGINE,CONTAINER,META,FTYPE,REPLICATION,RTYPE,RSTATUS> fFr;
	private final IoFileRepositoryFactoryBuilder<L,D,LOC,SYSTEM,STORAGE,STYPE,ENGINE,CONTAINER,META,FTYPE,REPLICATION,RTYPE,RSTATUS> fbFr;
	
	private final JsonTuple2Handler<REPLICATION,RSTATUS> thCount; public JsonTuple2Handler<REPLICATION,RSTATUS> getThCount() {return thCount;}
	
	private final List<STORAGE> srcStorages; public List<STORAGE> getSrcStorages() {return srcStorages;}
	private final List<STORAGE> dstStorages; public List<STORAGE> getDstStorages() {return dstStorages;}
	private final List<RTYPE> types; public List<RTYPE> getTypes() {return types;}
	private final List<REPLICATION> replications; public List<REPLICATION> getReplications() {return replications;}
	
	private REPLICATION replication;  public REPLICATION getReplication() {return replication;} public void setReplication(REPLICATION replication) {this.replication = replication;}

	protected AbstractFrReplicationBean(IoFileRepositoryFactoryBuilder<L,D,LOC,SYSTEM,STORAGE,STYPE,ENGINE,CONTAINER,META,FTYPE,REPLICATION,RTYPE,RSTATUS> fbFr,
									JeeslComparatorProvider<FTYPE> jcpB)
	{
		super(fbFr.getClassL(),fbFr.getClassD());
		this.fbFr=fbFr;
		
		thCount = new JsonTuple2Handler<REPLICATION,RSTATUS>(fbFr.getClassReplication(),fbFr.getClassStatus());
//		thCount.setComparatorProviderB(jcpB);
		
		srcStorages = new ArrayList<>();
		dstStorages = new ArrayList<>();
		types = new ArrayList<>();
		replications = new ArrayList<>();
	}
	
	protected void postConstructFrReplication(JeeslTranslationBean<L,D,LOC> bTranslation, JeeslFacesMessageBean bMessage,
											JeeslIoFrFacade<L,D,SYSTEM,STORAGE,STYPE,ENGINE,CONTAINER,META,FTYPE,REPLICATION,RTYPE,RSTATUS> fFr)
	{
		super.initJeeslAdmin(bTranslation,bMessage);
		this.fFr=fFr;
		
		types.addAll(fFr.allOrderedPosition(fbFr.getClassReplicationType()));
		reloadStorages();
		reloadReplications();
	}
	
	private void reloadStorages()
	{
		EjbCodeCache<STYPE> cacheType = EjbCodeCache.instance(fbFr.getClassStorageType()).facade(fFr);
		for(STORAGE s : fFr.allOrderedPosition(fbFr.getClassStorage()))
		{
			if(s.getType().equals(cacheType.ejb(JeeslFileStorageType.Code.primary)))
			{
				srcStorages.add(s);
			}
			else if(s.getType().equals(cacheType.ejb(JeeslFileStorageType.Code.replica)))
			{
				srcStorages.add(s);
				dstStorages.add(s);
			}
			else if(s.getType().equals(cacheType.ejb(JeeslFileStorageType.Code.cache)))
			{
				dstStorages.add(s);
			}
		}
	}
	
	public void resetReplication() {reset(true);}
	private void reset(boolean rReplication)
	{
		if(rReplication) {replication=null;}
	}
	
	
	private void reloadReplications()
	{
		replications.clear();
		replications.addAll(fFr.all(fbFr.getClassReplication()));
	}
	
	public void selectReplication()
	{
		logger.info(AbstractLogMessage.selectEntity(replication));
		reset(false);
		replication = fFr.find(fbFr.getClassReplication(), replication);
//		storage = efLang.persistMissingLangs(fFr, localeCodes, storage);
//		storage = efDescription.persistMissingLangs(fFr, localeCodes, storage);
	}
	
	public void addReplication()
	{
		reset(true);
		
		replication = fbFr.ejbReplication().build(fFr.fByEnum(fbFr.getClassReplicationType(),JeeslFileReplicationType.Code.manual));
//		storage.setName(efLang.createEmpty(localeCodes));
//		storage.setDescription(efDescription.createEmpty(localeCodes));
	}
	
	public void saveReplication() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.saveEntity(replication));}
		fbFr.ejbReplication().converter(fFr,replication);
		replication = fFr.save(replication);
		reloadReplications();
	}
	
	public void deleteReplication() throws JeeslConstraintViolationException
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.rmEntity(replication));}
		fFr.rm(replication);
		reloadReplications();
		reset(true);
	}	
}