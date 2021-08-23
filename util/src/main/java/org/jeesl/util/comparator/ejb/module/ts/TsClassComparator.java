package org.jeesl.util.comparator.ejb.module.ts;

import java.util.Comparator;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.jeesl.interfaces.model.module.ts.config.JeeslTsCategory;
import org.jeesl.interfaces.model.module.ts.core.JeeslTsEntityClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TsClassComparator<CAT extends JeeslTsCategory<?,?,CAT,?>,
								EC extends JeeslTsEntityClass<?,?,CAT,?>
								>
{
	final static Logger logger = LoggerFactory.getLogger(TsClassComparator.class);

    public enum Type {position};

    public TsClassComparator()
    {
    	
    }
    
    public Comparator<EC> factory(Type type)
    {
        Comparator<EC> c = null;
        TsClassComparator<CAT,EC> factory = new TsClassComparator<>();
        switch (type)
        {
            case position: c = factory.new PositionCodeComparator();break;
        }

        return c;
    }

    private class PositionCodeComparator implements Comparator<EC>
    {
        public int compare(EC a, EC b)
        {
			CompareToBuilder ctb = new CompareToBuilder();
			ctb.append(a.getCategory().getPosition(), b.getCategory().getPosition());
			ctb.append(a.getPosition(), b.getPosition());
			return ctb.toComparison();
        }
    }
}