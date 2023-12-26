package net.sf.ahtutils.monitor.factory.net;

import java.util.Date;
import java.util.List;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.jeesl.api.facade.module.JeeslMonitoringFacade;
import org.jeesl.controller.monitoring.result.net.DnsResult;
import org.jeesl.controller.monitoring.result.net.IcmpResult;
import org.jeesl.model.xml.module.monitoring.Data;
import org.jeesl.model.xml.module.monitoring.DataSet;
import org.jeesl.model.xml.module.monitoring.Indicator;
import org.jeesl.model.xml.module.monitoring.Value;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.MutableDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.monitor.factory.AbstractTransmissionFactory;
import net.sf.ahtutils.monitor.util.DbCleaner;

public class TxDnsFactory extends AbstractTransmissionFactory
{
	final static Logger logger = LoggerFactory.getLogger(TxDnsFactory.class);
	
	public TxDnsFactory(JeeslMonitoringFacade fUm,DbCleaner dbCleaner)
	{
		super(fUm,dbCleaner);
	}
	
	public Indicator build(DateTime first, DateTime last, Duration range)
	{
		for(IcmpResult icmp : fUm.inInterval(IcmpResult.class,new Date(0),first.toDate()))
        {	//Ignoring all records before the first ceil
			dbCleaner.add(IcmpResult.class.getName(), icmp.getId());
        }
		
		Indicator indicator = new Indicator();		
		indicator.setCode(DnsResult.class.getCanonicalName());
		
		MutableDateTime mdtStart = new MutableDateTime(first);
		MutableDateTime mdtEnd = new MutableDateTime(first);
		mdtEnd.add(range);
		
		Duration dataRange = Duration.standardMinutes(1);
		while(mdtEnd.isBefore(last) || mdtEnd.isEqual(last))
		{
			logger.info("Processing "+mdtStart+" -> "+mdtEnd);
			indicator.getDataSet().add(buildDataSet(mdtStart.toDateTime(),mdtEnd.toDateTime(),dataRange));	
			mdtStart.add(range);
			mdtEnd.add(range);
		}
		
		return indicator;
	}
	
	private DataSet buildDataSet(DateTime first, DateTime last, Duration dataRange)
	{
		MutableDateTime mdtStart = new MutableDateTime(first);
		MutableDateTime mdtEnd = new MutableDateTime(first);
		mdtEnd.add(dataRange);
		
		DataSet ds = new DataSet();
		while(mdtEnd.isBefore(last) || mdtEnd.isEqual(last))
		{
			ds.getData().add(buildData(fUm.inInterval(IcmpResult.class, mdtStart.toDate(), mdtEnd.toDate())));
			mdtStart.add(dataRange);
			mdtEnd.add(dataRange);
		}
		
		return ds;
	}
	
	private Data buildData(List<IcmpResult> list)
	{
		SummaryStatistics ssPing = new SummaryStatistics();
		SummaryStatistics ssReachable = new SummaryStatistics();
		
		for(IcmpResult icmp : list)
		{
			logger.info(icmp.toString());
			dbCleaner.add(IcmpResult.class.getName(), icmp.getId());
			if(icmp.getCode().equals(IcmpResult.Code.REACHABLE))
			{
				ssPing.addValue(icmp.getDuration());
				ssReachable.addValue(1);
			}
			else
			{
				ssReachable.addValue(0);
			}
		}
		
		Data data = new Data();
		
		Value vPing = new Value();
		vPing.setValue(""+ssPing.getMean());
		data.getValue().add(vPing);
		
		Value vReachable = new Value();
		vReachable.setValue(""+ssReachable.getMean());
		data.getValue().add(vReachable);
		
		return data;
	}
}