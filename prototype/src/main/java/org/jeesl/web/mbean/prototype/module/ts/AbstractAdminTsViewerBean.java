package org.jeesl.web.mbean.prototype.module.ts;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.exlp.util.jx.JaxbUtil;
import org.jeesl.api.bean.JeeslTranslationBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.module.JeeslTsFacade;
import org.jeesl.controller.web.util.AbstractLogMessage;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.builder.module.TsFactoryBuilder;
import org.jeesl.factory.mc.ts.McTsViewerFactory;
import org.jeesl.interfaces.bean.sb.bean.SbSingleBean;
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
import org.jeesl.jsf.handler.op.OpEntitySelectionHandler;
import org.jeesl.jsf.handler.sb.SbSingleHandler;
import org.jeesl.util.query.ejb.module.EjbTimeSeriesQuery;
import org.metachart.model.xml.chart.Ds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractAdminTsViewerBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
											CAT extends JeeslTsCategory<L,D,CAT,?>,
											SCOPE extends JeeslTsScope<L,D,CAT,ST,UNIT,EC,INT>,
											ST extends JeeslTsScopeType<L,D,ST,?>,
											UNIT extends JeeslStatus<L,D,UNIT>,
											MP extends JeeslTsMultiPoint<L,D,SCOPE,UNIT,?>,
											TS extends JeeslTimeSeries<SCOPE,TS,BRIDGE,INT,STAT>,
											TX extends JeeslTsTransaction<SRC,DATA,USER,?>,
											SRC extends JeeslTsDataSource2<L,D>, 
											BRIDGE extends JeeslTsBridge<EC>,
											EC extends JeeslTsEntityClass<L,D,CAT,ENTITY>,
											ENTITY extends JeeslRevisionEntity<L,D,?,?,?,?>,
											INT extends JeeslTsInterval<L,D,INT,?>,
											STAT extends JeeslTsStatistic<L,D,STAT,?>,
											DATA extends JeeslTsData<TS,TX,SAMPLE,POINT,WS>,
											POINT extends JeeslTsDataPoint<DATA,MP>,
											SAMPLE extends JeeslTsSample, 
											USER extends EjbWithId, 
											WS extends JeeslStatus<L,D,WS>,
											QAF extends JeeslStatus<L,D,QAF>,
											CRON extends JeeslTsCron<SCOPE,INT,STAT>>
					extends AbstractAdminTsBean<L,D,LOC,CAT,SCOPE,ST,UNIT,MP,TS,TX,SRC,BRIDGE,EC,ENTITY,INT,STAT,DATA,POINT,SAMPLE,USER,WS,QAF,CRON>
					implements Serializable,SbSingleBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractAdminTsViewerBean.class);

	private final OpEntitySelectionHandler<TS> tsh; public OpEntitySelectionHandler<TS> getTsh() {return tsh;}
	private final Map<TS,EjbWithId> mapTsEntity; public Map<TS,EjbWithId> getMapTsEntity() {return mapTsEntity;}
	
	protected final SbSingleHandler<SCOPE> sbhScope; public SbSingleHandler<SCOPE> getSbhScope() {return sbhScope;}
	protected final SbSingleHandler<EC> sbhClass; public SbSingleHandler<EC> getSbhClass() {return sbhClass;}
	protected final SbSingleHandler<INT> sbhInterval; public SbSingleHandler<INT> getSbhInterval() {return sbhInterval;}
	
	public AbstractAdminTsViewerBean(final TsFactoryBuilder<L,D,LOC,CAT,SCOPE,ST,UNIT,MP,TS,TX,SRC,BRIDGE,EC,ENTITY,INT,STAT,DATA,POINT,SAMPLE,USER,WS,QAF,CRON> fbTs)
	{
		super(fbTs);
		sbhScope = new SbSingleHandler<SCOPE>(fbTs.getClassScope(),this);
		sbhClass = new SbSingleHandler<EC>(fbTs.getClassEntity(),this);
		sbhInterval = new SbSingleHandler<INT>(fbTs.getClassInterval(),this);
		
		tsh = new OpEntitySelectionHandler<TS>(null);
		mapTsEntity = new HashMap<TS,EjbWithId>();
	}
	
	protected void initSuper(JeeslTranslationBean<L,D,LOC> bTranslation, JeeslFacesMessageBean bMessage, JeeslTsFacade<CAT,SCOPE,ST,UNIT,MP,TS,TX,SRC,BRIDGE,EC,ENTITY,INT,STAT,DATA,POINT,SAMPLE,USER,WS,CRON> fTs)
	{
		super.postConstructTs(bTranslation,bMessage,fTs);
		sbhCategory.toggleAll();
	}
	
	@Override public void toggled(SbToggleSelection handler, Class<?> c) throws JeeslLockingException, JeeslConstraintViolationException
	{
		super.toggled(handler,c);
		if(fbTs.getClassCategory().isAssignableFrom(c))
		{
			EjbTimeSeriesQuery<CAT,SCOPE,MP,TS,TX,SRC,BRIDGE,INT,STAT,DATA> query = new EjbTimeSeriesQuery<>();
			query.addTsCategories(sbhCategory.getSelected());
			
			sbhScope.setList(fTs.fTsScopes(query));
			Collections.sort(sbhScope.getList(), comparatorScope);
			if(debugOnInfo){logger.info(AbstractLogMessage.reloaded(fbTs.getClassScope(),sbhScope.getList()));}
			sbhScope.silentCallback();
		}
	}
	
	@Override
	public void selectSbSingle(EjbWithId item)
	{
		if(fbTs.getClassScope().isAssignableFrom(item.getClass()))
		{
			sbhClass.clear();sbhInterval.clear();
			
			sbhClass.setList(sbhScope.getSelection().getClasses());
			if(debugOnInfo){logger.info(AbstractLogMessage.reloaded(fbTs.getClassEntity(),sbhClass.getList()));}
			sbhClass.setDefault();sbhClass.silentCallback();
			
			sbhInterval.setList(sbhScope.getSelection().getIntervals());
			if(debugOnInfo){logger.info(AbstractLogMessage.reloaded(fbTs.getClassInterval(),sbhInterval.getList()));}
			sbhInterval.setDefault();sbhInterval.silentCallback();
		}
		else if(fbTs.getClassEntity().isAssignableFrom(item.getClass())) {if(sbhClass.isSelected() && sbhInterval.isSelected()) {reloadBridges();}}
		else if(fbTs.getClassInterval().isAssignableFrom(item.getClass())) {if(sbhClass.isSelected() && sbhInterval.isSelected()) {reloadBridges();}}
	}
	
	private void reloadBridges()
	{
		tsh.setTbList(fTs.fTimeSeries(sbhScope.getSelection(), sbhInterval.getSelection(), sbhClass.getSelection()));
		if(debugOnInfo){logger.info(AbstractLogMessage.reloaded(fbTs.getClassTs(),tsh.getTbList()));}
		
		try
		{
			mapTsEntity.clear();
			Class<EjbWithId> c = (Class<EjbWithId>)Class.forName(sbhClass.getSelection().getCode()).asSubclass(EjbWithId.class);
			Map<Long,TS> mapBridgeTs = efTs.toMapBridgeRefIdTs(tsh.getTbList());
			for(EjbWithId ejb : fTs.find(c,efTs.toBridgeIds(tsh.getTbList())))
			{
				mapTsEntity.put(mapBridgeTs.get(ejb.getId()),ejb);
			}
		}
		catch (ClassNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void selectTimeseries()
	{
		logger.info("Selected: "+tsh.getOpList().size());
		List<DATA> list = fTs.fData(sbhWorkspace.getSelected().get(0), tsh.getOpList().get(0));
		
		McTsViewerFactory<TS,DATA> f = new McTsViewerFactory<TS,DATA>();
		ds=f.build2(list);
		JaxbUtil.info(ds);
	}
	
	Ds ds; public Ds getDs() {return ds;} public void setDs(Ds ds) {this.ds = ds;}
}