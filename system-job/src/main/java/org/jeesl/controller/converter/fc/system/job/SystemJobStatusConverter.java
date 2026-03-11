package org.jeesl.controller.converter.fc.system.job;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.FacesConverter;

import org.jeesl.jsf.jx.converter.AbstractEjbIdConverter;
import org.jeesl.model.ejb.system.job.core.SystemJobStatus;

@RequestScoped
@FacesConverter(forClass=SystemJobStatus.class)
public class SystemJobStatusConverter extends AbstractEjbIdConverter<SystemJobStatus>
{      
	public SystemJobStatusConverter()
	{
		super(SystemJobStatus.class);
	}
}  