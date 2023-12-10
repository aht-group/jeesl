package org.jeesl.controller.converter.fc.module.tafu;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.FacesConverter;

import org.jeesl.jsf.jx.converter.AbstractEjbIdConverter;
import org.jeesl.model.ejb.module.tafu.TafuStatus;

@RequestScoped
@FacesConverter(forClass=TafuStatus.class)
public class TafuStatusConverter extends AbstractEjbIdConverter<TafuStatus>
{
	public TafuStatusConverter()
	{
		super(TafuStatus.class);
	}
}