package org.jeesl.controller.converter.fc.io.label;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.FacesConverter;

import org.jeesl.jsf.converter.AbstractEjbIdConverter;
import org.jeesl.model.ejb.io.label.entity.IoLabelEntity;

@RequestScoped
@FacesConverter(forClass=IoLabelEntity.class)
public class IoLabelEntityConverter extends AbstractEjbIdConverter<IoLabelEntity>
{
	public IoLabelEntityConverter()
	{
		super(IoLabelEntity.class);
	}
}