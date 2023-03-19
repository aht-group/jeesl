package org.jeesl.controller.converter.forclass.system.constraint;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.FacesConverter;

import org.jeesl.jsf.converter.AbstractEjbIdConverter;
import org.jeesl.model.ejb.system.constraint.core.SystemConstraintLevel;

@RequestScoped
@FacesConverter(forClass=SystemConstraintLevel.class)
public class ConstraintLevelConverter extends AbstractEjbIdConverter<SystemConstraintLevel>
{
	public ConstraintLevelConverter()
	{
		super(SystemConstraintLevel.class);
	}
}  