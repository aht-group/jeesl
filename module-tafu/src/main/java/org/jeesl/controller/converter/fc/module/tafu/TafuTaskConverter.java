package org.jeesl.controller.converter.fc.module.tafu;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.FacesConverter;

import org.jeesl.jsf.jx.converter.AbstractEjbIdConverter;
import org.jeesl.model.ejb.module.tafu.TafuTask;

@RequestScoped
@FacesConverter(forClass=TafuTask.class)
public class TafuTaskConverter extends AbstractEjbIdConverter<TafuTask>
{
	public TafuTaskConverter()
	{
		super(TafuTask.class);
	}
}