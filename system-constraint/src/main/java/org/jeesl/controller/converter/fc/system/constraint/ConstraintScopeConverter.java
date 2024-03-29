package org.jeesl.controller.converter.fc.system.constraint;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.FacesConverter;

import org.jeesl.jsf.jx.converter.AbstractEjbIdConverter;
import org.jeesl.model.ejb.system.constraint.core.SystemConstraintScope;

@RequestScoped
@FacesConverter(forClass=SystemConstraintScope.class)
public class ConstraintScopeConverter extends AbstractEjbIdConverter<SystemConstraintScope>
{
	public ConstraintScopeConverter()
	{
		super(SystemConstraintScope.class);
	}
}  