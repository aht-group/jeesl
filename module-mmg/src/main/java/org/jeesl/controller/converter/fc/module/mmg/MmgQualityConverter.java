package org.jeesl.controller.converter.fc.module.mmg;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.FacesConverter;

import org.jeesl.jsf.jx.converter.AbstractEjbIdConverter;
import org.jeesl.model.ejb.module.mmg.MmgQuality;

@RequestScoped
@FacesConverter(forClass=MmgQuality.class)
public class MmgQualityConverter extends AbstractEjbIdConverter<MmgQuality>
{
	public MmgQualityConverter()
	{
		super(MmgQuality.class);
	}
}
