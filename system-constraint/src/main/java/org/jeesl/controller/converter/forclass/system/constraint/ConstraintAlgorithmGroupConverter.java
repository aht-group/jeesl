package org.jeesl.controller.converter.forclass.system.constraint;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.FacesConverter;

import org.jeesl.jsf.converter.AbstractEjbIdConverter;
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