package org.jeesl.controller.converter.fc.module.cal.unit;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.FacesConverter;

import org.jeesl.jsf.converter.AbstractEjbIdConverter;
import org.jeesl.model.ejb.module.cal.unit.CalYear;

@RequestScoped
@FacesConverter(forClass=CalYear.class)
public class CalYearConverter extends AbstractEjbIdConverter<CalYear>
{      
	public CalYearConverter()
	{
		super(CalYear.class);
	}
}  