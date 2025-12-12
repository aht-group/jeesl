package org.jeesl.util.query.ejb.module;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.jeesl.interfaces.model.module.ts.config.JeeslTsCategory;
import org.jeesl.interfaces.model.module.ts.config.JeeslTsDataSource2;
import org.jeesl.interfaces.model.module.ts.config.JeeslTsInterval;
import org.jeesl.interfaces.model.module.ts.core.JeeslTimeSeries;
import org.jeesl.interfaces.model.module.ts.core.JeeslTsMultiPoint;
import org.jeesl.interfaces.model.module.ts.core.JeeslTsScope;
import org.jeesl.interfaces.model.module.ts.core.JeeslTsScopeType;
import org.jeesl.interfaces.model.module.ts.data.JeeslTsBridge;
import org.jeesl.interfaces.model.module.ts.data.JeeslTsData;
import org.jeesl.interfaces.model.module.ts.data.JeeslTsTransaction;
import org.jeesl.interfaces.model.module.ts.stat.JeeslTsStatistic;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.util.query.module.JeeslTimeSeriesQuery;
import org.jeesl.util.query.ejb.AbstractEjbQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbTimeSeriesQuery<CAT extends JeeslTsCategory<?,?,CAT,?>,
								SCOPE extends JeeslTsScope<?,?,CAT,?,?,?,INTERVAL>,
								TYPE extends JeeslTsScopeType<?,?,TYPE,?>,
								MP extends JeeslTsMultiPoint<?,?,SCOPE,?,?>,
								TS extends JeeslTimeSeries<SCOPE,TS,BRIDGE,INTERVAL,STAT>,
								TX extends JeeslTsTransaction<?,?,?,?>,
								SRC extends JeeslTsDataSource2<?,?>,
								BRIDGE extends JeeslTsBridge<?>,
								INTERVAL extends JeeslTsInterval<?,?,INTERVAL,?>,
								STAT extends JeeslTsStatistic<?,?,STAT,?>,
								DATA extends JeeslTsData<TS,TX,?,?,?>
