package org.jeesl.util.comparator.ejb.system.io.report;

import java.util.Comparator;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.jeesl.interfaces.model.io.report.JeeslIoReport;
import org.jeesl.interfaces.model.io.report.JeeslIoReportCategory;
import org.jeesl.interfaces.model.io.report.data.JeeslReportTemplate;
import org.jeesl.interfaces.model.io.report.style.JeeslReportStyle;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportCell;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportColumn;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportColumnGroup;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportRow;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportSheet;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportWorkbook;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.system.util.JeeslTrafficLight;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IoReportColumnComparator<L extends JeeslLang,D extends JeeslDescription,
								CATEGORY extends JeeslIoReportCategory<L,D,CATEGORY,?>,
								REPORT extends JeeslIoReport<L,D,CATEGORY,WORKBOOK>,
								WORKBOOK extends JeeslReportWorkbook<REPORT,SHEET>,
								SHEET extends JeeslReportSheet<L,D,?,WORKBOOK,GROUP,?>,
								GROUP extends JeeslReportColumnGroup<L,D,SHEET,COLUMN,?>,
								COLUMN extends JeeslReportColumn<L,D,GROUP,?,?,?,?>
								>
{
	final static Logger logger = LoggerFactory.getLogger(IoReportColumnComparator.class);

    public enum Type {position};
    
    public Comparator<COLUMN> factory(Type type)
    {
        Comparator<COLUMN> c = null;
        IoReportColumnComparator<L,D,CATEGORY,REPORT,WORKBOOK,SHEET,GROUP,COLUMN> factory = new IoReportColumnComparator<>();
        switch (type)
        {
            case position: c = factory.new PositionCodeComparator();break;
        }

        return c;
    }

    private class PositionCodeComparator implements Comparator<COLUMN>
    {
        public int compare(COLUMN a, COLUMN b)
        {
			  CompareToBuilder ctb = new CompareToBuilder();
			  ctb.append(a.getGroup().getSheet().getWorkbook().getReport().getCategory().getPosition(), b.getGroup().getSheet().getWorkbook().getReport().getCategory().getPosition());
			  ctb.append(a.getGroup().getSheet().getWorkbook().getReport().getPosition(), b.getGroup().getSheet().getWorkbook().getReport().getPosition());
			  ctb.append(a.getGroup().getSheet().getPosition(), b.getGroup().getSheet().getPosition());
			  ctb.append(a.getGroup().getPosition(), b.getGroup().getPosition());
			  ctb.append(a.getPosition(), b.getPosition());
			  return ctb.toComparison();
        }
    }
}