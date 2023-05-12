package org.jeesl.controller.util.comparator.ejb.module.calendar;

import java.util.Comparator;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.jeesl.interfaces.model.with.date.ju.EjbWithRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RecordComparator<T extends EjbWithRecord> implements Comparator<T>
{
	final static Logger logger = LoggerFactory.getLogger(RecordComparator.class);

	public RecordComparator() {}
	
	@Override public int compare(T a, T b)
    {
		  CompareToBuilder ctb = new CompareToBuilder();
		  ctb.append(a.getRecord(), b.getRecord());
		  ctb.append(a.getId(), b.getId());
		  return ctb.toComparison();
    }
}