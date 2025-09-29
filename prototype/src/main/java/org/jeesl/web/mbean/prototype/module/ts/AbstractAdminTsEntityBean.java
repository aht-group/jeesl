package org.jeesl.web.mbean.prototype.module.ts;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import org.jeesl.api.bean.JeeslTranslationBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.module.JeeslTsFacade;
import org.jeesl.controller.web.util.AbstractLogMessage;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.module.TsFactoryBuilder;
import org.jeesl.interfaces.bean.sb.handler.SbToggleSelection;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.module.ts.config.JeeslTsCategory;
import org.jeesl.interfaces.model.module.ts.config.JeeslTsDataSource2;
import org.jeesl.interfaces.model.module.ts.config.JeeslTsInterval;
import org.jeesl.interfaces.model.module.ts.core.JeeslTimeSeries;
import org.jeesl.interfaces.model.module.ts.core.JeeslTsEntityClass;
import org.jeesl.interfaces.model.module.ts.core.JeeslTsMultiPoint;
import org.jeesl.interfaces.model.module.ts.core.JeeslTsScope;
import org.jeesl.interfaces.model.module.ts.core.JeeslTsScopeType;
import org.jeesl.interfaces.model.module.ts.data.JeeslTsBridge;
import org.jeesl.interfaces.model.module.ts.data.JeeslTsData;
import org.jeesl.interfaces.model.module.ts.data.JeeslTsDataPoint;
import org.jeesl.interfaces.model.module.ts.data.JeeslTsSample;
import org.jeesl.interfaces.model.module.ts.data.JeeslTsTransaction;
import org.jeesl.interfaces.model.module.ts.stat.JeeslTsCron;
import org.jeesl.interfaces.model.module.ts.stat.JeeslTsStatistic;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.web.JeeslJsfSecurityHandler;
import org.jeesl.jsf.handler.PositionListReorderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractAdminTsEntityBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
											CAT extends JeeslTsCategory<L,D,CAT,?>,
											SCOPE extends JeeslTsScope<L,D,CAT,ST,UNIT,EC,INT>,
											ST extends JeeslTsScopeType<L,D,ST,?>,
											UNIT extends JeeslStatus<L,D,UNIT>,
											MP extends JeeslTsMultiPoint<L,D,SCOPE,UNIT,?>,
											TS extends JeeslTimeSeries<SCOPE,TS,BRIDGE,INT,STAT>,
											TRANSACTION extends JeeslTsTransaction<SRC,DATA,USER,?>,
											SRC extends JeeslTsDataSource2<L,D>, 
											BRIDGE extends JeeslTsBridge<EC>,
											EC extends JeeslTsEntityClass<L,D,CAT,ENTITY>,
											ENTITY extends JeeslRevisionEntity<L,D,?,?,?,?>,
											INT extends JeeslTsInterval<L,D,INT,?>,
											STAT extends JeeslTsStatistic<L,D,STAT,?>,
											DATA extends JeeslTsData<TS,TRANSACTION,SAMPLE,POINT,WS>,
											POINT extends JeeslTsDataPoint<DATA,MP>,
											SAMPLE extends JeeslTsSample, 
											USER extends EjbWithId, 
											WS extends JeeslStatus<L,D,WS>,
											QAF extends JeeslStatus<L,D,QAF>,
											CRON extends JeeslTsCron<SCOPE,INT,STAT>>
					extends AbstractAdminTsBean<L,D,LOC,CAT,SCOPE,ST,UNIT,MP,TS,TRANSACTION,SRC,BRIDGE,EC,ENTITY,INT,STAT,DATA,POINT,SAMPLE,USER,WS,QAF,CRON>
					implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractAdminTsEntityBean.class);
			
	protected List<EC> classes; public List<EC> getClasses() {return classes;}
	
	protected EC entity; public void setEntity(EC entityClass) {this.entity = entityClass;} public EC getEntity() {return entity;}

	public AbstractAdminTsEntityBean(final TsFactoryBuilder<L,D,LOC,CAT,SCOPE,ST,UNIT,MP,TS,TRANSACTION,SRC,BRIDGE,EC,ENTITY,INT,STAT,DATA,POINT,SAMPLE,USER,WS,QAF,CRON> fbTs)
	{
		super(fbTs);
	}
	
	protected void postConstructEntity(JeeslTranslationBean<L,D,LOC> bTranslation, JeeslTsFacade<CAT,SCOPE,ST,UNIT,MP,TS,TRANSACTION,SRC,BRIDGE,EC,ENTITY,INT,STAT,DATA,POINT,SAMPLE,USER,WS,CRON> fTs, JeeslFacesMessageBean bMessage)
	{
		super.postConstructTs(bTranslation,bMessage,fTs);
		reloadClasses();
	}
	
	@Override public void toggled(SbToggleSelection handler, Class<?> c) throws JeeslLockingException, JeeslConstraintViolationException
	{
		super.toggled(handler,c);
		if(fbTs.getClassCategory().isAssignableFrom(c)){reloadClasses();cancel();}
	}
	
	public void reloadClasses()
	{
		classes = fTs.findClasses(fbTs.getClassEntity(), fbTs.getClassCategory(), sbhCategory.getSelected(), uiShowInvisible);
		logger.info(AbstractLogMessage.reloaded(fbTs.getClassEntity(), classes));
		Collections.sort(classes, comparatorClass);
	}
	
	public void add() throws JeeslNotFoundException
	{
		logger.info(AbstractLogMessage.createEntity(fbTs.getClassEntity()));
		entity = fbTs.ejbEntityClass().build(null);
		entity.setName(efLang.createEmpty(localeCodes));
		entity.setDescription(efDescription.createEmpty(localeCodes));
	}
	
	public void select() throws JeeslNotFoundException
	{
		logger.info(AbstractLogMessage.selectEntity(entity));
		entity = fTs.find(fbTs.getClassEntity(), entity);
		entity = efLang.persistMissingLangs(fTs,localeCodes,entity);
		entity = efDescription.persistMissingLangs(fTs,localeCodes,entity);
	}
	
	public void save() throws JeeslConstraintViolationException, JeeslLockingException, JeeslNotFoundException
	{
		logger.info(AbstractLogMessage.saveEntity(entity));
		entity.setCategory(fTs.find(fbTs.getClassCategory(), entity.getCategory()));
		entity = fTs.save(entity);
		reloadClasses();
		updatePerformed();
	}
	
	public void rm() throws JeeslConstraintViolationException, JeeslLockingException, JeeslNotFoundException
	{
		logger.info(AbstractLogMessage.deleteEntity(entity));
		fTs.rm(entity);
		entity=null;
		reloadClasses();
	}
	
	public void cancel()
	{
		entity = null;
	}
	
	protected void reorderEntities() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fTs, fbTs.getClassEntity(), classes);Collections.sort(classes, comparatorClass);}
	protected void updatePerformed(){}
	
	@Override protected void updateSecurity2(JeeslJsfSecurityHandler jsfSecurityHandler, String action)
	{
		uiAllowSave = jsfSecurityHandler.allow(action);

		if(logger.isTraceEnabled())
		{
			logger.info(uiAllowSave+" uiAllowSave "+action);
		}
	}
}