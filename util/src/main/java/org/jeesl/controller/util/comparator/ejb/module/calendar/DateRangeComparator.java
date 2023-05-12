package org.jeesl.controller.util.comparator.ejb.module.calendar;

import java.util.Comparator;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.jeesl.interfaces.model.with.date.ju.EjbWithDateRange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateRangeComparator<T extends EjbWithDateRange> implements Comparator<T>
{
	final static Logger logger = LoggerFactory.getLogger(DateRangeComparator.class);

	public int compare(T a, T b)
    {
		  CompareToBuilder ctb = new CompareToBuilder();
		  ctb.append(a.getStartDate(),b.getStartDate());
		  ctb.append(a.getId(), b.getId());
		  return ctb.toComparison();
    }
}