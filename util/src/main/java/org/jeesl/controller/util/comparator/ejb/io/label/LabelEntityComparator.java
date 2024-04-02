package org.jeesl.controller.util.comparator.ejb.io.label;

import java.util.Comparator;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionCategory;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LabelEntityComparator<RC extends JeeslRevisionCategory<?,?,RC,?>,	
										RE extends JeeslRevisionEntity<?,?,RC,?,?,?>
										>
{
	final static Logger logger = LoggerFactory.getLogger(LabelEntityComparator.class);

    public enum Type {position};

    public LabelEntityComparator()
    {
    	
    }
    
    public Comparator<RE> factory(Type type)
    {
        Comparator<RE> c = null;
        LabelEntityComparator<RC,RE> factory = new LabelEntityComparator<>();
        switch (type)
        {
            case position: c = factory.new PositionCodeComparator();break;
        }

        return c;
    }

    private class PositionCodeComparator implements Comparator<RE>
    {
        public int compare(RE a, RE b)
        {
			  CompareToBuilder ctb = new CompareToBuilder();
			  if(ObjectUtils.allNotNull(a.getCategory(),b.getCategory())) {ctb.append(a.getCategory().getPosition(), b.getCategory().getPosition());}
			  ctb.append(a.getPosition(), b.getPosition());
			  return ctb.toComparison();
        }
    }
}