package org.jeesl.report.excel.aggregation;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.jeesl.interfaces.model.system.io.report.JeeslReportSettingFilling;

import net.sf.ahtutils.interfaces.model.status.UtilsDescription;
import net.sf.ahtutils.interfaces.model.status.UtilsLang;
import net.sf.ahtutils.interfaces.model.status.UtilsStatus;
import net.sf.ahtutils.xml.finance.Figures;
import net.sf.ahtutils.xml.report.Info;

public class JeeslAggregationsExcelExporter
{
    public static <L extends UtilsLang,D extends UtilsDescription,TYPE extends UtilsStatus<TYPE,L,D>>
    	InputStream export(TYPE filling, Info info, Figures figures) throws IOException
    {
    	switch(JeeslReportSettingFilling.Code.valueOf(filling.getCode()))
    	{
    		case flat:			return flat(info,figures);
    		case hierarchical:	return hierarchical(info,figures);
    		default:			return null;
    	}
    }
    
    public static InputStream export(Info info, Figures figures) throws IOException
	{
		return hierarchical(info,figures);
	}
    
    private static InputStream flat(Info info, Figures figures) throws IOException
    {
    	JeeslAggregationsFlatExporter exporter = new JeeslAggregationsFlatExporter(info, figures);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		exporter.getWb().write(baos);
		return new ByteArrayInputStream(baos.toByteArray());
		
    }
    
    private static InputStream hierarchical(Info info, Figures figures) throws IOException
    {
    	JeeslAggregationsHierachicalExporter exporter = new JeeslAggregationsHierachicalExporter(info, figures);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		exporter.getWb().write(baos);
		return new ByteArrayInputStream(baos.toByteArray());
    }
}