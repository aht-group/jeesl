package org.jeesl.controller.util.comparator.ejb.system.constraint;

import java.util.Comparator;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.jeesl.interfaces.model.system.constraint.algorithm.JeeslConstraintAlgorithm;
import org.jeesl.interfaces.model.system.constraint.algorithm.JeeslConstraintAlgorithmGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SystemConstraintAlgorithmComparator<ALGORITHM extends JeeslConstraintAlgorithm<?,?,GROUP>,
										GROUP extends JeeslConstraintAlgorithmGroup<?,?,GROUP,?>>
{
	final static Logger logger = LoggerFactory.getLogger(SystemConstraintAlgorithmComparator.class);

    public enum Type {position};

    public SystemConstraintAlgorithmComparator()
    {
    	
    }
    
    public Comparator<ALGORITHM> factory(Type type)
    {
        Comparator<ALGORITHM> c = null;
        SystemConstraintAlgorithmComparator<ALGORITHM,GROUP> factory = new SystemConstraintAlgorithmComparator<>();
        switch (type)
        {
            case position: c = factory.new PositionComparator();break;
        }

        return c;
    }

    private class PositionComparator implements Comparator<ALGORITHM>
    {
        public int compare(ALGORITHM a, ALGORITHM b)
        {
			  CompareToBuilder ctb = new CompareToBuilder();
			  ctb.append(a.getCategory().getPosition(), b.getCategory().getPosition());
			  ctb.append(a.getPosition(), b.getPosition());
			  return ctb.toComparison();
        }
    }
}