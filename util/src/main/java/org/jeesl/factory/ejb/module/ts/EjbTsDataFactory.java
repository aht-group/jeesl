package org.jeesl.factory.ejb.module.ts;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.exlp.util.system.DateUtil;
import org.jeesl.interfaces.model.module.ts.core.JeeslTimeSeries;
import org.jeesl.interfaces.model.module.ts.data.JeeslTsData;
import org.jeesl.interfaces.model.module.ts.data.JeeslTsTransaction;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.model.pojo.map.generic.Nested2Map;
import org.jeesl.model.xml.module.ts.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbTsDataFactory<TS extends JeeslTimeSeries<?,TS,?,?,?>,
								TRANSACTION extends JeeslTsTransaction<?,DATA,?,?>,
								DATA extends JeeslTsData<TS,TRANSACTION,?,?,WS>,
								WS extends JeeslStatus<?,?,WS>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbTsDataFactory.class);
	
	private final Class<DATA> cData;
    
	public EjbTsDataFactory(final Class<DATA> cData)
	{       
        this.cData=cData;
	}
	
	public DATA build(WS workspace, TS timeSeries, TRANSACTION transaction, Data data)
	{
		if(Objects.nonNull(data.getValue()))
		{
			return build(workspace,timeSeries,transaction,data.getRecord().toGregorianCalendar().getTime(),data.getValue());
		}
		else
		{
			return build(workspace,timeSeries,transaction,data.getRecord().toGregorianCalendar().getTime(),null);
		}
	}
	
	public DATA build() {return build(null,null,null,null,null);}
	public DATA build(WS workspace, TS timeSeries) {return build(workspace,timeSeries,null,new Date(),null);}
	public DATA build2(WS workspace, TS timeSeries, TRANSACTION transaction, LocalDateTime ldt, Double value) {return build(workspace,timeSeries, transaction,DateUtil.toDate(ldt),value);}
	public DATA build(WS workspace, TS timeSeries, TRANSACTION transaction, Date date, Double value)
	{
		DATA ejb = null;
		try
		{
			ejb = cData.getDeclaredConstructor().newInstance();
			ejb.setWorkspace(workspace);
			ejb.setTimeSeries(timeSeries);
			ejb.setTransaction(transaction);
			ejb.setRecord(date);
			ejb.setValue(value);
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		catch (IllegalArgumentException e) {e.printStackTrace();}
		catch (InvocationTargetException e) {e.printStackTrace();}
		catch (NoSuchMethodException e) {e.printStackTrace();}
		catch (SecurityException e) {e.printStackTrace();}
		return ejb;
	}

	public Set<Date> toSetDate(List<DATA> list)
	{
		Set<Date> set = new HashSet<Date>();
		for(DATA d : list ) {set.add(d.getRecord());}
		return set;
	}
	
	public Map<TS,List<DATA>> toMapSeriesListData(List<DATA> list)
	{
		return list.stream().collect(Collectors.groupingBy(DATA::getTimeSeries));
	}
	public Map<LocalDateTime,DATA> toMapRecord(List<DATA> list)
	{
		return list.stream().collect(Collectors.toMap(k -> DateUtil.toLocalDateTime(k.getRecord()), v -> v));
	}
	public Map<TS,DATA> toMapSeriesSingleData(List<DATA> list)
	{
		Map<TS,DATA> map = new HashMap<>();
		for(DATA d : list) {map.put(d.getTimeSeries(), d);}
		return map;
	}
}