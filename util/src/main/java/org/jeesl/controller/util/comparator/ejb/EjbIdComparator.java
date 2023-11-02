package org.jeesl.controller.util.comparator.ejb;

import java.util.Comparator;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbIdComparator<T extends EjbWithId>
{
	final static Logger logger = LoggerFactory.getLogger(EjbIdComparator.class);

	public enum Type {asc,dsc};
	
	public EjbIdComparator() {}
	
	public Comparator<T> factory(Type type)
    {
        Comparator<T> c = null;
        EjbIdComparator<T> factory = new EjbIdComparator<>();
        switch (type)
        {
            case asc: c = factory.new AscComparator(); break;
            case dsc: c = factory.new DscComparator(); break;
        }

        return c;
    }
	
	private class AscComparator implements Comparator<T>
    {
        @Override public int compare(T a, T b)
        {
        	CompareToBuilder ctb = new CompareToBuilder();
        	ctb.append(a.getId(),b.getId());
        	return ctb.toComparison();
        }
    }
	
	private class DscComparator implements Comparator<T>
    {
		@Override public int compare(T a, T b)
        {
        	CompareToBuilder ctb = new CompareToBuilder();

        	ctb.append(b.getId(),a.getId());
        	return ctb.toComparison();
        }
    }
}