package org.jeesl.util.comparator.xml.ts;

import java.util.Comparator;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.jeesl.model.xml.module.ts.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TsDataComparator
{
	final static Logger logger = LoggerFactory.getLogger(TsDataComparator.class);

    public enum Type {date};

    public static Comparator<Data> factory(Type type)
    {
        Comparator<Data> c = null;
        TsDataComparator factory = new TsDataComparator();
        switch (type)
        {
            case date: c = factory.new DateComparator();break;
        }

        return c;
    }

	private class DateComparator implements Comparator<Data>
    {
    	public int compare(Data a, Data b)
        {
    		CompareToBuilder ctb = new CompareToBuilder();
    		ctb.append(a.getRecord().toGregorianCalendar().getTime(), b.getRecord().toGregorianCalendar().getTime());
    		return ctb.toComparison();
        }
    }
}