package org.jeesl.controller.util.comparator.ejb.io.attribute;

import java.util.Comparator;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeCategory;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AttributeSetComparator<CAT extends JeeslAttributeCategory<?,?,?,CAT,?>,
									
									SET extends JeeslAttributeSet<?,?,?,CAT,?>>
{
	final static Logger logger = LoggerFactory.getLogger(AttributeSetComparator.class);

    public enum Type {position};

    public AttributeSetComparator()
    {
    	
    }
    
    public Comparator<SET> factory(Type type)
    {
        Comparator<SET> c = null;
        AttributeSetComparator<CAT,SET> factory = new AttributeSetComparator<CAT,SET>();
        switch (type)
        {
            case position: c = factory.new PositionComparator();break;
        }

        return c;
    }

    private class PositionComparator implements Comparator<SET>
    {
        public int compare(SET a, SET b)
        {
			  CompareToBuilder ctb = new CompareToBuilder();
			  ctb.append(a.getCategory2().getPosition(), b.getCategory2().getPosition());
			  ctb.append(a.getPosition(), b.getPosition());
			  return ctb.toComparison();
        }
    }
}