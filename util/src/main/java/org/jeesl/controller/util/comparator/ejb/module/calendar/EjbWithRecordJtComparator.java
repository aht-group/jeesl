package org.jeesl.controller.util.comparator.ejb.module.calendar;

import java.util.Comparator;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.jeesl.interfaces.model.with.date.jt.JeeslWithRecordDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbWithRecordJtComparator<T extends JeeslWithRecordDateTime>
{
	final static Logger logger = LoggerFactory.getLogger(EjbWithRecordJtComparator.class);

	public enum Type {asc,dsc};
	
	public EjbWithRecordJtComparator() {}
	
	public Comparator<T> factory(Type type)
    {
        Comparator<T> c = null;
        EjbWithRecordJtComparator<T> factory = new EjbWithRecordJtComparator<>();
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
			  ctb.append(a.getRecord(),b.getRecord());
			  ctb.append(a.getId(),b.getId());
			  return ctb.toComparison();
        }
    }
	
	private class DscComparator implements Comparator<T>
    {
        @Override public int compare(T a, T b)
        {
			  CompareToBuilder ctb = new CompareToBuilder();
			  ctb.append(b.getRecord(),a.getRecord());
			  ctb.append(a.getId(),b.getId());
			  return ctb.toComparison();
        }
    }
}