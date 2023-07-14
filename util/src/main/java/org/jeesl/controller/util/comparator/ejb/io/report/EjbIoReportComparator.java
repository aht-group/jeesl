package org.jeesl.controller.util.comparator.ejb.io.report;

import java.util.Comparator;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.jeesl.interfaces.model.io.report.JeeslIoReport;
import org.jeesl.interfaces.model.io.report.JeeslIoReportCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbIoReportComparator<CATEGORY extends JeeslIoReportCategory<?,?,CATEGORY,?>,
								REPORT extends JeeslIoReport<?,?,CATEGORY,?>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbIoReportComparator.class);

    public enum Type {position};

    public EjbIoReportComparator()
    {
    	
    }
    
    public Comparator<REPORT> instance(Type type)
    {
        Comparator<REPORT> c = null;
        EjbIoReportComparator<CATEGORY,REPORT> factory = new EjbIoReportComparator<>();
        switch (type)
        {
            case position: c = factory.new PositionCodeComparator();break;
        }

        return c;
    }

    private class PositionCodeComparator implements Comparator<REPORT>
    {
        @Override public int compare(REPORT a, REPORT b)
        {
			  CompareToBuilder ctb = new CompareToBuilder();
			  ctb.append(a.getCategory().getPosition(), b.getCategory().getPosition());
			  ctb.append(a.getPosition(), b.getPosition());
			  ctb.append(a.getId(),b.getId());
			  return ctb.toComparison();
        }
    }
}