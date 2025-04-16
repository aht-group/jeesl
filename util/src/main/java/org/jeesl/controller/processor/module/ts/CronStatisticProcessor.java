package org.jeesl.controller.processor.module.ts;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.jeesl.interfaces.model.module.ts.core.JeeslTimeSeries;
import org.jeesl.interfaces.model.module.ts.core.JeeslTsEntityClass;
import org.jeesl.interfaces.model.module.ts.core.JeeslTsScope;
import org.jeesl.interfaces.model.module.ts.data.JeeslTsBridge;
import org.jeesl.interfaces.model.module.ts.data.JeeslTsData;
import org.jeesl.interfaces.model.module.ts.data.JeeslTsSample;
import org.jeesl.interfaces.model.module.ts.data.JeeslTsTransaction;
import org.jeesl.interfaces.model.module.ts.stat.JeeslTsCron;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.api.facade.module.JeeslTsFacade;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.builder.module.TsFactoryBuilder;
import org.jeesl.factory.ejb.module.ts.EjbTsBridgeFactory;
import org.jeesl.factory.ejb.module.ts.EjbTsFactory;
import org.jeesl.interfaces.model.module.ts.config.JeeslTsInterval;

import org.jeesl.interfaces.model.module.ts.stat.JeeslTsStatistic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CronStatisticProcessor <SCOPE extends JeeslTsScope<?,?,?,?,?,EC,INT>,
									INT extends JeeslTsInterval<?,?,INT,?>,
									TS extends JeeslTimeSeries<SCOPE,TS,BRIDGE,INT,STAT>,
									TRANSACTION extends JeeslTsTransaction<?,DATA,?,?>,
									BRIDGE extends JeeslTsBridge<EC>,
									EC extends JeeslTsEntityClass<?,?,?,?>,
									STAT extends JeeslTsStatistic<?,?,STAT,?>,
									DATA extends JeeslTsData<TS,TRANSACTION,SAMPLE,?,WS>,
									SAMPLE extends JeeslTsSample,
									WS extends JeeslStatus<?,?,WS>,
									CRON extends JeeslTsCron<SCOPE,INT,STAT>
									>
	extends AbstractStatisticProcessor<TS,TRANSACTION,DATA,SAMPLE,WS>
{
	final static Logger logger = LoggerFactory.getLogger(CronStatisticProcessor.class);
	
	protected JeeslTsFacade<?,SCOPE,?,?,?,TS,TRANSACTION,?,BRIDGE,EC,?,INT,STAT,DATA,?,SAMPLE,?,WS,CRON> fTs;
	
	protected List<CRON> crons;
	
	protected EjbTsFactory<SCOPE,TS,BRIDGE,INT,STAT> efTs;
	protected EjbTsBridgeFactory<SCOPE,?,TS,BRIDGE,EC,DATA,?> efBridge;
	
	protected WS workspace; public WS getWorkspace() {return workspace;} public void setWorkspace(WS workspace) {this.workspace=workspace;}
	protected TRANSACTION transaction; public TRANSACTION getTransaction() {return transaction;} public void setTransaction(TRANSACTION transaction) {this.transaction=transaction;}
	
	public CronStatisticProcessor(TsFactoryBuilder<?,?,?,?,SCOPE,?,?,?,TS,TRANSACTION,?,BRIDGE,EC,?,INT,STAT,DATA,?,SAMPLE,?,WS,?,CRON> fbTs,
			JeeslTsFacade<?,SCOPE,?,?,?,TS,TRANSACTION,?,BRIDGE,EC,?,INT,STAT,DATA,?,SAMPLE,?,WS,CRON> fTs,
			TRANSACTION transaction,
			WS workspace)
	{
		super(fbTs);
		this.fTs = fTs;
		efTs = fbTs.ejbTs();
		efBridge = fbTs.ejbBridge();
		this.transaction = transaction;
		this.workspace = workspace;
	}
		
	public void saveStatistics(List<CRON> crons)
	{
		for(CRON c : crons)
		{
			try {saveStatistic(c);} 
			catch (JeeslConstraintViolationException | JeeslLockingException e) {e.printStackTrace();}
		}
		logger.info("All Statistics updated");
	}
	
	public void saveStatistic(CRON cron) throws JeeslConstraintViolationException, JeeslLockingException
	{
		List<TS> timeseries = findTimeSeries(cron);
		logger.info("Found timeseries: "+timeseries.size());
		for(TS ts : timeseries)
		{
			List<DATA> data = fTs.fData(workspace,ts);
			if(!data.isEmpty()) 
			{
				TS statTs = buildStatisticTimeSeries(ts,cron);
				rmExistingStatistic(statTs);
				List<DATA> statisticData = buildStatistic(data,JeeslTsStatistic.Code.valueOf(cron.getStatisticDst().getCode()), JeeslTsInterval.Code.valueOf(cron.getIntervalDst().getCode()));
				for(DATA d : statisticData)
				{
					d.setTimeSeries(statTs);
					d.setTransaction(transaction);
				}
				fTs.save(statisticData);
				logger.info("Statistic saved");
			}
		}
	}
	
	public List<TS> findTimeSeries(CRON cron)
	{
		List<TS> timeseries = new ArrayList<>();
		List<EC> entityClasses = cron.getScope().getClasses();
		logger.info("Found enities: "+entityClasses.size());
		for(EC ec : entityClasses)
		{
			List<TS> ts = new ArrayList<>();
			ts = fTs.fTimeSeries(cron.getScope(), cron.getIntervalSrc(), ec).stream().filter(i -> i.getStatistic().getCode().equals(cron.getStatisticSrc().getCode())).collect(Collectors.toList());
			timeseries.addAll(ts);
		}
		logger.info("Found ts for entities: "+timeseries.size());
		return timeseries;
	}
	
	public TS buildStatisticTimeSeries(TS ts, CRON cron) throws JeeslConstraintViolationException
	{
		logger.info("building statistic timeseries");
		TS statTs = fTs.fcTimeSeries(cron.getScope(), cron.getIntervalDst(), cron.getStatisticDst(), ts.getBridge());
		statTs.setTsSrc(ts);
		statTs = fTs.persist(statTs);
		return statTs;
	}

	public void rmExistingStatistic(TS ts) throws JeeslConstraintViolationException
	{
		List<DATA> existingData = new ArrayList<>();
		existingData = fTs.fData(workspace, ts);
		if(!existingData.isEmpty())
		{
			fTs.rm(existingData);
		}
	}
}
