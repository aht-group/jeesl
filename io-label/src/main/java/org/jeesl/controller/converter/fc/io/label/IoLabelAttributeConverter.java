package org.jeesl.controller.converter.fc.io.label;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.FacesConverter;

import org.jeesl.jsf.jx.converter.AbstractEjbIdConverter;
import org.jeesl.model.ejb.io.label.entity.IoLabelAttribute;

@RequestScoped
@FacesConverter(forClass=IoLabelAttribute.class)
public class IoLabelAttributeConverter extends AbstractEjbIdConverter<IoLabelAttribute>
{
	public IoLabelAttributeConverter()
	{
		super(IoLabelAttribute.class);
	}
}