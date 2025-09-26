package org.jeesl.controller.facade.jx.module;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
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
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.exlp.util.system.DateUtil;
import org.jeesl.api.facade.module.JeeslTsFacade;
import org.jeesl.controller.facade.jx.JeeslFacadeBean;
import org.jeesl.controller.facade.jx.predicate.BooleanPredicateBuilder;
import org.jeesl.controller.facade.jx.predicate.DatePredicateBuilder;
import org.jeesl.controller.facade.jx.predicate.DoublePredicateBuilder;
import org.jeesl.controller.facade.jx.predicate.LiteralPredicateBuilder;
import org.jeesl.controller.facade.jx.predicate.ParentPredicateBuilder;
import org.jeesl.controller.facade.jx.predicate.SortByPredicateBuilder;
import org.jeesl.controller.facade.jx.predicate.TimePredicateBuilder;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.module.TsFactoryBuilder;
import org.jeesl.factory.ejb.module.ts.EjbTsFactory;
import org.jeesl.factory.ejb.util.EjbIdFactory;
import org.jeesl.factory.json.io.db.tuple.JsonTupleFactory;
import org.jeesl.factory.json.system.io.db.tuple.t1.Json1TuplesFactory;
import org.jeesl.factory.sql.SqlFactory;
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
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLangDescription;
import org.jeesl.interfaces.util.query.module.JeeslTimeSeriesQuery;
import org.jeesl.model.ejb.io.db.JeeslCqBoolean;
import org.jeesl.model.ejb.io.db.JeeslCqDate;
import org.jeesl.model.ejb.io.db.JeeslCqDouble;
import org.jeesl.model.ejb.io.db.JeeslCqLiteral;
import org.jeesl.model.ejb.io.db.JeeslCqOrdering;
import org.jeesl.model.ejb.io.db.JeeslCqTime;
import org.jeesl.model.json.io.db.tuple.container.JsonTuples1;
import org.jeesl.model.json.io.db.tuple.instance.JsonTuple1;
import org.jeesl.model.json.module.ts.data.JsonTsAggegation;
import org.jeesl.util.query.cq.CqBool;
import org.jeesl.util.query.cq.CqDate;
import org.jeesl.util.query.cq.CqLiteral;
import org.jeesl.util.query.cq.CqOrdering;
import org.jeesl.util.query.ejb.module.EjbTimeSeriesQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslTsFacadeBean<CAT extends JeeslTsCategory<?,?,CAT,?>,
							SCOPE extends JeeslTsScope<?,?,CAT,ST,UNIT,EC,INT>,
							ST extends JeeslTsScopeType<?,?,ST,?>,
							UNIT extends JeeslStatus<?,?,UNIT>,
							MP extends JeeslTsMultiPoint<?,?,SCOPE,UNIT>,
							TS extends JeeslTimeSeries<SCOPE,TS,BRIDGE,INT,STAT>,
							TX extends JeeslTsTransaction<SOURCE,DATA,USER,?>,
							SOURCE extends EjbWithLangDescription<?,?>,
							BRIDGE extends JeeslTsBridge<EC>,
							EC extends JeeslTsEntityClass<?,?,CAT,ENTITY>,
							ENTITY extends JeeslRevisionEntity<?,?,?,?,?,?>,
							INT extends JeeslTsInterval<?,?,INT,?>,
							STAT extends JeeslTsStatistic<?,?,STAT,?>,
							DATA extends JeeslTsData<TS,TX,SAMPLE,POINT,WS>,
							POINT extends JeeslTsDataPoint<DATA,MP>,
							SAMPLE extends JeeslTsSample,
							USER extends EjbWithId,
							WS extends JeeslStatus<?,?,WS>,
							CRON extends JeeslTsCron<SCOPE,INT,STAT>>
					extends JeeslFacadeBean
					implements JeeslTsFacade<CAT,SCOPE,ST,UNIT,MP,TS,TX,SOURCE,BRIDGE,EC,ENTITY,INT,STAT,DATA,POINT,SAMPLE,USER,WS,CRON>
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslTsFacadeBean.class);

	private final TsFactoryBuilder<?,?,?,CAT,SCOPE,ST,UNIT,MP,TS,TX,SOURCE,BRIDGE,EC,ENTITY,INT,STAT,DATA,POINT,SAMPLE,USER,WS,?,CRON> fbTs;

	private final EjbTsFactory<SCOPE,TS,BRIDGE,INT,STAT> efTs;
	private final SqlTimeSeriesFactory<TS,DATA> sqlFactory;

	public JeeslTsFacadeBean(EntityManager em, final TsFactoryBuilder<?,?,?,CAT,SCOPE,ST,UNIT,MP,TS,TX,SOURCE,BRIDGE,EC,ENTITY,INT,STAT,DATA,POINT,SAMPLE,USER,WS,?,CRON> fbTs)
	{
		super(em);
		this.fbTs=fbTs;

		efTs = fbTs.ejbTs();
		sqlFactory = new SqlTimeSeriesFactory<>(fbTs.getClassData());
	}
	
	@Override public List<SCOPE> fTsScopes(JeeslTimeSeriesQuery<CAT,SCOPE,MP,TS,TX,BRIDGE,INT,STAT> query)
	{
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<SCOPE> cQ = cB.createQuery(fbTs.getClassScope());
		Root<SCOPE> root = cQ.from(fbTs.getClassScope());
		if(ObjectUtils.isNotEmpty(query.getRootFetches())) {cQ.distinct(true); for(String fetch : query.getRootFetches()) {root.fetch(fetch, JoinType.LEFT);}}
		
		if(ObjectUtils.isNotEmpty(query.getTsCategories()))
		{
			Path<CAT> pCategory = root.get(JeeslTsScope.Attributes.category.toString());
			predicates.add(pCategory.in(query.getTsCategories()));
		}
		
		cQ.select(root);
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		
		return em.createQuery(cQ).getResultList();
	}

	@Override public List<MP> fTsMultiPoints(JeeslTimeSeriesQuery<CAT,SCOPE,MP,TS,TX,BRIDGE,INT,STAT> query)
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<MP> cQ = cB.createQuery(fbTs.getClassMp());
		Root<MP> root = cQ.from(fbTs.getClassMp());
		
		cQ.select(root);
		cQ.where(cB.and(this.pMultiPoint(cB,query,root)));
		this.obMultiPoint(cB,cQ,query,root);
		
		TypedQuery<MP> tQ = em.createQuery(cQ);
		super.pagination(tQ, query);
		return tQ.getResultList();
	}

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
	
	@Override public List<TS> fTsSeries(JeeslTimeSeriesQuery<CAT,SCOPE,MP,TS,TX,BRIDGE,INT,STAT> query)
	{
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<TS> cQ = cB.createQuery(fbTs.getClassTs());
		Root<TS> ts = cQ.from(fbTs.getClassTs());
		
		int activeFilters=0;
		if(ObjectUtils.isNotEmpty(query.getTsBridges()))
		{
			Path<BRIDGE> pBridge = ts.get(JeeslTimeSeries.Attributes.bridge.toString());
			predicates.add(pBridge.in(query.getTsBridges()));
			activeFilters++;
		}
		if(ObjectUtils.isNotEmpty(query.getTsScopes()))
		{
			Path<SCOPE> pScope = ts.get(JeeslTimeSeries.Attributes.scope.toString());
			predicates.add(pScope.in(query.getTsScopes()));
			activeFilters++;
		}
		if(ObjectUtils.isNotEmpty(query.getTsIntervals()))
		{
			Path<INT> pInterval = ts.get(JeeslTimeSeries.Attributes.interval.toString());
			predicates.add(pInterval.in(query.getTsIntervals()));
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
	
	@Override public List<DATA> fTsData(JeeslTimeSeriesQuery<CAT,SCOPE,MP,TS,TX,BRIDGE,INT,STAT> query)
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<DATA> cQ = cB.createQuery(fbTs.getClassData());
		Root<DATA> root = cQ.from(fbTs.getClassData());
		super.rootFetch(root, query);
		super.distinct(cQ,query);
		
		cQ.select(root);
		cQ.where(cB.and(this.pData(cB, query, root)));
		
		if(ObjectUtils.isEmpty(query.getCqOrderings()))
		{
			Expression<Date> eRecord = root.get(JeeslTsData.Attributes.record.toString());
			cQ.orderBy(cB.asc(eRecord));
		}
		else {this.obData(cB, cQ, query, root);}
		
		TypedQuery<DATA> tQ = em.createQuery(cQ);
		super.pagination(tQ, query);
		return tQ.getResultList();
	}
	
	@Override public List<DATA> fTsDataLatestOfDay(JeeslTimeSeriesQuery<CAT,SCOPE,MP,TS,TX,BRIDGE,INT,STAT> query)
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<DATA> cQ = cB.createQuery(fbTs.getClassData());
		Root<DATA> root = cQ.from(fbTs.getClassData());
		List<Predicate> pRoot = new ArrayList<>();
		
		List<Predicate> pSub = new ArrayList<Predicate>();
		Subquery<Date> sQ = cQ.subquery(Date.class);
		Root<DATA> sub = sQ.from(fbTs.getClassData());
		
		Expression<Date> eRootDate = root.get("record");
		Expression<Date> eSubDate = sub.get("record");
		
		Expression<Date> truncatedDayMain = cB.function("date_trunc", Date.class, cB.literal("day"), eRootDate);
		Expression<Date> truncatedDaySub = cB.function("date_trunc", Date.class, cB.literal("day"), eSubDate);
		
		pSub.addAll(Arrays.asList(this.pData(cB, query, sub)));
		pSub.add(cB.equal(truncatedDayMain,truncatedDaySub));
		
		sQ.select(cB.greatest(eSubDate));
		sQ.where(cB.and(pSub.toArray(new Predicate[pSub.size()])));
		
		pRoot.add(eRootDate.in(sQ));
		pRoot.addAll(Arrays.asList(this.pData(cB, query, root)));
		cQ.select(root);
		cQ.where(cB.and(pRoot.toArray(new Predicate[pRoot.size()])));
		
		if(ObjectUtils.isEmpty(query.getCqOrderings())) {cQ.orderBy(cB.asc(eRootDate));}
		else {this.obData(cB, cQ, query, root);}
		
		TypedQuery<DATA> tQ = em.createQuery(cQ);
		super.pagination(tQ, query);
		return tQ.getResultList();
	}
	
	@Override public List<DATA> fTsDataExtrema(JeeslTimeSeriesQuery<CAT,SCOPE,MP,TS,TX,BRIDGE,INT,STAT> query, JeeslTsFacade.Extrema aggegation, JeeslTsInterval.Aggregation interval)
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<DATA> cQ = cB.createQuery(fbTs.getClassData());
		Root<DATA> root = cQ.from(fbTs.getClassData());
		List<Predicate> pRoot = new ArrayList<>();
		
		List<Predicate> pSub = new ArrayList<Predicate>();
		Subquery<Double> sQ = cQ.subquery(Double.class);
		Root<DATA> sub = sQ.from(fbTs.getClassData());
		
		Expression<Date> eRootDate = root.get("record");
		Expression<Date> eSubDate = sub.get("record");
		
		Expression<Double> eRootValue = root.get("value");
		Expression<Double> eSubValue = sub.get("value");
		
		Expression<Date> truncatedDayRoot = cB.function("date_trunc", Date.class, cB.literal("day"), eRootDate);
		Expression<Date> truncatedDaySub = cB.function("date_trunc", Date.class, cB.literal("day"), eSubDate);
		
		pSub.addAll(Arrays.asList(this.pData(cB,query,sub)));
		pSub.add(cB.equal(truncatedDayRoot,truncatedDaySub));
		
		switch(aggegation)
		{
			case min: sQ.select(cB.min(eSubValue)); break;
			case max: sQ.select(cB.greatest(eSubValue)); break;
		}
		
		sQ.where(cB.and(pSub.toArray(new Predicate[pSub.size()])));
		
		pRoot.add(eRootValue.in(sQ));
		pRoot.addAll(Arrays.asList(this.pData(cB, query, root)));
		cQ.select(root);
		cQ.where(cB.and(pRoot.toArray(new Predicate[pRoot.size()])));
		
		if(ObjectUtils.isEmpty(query.getCqOrderings())) {cQ.orderBy(cB.asc(eRootDate));}
		else {this.obData(cB, cQ, query, root);}
		
		TypedQuery<DATA> tQ = em.createQuery(cQ);
		super.pagination(tQ, query);
		return tQ.getResultList();
	}
	
	@Override public List<JsonTsAggegation> fTsDataAggregation(JeeslTimeSeriesQuery<CAT,SCOPE,MP,TS,TX,BRIDGE,INT,STAT> query, JeeslTsFacade.Extrema aggegation, JeeslTsInterval.Aggregation interval)
	{
		Class<DATA> cData = fbTs.getClassData();
		
		SqlFactory sql = SqlFactory.instance();
		sql.alias(cData, "ts");
		sql.id(cData, JeeslTsData.Attributes.timeSeries);
		
		String truc = sql.dateTrunc(cData, JeeslTsData.Attributes.record, JeeslTsInterval.Aggregation.hour);

		sql.select(cData,JeeslTsData.Attributes.timeSeries).select(truc).select("avg(ts.value)");
		sql.from(cData);
		if(ObjectUtils.isNotEmpty(query.getTsSeries())) {sql.whereIn(cData, JeeslTsData.Attributes.timeSeries, query.getTsSeries());}
		
		sql.where(cData, JeeslTsData.Attributes.record, JeeslCqDate.Type.AtOrAfter, LocalDate.of(2025,1,1));
//		sql.where(TsData.class, JeeslTsData.Attributes.record, JeeslCqDate.Type.Before, LocalDate.of(2025,1,3));
		sql.group(cData, JeeslTsData.Attributes.timeSeries).group(truc);
		sql.limit(100);
		logger.info(sql.assemble());
		
		@SuppressWarnings("unchecked")
		List<Object[]> result = em.createNativeQuery(sql.assemble()).getResultList();
	
		List<JsonTsAggegation> list = new ArrayList<>();
		for(Object[] o : result)
		{
			logger.info(o[0].getClass().getName()+" "+o[1].getClass().getName()+" "+o[2].getClass().getName());
			
			JsonTsAggegation json = new JsonTsAggegation();
			json.setSeriesId(((BigInteger)o[0]).longValue());
			
			
			
			if(o.length>1 && Objects.nonNull(o[1])) {json.setRecord(DateUtil.toLocalDateTime((java.sql.Timestamp)o[1]));}
			if(o.length>2 && Objects.nonNull(o[2])) {json.setValue((Double)o[2]);}
			list.add(json);
		}
		return list;
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
	
	@Override public List<POINT> fTsPoints(JeeslTimeSeriesQuery<CAT,SCOPE,MP,TS,TX,BRIDGE,INT,STAT> query)
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<POINT> cQ = cB.createQuery(fbTs.getClassPoint());
		Root<POINT> root = cQ.from(fbTs.getClassPoint());
		
		Path<DATA> pData = root.get(JeeslTsDataPoint.Attributes.data.toString());
		Expression<Date> eRecord = pData.get(JeeslTsData.Attributes.record.toString());
		
		cQ.select(root);
		cQ.where(cB.and(this.pPoint(cB, query, root)));
		cQ.orderBy(cB.asc(eRecord));
		
		TypedQuery<POINT> tQ = em.createQuery(cQ);
		super.pagination(tQ, query);
		return tQ.getResultList();
	}
	
	@Override public List<POINT> fTsPoints(JeeslTimeSeriesQuery<CAT,SCOPE,MP,TS,TX,BRIDGE,INT,STAT> query, JeeslTsFacade.Extrema aggegation, JeeslTsInterval.Aggregation interval)
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<POINT> cQ = cB.createQuery(fbTs.getClassPoint());
		Root<POINT> root = cQ.from(fbTs.getClassPoint());
		List<Predicate> pRoot = new ArrayList<>();
		
		List<Predicate> pSub = new ArrayList<>();
		Subquery<Double> sQ = cQ.subquery(Double.class);
		Root<POINT> sub = sQ.from(fbTs.getClassPoint());
		
		Expression<Date> eRootDate = root.get(JeeslTsDataPoint.Attributes.data.toString()).get("record");
		Expression<Date> eSubDate = sub.get(JeeslTsDataPoint.Attributes.data.toString()).get("record");
		
		Expression<Double> eRootValue = root.get("value");
		Expression<Double> eSubValue = sub.get("value");
		
		Expression<Date> truncatedDayRoot = cB.function("date_trunc", Date.class, cB.literal("day"), eRootDate);
		Expression<Date> truncatedDaySub = cB.function("date_trunc", Date.class, cB.literal("day"), eSubDate);
		
		pSub.addAll(Arrays.asList(this.pPoint(cB,query,sub)));
		pSub.add(cB.equal(truncatedDayRoot,truncatedDaySub));
		
		switch(aggegation)
		{
			case min: sQ.select(cB.min(eSubValue)); break;
			case max: sQ.select(cB.greatest(eSubValue)); break;
//			case avg: sQ.select(cB.avg(eSubValue)); break;
		}
		
		sQ.where(cB.and(pSub.toArray(new Predicate[pSub.size()])));
		
		pRoot.add(eRootValue.in(sQ));
		pRoot.addAll(Arrays.asList(this.pPoint(cB, query, root)));
		cQ.select(root);
		cQ.where(cB.and(pRoot.toArray(new Predicate[pRoot.size()])));
		
