package org.jeesl.controller.converter.fc.system.constraint;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.FacesConverter;

import org.jeesl.jsf.jx.converter.AbstractEjbIdConverter;
import org.jeesl.model.ejb.system.constraint.algorithm.SystemConstraintAlgorithmGroup;

@RequestScoped
@FacesConverter(forClass=SystemConstraintAlgorithmGroup.class)
public class ConstraintAlgorithmGroupConverter extends AbstractEjbIdConverter<SystemConstraintAlgorithmGroup>
{
	public ConstraintAlgorithmGroupConverter()
	{
		super(SystemConstraintAlgorithmGroup.class);
	}
}  