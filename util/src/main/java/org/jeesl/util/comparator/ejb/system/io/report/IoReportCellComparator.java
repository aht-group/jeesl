package org.jeesl.util.comparator.ejb.system.io.report;

import java.util.Comparator;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.jeesl.interfaces.model.io.report.row.JeeslReportTemplate;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportCell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IoReportCellComparator<TEMPLATE extends JeeslReportTemplate<?,?,CELL>,
								CELL extends JeeslReportCell<?,?,TEMPLATE>
								>
{
	final static Logger logger = LoggerFactory.getLogger(IoReportCellComparator.class);

    public enum Type {position};
    
    public Comparator<CELL> factory(Type type)
    {
        Comparator<CELL> c = null;
        IoReportCellComparator<TEMPLATE,CELL> factory = new IoReportCellComparator<>();
        switch (type)
        {
            case position: c = factory.new PositionCodeComparator();break;
        }

        return c;
    }

    private class PositionCodeComparator implements Comparator<CELL>
    {
        public int compare(CELL a, CELL b)
        {
			  CompareToBuilder ctb = new CompareToBuilder();
			  ctb.append(a.getTemplate().getPosition(), b.getTemplate().getPosition());
			  ctb.append(a.getRowNr(), b.getRowNr());
			  ctb.append(a.getColNr(), b.getColNr());
			  return ctb.toComparison();
        }
    }
}