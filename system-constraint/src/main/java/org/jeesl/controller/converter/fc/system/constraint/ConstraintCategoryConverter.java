package org.jeesl.controller.converter.fc.system.constraint;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.FacesConverter;

import org.jeesl.jsf.jx.converter.AbstractEjbIdConverter;
import org.jeesl.model.ejb.system.constraint.core.SystemConstraintCategory;

@RequestScoped
@FacesConverter(forClass=SystemConstraintCategory.class)
public class ConstraintCategoryConverter extends AbstractEjbIdConverter<SystemConstraintCategory>
{
	public ConstraintCategoryConverter()
	{
		super(SystemConstraintCategory.class);
	}
}  