package org.jeesl.controller.util.comparator.ejb.system.constraint;

import java.util.Comparator;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.jeesl.interfaces.model.system.constraint.core.JeeslConstraintScope;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContraintScopeComparator<SCOPE extends JeeslConstraintScope<?,?,CAT>,
										CAT extends JeeslStatus<?,?,CAT>>
{
	final static Logger logger = LoggerFactory.getLogger(ContraintScopeComparator.class);

    public enum Type {position};

    public ContraintScopeComparator()
    {
    	
    }
    
    public Comparator<SCOPE> factory(Type type)
    {
        Comparator<SCOPE> c = null;
        ContraintScopeComparator<SCOPE,CAT> factory = new ContraintScopeComparator<>();
        switch (type)
        {
            case position: c = factory.new PositionComparator();break;
        }

        return c;
    }

    private class PositionComparator implements Comparator<SCOPE>
    {
        public int compare(SCOPE a, SCOPE b)
        {
			  CompareToBuilder ctb = new CompareToBuilder();
			  ctb.append(a.getCategory().getPosition(), b.getCategory().getPosition());
			  ctb.append(a.getPosition(), b.getPosition());
			  return ctb.toComparison();
        }
    }
}