//		if(ObjectUtils.isEmpty(query.getCqOrderings())) {cQ.orderBy(cB.asc(eRootDate));}
//		else {this.obData(cB, cQ, query, root);}
		
		TypedQuery<POINT> tQ = em.createQuery(cQ);
		super.pagination(tQ, query);
		return tQ.getResultList();
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
		EjbTimeSeriesQuery<CAT,SCOPE,MP,TS,TX,BRIDGE,INT,STAT> qData = new EjbTimeSeriesQuery<>();
		qData.add(series);
		
		List<DATA> datas = this.fTsData(qData);
		logger.info(fbTs.getClassData()+": "+datas.size());
		
		Set<Long> setTxCount = new HashSet<>();
		List<TX> listTxDelete = new ArrayList<>();
		
		EjbTimeSeriesQuery<CAT,SCOPE,MP,TS,TX,BRIDGE,INT,STAT> qTx = new EjbTimeSeriesQuery<>();
		for(DATA d : datas) {qTx.add(d.getTransaction());}
		
		logger.info(fbTs.getClassData()+": Deleting "+datas.size());
		this.rm(datas);
		
		
		for(JsonTuple1<TX> t : this.tpcTsDataByTx(qTx).getTuples()) {setTxCount.add(t.getId1());}
		logger.info(fbTs.getClassTransaction()+": remaining "+setTxCount.size());
		for(TX tx : qTx.getTsTransactions())
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

	@Override public List<TX> fTransactions(List<USER> users, JeeslTsData.QueryInterval interval, Date from, Date to)
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
	@Override public List<TX> fTsTransactions(JeeslTimeSeriesQuery<CAT,SCOPE,MP,TS,TX,BRIDGE,INT,STAT> query)
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<TX> cQ = cB.createQuery(fbTs.getClassTransaction());
		Root<TX> root = cQ.from(fbTs.getClassTransaction());
		
		Predicate[] p = this.pTransaction(cB, query, root);
		if(super.abort(query,p)) {return new ArrayList<TX>();}
		
		cQ.select(root);
		cQ.where(cB.and(p));	
		this.obTransaction(cB,cQ,query,root);
		
		TypedQuery<TX> tQ = em.createQuery(cQ);
		super.pagination(tQ,query);
		return tQ.getResultList();
	}

	@Override public JsonTuples1<TS> tpTsDataByTs(JeeslTimeSeriesQuery<CAT,SCOPE,MP,TS,TX,BRIDGE,INT,STAT> query)
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cQ = cB.createTupleQuery();
		Root<DATA> root = cQ.from(fbTs.getClassData());
		
		Expression<Long> eCount = cB.count(root.<Long>get("id"));
		Path<TS> pTs = root.join(JeeslTsData.Attributes.timeSeries.toString());
		
		cQ.multiselect(pTs.get("id"),eCount);
		cQ.where(cB.and(this.pData(cB,query,root)));
		cQ.groupBy(pTs.get("id"));
		
		Json1TuplesFactory<TS> jtf = Json1TuplesFactory.instance(fbTs.getClassTs()).tupleLoad(this,query.getTupleLoad());
		return jtf.buildV2(em.createQuery(cQ).getResultList(),JsonTupleFactory.Type.count);
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

	@Override public JsonTuples1<TX> tpcTsDataByTx(JeeslTimeSeriesQuery<CAT,SCOPE,MP,TS,TX,BRIDGE,INT,STAT> query)
	{
		if(ObjectUtils.isEmpty(query.getTsTransactions()))
		{
			return new JsonTuples1<>();
		}
		
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cQ = cB.createTupleQuery();
		Root<DATA> root = cQ.from(fbTs.getClassData());
		
		Expression<Long> eCount = cB.count(root.<Long>get("id"));
		Path<TX> pTx = root.get(JeeslTsData.Attributes.transaction.toString());
		
		cQ.multiselect(pTx.get("id"),eCount);
		cQ.where(cB.and(this.pData(cB,query,root)));
		cQ.groupBy(pTx.get("id"));
		
		Json1TuplesFactory<TX> jtf = Json1TuplesFactory.instance(fbTs.getClassTransaction()).tupleLoad(this,query.getTupleLoad());
		return jtf.buildV2(em.createQuery(cQ).getResultList(),JsonTupleFactory.Type.count);
	}
	
	// Predicates
	
	private Predicate[] pTransaction(CriteriaBuilder cB, JeeslTimeSeriesQuery<CAT,SCOPE,MP,TS,TX,BRIDGE,INT,STAT> query, Root<TX> root)
	{
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		for(JeeslCqDate cq : ListUtils.emptyIfNull(query.getCqDates()))
		{
			if(cq.getPath().equals(CqDate.path(JeeslTsData.Attributes.record)))
			{
				Expression<Date> eDate = root.get(JeeslTsData.Attributes.record.toString());
				DatePredicateBuilder.juDate(cB,predicates, cq, eDate);
			}
			else {logger.warn(cq.nyi(JeeslTsData.class));}
		}
		for(JeeslCqTime cq : ListUtils.emptyIfNull(query.getCqTimes()))
		{
			if(cq.getPath().equals(CqDate.path(JeeslTsTransaction.Attributes.record)))
			{
				Expression<Date> eDate = root.get(JeeslTsTransaction.Attributes.record.toString());
				TimePredicateBuilder.juDate(cB,predicates, cq, eDate);
			}
			else {logger.warn(cq.nyi(JeeslTsData.class));}
		}
		
		if(ObjectUtils.isNotEmpty(query.getTsSeries()))
		{
			Join<DATA,TS> jTs = root.join(JeeslTsData.Attributes.timeSeries.toString());
			predicates.add(jTs.in(query.getTsSeries()));
		}
		if(ObjectUtils.isNotEmpty(query.getTsTransactions()))
		{
			Path<TX> pTx = root.get(JeeslTsData.Attributes.transaction.toString());
			predicates.add(pTx.in(query.getTsTransactions()));
		}
		
		return predicates.toArray(new Predicate[predicates.size()]);
	}
	private void obTransaction(CriteriaBuilder cB, CriteriaQuery<TX> cQ, JeeslTimeSeriesQuery<CAT,SCOPE,MP,TS,TX,BRIDGE,INT,STAT> query, Root<TX> ejb)
	{
		List<Order> orders = new ArrayList<>();
		for(JeeslCqOrdering cq : ListUtils.emptyIfNull(query.getCqOrderings()))
		{
			if(cq.getPath().equals(CqOrdering.path(JeeslTsTransaction.Attributes.record)))
			{
				Expression<Date> e = ejb.get(JeeslTsTransaction.Attributes.record.toString());
				SortByPredicateBuilder.juDate(cB, orders, cq, e);
			}
			else {logger.warn("No Handling for "+cq.toString());}
		}
		if(!orders.isEmpty()) {cQ.orderBy(orders);}
	}
	
	//TODO @tk This is a workaround for testing avg, return to private
	public Predicate[] pData(CriteriaBuilder cB, JeeslTimeSeriesQuery<CAT,SCOPE,MP,TS,TX,BRIDGE,INT,STAT> query, Root<DATA> root)
	{
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		if(ObjectUtils.isNotEmpty(query.getTsSeries()))
		{
			Join<DATA,TS> jTs = root.join(JeeslTsData.Attributes.timeSeries.toString());
			predicates.add(jTs.in(query.getTsSeries()));
		}
		if(ObjectUtils.isNotEmpty(query.getTsTransactions()))
		{
			Path<TX> pTx = root.get(JeeslTsData.Attributes.transaction.toString());
			predicates.add(pTx.in(query.getTsTransactions()));
		}
		if(ObjectUtils.isNotEmpty(query.getTsMultiPoints()))
		{
			Join<DATA,POINT> jPoint = root.joinList(JeeslTsData.Attributes.dataPoints.toString());
			Join<POINT,MP> jMp = jPoint.join(JeeslTsDataPoint.Attributes.multiPoint.toString());
			predicates.add(jMp.in(query.getTsMultiPoints()));
		}
		
		for(JeeslCqDouble cq : ListUtils.emptyIfNull(query.getCqDouble()))
		{
			Expression<Double> e = null;
			if(cq.getPath().equals(CqDate.path(JeeslTsData.Attributes.value))) {e = root.<Double>get(JeeslTsData.Attributes.value.toString());}
			if(Objects.nonNull(e)) {DoublePredicateBuilder.add(cB,predicates, cq,e);}
			else {logger.warn(cq.nyi(JeeslTsData.class));}
		}
		for(JeeslCqDate cq : ListUtils.emptyIfNull(query.getCqDates()))
		{
			if(cq.getPath().equals(CqDate.path(JeeslTsData.Attributes.record)))
			{
				DatePredicateBuilder.juDate(cB,predicates, cq, root.<Date>get(JeeslTsData.Attributes.record.toString()));
			}
			else {logger.warn(cq.nyi(JeeslTsData.class));}
		}
		for(JeeslCqTime cq : ListUtils.emptyIfNull(query.getCqTimes()))
		{
			Expression<Date> eDate = null;
			if(cq.getPath().equals(CqDate.path(JeeslTsData.Attributes.record)))
			{
				eDate = root.get(JeeslTsData.Attributes.record.toString());
			}
			if(Objects.nonNull(eDate)) {TimePredicateBuilder.juDate(cB,predicates, cq, eDate);}
			else {logger.warn(cq.nyi(JeeslTsData.class));}
		}
		
		return predicates.toArray(new Predicate[predicates.size()]);
	}
	
	//TODO @tk This is a workaround for testing avg, return to private
	public void obData(CriteriaBuilder cB, CriteriaQuery<DATA> cQ, JeeslTimeSeriesQuery<CAT,SCOPE,MP,TS,TX,BRIDGE,INT,STAT> query, Root<DATA> root)
	{
		List<Order> orders = new ArrayList<>();
		for(JeeslCqOrdering cq : ListUtils.emptyIfNull(query.getCqOrderings()))
		{
			if(cq.getPath().equals(CqOrdering.path(JeeslTsData.Attributes.record)))
			{
				SortByPredicateBuilder.juDate(cB,orders,cq,root.<Date>get(JeeslTsData.Attributes.record.toString()));
			}
			else if(cq.getPath().equals(CqOrdering.path(JeeslTsData.Attributes.timeSeries,JeeslTimeSeries.Attributes.id)))
			{
				Path<TS> pTs = root.get(JeeslTsData.Attributes.timeSeries.toString());
				SortByPredicateBuilder.addByLong(cB,orders,cq,pTs.<Long>get(JeeslTimeSeries.Attributes.id.toString()));
			}
			else {logger.warn(cq.nyi(fbTs.getClassData()));}
		}
		if(!orders.isEmpty()) {cQ.orderBy(orders);}
	}
	
	private Predicate[] pPoint(CriteriaBuilder cB, JeeslTimeSeriesQuery<CAT,SCOPE,MP,TS,TX,BRIDGE,INT,STAT> query, Root<POINT> root)
	{
		List<Predicate> predicates = new ArrayList<Predicate>();
		Join<POINT,DATA> jData = root.join(JeeslTsDataPoint.Attributes.data.toString());

		for(JeeslCqDate cq : ListUtils.emptyIfNull(query.getCqDates()))
		{
			Expression<Date> eDate = null;
			if(cq.getPath().equals(CqDate.path(JeeslTsDataPoint.Attributes.data,JeeslTsData.Attributes.record)))
			{
				eDate = jData.get(JeeslTsData.Attributes.record.toString());
			}
			
			if(Objects.nonNull(eDate)) {DatePredicateBuilder.juDate(cB,predicates, cq, eDate);}
			else {logger.warn(cq.nyi(JeeslTsData.class));}
		}
		for(JeeslCqTime cq : ListUtils.emptyIfNull(query.getCqTimes()))
		{
			Expression<Date> eDate = null;
			if(cq.getPath().equals(CqDate.path(JeeslTsDataPoint.Attributes.data,JeeslTsData.Attributes.record)))
			{
				eDate = jData.get(JeeslTsData.Attributes.record.toString());
			}
			
			if(Objects.nonNull(eDate)) {TimePredicateBuilder.juDate(cB,predicates, cq, eDate);}
			else {logger.warn(cq.nyi(JeeslTsData.class));}
		}
		
		if(ObjectUtils.isNotEmpty(query.getTsMultiPoints()))
		{
			Join<POINT,MP> jMp = root.join(JeeslTsDataPoint.Attributes.multiPoint.toString());
			predicates.add(jMp.in(query.getTsMultiPoints()));
		}
		if(ObjectUtils.isNotEmpty(query.getTsSeries()))
		{
			Join<DATA,TS> jTs = jData.join(JeeslTsData.Attributes.timeSeries.toString());
			predicates.add(jTs.in(query.getTsSeries()));
		}
		if(ObjectUtils.isNotEmpty(query.getTsTransactions()))
		{
			Path<TX> pTx = jData.get(JeeslTsData.Attributes.transaction.toString());
			predicates.add(pTx.in(query.getTsTransactions()));
		}
		
		return predicates.toArray(new Predicate[predicates.size()]);
	}
	
	private Predicate[] pMultiPoint(CriteriaBuilder cB, JeeslTimeSeriesQuery<CAT,SCOPE,MP,TS,TX,BRIDGE,INT,STAT> query, Root<MP> root)
	{
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		if(ObjectUtils.isNotEmpty(query.getTsMultiPoints())) {predicates.add(root.in(query.getTsMultiPoints()));}
		
		if(ObjectUtils.isNotEmpty(query.getTsScopes()))
		{
			Path<SCOPE> pScope = root.get(JeeslTsMultiPoint.Attributes.scope.toString());
			predicates.add(pScope.in(query.getTsScopes()));
		}
		
		for(JeeslCqBoolean cq : ListUtils.emptyIfNull(query.getCqBooleans()))
		{
			if(cq.getPath().equals(CqBool.path(JeeslTsMultiPoint.Attributes.visible)))
			{
				BooleanPredicateBuilder.add(cB,predicates, cq, root.<Boolean>get(JeeslTsMultiPoint.Attributes.visible.toString()));
			}
			else {logger.warn(cq.nyi(fbTs.getClassMp()));}
		}
		for(JeeslCqLiteral c : ListUtils.emptyIfNull(query.getCqLiterals()))
		{
			if(c.getPath().equals(CqLiteral.path(JeeslTsMultiPoint.Attributes.code)))
			{					
				Expression<String> e = root.get(JeeslTsMultiPoint.Attributes.code.toString());
				LiteralPredicateBuilder.add(cB,predicates,c,e);
			}
			else {logger.warn("NYI: "+c.toString());}
		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}
	private void obMultiPoint(CriteriaBuilder cB, CriteriaQuery<MP> cQ, JeeslTimeSeriesQuery<CAT,SCOPE,MP,TS,TX,BRIDGE,INT,STAT> query, Root<MP> root)
	{
		List<Order> orders = new ArrayList<>();
		for(JeeslCqOrdering cq : ListUtils.emptyIfNull(query.getCqOrderings()))
		{
			if(cq.getPath().equals(CqOrdering.path(JeeslTsMultiPoint.Attributes.position)))
			{
				SortByPredicateBuilder.addByInteger(cB, orders, cq, root.<Integer>get(JeeslTsMultiPoint.Attributes.position.toString()));
			}
			else {logger.warn(cq.nyi(fbTs.getClassMp()));}
		}
		if(!orders.isEmpty()) {cQ.orderBy(orders);}
	}
}