>
			extends AbstractEjbQuery
			implements JeeslTimeSeriesQuery<CAT,SCOPE,TYPE,MP,TS,TX,SRC,BRIDGE,INTERVAL,STAT,DATA>
{
	private static final long serialVersionUID = 1L;
	
	final static Logger logger = LoggerFactory.getLogger(EjbTimeSeriesQuery.class);
	
	public EjbTimeSeriesQuery()
	{       
		reset();
	}
	
	@Override public void reset()
	{
		tsBridges=null;
	}
	
	//Fetches
	public <E extends Enum<E>> EjbTimeSeriesQuery<CAT,SCOPE,TYPE,MP,TS,TX,SRC,BRIDGE,INTERVAL,STAT,DATA> addRootFetch(E e){if(rootFetches==null) {rootFetches = new ArrayList<>();} rootFetches.add(e.toString()); return this;}
	public EjbTimeSeriesQuery<CAT,SCOPE,TYPE,MP,TS,TX,SRC,BRIDGE,INTERVAL,STAT,DATA> distinct(boolean distinct) {super.setDistinct(distinct); return this;}
	public EjbTimeSeriesQuery<CAT,SCOPE,TYPE,MP,TS,TX,SRC,BRIDGE,INTERVAL,STAT,DATA> maxResults(int maxResults) {super.setMaxResults(maxResults); return this;}
	
	//Lists
	@Override public EjbTimeSeriesQuery<CAT,SCOPE,TYPE,MP,TS,TX,SRC,BRIDGE,INTERVAL,STAT,DATA> id(EjbWithId id) {if(Objects.isNull(idList)) {idList = new ArrayList<>();} idList.add(id.getId()); return this;}
	@Override public <T extends EjbWithId> EjbTimeSeriesQuery<CAT,SCOPE,TYPE,MP,TS,TX,SRC,BRIDGE,INTERVAL,STAT,DATA> ids(List<T> ids) {logger.error("NYI"); return this;}
	@Override public EjbTimeSeriesQuery<CAT,SCOPE,TYPE,MP,TS,TX,SRC,BRIDGE,INTERVAL,STAT,DATA> idList(List<Long> list) {if(Objects.isNull(idList)) {idList = new ArrayList<>();} idList.addAll(list); return this;}
	@Override public EjbTimeSeriesQuery<CAT,SCOPE,TYPE,MP,TS,TX,SRC,BRIDGE,INTERVAL,STAT,DATA> codeList(List<String> list) {if(Objects.isNull(codeList)) {codeList = new ArrayList<>();} codeList.addAll(list); return this;}

	//LocalDate
	public EjbTimeSeriesQuery<CAT,SCOPE,TYPE,MP,TS,TX,SRC,BRIDGE,INTERVAL,STAT,DATA> ld1(LocalDate ld1) {this.localDate1 = ld1; return this;}
	public EjbTimeSeriesQuery<CAT,SCOPE,TYPE,MP,TS,TX,SRC,BRIDGE,INTERVAL,STAT,DATA> ld2(LocalDate ld2) {this.localDate2 = ld2; return this;}
	public EjbTimeSeriesQuery<CAT,SCOPE,TYPE,MP,TS,TX,SRC,BRIDGE,INTERVAL,STAT,DATA> ld3(LocalDate ld3) {this.ld3 = ld3; return this;}
	
	private List<CAT> tsCategories;
	@Override public List<CAT> getTsCategories() {return tsCategories;}
	public EjbTimeSeriesQuery<CAT,SCOPE,TYPE,MP,TS,TX,SRC,BRIDGE,INTERVAL,STAT,DATA> add(CAT category) {if(Objects.isNull(tsCategories)) {tsCategories = new ArrayList<>();} tsCategories.add(category); return this;}
	public EjbTimeSeriesQuery<CAT,SCOPE,TYPE,MP,TS,TX,SRC,BRIDGE,INTERVAL,STAT,DATA> addTsCategories(List<CAT> list) {if(Objects.isNull(tsCategories)) {tsCategories = new ArrayList<>();} tsCategories.addAll(list); return this;}
	
	private List<SCOPE> tsScopes;
	@Override public List<SCOPE> getTsScopes() {return tsScopes;}
	public EjbTimeSeriesQuery<CAT,SCOPE,TYPE,MP,TS,TX,SRC,BRIDGE,INTERVAL,STAT,DATA> add(SCOPE scope) {if(Objects.isNull(tsScopes)) {tsScopes = new ArrayList<>();} tsScopes.add(scope); return this;}
	public EjbTimeSeriesQuery<CAT,SCOPE,TYPE,MP,TS,TX,SRC,BRIDGE,INTERVAL,STAT,DATA> addTsScopes(List<SCOPE> list) {if(Objects.isNull(tsScopes)) {tsScopes = new ArrayList<>();} tsScopes.addAll(list); return this;}

	private List<MP> tsMultiPoints;
	@Override public List<MP> getTsMultiPoints() {return tsMultiPoints;}
	
	private List<INTERVAL> tsIntervals;
	@Override public List<INTERVAL> getTsIntervals() {return tsIntervals;}
	public EjbTimeSeriesQuery<CAT,SCOPE,TYPE,MP,TS,TX,SRC,BRIDGE,INTERVAL,STAT,DATA> add(INTERVAL interval) {if(Objects.isNull(tsIntervals)) {tsIntervals = new ArrayList<>();} tsIntervals.add(interval); return this;}
	
	private List<BRIDGE> tsBridges;
	@Override public List<BRIDGE> getTsBridges() {return tsBridges;}
	public EjbTimeSeriesQuery<CAT,SCOPE,TYPE,MP,TS,TX,SRC,BRIDGE,INTERVAL,STAT,DATA> add(BRIDGE bridge) {if(Objects.isNull(tsBridges)) {tsBridges = new ArrayList<>();} tsBridges.add(bridge); return this;}
	public EjbTimeSeriesQuery<CAT,SCOPE,TYPE,MP,TS,TX,SRC,BRIDGE,INTERVAL,STAT,DATA> addTsBridges(List<BRIDGE> list) {if(Objects.isNull(tsBridges)) {tsBridges = new ArrayList<>();} tsBridges.addAll(list); return this;}
	
	private List<TS> tsSeries;
	@Override public List<TS> getTsSeries() {return tsSeries;}
	public EjbTimeSeriesQuery<CAT,SCOPE,TYPE,MP,TS,TX,SRC,BRIDGE,INTERVAL,STAT,DATA> add(TS ts) {if(Objects.isNull(tsSeries)) {tsSeries = new ArrayList<>();} tsSeries.add(ts); return this;}
	
	private List<TX> tsTransactions;
	@Override public List<TX> getTsTransactions() {return tsTransactions;}
	public EjbTimeSeriesQuery<CAT,SCOPE,TYPE,MP,TS,TX,SRC,BRIDGE,INTERVAL,STAT,DATA> add(TX tx) {if(Objects.isNull(tsTransactions)) {tsTransactions = new ArrayList<>();} tsTransactions.add(tx); return this;}
	
	private List<SRC> tsDataSources;
	@Override public List<SRC> getTsDataSources() {return tsDataSources;}
	
	private List<STAT> tsTypes;
	@Override public List<STAT> getTsTypes() {return tsTypes;}
	
	private List<DATA> tsData;
	@Override public List<DATA> getTsData() {return tsData;}
}