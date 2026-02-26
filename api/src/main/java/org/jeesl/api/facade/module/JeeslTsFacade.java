package org.jeesl.api.facade.module;

import java.util.Date;
import java.util.List;

import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.module.ts.config.JeeslTsCategory;
import org.jeesl.interfaces.model.module.ts.config.JeeslTsDataSource2;
import org.jeesl.interfaces.model.module.ts.config.JeeslTsInterval;
import org.jeesl.interfaces.model.module.ts.core.JeeslTimeSeries;
import org.jeesl.interfaces.model.module.ts.core.JeeslTsEntityClass;
import org.jeesl.interfaces.model.module.ts.core.JeeslTsMultiPoint;
import org.jeesl.interfaces.model.module.ts.core.JeeslTsScope;
import org.jeesl.interfaces.model.module.ts.core.JeeslTsType;
import org.jeesl.interfaces.model.module.ts.data.JeeslTsBridge;
import org.jeesl.interfaces.model.module.ts.data.JeeslTsData;
import org.jeesl.interfaces.model.module.ts.data.JeeslTsDataPoint;
import org.jeesl.interfaces.model.module.ts.data.JeeslTsSample;
import org.jeesl.interfaces.model.module.ts.data.JeeslTsTransaction;
import org.jeesl.interfaces.model.module.ts.stat.JeeslTsCron;
import org.jeesl.interfaces.model.module.ts.stat.JeeslTsStatistic;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.util.query.module.JeeslTimeSeriesQuery;
import org.jeesl.model.json.io.db.tuple.container.JsonTuples1;
import org.jeesl.model.json.io.db.tuple.container.JsonTuples2;

public interface JeeslTsFacade <CAT extends JeeslTsCategory<?,?,CAT,?>,
								SCOPE extends JeeslTsScope<?,?,CAT,TYPE,UNIT,EC,INTERVAL>,
								TYPE extends JeeslTsType<?,?,TYPE,?>,
								UNIT extends JeeslStatus<?,?,UNIT>,
								MP extends JeeslTsMultiPoint<?,?,SCOPE,UNIT,?>,
								TS extends JeeslTimeSeries<SCOPE,TS,BRIDGE,INTERVAL,STAT>,
								TX extends JeeslTsTransaction<SRC,DATA,USER,?>,
								SRC extends JeeslTsDataSource2<?,?>,
