package org.jeesl.controller.converter.forclass.io.label;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.FacesConverter;

import org.jeesl.jsf.converter.AbstractEjbIdConverter;
import org.jeesl.model.ejb.io.label.revision.IoRevisionEntityMapping;

@RequestScoped
@FacesConverter(forClass=IoRevisionEntityMapping.class)
public class IoRevisionEntityMappingConverter extends AbstractEjbIdConverter<IoRevisionEntityMapping>
{
	public IoRevisionEntityMappingConverter()
	{
		super(IoRevisionEntityMapping.class);
	}
}