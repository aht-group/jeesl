package org.jeesl.factory.json.util;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.jeesl.model.json.util.time.JsonDay;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.util.DateUtil;

public class JsonDayFactory
{
	final static Logger logger = LoggerFactory.getLogger(JsonDayFactory.class);
	public static final long serialVersionUID=1;
	
	private Locale locale;
	
	private final DateTime now;

	public static <E extends Enum<E>> JsonDayFactory instance (E localeCode) {return new JsonDayFactory(localeCode.toString());}
	public static JsonDayFactory instance (String localeCode) {return new JsonDayFactory(localeCode);}
	public JsonDayFactory(String localeCode)
	{
		if(localeCode.equals("de")){locale = Locale.GERMAN;}
		else if(localeCode.equals("fr")){locale = Locale.FRENCH;}
		else {locale = Locale.ENGLISH;}
		
		now = new DateTime(new Date());
	}
	
	public static JsonDay build()
	{
		JsonDay json = new JsonDay();
		
		return json;
	}
	
	public JsonDay build(DateTime dt)
	{
		JsonDay json = build();
		json.setNr(dt.getDayOfMonth());
		json.setWeekend(dt.getDayOfWeek()>5);
		json.setMonth(dt.getMonthOfYear());
		json.setYear(dt.getYear());
		
		DateTime.Property pDoW = dt.dayOfWeek();
		json.setName(pDoW.getAsText(locale));
		json.setCode(pDoW.getAsShortText(locale));
		
		json.setToday(now.getDayOfMonth()==dt.getDayOfMonth() && now.getMonthOfYear()==dt.getMonthOfYear() && now.getYear()==dt.getYear());
		
		return json;
	}
	
	public JsonDay build(LocalDate ld)
	{		
		JsonDay json = build();
		json.setNr(ld.getDayOfMonth());
		json.setWeekend(ld.getDayOfWeek().getValue()>5);
		json.setMonth(ld.getMonth().getValue());
		json.setYear(ld.getYear());
		
		json.setName(ld.getDayOfWeek().getDisplayName(TextStyle.FULL,locale));
		json.setCode(ld.getDayOfWeek().getDisplayName(TextStyle.SHORT,locale));
		json.setToday(LocalDate.now().isEqual(ld));
		
		return json;
	}
	
	public List<JsonDay> build(Date from, Date to)
	{
		DateTime dtFrom = new DateTime(from).withTimeAtStartOfDay();
		DateTime dtTo = new DateTime(to).withTimeAtStartOfDay();
		
		logger.info(dtFrom.toString()+" -> "+dtTo.toString());
		
		List<JsonDay> list = new ArrayList<>();
		
		boolean work = dtFrom.isBefore(dtTo) || dtFrom.isEqual(dtTo);
		int i = 0;
		while(work)
		{
			work = dtFrom.plusDays(i).isBefore(dtTo) || dtFrom.isEqual(dtTo);
			JsonDay day = build(dtFrom.plusDays(i));
			i++;
			day.setId(i);
			list.add(day);
		}
		
		return list;
	}
	
	public List<JsonDay> build(LocalDate start, int size)
	{
		List<JsonDay> list = new ArrayList<>();

		for(int i=0;i<size;i++)
		{
			JsonDay day = build(start.plusDays(i));
			day.setId(i+1);
			list.add(day);
		}
		
		return list;
	}
	
	public List<JsonDay> buildMonth(int year, int month)
	{
		LocalDate ld = LocalDate.of(year,month,1);
		int maxDaysNew = ld.plusMonths(1).minusDays(1).getDayOfMonth();
		
		List<JsonDay> days = new ArrayList<JsonDay>();
		for(int i=0;i<maxDaysNew;i++)
		{
			JsonDay day = build(ld.plusDays(i));
			day.setId(i+1);
			days.add(day);
		}
		return days;
	}
	
	public static Map<Integer,JsonDay> toMapDayNr(List<JsonDay> list)
	{
		Map<Integer,JsonDay> map = new HashMap<>();
		for(JsonDay day : list) {map.put(day.getNr(),day);}
		return map;
	}
	public static Map<LocalDate,JsonDay> toMapLd(List<JsonDay> list)
	{
		Map<LocalDate,JsonDay> map = new HashMap<>();
		for(JsonDay day : list) {map.put(LocalDate.of(day.getYear(),day.getMonth(),day.getNr()),day);}
		return map;
	}
	
	public static Date toDate(JsonDay day)
	{
		return DateUtil.getDateFromInt(day.getYear(),day.getMonth(),day.getNr());
	}
	
	public static List<Integer> toIntegerDayNr(List<JsonDay> days)
	{
		Set<Integer> set = new HashSet<>();
		for(JsonDay day : days) {set.add(day.getNr());}
		return new ArrayList<>(set);
	}
}