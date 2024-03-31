package org.jeesl.controller.facade.jx.module;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.jeesl.api.facade.module.JeeslTsFacade;
import org.jeesl.controller.facade.jx.JeeslFacadeBean;
import org.jeesl.controller.facade.jx.predicate.ParentPredicateBuilder;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.module.TsFactoryBuilder;
import org.jeesl.factory.ejb.module.ts.EjbTsFactory;
import org.jeesl.factory.ejb.util.EjbIdFactory;
import org.jeesl.factory.json.io.db.tuple.JsonTupleFactory;
import org.jeesl.factory.json.system.io.db.tuple.t1.Json1TuplesFactory;
import org.jeesl.factory.sql.module.SqlTimeSeriesFactory;
import org.jeesl.interfaces.facade.ParentPredicate;
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
import org.jeesl.model.json.io.db.tuple.instance.JsonTuple1;
import org.jeesl.util.query.ejb.module.EjbTimeSeriesQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslTsFacadeBean<L extends JeeslLang, D extends JeeslDescription,
							CAT extends JeeslTsCategory<L,D,CAT,?>,
							SCOPE extends JeeslTsScope<L,D,CAT,ST,UNIT,EC,INT>,
							ST extends JeeslTsScopeType<L,D,ST,?>,
							UNIT extends JeeslStatus<L,D,UNIT>,
							MP extends JeeslTsMultiPoint<L,D,SCOPE,UNIT>,
							TS extends JeeslTimeSeries<SCOPE,TS,BRIDGE,INT,STAT>,
							TX extends JeeslTsTransaction<SOURCE,DATA,USER,?>,
							SOURCE extends EjbWithLangDescription<L,D>,
							BRIDGE extends JeeslTsBridge<EC>,
							EC extends JeeslTsEntityClass<L,D,CAT,ENTITY>,
							ENTITY extends JeeslRevisionEntity<L,D,?,?,?,?>,
							INT extends JeeslTsInterval<L,D,INT,?>,
							STAT extends JeeslTsStatistic<L,D,STAT,?>,
							DATA extends JeeslTsData<TS,TX,SAMPLE,POINT,WS>,
							POINT extends JeeslTsDataPoint<DATA,MP>,
							SAMPLE extends JeeslTsSample,
							USER extends EjbWithId,
							WS extends JeeslStatus<L,D,WS>,
							QAF extends JeeslStatus<L,D,QAF>,
							CRON extends JeeslTsCron<SCOPE,INT,STAT>>
					extends JeeslFacadeBean
					implements JeeslTsFacade<L,D,CAT,SCOPE,ST,UNIT,MP,TS,TX,SOURCE,BRIDGE,EC,ENTITY,INT,STAT,DATA,POINT,SAMPLE,USER,WS,QAF,CRON>
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslTsFacadeBean.class);

	private final TsFactoryBuilder<L,D,?,CAT,SCOPE,ST,UNIT,MP,TS,TX,SOURCE,BRIDGE,EC,ENTITY,INT,STAT,DATA,POINT,SAMPLE,USER,WS,QAF,CRON> fbTs;

	private final EjbTsFactory<SCOPE,UNIT,TS,SOURCE,BRIDGE,EC,INT,STAT> efTs;
	private final SqlTimeSeriesFactory<TS,DATA> sqlFactory;

	public JeeslTsFacadeBean(EntityManager em, final TsFactoryBuilder<L,D,?,CAT,SCOPE,ST,UNIT,MP,TS,TX,SOURCE,BRIDGE,EC,ENTITY,INT,STAT,DATA,POINT,SAMPLE,USER,WS,QAF,CRON> fbTs)
	{
		super(em);
		this.fbTs=fbTs;

		efTs = fbTs.ejbTs();
		sqlFactory = new SqlTimeSeriesFactory<>(fbTs.getClassData());
	}
	
	@Override public List<SCOPE> fTsScopes(JeeslTimeSeriesQuery<CAT,SCOPE,TS,TX,BRIDGE,INT,STAT> query)
	{
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<SCOPE> cQ = cB.createQuery(fbTs.getClassScope());
		Root<SCOPE> root = cQ.from(fbTs.getClassScope());
		if(ObjectUtils.isNotEmpty(query.getRootFetches())) {cQ.distinct(true); for(String fetch : query.getRootFetches()) {root.fetch(fetch, JoinType.LEFT);}}
		
		if(ObjectUtils.isNotEmpty(query.getCategories()))
		{
			Path<CAT> pCategory = root.get(JeeslTsScope.Attributes.category.toString());
			predicates.add(pCategory.in(query.getCategories()));
		}
		
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.select(root);
		return em.createQuery(cQ).getResultList();
	}

