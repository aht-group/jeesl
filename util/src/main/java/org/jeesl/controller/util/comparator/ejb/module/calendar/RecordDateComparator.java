package org.jeesl.controller.util.comparator.ejb.module.calendar;

import java.util.Comparator;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.jeesl.interfaces.model.with.date.jt.JeeslWithRecordDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RecordDateComparator<T extends JeeslWithRecordDate> implements Comparator<T>
{
	final static Logger logger = LoggerFactory.getLogger(RecordDateComparator.class);

	public RecordDateComparator() {}
	
	@Override public int compare(T a, T b)
    {
		  CompareToBuilder ctb = new CompareToBuilder();
		  ctb.append(a.getRecord(), b.getRecord());
		  ctb.append(a.getId(), b.getId());
		  return ctb.toComparison();
    }
}