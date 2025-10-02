package org.jeesl.interfaces.util.query.module;

import java.util.List;

import org.jeesl.interfaces.model.module.ts.config.JeeslTsCategory;
import org.jeesl.interfaces.model.module.ts.config.JeeslTsDataSource2;
import org.jeesl.interfaces.model.module.ts.config.JeeslTsInterval;
import org.jeesl.interfaces.model.module.ts.core.JeeslTimeSeries;
import org.jeesl.interfaces.model.module.ts.core.JeeslTsMultiPoint;
import org.jeesl.interfaces.model.module.ts.core.JeeslTsScope;
import org.jeesl.interfaces.model.module.ts.data.JeeslTsBridge;
import org.jeesl.interfaces.model.module.ts.data.JeeslTsTransaction;
import org.jeesl.interfaces.model.module.ts.stat.JeeslTsStatistic;
import org.jeesl.interfaces.util.query.JeeslCoreQuery;

public interface JeeslTimeSeriesQuery<CAT extends JeeslTsCategory<?,?,CAT,?>,
								SCOPE extends JeeslTsScope<?,?,CAT,?,?,?,INTV>,
								MP extends JeeslTsMultiPoint<?,?,SCOPE,?,?>,
								TS extends JeeslTimeSeries<SCOPE,TS,BRIDGE,INTV,STAT>,
								TX extends JeeslTsTransaction<?,?,?,?>,
								SRC extends JeeslTsDataSource2<?,?>,
								BRIDGE extends JeeslTsBridge<?>,
								INTV extends JeeslTsInterval<?,?,INTV,?>,
								STAT extends JeeslTsStatistic<?,?,STAT,?>
>
			extends JeeslCoreQuery
{
//	void x();
	List<String> getRootFetches();
	
	List<CAT> getTsCategories();
	List<SCOPE> getTsScopes();
	List<MP> getTsMultiPoints();
	List<INTV> getTsIntervals();
	List<BRIDGE> getTsBridges();
	List<STAT> getTsTypes();
	List<TS> getTsSeries();
	List<TX> getTsTransactions();
	List<SRC> getTsDataSources();
}