package org.jeesl.controller.converter.fc.system.job;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.FacesConverter;

import org.jeesl.jsf.jx.converter.AbstractEjbIdConverter;
import org.jeesl.model.ejb.system.job.core.SystemJobPriority;

@RequestScoped
@FacesConverter(forClass=SystemJobPriority.class)
public class SystemJobPriorityConverter extends AbstractEjbIdConverter<SystemJobPriority>
{
	public SystemJobPriorityConverter()
	{
		super(SystemJobPriority.class);
	}
}