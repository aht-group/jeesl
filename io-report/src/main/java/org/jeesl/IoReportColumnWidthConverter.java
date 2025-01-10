package org.jeesl;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.FacesConverter;

import org.jeesl.jsf.jx.converter.AbstractEjbIdConverter;
import org.jeesl.model.ejb.io.report.column.IoReportColumnWidth;

@RequestScoped
@FacesConverter(forClass=IoReportColumnWidth.class)
public class IoReportColumnWidthConverter extends AbstractEjbIdConverter<IoReportColumnWidth>
{
	public IoReportColumnWidthConverter()
	{
		super(IoReportColumnWidth.class);
	}
}