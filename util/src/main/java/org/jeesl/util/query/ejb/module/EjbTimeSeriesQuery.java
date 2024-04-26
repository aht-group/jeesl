package org.jeesl.util.query.ejb.module;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.jeesl.interfaces.model.module.ts.config.JeeslTsCategory;
import org.jeesl.interfaces.model.module.ts.config.JeeslTsInterval;
import org.jeesl.interfaces.model.module.ts.core.JeeslTimeSeries;
import org.jeesl.interfaces.model.module.ts.core.JeeslTsScope;
import org.jeesl.interfaces.model.module.ts.data.JeeslTsBridge;
import org.jeesl.interfaces.model.module.ts.data.JeeslTsTransaction;
import org.jeesl.interfaces.model.module.ts.stat.JeeslTsStatistic;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.util.query.module.JeeslTimeSeriesQuery;
import org.jeesl.util.query.ejb.AbstractEjbQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbTimeSeriesQuery<CAT extends JeeslTsCategory<?,?,CAT,?>,
								SCOPE extends JeeslTsScope<?,?,CAT,?,?,?,INTV>,
								TS extends JeeslTimeSeries<SCOPE,TS,BRIDGE,INTV,STAT>,
								TX extends JeeslTsTransaction<?,?,?,?>,
								BRIDGE extends JeeslTsBridge<?>,
								INTV extends JeeslTsInterval<?,?,INTV,?>,
								STAT extends JeeslTsStatistic<?,?,STAT,?>
>
			extends AbstractEjbQuery
			implements JeeslTimeSeriesQuery<CAT,SCOPE,TS,TX,BRIDGE,INTV,STAT>
{
	private static final long serialVersionUID = 1L;
	
	final static Logger logger = LoggerFactory.getLogger(EjbTimeSeriesQuery.class);
	
	public EjbTimeSeriesQuery()
	{       
		reset();
	}
	
	@Override public void reset()
	{
		bridges=null;
	}
	
	//Fetches
	public <E extends Enum<E>> EjbTimeSeriesQuery<CAT,SCOPE,TS,TX,BRIDGE,INTV,STAT> addRootFetch(E e){if(rootFetches==null) {rootFetches = new ArrayList<>();} rootFetches.add(e.toString()); return this;}
	public EjbTimeSeriesQuery<CAT,SCOPE,TS,TX,BRIDGE,INTV,STAT> distinct(boolean distinct) {super.setDistinct(distinct); return this;}
	public EjbTimeSeriesQuery<CAT,SCOPE,TS,TX,BRIDGE,INTV,STAT> maxResults(int maxResults) {super.setMaxResults(maxResults); return this;}
	
	//Lists
	@Override public EjbTimeSeriesQuery<CAT,SCOPE,TS,TX,BRIDGE,INTV,STAT> id(EjbWithId id) {if(Objects.isNull(idList)) {idList = new ArrayList<>();} idList.add(id.getId()); return this;}
	@Override public <T extends EjbWithId> EjbTimeSeriesQuery<CAT,SCOPE,TS,TX,BRIDGE,INTV,STAT> ids(List<T> ids) {logger.error("NYI"); return this;}
	@Override public EjbTimeSeriesQuery<CAT,SCOPE,TS,TX,BRIDGE,INTV,STAT> idList(List<Long> list) {if(Objects.isNull(idList)) {idList = new ArrayList<>();} idList.addAll(list); return this;}
	@Override public EjbTimeSeriesQuery<CAT,SCOPE,TS,TX,BRIDGE,INTV,STAT> codeList(List<String> list) {if(Objects.isNull(codeList)) {codeList = new ArrayList<>();} codeList.addAll(list); return this;}

	//LocalDate
	public EjbTimeSeriesQuery<CAT,SCOPE,TS,TX,BRIDGE,INTV,STAT> ld1(LocalDate ld1) {this.localDate1 = ld1; return this;}
	public EjbTimeSeriesQuery<CAT,SCOPE,TS,TX,BRIDGE,INTV,STAT> ld2(LocalDate ld2) {this.localDate2 = ld2; return this;}
	public EjbTimeSeriesQuery<CAT,SCOPE,TS,TX,BRIDGE,INTV,STAT> ld3(LocalDate ld3) {this.ld3 = ld3; return this;}
	
	private List<CAT> categories; public List<CAT> getCategories() {return categories;}
	public EjbTimeSeriesQuery<CAT,SCOPE,TS,TX,BRIDGE,INTV,STAT> add(CAT category) {if(Objects.isNull(categories)) {categories = new ArrayList<>();} categories.add(category); return this;}
	public EjbTimeSeriesQuery<CAT,SCOPE,TS,TX,BRIDGE,INTV,STAT> addCategories(List<CAT> list) {if(Objects.isNull(categories)) {categories = new ArrayList<>();} categories.addAll(list); return this;}
	
	private List<SCOPE> scopes; public List<SCOPE> getScopes() {return scopes;}
	public EjbTimeSeriesQuery<CAT,SCOPE,TS,TX,BRIDGE,INTV,STAT> add(SCOPE scope) {if(Objects.isNull(scopes)) {scopes = new ArrayList<>();} scopes.add(scope); return this;}
	public EjbTimeSeriesQuery<CAT,SCOPE,TS,TX,BRIDGE,INTV,STAT> addTsScopes(List<SCOPE> list) {if(Objects.isNull(scopes)) {scopes = new ArrayList<>();} scopes.addAll(list); return this;}

	private List<INTV> intervals; public List<INTV> getIntervals() {return intervals;}
	public EjbTimeSeriesQuery<CAT,SCOPE,TS,TX,BRIDGE,INTV,STAT> add(INTV interval) {if(Objects.isNull(intervals)) {intervals = new ArrayList<>();} intervals.add(interval); return this;}
	
	private List<BRIDGE> bridges; public List<BRIDGE> getBridges() {return bridges;}
	public EjbTimeSeriesQuery<CAT,SCOPE,TS,TX,BRIDGE,INTV,STAT> add(BRIDGE bridge) {if(Objects.isNull(bridges)) {bridges = new ArrayList<>();} bridges.add(bridge); return this;}
	public EjbTimeSeriesQuery<CAT,SCOPE,TS,TX,BRIDGE,INTV,STAT> addBridges(List<BRIDGE> list) {if(Objects.isNull(bridges)) {bridges = new ArrayList<>();} bridges.addAll(list); return this;}
	
	private List<TS> series; public List<TS> getSeries() {return series;}
	public EjbTimeSeriesQuery<CAT,SCOPE,TS,TX,BRIDGE,INTV,STAT> add(TS ts) {if(Objects.isNull(series)) {series = new ArrayList<>();} series.add(ts); return this;}
	
	private List<TX> transactions; public List<TX> getTransactions() {return transactions;}
	public EjbTimeSeriesQuery<CAT,SCOPE,TS,TX,BRIDGE,INTV,STAT> add(TX tx) {if(Objects.isNull(transactions)) {transactions = new ArrayList<>();} transactions.add(tx); return this;}
}