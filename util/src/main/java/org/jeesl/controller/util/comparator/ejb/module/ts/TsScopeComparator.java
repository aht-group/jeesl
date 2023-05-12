package org.jeesl.controller.util.comparator.ejb.module.ts;

import java.util.Comparator;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.jeesl.interfaces.model.module.ts.config.JeeslTsCategory;
import org.jeesl.interfaces.model.module.ts.core.JeeslTsScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TsScopeComparator<CAT extends JeeslTsCategory<?,?,CAT,?>,
								SCOPE extends JeeslTsScope<?,?,CAT,?,?,?,?>
								>
{
	final static Logger logger = LoggerFactory.getLogger(TsScopeComparator.class);

    public enum Type {position};

    public TsScopeComparator()
    {
    	
    }
    
    public Comparator<SCOPE> factory(Type type)
    {
        Comparator<SCOPE> c = null;
        TsScopeComparator<CAT,SCOPE> factory = new TsScopeComparator<>();
        switch (type)
        {
            case position: c = factory.new PositionCodeComparator();break;
        }

        return c;
    }

    private class PositionCodeComparator implements Comparator<SCOPE>
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