//	@Override public List<SCOPE> findScopes(Class<SCOPE> cScope, Class<CAT> cCategory, List<CAT> categories, boolean showInvisibleScopes)
//	{
//		List<ParentPredicate<CAT>> ppCategory = ParentPredicate.createFromList(cCategory,"category",categories);
//		return allForOrParents(cScope,ppCategory);
//	}

	@Override public List<EC> findClasses(Class<EC> cClass, Class<CAT> cCategory, List<CAT> categories, boolean showInvisibleScopes)
	{
		List<ParentPredicate<CAT>> ppCategory = ParentPredicateBuilder.createFromList(cCategory,"category",categories);
		return allForOrParents(cClass,ppCategory);
	}

	@Override public <T extends EjbWithId> BRIDGE fcBridge(Class<BRIDGE> cBridge, EC entityClass, T ejb) throws JeeslConstraintViolationException
	{
		try {return fBridge(entityClass, ejb);}
		catch (JeeslNotFoundException ex)
		{
			BRIDGE bridge = fbTs.ejbBridge().build(entityClass, ejb.getId());
			return this.persist(bridge);
		}
	}
	@Override public <T extends EjbWithId> BRIDGE fBridge(EC ec, T ejb) throws JeeslNotFoundException
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<BRIDGE> cQ = cB.createQuery(fbTs.getClassBridge());
		Root<BRIDGE> from = cQ.from(fbTs.getClassBridge());

		Path<EC> pClass = from.get(JeeslTsBridge.Attributes.entityClass.toString());
		Path<Long> pRef = from.get(JeeslTsBridge.Attributes.refId.toString());

		CriteriaQuery<BRIDGE> select = cQ.select(from);
		select.where(cB.equal(pClass, ec),cB.equal(pRef, ejb.getId()));
		try	{return em.createQuery(select).getSingleResult();}
		catch (NoResultException ex){throw new JeeslNotFoundException("No "+fbTs.getClassBridge().getName()+" found for entityClass/refId");}
	}

	@Override public <T extends EjbWithId> List<BRIDGE> fBridges(EC ec, List<T> ejbs)
	{
		if(ejbs==null || ejbs.isEmpty()) {return new ArrayList<BRIDGE>();}
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<BRIDGE> cQ = cB.createQuery(fbTs.getClassBridge());
		Root<BRIDGE> from = cQ.from(fbTs.getClassBridge());

		Path<EC> pClass = from.get(JeeslTsBridge.Attributes.entityClass.toString());
		Path<Long> pRef = from.get(JeeslTsBridge.Attributes.refId.toString());

		CriteriaQuery<BRIDGE> select = cQ.select(from);
		select.where(cB.equal(pClass, ec),pRef.in(EjbIdFactory.toLongs(ejbs)));
		return em.createQuery(select).getResultList();
	}

	@Override public boolean isTimeSeriesAllowed(SCOPE scope, INT interval, EC c)
	{
		scope = this.find(fbTs.getClassScope(),scope);
		return scope.getIntervals().contains(interval) && scope.getClasses().contains(c);
	}

