package org.jeesl.controller.util.comparator.ejb.system.security;

import java.util.Comparator;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityCategory;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityRole;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.interfaces.model.system.security.util.JeeslStaff;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SecurityStaffComparator<C extends JeeslSecurityCategory<?,?>,
									R extends JeeslSecurityRole<?,?,C,?,?,?,USER>,
									USER extends JeeslUser<R>,
									STAFF extends JeeslStaff<R,USER,?,?>>
{
	final static Logger logger = LoggerFactory.getLogger(SecurityStaffComparator.class);

    public enum Type {position};

    public SecurityStaffComparator()
    {
    	
    }
    
    public Comparator<STAFF> factory(Type type)
    {
        Comparator<STAFF> c = null;
        SecurityStaffComparator<C,R,USER,STAFF> factory = new SecurityStaffComparator<>();
        switch (type)
        {
            case position: c = factory.new PositionCodeComparator();break;
        }

        return c;
    }

	private class PositionCodeComparator implements Comparator<STAFF>
    {
		public int compare(STAFF a, STAFF b)
        {
			CompareToBuilder ctb = new CompareToBuilder();
			ctb.append(a.getRole().getCategory().getPosition(), b.getRole().getCategory().getPosition());
			ctb.append(a.getRole().getPosition(), b.getRole().getPosition());
			ctb.append(a.getPosition(), b.getPosition());
			return ctb.toComparison();
        }
    }
}