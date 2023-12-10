package org.jeesl.controller.converter.fc.system.property;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.FacesConverter;

import org.jeesl.jsf.jx.converter.AbstractEjbIdConverter;
import org.jeesl.model.ejb.system.property.SystemPropertyCategory;

@RequestScoped
@FacesConverter(forClass=SystemPropertyCategory.class)
public class SystemPropertyCategoryConverter extends AbstractEjbIdConverter<SystemPropertyCategory>
{
	public SystemPropertyCategoryConverter()
	{
		super(SystemPropertyCategory.class);
	}
}  