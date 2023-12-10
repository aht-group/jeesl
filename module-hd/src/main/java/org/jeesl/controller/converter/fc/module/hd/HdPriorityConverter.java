package org.jeesl.controller.converter.fc.module.hd;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.FacesConverter;

import org.jeesl.jsf.jx.converter.AbstractEjbIdConverter;
import org.jeesl.model.ejb.module.hd.resolution.HdPriority;

@RequestScoped
@FacesConverter(forClass=HdPriority.class)
public class HdPriorityConverter extends AbstractEjbIdConverter<HdPriority>
{      
	public HdPriorityConverter()
	{
		super(HdPriority.class);
	}
}  