//	@Override public List<TS> fTimeSeries(List<BRIDGE> bridges)
//	{
//		List<Predicate> predicates = new ArrayList<Predicate>();
//		CriteriaBuilder cB = em.getCriteriaBuilder();
//		CriteriaQuery<TS> cQ = cB.createQuery(fbTs.getClassTs());
//
//		Root<TS> ts = cQ.from(fbTs.getClassTs());
//		Join<TS,BRIDGE> jBridge = ts.join(JeeslTimeSeries.Attributes.bridge.toString());
//
//		predicates.add(jBridge.in(bridges));
//
//		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
//		cQ.select(ts);
//		return em.createQuery(cQ).getResultList();
//	}

	@Override public List<TS> fTimeSeries(List<BRIDGE> bridges, List<SCOPE> scopes)
	{
		if(bridges==null || bridges.isEmpty()) {return new ArrayList<TS>();}
		if(scopes==null || scopes.isEmpty()) {return new ArrayList<TS>();}

		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<TS> cQ = cB.createQuery(fbTs.getClassTs());

		Root<TS> ts = cQ.from(fbTs.getClassTs());
		Join<TS,BRIDGE> jBridge = ts.join(JeeslTimeSeries.Attributes.bridge.toString());
		Join<TS,SCOPE> jScope = ts.join(JeeslTimeSeries.Attributes.scope.toString());

		predicates.add(jBridge.in(bridges));
		predicates.add(jScope.in(scopes));

		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.select(ts);
		return em.createQuery(cQ).getResultList();
	}

	@Override public List<TS> fTimeSeries(SCOPE scope, INT interval, EC entityClass)
	{
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<TS> cQ = cB.createQuery(fbTs.getClassTs());

		Root<TS> ts = cQ.from(fbTs.getClassTs());
		Join<TS,BRIDGE> jBridge = ts.join(JeeslTimeSeries.Attributes.bridge.toString());

		predicates.add(cB.equal(ts.<SCOPE>get(JeeslTimeSeries.Attributes.scope.toString()), scope));
		predicates.add(cB.equal(ts.<INT>get(JeeslTimeSeries.Attributes.interval.toString()), interval));
		predicates.add(cB.equal(jBridge.<EC>get(JeeslTsBridge.Attributes.entityClass.toString()), entityClass));

		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.select(ts);
		return em.createQuery(cQ).getResultList();
	}
	
	@Override public List<TS> fTimeSeries(JeeslTimeSeriesQuery<CAT,SCOPE,TS,TX,BRIDGE,INT,STAT> query)
	{
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<TS> cQ = cB.createQuery(fbTs.getClassTs());
		Root<TS> ts = cQ.from(fbTs.getClassTs());
		
		int activeFilters=0;
		if(Objects.nonNull(query.getBridges()) && !query.getBridges().isEmpty())
		{
			Path<BRIDGE> pBridge = ts.get(JeeslTimeSeries.Attributes.bridge.toString());
			predicates.add(pBridge.in(query.getBridges()));
			activeFilters++;
		}
		if(Objects.nonNull(query.getScopes()) && !query.getScopes().isEmpty())
		{
			Path<SCOPE> pScope = ts.get(JeeslTimeSeries.Attributes.scope.toString());
			predicates.add(pScope.in(query.getScopes()));
			activeFilters++;
		}
		if(ObjectUtils.isNotEmpty(query.getIntervals()))
		{
			Path<INT> pInterval = ts.get(JeeslTimeSeries.Attributes.interval.toString());
			predicates.add(pInterval.in(query.getIntervals()));
			activeFilters++;
		}
		if(activeFilters==0) {return new ArrayList<>();}
		
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.select(ts);
		return em.createQuery(cQ).getResultList();
	}

	@Override public TS fcTimeSeries(SCOPE scope, INT interval, STAT statistic, BRIDGE bridge) throws JeeslConstraintViolationException
	{
		if (!isTimeSeriesAllowed(scope,interval,bridge.getEntityClass()))
		{
			StringBuilder sb = new StringBuilder();
			sb.append("The requested time series combintaion of scope, interval and class ist not allowed. ");
			sb.append(" scope:"+scope.getCode());
			sb.append(" interval:"+interval.getCode());
			sb.append(" class:"+bridge.getEntityClass().getCode());
			throw new JeeslConstraintViolationException(sb.toString());
		}
		try {return fTimeSeries(scope,interval,statistic,bridge);}
		catch (JeeslNotFoundException e)
		{
			TS ts = efTs.build(scope,interval,statistic,bridge);
			return this.persist(ts);
		}
	}

	@Override public TS fTimeSeries(SCOPE scope, INT interval, STAT statistic, BRIDGE bridge) throws JeeslNotFoundException
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<TS> cQ = cB.createQuery(fbTs.getClassTs());
		Root<TS> ts = cQ.from(fbTs.getClassTs());

		Path<SCOPE> pScope = ts.get(JeeslTimeSeries.Attributes.scope.toString());
		Path<INT> pInterval = ts.get(JeeslTimeSeries.Attributes.interval.toString());
		Path<STAT> pStatistic = ts.get(JeeslTimeSeries.Attributes.statistic.toString());
		Path<BRIDGE> pBridge = ts.get(JeeslTimeSeries.Attributes.bridge.toString());

		CriteriaQuery<TS> select = cQ.select(ts);
		select.where(cB.equal(pScope,scope),cB.equal(pInterval,interval),cB.equal(pStatistic,statistic),cB.equal(pBridge, bridge));

		try	{return em.createQuery(select).getSingleResult();}
		catch (NoResultException ex){throw new JeeslNotFoundException("No "+fbTs.getClassTs().getName()+" found for scope/interval/bridge");}
	}
	
	@Override public DATA fDataLast(TS series) throws JeeslNotFoundException
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<DATA> cQ = cB.createQuery(fbTs.getClassData());
		Root<DATA> root = cQ.from(fbTs.getClassData());

		Path<TS> pSeries = root.get(JeeslTsData.Attributes.timeSeries.toString());
		Expression<Date> eRecord = root.get(JeeslTsData.Attributes.record.toString());
		
		cQ.where(cB.equal(pSeries,series));
		cQ.select(root);
		cQ.orderBy(cB.desc(eRecord));
		
		TypedQuery<DATA> q = em.createQuery(cQ);
		q.setMaxResults(1);
		try	{return q.getSingleResult();}
		catch (NoResultException ex){throw new JeeslNotFoundException("No "+ fbTs.getClassData()+" found for series="+series.toString());}
		catch (NonUniqueResultException ex){throw new JeeslNotFoundException("Results for "+fbTs.getClassData()+" and series="+series.toString()+" not unique");}
	}
	
	@Override public List<DATA> fData(JeeslTimeSeriesQuery<CAT,SCOPE,TS,TX,BRIDGE,INT,STAT> query)
	{
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<DATA> cQ = cB.createQuery(fbTs.getClassData());
		Root<DATA> ejb = cQ.from(fbTs.getClassData());
		Expression<Date> eRecord = ejb.get(JeeslTsData.Attributes.record.toString());
		
		if(ObjectUtils.isNotEmpty(query.getSeries()))
		{
			Path<TS> pSeries = ejb.get(JeeslTsData.Attributes.timeSeries.toString());
			predicates.add(pSeries.in(query.getSeries()));
		}
		
		cQ.select(ejb);
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.orderBy(cB.asc(eRecord));
		
		TypedQuery<DATA> tQ = em.createQuery(cQ);
		if(Objects.nonNull(query.getMaxResults())) {tQ.setMaxResults(query.getMaxResults());}
		return tQ.getResultList();
	}

	@Override
	public List<DATA> fData(TX transaction)
	{
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<DATA> cQ = cB.createQuery(fbTs.getClassData());
		Root<DATA> data = cQ.from(fbTs.getClassData());

		predicates.add(cB.equal(data.<TX>get(JeeslTsData.Attributes.transaction.toString()), transaction));
		Expression<Date> eRecord = data.get(JeeslTsData.Attributes.record.toString());

		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.select(data);
		cQ.orderBy(cB.asc(eRecord));

		return em.createQuery(cQ).getResultList();
	}

	@Override public List<DATA> fData(WS workspace, TS timeSeries){return fData(workspace,timeSeries,JeeslTsData.QueryInterval.closedOpen,null,null);}
	@Override public List<DATA> fData(WS workspace, TS timeSeries, JeeslTsData.QueryInterval interval, Date from, Date to)
	{
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<DATA> cQ = cB.createQuery(fbTs.getClassData());
		Root<DATA> data = cQ.from(fbTs.getClassData());

		predicates.add(cB.equal(data.<WS>get(JeeslTsData.Attributes.workspace.toString()), workspace));
		predicates.add(cB.equal(data.<TS>get(JeeslTsData.Attributes.timeSeries.toString()), timeSeries));

		Expression<Date> eRecord = data.get(JeeslTsData.Attributes.record.toString());
		if(from!=null)
		{
			switch(interval)
			{
				case closedOpen: predicates.add(cB.greaterThanOrEqualTo(eRecord,from)); break;
				case closedClosed: predicates.add(cB.greaterThanOrEqualTo(eRecord,from)); break;
			}
		}
		if(to!=null)
		{
			switch(interval)
			{
				case closedOpen: predicates.add(cB.lessThan(eRecord,to)); break;
				case closedClosed: predicates.add(cB.lessThanOrEqualTo(eRecord,to)); break;
			}

		}

		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.select(data);
		cQ.orderBy(cB.asc(eRecord));

		return em.createQuery(cQ).getResultList();
	}

	@Override
	public List<DATA> fData(WS workspace, TS timeSeries, int year)
	{
		//JeeslTsData.QueryInterval interval
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<DATA> cQ = cB.createQuery(fbTs.getClassData());
		Root<DATA> data = cQ.from(fbTs.getClassData());

		predicates.add(cB.equal(data.<WS>get(JeeslTsData.Attributes.workspace.toString()), workspace));
		predicates.add(cB.equal(data.<TS>get(JeeslTsData.Attributes.timeSeries.toString()), timeSeries));

		Expression<Date> eRecord = data.get(JeeslTsData.Attributes.record.toString());
		predicates.add(cB.equal(cB.function("YEAR", Integer.class, eRecord), year));

		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.select(data);
		cQ.orderBy(cB.asc(eRecord));

		return em.createQuery(cQ).getResultList();
	}

	@Override public List<DATA> fDataFirst(List<TS> list)
	{
		if(list==null || list.isEmpty()) {return new ArrayList<>();}
		List<Long> ids = this.listId(sqlFactory.firstData(list));
		return this.list(fbTs.getClassData(),ids);
	}
	@Override public List<DATA> fDataLast(List<TS> list)
	{
		if(list==null || list.isEmpty()) {return new ArrayList<>();}
		List<Long> ids = this.listId(sqlFactory.lastData(list));
		return this.list(fbTs.getClassData(),ids);
	}
	

	@Override public List<POINT> fPoints(WS workspace, TS timeSeries, JeeslTsData.QueryInterval interval, Date from, Date to)
	{
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<POINT> cQ = cB.createQuery(fbTs.getClassPoint());
		Root<POINT> point = cQ.from(fbTs.getClassPoint());

		Path<DATA> data = point.get(JeeslTsDataPoint.Attributes.data.toString());
		predicates.add(cB.equal(data.<WS>get(JeeslTsData.Attributes.workspace.toString()), workspace));
		predicates.add(cB.equal(data.<TS>get(JeeslTsData.Attributes.timeSeries.toString()), timeSeries));

		Expression<Date> eRecord = data.get(JeeslTsData.Attributes.record.toString());
		if(from!=null)
		{
			switch(interval)
			{
				case closedOpen: predicates.add(cB.greaterThanOrEqualTo(eRecord,from));break;
				case closedClosed: predicates.add(cB.greaterThanOrEqualTo(eRecord,from));break;
			}
		}
		if(to!=null)
		{
			switch(interval)
			{
				case closedOpen: predicates.add(cB.lessThan(eRecord,to));break;
				case closedClosed: predicates.add(cB.lessThanOrEqualTo(eRecord,to));break;
			}
		}

		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.select(point);
		cQ.orderBy(cB.asc(eRecord));

		return em.createQuery(cQ).getResultList();
	}
	
	@Override public List<POINT> fPoints(WS workspace, List<TS> timeSeries, List<MP> mps, JeeslTsData.QueryInterval interval, Date from, Date to)
	{
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<POINT> cQ = cB.createQuery(fbTs.getClassPoint());
		Root<POINT> point = cQ.from(fbTs.getClassPoint());

		Path<DATA> data = point.get(JeeslTsDataPoint.Attributes.data.toString());
		predicates.add(cB.equal(data.<WS>get(JeeslTsData.Attributes.workspace.toString()), workspace));
		
		Path<TS> pTs = data.get(JeeslTsData.Attributes.timeSeries.toString());
		predicates.add(pTs.in(timeSeries));

		Expression<Date> eRecord = data.get(JeeslTsData.Attributes.record.toString());
		if(from!=null)
		{
			switch(interval)
			{
				case closedOpen: predicates.add(cB.greaterThanOrEqualTo(eRecord,from));break;
				case closedClosed: predicates.add(cB.greaterThanOrEqualTo(eRecord,from));break;
			}
		}
		if(to!=null)
		{
			switch(interval)
			{
				case closedOpen: predicates.add(cB.lessThan(eRecord,to));break;
				case closedClosed: predicates.add(cB.lessThanOrEqualTo(eRecord,to));break;
			}
		}

		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.select(point);
		cQ.orderBy(cB.asc(eRecord));

		return em.createQuery(cQ).getResultList();
	}
	
	@Override
	public void deleteTsSeries(TS series) throws JeeslConstraintViolationException
	{
		EjbTimeSeriesQuery<CAT,SCOPE,TS,TX,BRIDGE,INT,STAT> qData = new EjbTimeSeriesQuery<>();
		qData.add(series);
		
		List<DATA> datas = this.fData(qData);
		logger.info(fbTs.getClassData()+": "+datas.size());
		
		Set<Long> setTxCount = new HashSet<>();
		List<TX> listTxDelete = new ArrayList<>();
		
		EjbTimeSeriesQuery<CAT,SCOPE,TS,TX,BRIDGE,INT,STAT> qTx = new EjbTimeSeriesQuery<>();
		for(DATA d : datas) {qTx.add(d.getTransaction());}
		
		logger.info(fbTs.getClassData()+": Deleting "+datas.size());
		this.rm(datas);
		
		
		for(JsonTuple1<TX> t : this.tpcTsDataByTx(qTx).getTuples()) {setTxCount.add(t.getId1());}
		logger.info(fbTs.getClassTransaction()+": remaining "+setTxCount.size());
		for(TX tx : qTx.getTransactions())
		{
			if(!setTxCount.contains(tx.getId())) {listTxDelete.add(tx);}
		}
		
		logger.info(fbTs.getClassTransaction()+": Deleting "+listTxDelete.size());
		this.rm(listTxDelete);
		
		logger.info(fbTs.getClassTs()+": Deleting "+series.toString());
		this.rm(series);
	}
	
	@Override
	public void deleteTransaction(TX transaction) throws JeeslConstraintViolationException
	{
		transaction = em.find(fbTs.getClassTransaction(), transaction.getId());
		this.rmProtected(transaction);
	}

	@Override
	public List<TX> fTransactions(List<USER> users, JeeslTsData.QueryInterval interval, Date from, Date to)
	{
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<TX> cQ = cB.createQuery(fbTs.getClassTransaction());
		Root<TX> data = cQ.from(fbTs.getClassTransaction());

		Expression<Date> eRecord = data.get(JeeslTsTransaction.Attributes.record.toString());
		if(from!=null)
		{
			switch(interval)
			{
				case closedOpen: predicates.add(cB.greaterThanOrEqualTo(eRecord,from)); break;
				case closedClosed: predicates.add(cB.greaterThanOrEqualTo(eRecord,from)); break;
			}

		}
		if(to!=null)
		{
			switch(interval)
			{
				case closedOpen: predicates.add(cB.lessThan(eRecord,to)); break;
				case closedClosed: predicates.add(cB.lessThanOrEqualTo(eRecord,to)); break;
			}
		}

		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.select(data);
		cQ.orderBy(cB.asc(eRecord));

		return em.createQuery(cQ).getResultList();
	}

	@Override public JsonTuples1<TS> tpcTsDataByTs(List<TS> series)
	{
		if(ObjectUtils.isEmpty(series)) {return new JsonTuples1<>();}
		Json1TuplesFactory<TS> jtf = Json1TuplesFactory.instance(fbTs.getClassTs()).facade(this);
		
		List<Tuple> tuples = new ArrayList<>();
		List<List<TS>> partitions = ListUtils.partition(series,30000);
		for(List<TS> partition : partitions)
		{
			List<Predicate> predicates = new ArrayList<Predicate>();
			CriteriaBuilder cB = em.getCriteriaBuilder();
			CriteriaQuery<Tuple> cQ = cB.createTupleQuery();
			Root<DATA> data = cQ.from(fbTs.getClassData());

			Path<TS> pTs = data.get(JeeslTsData.Attributes.timeSeries.toString());
			predicates.add(pTs.in(partition));
			
			Expression<Long> eCount = cB.count(data.<Long>get("id"));
			Join<DATA,TS> jTs = data.join(JeeslTsData.Attributes.timeSeries.toString());

			cQ.multiselect(jTs.get("id"),eCount);
			cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
			cQ.groupBy(jTs.get("id"));
			
			TypedQuery<Tuple> tQ = em.createQuery(cQ);
			tuples.addAll(tQ.getResultList());
		}	
		
        return jtf.buildV2(tuples,JsonTupleFactory.Type.count);
	}

	@Override public JsonTuples1<TX> tpcTsDataByTx(JeeslTimeSeriesQuery<CAT,SCOPE,TS,TX,BRIDGE,INT,STAT> query)
	{
		if(ObjectUtils.isEmpty(query.getTransactions()))
		{
			return new JsonTuples1<>();
		}
		
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cQ = cB.createTupleQuery();
		Root<DATA> ejb = cQ.from(fbTs.getClassData());
		
		Expression<Long> eCount = cB.count(ejb.<Long>get("id"));
		Path<TX> pTx = ejb.join(JeeslTsData.Attributes.transaction.toString());
		
		if(ObjectUtils.isNotEmpty(query.getTransactions()))
		{
			predicates.add(pTx.in(query.getTransactions()));
		}
		
		cQ.multiselect(pTx.get("id"),eCount);
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.groupBy(pTx.get("id"));
		
		Json1TuplesFactory<TX> jtf = Json1TuplesFactory.instance(fbTs.getClassTransaction()).tupleLoad(this,query.getTupleLoad());
		return jtf.buildV2(em.createQuery(cQ).getResultList(),JsonTupleFactory.Type.count);
	}
}