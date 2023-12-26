package org.jeesl.report.excel.aggregation;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.jeesl.interfaces.model.io.report.setting.JeeslReportSetting;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.model.xml.io.report.Info;
import org.jeesl.model.xml.module.finance.Figures;

public class JeeslAggregationsExcelExporter
{
    public static <L extends JeeslLang,D extends JeeslDescription,TYPE extends JeeslStatus<L,D,TYPE>>
    	InputStream export(TYPE filling, Info info, Figures figures) throws IOException
    {
    	switch(JeeslReportSetting.Filling.valueOf(filling.getCode()))
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