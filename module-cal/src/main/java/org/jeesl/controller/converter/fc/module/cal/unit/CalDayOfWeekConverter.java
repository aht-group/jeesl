package org.jeesl.controller.converter.fc.module.cal.unit;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.FacesConverter;

import org.jeesl.jsf.converter.AbstractEjbIdConverter;
import org.jeesl.model.ejb.module.cal.unit.CalDayOfWeek;

@RequestScoped
@FacesConverter(forClass=CalDayOfWeek.class)
public class CalDayOfWeekConverter extends AbstractEjbIdConverter<CalDayOfWeek>
{      
	public CalDayOfWeekConverter()
	{
		super(CalDayOfWeek.class);
	}
}  