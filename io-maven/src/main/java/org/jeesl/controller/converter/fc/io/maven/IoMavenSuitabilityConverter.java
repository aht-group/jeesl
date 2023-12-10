package org.jeesl.controller.converter.fc.io.maven;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.FacesConverter;

import org.jeesl.jsf.jx.converter.AbstractEjbIdConverter;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenSuitability;

@RequestScoped
@FacesConverter(forClass=IoMavenSuitability.class)
public class IoMavenSuitabilityConverter extends AbstractEjbIdConverter<IoMavenSuitability>
{
	public IoMavenSuitabilityConverter()
	{
		super(IoMavenSuitability.class);
	}
}  