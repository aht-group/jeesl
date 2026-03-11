package org.jeesl.controller.converter.fc.system.job;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.FacesConverter;

import org.jeesl.jsf.jx.converter.AbstractEjbIdConverter;
import org.jeesl.model.ejb.system.job.template.SystemJobType;

@RequestScoped
@FacesConverter(forClass=SystemJobType.class)
public class SystemJobTypeConverter extends AbstractEjbIdConverter<SystemJobType>
{
	public SystemJobTypeConverter()
	{
		super(SystemJobType.class);
	}
}