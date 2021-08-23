package org.jeesl.util.comparator.ejb.system.security;

import java.util.Comparator;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityView;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityTemplate;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityAction;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityUsecase;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityCategory;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SecurityActionComparator<L extends JeeslLang,
									D extends JeeslDescription,
									C extends JeeslSecurityCategory<L,D>,
									R extends JeeslSecurityRole<L,D,C,V,U,A,USER>,
									V extends JeeslSecurityView<L,D,C,R,U,A>,
									U extends JeeslSecurityUsecase<L,D,C,R,V,A>,
									A extends JeeslSecurityAction<L,D,R,V,U,AT>,
									AT extends JeeslSecurityTemplate<L,D,C>,
									USER extends JeeslUser<R>>
{
	final static Logger logger = LoggerFactory.getLogger(SecurityActionComparator.class);

    public enum Type {position};

    public SecurityActionComparator()
    {
    	
    }
    
    public Comparator<A> factory(Type type)
    {
        Comparator<A> c = null;
        SecurityActionComparator<L,D,C,R,V,U,A,AT,USER> factory = new SecurityActionComparator<L,D,C,R,V,U,A,AT,USER>();
        switch (type)
        {
            case position: c = factory.new PositionCodeComparator();break;
        }

        return c;
    }

    private class PositionCodeComparator implements Comparator<A>
    {
        public int compare(A a, A b)
        {
			  CompareToBuilder ctb = new CompareToBuilder();
			  ctb.append(a.getView().getCategory().getPosition(), b.getView().getCategory().getPosition());
			  ctb.append(a.getView().getPosition(), b.getView().getPosition());
			  ctb.append(a.getPosition(), b.getPosition());
			  return ctb.toComparison();
        }
    }
}