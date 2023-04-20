package org.jeesl.controller.converter.fc.io.label;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.FacesConverter;

import org.jeesl.jsf.converter.AbstractEjbIdConverter;
import org.jeesl.model.ejb.io.label.entity.IoLabelAttributeType;

@RequestScoped
@FacesConverter(forClass=IoLabelAttributeType.class)
public class IoLabelAttributeTypeConverter extends AbstractEjbIdConverter<IoLabelAttributeType>
{
	public IoLabelAttributeTypeConverter()
	{
		super(IoLabelAttributeType.class);
	}
}