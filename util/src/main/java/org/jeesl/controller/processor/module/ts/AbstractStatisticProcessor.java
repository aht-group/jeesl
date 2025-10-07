package org.jeesl.controller.processor.module.ts;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.jeesl.controller.util.comparator.ejb.module.ts.TsDataComparator;
import org.jeesl.factory.builder.module.TsFactoryBuilder;
import org.jeesl.factory.ejb.module.ts.EjbTsDataFactory;
import org.jeesl.interfaces.model.module.ts.config.JeeslTsInterval;
import org.jeesl.interfaces.model.module.ts.core.JeeslTimeSeries;
import org.jeesl.interfaces.model.module.ts.data.JeeslTsData;
import org.jeesl.interfaces.model.module.ts.data.JeeslTsSample;
import org.jeesl.interfaces.model.module.ts.data.JeeslTsTransaction;
import org.jeesl.interfaces.model.module.ts.stat.JeeslTsStatistic;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractStatisticProcessor <TS extends JeeslTimeSeries<?,TS,?,?,?>,
									TRANSACTION extends JeeslTsTransaction<?,DATA,?,?>,
									DATA extends JeeslTsData<TS,TRANSACTION,SAMPLE,?,WS>,
									SAMPLE extends JeeslTsSample, 
									WS extends JeeslStatus<?,?,WS>>
{
	final static Logger logger = LoggerFactory.getLogger(AbstractStatisticProcessor.class);
	
	protected final TsFactoryBuilder<?,?,?,?,?,?,?,?,TS,TRANSACTION,?,?,?,?,?,?,DATA,?,SAMPLE,?,WS,?,?> fbTs;
	
	protected final EjbTsDataFactory<TS,TRANSACTION,DATA,WS> efData;
	
	protected Comparator<DATA> comparatorData;
	
	public AbstractStatisticProcessor(TsFactoryBuilder<?,?,?,?,?,?,?,?,TS,TRANSACTION,?,?,?,?,?,?,DATA,?,SAMPLE,?,WS,?,?> fbTs)
	{
		this.fbTs = fbTs;
		efData = fbTs.ejbData();
		comparatorData = fbTs.cmpData(TsDataComparator.Type.date);
	}
	
	public List<DATA> buildStatistic(List<DATA> tsData, JeeslTsStatistic.Code code, JeeslTsInterval.Code iCode)
	{
		Collections.sort(tsData,comparatorData);
		List<DATA> statistics = new ArrayList<>();
		int counter = 0;
		for(int i=0; i<tsData.size(); i=i+counter)
		{
			DescriptiveStatistics stats = new DescriptiveStatistics();
			stats.addValue(tsData.get(i).getValue());
			LocalDate startDate = tsData.get(i).getRecord().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			counter = 1;
			for(int j=i+1; j<tsData.size(); j++)
			{
				
				LocalDate compareDate = tsData.get(j).getRecord().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				if(isMatchingInterval(iCode,startDate,compareDate)) {stats.addValue(tsData.get(j).getValue());counter++;}
				else { break;}
			}
			DATA statisticData = buildStatisticData(stats, code);
			statisticData.setRecord(Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
			statisticData.setWorkspace(tsData.get(i).getWorkspace());
			statistics.add(statisticData);
		}
		return statistics;
	}
	
	private DATA buildStatisticData(DescriptiveStatistics stats, JeeslTsStatistic.Code code)
	{
		DATA statisticData = efData.build();
		switch(code)
		{
			case min: statisticData.setValue(stats.getMin());break;
			case max: statisticData.setValue(stats.getMax());break;
			case mean: statisticData.setValue(stats.getMean());break;
			case sum: statisticData.setValue(stats.getSum());break;
			default: break;
		}
		return statisticData;
	}
	
	private boolean isMatchingInterval(JeeslTsInterval.Code code, LocalDate startDate, LocalDate compareDate)
	{
		boolean match = false;
		switch(code)
		{
			case day: match = matchDay(startDate,compareDate); break;
			case weekly: match = matchMondayWeek(startDate,compareDate); break;
			case monthly: match = matchMonth(startDate,compareDate); break;
			case quarterly: match = matchQuarter(startDate,compareDate); break;
			case yearly: match = matchYear(startDate,compareDate); break;
			default: break;
		}
		return match;
	}
	
	private boolean matchDay(LocalDate startDate, LocalDate compareDate)
	{
		if(startDate.equals(compareDate)) {return true;}		
		return false;
	}
	
	private boolean matchMondayWeek(LocalDate startDate, LocalDate compareDate)
	{
		LocalDate firstDayOfWeek = startDate.with(DayOfWeek.MONDAY);
		LocalDate firstDayOfNextWeek = firstDayOfWeek.plusWeeks(1);
		if(compareDate.isAfter(firstDayOfWeek) && compareDate.isBefore(firstDayOfNextWeek) || firstDayOfWeek.equals(compareDate)) {return true;}
		return false;
	}

	private boolean matchMonth(LocalDate startDate, LocalDate compareDate)
	{
		if(YearMonth.from(startDate).equals(YearMonth.from(compareDate))) {return true;}
		return false;
	}
	
	private boolean matchQuarter(LocalDate startDate, LocalDate compareDate)
	{
		LocalDate firstDayOfQuarter = startDate.with(startDate.getMonth().firstMonthOfQuarter()).with(TemporalAdjusters.firstDayOfMonth());
		LocalDate firstDayOfNextQuarter = firstDayOfQuarter.plusMonths(3);
		if(compareDate.isAfter(firstDayOfQuarter) && compareDate.isBefore(firstDayOfNextQuarter) || compareDate.equals(firstDayOfQuarter)) {return true;}
		return false;
	}
	
	private boolean matchYear(LocalDate startDate, LocalDate compareDate)
	{
		if(Year.from(startDate).equals(Year.from(compareDate))) {return true;}
		return false;
	}

}
