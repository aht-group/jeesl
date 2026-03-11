package org.jeesl.controller.converter.fc.system.job;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.FacesConverter;

import org.jeesl.jsf.jx.converter.AbstractEjbIdConverter;
import org.jeesl.model.ejb.system.job.template.SystemJobCategory;

@RequestScoped
@FacesConverter(forClass=SystemJobCategory.class)
public class SystemJobCategoryConverter extends AbstractEjbIdConverter<SystemJobCategory>
{
	public SystemJobCategoryConverter()
	{
		super(SystemJobCategory.class);
	}
}