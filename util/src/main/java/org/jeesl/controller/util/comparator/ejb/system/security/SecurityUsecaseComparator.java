package org.jeesl.controller.util.comparator.ejb.system.security;

import java.util.Comparator;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.jeesl.interfaces.model.system.security.access.JeeslSecurityUsecase;
import org.jeesl.interfaces.model.system.security.util.JeeslSecurityCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SecurityUsecaseComparator<C extends JeeslSecurityCategory<?,?>,
									U extends JeeslSecurityUsecase<?,?,C,?,?,?>
									>
{
	final static Logger logger = LoggerFactory.getLogger(SecurityUsecaseComparator.class);

    public enum Type {position};

    public SecurityUsecaseComparator()
    {
    	
    }
    
    public Comparator<U> factory(Type type)
    {
        Comparator<U> c = null;
        SecurityUsecaseComparator<C,U> factory = new SecurityUsecaseComparator<>();
        switch (type)
        {
            case position: c = factory.new PositionCodeComparator();break;
        }

        return c;
    }

	private class PositionCodeComparator implements Comparator<U>
    {
    	@Override public int compare(U a, U b)
    	{
    		CompareToBuilder ctb = new CompareToBuilder();
    		ctb.append(a.getCategory().getPosition(), b.getCategory().getPosition());
    		ctb.append(a.getPosition(), b.getPosition());
    		return ctb.toComparison();
    	}
    }
}