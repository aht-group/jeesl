package org.jeesl.util.comparator.ejb.system.io.report;

import java.util.Comparator;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.jeesl.interfaces.model.io.report.JeeslIoReport;
import org.jeesl.interfaces.model.io.report.JeeslIoReportCategory;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportColumn;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportColumnGroup;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportRow;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportSheet;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportWorkbook;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IoReportRowComparator<L extends JeeslLang,D extends JeeslDescription,
								CATEGORY extends JeeslIoReportCategory<L,D,CATEGORY,?>,
								REPORT extends JeeslIoReport<L,D,CATEGORY,WORKBOOK>,
								WORKBOOK extends JeeslReportWorkbook<REPORT,SHEET>,
								SHEET extends JeeslReportSheet<L,D,?,WORKBOOK,GROUP,ROW>,
								GROUP extends JeeslReportColumnGroup<L,D,SHEET,COLUMN,?>,
								COLUMN extends JeeslReportColumn<L,D,GROUP,?,?,?,?>,
								ROW extends JeeslReportRow<L,D,SHEET,?,?,?>
								>
{
	final static Logger logger = LoggerFactory.getLogger(IoReportRowComparator.class);

    public enum Type {position};
    
    public Comparator<ROW> factory(Type type)
    {
        Comparator<ROW> c = null;
        IoReportRowComparator<L,D,CATEGORY,REPORT,WORKBOOK,SHEET,GROUP,COLUMN,ROW> factory = new IoReportRowComparator<>();
        switch (type)
        {
            case position: c = factory.new PositionCodeComparator();break;
        }

        return c;
    }

    private class PositionCodeComparator implements Comparator<ROW>
    {
        public int compare(ROW a, ROW b)
        {
			  CompareToBuilder ctb = new CompareToBuilder();
			  ctb.append(a.getSheet().getWorkbook().getReport().getCategory().getPosition(), b.getSheet().getWorkbook().getReport().getCategory().getPosition());
			  ctb.append(a.getSheet().getWorkbook().getReport().getPosition(), b.getSheet().getWorkbook().getReport().getPosition());
			  ctb.append(a.getSheet().getPosition(), b.getSheet().getPosition());
			  ctb.append(a.getPosition(), b.getPosition());
			  return ctb.toComparison();
        }
    }
}