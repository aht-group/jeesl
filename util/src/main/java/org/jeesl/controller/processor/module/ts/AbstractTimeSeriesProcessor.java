package org.jeesl.controller.processor.module.ts;

import java.util.ArrayList;
import java.util.List;

import org.jeesl.api.facade.module.JeeslTsFacade;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.module.TsFactoryBuilder;
import org.jeesl.factory.ejb.module.ts.EjbTsDataFactory;
import org.jeesl.factory.ejb.module.ts.EjbTsDataPointFactory;
import org.jeesl.factory.mc.ts.McTimeSeriesFactory;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.module.ts.config.JeeslTsInterval;
import org.jeesl.interfaces.model.module.ts.core.JeeslTimeSeries;
import org.jeesl.interfaces.model.module.ts.core.JeeslTsEntityClass;
import org.jeesl.interfaces.model.module.ts.core.JeeslTsMultiPoint;
import org.jeesl.interfaces.model.module.ts.core.JeeslTsScope;
import org.jeesl.interfaces.model.module.ts.core.JeeslTsScopeType;
import org.jeesl.interfaces.model.module.ts.data.JeeslTsBridge;
import org.jeesl.interfaces.model.module.ts.data.JeeslTsData;
import org.jeesl.interfaces.model.module.ts.data.JeeslTsDataPoint;
import org.jeesl.interfaces.model.module.ts.data.JeeslTsTransaction;
import org.jeesl.interfaces.model.module.ts.stat.JeeslTsStatistic;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractTimeSeriesProcessor<SCOPE extends JeeslTsScope<?,?,?,ST,?,EC,INT>,
									ST extends JeeslTsScopeType<?,?,ST,?>,
									MP extends JeeslTsMultiPoint<?,?,SCOPE,?,?>,
									TS extends JeeslTimeSeries<SCOPE,TS,BRIDGE,INT,STAT>,
									TRANSACTION extends JeeslTsTransaction<?,DATA,?,?>,
									BRIDGE extends JeeslTsBridge<EC>,
									EC extends JeeslTsEntityClass<?,?,?,ENTITY>,
									ENTITY extends JeeslRevisionEntity<?,?,?,?,?,?>,
									INT extends JeeslTsInterval<?,?,INT,?>,
									STAT extends JeeslTsStatistic<?,?,STAT,?>,
									DATA extends JeeslTsData<TS,TRANSACTION,?,POINT,WS>,
									POINT extends JeeslTsDataPoint<DATA,MP>,
									WS extends JeeslStatus<?,?,WS>
