package org.jeesl.controller.util.comparator.ejb.io.attribute;

import java.util.Comparator;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeCategory;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AttributeCriteriaComparator<CAT extends JeeslAttributeCategory<?,?,?,CAT,?>,

										CRITERIA extends JeeslAttributeCriteria<?,?,?,CAT,?,?,?>>
{
	final static Logger logger = LoggerFactory.getLogger(AttributeCriteriaComparator.class);

    public enum Type {position};

    public AttributeCriteriaComparator()
    {
    	
    }
    
    public Comparator<CRITERIA> factory(Type type)
    {
        Comparator<CRITERIA> c = null;
        AttributeCriteriaComparator<CAT,CRITERIA> factory = new AttributeCriteriaComparator<CAT,CRITERIA>();
        switch (type)
        {
            case position: c = factory.new PositionComparator();break;
        }

        return c;
    }

    private class PositionComparator implements Comparator<CRITERIA>
    {
        public int compare(CRITERIA a, CRITERIA b)
        {
			  CompareToBuilder ctb = new CompareToBuilder();
			  ctb.append(a.getCategory().getPosition(), b.getCategory().getPosition());
			  ctb.append(a.getPosition(), b.getPosition());
			  return ctb.toComparison();
        }
    }
}