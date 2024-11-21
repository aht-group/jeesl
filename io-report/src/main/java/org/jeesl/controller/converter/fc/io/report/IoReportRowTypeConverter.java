package org.jeesl.controller.converter.fc.io.report;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.FacesConverter;

import org.jeesl.jsf.jx.converter.AbstractEjbIdConverter;
import org.jeesl.model.ejb.io.report.row.IoReportRowType;

@RequestScoped
@FacesConverter(forClass=IoReportRowType.class)
public class IoReportRowTypeConverter extends AbstractEjbIdConverter<IoReportRowType>
{
	public IoReportRowTypeConverter()
	{
		super(IoReportRowType.class);
	}
}