package org.jeesl.controller.converter.fc.io.label;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.FacesConverter;

import org.jeesl.jsf.jx.converter.AbstractEjbIdConverter;
import org.jeesl.model.ejb.io.label.revision.IoRevisionScopeType;

@RequestScoped
@FacesConverter(forClass=IoRevisionScopeType.class)
public class IoRevisionScopeTypeConverter extends AbstractEjbIdConverter<IoRevisionScopeType>
{
	public IoRevisionScopeTypeConverter()
	{
		super(IoRevisionScopeType.class);
	}
}