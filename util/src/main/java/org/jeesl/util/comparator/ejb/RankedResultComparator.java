package org.jeesl.util.comparator.ejb;

import java.util.Comparator;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.jeesl.interfaces.model.util.UtilsRankedResult;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RankedResultComparator<RR extends UtilsRankedResult<T>, T extends EjbWithId> implements Comparator<RR>
{
	final static Logger logger = LoggerFactory.getLogger(RankedResultComparator.class);

	public int compare(RR a, RR b)
    {
		  CompareToBuilder ctb = new CompareToBuilder();
		  ctb.append(a.getRanking(), b.getRanking());
		  return ctb.toComparison();
    }
}