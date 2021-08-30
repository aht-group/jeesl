package org.jeesl.util.comparator.ejb.system.io.report;

import java.util.Comparator;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.jeesl.interfaces.model.io.report.JeeslIoReport;
import org.jeesl.interfaces.model.io.report.JeeslIoReportCategory;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportSheet;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IoReportSheetComparator<
								CATEGORY extends JeeslIoReportCategory<?,?,CATEGORY,?>,
								REPORT extends JeeslIoReport<?,?,CATEGORY,WORKBOOK>,
								
								WORKBOOK extends JeeslReportWorkbook<REPORT,SHEET>,
								SHEET extends JeeslReportSheet<?,?,?,WORKBOOK,?,?>
								>
{
	final static Logger logger = LoggerFactory.getLogger(IoReportSheetComparator.class);

    public enum Type {position};
    
    public Comparator<SHEET> factory(Type type)
    {
        Comparator<SHEET> c = null;
        IoReportSheetComparator<CATEGORY,REPORT,WORKBOOK,SHEET> factory = new IoReportSheetComparator<>();
        switch (type)
        {
            case position: c = factory.new PositionCodeComparator();break;
        }

        return c;
    }

    private class PositionCodeComparator implements Comparator<SHEET>
    {
        public int compare(SHEET a, SHEET b)
        {
			  CompareToBuilder ctb = new CompareToBuilder();
			  ctb.append(a.getWorkbook().getReport().getCategory().getPosition(), b.getWorkbook().getReport().getCategory().getPosition());
			  ctb.append(a.getWorkbook().getReport().getPosition(), b.getWorkbook().getReport().getPosition());
			  ctb.append(a.getPosition(), b.getPosition());
			  return ctb.toComparison();
        }
    }
}