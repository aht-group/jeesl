package org.jeesl.controller.converter.fc.module.aom;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.FacesConverter;

import org.jeesl.jsf.converter.AbstractEjbIdConverter;
import org.jeesl.model.ejb.module.aom.event.AomEventStatus;

@RequestScoped
@FacesConverter(forClass=AomEventStatus.class)
public class AomEventStatusConverter extends AbstractEjbIdConverter<AomEventStatus>
{      
	public AomEventStatusConverter()
	{
		super(AomEventStatus.class);
	}
}  