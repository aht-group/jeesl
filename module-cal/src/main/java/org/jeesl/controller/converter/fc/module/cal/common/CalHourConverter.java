package org.jeesl.controller.converter.fc.module.cal.common;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.FacesConverter;

import org.jeesl.jsf.jx.converter.AbstractEjbIdConverter;
import org.jeesl.model.ejb.module.cal.common.CalHour;

@RequestScoped
@FacesConverter(forClass=CalHour.class)
public class CalHourConverter extends AbstractEjbIdConverter<CalHour>
{      
	public CalHourConverter()
	{
		super(CalHour.class);
	}
}  