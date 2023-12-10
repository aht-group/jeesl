package org.jeesl.controller.converter.fc.system.security;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.FacesConverter;

import org.jeesl.jsf.jx.converter.AbstractEjbIdConverter;
import org.jeesl.model.ejb.system.security.SecurityCategory;

@RequestScoped
@FacesConverter(forClass=SecurityCategory.class)
public class SecurityCategoryConverter extends AbstractEjbIdConverter<SecurityCategory>
{      
	public SecurityCategoryConverter()
	{
		super(SecurityCategory.class);
	}
}  