package org.jeesl.interfaces.util.query.module;

import java.util.List;

import org.jeesl.interfaces.model.module.ts.config.JeeslTsCategory;
import org.jeesl.interfaces.model.module.ts.config.JeeslTsInterval;
import org.jeesl.interfaces.model.module.ts.core.JeeslTimeSeries;
import org.jeesl.interfaces.model.module.ts.core.JeeslTsScope;
import org.jeesl.interfaces.model.module.ts.data.JeeslTsBridge;
import org.jeesl.interfaces.model.module.ts.data.JeeslTsTransaction;
import org.jeesl.interfaces.model.module.ts.stat.JeeslTsStatistic;
import org.jeesl.interfaces.util.query.JeeslCoreQuery;

public interface JeeslTimeSeriesQuery<CAT extends JeeslTsCategory<?,?,CAT,?>,
								SCOPE extends JeeslTsScope<?,?,CAT,?,?,?,INTV>,
								TS extends JeeslTimeSeries<SCOPE,TS,BRIDGE,INTV,STAT>,
								TX extends JeeslTsTransaction<?,?,?,?>,
								BRIDGE extends JeeslTsBridge<?>,
								INTV extends JeeslTsInterval<?,?,INTV,?>,
								STAT extends JeeslTsStatistic<?,?,STAT,?>
>
			extends JeeslCoreQuery
{
	List<String> getRootFetches();
	
	List<CAT> getCategories();
	List<SCOPE> getScopes();
	List<INTV> getIntervals();
	List<BRIDGE> getBridges();
	List<TS> getSeries();
	List<TX> getTransactions();
}