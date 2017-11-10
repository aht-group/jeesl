package org.jeesl.util.comparator.ejb.system.constraint;

import java.util.Comparator;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.jeesl.interfaces.model.system.constraint.algorithm.JeeslConstraintAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.interfaces.model.status.UtilsStatus;

public class SystemConstraintAlgorithmComparator<ALGCAT extends UtilsStatus<ALGCAT,?,?>, ALGO extends JeeslConstraintAlgorithm<?,?,ALGCAT>>
{
	final static Logger logger = LoggerFactory.getLogger(SystemConstraintAlgorithmComparator.class);

    public enum Type {position};

    public SystemConstraintAlgorithmComparator()
    {
    	
    }
    
    public Comparator<ALGO> factory(Type type)
    {
        Comparator<ALGO> c = null;
        SystemConstraintAlgorithmComparator<ALGCAT,ALGO> factory = new SystemConstraintAlgorithmComparator<ALGCAT,ALGO>();
        switch (type)
        {
            case position: c = factory.new PositionComparator();break;
        }

        return c;
    }

    private class PositionComparator implements Comparator<ALGO>
    {
        public int compare(ALGO a, ALGO b)
        {
			  CompareToBuilder ctb = new CompareToBuilder();
			  ctb.append(a.getCategory().getPosition(), b.getCategory().getPosition());
			  ctb.append(a.getPosition(), b.getPosition());
			  return ctb.toComparison();
        }
    }
}