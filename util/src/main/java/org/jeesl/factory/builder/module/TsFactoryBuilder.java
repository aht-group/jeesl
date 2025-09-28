package org.jeesl.factory.builder.module;

import java.util.Comparator;

import org.jeesl.api.facade.module.JeeslTsFacade;
import org.jeesl.controller.util.comparator.ejb.module.ts.TsClassComparator;
import org.jeesl.controller.util.comparator.ejb.module.ts.TsDataComparator;
import org.jeesl.controller.util.comparator.ejb.module.ts.TsScopeComparator;
import org.jeesl.factory.builder.AbstractFactoryBuilder;
import org.jeesl.factory.ejb.module.ts.EjbTsBridgeFactory;
import org.jeesl.factory.ejb.module.ts.EjbTsClassFactory;
import org.jeesl.factory.ejb.module.ts.EjbTsCronFactory;
import org.jeesl.factory.ejb.module.ts.EjbTsDataFactory;
import org.jeesl.factory.ejb.module.ts.EjbTsDataPointFactory;
import org.jeesl.factory.ejb.module.ts.EjbTsFactory;
import org.jeesl.factory.ejb.module.ts.EjbTsMutliPointFactory;
import org.jeesl.factory.ejb.module.ts.EjbTsScopeFactory;
import org.jeesl.factory.ejb.module.ts.EjbTsTransactionFactory;
import org.jeesl.factory.mc.ts.McTimeSeriesFactory;
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
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLangDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TsFactoryBuilder<L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
								CAT extends JeeslTsCategory<L,D,CAT,?>,
								SCOPE extends JeeslTsScope<L,D,CAT,ST,UNIT,EC,INT>,
								ST extends JeeslTsScopeType<L,D,ST,?>,
								UNIT extends JeeslStatus<L,D,UNIT>,
								MP extends JeeslTsMultiPoint<L,D,SCOPE,UNIT,?>,
								TS extends JeeslTimeSeries<SCOPE,TS,BRIDGE,INT,STAT>,
								TRANSACTION extends JeeslTsTransaction<SOURCE,DATA,USER,?>,
								SOURCE extends EjbWithLangDescription<L,D>, 
								BRIDGE extends JeeslTsBridge<EC>,
								EC extends JeeslTsEntityClass<L,D,CAT,ENTITY>,
								ENTITY extends JeeslRevisionEntity<L,D,?,?,?,?>,
								INT extends JeeslTsInterval<L,D,INT,?>,
								STAT extends JeeslTsStatistic<L,D,STAT,?>,
								DATA extends JeeslTsData<TS,TRANSACTION,SAMPLE,POINT,WS>,
								POINT extends JeeslTsDataPoint<DATA,MP>,
								SAMPLE extends JeeslTsSample,
								USER extends EjbWithId, 
								WS extends JeeslStatus<L,D,WS>,
								QAF extends JeeslStatus<L,D,QAF>,
								CRON extends JeeslTsCron<SCOPE,INT,STAT>>
				extends AbstractFactoryBuilder<L,D>
{
	private static final long serialVersionUID = 1L;

	final static Logger logger = LoggerFactory.getLogger(TsFactoryBuilder.class);
	
	private final Class<LOC> cLocale; public Class<LOC> getClassLocale() {return cLocale;}
	private final Class<CAT> cCategory; public Class<CAT> getClassCategory() {return cCategory;}
	private final Class<SCOPE> cScope; public Class<SCOPE> getClassScope() {return cScope;}
	private final Class<ST> cScopeType; public Class<ST> getClassScopeType() {return cScopeType;}
	private final Class<UNIT> cUnit; public Class<UNIT> getClassUnit()  {return cUnit;}
	private final Class<MP> cMp; public Class<MP> getClassMp() {return cMp;}
	private final Class<TS> cTs; public Class<TS> getClassTs() {return cTs;}
	private final Class<TRANSACTION> cTransaction; public Class<TRANSACTION> getClassTransaction() {return cTransaction;}
	private final Class<SOURCE> cSource; public Class<SOURCE> getClassSource() {return cSource;}
	private final Class<BRIDGE> cBridge; public Class<BRIDGE> getClassBridge() {return cBridge;}
	private final Class<EC> cEc; public Class<EC> getClassEntity() {return cEc;}
	private final Class<INT> cInt; public Class<INT> getClassInterval() {return cInt;}
	private final Class<STAT> cStat; public Class<STAT> getClassStat(){return cStat;}
	private final Class<DATA> cData;  public Class<DATA> getClassData() {return cData;}
	private final Class<POINT> cPoint;  public Class<POINT> getClassPoint() {return cPoint;}
	private final Class<WS> cWs; public Class<WS> getClassWorkspace() {return cWs;}
	private final Class<CRON> cCron; public Class<CRON> getClassCron(){return cCron;}
	
	public TsFactoryBuilder(final Class<L> cL, final Class<D> cD, final Class<LOC> cLocale,
							final Class<CAT> cCategory,
							final Class<SCOPE> cScope,
							final Class<ST> cScopeType,
							final Class<UNIT> cUnit,
							final Class<MP> cMp,
							final Class<TS> cTs,
							final Class<TRANSACTION> cTransaction,
							final Class<SOURCE> cSource,
							final Class<BRIDGE> cBridge,
							final Class<EC> cEc,
							final Class<INT> cInt,
							final Class<STAT> cStat,
							final Class<DATA> cData,
							final Class<POINT> cPoint,
							final Class<WS> cWs, final Class<CRON> cCron)
	{
		super(cL,cD);
		this.cLocale=cLocale;
		this.cCategory=cCategory;
		this.cScope=cScope;
		this.cScopeType=cScopeType;
		this.cUnit=cUnit;
		this.cTs=cTs;
		this.cMp=cMp;
        this.cTransaction=cTransaction;
        this.cSource=cSource;
        this.cBridge=cBridge;
        this.cEc=cEc;
        this.cInt=cInt;
        this.cData=cData;
        this.cPoint=cPoint;
        this.cWs=cWs;
        this.cStat=cStat;
        this.cCron=cCron;
	}
	
	public EjbTsFactory<SCOPE,TS,BRIDGE,INT,STAT> ejbTs(){return new EjbTsFactory<>(cTs);}
	public EjbTsScopeFactory<CAT,SCOPE,UNIT> ejbScope(){return new EjbTsScopeFactory<>(cScope);}
	public EjbTsBridgeFactory<SCOPE,MP,TS,BRIDGE,EC,DATA,POINT> ejbBridge(){return new EjbTsBridgeFactory<>(cBridge);}
	public EjbTsTransactionFactory<TRANSACTION,SOURCE,USER> ejbTransaction() {return new EjbTsTransactionFactory<>(cTransaction);}
	public EjbTsDataFactory<TS,TRANSACTION,DATA,WS> ejbData() {return new EjbTsDataFactory<>(cData);}
	public EjbTsDataPointFactory<TS,MP,DATA,POINT> ejbDataPoint() {return new EjbTsDataPointFactory<>(cPoint);}
	public EjbTsClassFactory<CAT,EC,ENTITY> ejbEntityClass(){return new EjbTsClassFactory<>(cEc);}
	public EjbTsMutliPointFactory<SCOPE,MP> ejbMultiPoint() {return new EjbTsMutliPointFactory<>(cMp);}
	public EjbTsCronFactory<SCOPE,INT,STAT,CRON> ejbCron() {return new EjbTsCronFactory<SCOPE,INT,STAT,CRON>(cCron);}
	
	public McTimeSeriesFactory<SCOPE,MP,TS,BRIDGE,EC,ENTITY,INT,STAT,DATA,POINT,WS> metaChart(JeeslTsFacade<CAT,SCOPE,ST,UNIT,MP,TS,TRANSACTION,SOURCE,BRIDGE,EC,ENTITY,INT,STAT,DATA,POINT,SAMPLE,USER,WS,CRON> fTs)
	{
		return new McTimeSeriesFactory<>(this,fTs);
	}
	
	public Comparator<EC> cmpClass(TsClassComparator.Type type) {return new TsClassComparator<CAT,EC>().factory(type);}
	public Comparator<SCOPE> cmpScope(TsScopeComparator.Type type) {return (new TsScopeComparator<CAT,SCOPE>()).factory(type);}
	public Comparator<DATA> cmpData(TsDataComparator.Type type) {return (new TsDataComparator<TS,DATA,SAMPLE,WS>()).factory(type);}
}