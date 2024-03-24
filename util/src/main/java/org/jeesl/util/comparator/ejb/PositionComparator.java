package org.jeesl.util.comparator.ejb;

import java.util.Comparator;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPosition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PositionComparator<T extends EjbWithPosition> implements Comparator<T>
{
	final static Logger logger = LoggerFactory.getLogger(PositionComparator.class);

	public static <T extends EjbWithPosition> PositionComparator<T> instance() {return new PositionComparator<>();}
	
	public int compare(T a, T b)
    {
		  CompareToBuilder ctb = new CompareToBuilder(); 
		  ctb.append(a.getPosition(), b.getPosition());
		  return ctb.toComparison();
    }
}