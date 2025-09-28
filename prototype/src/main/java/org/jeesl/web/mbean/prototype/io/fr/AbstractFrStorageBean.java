package org.jeesl.web.mbean.prototype.io.fr;

import java.util.List;

import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.io.JeeslIoFrFacade;
import org.jeesl.controller.handler.system.io.fr.JeeslFileTypeHandler;
import org.jeesl.controller.handler.tuple.JsonTuple1Handler;
import org.jeesl.controller.handler.tuple.JsonTuple2Handler;
import org.jeesl.controller.web.util.AbstractLogMessage;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.builder.io.IoFileRepositoryFactoryBuilder;
import org.jeesl.interfaces.bean.sb.bean.SbToggleBean;
import org.jeesl.interfaces.bean.sb.handler.SbToggleSelection;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
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
import org.jeesl.jsf.handler.PositionListReorderer;
import org.jeesl.jsf.handler.sb.SbMultiHandler;
import org.jeesl.web.mbean.prototype.system.AbstractAdminBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractFrStorageBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
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
						implements SbToggleBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractFrStorageBean.class);
	
	private JeeslIoFrFacade<L,D,SYSTEM,STORAGE,STYPE,ENGINE,CONTAINER,META,FTYPE,REPLICATION,RTYPE,RSTATUS> fFr;
	private final IoFileRepositoryFactoryBuilder<L,D,LOC,SYSTEM,STORAGE,STYPE,ENGINE,CONTAINER,META,FTYPE,REPLICATION,RTYPE,RSTATUS> fbFr;
	
	protected final SbMultiHandler<STYPE> sbhStorageType; public SbMultiHandler<STYPE> getSbhStorageType() {return sbhStorageType;}
	private final JsonTuple1Handler<STORAGE> thSize; public JsonTuple1Handler<STORAGE> getThSize() {return thSize;}
	private final JsonTuple2Handler<STORAGE,FTYPE> thCount; public JsonTuple2Handler<STORAGE,FTYPE> getThCount() {return thCount;}

	private JeeslFileTypeHandler<META,FTYPE> fth;
	
	private List<STORAGE> storages; public List<STORAGE> getStorages() {return storages;}
	private List<ENGINE> engines; public List<ENGINE> getEngines() {return engines;}
	
	private STORAGE storage; public STORAGE getStorage() {return storage;} public void setStorage(STORAGE storage) {this.storage = storage;}
	private FTYPE typeUnknown;public FTYPE getTypeUnknown() {return typeUnknown;}

	protected AbstractFrStorageBean(IoFileRepositoryFactoryBuilder<L,D,LOC,SYSTEM,STORAGE,STYPE,ENGINE,CONTAINER,META,FTYPE,REPLICATION,RTYPE,RSTATUS> fbFr,
									JeeslComparatorProvider<FTYPE> jcpB)
	{
		super(fbFr.getClassL(),fbFr.getClassD());
		this.fbFr=fbFr;
		sbhStorageType = new SbMultiHandler<>(fbFr.getClassStorageType(),this);
		thSize = new JsonTuple1Handler<STORAGE>(fbFr.getClassStorage());
		thCount = new JsonTuple2Handler<>(fbFr.getClassStorage(),fbFr.getClassType());
		thCount.setComparatorProviderB(jcpB);
	}
	
	protected void postConstructFrStorage(JeeslLocaleProvider<LOC> lp, JeeslFacesMessageBean bMessage,
											JeeslIoFrFacade<L,D,SYSTEM,STORAGE,STYPE,ENGINE,CONTAINER,META,FTYPE,REPLICATION,RTYPE,RSTATUS> fFr)
	{
		super.initJeeslAdmin(lp,bMessage);
		this.fFr=fFr;
		fth = new JeeslFileTypeHandler<>(fbFr,fFr);
		typeUnknown = fFr.fByEnum(fbFr.getClassType(), JeeslFileType.Code.unknown);
		initSbh();
		reloadStorages();
		engines = fFr.allOrderedPositionVisible(fbFr.getClassEngine());
		thCount.load(fFr.tpcIoFileByStorageType());
		thSize.init(fFr.tpsIoFileByStorage());
	}
	
	protected void initSbh()
	{
		sbhStorageType.setList(fFr.allOrderedPositionVisible(fbFr.getClassStorageType()));
		sbhStorageType.toggleAll();
	}
	
	public void resetStorage() {reset(true);}
	private void reset(boolean rStorage)
	{
		if(rStorage) {storage=null;}
	}
	
	@Override
	public void toggled(SbToggleSelection handler, Class<?> c) throws JeeslLockingException, JeeslConstraintViolationException
	{
		reloadStorages();
	}
	
	private void reloadStorages()
	{
		storages = fFr.allOrderedPosition(fbFr.getClassStorage());
	}
	
	public void addStorage()
	{
		reset(true);
		STYPE type = null; if(!sbhStorageType.getList().isEmpty()) {type = sbhStorageType.getList().get(0);}
		storage = fbFr.ejbStorage().build(type);
		storage.setName(efLang.build(lp));
		storage.setDescription(efDescription.build(lp));
	}
	
	public void saveStorage() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.saveEntity(storage));}
		storage.setEngine(fFr.find(fbFr.getClassEngine(),storage.getEngine()));
		fbFr.ejbStorage().converter(fFr,storage);
		storage = fFr.save(storage);
		reloadStorages();
	}
	
	public void deleteStorage() throws JeeslConstraintViolationException
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.deleteEntity(storage));}
		fFr.rm(storage);
		reloadStorages();
		reset(true);
	}
	
	public void selectStorage()
	{
		storage = fFr.find(fbFr.getClassStorage(), storage);
		storage = efLang.persistMissingLangs(fFr, lp, storage);
		storage = efDescription.persistMissingLangs(fFr, lp, storage);
		if(debugOnInfo) {logger.info(AbstractLogMessage.selectEntity(storage));}
		reset(false);
	}
	
	public void fixType()
	{	
		if(typeUnknown!=null && fth!=null)
		{
			int i=0;
			for(META meta : fFr.allForType(fbFr.getClassMeta(), typeUnknown))
			{
				String code = meta.getType().getCode();
				fth.updateType(meta);
				if(!code.contentEquals(meta.getType().getCode()))
				{
					i++;
					try {fFr.save(meta);}
					catch (JeeslConstraintViolationException | JeeslLockingException e) {e.printStackTrace();}
				}
				if(i==250)
				{
					logger.info("Breaking loop to prevent timeout");
					break;
				}
			}
		}
		thCount.load(fFr.tpcIoFileByStorageType());
	}
	
	public void reorderStorages() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fFr, storages);}
}