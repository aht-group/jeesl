package org.jeesl.controller.converter.fc.system.job;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.FacesConverter;

import org.jeesl.jsf.jx.converter.AbstractEjbIdConverter;
import org.jeesl.model.ejb.system.job.maintenance.SystemJobMaintenance;

@RequestScoped
@FacesConverter(forClass=SystemJobMaintenance.class)
public class SystemJobMaintenanceConverter extends AbstractEjbIdConverter<SystemJobMaintenance>
{      
	public SystemJobMaintenanceConverter()
	{
		super(SystemJobMaintenance.class);
	}
}  