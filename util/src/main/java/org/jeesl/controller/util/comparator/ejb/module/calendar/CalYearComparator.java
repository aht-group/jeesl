package org.jeesl.controller.util.comparator.ejb.module.calendar;

import java.util.Comparator;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.jeesl.interfaces.model.module.calendar.unit.JeeslCalendarYear;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CalYearComparator<T extends JeeslCalendarYear<?,?,?,?>> implements Comparator<T>
{
	final static Logger logger = LoggerFactory.getLogger(CalYearComparator.class);

	@Override public int compare(T a, T b)
    {
		  CompareToBuilder ctb = new CompareToBuilder();
		  
		  ctb.append(Integer.valueOf(a.getCode()),Integer.valueOf(b.getCode()));
		  ctb.append(a.getId(), b.getId());
		  return ctb.toComparison();
    }
}