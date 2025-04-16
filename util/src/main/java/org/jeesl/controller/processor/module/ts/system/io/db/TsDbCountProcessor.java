package org.jeesl.controller.processor.module.ts.system.io.db;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Table;

import org.jeesl.api.facade.io.JeeslIoDbFacade;
import org.jeesl.api.facade.module.JeeslTsFacade;
import org.jeesl.controller.processor.module.ts.AbstractTimeSeriesProcessor;
import org.jeesl.controller.util.comparator.primitive.BooleanComparator;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.builder.io.IoRevisionFactoryBuilder;
import org.jeesl.factory.builder.module.TsFactoryBuilder;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.module.ts.config.JeeslTsInterval;
import org.jeesl.interfaces.model.module.ts.core.JeeslTimeSeries;
import org.jeesl.interfaces.model.module.ts.core.JeeslTsEntityClass;
import org.jeesl.interfaces.model.module.ts.core.JeeslTsMultiPoint;
import org.jeesl.interfaces.model.module.ts.core.JeeslTsScope;
import org.jeesl.interfaces.model.module.ts.core.JeeslTsScopeType;
import org.jeesl.interfaces.model.module.ts.data.JeeslTsBridge;
import org.jeesl.interfaces.model.module.ts.data.JeeslTsData;
import org.jeesl.interfaces.model.module.ts.data.JeeslTsDataPoint;
import org.jeesl.interfaces.model.module.ts.data.JeeslTsTransaction;
import org.jeesl.interfaces.model.module.ts.stat.JeeslTsStatistic;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TsDbCountProcessor<RE extends JeeslRevisionEntity<?,?,?,?,?,?>,
									SCOPE extends JeeslTsScope<?,?,?,ST,?,EC,INT>,
									ST extends JeeslTsScopeType<?,?,ST,?>,
									MP extends JeeslTsMultiPoint<?,?,SCOPE,?>,
									TS extends JeeslTimeSeries<SCOPE,TS,BRIDGE,INT,STAT>,
									TRANSACTION extends JeeslTsTransaction<?,DATA,?,?>,
									BRIDGE extends JeeslTsBridge<EC>,
									EC extends JeeslTsEntityClass<?,?,?,ENTITY>,
									ENTITY extends JeeslRevisionEntity<?,?,?,?,?,?>,
									INT extends JeeslTsInterval<?,?,INT,?>,
									STAT extends JeeslTsStatistic<?,?,STAT,?>,
									DATA extends JeeslTsData<TS,TRANSACTION,?,POINT,WS>,
									POINT extends JeeslTsDataPoint<DATA,MP>,
									WS extends JeeslStatus<?,?,WS>>
	extends AbstractTimeSeriesProcessor<SCOPE,ST,MP,TS,TRANSACTION,BRIDGE,EC,ENTITY,INT,STAT,DATA,POINT,WS>
{
	final static Logger logger = LoggerFactory.getLogger(TsDbCountProcessor.class);

	private final IoRevisionFactoryBuilder<?,?,?,?,?,?,?,RE,?,?,?,?,?,?> fbRevision;

	private final JeeslIoDbFacade<?,?,?,?,?,?,?,?,?,?,?> fDb;

	public TsDbCountProcessor(IoRevisionFactoryBuilder<?,?,?,?,?,?,?,RE,?,?,?,?,?,?> fbRevision,
									TsFactoryBuilder<?,?,?,?,SCOPE,ST,?,MP,TS,TRANSACTION,?,BRIDGE,EC,ENTITY,INT,STAT,DATA,POINT,?,?,WS,?,?> fbTs,
									JeeslIoDbFacade<?,?,?,?,?,?,?,?,?,?,?> fDb,
									JeeslTsFacade<?,SCOPE,ST,?,MP,TS,TRANSACTION,?,BRIDGE,EC,ENTITY,INT,STAT,DATA,POINT,?,?,WS,?> fTs)
	{
		super(fbTs,fTs);
		this.fbRevision=fbRevision;
		this.fDb=fDb;
	}

	public List<RE> findActive()
	{
		List<RE> listTs = new ArrayList<RE>();
		for(RE entity : fDb.all(fbRevision.getClassEntity()))
		{
			try
			{
				Class<?> c = Class.forName(entity.getCode());

				boolean active = BooleanComparator.active(entity.getTimeseries());
				boolean table = c.getAnnotation(Table.class)!=null;

				if(active && table){listTs.add(entity);}
			}
			catch (ClassNotFoundException e) {e.printStackTrace();}
		}
		return listTs;
	}

	public void count() throws IllegalStateException
	{
		if(!isInitialized()) {throw new IllegalStateException(this.getClass().getSimpleName()+" is not fully initialized");}
		List<RE> listTs = findActive();

		if(!listTs.isEmpty())
		{
			try
			{
				TRANSACTION transaction = fbTs.ejbTransaction().build(null,null);
				transaction = fTs.save(transaction);
				Date date = (new DateTime(new Date())).withTimeAtStartOfDay().toDate();
				for(RE entity : listTs)
				{
					try
					{
						Class<?> c = Class.forName(entity.getCode());
						count(transaction,date,entity,c);
					}
					catch (ClassNotFoundException e) {e.printStackTrace();}
					catch (JeeslConstraintViolationException | JeeslLockingException e) {e.printStackTrace();}
				}
			}
			catch (JeeslConstraintViolationException | JeeslLockingException e) {e.printStackTrace();}
		}
	}

	private void count(TRANSACTION transaction, Date date, RE entity, Class<?> c) throws JeeslConstraintViolationException, JeeslLockingException
	{
		BRIDGE bridge = fTs.fcBridge(fbTs.getClassBridge(),ec,entity);
		STAT statistic = fTs.fByEnum(fbTs.getClassStat(), JeeslTsStatistic.Code.raw);
		TS ts = fTs.fcTimeSeries(scope,interval,statistic,bridge);
		Long count = fDb.countEstimate(c);
		DATA data = efData.build(ws, ts, transaction, date, count.doubleValue());
		data = fTs.save(data);
		logger.info(entity.getCode()+" "+c.getAnnotation(Table.class).name()+" "+count);
	}
}