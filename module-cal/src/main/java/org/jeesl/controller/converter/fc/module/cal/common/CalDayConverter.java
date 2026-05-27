package org.jeesl.controller.converter.fc.module.cal.common;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.FacesConverter;

import org.jeesl.jsf.jx.converter.AbstractEjbIdConverter;
import org.jeesl.model.ejb.module.cal.common.CalDay;

@RequestScoped
@FacesConverter(forClass=CalDay.class)
public class CalDayConverter extends AbstractEjbIdConverter<CalDay>
{      
	public CalDayConverter()
	{
		super(CalDay.class);
	}
}  