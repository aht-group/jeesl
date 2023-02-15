package org.jeesl.util.comparator.ejb.module.rmmv;

import java.util.Comparator;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.jeesl.factory.txt.module.rmmv.TxtRmmvClassificationFactory;
import org.jeesl.interfaces.model.module.rmmv.JeeslRmmvClassification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbRmmvClassificationComparator <C extends JeeslRmmvClassification<?,?,C,?>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbRmmvClassificationComparator.class);

    public enum Type {position,tree};
    
    public Comparator<C> instance(Type type)
    {
        Comparator<C> c = null;
        EjbRmmvClassificationComparator<C> factory = new EjbRmmvClassificationComparator<C>();
        switch (type)
        {
            case position: c = factory.new PositionComparator();break;
            case tree: c = factory.new TreePositionComparator();break;
        }

        return c;
    }

    private class PositionComparator implements Comparator<C>
    {
        @Override public int compare(C a, C b)
        {
			  CompareToBuilder ctb = new CompareToBuilder();
			  ctb.append(a.getPosition(),b.getPosition());
			  ctb.append(a.getId(),b.getId());
			  return ctb.toComparison();
        }
    }
    
    private class TreePositionComparator implements Comparator<C>
    {
        @Override public int compare(C a, C b)
        {
			  CompareToBuilder ctb = new CompareToBuilder();
			  ctb.append(TxtRmmvClassificationFactory.positions(a),TxtRmmvClassificationFactory.positions(b));
			  ctb.append(a.getId(),b.getId());
			  return ctb.toComparison();
        }
    }
}