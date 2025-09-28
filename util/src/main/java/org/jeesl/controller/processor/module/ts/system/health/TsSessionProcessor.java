package org.jeesl.controller.processor.module.ts.system.health;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.exlp.util.system.DateUtil;
import org.jeesl.api.facade.module.JeeslTsFacade;
import org.jeesl.controller.processor.module.ts.AbstractTimeSeriesProcessor;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.module.TsFactoryBuilder;
import org.jeesl.factory.ejb.util.EjbIdFactory;
import org.jeesl.interfaces.bean.system.JeeslSessionRegistryBean;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
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
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.model.json.module.ts.JsonTsData;
import org.jeesl.model.json.module.ts.JsonTsPoint;
import org.jeesl.model.json.module.ts.JsonTsSeries;
import org.joda.time.DateTime;
import org.metachart.model.xml.chart.Chart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TsSessionProcessor<SYSTEM extends JeeslIoSsiSystem<?,?>,
									USER extends JeeslUser<?>,
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
	final static Logger logger = LoggerFactory.getLogger(TsSessionProcessor.class);
	private final String localeCode = "en";
	
	public TsSessionProcessor(TsFactoryBuilder<?,?,?,?,SCOPE,ST,?,MP,TS,TRANSACTION,?,BRIDGE,EC,ENTITY,INT,STAT,DATA,POINT,?,?,WS,?,?> fbTs,
									JeeslTsFacade<?,SCOPE,ST,?,MP,TS,TRANSACTION,?,BRIDGE,EC,ENTITY,INT,STAT,DATA,POINT,?,?,WS,?> fTs)
	{
		super(fbTs,fTs);
	}
		
	public String update(SYSTEM system, JeeslSessionRegistryBean<USER> bSession)
	{
		StringBuilder sb = new StringBuilder();
		try
		{
			DateTime dt = new DateTime(new Date());
			Date date = dt.withMillisOfSecond(0).withSecondOfMinute(0).toDate();
			
			TS ts = fcTs(system);
			TRANSACTION transaction = fTs.save(fbTs.ejbTransaction().build(null,null));
			
			DATA data = efData.build(ws,ts,transaction,date,null);
			data = fTs.save(data);
					
			for(MP mp : fTs.allForParent(fbTs.getClassMp(), scope))
			{
				if(mp.getCode().equals("active"))
				{
					POINT dp =  efPoint.build(data, mp, Integer.valueOf(bSession.activeSessions()).doubleValue());
					fTs.save(dp);
					sb.append(mp.getName().get(localeCode).getLang()).append(": ").append(dp.getValue());
				}
				else if(mp.getCode().equals("authenticated"))
				{
					POINT dp =  efPoint.build(data, mp, Integer.valueOf(bSession.authenticatedSessions()).doubleValue());
					fTs.save(dp);
					sb.append(mp.getName().get(localeCode).getLang()).append(": ").append(dp.getValue());
				}
			}
			
		}
		catch (JeeslConstraintViolationException | JeeslLockingException e) {e.printStackTrace();}
		return sb.toString();
	}
	
	public void update(SYSTEM system, TRANSACTION transaction, JsonTsSeries json)
	{
		TS ts = null;
		try {ts = fcTs(system);} catch (JeeslConstraintViolationException e) {logger.error(e.getMessage());}
		
		if(EjbIdFactory.isSaved(ts))
		{
			List<MP> mps = fTs.allForParent(fbTs.getClassMp(),ts.getScope());
			
			Set<Date> setDate = efData.toSetDate(fTs.fData(ws,ts,JeeslTsData.QueryInterval.closedClosed,json.getDateStart(),json.getDateEnd()));
			
			for(JsonTsData jData : json.getDatas())
			{
				if(!setDate.contains(DateUtil.toDate(jData.getLocalDateTime())))
				{
					DATA data = efData.build2(ws,ts,transaction,jData.getLocalDateTime(),null);
					try {data = fTs.save(data);}
					catch (JeeslConstraintViolationException | JeeslLockingException e) {}
					
					if(EjbIdFactory.isSaved(data))
					{
						for(JsonTsPoint jPoint : jData.getPoints())
						{
							for(MP mp : mps)
							{
								if(jPoint.getMp().getCode().equals(mp.getCode()))
								{
									try
									{
										POINT dp =  fTs.save(efPoint.build(data,mp,jPoint.getValue()));
										logger.info("saved: "+dp.getId()+" "+dp.getValue()+" "+dp.getMultiPoint().getCode());
									}
									catch (JeeslConstraintViolationException | JeeslLockingException e) {logger.error(e.getMessage());}
								}
							}
						}
					}
				}
			}
		}
	}
	
	public Chart build(String localeCode, Date begin, Date end, SYSTEM system) 
	{
		Chart chart = mfTs.build(localeCode);
		chart.setSubtitle(null);
		try
		{
			chart.setDs(mfTs.multiPoints(localeCode,system,begin,end));
		}
		catch (JeeslNotFoundException e) {}
		return chart;
	}
}