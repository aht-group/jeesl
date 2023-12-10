package org.jeesl.controller.converter.fc.module.tafu;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.FacesConverter;

import org.jeesl.jsf.jx.converter.AbstractEjbIdConverter;
import org.jeesl.model.ejb.module.tafu.TafuScope;

@RequestScoped
@FacesConverter(forClass=TafuScope.class)
public class TafuScopeConverter extends AbstractEjbIdConverter<TafuScope>
{
	public TafuScopeConverter()
	{
		super(TafuScope.class);
	}
}