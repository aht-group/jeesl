package org.jeesl.controller.converter.fc.module.cal.unit;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.FacesConverter;

import org.jeesl.jsf.jx.converter.AbstractEjbIdConverter;
import org.jeesl.model.ejb.module.cal.unit.CalWeekOfMonth;

@RequestScoped
@FacesConverter(forClass=CalWeekOfMonth.class)
public class CalWeekOfMonthConverter extends AbstractEjbIdConverter<CalWeekOfMonth>
{      
	public CalWeekOfMonthConverter()
	{
		super(CalWeekOfMonth.class);
	}
}  