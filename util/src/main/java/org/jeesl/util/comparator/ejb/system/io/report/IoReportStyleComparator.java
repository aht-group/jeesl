package org.jeesl.util.comparator.ejb.system.io.report;

import java.util.Comparator;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.jeesl.interfaces.model.io.report.style.JeeslReportStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IoReportStyleComparator<STYLE extends JeeslReportStyle<?,?,?>>
{
	final static Logger logger = LoggerFactory.getLogger(IoReportStyleComparator.class);

    public enum Type {position};
    
    public Comparator<STYLE> factory(Type type)
    {
        Comparator<STYLE> c = null;
        IoReportStyleComparator<STYLE> factory = new IoReportStyleComparator<>();
        switch (type)
        {
            case position: c = factory.new PositionCodeComparator();break;
        }

        return c;
    }

    private class PositionCodeComparator implements Comparator<STYLE>
    {
        public int compare(STYLE a, STYLE b)
        {
			  CompareToBuilder ctb = new CompareToBuilder();
			  ctb.append(a.getPosition(), b.getPosition());
			  return ctb.toComparison();
        }
    }
}