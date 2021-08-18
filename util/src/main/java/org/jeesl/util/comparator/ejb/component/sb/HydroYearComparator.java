package org.jeesl.util.comparator.ejb.component.sb;

import java.util.Comparator;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.jeesl.interfaces.model.module.hydro.JeeslHydroYear;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HydroYearComparator<L extends JeeslLang,D extends JeeslDescription,
								HD extends JeeslStatus<L,D,HD>,
								HY extends JeeslHydroYear<L,D,HD,HY>>
{
    final static Logger logger = LoggerFactory.getLogger(HydroYearComparator.class);

    public enum Type {code};

    public Comparator<HY> factory(Type type)
    {
        Comparator<HY> c = null;
        HydroYearComparator<L,D,HD,HY> factory = new HydroYearComparator<>();
        switch (type)
        {
            case code: c = factory.new CodeComparator();break;
        }

        return c;
    }

    private class CodeComparator implements Comparator<HY>
    {
    	@Override
		public int compare(HY a, HY b)
        {
            CompareToBuilder ctb = new CompareToBuilder();
            ctb.append(a.getCode(),b.getCode());
            return ctb.toComparison();
        }
    }

}
