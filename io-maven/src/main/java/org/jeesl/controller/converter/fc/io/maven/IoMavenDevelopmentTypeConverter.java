package org.jeesl.controller.converter.fc.io.maven;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.FacesConverter;

import org.jeesl.jsf.converter.AbstractEjbIdConverter;
import org.jeesl.model.ejb.io.maven.classification.IoMavenDevelopmentType;

@RequestScoped
@FacesConverter(forClass=IoMavenDevelopmentType.class)
public class IoMavenDevelopmentTypeConverter extends AbstractEjbIdConverter<IoMavenDevelopmentType>
{
	public IoMavenDevelopmentTypeConverter()
	{
		super(IoMavenDevelopmentType.class);
	}
}  