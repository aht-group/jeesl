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
import org.jeesl.interfaces.util.query.module.EjbTimeSeriesQuery;
import org.jeesl.model.json.io.db.tuple.container.JsonTuples1;

public interface JeeslTsFacade <L extends JeeslLang, D extends JeeslDescription,
								CATEGORY extends JeeslTsCategory<L,D,CATEGORY,?>,
								SCOPE extends JeeslTsScope<L,D,CATEGORY,ST,UNIT,EC,INTERVAL>,
								ST extends JeeslTsScopeType<L,D,ST,?>,
								UNIT extends JeeslStatus<L,D,UNIT>,
								MP extends JeeslTsMultiPoint<L,D,SCOPE,UNIT>,
								TS extends JeeslTimeSeries<SCOPE,TS,BRIDGE,INTERVAL,STATISTIC>,
								TX extends JeeslTsTransaction<SRC,DATA,USER,?>,
								SRC extends EjbWithLangDescription<L,D>, 
								BRIDGE extends JeeslTsBridge<EC>,
								EC extends JeeslTsEntityClass<L,D,CATEGORY,ENTITY>,
								ENTITY extends JeeslRevisionEntity<L,D,?,?,?,?>,
								INTERVAL extends JeeslTsInterval<L,D,INTERVAL,?>,
								STATISTIC extends JeeslTsStatistic<L,D,STATISTIC,?>,
								DATA extends JeeslTsData<TS,TX,SAMPLE,POINT,WS>,
								POINT extends JeeslTsDataPoint<DATA,MP>,
								SAMPLE extends JeeslTsSample, 
								USER extends EjbWithId, 
								WS extends JeeslStatus<L,D,WS>,
								QAF extends JeeslStatus<L,D,QAF>,
								CRON extends JeeslTsCron<SCOPE,INTERVAL,STATISTIC>>
			extends JeeslFacade
{	
	List<SCOPE> fTsScopes(EjbTimeSeriesQuery<CATEGORY,SCOPE,TS,TX,BRIDGE,INTERVAL,STATISTIC> query);
	List<EC> findClasses(Class<EC> cClass, Class<CATEGORY> cCategory, List<CATEGORY> categories, boolean showInvisibleClasses);
	
	<T extends EjbWithId> BRIDGE fBridge(EC entityClass, T ejb) throws JeeslNotFoundException;
	<T extends EjbWithId> BRIDGE fcBridge(Class<BRIDGE> cBridge, EC entityClass, T ejb) throws JeeslConstraintViolationException;
	<T extends EjbWithId> List<BRIDGE> fBridges(EC ec, List<T> ejbs);
	
	boolean isTimeSeriesAllowed(SCOPE scope, INTERVAL interval, EC c);
	
	TS fTimeSeries(SCOPE scope, INTERVAL interval, STATISTIC statistic, BRIDGE bridge) throws JeeslNotFoundException;
	TS fcTimeSeries(SCOPE scope, INTERVAL interval, STATISTIC statistic, BRIDGE bridge) throws JeeslConstraintViolationException;

	List<TS> fTimeSeries(List<BRIDGE> bridges, List<SCOPE> scopes);
	List<TS> fTimeSeries(SCOPE scope, INTERVAL interval, EC entityClass);
	List<TS> fTimeSeries(EjbTimeSeriesQuery<CATEGORY,SCOPE,TS,TX,BRIDGE,INTERVAL,STATISTIC> query);
	
	DATA fDataLast(TS series) throws JeeslNotFoundException;
	
	List<DATA> fData(EjbTimeSeriesQuery<CATEGORY,SCOPE,TS,TX,BRIDGE,INTERVAL,STATISTIC> query);
	List<DATA> fData(TX transaction);
	List<DATA> fData(WS workspace, TS timeSeries);
	List<DATA> fData(WS workspace, TS timeSeries, int year);
	List<DATA> fData(WS workspace, TS timeSeries, JeeslTsData.QueryInterval interval, Date from, Date to);
	List<DATA> fDataFirst(List<TS> list);
	List<DATA> fDataLast(List<TS> list);
	
	List<POINT> fPoints(WS workspace, TS timeSeries, JeeslTsData.QueryInterval interval, Date from, Date to);
	List<POINT> fPoints(WS workspace, List<TS> timeSeries, List<MP> mps, JeeslTsData.QueryInterval interval, Date from, Date to);
	
	List<TX> fTransactions(List<USER> users, JeeslTsData.QueryInterval interval, Date from, Date to);
	
	void deleteTsSeries(TS series) throws JeeslConstraintViolationException;
	void deleteTransaction(TX transaction) throws JeeslConstraintViolationException;
	
	JsonTuples1<TS> tpcTsDataByTs(List<TS> series);
	JsonTuples1<TX> tpcTsDataByTx(EjbTimeSeriesQuery<CATEGORY,SCOPE,TS,TX,BRIDGE,INTERVAL,STATISTIC> query);
}