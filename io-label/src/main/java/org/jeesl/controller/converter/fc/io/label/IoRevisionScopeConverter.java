package org.jeesl.controller.converter.fc.io.label;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.FacesConverter;

import org.jeesl.jsf.converter.AbstractEjbIdConverter;
import org.jeesl.model.ejb.io.label.revision.IoRevisionScope;

@RequestScoped
@FacesConverter(forClass=IoRevisionScope.class)
public class IoRevisionScopeConverter extends AbstractEjbIdConverter<IoRevisionScope>
{
	public IoRevisionScopeConverter()
	{
		super(IoRevisionScope.class);
	}
}