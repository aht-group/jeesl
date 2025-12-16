package org.jeesl.controller.util.comparator.ejb.io.fr;

import java.util.Comparator;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.jeesl.controller.util.comparator.ejb.io.db.EjbIoDbConstraintComparator;
import org.jeesl.interfaces.model.io.fr.JeeslFileMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbIoFrMetaComparator<META extends JeeslFileMeta<?,?,?,?>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbIoDbConstraintComparator.class);

    public enum Type {name};

    public EjbIoFrMetaComparator()
    {
    	
    }
    
    public Comparator<META> instance(Type type)
    {
        Comparator<META> c = null;
        EjbIoFrMetaComparator<META> factory = new EjbIoFrMetaComparator<>();
        switch (type)
        {
            case name: c = factory.new PositionCodeComparator();break;
        }

        return c;
    }

    private class PositionCodeComparator implements Comparator<META>
    {
        @Override public int compare(META a, META b)
        {
			  CompareToBuilder ctb = new CompareToBuilder();
			  if(ObjectUtils.allNotNull(a.getFileName(),b.getFileName())) {ctb.append(a.getFileName(), b.getFileName());}
			  ctb.append(a.getId(),b.getId());
			  return ctb.toComparison();
        }
    }
}