package org.jeesl.util.comparator.ejb.date;

import java.util.Comparator;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.jeesl.interfaces.model.with.date.ju.EjbWithValidFrom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ValidFromComparator<T extends EjbWithValidFrom> implements Comparator<T>
{
	final static Logger logger = LoggerFactory.getLogger(ValidFromComparator.class);

	public int compare(T a, T b)
    {
		  CompareToBuilder ctb = new CompareToBuilder();
		  ctb.append(a.getValidFrom(),b.getValidFrom());
		  ctb.append(a.getId(), b.getId());
		  return ctb.toComparison();
    }
}