package org.jeesl.controller.converter.fc.io.report;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.FacesConverter;

import org.jeesl.jsf.jx.converter.AbstractEjbIdConverter;
import org.jeesl.model.ejb.io.report.core.IoReportCategory;

@RequestScoped
@FacesConverter(forClass=IoReportCategory.class)
public class IoReportCategoryConverter extends AbstractEjbIdConverter<IoReportCategory>
{
	public IoReportCategoryConverter()
	{
		super(IoReportCategory.class);
	}
}