>
{
	final static Logger logger = LoggerFactory.getLogger(AbstractTimeSeriesProcessor.class);
	
	protected final TsFactoryBuilder<?,?,?,?,SCOPE,ST,?,MP,TS,TRANSACTION,?,BRIDGE,EC,ENTITY,INT,STAT,DATA,POINT,?,?,WS,?,?> fbTs;
	
	protected final JeeslTsFacade<?,SCOPE,ST,?,?,TS,TRANSACTION,?,BRIDGE,EC,ENTITY,INT,STAT,DATA,POINT,?,?,WS,?> fTs;
	
	protected final McTimeSeriesFactory<SCOPE,MP,TS,BRIDGE,EC,ENTITY,INT,STAT,DATA,POINT,WS> mfTs;
	protected final EjbTsDataFactory<TS,TRANSACTION,DATA,WS> efData;
	protected final EjbTsDataPointFactory<TS,MP,DATA,POINT> efPoint;
	
	protected final List<MP> mps;
	
	protected WS ws; public WS getWorkspace() {return ws;}
	protected SCOPE scope;
	protected INT interval;
	protected EC ec;
	
	protected boolean developmentMode; public void activateDevelopmentMode() {developmentMode=true;}
	protected boolean debugOnInfo; public boolean isDebugOnInfo() {return debugOnInfo;} public void setDebugOnInfo(boolean debugOnInfo) {this.debugOnInfo = debugOnInfo;}

	public AbstractTimeSeriesProcessor(TsFactoryBuilder<?,?,?,?,SCOPE,ST,?,MP,TS,TRANSACTION,?,BRIDGE,EC,ENTITY,INT,STAT,DATA,POINT,?,?,WS,?,?> fbTs,
									JeeslTsFacade<?,SCOPE,ST,?,MP,TS,TRANSACTION,?,BRIDGE,EC,ENTITY,INT,STAT,DATA,POINT,?,?,WS,?> fTs)
	{
		this.fbTs=fbTs;
		this.fTs=fTs;
		mfTs = new McTimeSeriesFactory<SCOPE,MP,TS,BRIDGE,EC,ENTITY,INT,STAT,DATA,POINT,WS>(fbTs,fTs);
		efData = fbTs.ejbData();
		efPoint = fbTs.ejbDataPoint();
		debugOnInfo = false;
		developmentMode = false;
		
		mps = new ArrayList<>();
	}
	
	public <EWS extends Enum<EWS>, ESC extends Enum<ESC>, EIN extends Enum<EIN>> void init(EWS ews, ESC esc, EIN ein, Class<?> c)
	{
		try
		{
			ws = fTs.fByCode(fbTs.getClassWorkspace(),ews);
			scope = fTs.fByCode(fbTs.getClassScope(),esc);
			interval = fTs.fByCode(fbTs.getClassInterval(),ein);
			ec = fTs.fByCode(fbTs.getClassEntity(),c.getName());
			initMetachart();
			initMultipoint();
		}
		catch (JeeslNotFoundException e)
		{
			logger.error("Error in init for TS");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void init(WS ws, SCOPE scope, INT interval, EC ec)
	{
		this.ws=ws;
		this.scope=scope;
		this.interval=interval;
		this.ec=ec;
		initMetachart();
		initMultipoint();
	}
	
	private void initMetachart()
	{
		mfTs.setWorkspace(ws);
		mfTs.setScope(scope);
		mfTs.setInterval(interval);
		mfTs.setEntityClass(ec);
	}
	
	private void initMultipoint()
	{
		mps.clear();
		if(scope!=null && scope.getType()!=null && scope.getType().getCode()!=null)
		{
			if(scope.getType().getCode().equals(JeeslTsScopeType.Code.mp.toString()))
			{
				mps.addAll(fTs.allForParent(fbTs.getClassMp(),scope));
			}
		}
	}
	
	protected void add(List<DATA> add) throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(!add.isEmpty())
		{
			logger.info("Adding "+add.size()+" point to TS");
			TRANSACTION transaction = fbTs.ejbTransaction().build(null,null);
			transaction = fTs.save(transaction);
			for(DATA d : add)
			{
				d.setTransaction(transaction);
			}
			fTs.save(add);
		}
	}
	
	protected boolean isInitialized()
	{
		return (ws!=null) && (scope!=null) && (interval!=null) && (ec!=null);
	}
	
	public <T extends EjbWithId> TS fcTs(T t) throws JeeslConstraintViolationException
	{
		STAT statistic = fTs.fByEnum(fbTs.getClassStat(), JeeslTsStatistic.Code.raw);
		BRIDGE bridge = fTs.fcBridge(fbTs.getClassBridge(),ec,t);
		return fTs.fcTimeSeries(scope,interval,statistic,bridge);
	}
	
	public <T extends EjbWithId> TS fTs(T t) throws JeeslNotFoundException
	{
		STAT statistic = fTs.fByEnum(fbTs.getClassStat(), JeeslTsStatistic.Code.raw);
		BRIDGE bridge = fTs.fBridge(ec,t);
		return fTs.fTimeSeries(scope,interval,statistic,bridge);
	}
	
	public <T extends EjbWithId> List<TS> fTs(List<T> list)
	{
		List<TS> result = new ArrayList<>();
		if(list==null || list.isEmpty()) {return result;}
		
		STAT statistic = fTs.fByEnum(fbTs.getClassStat(), JeeslTsStatistic.Code.raw);
		List<BRIDGE> bridges = fTs.fBridges(ec,list);
		for(BRIDGE b : bridges)
		{
			try {result.add(fTs.fTimeSeries(scope,interval,statistic,b));}
			catch (JeeslNotFoundException e) {e.printStackTrace();}
		}
		
		return result;
	}
}