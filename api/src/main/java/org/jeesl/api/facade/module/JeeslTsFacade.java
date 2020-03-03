package org.jeesl.api.facade.module;

import java.util.Date;
import java.util.List;

import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.module.ts.core.JeeslTimeSeries;
import org.jeesl.interfaces.model.module.ts.core.JeeslTsEntityClass;
import org.jeesl.interfaces.model.module.ts.core.JeeslTsMultiPoint;
import org.jeesl.interfaces.model.module.ts.core.JeeslTsScope;
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
import org.jeesl.model.json.db.tuple.t1.Json1Tuples;

public interface JeeslTsFacade <L extends JeeslLang, D extends JeeslDescription,
								CAT extends JeeslStatus<CAT,L,D>,
								SCOPE extends JeeslTsScope<L,D,CAT,ST,UNIT,EC,INT>,
								ST extends JeeslStatus<ST,L,D>,
								UNIT extends JeeslStatus<UNIT,L,D>,
								MP extends JeeslTsMultiPoint<L,D,SCOPE,UNIT>,
								TS extends JeeslTimeSeries<SCOPE,BRIDGE,INT,STAT>,
								TRANSACTION extends JeeslTsTransaction<SOURCE,DATA,USER,?>,
								SOURCE extends EjbWithLangDescription<L,D>, 
								BRIDGE extends JeeslTsBridge<EC>,
								EC extends JeeslTsEntityClass<L,D,CAT>,
								INT extends JeeslStatus<INT,L,D>,
								STAT extends JeeslTsStatistic<L,D,STAT,?>,
								DATA extends JeeslTsData<TS,TRANSACTION,SAMPLE,WS>,
								POINT extends JeeslTsDataPoint<DATA,MP>,
								SAMPLE extends JeeslTsSample, 
								USER extends EjbWithId, 
								WS extends JeeslStatus<WS,L,D>,
								QAF extends JeeslStatus<QAF,L,D>,
								CRON extends JeeslTsCron<SCOPE,INT,STAT>>
			extends JeeslFacade
{	
	List<SCOPE> findScopes(Class<SCOPE> cScope, Class<CAT> cCategory, List<CAT> categories, boolean showInvisibleScopes);
	List<EC> findClasses(Class<EC> cClass, Class<CAT> cCategory, List<CAT> categories, boolean showInvisibleClasses);
	
	<T extends EjbWithId> BRIDGE fBridge(EC entityClass, T ejb) throws JeeslNotFoundException;
	<T extends EjbWithId> BRIDGE fcBridge(Class<BRIDGE> cBridge, EC entityClass, T ejb) throws JeeslConstraintViolationException;
	<T extends EjbWithId> List<BRIDGE> fBridges(EC ec, List<T> ejbs);
	
	boolean isTimeSeriesAllowed(SCOPE scope, INT interval, EC c);
	
	List<TS> fTimeSeries(List<BRIDGE> bridges);
	List<TS> fTimeSeries(List<BRIDGE> bridges, List<SCOPE> scopes);
	List<TS> fTimeSeries(SCOPE scope, INT interval, EC entityClass);
	TS fTimeSeries(SCOPE scope, INT interval, STAT statistic, BRIDGE bridge) throws JeeslNotFoundException;
	TS fcTimeSeries(SCOPE scope, INT interval, STAT statistic, BRIDGE bridge) throws JeeslConstraintViolationException;
	
	List<DATA> fData(TRANSACTION transaction);
	List<DATA> fData(WS workspace, TS timeSeries);
	List<DATA> fData(WS workspace, TS timeSeries, JeeslTsData.QueryInterval interval, Date from, Date to);
	
	List<POINT> fPoints(WS workspace, TS timeSeries, JeeslTsData.QueryInterval interval, Date from, Date to);
	
	List<TRANSACTION> fTransactions(List<USER> users, JeeslTsData.QueryInterval interval, Date from, Date to);
	
	void deleteTransaction(TRANSACTION transaction) throws JeeslConstraintViolationException;
	
	Json1Tuples<TS> tpCountRecordsByTs(List<TS> series);
}