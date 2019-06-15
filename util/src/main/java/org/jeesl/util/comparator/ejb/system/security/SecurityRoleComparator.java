package org.jeesl.util.comparator.ejb.system.security;

import java.util.Comparator;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityCategory;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SecurityRoleComparator<C extends JeeslSecurityCategory<?,?>,
									R extends JeeslSecurityRole<?,?,C,?,?,?,?>>
{
	final static Logger logger = LoggerFactory.getLogger(SecurityRoleComparator.class);

    public enum Type {position};

    public SecurityRoleComparator()
    {
    	
    }
    
    public Comparator<R> factory(Type type)
    {
        Comparator<R> c = null;
        SecurityRoleComparator<C,R> factory = new SecurityRoleComparator<C,R>();
        switch (type)
        {
            case position: c = factory.new PositionCodeComparator();break;
        }

        return c;
    }

    private class PositionCodeComparator implements Comparator<R>
    {
        public int compare(R a, R b)
        {
			  CompareToBuilder ctb = new CompareToBuilder();
			  ctb.append(a.getCategory().getPosition(), b.getCategory().getPosition());
			  ctb.append(a.getPosition(), b.getPosition());
			  return ctb.toComparison();
        }
    }
}