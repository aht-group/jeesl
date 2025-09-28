package org.jeesl.controller.processor.module.ts.system.io.mail;

import java.util.Date;

import org.jeesl.api.facade.module.JeeslTsFacade;
import org.jeesl.controller.processor.module.ts.AbstractTimeSeriesProcessor;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.module.TsFactoryBuilder;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.io.mail.core.JeeslIoMailStatus;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiSystem;
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
import org.jeesl.model.json.io.db.tuple.container.JsonTuples1;
import org.jeesl.model.json.io.db.tuple.instance.JsonTuple1;
import org.metachart.model.xml.chart.Chart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TsMailQueueProcessor<SYSTEM extends JeeslIoSsiSystem<?,?>,
									STATUS extends JeeslIoMailStatus<?,?,STATUS,?>,
									SCOPE extends JeeslTsScope<?,?,?,ST,?,EC,INT>,
									ST extends JeeslTsScopeType<?,?,ST,?>,
									MP extends JeeslTsMultiPoint<?,?,SCOPE,?,?>,
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
	final static Logger logger = LoggerFactory.getLogger(TsMailQueueProcessor.class);

	public TsMailQueueProcessor(TsFactoryBuilder<?,?,?,?,SCOPE,ST,?,MP,TS,TRANSACTION,?,BRIDGE,EC,ENTITY,INT,STAT,DATA,POINT,?,?,WS,?,?> fbTs,
									JeeslTsFacade<?,SCOPE,ST,?,MP,TS,TRANSACTION,?,BRIDGE,EC,ENTITY,INT,STAT,DATA,POINT,?,?,WS,?> fTs)
	{
		super(fbTs,fTs);
	}
	
	public void update(SYSTEM system, Date record, JsonTuples1<STATUS> tuples)
	{
		try
		{
			TS ts = super.fcTs(system);
			TRANSACTION transaction = fbTs.ejbTransaction().build(null,null);
			if(!developmentMode) {transaction = fTs.save(transaction);}
			DATA data = efData.build(ws,ts,transaction,record,0d);
			if(!developmentMode) {data = fTs.save(data);}
			logger.info("Series: "+ts.toString()+" data:"+data.toString());
			
			for(JsonTuple1<STATUS> t : tuples.getTuples())
			{
				for(MP mp : mps)
				{
					if(t.getEjb1().getCode().equals(mp.getCode()))
					{
						POINT point = efPoint.build(data,mp,Long.valueOf(t.getCount1()).doubleValue());
						if(!developmentMode) {point = fTs.save(point);}
						logger.info(point.toString());
					}
				}
			}
		}
		catch (JeeslConstraintViolationException | JeeslLockingException  e) {e.printStackTrace();}

	}
	
	public Chart build(String localeCode, Date begin, Date end, SYSTEM system) 
	{
		Chart chart = mfTs.build(localeCode);
		chart.setSubtitle(null);
		try
		{
			chart.setDs(mfTs.singleData(localeCode,system,begin,end));
		}
		catch (JeeslNotFoundException e) {}
		return chart;
	}
}