package org.jeesl.controller.converter.fc.io.report;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.FacesConverter;

import org.jeesl.jsf.jx.converter.AbstractEjbIdConverter;
import org.jeesl.model.ejb.io.report.column.IoReportCellType;

@RequestScoped
@FacesConverter(forClass=IoReportCellType.class)
public class IoReportCellTypeConverter extends AbstractEjbIdConverter<IoReportCellType>
{
	public IoReportCellTypeConverter()
	{
		super(IoReportCellType.class);
	}
}