package org.jeesl.controller.util.comparator.ejb.io.db;

import java.util.Comparator;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbIoDbTableComparator<MT extends JeeslDbMetaTable<?,?,?>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbIoDbTableComparator.class);

    public enum Type {code};

    public EjbIoDbTableComparator()
    {
    	
    }
    
    public Comparator<MT> instance(Type type)
    {
        Comparator<MT> c = null;
        EjbIoDbTableComparator<MT> factory = new EjbIoDbTableComparator<>();
        switch (type)
        {
            case code: c = factory.new PositionCodeComparator();break;
        }

        return c;
    }

    private class PositionCodeComparator implements Comparator<MT>
    {
        @Override public int compare(MT a, MT b)
        {
			  CompareToBuilder ctb = new CompareToBuilder();
			  ctb.append(a.getCode().startsWith("_"), b.getCode().startsWith("_"));
			  ctb.append(a.getCode().endsWith("_"), b.getCode().endsWith("_"));
			  ctb.append(a.getCode(), b.getCode());
			  ctb.append(a.getId(),b.getId());
			  return ctb.toComparison();
        }
    }
}