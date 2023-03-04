package org.jeesl.factory.json.module.calendar;

import java.util.ArrayList;
import java.util.Objects;

import org.jeesl.model.json.module.calendar.JsonCalendar;
import org.jeesl.model.json.module.calendar.JsonCalendarItem;

public class JsonCalendarFactory
{
	public static JsonCalendar build() {return new JsonCalendar();}
	
	public static void add(JsonCalendar container, JsonCalendarItem item)
	{
		if(Objects.isNull(container.getItems())) {container.setItems(new ArrayList<>());}
		if(Objects.nonNull(item)) {container.getItems().add(item);}
	}
}