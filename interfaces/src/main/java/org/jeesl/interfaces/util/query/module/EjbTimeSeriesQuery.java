package org.jeesl.interfaces.util.query.module;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.jeesl.interfaces.model.module.ts.config.JeeslTsCategory;
import org.jeesl.interfaces.model.module.ts.core.JeeslTsScope;
import org.jeesl.interfaces.model.module.ts.data.JeeslTsBridge;
import org.jeesl.interfaces.model.module.ts.stat.JeeslTsStatistic;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.util.query.AbstractEjbQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbTimeSeriesQuery<CAT extends JeeslTsCategory<?,?,CAT,?>,
								SCOPE extends JeeslTsScope<?,?,CAT,?,?,?,INT>,
								BRIDGE extends JeeslTsBridge<?>,
								INT extends JeeslStatus<?,?,INT>,
								STAT extends JeeslTsStatistic<?,?,STAT,?>
>
			extends AbstractEjbQuery
{
	private static final long serialVersionUID = 1L;
	
	final static Logger logger = LoggerFactory.getLogger(EjbTimeSeriesQuery.class);
	
//	public static <CAT extends JeeslTsCategory<?,?,CAT,?>,
//					SCOPE extends JeeslTsScope<?,?,CAT,?,?,?,INT>,
//					BRIDGE extends JeeslTsBridge<?>,
//					INT extends JeeslStatus<?,?,INT>,
//					STAT extends JeeslTsStatistic<?,?,STAT,?>>
//		EjbTimeSeriesQuery<CAT,SCOPE,BRIDGE,INT,STAT> instance()
//	{
//		return new EjbTimeSeriesQuery<CAT,SCOPE,BRIDGE,INT,STAT>();
//	}
	public EjbTimeSeriesQuery()
	{       
		reset();
	}
	
	@Override public void reset()
	{
		bridges=null;
	}
	
	//Fetches
	public <E extends Enum<E>> EjbTimeSeriesQuery<CAT,SCOPE,BRIDGE,INT,STAT> addRootFetch(E e){if(rootFetches==null) {rootFetches = new ArrayList<>();} rootFetches.add(e.toString()); return this;}
	
	//LocalDate
	public EjbTimeSeriesQuery<CAT,SCOPE,BRIDGE,INT,STAT> ld1(LocalDate ld1) {this.ld1 = ld1; return this;}
	public EjbTimeSeriesQuery<CAT,SCOPE,BRIDGE,INT,STAT> ld2(LocalDate ld2) {this.ld2 = ld2; return this;}
	public EjbTimeSeriesQuery<CAT,SCOPE,BRIDGE,INT,STAT> ld3(LocalDate ld3) {this.ld3 = ld3; return this;}
	
	private List<CAT> categories; public List<CAT> getCategories() {return categories;}
	public EjbTimeSeriesQuery<CAT,SCOPE,BRIDGE,INT,STAT> add(CAT category) {if(Objects.isNull(categories)) {categories = new ArrayList<>();} categories.add(category); return this;}
	public EjbTimeSeriesQuery<CAT,SCOPE,BRIDGE,INT,STAT> addCategories(List<CAT> list) {if(Objects.isNull(categories)) {categories = new ArrayList<>();} categories.addAll(list); return this;}
	
	private List<SCOPE> scopes; public List<SCOPE> getScopes() {return scopes;}
	public EjbTimeSeriesQuery<CAT,SCOPE,BRIDGE,INT,STAT> add(SCOPE scope) {if(Objects.isNull(scopes)) {scopes = new ArrayList<>();} scopes.add(scope); return this;}

	private List<BRIDGE> bridges; public List<BRIDGE> getBridges() {return bridges;}
	public EjbTimeSeriesQuery<CAT,SCOPE,BRIDGE,INT,STAT> add(BRIDGE bridge) {if(Objects.isNull(bridges)) {bridges = new ArrayList<>();} bridges.add(bridge); return this;}
	public EjbTimeSeriesQuery<CAT,SCOPE,BRIDGE,INT,STAT> addBridges(List<BRIDGE> list) {if(Objects.isNull(bridges)) {bridges = new ArrayList<>();} bridges.addAll(list); return this;}
}