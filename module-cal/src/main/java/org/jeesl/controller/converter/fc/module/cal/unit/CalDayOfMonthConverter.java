package org.jeesl.controller.converter.fc.module.cal.unit;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.FacesConverter;

import org.jeesl.jsf.jx.converter.AbstractEjbIdConverter;
import org.jeesl.model.ejb.module.cal.unit.CalDayOfMonth;

@RequestScoped
@FacesConverter(forClass=CalDayOfMonth.class)
public class CalDayOfMonthConverter extends AbstractEjbIdConverter<CalDayOfMonth>
{      
	public CalDayOfMonthConverter()
	{
		super(CalDayOfMonth.class);
	}
}  