package org.jeesl.controller.processor.module.ts.system.io.db;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.jeesl.api.facade.module.JeeslTsFacade;
import org.jeesl.controller.processor.module.ts.AbstractTimeSeriesProcessor;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.module.TsFactoryBuilder;
import org.jeesl.interfaces.model.io.db.JeeslDbDump;
import org.jeesl.interfaces.model.io.revision.entity.JeeslRevisionEntity;
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
import org.metachart.xml.chart.Chart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.xml.io.Dir;
import net.sf.exlp.xml.io.File;

public class TsDbBackupSizeProcessor<SYSTEM extends JeeslIoSsiSystem<?,?>,
									DUMP extends JeeslDbDump<SYSTEM,?>,
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
	final static Logger logger = LoggerFactory.getLogger(TsDbBackupSizeProcessor.class);
	
	public TsDbBackupSizeProcessor(TsFactoryBuilder<?,?,?,SCOPE,ST,?,MP,TS,TRANSACTION,?,BRIDGE,EC,ENTITY,INT,STAT,DATA,POINT,?,?,WS,?,?> fbTs,
									JeeslTsFacade<?,?,?,SCOPE,ST,?,MP,TS,TRANSACTION,?,BRIDGE,EC,ENTITY,INT,STAT,DATA,POINT,?,?,WS,?,?> fTs)
	{
		super(fbTs,fTs);
	}
		
	public void update(SYSTEM system, Dir xDirectory)
	{
		if(xDirectory.isSetClassifier() && xDirectory.getClassifier().contentEquals(system.getCode()))
		{
			try
			{
				TS ts = fcTs(system);
				Set<Date> set = efData.toSetDate(fTs.fData(ws,ts));
				List<DATA> add = new ArrayList<>();
				for(File xFile : xDirectory.getFile())
				{
					Date date = xFile.getLastModifed().toGregorianCalendar().getTime();
					if(!set.contains(date))
					{
						add.add(efData.build(ws,ts,null,date,Long.valueOf(xFile.getSize()).doubleValue()));
					}
				}
				add(add);
			}
			catch (JeeslConstraintViolationException | JeeslLockingException e) {e.printStackTrace();}
		}
	}
	
	public void update(SYSTEM system, List<DUMP> dumps)
	{
		try
		{
			TS ts = super.fcTs(system);
			Set<Date> set = efData.toSetDate(fTs.fData(ws,ts));
			List<DATA> add = new ArrayList<>();
			for(DUMP dump : dumps)
			{
				if(dump.getSystem().equals(system) && !set.contains(dump.getRecord()))
				{
					add.add(efData.build(ws,ts,null,dump.getRecord(),Long.valueOf(dump.getSize()).doubleValue()));
				}
			}
			super.add(add);
		}
		catch (JeeslConstraintViolationException | JeeslLockingException e) {e.printStackTrace();}
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