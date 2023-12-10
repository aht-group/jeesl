package org.jeesl.controller.converter.fc.module.cal.unit;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.FacesConverter;

import org.jeesl.jsf.jx.converter.AbstractEjbIdConverter;
import org.jeesl.model.ejb.module.cal.unit.CalMonth;

@RequestScoped
@FacesConverter(forClass=CalMonth.class)
public class CalMonthConverter extends AbstractEjbIdConverter<CalMonth>
{      
	public CalMonthConverter()
	{
		super(CalMonth.class);
	}
}  