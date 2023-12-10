package org.jeesl.controller.converter.fc.system.security;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.FacesConverter;

import org.jeesl.jsf.jx.converter.AbstractEjbIdConverter;
import org.jeesl.model.ejb.system.security.access.SecurityRole;

@RequestScoped
@FacesConverter(forClass=SecurityRole.class)
public class SecurityRoleConverter extends AbstractEjbIdConverter<SecurityRole>
{      
	public SecurityRoleConverter()
	{
		super(SecurityRole.class);
	}
}  