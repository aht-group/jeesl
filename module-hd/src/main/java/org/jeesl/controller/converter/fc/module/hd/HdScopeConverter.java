package org.jeesl.controller.converter.fc.module.hd;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.FacesConverter;

import org.jeesl.jsf.jx.converter.AbstractEjbIdConverter;
import org.jeesl.model.ejb.module.hd.HdScope;

@RequestScoped
@FacesConverter(forClass=HdScope.class)
public class HdScopeConverter extends AbstractEjbIdConverter<HdScope>
{      
	public HdScopeConverter()
	{
		super(HdScope.class);
	}
}  