package org.jeesl.api.facade.module;

import java.util.Date;
import java.util.List;

import org.jeesl.interfaces.model.module.ts.core.JeeslTimeSeries;
import org.jeesl.interfaces.model.module.ts.core.JeeslTsEntityClass;
import org.jeesl.interfaces.model.module.ts.core.JeeslTsMultiPoint;
import org.jeesl.interfaces.model.module.ts.core.JeeslTsScope;
import org.jeesl.interfaces.model.module.ts.data.JeeslTsBridge;
import org.jeesl.interfaces.model.module.ts.data.JeeslTsData;
import org.jeesl.interfaces.model.module.ts.data.JeeslTsDataPoint;
import org.jeesl.interfaces.model.module.ts.data.JeeslTsSample;
import org.jeesl.interfaces.model.module.ts.data.JeeslTsTransaction;
import org.jeesl.interfaces.model.module.ts.stat.JeeslTsStatistic;
import org.jeesl.model.json.db.tuple.t1.Json1Tuples;

import net.sf.ahtutils.exception.ejb.UtilsConstraintViolationException;
import net.sf.ahtutils.exception.ejb.UtilsNotFoundException;
import net.sf.ahtutils.interfaces.facade.UtilsFacade;
import net.sf.ahtutils.interfaces.model.status.UtilsDescription;
import net.sf.ahtutils.interfaces.model.status.UtilsLang;
import net.sf.ahtutils.interfaces.model.status.UtilsStatus;
import net.sf.ahtutils.interfaces.model.with.EjbWithLangDescription;
import net.sf.ahtutils.model.interfaces.with.EjbWithId;

public interface JeeslTsFacade <L extends UtilsLang,
								D extends UtilsDescription,
								CAT extends UtilsStatus<CAT,L,D>,
								SCOPE extends JeeslTsScope<L,D,CAT,ST,UNIT,EC,INT>,
								ST extends UtilsStatus<ST,L,D>,
								UNIT extends UtilsStatus<UNIT,L,D>,
								MP extends JeeslTsMultiPoint<L,D,SCOPE,UNIT>,
								TS extends JeeslTimeSeries<SCOPE,BRIDGE,INT>,
								TRANSACTION extends JeeslTsTransaction<SOURCE,DATA,USER,?>,
								SOURCE extends EjbWithLangDescription<L,D>, 
								BRIDGE extends JeeslTsBridge<EC>,
								EC extends JeeslTsEntityClass<L,D,CAT>,
								INT extends UtilsStatus<INT,L,D>,
								STAT extends JeeslTsStatistic<L,D,STAT,?>,
								DATA extends JeeslTsData<TS,TRANSACTION,SAMPLE,WS>,
								POINT extends JeeslTsDataPoint<DATA,MP>,
								SAMPLE extends JeeslTsSample, 
								USER extends EjbWithId, 
								WS extends UtilsStatus<WS,L,D>,
								QAF extends UtilsStatus<QAF,L,D>>
			extends UtilsFacade
{	
	List<SCOPE> findScopes(Class<SCOPE> cScope, Class<CAT> cCategory, List<CAT> categories, boolean showInvisibleScopes);
	List<EC> findClasses(Class<EC> cClass, Class<CAT> cCategory, List<CAT> categories, boolean showInvisibleClasses);
	
	<T extends EjbWithId> BRIDGE fBridge(EC entityClass, T ejb) throws UtilsNotFoundException;
	<T extends EjbWithId> BRIDGE fcBridge(Class<BRIDGE> cBridge, EC entityClass, T ejb) throws UtilsConstraintViolationException;
	<T extends EjbWithId> List<BRIDGE> fBridges(EC ec, List<T> ejbs);
	
	boolean isTimeSeriesAllowed(SCOPE scope, INT interval, EC c);
	
	List<TS> fTimeSeries(List<BRIDGE> bridges);
	List<TS> fTimeSeries(List<BRIDGE> bridges, List<SCOPE> scopes);
	List<TS> fTimeSeries(SCOPE scope, INT interval, EC entityClass);
	TS fTimeSeries(SCOPE scope, INT interval, BRIDGE bridge) throws UtilsNotFoundException;
	TS fcTimeSeries(SCOPE scope, INT interval, BRIDGE bridge) throws UtilsConstraintViolationException;
	
	List<DATA> fData(TRANSACTION transaction);
	List<DATA> fData(WS workspace, TS timeSeries);
	List<DATA> fData(WS workspace, TS timeSeries, Date from, Date to);
	
	List<POINT> fPoints(WS workspace, TS timeSeries, Date from, Date to);
	
	List<TRANSACTION> fTransactions(List<USER> users, Date from, Date to);
	
	void deleteTransaction(TRANSACTION transaction) throws UtilsConstraintViolationException;
	
	Json1Tuples<TS> tpCountRecordsByTs(List<TS> series);
}