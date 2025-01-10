package org.jeesl.controller.converter.fc.module.cal.unit;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.FacesConverter;

import org.jeesl.jsf.jx.converter.AbstractEjbIdConverter;
import org.jeesl.model.ejb.module.cal.unit.CalWeekOfYear;

@RequestScoped
@FacesConverter(forClass=CalWeekOfYear.class)
public class CalWeekOfYearConverter extends AbstractEjbIdConverter<CalWeekOfYear>
{      
	public CalWeekOfYearConverter()
	{
		super(CalWeekOfYear.class);
	}
}  