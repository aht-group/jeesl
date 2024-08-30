package org.jeesl.api.facade.module;

import java.util.Date;
import java.util.List;

import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.module.ts.config.JeeslTsCategory;
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
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLangDescription;
import org.jeesl.interfaces.util.query.module.JeeslTimeSeriesQuery;
import org.jeesl.model.json.io.db.tuple.container.JsonTuples1;

public interface JeeslTsFacade <L extends JeeslLang, D extends JeeslDescription,
								CATEGORY extends JeeslTsCategory<L,D,CATEGORY,?>,
								SCOPE extends JeeslTsScope<L,D,CATEGORY,ST,UNIT,EC,INTERVAL>,
								ST extends JeeslTsScopeType<L,D,ST,?>,
								UNIT extends JeeslStatus<L,D,UNIT>,
								MP extends JeeslTsMultiPoint<L,D,SCOPE,UNIT>,
								TS extends JeeslTimeSeries<SCOPE,TS,BRIDGE,INTERVAL,TYPE>,
								TX extends JeeslTsTransaction<SRC,DATA,USER,?>,
								SRC extends EjbWithLangDescription<L,D>, 
								BRIDGE extends JeeslTsBridge<EC>,
								EC extends JeeslTsEntityClass<L,D,CATEGORY,ENTITY>,
								ENTITY extends JeeslRevisionEntity<L,D,?,?,?,?>,
								INTERVAL extends JeeslTsInterval<L,D,INTERVAL,?>,
								TYPE extends JeeslTsStatistic<L,D,TYPE,?>,
								DATA extends JeeslTsData<TS,TX,SAMPLE,POINT,WS>,
								POINT extends JeeslTsDataPoint<DATA,MP>,
								SAMPLE extends JeeslTsSample, 
								USER extends EjbWithId, 
								WS extends JeeslStatus<L,D,WS>,
								
								CRON extends JeeslTsCron<SCOPE,INTERVAL,TYPE>>
			extends JeeslFacade
{	
	List<SCOPE> fTsScopes(JeeslTimeSeriesQuery<CATEGORY,SCOPE,MP,TS,TX,BRIDGE,INTERVAL,TYPE> query);
	List<MP> fTsMultiPoints(JeeslTimeSeriesQuery<CATEGORY,SCOPE,MP,TS,TX,BRIDGE,INTERVAL,TYPE> query);
	List<EC> findClasses(Class<EC> cClass, Class<CATEGORY> cCategory, List<CATEGORY> categories, boolean showInvisibleClasses);
	
	<T extends EjbWithId> BRIDGE fBridge(EC entityClass, T ejb) throws JeeslNotFoundException;
	<T extends EjbWithId> BRIDGE fcBridge(Class<BRIDGE> cBridge, EC entityClass, T ejb) throws JeeslConstraintViolationException;
	<T extends EjbWithId> List<BRIDGE> fBridges(EC ec, List<T> ejbs);
	
	boolean isTimeSeriesAllowed(SCOPE scope, INTERVAL interval, EC c);
	
	TS fTimeSeries(SCOPE scope, INTERVAL interval, TYPE statistic, BRIDGE bridge) throws JeeslNotFoundException;
	TS fcTimeSeries(SCOPE scope, INTERVAL interval, TYPE statistic, BRIDGE bridge) throws JeeslConstraintViolationException;

	List<TS> fTimeSeries(List<BRIDGE> bridges, List<SCOPE> scopes);
	List<TS> fTimeSeries(SCOPE scope, INTERVAL interval, EC entityClass);
	List<TS> fTsSeries(JeeslTimeSeriesQuery<CATEGORY,SCOPE,MP,TS,TX,BRIDGE,INTERVAL,TYPE> query);
	
	DATA fDataLast(TS series) throws JeeslNotFoundException;
	List<DATA> fDataFirst(List<TS> list);
	List<DATA> fDataLast(List<TS> list);
	
	List<DATA> fTsData(JeeslTimeSeriesQuery<CATEGORY,SCOPE,MP,TS,TX,BRIDGE,INTERVAL,TYPE> query);
	List<DATA> fData(TX transaction);
	List<DATA> fData(WS workspace, TS timeSeries);
	List<DATA> fData(WS workspace, TS timeSeries, int year);
	List<DATA> fData(WS workspace, TS timeSeries, JeeslTsData.QueryInterval interval, Date from, Date to);
	
	List<POINT> fTsPoints(JeeslTimeSeriesQuery<CATEGORY,SCOPE,MP,TS,TX,BRIDGE,INTERVAL,TYPE> query);
	List<POINT> fPoints(WS workspace, TS timeSeries, JeeslTsData.QueryInterval interval, Date from, Date to);
	List<POINT> fPoints(WS workspace, List<TS> timeSeries, List<MP> mps, JeeslTsData.QueryInterval interval, Date from, Date to);
	
	List<TX> fTransactions(List<USER> users, JeeslTsData.QueryInterval interval, Date from, Date to);
	
	void deleteTsSeries(TS series) throws JeeslConstraintViolationException;
	void deleteTransaction(TX transaction) throws JeeslConstraintViolationException;
	
	JsonTuples1<TS> tpcTsDataByTs(List<TS> series);
	JsonTuples1<TS> tpTsDataByTs(JeeslTimeSeriesQuery<CATEGORY,SCOPE,MP,TS,TX,BRIDGE,INTERVAL,TYPE> query);
	JsonTuples1<TX> tpcTsDataByTx(JeeslTimeSeriesQuery<CATEGORY,SCOPE,MP,TS,TX,BRIDGE,INTERVAL,TYPE> query);
}