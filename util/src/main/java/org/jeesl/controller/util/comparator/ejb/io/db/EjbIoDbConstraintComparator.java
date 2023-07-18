package org.jeesl.controller.util.comparator.ejb.io.db;

import java.util.Comparator;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaColumn;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaConstraint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbIoDbConstraintComparator<COL extends JeeslDbMetaColumn<?,?,?>,
											CON extends JeeslDbMetaConstraint<?,?,COL,?>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbIoDbConstraintComparator.class);

    public enum Type {col};

    public EjbIoDbConstraintComparator()
    {
    	
    }
    
    public Comparator<CON> instance(Type type)
    {
        Comparator<CON> c = null;
        EjbIoDbConstraintComparator<COL,CON> factory = new EjbIoDbConstraintComparator<>();
        switch (type)
        {
            case col: c = factory.new PositionCodeComparator();break;
        }

        return c;
    }

    private class PositionCodeComparator implements Comparator<CON>
    {
        @Override public int compare(CON a, CON b)
        {
			  CompareToBuilder ctb = new CompareToBuilder();
			  ctb.append(a.getColumnLocal().getId(), b.getColumnLocal().getId());
			  ctb.append(a.getId(),b.getId());
			  return ctb.toComparison();
        }
    }
}