//								SRC extends JeeslTsDataSource<?,?,SRC,?>, 
								BRIDGE extends JeeslTsBridge<EC>,
								EC extends JeeslTsEntityClass<?,?,CAT,ENTITY>,
								ENTITY extends JeeslRevisionEntity<?,?,?,?,?,?>,
								INTERVAL extends JeeslTsInterval<?,?,INTERVAL,?>,
								STAT extends JeeslTsStatistic<?,?,STAT,?>,
								DATA extends JeeslTsData<TS,TX,SAMPLE,POINT,WS>,
								POINT extends JeeslTsDataPoint<DATA,MP>,
								SAMPLE extends JeeslTsSample, 
								USER extends EjbWithId, 
								WS extends JeeslStatus<?,?,WS>,
								
								CRON extends JeeslTsCron<SCOPE,INTERVAL,STAT>>
			extends JeeslFacade
{
	public enum Extrema{max,min} //avg
	
	List<SCOPE> fTsScopes(JeeslTimeSeriesQuery<CAT,SCOPE,TYPE,MP,TS,TX,SRC,BRIDGE,INTERVAL,STAT,DATA> query);
	List<MP> fTsMultiPoints(JeeslTimeSeriesQuery<CAT,SCOPE,TYPE,MP,TS,TX,SRC,BRIDGE,INTERVAL,STAT,DATA> query);
	List<EC> findClasses(Class<EC> cClass, Class<CAT> cCategory, List<CAT> categories, boolean showInvisibleClasses);
	
	<T extends EjbWithId> BRIDGE fBridge(EC entityClass, T ejb) throws JeeslNotFoundException;
	<T extends EjbWithId> BRIDGE fcBridge(Class<BRIDGE> cBridge, EC entityClass, T ejb) throws JeeslConstraintViolationException;
	<T extends EjbWithId> List<BRIDGE> fBridges(EC ec, List<T> ejbs);
	
	boolean isTimeSeriesAllowed(SCOPE scope, INTERVAL interval, EC c);
	
	TS fTimeSeries(SCOPE scope, INTERVAL interval, STAT statistic, BRIDGE bridge) throws JeeslNotFoundException;
	TS fcTimeSeries(SCOPE scope, INTERVAL interval, STAT statistic, BRIDGE bridge) throws JeeslConstraintViolationException;

	List<TS> fTimeSeries(List<BRIDGE> bridges, List<SCOPE> scopes);
	List<TS> fTimeSeries(SCOPE scope, INTERVAL interval, EC entityClass);
	List<TS> fTsSeries(JeeslTimeSeriesQuery<CAT,SCOPE,TYPE,MP,TS,TX,SRC,BRIDGE,INTERVAL,STAT,DATA> query);
	
	DATA fDataLast(TS series) throws JeeslNotFoundException;
	List<DATA> fDataFirst(List<TS> list);
	List<DATA> fDataLast(List<TS> list);
	
	List<DATA> fTsData(JeeslTimeSeriesQuery<CAT,SCOPE,TYPE,MP,TS,TX,SRC,BRIDGE,INTERVAL,STAT,DATA> query);
	List<DATA> fTsDataExtrema(JeeslTimeSeriesQuery<CAT,SCOPE,TYPE,MP,TS,TX,SRC,BRIDGE,INTERVAL,STAT,DATA> query, JeeslTsFacade.Extrema aggegation, JeeslTsInterval.Aggregation interval);
	List<DATA> fTsDataLatestOf(JeeslTsInterval.Aggregation interval, JeeslTimeSeriesQuery<CAT,SCOPE,TYPE,MP,TS,TX,SRC,BRIDGE,INTERVAL,STAT,DATA> query);
	List<DATA> fTsDataDuplicates(JeeslTimeSeriesQuery<CAT,SCOPE,TYPE,MP,TS,TX,SRC,BRIDGE,INTERVAL,STAT,DATA> query);
	List<DATA> fData(TX transaction);
	List<DATA> fData(WS workspace, TS timeSeries);
	List<DATA> fData(WS workspace, TS timeSeries, int year);
	List<DATA> fData(WS workspace, TS timeSeries, JeeslTsData.QueryInterval interval, Date from, Date to);
	
	List<POINT> fTsPoints(JeeslTimeSeriesQuery<CAT,SCOPE,TYPE,MP,TS,TX,SRC,BRIDGE,INTERVAL,STAT,DATA> query);
//	List<POINT> fTsPoints(JeeslTimeSeriesQuery<CATEGORY,SCOPE,MP,TS,TX,SRC,BRIDGE,INTERVAL,TYPE,DATA> query, JeeslTsFacade.Extrema aggegation, JeeslTsInterval.Aggregation interval);
	List<POINT> fPoints(WS workspace, TS timeSeries, JeeslTsData.QueryInterval interval, Date from, Date to);
	List<POINT> fPoints(WS workspace, List<TS> timeSeries, List<MP> mps, JeeslTsData.QueryInterval interval, Date from, Date to);
	
	List<TX> fTransactions(List<USER> users, JeeslTsData.QueryInterval interval, Date from, Date to);
	List<TX> fTsTransactions(JeeslTimeSeriesQuery<CAT,SCOPE,TYPE,MP,TS,TX,SRC,BRIDGE,INTERVAL,STAT,DATA> query);
	
	void deleteTsSeries(TS series) throws JeeslConstraintViolationException;
	void deleteTransaction(TX transaction) throws JeeslConstraintViolationException;
	
	JsonTuples1<TS> fTsDataAggregation(JeeslTimeSeriesQuery<CAT,SCOPE,TYPE,MP,TS,TX,SRC,BRIDGE,INTERVAL,STAT,DATA> query);
	JsonTuples2<TS,MP> fTsPointAggregation(JeeslTimeSeriesQuery<CAT,SCOPE,TYPE,MP,TS,TX,SRC,BRIDGE,INTERVAL,STAT,DATA> query);
	JsonTuples1<TS> tpcTsDataByTs(List<TS> series);
	JsonTuples1<TS> tpTsDataByTs(JeeslTimeSeriesQuery<CAT,SCOPE,TYPE,MP,TS,TX,SRC,BRIDGE,INTERVAL,STAT,DATA> query);
	JsonTuples1<SCOPE> tpTsDataByScope(JeeslTimeSeriesQuery<CAT,SCOPE,TYPE,MP,TS,TX,SRC,BRIDGE,INTERVAL,STAT,DATA> query);
	JsonTuples1<TX> tpcTsDataByTx(JeeslTimeSeriesQuery<CAT,SCOPE,TYPE,MP,TS,TX,SRC,BRIDGE,INTERVAL,STAT,DATA> query);
}