package org.jeesl.factory.mc.ts;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import org.exlp.util.system.DateUtil;
import org.jeesl.api.facade.module.JeeslTsFacade;
import org.jeesl.controller.util.comparator.primitive.BooleanComparator;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.module.TsFactoryBuilder;
import org.jeesl.factory.ejb.module.ts.EjbTsDataPointFactory;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.module.ts.config.JeeslTsInterval;
import org.jeesl.interfaces.model.module.ts.core.JeeslTimeSeries;
import org.jeesl.interfaces.model.module.ts.core.JeeslTsEntityClass;
import org.jeesl.interfaces.model.module.ts.core.JeeslTsMultiPoint;
import org.jeesl.interfaces.model.module.ts.core.JeeslTsScope;
import org.jeesl.interfaces.model.module.ts.data.JeeslTsBridge;
import org.jeesl.interfaces.model.module.ts.data.JeeslTsData;
import org.jeesl.interfaces.model.module.ts.data.JeeslTsDataPoint;
import org.jeesl.interfaces.model.module.ts.stat.JeeslTsStatistic;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.model.xml.module.ts.TimeSeries;
import org.metachart.factory.xml.chart.high.core.XmlChartFactory;
import org.metachart.factory.xml.chart.high.core.XmlDataFactory;
import org.metachart.factory.xml.chart.high.core.XmlSubtitleFactory;
import org.metachart.factory.xml.chart.high.core.XmlTitleFactory;
import org.metachart.model.xml.chart.Chart;
import org.metachart.model.xml.chart.Data;
import org.metachart.model.xml.chart.Ds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class McTimeSeriesFactory <SCOPE extends JeeslTsScope<?,?,?,?,?,EC,INT>,
								MP extends JeeslTsMultiPoint<?,?,SCOPE,?>,
								TS extends JeeslTimeSeries<SCOPE,TS,BRIDGE,INT,STAT>,
								BRIDGE extends JeeslTsBridge<EC>,
								EC extends JeeslTsEntityClass<?,?,?,ENTITY>,
								ENTITY extends JeeslRevisionEntity<?,?,?,?,?,?>,
								INT extends JeeslTsInterval<?,?,INT,?>,
								STAT extends JeeslTsStatistic<?,?,STAT,?>,
								DATA extends JeeslTsData<TS,?,?,POINT,WS>,
								POINT extends JeeslTsDataPoint<DATA,MP>,
								WS extends JeeslStatus<?,?,WS>>
{
	final static Logger logger = LoggerFactory.getLogger(McTimeSeriesFactory.class);

	private final boolean debugOnInfo = false;
//	private String localeCode;

	private final JeeslTsFacade<?,SCOPE,?,?,MP,TS,?,?,BRIDGE,EC,ENTITY,INT,STAT,DATA,POINT,?,?,WS,?> fTs;

	private final TsFactoryBuilder<?,?,?,?,SCOPE,?,?,MP,TS,?,?,BRIDGE,EC,ENTITY,INT,STAT,DATA,POINT,?,?,WS,?,?> fbTs;
	private final EjbTsDataPointFactory<MP,DATA,POINT> efPoint;

	private SCOPE scope; public SCOPE getScope() {return scope;} public void setScope(SCOPE scope) {this.scope = scope;}
	private EC entityClass; public EC getEntityClass() {return entityClass;} public void setEntityClass(EC entityClass) {this.entityClass = entityClass;}
	private INT interval; public INT getInterval() {return interval;} public void setInterval(INT interval) {this.interval = interval;}
	private WS workspace; public WS getWorkspace() {return workspace;} public void setWorkspace(WS workspace) {this.workspace = workspace;}

	public McTimeSeriesFactory(TsFactoryBuilder<?,?,?,?,SCOPE,?,?,MP,TS,?,?,BRIDGE,EC,ENTITY,INT,STAT,DATA,POINT,?,?,WS,?,?> fbTs,
							   JeeslTsFacade<?,SCOPE,?,?,MP,TS,?,?,BRIDGE,EC,ENTITY,INT,STAT,DATA,POINT,?,?,WS,?> fTs)
	{
		this.fbTs=fbTs;
		this.fTs=fTs;

		efPoint = fbTs.ejbDataPoint();
	}

	public <E extends Enum<E>> void initScope(E scope) throws JeeslNotFoundException {this.scope = fTs.fByCode(fbTs.getClassScope(), scope);}
	public void initEntityClass(Class<?> c) throws JeeslNotFoundException {this.entityClass = fTs.fByCode(fbTs.getClassEntity(), c.getName());}
	public <E extends Enum<E>> void initInterval(E interval) throws JeeslNotFoundException {this.interval = fTs.fByCode(fbTs.getClassInterval(), interval);}
	public <E extends Enum<E>> void initWorkspace(E workspace) throws JeeslNotFoundException {this.workspace = fTs.fByCode(fbTs.getClassWorkspace(), workspace);}

	public Chart build(String localeCode)
	{
		Chart chart = XmlChartFactory.build();
		chart.setTitle(XmlTitleFactory.build(scope.getName().get(localeCode).getLang()));
		chart.setSubtitle(XmlSubtitleFactory.build("Interval: "+interval.getName().get(localeCode).getLang()));
		return chart;
	}


	public Ds build2(List<DATA> datas)
	{
		Ds ds = new Ds();
		DatatypeFactory df;
		try {
			df = DatatypeFactory.newInstance();
			GregorianCalendar cal = new GregorianCalendar();
			for(DATA data: datas)
			{
				Data cd = new Data();
				cal.setTime(data.getRecord());
				cd.setRecord(df.newXMLGregorianCalendar(cal));
				if(data.getValue()!=null) {cd.setY(data.getValue());}
				ds.getData().add(cd);
			}
		} catch (DatatypeConfigurationException e) {
			for(DATA data: datas)
			{
				Data cd = new Data();
				cd.setRecord(DateUtil.toXmlGc(data.getRecord()));
				if(data.getValue()!=null) {cd.setY(data.getValue());}
				ds.getData().add(cd);
			}
		}
		return ds;
	}
	public static Ds build2(TimeSeries timeSeries)
	{
		Ds ds = new Ds();

		for(org.jeesl.model.xml.module.ts.Data tsD: timeSeries.getData())
		{
			if(Objects.nonNull(tsD.getValue()))
			{
				Data cd = new Data();
				cd.setRecord(tsD.getRecord());

				cd.setY(tsD.getValue());
				ds.getData().add(cd);
			}
		}
		return ds;
	}

	public <T extends EjbWithId> Ds singleData(String localeCode, T entity, Date from, Date to) throws JeeslNotFoundException
	{
		BRIDGE bridge = fTs.fBridge(entityClass,entity);
		return singleData(localeCode,bridge,from,to);
	}
	public <T extends EjbWithId> Ds singleData(String localeCode, BRIDGE bridge, Date from, Date to) throws JeeslNotFoundException
	{
		STAT statistic = fTs.fByEnum(fbTs.getClassStat(), JeeslTsStatistic.Code.raw);
		TS ts = fTs.fTimeSeries(scope,interval,statistic,bridge);
		
		logger.info("fTs.fData ts:"+ts.toString()+" from:"+from+" to:"+to);
		List<DATA> datas = fTs.fData(workspace,ts,JeeslTsData.QueryInterval.closedOpen,from,to);
		logger.info(fbTs.getClassData().getSimpleName()+": "+datas.size());
		
		Ds xml = new Ds();
		for(DATA d : datas)
		{
			xml.getData().add(XmlDataFactory.build(d.getValue(),d.getRecord()));
		}
		return xml;
	}

	public <T extends EjbWithId> Ds multiPoints(String localeCode, T entity, Date from, Date to) throws JeeslNotFoundException {return multiPoints(null,localeCode,entity,from,to);}
	
	@SuppressWarnings("unchecked")
	public <T extends EjbWithId, E extends Enum<E>> Ds multiPoints(String localeCode, T entity, Date from, Date to, E... onlyShowMp) throws JeeslNotFoundException
	{
		Set<String> set = new HashSet<>();
		for(E e : onlyShowMp) {set.add(e.toString());}
		return multiPoints(set,localeCode,entity,from,to);
	}
	public <T extends EjbWithId, E extends Enum<E>> Ds multiPoints(Set<String> onlyShowMp, String localeCode, T entity, Date from, Date to) throws JeeslNotFoundException
	{
		STAT statistic = fTs.fByEnum(fbTs.getClassStat(), JeeslTsStatistic.Code.raw);
		BRIDGE bridge = fTs.fBridge(entityClass,entity);
		TS ts = fTs.fTimeSeries(scope,interval,statistic,bridge);

		List<MP> multiPoints = fTs.allForParent(fbTs.getClassMp(),scope);
		List<DATA> datas = fTs.fData(workspace,ts,JeeslTsData.QueryInterval.closedOpen,from,to);
		List<POINT> points = fTs.fPoints(workspace,ts,JeeslTsData.QueryInterval.closedOpen,from,to);
		Map<MP,List<POINT>> mapMp = efPoint.toMapMultiPoint(points);

		if(debugOnInfo)
		{
			logger.info("Datas: "+datas.size());
			logger.info("Points: "+points.size());
		}

		Ds xml = new Ds();
		for(MP mp : multiPoints)
		{
			boolean includeMp = true;
			if(onlyShowMp!=null) {includeMp = onlyShowMp.contains(mp.getCode());}
			if(includeMp && mp.getVisible() && mapMp.containsKey(mp))
			{
				if(debugOnInfo) {logger.info("MP: "+mp.getCode());}
				Map<DATA,POINT> mapData = efPoint.toMapDataUnique(mapMp.get(mp));
				if(debugOnInfo) {logger.info("\t mapData.size():"+mapData.size());}
				Ds ds = new Ds();
				ds.setLabel(mp.getName().get(localeCode).getLang());
				for(DATA data : datas)
				{
					Data d = new Data();
					d.setRecord(DateUtil.toXmlGc(data.getRecord()));
					POINT p = mapData.get(data);
					if(debugOnInfo) {logger.info("P: "+(p!=null) + " "+mapData.containsKey(data));}

					if(p!=null) {d.setY(p.getValue());}
					ds.getData().add(d);
				}
				xml.getDs().add(ds);
			}
		}

		return xml;
	}

	public Ds multiPoints(List<MP> multiPoints, List<POINT> points)
	{
		Map<MP,List<POINT>> map = efPoint.toMapMultiPoint(points);

		Ds xml = new Ds();

		for(MP mp : multiPoints)
		{
			if(BooleanComparator.active(mp.getVisible()) && map.containsKey(mp))
			{
				Ds ds = new Ds();
				ds.setCode(mp.getCode());
				ds.setLabel(mp.getName().get("de").getLang());
				if(mp.getColor1()!=null && !mp.getColor1().trim().isEmpty()) {ds.setColor("#"+mp.getColor1());}
				for(POINT p : map.get(mp))
				{
					Data d = new Data();
					d.setRecord(DateUtil.toXmlGc(p.getData().getRecord()));
					d.setY(p.getValue());
					ds.getData().add(d);
				}
				xml.getDs().add(ds);
			}
		}

		return xml;
	}
}