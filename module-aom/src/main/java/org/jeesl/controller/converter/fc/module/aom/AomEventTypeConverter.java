package org.jeesl.controller.converter.fc.module.aom;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.FacesConverter;

import org.jeesl.jsf.converter.AbstractEjbIdConverter;
import org.jeesl.model.ejb.module.aom.event.AomEventType;

@RequestScoped
@FacesConverter(forClass=AomEventType.class)
public class AomEventTypeConverter extends AbstractEjbIdConverter<AomEventType>
{      
	public AomEventTypeConverter()
	{
		super(AomEventType.class);
	}
}  