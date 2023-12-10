package org.jeesl.controller.converter.forclass.system.constraint;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.FacesConverter;

import org.jeesl.jsf.jx.converter.AbstractEjbIdConverter;
import org.jeesl.model.ejb.system.constraint.core.SystemConstraintType;

@RequestScoped
@FacesConverter(forClass=SystemConstraintType.class)
public class ConstraintTypeConverter extends AbstractEjbIdConverter<SystemConstraintType>
{
	public ConstraintTypeConverter()
	{
		super(SystemConstraintType.class);
	}
}  