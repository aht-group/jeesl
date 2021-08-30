package org.jeesl.util.comparator.ejb.io.revision;

import java.util.Comparator;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.jeesl.interfaces.model.io.revision.core.JeeslRevisionCategory;
import org.jeesl.interfaces.model.io.revision.entity.JeeslRevisionEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RevisionEntityComparator<RC extends JeeslRevisionCategory<?,?,RC,?>,	
										RE extends JeeslRevisionEntity<?,?,RC,?,?,?>
										>
{
	final static Logger logger = LoggerFactory.getLogger(RevisionEntityComparator.class);

    public enum Type {position};

    public RevisionEntityComparator()
    {
    	
    }
    
    public Comparator<RE> factory(Type type)
    {
        Comparator<RE> c = null;
        RevisionEntityComparator<RC,RE> factory = new RevisionEntityComparator<>();
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
			  ctb.append(a.getCategory().getPosition(), b.getCategory().getPosition());
			  ctb.append(a.getPosition(), b.getPosition());
			  return ctb.toComparison();
        }
    }
}