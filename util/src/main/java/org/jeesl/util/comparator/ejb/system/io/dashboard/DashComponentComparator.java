package org.jeesl.util.comparator.ejb.system.io.dashboard;

import java.util.Comparator;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.jeesl.interfaces.model.io.dash.JeeslIoDashComponent;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DashComponentComparator<L extends JeeslLang,D extends JeeslDescription, DBC extends JeeslIoDashComponent<L,D,DBC>>
{
    final static Logger logger = LoggerFactory.getLogger(DashComponentComparator.class);

    public enum Type {code};

    public Comparator<DBC> factory(Type type)
    {
        Comparator<DBC> c = null;
        DashComponentComparator<L,D,DBC> factory = new DashComponentComparator<>();
        switch (type)
        {
            case code: c = factory.new CodeComparator();break;
        }

        return c;
    }

    private class CodeComparator implements Comparator<DBC>
    {
    	@Override
		public int compare(DBC a, DBC b)
        {
            CompareToBuilder ctb = new CompareToBuilder();
            ctb.append(a.getCode(),b.getCode());
            return ctb.toComparison();
        }
    }

}
