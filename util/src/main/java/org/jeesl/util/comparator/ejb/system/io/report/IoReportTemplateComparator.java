package org.jeesl.util.comparator.ejb.system.io.report;

import java.util.Comparator;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.jeesl.interfaces.model.io.report.row.JeeslReportTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IoReportTemplateComparator<TEMPLATE extends JeeslReportTemplate<?,?,?>>
{
	final static Logger logger = LoggerFactory.getLogger(IoReportTemplateComparator.class);

    public enum Type {position};
    
    public Comparator<TEMPLATE> factory(Type type)
    {
        Comparator<TEMPLATE> c = null;
        IoReportTemplateComparator<TEMPLATE> factory = new IoReportTemplateComparator<>();
        switch (type)
        {
            case position: c = factory.new PositionCodeComparator();break;
        }

        return c;
    }

    private class PositionCodeComparator implements Comparator<TEMPLATE>
    {
        public int compare(TEMPLATE a, TEMPLATE b)
        {
			  CompareToBuilder ctb = new CompareToBuilder();
			  ctb.append(a.getPosition(), b.getPosition());
			  return ctb.toComparison();
        }
    }
}