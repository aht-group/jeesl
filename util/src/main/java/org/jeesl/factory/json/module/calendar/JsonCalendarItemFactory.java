package org.jeesl.factory.json.module.calendar;

import java.time.LocalDate;

import org.jeesl.model.json.module.calendar.JsonCalendarItem;

public class JsonCalendarItemFactory
{
	public static JsonCalendarItem build() {return new JsonCalendarItem();}
	public static JsonCalendarItem build(String code, String title, LocalDate startDate, LocalDate endDate)
	{
		JsonCalendarItem json = build();
		json.setCode(code);
		json.setTitle(title);
		json.setStartDate(startDate);
		json.setEndDate(endDate);
		return json;
	}
	
}