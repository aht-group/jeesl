package org.jeesl.controller.converter.fc.module.hd;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.FacesConverter;

import org.jeesl.jsf.converter.AbstractEjbIdConverter;
import org.jeesl.model.ejb.module.hd.resolution.HdLevel;

@RequestScoped
@FacesConverter(forClass=HdLevel.class)
public class HdLevelConverter extends AbstractEjbIdConverter<HdLevel>
{      
	public HdLevelConverter()
	{
		super(HdLevel.class);
	}
}  