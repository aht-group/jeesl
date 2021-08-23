package org.jeesl.util.comparator.ejb.component.sb;

import java.util.Comparator;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HydroDecadeComparator<L extends JeeslLang,D extends JeeslDescription,
									HD extends JeeslStatus<L,D,HD>>
{
    final static Logger logger = LoggerFactory.getLogger(HydroDecadeComparator.class);

    public enum Type {code};

    public Comparator<HD> factory(Type type)
    {
        Comparator<HD> c = null;
        HydroDecadeComparator factory = new HydroDecadeComparator();
        switch (type)
        {
            case code: c = factory.new CodeComparator();break;
        }

        return c;
    }

    private class CodeComparator implements Comparator<HD>
    {
    	@Override
		public int compare(HD a, HD b)
        {
            CompareToBuilder ctb = new CompareToBuilder();
            ctb.append(a.getCode(),b.getCode());
            return ctb.toComparison();
        }
    }

}
